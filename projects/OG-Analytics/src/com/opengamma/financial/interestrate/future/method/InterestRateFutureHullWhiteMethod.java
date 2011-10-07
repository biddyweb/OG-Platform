/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.interestrate.future.method;

import com.opengamma.financial.interestrate.InterestRateDerivative;
import com.opengamma.financial.interestrate.PresentValueSensitivity;
import com.opengamma.financial.interestrate.YieldCurveBundle;
import com.opengamma.financial.interestrate.future.definition.InterestRateFuture;
import com.opengamma.financial.model.interestrate.HullWhiteOneFactorPiecewiseConstantInterestRateModel;
import com.opengamma.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.financial.model.interestrate.definition.HullWhiteOneFactorPiecewiseConstantDataBundle;
import com.opengamma.util.money.CurrencyAmount;
import com.opengamma.util.tuple.DoublesPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * Method to compute the price for an interest rate future with convexity adjustment from a Hull-White one factor model.
 */
public class InterestRateFutureHullWhiteMethod extends InterestRateFutureMethod {

  /**
   * The Hull-White model.
   */
  private static final HullWhiteOneFactorPiecewiseConstantInterestRateModel MODEL = new HullWhiteOneFactorPiecewiseConstantInterestRateModel();

  /**
   * Computes the price of a future from the curves using an estimation of the future rate without convexity adjustment.
   * @param future The future.
   * @param curves The Hull-White parameters and the curves.
   * @return The price.
   */
  public double price(final InterestRateFuture future, final HullWhiteOneFactorPiecewiseConstantDataBundle curves) {
    Validate.notNull(future, "Future");
    Validate.notNull(curves, "Curves");
    final YieldAndDiscountCurve forwardCurve = curves.getCurve(future.getForwardCurveName());
    double dfForwardStart = forwardCurve.getDiscountFactor(future.getFixingPeriodStartTime());
    double dfForwardEnd = forwardCurve.getDiscountFactor(future.getFixingPeriodEndTime());
    double forward = (dfForwardStart / dfForwardEnd - 1) / future.getFixingPeriodAccrualFactor();
    double futureConvexityFactor = MODEL.futureConvexityFactor(future.getLastTradingTime(), future.getFixingPeriodStartTime(), future.getFixingPeriodEndTime(), curves.getHullWhiteParameter());
    double price = 1.0 - futureConvexityFactor * forward + (1 - futureConvexityFactor) / future.getFixingPeriodAccrualFactor();
    return price;
  }

  public CurrencyAmount presentValue(final InterestRateFuture future, final HullWhiteOneFactorPiecewiseConstantDataBundle curves) {
    final double pv = presentValueFromPrice(future, price(future, curves));
    return CurrencyAmount.of(future.getCurrency(), pv);
  }

  @Override
  public CurrencyAmount presentValue(final InterestRateDerivative instrument, final YieldCurveBundle curves) {
    Validate.isTrue(instrument instanceof InterestRateFuture, "Interest rate future");
    Validate.isTrue(curves instanceof HullWhiteOneFactorPiecewiseConstantDataBundle, "Bundle should contain Hull-White data");
    return presentValue((InterestRateFuture) instrument, (HullWhiteOneFactorPiecewiseConstantDataBundle) curves);
  }

  /**
   * Compute the price sensitivity to rates of a interest rate future by discounting.
   * @param future The future.
   * @param curves The Hull-White parameters and the curves.
   * @return The price rate sensitivity.
   */
  public PresentValueSensitivity priceCurveSensitivity(final InterestRateFuture future, final HullWhiteOneFactorPiecewiseConstantDataBundle curves) {
    Validate.notNull(future, "Future");
    Validate.notNull(curves, "Curves");
    final YieldAndDiscountCurve forwardCurve = curves.getCurve(future.getForwardCurveName());
    double dfForwardStart = forwardCurve.getDiscountFactor(future.getFixingPeriodStartTime());
    double dfForwardEnd = forwardCurve.getDiscountFactor(future.getFixingPeriodEndTime());
    double futureConvexityFactor = MODEL.futureConvexityFactor(future.getLastTradingTime(), future.getFixingPeriodStartTime(), future.getFixingPeriodEndTime(), curves.getHullWhiteParameter());
    // Backward sweep
    double priceBar = 1.0;
    double forwardBar = -futureConvexityFactor * priceBar;
    double dfForwardEndBar = -dfForwardStart / (dfForwardEnd * dfForwardEnd) / future.getFixingPeriodAccrualFactor() * forwardBar;
    double dfForwardStartBar = 1.0 / (future.getFixingPeriodAccrualFactor() * dfForwardEnd) * forwardBar;
    Map<String, List<DoublesPair>> resultMap = new HashMap<String, List<DoublesPair>>();
    List<DoublesPair> listForward = new ArrayList<DoublesPair>();
    listForward.add(new DoublesPair(future.getFixingPeriodStartTime(), -future.getFixingPeriodStartTime() * dfForwardStart * dfForwardStartBar));
    listForward.add(new DoublesPair(future.getFixingPeriodEndTime(), -future.getFixingPeriodEndTime() * dfForwardEnd * dfForwardEndBar));
    resultMap.put(future.getForwardCurveName(), listForward);
    PresentValueSensitivity result = new PresentValueSensitivity(resultMap);
    return result;
  }

  @Override
  public PresentValueSensitivity priceCurveSensitivity(InterestRateFuture future, YieldCurveBundle curves) {
    Validate.isTrue(curves instanceof HullWhiteOneFactorPiecewiseConstantDataBundle, "Bundle should contain Hull-White data");
    return priceCurveSensitivity(future, (HullWhiteOneFactorPiecewiseConstantDataBundle) curves);
  }

}
