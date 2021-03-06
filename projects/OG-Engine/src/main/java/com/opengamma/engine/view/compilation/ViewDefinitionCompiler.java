/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view.compilation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Instant;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.position.Portfolio;
import com.opengamma.core.position.Position;
import com.opengamma.core.position.impl.PortfolioNodeTraverser;
import com.opengamma.core.security.Security;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.depgraph.DependencyGraph;
import com.opengamma.engine.depgraph.DependencyGraphBuilder;
import com.opengamma.engine.depgraph.DependencyNode;
import com.opengamma.engine.depgraph.DependencyNodeFormatter;
import com.opengamma.engine.depgraph.Housekeeper;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.target.ComputationTargetReference;
import com.opengamma.engine.target.ComputationTargetType;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.engine.view.ResultModelDefinition;
import com.opengamma.engine.view.ResultOutputMode;
import com.opengamma.engine.view.ViewCalculationConfiguration;
import com.opengamma.engine.view.ViewDefinition;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.tuple.Pair;

/**
 * Ultimately produces a set of {@link DependencyGraph}s from a {@link ViewDefinition}, one for each {@link ViewCalculationConfiguration}. Additional information, such as the live data requirements,
 * is collected along the way and exposed after compilation.
 * <p>
 * The compiled graphs are guaranteed to be calculable for at least the requested timestamp. One or more of the referenced functions may not be valid at other timestamps.
 */
public final class ViewDefinitionCompiler {

  private static final Logger s_logger = LoggerFactory.getLogger(ViewDefinitionCompiler.class);
  private static final boolean OUTPUT_DEPENDENCY_GRAPHS = false;
  private static final boolean OUTPUT_LIVE_DATA_REQUIREMENTS = false;
  private static final boolean OUTPUT_FAILURE_REPORTS = false;
  private static boolean s_striped;
  private static Timer s_fullTimer = new Timer(); // timer for full graph compilation (replaced if registerMetrics called)
  private static Timer s_deltaTimer = new Timer(); // timer for delta graph compilation (replaced if registerMetrics called)

  private static final Supplier<String> s_uniqueIdentifiers = new Supplier<String>() {

    private final String _prefix = UUID.randomUUID().toString() + "-";
    private final AtomicLong _number = new AtomicLong();

    @Override
    public String get() {
      return _prefix + Long.toString(_number.incrementAndGet());
    }

  };

  private ViewDefinitionCompiler() {
  }

  public static void registerMetricsStatic(MetricRegistry summaryRegistry, MetricRegistry detailRegistry, String namePrefix) {
    s_deltaTimer = summaryRegistry.timer(namePrefix + ".delta");
    s_fullTimer = summaryRegistry.timer(namePrefix + ".full");
  }

  //-------------------------------------------------------------------------

  /**
   * Compiles the specified view definition wrt the supplied compilation context, valuation time and version correction and returns the compiled view. This method wraps the compileTask method, waiting
   * for completion of the async compilation task and returning the resulting CompiledViewDefinitionWithGraphsImpl, rather than a future reference to it.
   * 
   * @param viewDefinition the view definition to compile
   * @param compilationServices the compilation context (market data availability provider, graph builder factory, etc.)
   * @param valuationTime the effective valuation time against which to compile
   * @param versionCorrection the version correction to use
   * @return the CompiledViewDefinitionWithGraphsImpl that results from the compilation
   */
  protected static final class CompilationCompletionEstimate implements Housekeeper.Callback<Supplier<Double>> {

    private final String _label;
    private final ConcurrentMap<String, Double> _buildEstimates;

    private CompilationCompletionEstimate(final ViewCompilationContext context) {
      final Collection<DependencyGraphBuilder> builders = context.getBuilders();
      _buildEstimates = new ConcurrentHashMap<String, Double>();
      for (final DependencyGraphBuilder builder : builders) {
        _buildEstimates.put(builder.getCalculationConfigurationName(), 0d);
        Housekeeper.of(builder, this, builder.buildFractionEstimate()).start();
      }
      _label = context.getViewDefinition().getName();
    }

