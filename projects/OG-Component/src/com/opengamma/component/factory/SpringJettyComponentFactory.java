/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.GenericApplicationContext;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.component.ComponentFactory;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.RestComponents;
import com.opengamma.transport.jaxrs.FudgeBinaryProducer;
import com.opengamma.transport.jaxrs.FudgeJSONConsumer;
import com.opengamma.transport.jaxrs.FudgeJSONProducer;
import com.opengamma.transport.jaxrs.FudgeObjectBinaryConsumer;
import com.opengamma.transport.jaxrs.FudgeObjectBinaryProducer;
import com.opengamma.transport.jaxrs.FudgeXMLConsumer;
import com.opengamma.transport.jaxrs.FudgeXMLProducer;
import com.opengamma.util.rest.DataDuplicationExceptionMapper;
import com.opengamma.util.rest.DataNotFoundExceptionMapper;
import com.opengamma.util.rest.IllegalArgumentExceptionMapper;
import com.opengamma.util.rest.ThrowableExceptionMapper;
import com.opengamma.util.rest.UnsupportedOperationExceptionMapper;
import com.opengamma.util.rest.WebApplicationExceptionMapper;

/**
 * Component definition for the database connectors defined in Spring.
 */
@BeanDefinition
public class SpringJettyComponentFactory extends AbstractSpringComponentFactory implements ComponentFactory {

  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) {
    GenericApplicationContext appContext = createApplicationContext();
    
    String[] beanNames = appContext.getBeanNamesForType(Server.class);
    if (beanNames.length != 1) {
      throw new IllegalStateException("Expected 1 Jetty server, but found " + beanNames.length);
    }
    Server server = appContext.getBean(beanNames[0], Server.class);
    repo.registerInfrastructure(Server.class, "jetty", server);
    repo.registerLifecycle(new ServerLifecycle(server));
    
    RestComponents restComponents = repo.getRestComponents();
    restComponents.publishHelper(new FudgeJSONConsumer());
    restComponents.publishHelper(new FudgeJSONProducer());
    restComponents.publishHelper(new FudgeXMLConsumer());
    restComponents.publishHelper(new FudgeXMLProducer());
    restComponents.publishHelper(new FudgeBinaryProducer());
    restComponents.publishHelper(new FudgeObjectBinaryConsumer());
    restComponents.publishHelper(new FudgeObjectBinaryProducer());
    restComponents.publishHelper(new DataNotFoundExceptionMapper());
    restComponents.publishHelper(new DataDuplicationExceptionMapper());
    restComponents.publishHelper(new IllegalArgumentExceptionMapper());
    restComponents.publishHelper(new UnsupportedOperationExceptionMapper());
    restComponents.publishHelper(new WebApplicationExceptionMapper());
    restComponents.publishHelper(new ThrowableExceptionMapper());
  }

  //-------------------------------------------------------------------------
  /**
   * Wraps Jetty LifeCycle in Spring's Lifecycle.
   */
  static class ServerLifecycle implements Lifecycle {
    private final Server _server;

    public ServerLifecycle(Server server) {
      _server = server;
    }

    @Override
    public void start() {
      try {
        _server.start();
      } catch (Exception ex) {
        throw new OpenGammaRuntimeException(ex.getMessage(), ex);
      }
    }

    @Override
    public void stop() {
      try {
        _server.stop();
      } catch (Exception ex) {
        throw new OpenGammaRuntimeException(ex.getMessage(), ex);
      }
    }

    @Override
    public boolean isRunning() {
      return _server.isStarted();
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SpringJettyComponentFactory}.
   * @return the meta-bean, not null
   */
  public static SpringJettyComponentFactory.Meta meta() {
    return SpringJettyComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(SpringJettyComponentFactory.Meta.INSTANCE);
  }

  @Override
  public SpringJettyComponentFactory.Meta metaBean() {
    return SpringJettyComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SpringJettyComponentFactory}.
   */
  public static class Meta extends AbstractSpringComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends SpringJettyComponentFactory> builder() {
      return new DirectBeanBuilder<SpringJettyComponentFactory>(new SpringJettyComponentFactory());
    }

    @Override
    public Class<? extends SpringJettyComponentFactory> beanType() {
      return SpringJettyComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
