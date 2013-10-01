/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve.strips;

import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.id.ExternalId;
import com.opengamma.util.ArgumentChecker;

/** Contains a curve node and the information necessary to get market data from the engine. */
@BeanDefinition
public class CurveNodeWithIdentifier extends DirectBean implements Comparable<CurveNodeWithIdentifier> {

  /** The node. */
  @PropertyDefinition(validate = "notNull")
  private CurveNode _curveNode;
  /** The market data id. */
  @PropertyDefinition(validate = "notNull")
  private ExternalId _identifier;
  /** The data field id. */
  @PropertyDefinition(validate = "notNull")
  private String _dataField;
  /** The data field type. */
  @PropertyDefinition(validate = "notNull")
  private DataFieldType _fieldType;

  /**
   * Creates an instance.
   * 
   * @param node  the curve node, not null
   * @param id  the market data id, not null
   * @param dataField  the data field, not null
   * @param fieldType  the field type, not null
   */
  public CurveNodeWithIdentifier(final CurveNode node,
                                 final ExternalId id,
                                 final String dataField,
                                 final DataFieldType fieldType) {
    ArgumentChecker.notNull(node, "node");
    ArgumentChecker.notNull(id, "id");
    ArgumentChecker.notNull(dataField, "data field");
    ArgumentChecker.notNull(fieldType, "field type");
    _curveNode = node;
    _identifier = id;
    _dataField = dataField;
    _fieldType = fieldType;
  }

  /**
   * Creates an instance.
   */
  protected CurveNodeWithIdentifier() {
  }