    public double[] estimates() {
      final double[] result = new double[_buildEstimates.size()];
      int i = 0;
      for (final Double estimate : _buildEstimates.values()) {
        result[i++] = estimate;
      }
      return result;
    }

    public double estimate() {
      double result = 0;
      for (final Double estimate : _buildEstimates.values()) {
        result += estimate;
      }
      return result / _buildEstimates.size();
    }

    @Override
    public boolean tick(final DependencyGraphBuilder builder, final Supplier<Double> estimate) {
      final Double estimateValue = estimate.get();
      s_logger.debug("{}/{} building at {}", new Object[] {_label, builder.getCalculationConfigurationName(), estimateValue });
      _buildEstimates.put(builder.getCalculationConfigurationName(), estimateValue);
      return estimateValue < 1d;
    }

    @Override
    public boolean cancelled(final DependencyGraphBuilder builder, final Supplier<Double> estimate) {
      return false;
    }

    @Override
    public boolean completed(final DependencyGraphBuilder builder, final Supplier<Double> estimate) {
      return estimate.get() < 1d;
    }
  }

  // TODO: return something that provides the caller with access to a completion metric to feedback to any interactive user
  private abstract static class CompilationTask implements Future<CompiledViewDefinitionWithGraphsImpl> {

    private final ViewCompilationContext _viewCompilationContext;
    private volatile CompiledViewDefinitionWithGraphsImpl _result;
    private boolean _portfolioOutputs;
    private Portfolio _portfolio;

    protected CompilationTask(final ViewCompilationContext context) {
      _viewCompilationContext = context;
      if (s_logger.isDebugEnabled()) {
        new CompilationCompletionEstimate(_viewCompilationContext);
      }
      final ResultModelDefinition resultModelDefinition = context.getViewDefinition().getResultModelDefinition();
      _portfolioOutputs = (resultModelDefinition.getPositionOutputMode() != ResultOutputMode.NONE) || (resultModelDefinition.getAggregatePositionOutputMode() != ResultOutputMode.NONE);
    }

    protected ViewCompilationContext getContext() {
      return _viewCompilationContext;
    }

    protected abstract void compile(DependencyGraphBuilder builder);

    protected void compile() {
      final Iterator<DependencyGraphBuilder> builders = getContext().getBuilders().iterator();
      while (builders.hasNext()) {
        final DependencyGraphBuilder builder = builders.next();
        compile(builder);
        // TODO: Use a heuristic to decide whether to let the graph builds run in parallel, or sequentially. We will force sequential builds for the time being.
        // Wait for the current config's dependency graph to be built before moving to the next view calc config
        final DependencyGraph graph = builder.getDependencyGraph();
        graph.removeUnnecessaryValues();
        getContext().getGraphs().add(graph);
        builders.remove();
        s_logger.debug("Built {}", graph);
      }
    }

    private void removeUnusedResolutions(final Collection<DependencyGraph> graphs) {
      final Set<UniqueId> validIdentifiers = new HashSet<UniqueId>(getContext().getActiveResolutions().size());
      if (_portfolio != null) {
        validIdentifiers.add(_portfolio.getUniqueId());
      }
      for (DependencyGraph graph : graphs) {
        for (final ComputationTargetSpecification target : graph.getAllComputationTargets()) {
          validIdentifiers.add(target.getUniqueId());
        }
      }
      final Iterator<Map.Entry<ComputationTargetReference, UniqueId>> itrResolutions = getContext().getActiveResolutions().entrySet().iterator();
      while (itrResolutions.hasNext()) {
        final Map.Entry<ComputationTargetReference, UniqueId> resolution = itrResolutions.next();
        if (resolution.getKey().getType().isTargetType(ComputationTargetType.POSITION)) {
          // Keep all positions; they'll be in our graph. It's a naughty function that could start requesting items for positions outside of the portfolio!
          continue;
        }
        if (validIdentifiers.contains(resolution.getValue())) {
          // Keep any resolutions relating to nodes in the graph
          continue;
        }
        // Delete anything else; legacy from failed resolutions
        itrResolutions.remove();
      }
    }

