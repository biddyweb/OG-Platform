/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.var.conditional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.opengamma.financial.var.NormalLinearVaRCalculator;
import com.opengamma.math.function.Function;

/**
 * 
 */
public class NormalLinearConditionalVaRCalculatorTest {
  private static final double PERIODS = 250;
  private static final double HORIZON = 10;
  private static final double QUANTILE = 0.99;
  private static final Function<Double, Double> STD_CALCULATOR = new Function<Double, Double>() {

    @Override
    public Double evaluate(final Double... x) {
      return 0.3;
    }

  };
  private static final NormalLinearConditionalVaRCalculator<Double> CALCULATOR = new NormalLinearConditionalVaRCalculator<Double>(HORIZON, PERIODS, QUANTILE, STD_CALCULATOR);

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeHorizon() {
    new NormalLinearVaRCalculator<Double>(-HORIZON, PERIODS, QUANTILE, STD_CALCULATOR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativePeriod() {
    new NormalLinearVaRCalculator<Double>(HORIZON, -PERIODS, QUANTILE, STD_CALCULATOR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeQuantile() {
    new NormalLinearVaRCalculator<Double>(HORIZON, PERIODS, -QUANTILE, STD_CALCULATOR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHighQuantile() {
    new NormalLinearVaRCalculator<Double>(HORIZON, PERIODS, 1 + QUANTILE, STD_CALCULATOR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCalculator() {
    new NormalLinearVaRCalculator<Double>(HORIZON, PERIODS, QUANTILE, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullData() {
    CALCULATOR.evaluate((Double[]) null);
  }

  @Test
  public void test() {
    assertEquals(CALCULATOR.evaluate(new Double[] {0.}), 0.1599, 1e-4);
  }

  @Test
  public void testObject() {
    assertEquals(CALCULATOR.getHorizon(), HORIZON, 0);
    assertEquals(CALCULATOR.getPeriods(), PERIODS, 0);
    assertEquals(CALCULATOR.getQuantile(), QUANTILE, 0);
    assertEquals(CALCULATOR.getStandardDeviationCalculator(), STD_CALCULATOR);
    NormalLinearConditionalVaRCalculator<Double> other = new NormalLinearConditionalVaRCalculator<Double>(HORIZON, PERIODS, QUANTILE, STD_CALCULATOR);
    assertEquals(CALCULATOR, other);
    assertEquals(CALCULATOR.hashCode(), other.hashCode());
    other = new NormalLinearConditionalVaRCalculator<Double>(HORIZON + 1, PERIODS, QUANTILE, STD_CALCULATOR);
    assertFalse(CALCULATOR.equals(other));
    other = new NormalLinearConditionalVaRCalculator<Double>(HORIZON, PERIODS + 1, QUANTILE, STD_CALCULATOR);
    assertFalse(CALCULATOR.equals(other));
    other = new NormalLinearConditionalVaRCalculator<Double>(HORIZON, PERIODS, QUANTILE * 0.5, STD_CALCULATOR);
    assertFalse(CALCULATOR.equals(other));
    other = new NormalLinearConditionalVaRCalculator<Double>(HORIZON, PERIODS, QUANTILE, new Function<Double, Double>() {

      @Override
      public Double evaluate(final Double... x) {
        return null;
      }

    });
  }
}
