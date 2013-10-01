/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.masterdb.security.hibernate.cds;

import java.util.Map;

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
public class LegacyVanillaCDSSecurityBean extends CreditDefaultSwapSecurityBean {
  
  @PropertyDefinition
  private Double _parSpread;

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code LegacyVanillaCDSSecurityBean}.
   * @return the meta-bean, not null
   */
  public static LegacyVanillaCDSSecurityBean.Meta meta() {
    return LegacyVanillaCDSSecurityBean.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(LegacyVanillaCDSSecurityBean.Meta.INSTANCE);
  }

  @Override
  public LegacyVanillaCDSSecurityBean.Meta metaBean() {
    return LegacyVanillaCDSSecurityBean.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the parSpread.
   * @return the value of the property
   */
  public Double getParSpread() {
    return _parSpread;
  }

  /**
   * Sets the parSpread.
   * @param parSpread  the new value of the property
   */
  public void setParSpread(Double parSpread) {
    this._parSpread = parSpread;
  }

  /**
   * Gets the the {@code parSpread} property.
   * @return the property, not null
   */
  public final Property<Double> parSpread() {
    return metaBean().parSpread().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public LegacyVanillaCDSSecurityBean clone() {
    return (LegacyVanillaCDSSecurityBean) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      LegacyVanillaCDSSecurityBean other = (LegacyVanillaCDSSecurityBean) obj;
      return JodaBeanUtils.equal(getParSpread(), other.getParSpread()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getParSpread());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("LegacyVanillaCDSSecurityBean{");
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
    buf.append("parSpread").append('=').append(getParSpread()).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code LegacyVanillaCDSSecurityBean}.
   */
  public static class Meta extends CreditDefaultSwapSecurityBean.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code parSpread} property.
     */
    private final MetaProperty<Double> _parSpread = DirectMetaProperty.ofReadWrite(
        this, "parSpread", LegacyVanillaCDSSecurityBean.class, Double.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "parSpread");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          return _parSpread;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends LegacyVanillaCDSSecurityBean> builder() {
      return new DirectBeanBuilder<LegacyVanillaCDSSecurityBean>(new LegacyVanillaCDSSecurityBean());
    }

    @Override
    public Class<? extends LegacyVanillaCDSSecurityBean> beanType() {
      return LegacyVanillaCDSSecurityBean.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code parSpread} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> parSpread() {
      return _parSpread;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          return ((LegacyVanillaCDSSecurityBean) bean).getParSpread();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1556795764:  // parSpread
          ((LegacyVanillaCDSSecurityBean) bean).setParSpread((Double) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
