/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.util.timeseries.DoubleTimeSeries;
import com.opengamma.util.tuple.DoublesPair;

/**
 * 
 */
public class SumUtils {

  public static Object addValue(Object currentTotal, Object value, String valueName) {
    if (currentTotal == null) {
      return value;
    }
    if (currentTotal.getClass() != value.getClass()) {
      throw new IllegalArgumentException("Inputs have different value types for requirement " + valueName);
    }
    if (value instanceof Double) {
      final Double previousDouble = (Double) currentTotal;
      return previousDouble + (Double) value;
    } else if (value instanceof BigDecimal) {
      final BigDecimal previousDecimal = (BigDecimal) currentTotal;
      return previousDecimal.add((BigDecimal) value);
    } else if (value instanceof DoubleTimeSeries<?>) {
      final DoubleTimeSeries<?> previousTS = (DoubleTimeSeries<?>) currentTotal;
      return previousTS.add((DoubleTimeSeries<?>) value);
    } else if (value instanceof DoubleLabelledMatrix1D) {
      final DoubleLabelledMatrix1D previousMatrix = (DoubleLabelledMatrix1D) currentTotal;
      final DoubleLabelledMatrix1D currentMatrix = (DoubleLabelledMatrix1D) value;
      return previousMatrix.add(currentMatrix);
    } else if (value instanceof LocalDateLabelledMatrix1D) {
      final LocalDateLabelledMatrix1D previousMatrix = (LocalDateLabelledMatrix1D) currentTotal;
      final LocalDateLabelledMatrix1D currentMatrix = (LocalDateLabelledMatrix1D) value;
      return previousMatrix.add(currentMatrix);
    } else if (value instanceof ZonedDateTimeLabelledMatrix1D) {
      final ZonedDateTimeLabelledMatrix1D previousMatrix = (ZonedDateTimeLabelledMatrix1D) currentTotal;
      final ZonedDateTimeLabelledMatrix1D currentMatrix = (ZonedDateTimeLabelledMatrix1D) value;
      return previousMatrix.add(currentMatrix);
    } else if (valueName.equals(ValueRequirementNames.PRESENT_VALUE_CURVE_SENSITIVITY)) { //TODO this should probably not be done like this
      @SuppressWarnings("unchecked")
      final Map<String, List<DoublesPair>> previousMap = (Map<String, List<DoublesPair>>) currentTotal;
      @SuppressWarnings("unchecked")
      final Map<String, List<DoublesPair>> currentMap = (Map<String, List<DoublesPair>>) value;
      final Map<String, List<DoublesPair>> result = new HashMap<String, List<DoublesPair>>();
      for (final String name : previousMap.keySet()) {
        final List<DoublesPair> temp = new ArrayList<DoublesPair>();
        for (final DoublesPair pair : previousMap.get(name)) {
          temp.add(pair);
        }
        if (currentMap.containsKey(name)) {
          for (final DoublesPair pair : currentMap.get(name)) {
            temp.add(pair);
          }
        }
        result.put(name, temp);
      }
      for (final String name : currentMap.keySet()) {
        if (!result.containsKey(name)) {
          final List<DoublesPair> temp = new ArrayList<DoublesPair>();
          for (final DoublesPair pair : currentMap.get(name)) {
            temp.add(pair);
          }
          result.put(name, temp);
        }
      }
    } else if (value instanceof DoubleLabelledMatrix2D) {
      final DoubleLabelledMatrix2D previousMatrix = (DoubleLabelledMatrix2D) currentTotal;
      final DoubleLabelledMatrix2D currentMatrix = (DoubleLabelledMatrix2D) value;
      return previousMatrix.add(currentMatrix, 0.005, 0.005);
    }
    throw new IllegalArgumentException("Can only add Doubles, BigDecimal, DoubleTimeSeries and LabelledMatrix1D (Double, LocalDate and ZonedDateTime), " +
        "or present value curve sensitivities right now.");
  }
  
}
