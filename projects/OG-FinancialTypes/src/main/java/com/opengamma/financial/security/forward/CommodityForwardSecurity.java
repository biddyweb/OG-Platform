/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.forward;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for commodity forwards.
 */
@BeanDefinition
public abstract class CommodityForwardSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * Quantity of unit.
   */
  @PropertyDefinition
  private Double _unitNumber;
  /**
   * Name of the unit.
   */
  @PropertyDefinition
  private String _unitName;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "COMMODITY_FORWARD";

  /**
   * The expiry.
   */
  @PropertyDefinition(validate = "notNull")
  private Expiry _expiry;

  /**
   * The currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _currency;
  /**
   * The unit amount.
   */
  @PropertyDefinition
  private double _unitAmount;

  /**
   * The future category.
   */
  @PropertyDefinition(validate = "notNull")
  private String _contractCategory;

  /**
   * The underlying identifier.
   */
  @PropertyDefinition
  private ExternalId _underlyingId;


  CommodityForwardSecurity() { //For builder
    super(SECURITY_TYPE);
  }

  public CommodityForwardSecurity(final String unitName, final Double unitNumber, final Expiry expiry, final Currency currency, final double unitAmount, final String category) {
    super(SECURITY_TYPE);
    ArgumentChecker.notNull(category, "category");
    setUnitName(unitName);
    setUnitNumber(unitNumber);
    setExpiry(expiry);
    setCurrency(currency);
    setUnitAmount(unitAmount);
    setContractCategory(category);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CommodityForwardSecurity}.
   * @return the meta-bean, not null
   */
  public static CommodityForwardSecurity.Meta meta() {
    return CommodityForwardSecurity.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CommodityForwardSecurity.Meta.INSTANCE);
  }

  @Override
  public CommodityForwardSecurity.Meta metaBean() {
    return CommodityForwardSecurity.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets quantity of unit.
   * @return the value of the property
   */
  public Double getUnitNumber() {
    return _unitNumber;
  }

  /**
   * Sets quantity of unit.
   * @param unitNumber  the new value of the property
   */
  public void setUnitNumber(Double unitNumber) {
    this._unitNumber = unitNumber;
  }

  /**
   * Gets the the {@code unitNumber} property.
   * @return the property, not null
   */
  public final Property<Double> unitNumber() {
    return metaBean().unitNumber().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets name of the unit.
   * @return the value of the property
   */
  public String getUnitName() {
    return _unitName;
  }

  /**
   * Sets name of the unit.
   * @param unitName  the new value of the property
   */
  public void setUnitName(String unitName) {
    this._unitName = unitName;
  }

  /**
   * Gets the the {@code unitName} property.
   * @return the property, not null
   */
  public final Property<String> unitName() {
    return metaBean().unitName().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry.
   * @return the value of the property, not null
   */
  public Expiry getExpiry() {
    return _expiry;
  }

  /**
   * Sets the expiry.
   * @param expiry  the new value of the property, not null
   */
  public void setExpiry(Expiry expiry) {
    JodaBeanUtils.notNull(expiry, "expiry");
    this._expiry = expiry;
  }

  /**
   * Gets the the {@code expiry} property.
   * @return the property, not null
   */
  public final Property<Expiry> expiry() {
    return metaBean().expiry().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return _currency;
  }

  /**
   * Sets the currency.
   * @param currency  the new value of the property, not null
   */
  public void setCurrency(Currency currency) {
    JodaBeanUtils.notNull(currency, "currency");
    this._currency = currency;
  }

  /**
   * Gets the the {@code currency} property.
   * @return the property, not null
   */
  public final Property<Currency> currency() {
    return metaBean().currency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the unit amount.
   * @return the value of the property
   */
  public double getUnitAmount() {
    return _unitAmount;
  }

  /**
   * Sets the unit amount.
   * @param unitAmount  the new value of the property
   */
  public void setUnitAmount(double unitAmount) {
    this._unitAmount = unitAmount;
  }

  /**
   * Gets the the {@code unitAmount} property.
   * @return the property, not null
   */
  public final Property<Double> unitAmount() {
    return metaBean().unitAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the future category.
   * @return the value of the property, not null
   */
  public String getContractCategory() {
    return _contractCategory;
  }

  /**
   * Sets the future category.
   * @param contractCategory  the new value of the property, not null
   */
  public void setContractCategory(String contractCategory) {
    JodaBeanUtils.notNull(contractCategory, "contractCategory");
    this._contractCategory = contractCategory;
  }

  /**
   * Gets the the {@code contractCategory} property.
   * @return the property, not null
   */
  public final Property<String> contractCategory() {
    return metaBean().contractCategory().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying identifier.
   * @return the value of the property
   */
  public ExternalId getUnderlyingId() {
    return _underlyingId;
  }

  /**
   * Sets the underlying identifier.
   * @param underlyingId  the new value of the property
   */
  public void setUnderlyingId(ExternalId underlyingId) {
    this._underlyingId = underlyingId;
  }

  /**
   * Gets the the {@code underlyingId} property.
   * @return the property, not null
   */
  public final Property<ExternalId> underlyingId() {
    return metaBean().underlyingId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CommodityForwardSecurity other = (CommodityForwardSecurity) obj;
      return JodaBeanUtils.equal(getUnitNumber(), other.getUnitNumber()) &&
          JodaBeanUtils.equal(getUnitName(), other.getUnitName()) &&
          JodaBeanUtils.equal(getExpiry(), other.getExpiry()) &&
          JodaBeanUtils.equal(getCurrency(), other.getCurrency()) &&
          JodaBeanUtils.equal(getUnitAmount(), other.getUnitAmount()) &&
          JodaBeanUtils.equal(getContractCategory(), other.getContractCategory()) &&
          JodaBeanUtils.equal(getUnderlyingId(), other.getUnderlyingId()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnitNumber());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnitName());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExpiry());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnitAmount());
    hash += hash * 31 + JodaBeanUtils.hashCode(getContractCategory());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnderlyingId());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(256);
    buf.append("CommodityForwardSecurity{");
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
    buf.append("unitNumber").append('=').append(getUnitNumber()).append(',').append(' ');
    buf.append("unitName").append('=').append(getUnitName()).append(',').append(' ');
    buf.append("expiry").append('=').append(getExpiry()).append(',').append(' ');
    buf.append("currency").append('=').append(getCurrency()).append(',').append(' ');
    buf.append("unitAmount").append('=').append(getUnitAmount()).append(',').append(' ');
    buf.append("contractCategory").append('=').append(getContractCategory()).append(',').append(' ');
    buf.append("underlyingId").append('=').append(getUnderlyingId()).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CommodityForwardSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code unitNumber} property.
     */
    private final MetaProperty<Double> _unitNumber = DirectMetaProperty.ofReadWrite(
        this, "unitNumber", CommodityForwardSecurity.class, Double.class);
    /**
     * The meta-property for the {@code unitName} property.
     */
    private final MetaProperty<String> _unitName = DirectMetaProperty.ofReadWrite(
        this, "unitName", CommodityForwardSecurity.class, String.class);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<Expiry> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", CommodityForwardSecurity.class, Expiry.class);
    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> _currency = DirectMetaProperty.ofReadWrite(
        this, "currency", CommodityForwardSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code unitAmount} property.
     */
    private final MetaProperty<Double> _unitAmount = DirectMetaProperty.ofReadWrite(
        this, "unitAmount", CommodityForwardSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code contractCategory} property.
     */
    private final MetaProperty<String> _contractCategory = DirectMetaProperty.ofReadWrite(
        this, "contractCategory", CommodityForwardSecurity.class, String.class);
    /**
     * The meta-property for the {@code underlyingId} property.
     */
    private final MetaProperty<ExternalId> _underlyingId = DirectMetaProperty.ofReadWrite(
        this, "underlyingId", CommodityForwardSecurity.class, ExternalId.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "unitNumber",
        "unitName",
        "expiry",
        "currency",
        "unitAmount",
        "contractCategory",
        "underlyingId");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 2053402093:  // unitNumber
          return _unitNumber;
        case -292854225:  // unitName
          return _unitName;
        case -1289159373:  // expiry
          return _expiry;
        case 575402001:  // currency
          return _currency;
        case 1673913084:  // unitAmount
          return _unitAmount;
        case -666828752:  // contractCategory
          return _contractCategory;
        case -771625640:  // underlyingId
          return _underlyingId;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CommodityForwardSecurity> builder() {
      throw new UnsupportedOperationException("CommodityForwardSecurity is an abstract class");
    }

    @Override
    public Class<? extends CommodityForwardSecurity> beanType() {
      return CommodityForwardSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code unitNumber} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> unitNumber() {
      return _unitNumber;
    }

    /**
     * The meta-property for the {@code unitName} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> unitName() {
      return _unitName;
    }

    /**
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Expiry> expiry() {
      return _expiry;
    }

    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> currency() {
      return _currency;
    }

    /**
     * The meta-property for the {@code unitAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> unitAmount() {
      return _unitAmount;
    }

    /**
     * The meta-property for the {@code contractCategory} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> contractCategory() {
      return _contractCategory;
    }

    /**
     * The meta-property for the {@code underlyingId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> underlyingId() {
      return _underlyingId;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2053402093:  // unitNumber
          return ((CommodityForwardSecurity) bean).getUnitNumber();
        case -292854225:  // unitName
          return ((CommodityForwardSecurity) bean).getUnitName();
        case -1289159373:  // expiry
          return ((CommodityForwardSecurity) bean).getExpiry();
        case 575402001:  // currency
          return ((CommodityForwardSecurity) bean).getCurrency();
        case 1673913084:  // unitAmount
          return ((CommodityForwardSecurity) bean).getUnitAmount();
        case -666828752:  // contractCategory
          return ((CommodityForwardSecurity) bean).getContractCategory();
        case -771625640:  // underlyingId
          return ((CommodityForwardSecurity) bean).getUnderlyingId();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 2053402093:  // unitNumber
          ((CommodityForwardSecurity) bean).setUnitNumber((Double) newValue);
          return;
        case -292854225:  // unitName
          ((CommodityForwardSecurity) bean).setUnitName((String) newValue);
          return;
        case -1289159373:  // expiry
          ((CommodityForwardSecurity) bean).setExpiry((Expiry) newValue);
          return;
        case 575402001:  // currency
          ((CommodityForwardSecurity) bean).setCurrency((Currency) newValue);
          return;
        case 1673913084:  // unitAmount
          ((CommodityForwardSecurity) bean).setUnitAmount((Double) newValue);
          return;
        case -666828752:  // contractCategory
          ((CommodityForwardSecurity) bean).setContractCategory((String) newValue);
          return;
        case -771625640:  // underlyingId
          ((CommodityForwardSecurity) bean).setUnderlyingId((ExternalId) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((CommodityForwardSecurity) bean)._expiry, "expiry");
      JodaBeanUtils.notNull(((CommodityForwardSecurity) bean)._currency, "currency");
      JodaBeanUtils.notNull(((CommodityForwardSecurity) bean)._contractCategory, "contractCategory");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