    /**
     * Fully resolves the portfolio structure for a view. A fully resolved structure has resolved {@link Security} objects for each {@link Position} within the portfolio. Note however that any
     * underlying or related data referenced by a security will not be resolved at this stage.
     * 
     * @param compilationContext the compilation context containing the view being compiled, not null
     * @return the resolved portfolio, not null
     */
    private Portfolio resolvePortfolio() {
      final UniqueId portfolioId = getContext().getViewDefinition().getPortfolioId();
      if (portfolioId == null) {
        throw new OpenGammaRuntimeException("The view definition '" + getContext().getViewDefinition().getName()
            + "' contains required portfolio outputs, but it does not reference a portfolio.");
      }
      final ComputationTargetResolver resolver = getContext().getServices().getFunctionCompilationContext().getRawComputationTargetResolver();
      final ComputationTargetResolver.AtVersionCorrection versioned = resolver.atVersionCorrection(getContext().getResolverVersionCorrection());
      final ComputationTargetSpecification specification = versioned.getSpecificationResolver()
          .getTargetSpecification(new ComputationTargetSpecification(ComputationTargetType.PORTFOLIO, portfolioId));
      if (specification == null) {
        throw new OpenGammaRuntimeException("Unable to identify portfolio '" + portfolioId + "' for view '" + getContext().getViewDefinition().getName() + "'");
      }
      final ComputationTarget target = versioned.resolve(specification);
      if (target == null) {
        throw new OpenGammaRuntimeException("Unable to resolve portfolio ID " + specification.getUniqueId() +
            " for view '" + getContext().getViewDefinition().getName() + "'");
      }
      return target.getValue(ComputationTargetType.PORTFOLIO);
    }

    protected boolean isPortfolioOutputs() {
      return _portfolioOutputs;
    }

    protected Portfolio getPortfolio() {
      return _portfolio;
    }

    /**
     * Cancels any active builders.
     */
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
      do {
        boolean result = true;
        try {
          for (final DependencyGraphBuilder builder : getContext().getBuilders()) {
            result &= builder.cancel(mayInterruptIfRunning);
          }
          return result;
        } catch (ConcurrentModificationException e) {
          // Ignore
        }
      } while (true);
    }

    /**
     * Tests if any of the builders have been canceled.
     */
    @Override
    public boolean isCancelled() {
      do {
        boolean result = false;
        try {
          for (final DependencyGraphBuilder builder : getContext().getBuilders()) {
            result |= builder.isCancelled();
          }
          return result;
        } catch (ConcurrentModificationException e) {
          // Ignore
        }
      } while (true);
    }

    /**
     * Tests if all of the builders have completed.
     */
    @Override
    public boolean isDone() {
      return _result != null;
    }

    @Override
    public CompiledViewDefinitionWithGraphsImpl get() {
      final ConcurrentMap<ComputationTargetReference, UniqueId> resolutions = getContext().getActiveResolutions();
      if (isPortfolioOutputs()) {
        for (final DependencyGraphBuilder builder : getContext().getBuilders()) {
          final FunctionCompilationContext functionContext = builder.getCompilationContext();
          if (!functionContext.getViewCalculationConfiguration().getAllPortfolioRequirements().isEmpty()) {
            if (_portfolio == null) {
              _portfolio = resolvePortfolio();
              resolutions.putIfAbsent(new ComputationTargetSpecification(ComputationTargetType.PORTFOLIO, getContext().getViewDefinition().getPortfolioId()), _portfolio.getUniqueId());
            }
            functionContext.setPortfolio(_portfolio);
          }
        }
      }
      long t = -System.nanoTime();
      compile();
      final Collection<DependencyGraph> graphs = getContext().getGraphs();
      t += System.nanoTime();
      s_logger.info("Processed dependency graphs after {}ms", t / 1e6);
      removeUnusedResolutions(graphs);
      _result = new CompiledViewDefinitionWithGraphsImpl(getContext().getResolverVersionCorrection(), s_uniqueIdentifiers.get(), getContext().getViewDefinition(), graphs, resolutions, _portfolio,
          getContext().getServices().getFunctionCompilationContext().getFunctionInitId());
      if (OUTPUT_DEPENDENCY_GRAPHS) {
        outputDependencyGraphs(graphs);
      }
      if (OUTPUT_LIVE_DATA_REQUIREMENTS) {
        outputLiveDataRequirements(graphs);
      }
      if (OUTPUT_FAILURE_REPORTS) {
        outputFailureReports(_viewCompilationContext.getBuilders());
      }
      return _result;
    }

