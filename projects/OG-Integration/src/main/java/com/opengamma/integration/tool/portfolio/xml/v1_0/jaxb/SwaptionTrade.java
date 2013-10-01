/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.tool.portfolio.xml.v1_0.jaxb;

import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

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
import org.threeten.bp.LocalDate;

import com.opengamma.integration.tool.portfolio.xml.v1_0.conversion.SwaptionTradeSecurityExtractor;
import com.opengamma.integration.tool.portfolio.xml.v1_0.conversion.TradeSecurityExtractor;
import com.opengamma.util.money.Currency;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@BeanDefinition
public class SwaptionTrade extends Trade {

  @XmlElement(name = "buySell", required = true)
  @PropertyDefinition
  private BuySell _buySell;

  @XmlElementWrapper(name = "paymentCalendars")
  @XmlElement(name = "calendar")
  @PropertyDefinition
  private Set<Calendar> _paymentCalendars;

  @XmlElementWrapper(name = "exerciseCalendars")
  @XmlElement(name = "calendar")
  @PropertyDefinition
  private Set<Calendar> _exerciseCalendars;

  @XmlElement(name = "expirationDate", required = true)
  @PropertyDefinition
  private LocalDate _expirationDate;

  @XmlElement(name = "exerciseType", required = true)
  @PropertyDefinition
  private ExerciseType _exerciseType;

/*
 <!--    In case of bermudan we need to specify the dates, so it is similar to a swap:
          Maybe we can turn this "schedule builders" into an object if it makes it easier
          If our data model does not support bermudans, this next block can be ignored-->
      <frequency>12m</frequency>
      <businessDayConvention>Modified Following</businessDayConvention>
      <scheduleGenerationDirection>Backward</scheduleGenerationDirection>
      <generationRule>EOM</generationRule>
*/

  @XmlElement(name = "stubPeriodType")
  @PropertyDefinition
  private StubPeriodType _stubPeriodType;

  @XmlElement(name = "settlementType")
  @PropertyDefinition
  private SettlementType _settlementType;

  @XmlElement(name = "cashSettlementCalculationMethod")
  @PropertyDefinition
  private CashSettlementCalculationMethod _cashSettlementCalculationMethod;

  @XmlElement(name = "cashSettlementPaymentDate")
  @PropertyDefinition
  private LocalDate _cashSettlementPaymentDate;

  @XmlElement(name = "cashSettlementCurrency")
  @PropertyDefinition
  private Currency _cashSettlementCurrency;

  @XmlElement(name = "underlyingSwapTrade")
  @PropertyDefinition
  private SwapTrade _underlyingSwapTrade;

  @Override
  public boolean canBePositionAggregated() {
    return false;
  }

  @Override
  public TradeSecurityExtractor getSecurityExtractor() {
    return new SwaptionTradeSecurityExtractor(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SwaptionTrade}.
   * @return the meta-bean, not null
   */
  public static SwaptionTrade.Meta meta() {
    return SwaptionTrade.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SwaptionTrade.Meta.INSTANCE);
  }