  //-------------------------------------------------------------------------
  @Override
  public int compareTo(final CurveNodeWithIdentifier o) {
    int result = _curveNode.compareTo(o._curveNode);
    if (result != 0) {
      return result;
    }
    result = _identifier.getValue().compareTo(o._identifier.getValue());
    if (result != 0) {
      return result;
    }
    result = _dataField.compareTo(o._dataField);
    if (result != 0) {
      return result;
    }
    return _fieldType.compareTo(o._fieldType);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CurveNodeWithIdentifier}.
   * @return the meta-bean, not null
   */
  public static CurveNodeWithIdentifier.Meta meta() {
    return CurveNodeWithIdentifier.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CurveNodeWithIdentifier.Meta.INSTANCE);
  }

  @Override
  public CurveNodeWithIdentifier.Meta metaBean() {
    return CurveNodeWithIdentifier.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the node.
   * @return the value of the property, not null
   */
  public CurveNode getCurveNode() {
    return _curveNode;
  }

  /**
   * Sets the node.
   * @param curveNode  the new value of the property, not null
   */
  public void setCurveNode(CurveNode curveNode) {
    JodaBeanUtils.notNull(curveNode, "curveNode");
    this._curveNode = curveNode;
  }

  /**
   * Gets the the {@code curveNode} property.
   * @return the property, not null
   */
  public final Property<CurveNode> curveNode() {
    return metaBean().curveNode().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the market data id.
   * @return the value of the property, not null
   */
  public ExternalId getIdentifier() {
    return _identifier;
  }

  /**
   * Sets the market data id.
   * @param identifier  the new value of the property, not null
   */
  public void setIdentifier(ExternalId identifier) {
    JodaBeanUtils.notNull(identifier, "identifier");
    this._identifier = identifier;
  }

  /**
   * Gets the the {@code identifier} property.
   * @return the property, not null
   */
  public final Property<ExternalId> identifier() {
    return metaBean().identifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data field id.
   * @return the value of the property, not null
   */
  public String getDataField() {
    return _dataField;
  }

  /**
   * Sets the data field id.
   * @param dataField  the new value of the property, not null
   */
  public void setDataField(String dataField) {
    JodaBeanUtils.notNull(dataField, "dataField");
    this._dataField = dataField;
  }

  /**
   * Gets the the {@code dataField} property.
   * @return the property, not null
   */
  public final Property<String> dataField() {
    return metaBean().dataField().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data field type.
   * @return the value of the property, not null
   */
  public DataFieldType getFieldType() {
    return _fieldType;
  }

  /**
   * Sets the data field type.
   * @param fieldType  the new value of the property, not null
   */
  public void setFieldType(DataFieldType fieldType) {
    JodaBeanUtils.notNull(fieldType, "fieldType");
    this._fieldType = fieldType;
  }

  /**
   * Gets the the {@code fieldType} property.
   * @return the property, not null
   */
  public final Property<DataFieldType> fieldType() {
    return metaBean().fieldType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public CurveNodeWithIdentifier clone() {
    BeanBuilder<? extends CurveNodeWithIdentifier> builder = metaBean().builder();
    for (MetaProperty<?> mp : metaBean().metaPropertyIterable()) {
      if (mp.style().isBuildable()) {
        Object value = mp.get(this);
        if (value instanceof Bean) {
          value = ((Bean) value).clone();
        }
        builder.set(mp.name(), value);
      }
    }
    return builder.build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CurveNodeWithIdentifier other = (CurveNodeWithIdentifier) obj;
      return JodaBeanUtils.equal(getCurveNode(), other.getCurveNode()) &&
          JodaBeanUtils.equal(getIdentifier(), other.getIdentifier()) &&
          JodaBeanUtils.equal(getDataField(), other.getDataField()) &&
          JodaBeanUtils.equal(getFieldType(), other.getFieldType());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getCurveNode());
    hash += hash * 31 + JodaBeanUtils.hashCode(getIdentifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(getDataField());
    hash += hash * 31 + JodaBeanUtils.hashCode(getFieldType());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("CurveNodeWithIdentifier{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  protected void toString(StringBuilder buf) {
    buf.append("curveNode").append('=').append(getCurveNode()).append(',').append(' ');
    buf.append("identifier").append('=').append(getIdentifier()).append(',').append(' ');
    buf.append("dataField").append('=').append(getDataField()).append(',').append(' ');
    buf.append("fieldType").append('=').append(getFieldType()).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CurveNodeWithIdentifier}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code curveNode} property.
     */
    private final MetaProperty<CurveNode> _curveNode = DirectMetaProperty.ofReadWrite(
        this, "curveNode", CurveNodeWithIdentifier.class, CurveNode.class);
    /**
     * The meta-property for the {@code identifier} property.
     */
    private final MetaProperty<ExternalId> _identifier = DirectMetaProperty.ofReadWrite(
        this, "identifier", CurveNodeWithIdentifier.class, ExternalId.class);
    /**
     * The meta-property for the {@code dataField} property.
     */
    private final MetaProperty<String> _dataField = DirectMetaProperty.ofReadWrite(
        this, "dataField", CurveNodeWithIdentifier.class, String.class);
    /**
     * The meta-property for the {@code fieldType} property.
     */
    private final MetaProperty<DataFieldType> _fieldType = DirectMetaProperty.ofReadWrite(
        this, "fieldType", CurveNodeWithIdentifier.class, DataFieldType.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "curveNode",
        "identifier",
        "dataField",
        "fieldType");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 771167121:  // curveNode
          return _curveNode;
        case -1618432855:  // identifier
          return _identifier;
        case -386794640:  // dataField
          return _dataField;
        case 1265211220:  // fieldType
          return _fieldType;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends CurveNodeWithIdentifier> builder() {
      return new DirectBeanBuilder<CurveNodeWithIdentifier>(new CurveNodeWithIdentifier());
    }

    @Override
    public Class<? extends CurveNodeWithIdentifier> beanType() {
      return CurveNodeWithIdentifier.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code curveNode} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CurveNode> curveNode() {
      return _curveNode;
    }

    /**
     * The meta-property for the {@code identifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExternalId> identifier() {
      return _identifier;
    }

    /**
     * The meta-property for the {@code dataField} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> dataField() {
      return _dataField;
    }

    /**
     * The meta-property for the {@code fieldType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<DataFieldType> fieldType() {
      return _fieldType;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 771167121:  // curveNode
          return ((CurveNodeWithIdentifier) bean).getCurveNode();
        case -1618432855:  // identifier
          return ((CurveNodeWithIdentifier) bean).getIdentifier();
        case -386794640:  // dataField
          return ((CurveNodeWithIdentifier) bean).getDataField();
        case 1265211220:  // fieldType
          return ((CurveNodeWithIdentifier) bean).getFieldType();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 771167121:  // curveNode
          ((CurveNodeWithIdentifier) bean).setCurveNode((CurveNode) newValue);
          return;
        case -1618432855:  // identifier
          ((CurveNodeWithIdentifier) bean).setIdentifier((ExternalId) newValue);
          return;
        case -386794640:  // dataField
          ((CurveNodeWithIdentifier) bean).setDataField((String) newValue);
          return;
        case 1265211220:  // fieldType
          ((CurveNodeWithIdentifier) bean).setFieldType((DataFieldType) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((CurveNodeWithIdentifier) bean)._curveNode, "curveNode");
      JodaBeanUtils.notNull(((CurveNodeWithIdentifier) bean)._identifier, "identifier");
      JodaBeanUtils.notNull(((CurveNodeWithIdentifier) bean)._dataField, "dataField");
      JodaBeanUtils.notNull(((CurveNodeWithIdentifier) bean)._fieldType, "fieldType");
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
