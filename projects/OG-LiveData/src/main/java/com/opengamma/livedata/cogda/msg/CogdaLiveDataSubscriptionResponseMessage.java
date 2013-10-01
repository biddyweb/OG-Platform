/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.livedata.cogda.msg;

import java.util.Map;

import org.fudgemsg.FudgeMsg;
import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * 
 */
@BeanDefinition
public class CogdaLiveDataSubscriptionResponseMessage extends CogdaLiveDataCommandResponseMessage {
  /**
   * The current snapshot of the values for the item.
   */
  @PropertyDefinition(validate = "notNull")
  private FudgeMsg _snapshot;

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CogdaLiveDataSubscriptionResponseMessage}.
   * @return the meta-bean, not null
   */
  public static CogdaLiveDataSubscriptionResponseMessage.Meta meta() {
    return CogdaLiveDataSubscriptionResponseMessage.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CogdaLiveDataSubscriptionResponseMessage.Meta.INSTANCE);
  }

  @Override
  public CogdaLiveDataSubscriptionResponseMessage.Meta metaBean() {
    return CogdaLiveDataSubscriptionResponseMessage.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the current snapshot of the values for the item.
   * @return the value of the property, not null
   */
  public FudgeMsg getSnapshot() {
    return _snapshot;
  }

  /**
   * Sets the current snapshot of the values for the item.
   * @param snapshot  the new value of the property, not null
   */
  public void setSnapshot(FudgeMsg snapshot) {
    JodaBeanUtils.notNull(snapshot, "snapshot");
    this._snapshot = snapshot;
  }

  /**
   * Gets the the {@code snapshot} property.
   * @return the property, not null
   */
  public final Property<FudgeMsg> snapshot() {
    return metaBean().snapshot().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public CogdaLiveDataSubscriptionResponseMessage clone() {
    return (CogdaLiveDataSubscriptionResponseMessage) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CogdaLiveDataSubscriptionResponseMessage other = (CogdaLiveDataSubscriptionResponseMessage) obj;
      return JodaBeanUtils.equal(getSnapshot(), other.getSnapshot()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getSnapshot());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("CogdaLiveDataSubscriptionResponseMessage{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("snapshot").append('=').append(getSnapshot()).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CogdaLiveDataSubscriptionResponseMessage}.
   */
  public static class Meta extends CogdaLiveDataCommandResponseMessage.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code snapshot} property.
     */
    private final MetaProperty<FudgeMsg> _snapshot = DirectMetaProperty.ofReadWrite(
        this, "snapshot", CogdaLiveDataSubscriptionResponseMessage.class, FudgeMsg.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "snapshot");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 284874180:  // snapshot
          return _snapshot;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CogdaLiveDataSubscriptionResponseMessage> builder() {
      return new DirectBeanBuilder<CogdaLiveDataSubscriptionResponseMessage>(new CogdaLiveDataSubscriptionResponseMessage());
    }

    @Override
    public Class<? extends CogdaLiveDataSubscriptionResponseMessage> beanType() {
      return CogdaLiveDataSubscriptionResponseMessage.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code snapshot} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<FudgeMsg> snapshot() {
      return _snapshot;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 284874180:  // snapshot
          return ((CogdaLiveDataSubscriptionResponseMessage) bean).getSnapshot();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 284874180:  // snapshot
          ((CogdaLiveDataSubscriptionResponseMessage) bean).setSnapshot((FudgeMsg) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((CogdaLiveDataSubscriptionResponseMessage) bean)._snapshot, "snapshot");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