    @Override
    public CompiledViewDefinitionWithGraphsImpl get(final long timeout, final TimeUnit unit) {
      throw new UnsupportedOperationException();
    }

  }

  private static class FullCompilationTask extends CompilationTask {

    protected FullCompilationTask(final ViewCompilationContext context) {
      super(context);
    }

    @Override
    protected void compile(final DependencyGraphBuilder builder) {
      final ViewCalculationConfiguration config = getContext().getViewDefinition().getCalculationConfiguration(builder.getCalculationConfigurationName());
      addSpecificRequirements(builder, getContext().getViewDefinition().getResultModelDefinition(), config);
      addPortfolioRequirements(builder, getContext(), config, null, null);
    }

    @Override
    protected void compile() {
      s_logger.info("Performing full compilation");
      try (Timer.Context context = s_fullTimer.time()) {
        super.compile();
      }
    }

  }

  private static class IncrementalCompilationTask extends CompilationTask {

    private final Map<String, Pair<DependencyGraph, Set<ValueRequirement>>> _previousGraphs;
    private final Set<UniqueId> _unchangedNodes;
    private final Set<UniqueId> _changedPositions;

    protected IncrementalCompilationTask(final ViewCompilationContext context, final Map<String, Pair<DependencyGraph, Set<ValueRequirement>>> previousGraphs,
        final Set<UniqueId> changedPositions, final Set<UniqueId> unchangedNodes) {
      super(context);
      _previousGraphs = previousGraphs;
      _unchangedNodes = unchangedNodes;
      _changedPositions = changedPositions;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void compile(final DependencyGraphBuilder builder) {
      final ViewCalculationConfiguration calcConfig = getContext().getViewDefinition().getCalculationConfiguration(builder.getCalculationConfigurationName());
      final Pair<DependencyGraph, Set<ValueRequirement>> graphPair = _previousGraphs.remove(builder.getCalculationConfigurationName());
      if (graphPair != null) {
        final DependencyGraph graph = graphPair.getFirst();
        if (builder.getCompilationContext().getPortfolio() != null) {
          // Remove any invalid terminal outputs from the graph
          final PortfolioIdentifierGatherer gatherer = new PortfolioIdentifierGatherer();
          PortfolioNodeTraverser.parallel(gatherer, getContext().getServices().getExecutorService()).traverse(builder.getCompilationContext().getPortfolio().getRootNode());
          final Set<UniqueId> identifiers = gatherer.getIdentifiers();
          final Set<ValueRequirement> specifics = calcConfig.getSpecificRequirements();
          final Map<ValueSpecification, Set<ValueRequirement>> terminalOutputs = graph.getTerminalOutputs();
          ValueSpecification[] removeValueSpec = null;
          Set<ValueRequirement>[] removeValueReq = null;
          int i = 0;
          for (Map.Entry<ValueSpecification, Set<ValueRequirement>> terminal : terminalOutputs.entrySet()) {
            if (!identifiers.contains(terminal.getKey().getTargetSpecification().getUniqueId())) {
              // Can't be a portfolio requirement
              Set<ValueRequirement> toRemove = null;
              for (ValueRequirement requirement : terminal.getValue()) {
                if (!specifics.contains(requirement)) {
                  // Not a specific requirement
                  if (toRemove == null) {
                    toRemove = Sets.newHashSetWithExpectedSize(terminal.getValue().size());
                  }
                  toRemove.add(requirement);
                }
              }
              if (toRemove != null) {
                if (i == 0) {
                  removeValueSpec = new ValueSpecification[terminalOutputs.size()];
                  removeValueReq = new Set[terminalOutputs.size()];
                }
                removeValueSpec[i] = terminal.getKey();
                removeValueReq[i++] = toRemove;
              }
            }
          }
          if (i > 0) {
            s_logger.info("Removing {} unmatched terminal outputs from {}", i, graph);
            do {
              i--;
              graph.removeTerminalOutputs(removeValueReq[i], removeValueSpec[i]);
            } while (i > 0);
          }
        }
        // Populate the builder with the graph
        builder.setDependencyGraph(graph);
        final Set<ValueRequirement> requirements = graphPair.getSecond();
        if (requirements.isEmpty()) {
          s_logger.debug("No incremental work for {}", graph);
        } else {
          s_logger.info("{} incremental resolutions required for {}", requirements.size(), graph);
          builder.addTarget(requirements);
        }
      }
      if (_unchangedNodes != null) {
        s_logger.info("Adding portfolio requirements with unchanged node set");
        addPortfolioRequirements(builder, getContext(), calcConfig, null, _unchangedNodes);
      } else if (_changedPositions != null) {
        s_logger.info("Adding portfolio requirements with changed position set");
        addPortfolioRequirements(builder, getContext(), calcConfig, _changedPositions, null);
      } else {
        s_logger.info("No additional portfolio requirements needed");
      }
    }

    @Override
    public void compile() {
      s_logger.info("Performing incremental compilation");
      try (Timer.Context context = s_deltaTimer.time()) {
        super.compile();
        while (getContext().hasExpiredResolutions()) {
          // The graph(s) may be inconsistent because we didn't detect all changes in advance. The identifiers in the expired set correspond to nodes
          // that we must get rid of, and create new value requirements to regenerate any affected top-level nodes.
          final Set<UniqueId> expiredResolutions = getContext().takeExpiredResolutions();
          s_logger.debug("Revalidate graph(s) against {} expired resolutions", expiredResolutions.size());
          final SubGraphingFilter filter = new SubGraphingFilter(new InvalidTargetDependencyNodeFilter(expiredResolutions));
          final Set<ValueRequirement> missing = new HashSet<ValueRequirement>();
          final Collection<DependencyGraph> graphs = new ArrayList<DependencyGraph>(getContext().getGraphs());
          getContext().getGraphs().clear();
          for (final DependencyGraph graph : graphs) {
            final DependencyGraph filtered = filter.subGraph(graph, missing);
            if (missing.isEmpty()) {
              // No requirements ejected from this graph - keep it
              getContext().getGraphs().add(graph);
            } else {
              s_logger.info("Late changes detected affecting {} requirements", missing.size());
              final DependencyGraphBuilder builder = getContext().createBuilder(getContext().getViewDefinition().getCalculationConfiguration(graph.getCalculationConfigurationName()));
              if (getPortfolio() != null) {
                builder.getCompilationContext().setPortfolio(getPortfolio());
              }
              builder.setDependencyGraph(filtered);
              builder.addTarget(missing);
              missing.clear();
              getContext().getGraphs().add(builder.getDependencyGraph());
            }
          }
        }
      }
    }

  }

  public static Future<CompiledViewDefinitionWithGraphsImpl> fullCompileTask(final ViewDefinition viewDefinition, final ViewCompilationServices compilationServices,
      final Instant valuationTime, final VersionCorrection versionCorrection) {
    s_logger.info("Full compile of {} for use at {}", viewDefinition.getName(), valuationTime);
    return new FullCompilationTask(new ViewCompilationContext(viewDefinition, compilationServices, valuationTime, versionCorrection, new ConcurrentHashMap<ComputationTargetReference, UniqueId>()));
  }

  public static Future<CompiledViewDefinitionWithGraphsImpl> incrementalCompileTask(final ViewDefinition viewDefinition, final ViewCompilationServices compilationServices,
      final Instant valuationTime, final VersionCorrection versionCorrection, final Map<String, Pair<DependencyGraph, Set<ValueRequirement>>> previousGraphs,
      final ConcurrentMap<ComputationTargetReference, UniqueId> resolutions, final Set<UniqueId> changedPositions, final Set<UniqueId> unchangedNodes) {
    s_logger.info("Incremental compile of {} for use at {}", viewDefinition.getName(), valuationTime);
    return new IncrementalCompilationTask(new ViewCompilationContext(viewDefinition, compilationServices, valuationTime, versionCorrection, resolutions), previousGraphs, changedPositions,
        unchangedNodes);
  }

  public static CompiledViewDefinitionWithGraphsImpl compile(final ViewDefinition viewDefinition, final ViewCompilationServices compilationServices,
      final Instant valuationTime, final VersionCorrection versionCorrection) {
    try {
      return fullCompileTask(viewDefinition, compilationServices, valuationTime, versionCorrection).get();
    } catch (final InterruptedException e) {
      throw new OpenGammaRuntimeException("Interrupted", e);
    } catch (final ExecutionException e) {
      throw new OpenGammaRuntimeException("Failed", e);
    }
  }

  private static void addSpecificRequirements(final DependencyGraphBuilder builder, final ResultModelDefinition resultModelDefinition, final ViewCalculationConfiguration calcConfig) {
    // Scan through the current calc config's specific requirements
    for (final ValueRequirement requirement : calcConfig.getSpecificRequirements()) {
      final ComputationTargetReference targetReference = requirement.getTargetReference();
      if (resultModelDefinition.getOutputMode(targetReference.getType()) == ResultOutputMode.NONE) {
        // We're not including this in the results, so no point it being a terminal output. It will be added
        // automatically if it is needed for some other terminal output.
        continue;
      }
      // Add the specific requirement to the current calc config's dep graph builder
      builder.addTarget(requirement);
    }
  }

  private static Set<Pair<String, ValueProperties>> getStripes(final Map<String, Set<Pair<String, ValueProperties>>> portfolioRequirementsBySecurityType) {
    final Set<Pair<String, ValueProperties>> stripes = new HashSet<Pair<String, ValueProperties>>();
    for (Set<Pair<String, ValueProperties>> stripe : portfolioRequirementsBySecurityType.values()) {
      stripes.addAll(stripe);
    }
    return stripes;
  }

  /**
   * Indicates whether portfolio requirements should be added in batches or together. All together may use more memory but may be quicker. In batches may use less memory but may be slower.
   * <p>
   * Views with many column definitions on large portfolios can benefit significantly from this as the memory required to process them all concurrently may be prohibitive.
   * 
   * @return true to stripe the portfolio requirements in batches to the graph builder, false to do them all at once
   * @deprecated this is a temporary measure; enabling/disabling the striping should be performed programaticaly based on view/portfolio heuristics
   */
  @Deprecated
  public static boolean isStripedPortfolioRequirements() {
    return s_striped;
  }

  /**
   * Sets whether to batch portfolio requirements during graph builds.
   * 
   * @param useStripes true to stripe the portfolio requirements in batches to the graph builder, false to do them all at once
   * @deprecated this is a temporary measure; enabling/disabling the striping should be performed programaticaly based on view/portfolio heuristics
   * @see {@link #isStripedPortfolioRequirements}.
   */
  @Deprecated
  public static void setStripedPortfolioRequirements(final boolean useStripes) {
    s_striped = useStripes;
  }

  private static void addPortfolioRequirements(final DependencyGraphBuilder builder, final ViewCompilationContext context, final ViewCalculationConfiguration calcConfig,
      final Set<UniqueId> includeEvents, final Set<UniqueId> excludeEvents) {
    if (calcConfig.getAllPortfolioRequirements().size() == 0) {
      // No portfolio requirements for this calculation configuration - avoid further processing.
      return;
    }
    final Portfolio portfolio = builder.getCompilationContext().getPortfolio();
    final PortfolioCompilerTraversalCallback traversalCallback = new PortfolioCompilerTraversalCallback(calcConfig, builder, context.getActiveResolutions(), includeEvents, excludeEvents);
    final PortfolioNodeTraverser traverser = PortfolioNodeTraverser.parallel(traversalCallback, context.getServices().getExecutorService());
    if (isStripedPortfolioRequirements()) {
      final Map<String, Set<Pair<String, ValueProperties>>> requirementsBySecurityType = traversalCallback.getPortfolioRequirementsBySecurityType();
      Map<String, Set<Pair<String, ValueProperties>>> requirementSubSet = Maps.newHashMapWithExpectedSize(requirementsBySecurityType.size());
      traversalCallback.setPortfolioRequirementsBySecurityType(requirementSubSet);
      for (Pair<String, ValueProperties> stripe : getStripes(requirementsBySecurityType)) {
        s_logger.debug("Adding {} portfolio requirement stripe", stripe);
        final Set<Pair<String, ValueProperties>> stripeRequirements = Collections.singleton(stripe);
        for (Map.Entry<String, Set<Pair<String, ValueProperties>>> securityTypeRequirement : requirementsBySecurityType.entrySet()) {
          if (securityTypeRequirement.getValue().contains(stripe)) {
            requirementSubSet.put(securityTypeRequirement.getKey(), stripeRequirements);
          } else {
            requirementSubSet.remove(securityTypeRequirement.getKey());
          }
        }
        traversalCallback.reset();
        traverser.traverse(portfolio.getRootNode());
        try {
          s_logger.debug("Waiting for stripe {} to complete", stripe);
          // TODO: Waiting for a completion state causes any progress tracker to abort (it sees 100% and stops). Need to rethink how to do the progress estimates.
          builder.waitForDependencyGraphBuild();
        } catch (InterruptedException e) {
          throw new OpenGammaRuntimeException("Interrupted during striped compilation", e);
        }
      }
    } else {
      s_logger.debug("Adding all portfolio requirements directly");
      traverser.traverse(portfolio.getRootNode());
    }
  }

  private static void outputDependencyGraphs(final Collection<DependencyGraph> graphs) {
    final StringBuilder sb = new StringBuilder();
    for (DependencyGraph graph : graphs) {
      final String configName = graph.getCalculationConfigurationName();
      sb.append("DepGraph for ").append(configName);
      sb.append("\tProducing values ").append(graph.getOutputSpecifications());
      for (final DependencyNode depNode : graph.getDependencyNodes()) {
        sb.append("\t\tNode:\n").append(DependencyNodeFormatter.toString(depNode));
      }
    }
    s_logger.warn("Dependency Graphs -- \n{}", sb);
  }

  private static void outputLiveDataRequirements(final Collection<DependencyGraph> graphs) {
    final StringBuilder sb = new StringBuilder();
    for (DependencyGraph graph : graphs) {
      final String configName = graph.getCalculationConfigurationName();
      final Collection<ValueSpecification> requiredLiveData = graph.getAllRequiredMarketData();
      if (requiredLiveData.isEmpty()) {
        sb.append(configName).append(" requires no live data.\n");
      } else {
        sb.append("Live data for ").append(configName).append("\n");
        for (final ValueSpecification liveRequirement : requiredLiveData) {
          sb.append("\t").append(liveRequirement).append("\n");
        }
      }
    }
    s_logger.warn("Live data requirements -- \n{}", sb);
  }

  private static void outputFailureReports(final Collection<DependencyGraphBuilder> builders) {
    for (final DependencyGraphBuilder builder : builders) {
      outputFailureReport(builder);
    }
  }

  public static void outputFailureReport(final DependencyGraphBuilder builder) {
    final Map<Throwable, Integer> exceptions = builder.getExceptions();
    if (!exceptions.isEmpty()) {
      for (final Map.Entry<Throwable, Integer> entry : exceptions.entrySet()) {
        final Throwable exception = entry.getKey();
        final Integer count = entry.getValue();
        if (exception.getCause() != null) {
          if (s_logger.isDebugEnabled()) {
            s_logger.debug("Nested exception raised " + count + " time(s)", exception);
          }
        } else {
          if (s_logger.isWarnEnabled()) {
            s_logger.warn("Exception raised " + count + " time(s)", exception);
          }
        }
      }
    } else {
      s_logger.info("No exceptions raised for configuration {}", builder.getCalculationConfigurationName());
    }
  }

}
