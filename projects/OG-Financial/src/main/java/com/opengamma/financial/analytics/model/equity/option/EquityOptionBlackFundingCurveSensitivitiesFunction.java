/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.option;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.equity.StaticReplicationDataBundle;
import com.opengamma.analytics.financial.equity.option.EquityIndexOption;
import com.opengamma.analytics.financial.equity.option.EquityIndexOptionBlackMethod;
import com.opengamma.analytics.financial.interestrate.NodeYieldSensitivityCalculator;
import com.opengamma.analytics.financial.interestrate.PresentValueNodeSensitivityCalculator;
import com.opengamma.analytics.financial.interestrate.YieldCurveBundle;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.analytics.math.curve.InterpolatedDoublesCurve;
import com.opengamma.analytics.math.matrix.DoubleMatrix1D;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.financial.analytics.ircurve.InterpolatedYieldCurveSpecificationWithSecurities;
import com.opengamma.financial.analytics.model.YieldCurveNodeSensitivitiesHelper;
import com.opengamma.financial.security.FinancialSecurityUtils;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.DoublesPair;

/**
 * Calculates the nodal sensitivities of an equity index or equity option to the funding curve (bucketed rho).
 */
public class EquityOptionBlackFundingCurveSensitivitiesFunction extends EquityOptionBlackFunction {

  /**
   * Default constructor
   */
  public EquityOptionBlackFundingCurveSensitivitiesFunction() {
    super(ValueRequirementNames.YIELD_CURVE_NODE_SENSITIVITIES);
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target, final ValueRequirement desiredValue) {
    final Set<ValueRequirement> result = super.getRequirements(context, target, desiredValue);
    if (result == null) {
      return null;
    }
    // Get Funding Curve Name
    final Set<String> fundingCurves = desiredValue.getConstraints().getValues(ValuePropertyNames.CURVE);
    if (fundingCurves == null || fundingCurves.size() != 1) {
      return null;
    }
    final String fundingCurveName = Iterables.getOnlyElement(fundingCurves);
    final Currency currency = FinancialSecurityUtils.getCurrency(target.getSecurity());
    result.add(getCurveSpecRequirement(currency, fundingCurveName));
    return result;
  }

  // Need to do this to get labels for the output
  private ValueRequirement getCurveSpecRequirement(final Currency currency, final String curveName) {
    final ValueProperties properties = ValueProperties.builder().with(ValuePropertyNames.CURVE, curveName).get();
    return new ValueRequirement(ValueRequirementNames.YIELD_CURVE_SPEC, ComputationTargetSpecification.of(currency), properties);
  }

  @Override
  protected Set<ComputedValue> computeValues(final EquityIndexOption derivative, final StaticReplicationDataBundle market, final FunctionInputs inputs,
      final Set<ValueRequirement> desiredValues, final ComputationTargetSpecification targetSpec, final ValueProperties resultProperties) {
    final YieldAndDiscountCurve fundingCurve = market.getDiscountCurve();
    if (!(fundingCurve instanceof YieldCurve)) {
      throw new OpenGammaRuntimeException("Can only handle YieldCurve");
    }
    final ValueRequirement desiredValue = Iterables.getOnlyElement(desiredValues);
    final String fundingCurveName = desiredValue.getConstraint(ValuePropertyNames.CURVE);
    final DoubleMatrix1D sensVector;
    if (!(((YieldCurve) fundingCurve).getCurve() instanceof InterpolatedDoublesCurve)) {
      throw new OpenGammaRuntimeException("YieldCurveNodeSensitivities currently available only for Funding Curve backed by a InterpolatedDoublesCurve");

    }
    final YieldCurveBundle curveBundle = new YieldCurveBundle();
    curveBundle.setCurve(fundingCurveName, fundingCurve);
    if (!(fundingCurve instanceof YieldCurve)) {
      throw new IllegalArgumentException("Can only handle YieldCurve");
    }
    // We can use chain rule to distribute closed-form model sensitivity across the curve
    // We have two dates of interest, expiry and settlement
    // Sensitivity to the rate to expiry might be used to estimate the underlying's forward, but we don't include this here.
    // The sensitivity to settlement rate is in the discounting, the ZeroBond price: PV = Z(t,S) * C(F,K,sig,T)
    //REVIEW emcleod 21-12-2012 calculations of analytic values does not belong in OG-Financial - this logic should be moved into OG-Analytics
    final double settle = derivative.getTimeToSettlement();
    final EquityIndexOptionBlackMethod model = EquityIndexOptionBlackMethod.getInstance();
    final double rhoSettle = -1 * settle * model.presentValue(derivative, market);
    //  We use PresentValueNodeSensitivityCalculator to distribute this risk across the curve
    final NodeYieldSensitivityCalculator distributor = PresentValueNodeSensitivityCalculator.getDefaultInstance();
    // What's left is to package up the inputs to the distributor, a YieldCurveBundle and a Map of Sensitivities
    final Map<String, List<DoublesPair>> curveSensMap = new HashMap<String, List<DoublesPair>>();
    curveSensMap.put(fundingCurveName, Lists.newArrayList(new DoublesPair(settle, rhoSettle)));
    sensVector = distributor.curveToNodeSensitivities(curveSensMap, curveBundle);

    // Build up InstrumentLabelledSensitivities for the Curve
    final Object curveSpecObject = inputs.getValue(ValueRequirementNames.YIELD_CURVE_SPEC);
    if (curveSpecObject == null) {
      throw new OpenGammaRuntimeException("Curve specification was null");
    }
    final InterpolatedYieldCurveSpecificationWithSecurities curveSpec = (InterpolatedYieldCurveSpecificationWithSecurities) curveSpecObject;
    final ValueSpecification resultSpec = new ValueSpecification(getValueRequirementNames()[0], targetSpec, resultProperties);
    return YieldCurveNodeSensitivitiesHelper.getInstrumentLabelledSensitivitiesForCurve(fundingCurveName, curveBundle, sensVector, curveSpec, resultSpec);
  }

}