  @Override
  public SwaptionTrade.Meta metaBean() {
    return SwaptionTrade.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the buySell.
   * @return the value of the property
   */
  public BuySell getBuySell() {
    return _buySell;
  }

  /**
   * Sets the buySell.
   * @param buySell  the new value of the property
   */
  public void setBuySell(BuySell buySell) {
    this._buySell = buySell;
  }

  /**
   * Gets the the {@code buySell} property.
   * @return the property, not null
   */
  public final Property<BuySell> buySell() {
    return metaBean().buySell().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the paymentCalendars.
   * @return the value of the property
   */
  public Set<Calendar> getPaymentCalendars() {
    return _paymentCalendars;
  }

  /**
   * Sets the paymentCalendars.
   * @param paymentCalendars  the new value of the property
   */
  public void setPaymentCalendars(Set<Calendar> paymentCalendars) {
    this._paymentCalendars = paymentCalendars;
  }

  /**
   * Gets the the {@code paymentCalendars} property.
   * @return the property, not null
   */
  public final Property<Set<Calendar>> paymentCalendars() {
    return metaBean().paymentCalendars().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exerciseCalendars.
   * @return the value of the property
   */
  public Set<Calendar> getExerciseCalendars() {
    return _exerciseCalendars;
  }

  /**
   * Sets the exerciseCalendars.
   * @param exerciseCalendars  the new value of the property
   */
  public void setExerciseCalendars(Set<Calendar> exerciseCalendars) {
    this._exerciseCalendars = exerciseCalendars;
  }

  /**
   * Gets the the {@code exerciseCalendars} property.
   * @return the property, not null
   */
  public final Property<Set<Calendar>> exerciseCalendars() {
    return metaBean().exerciseCalendars().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expirationDate.
   * @return the value of the property
   */
  public LocalDate getExpirationDate() {
    return _expirationDate;
  }

  /**
   * Sets the expirationDate.
   * @param expirationDate  the new value of the property
   */
  public void setExpirationDate(LocalDate expirationDate) {
    this._expirationDate = expirationDate;
  }

  /**
   * Gets the the {@code expirationDate} property.
   * @return the property, not null
   */
  public final Property<LocalDate> expirationDate() {
    return metaBean().expirationDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exerciseType.
   * @return the value of the property
   */
  public ExerciseType getExerciseType() {
    return _exerciseType;
  }

  /**
   * Sets the exerciseType.
   * @param exerciseType  the new value of the property
   */
  public void setExerciseType(ExerciseType exerciseType) {
    this._exerciseType = exerciseType;
  }

  /**
   * Gets the the {@code exerciseType} property.
   * @return the property, not null
   */
  public final Property<ExerciseType> exerciseType() {
    return metaBean().exerciseType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the stubPeriodType.
   * @return the value of the property
   */
  public StubPeriodType getStubPeriodType() {
    return _stubPeriodType;
  }

  /**
   * Sets the stubPeriodType.
   * @param stubPeriodType  the new value of the property
   */
  public void setStubPeriodType(StubPeriodType stubPeriodType) {
    this._stubPeriodType = stubPeriodType;
  }

  /**
   * Gets the the {@code stubPeriodType} property.
   * @return the property, not null
   */
  public final Property<StubPeriodType> stubPeriodType() {
    return metaBean().stubPeriodType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the settlementType.
   * @return the value of the property
   */
  public SettlementType getSettlementType() {
    return _settlementType;
  }

  /**
   * Sets the settlementType.
   * @param settlementType  the new value of the property
   */
  public void setSettlementType(SettlementType settlementType) {
    this._settlementType = settlementType;
  }

  /**
   * Gets the the {@code settlementType} property.
   * @return the property, not null
   */
  public final Property<SettlementType> settlementType() {
    return metaBean().settlementType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cashSettlementCalculationMethod.
   * @return the value of the property
   */
  public CashSettlementCalculationMethod getCashSettlementCalculationMethod() {
    return _cashSettlementCalculationMethod;
  }

  /**
   * Sets the cashSettlementCalculationMethod.
   * @param cashSettlementCalculationMethod  the new value of the property
   */
  public void setCashSettlementCalculationMethod(CashSettlementCalculationMethod cashSettlementCalculationMethod) {
    this._cashSettlementCalculationMethod = cashSettlementCalculationMethod;
  }

  /**
   * Gets the the {@code cashSettlementCalculationMethod} property.
   * @return the property, not null
   */
  public final Property<CashSettlementCalculationMethod> cashSettlementCalculationMethod() {
    return metaBean().cashSettlementCalculationMethod().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cashSettlementPaymentDate.
   * @return the value of the property
   */
  public LocalDate getCashSettlementPaymentDate() {
    return _cashSettlementPaymentDate;
  }

  /**
   * Sets the cashSettlementPaymentDate.
   * @param cashSettlementPaymentDate  the new value of the property
   */
  public void setCashSettlementPaymentDate(LocalDate cashSettlementPaymentDate) {
    this._cashSettlementPaymentDate = cashSettlementPaymentDate;
  }

  /**
   * Gets the the {@code cashSettlementPaymentDate} property.
   * @return the property, not null
   */
  public final Property<LocalDate> cashSettlementPaymentDate() {
    return metaBean().cashSettlementPaymentDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cashSettlementCurrency.
   * @return the value of the property
   */
  public Currency getCashSettlementCurrency() {
    return _cashSettlementCurrency;
  }

  /**
   * Sets the cashSettlementCurrency.
   * @param cashSettlementCurrency  the new value of the property
   */
  public void setCashSettlementCurrency(Currency cashSettlementCurrency) {
    this._cashSettlementCurrency = cashSettlementCurrency;
  }

  /**
   * Gets the the {@code cashSettlementCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> cashSettlementCurrency() {
    return metaBean().cashSettlementCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlyingSwapTrade.
   * @return the value of the property
   */
  public SwapTrade getUnderlyingSwapTrade() {
    return _underlyingSwapTrade;
  }

  /**
   * Sets the underlyingSwapTrade.
   * @param underlyingSwapTrade  the new value of the property
   */
  public void setUnderlyingSwapTrade(SwapTrade underlyingSwapTrade) {
    this._underlyingSwapTrade = underlyingSwapTrade;
  }

  /**
   * Gets the the {@code underlyingSwapTrade} property.
   * @return the property, not null
   */
  public final Property<SwapTrade> underlyingSwapTrade() {
    return metaBean().underlyingSwapTrade().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public SwaptionTrade clone() {
    return (SwaptionTrade) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SwaptionTrade other = (SwaptionTrade) obj;
      return JodaBeanUtils.equal(getBuySell(), other.getBuySell()) &&
          JodaBeanUtils.equal(getPaymentCalendars(), other.getPaymentCalendars()) &&
          JodaBeanUtils.equal(getExerciseCalendars(), other.getExerciseCalendars()) &&
          JodaBeanUtils.equal(getExpirationDate(), other.getExpirationDate()) &&
          JodaBeanUtils.equal(getExerciseType(), other.getExerciseType()) &&
          JodaBeanUtils.equal(getStubPeriodType(), other.getStubPeriodType()) &&
          JodaBeanUtils.equal(getSettlementType(), other.getSettlementType()) &&
          JodaBeanUtils.equal(getCashSettlementCalculationMethod(), other.getCashSettlementCalculationMethod()) &&
          JodaBeanUtils.equal(getCashSettlementPaymentDate(), other.getCashSettlementPaymentDate()) &&
          JodaBeanUtils.equal(getCashSettlementCurrency(), other.getCashSettlementCurrency()) &&
          JodaBeanUtils.equal(getUnderlyingSwapTrade(), other.getUnderlyingSwapTrade()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getBuySell());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPaymentCalendars());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExerciseCalendars());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExpirationDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExerciseType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getStubPeriodType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSettlementType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCashSettlementCalculationMethod());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCashSettlementPaymentDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCashSettlementCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getUnderlyingSwapTrade());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(384);
    buf.append("SwaptionTrade{");
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
    buf.append("buySell").append('=').append(getBuySell()).append(',').append(' ');
    buf.append("paymentCalendars").append('=').append(getPaymentCalendars()).append(',').append(' ');
    buf.append("exerciseCalendars").append('=').append(getExerciseCalendars()).append(',').append(' ');
    buf.append("expirationDate").append('=').append(getExpirationDate()).append(',').append(' ');
    buf.append("exerciseType").append('=').append(getExerciseType()).append(',').append(' ');
    buf.append("stubPeriodType").append('=').append(getStubPeriodType()).append(',').append(' ');
    buf.append("settlementType").append('=').append(getSettlementType()).append(',').append(' ');
    buf.append("cashSettlementCalculationMethod").append('=').append(getCashSettlementCalculationMethod()).append(',').append(' ');
    buf.append("cashSettlementPaymentDate").append('=').append(getCashSettlementPaymentDate()).append(',').append(' ');
    buf.append("cashSettlementCurrency").append('=').append(getCashSettlementCurrency()).append(',').append(' ');
    buf.append("underlyingSwapTrade").append('=').append(getUnderlyingSwapTrade()).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SwaptionTrade}.
   */
  public static class Meta extends Trade.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code buySell} property.
     */
    private final MetaProperty<BuySell> _buySell = DirectMetaProperty.ofReadWrite(
        this, "buySell", SwaptionTrade.class, BuySell.class);
    /**
     * The meta-property for the {@code paymentCalendars} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<Calendar>> _paymentCalendars = DirectMetaProperty.ofReadWrite(
        this, "paymentCalendars", SwaptionTrade.class, (Class) Set.class);
    /**
     * The meta-property for the {@code exerciseCalendars} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Set<Calendar>> _exerciseCalendars = DirectMetaProperty.ofReadWrite(
        this, "exerciseCalendars", SwaptionTrade.class, (Class) Set.class);
    /**
     * The meta-property for the {@code expirationDate} property.
     */
    private final MetaProperty<LocalDate> _expirationDate = DirectMetaProperty.ofReadWrite(
        this, "expirationDate", SwaptionTrade.class, LocalDate.class);
    /**
     * The meta-property for the {@code exerciseType} property.
     */
    private final MetaProperty<ExerciseType> _exerciseType = DirectMetaProperty.ofReadWrite(
        this, "exerciseType", SwaptionTrade.class, ExerciseType.class);
    /**
     * The meta-property for the {@code stubPeriodType} property.
     */
    private final MetaProperty<StubPeriodType> _stubPeriodType = DirectMetaProperty.ofReadWrite(
        this, "stubPeriodType", SwaptionTrade.class, StubPeriodType.class);
    /**
     * The meta-property for the {@code settlementType} property.
     */
    private final MetaProperty<SettlementType> _settlementType = DirectMetaProperty.ofReadWrite(
        this, "settlementType", SwaptionTrade.class, SettlementType.class);
    /**
     * The meta-property for the {@code cashSettlementCalculationMethod} property.
     */
    private final MetaProperty<CashSettlementCalculationMethod> _cashSettlementCalculationMethod = DirectMetaProperty.ofReadWrite(
        this, "cashSettlementCalculationMethod", SwaptionTrade.class, CashSettlementCalculationMethod.class);
    /**
     * The meta-property for the {@code cashSettlementPaymentDate} property.
     */
    private final MetaProperty<LocalDate> _cashSettlementPaymentDate = DirectMetaProperty.ofReadWrite(
        this, "cashSettlementPaymentDate", SwaptionTrade.class, LocalDate.class);
    /**
     * The meta-property for the {@code cashSettlementCurrency} property.
     */
    private final MetaProperty<Currency> _cashSettlementCurrency = DirectMetaProperty.ofReadWrite(
        this, "cashSettlementCurrency", SwaptionTrade.class, Currency.class);
    /**
     * The meta-property for the {@code underlyingSwapTrade} property.
     */
    private final MetaProperty<SwapTrade> _underlyingSwapTrade = DirectMetaProperty.ofReadWrite(
        this, "underlyingSwapTrade", SwaptionTrade.class, SwapTrade.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "buySell",
        "paymentCalendars",
        "exerciseCalendars",
        "expirationDate",
        "exerciseType",
        "stubPeriodType",
        "settlementType",
        "cashSettlementCalculationMethod",
        "cashSettlementPaymentDate",
        "cashSettlementCurrency",
        "underlyingSwapTrade");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 244977400:  // buySell
          return _buySell;
        case -299417201:  // paymentCalendars
          return _paymentCalendars;
        case 1844059645:  // exerciseCalendars
          return _exerciseCalendars;
        case -668811523:  // expirationDate
          return _expirationDate;
        case -466331342:  // exerciseType
          return _exerciseType;
        case -1686629399:  // stubPeriodType
          return _stubPeriodType;
        case -295448573:  // settlementType
          return _settlementType;
        case 74953646:  // cashSettlementCalculationMethod
          return _cashSettlementCalculationMethod;
        case -1222573960:  // cashSettlementPaymentDate
          return _cashSettlementPaymentDate;
        case -1757113107:  // cashSettlementCurrency
          return _cashSettlementCurrency;
        case -1771846476:  // underlyingSwapTrade
          return _underlyingSwapTrade;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends SwaptionTrade> builder() {
      return new DirectBeanBuilder<SwaptionTrade>(new SwaptionTrade());
    }

    @Override
    public Class<? extends SwaptionTrade> beanType() {
      return SwaptionTrade.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code buySell} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BuySell> buySell() {
      return _buySell;
    }

    /**
     * The meta-property for the {@code paymentCalendars} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Set<Calendar>> paymentCalendars() {
      return _paymentCalendars;
    }

    /**
     * The meta-property for the {@code exerciseCalendars} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Set<Calendar>> exerciseCalendars() {
      return _exerciseCalendars;
    }

    /**
     * The meta-property for the {@code expirationDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> expirationDate() {
      return _expirationDate;
    }

    /**
     * The meta-property for the {@code exerciseType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExerciseType> exerciseType() {
      return _exerciseType;
    }

    /**
     * The meta-property for the {@code stubPeriodType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<StubPeriodType> stubPeriodType() {
      return _stubPeriodType;
    }

    /**
     * The meta-property for the {@code settlementType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SettlementType> settlementType() {
      return _settlementType;
    }

    /**
     * The meta-property for the {@code cashSettlementCalculationMethod} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CashSettlementCalculationMethod> cashSettlementCalculationMethod() {
      return _cashSettlementCalculationMethod;
    }

    /**
     * The meta-property for the {@code cashSettlementPaymentDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LocalDate> cashSettlementPaymentDate() {
      return _cashSettlementPaymentDate;
    }

    /**
     * The meta-property for the {@code cashSettlementCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> cashSettlementCurrency() {
      return _cashSettlementCurrency;
    }

    /**
     * The meta-property for the {@code underlyingSwapTrade} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SwapTrade> underlyingSwapTrade() {
      return _underlyingSwapTrade;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 244977400:  // buySell
          return ((SwaptionTrade) bean).getBuySell();
        case -299417201:  // paymentCalendars
          return ((SwaptionTrade) bean).getPaymentCalendars();
        case 1844059645:  // exerciseCalendars
          return ((SwaptionTrade) bean).getExerciseCalendars();
        case -668811523:  // expirationDate
          return ((SwaptionTrade) bean).getExpirationDate();
        case -466331342:  // exerciseType
          return ((SwaptionTrade) bean).getExerciseType();
        case -1686629399:  // stubPeriodType
          return ((SwaptionTrade) bean).getStubPeriodType();
        case -295448573:  // settlementType
          return ((SwaptionTrade) bean).getSettlementType();
        case 74953646:  // cashSettlementCalculationMethod
          return ((SwaptionTrade) bean).getCashSettlementCalculationMethod();
        case -1222573960:  // cashSettlementPaymentDate
          return ((SwaptionTrade) bean).getCashSettlementPaymentDate();
        case -1757113107:  // cashSettlementCurrency
          return ((SwaptionTrade) bean).getCashSettlementCurrency();
        case -1771846476:  // underlyingSwapTrade
          return ((SwaptionTrade) bean).getUnderlyingSwapTrade();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 244977400:  // buySell
          ((SwaptionTrade) bean).setBuySell((BuySell) newValue);
          return;
        case -299417201:  // paymentCalendars
          ((SwaptionTrade) bean).setPaymentCalendars((Set<Calendar>) newValue);
          return;
        case 1844059645:  // exerciseCalendars
          ((SwaptionTrade) bean).setExerciseCalendars((Set<Calendar>) newValue);
          return;
        case -668811523:  // expirationDate
          ((SwaptionTrade) bean).setExpirationDate((LocalDate) newValue);
          return;
        case -466331342:  // exerciseType
          ((SwaptionTrade) bean).setExerciseType((ExerciseType) newValue);
          return;
        case -1686629399:  // stubPeriodType
          ((SwaptionTrade) bean).setStubPeriodType((StubPeriodType) newValue);
          return;
        case -295448573:  // settlementType
          ((SwaptionTrade) bean).setSettlementType((SettlementType) newValue);
          return;
        case 74953646:  // cashSettlementCalculationMethod
          ((SwaptionTrade) bean).setCashSettlementCalculationMethod((CashSettlementCalculationMethod) newValue);
          return;
        case -1222573960:  // cashSettlementPaymentDate
          ((SwaptionTrade) bean).setCashSettlementPaymentDate((LocalDate) newValue);
          return;
        case -1757113107:  // cashSettlementCurrency
          ((SwaptionTrade) bean).setCashSettlementCurrency((Currency) newValue);
          return;
        case -1771846476:  // underlyingSwapTrade
          ((SwaptionTrade) bean).setUnderlyingSwapTrade((SwapTrade) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
