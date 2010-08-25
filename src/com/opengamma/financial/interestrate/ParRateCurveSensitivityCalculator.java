/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.interestrate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.Validate;

import com.opengamma.financial.interestrate.annuity.definition.Annuity;
import com.opengamma.financial.interestrate.annuity.definition.ConstantCouponAnnuity;
import com.opengamma.financial.interestrate.annuity.definition.FixedAnnuity;
import com.opengamma.financial.interestrate.annuity.definition.VariableAnnuity;
import com.opengamma.financial.interestrate.bond.definition.Bond;
import com.opengamma.financial.interestrate.cash.definition.Cash;
import com.opengamma.financial.interestrate.fra.definition.ForwardRateAgreement;
import com.opengamma.financial.interestrate.future.definition.InterestRateFuture;
import com.opengamma.financial.interestrate.swap.definition.BasisSwap;
import com.opengamma.financial.interestrate.swap.definition.FixedFloatSwap;
import com.opengamma.financial.interestrate.swap.definition.FloatingRateNote;
import com.opengamma.financial.interestrate.swap.definition.Swap;
import com.opengamma.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.util.CompareUtils;
import com.opengamma.util.tuple.DoublesPair;

/**
 * 
 */
public final class ParRateCurveSensitivityCalculator implements InterestRateDerivativeVisitor<Map<String, List<DoublesPair>>> {

  private final PresentValueCalculator _pvCalculator = PresentValueCalculator.getInstance();
  private final PresentValueSensitivityCalculator _pvSenseCalculator = PresentValueSensitivityCalculator.getInstance();

  private static final ParRateCurveSensitivityCalculator s_instance = new ParRateCurveSensitivityCalculator();

  public static ParRateCurveSensitivityCalculator getInstance() {
    return s_instance;
  }

  private ParRateCurveSensitivityCalculator() {
  }

  @Override
  public Map<String, List<DoublesPair>> getValue(final InterestRateDerivative instrument, final YieldCurveBundle curves) {
    Validate.notNull(instrument);
    Validate.notNull(curves);
    return instrument.accept(this, curves);
  }

  @Override
  public Map<String, List<DoublesPair>> visitCash(final Cash cash, final YieldCurveBundle curves) {
    final String curveName = cash.getYieldCurveName();
    final YieldAndDiscountCurve curve = curves.getCurve(curveName);
    final double ta = cash.getTradeTime();
    final double tb = cash.getPaymentTime();
    final double yearFrac = cash.getYearFraction();
    final Map<String, List<DoublesPair>> result = new HashMap<String, List<DoublesPair>>();
    final List<DoublesPair> temp = new ArrayList<DoublesPair>();
    if (yearFrac == 0.0) {
      if (!CompareUtils.closeEquals(ta, tb, 1e-16)) {
        throw new IllegalArgumentException("year fraction is zero, but payment time not equal the trade time");
      }
      temp.add(new DoublesPair(ta, 1.0));
    } else {
      final double ratio = curve.getDiscountFactor(ta) / curve.getDiscountFactor(tb) / yearFrac;
      temp.add(new DoublesPair(ta, -ta * ratio));
      temp.add(new DoublesPair(tb, tb * ratio));
    }
    result.put(curveName, temp);
    return result;
  }

  @Override
  public Map<String, List<DoublesPair>> visitForwardRateAgreement(final ForwardRateAgreement fra, final YieldCurveBundle curves) {
    final String curveName = fra.getLiborCurveName();
    final YieldAndDiscountCurve curve = curves.getCurve(curveName);
    final double ta = fra.getFixingDate();
    final double tb = fra.getMaturity();
    final double delta = fra.getForwardYearFraction();
    final double ratio = curve.getDiscountFactor(ta) / curve.getDiscountFactor(tb) / delta;
    final DoublesPair s1 = new DoublesPair(ta, -ta * ratio);
    final DoublesPair s2 = new DoublesPair(tb, tb * ratio);
    final List<DoublesPair> temp = new ArrayList<DoublesPair>();
    temp.add(s1);
    temp.add(s2);
    final Map<String, List<DoublesPair>> result = new HashMap<String, List<DoublesPair>>();
    result.put(curveName, temp);
    return result;
  }

  @Override
  public Map<String, List<DoublesPair>> visitInterestRateFuture(final InterestRateFuture future, final YieldCurveBundle curves) {
    final String curveName = future.getCurveName();
    final YieldAndDiscountCurve curve = curves.getCurve(curveName);
    final double ta = future.getFixingDate();
    final double tb = future.getMaturity();
    final double ratio = curve.getDiscountFactor(ta) / curve.getDiscountFactor(tb) / future.getIndexYearFraction();
    final DoublesPair s1 = new DoublesPair(ta, -ta * ratio);
    final DoublesPair s2 = new DoublesPair(tb, tb * ratio);
    final List<DoublesPair> temp = new ArrayList<DoublesPair>();
    temp.add(s1);
    temp.add(s2);
    final Map<String, List<DoublesPair>> result = new HashMap<String, List<DoublesPair>>();
    result.put(curveName, temp);
    return result;
  }

  @Override
  public Map<String, List<DoublesPair>> visitSwap(final Swap swap, final YieldCurveBundle curves) {
    final Annuity payLeg = swap.getPayLeg().withZeroSpread();
    final Annuity receiveLeg = swap.getReceiveLeg().withZeroSpread();
    final Annuity spreadLeg = swap.getReceiveLeg().withUnitCoupons();

    final double a = _pvCalculator.getValue(receiveLeg, curves);
    final double b = _pvCalculator.getValue(payLeg, curves);
    final double c = _pvCalculator.getValue(spreadLeg, curves);

    final Map<String, List<DoublesPair>> senseA = _pvSenseCalculator.getValue(receiveLeg, curves);
    final Map<String, List<DoublesPair>> senseB = _pvSenseCalculator.getValue(payLeg, curves);
    final Map<String, List<DoublesPair>> senseC = _pvSenseCalculator.getValue(spreadLeg, curves);
    final Map<String, List<DoublesPair>> result = new HashMap<String, List<DoublesPair>>();

    final double factor = -(b - a) / c / c;

    for (final String name : curves.getAllNames()) {
      boolean flag = false;
      final List<DoublesPair> temp = new ArrayList<DoublesPair>();
      if (senseA.containsKey(name)) {
        flag = true;
        for (final DoublesPair pair : senseA.get(name)) {
          temp.add(new DoublesPair(pair.getFirst(), -pair.getSecond() / c));
        }
      }
      if (senseB.containsKey(name)) {
        flag = true;
        for (final DoublesPair pair : senseB.get(name)) {
          temp.add(new DoublesPair(pair.getFirst(), pair.getSecond() / c));
        }
      }
      if (senseC.containsKey(name)) {
        flag = true;
        for (final DoublesPair pair : senseC.get(name)) {
          temp.add(new DoublesPair(pair.getFirst(), factor * pair.getSecond()));
        }
      }
      if (flag) {
        result.put(name, temp);
      }
    }
    return result;
  }

  @Override
  public Map<String, List<DoublesPair>> visitFixedFloatSwap(final FixedFloatSwap swap, final YieldCurveBundle curves) {
    final FixedAnnuity tempAnnuity = swap.getFixedLeg().withUnitCoupons();
    final double a = _pvCalculator.getValue(tempAnnuity, curves);
    final double b = _pvCalculator.getValue(swap.getFloatingLeg(), curves);
    final double bOveraSq = b / a / a;
    final Map<String, List<DoublesPair>> senseA = _pvSenseCalculator.getValue(tempAnnuity, curves);
    final Map<String, List<DoublesPair>> senseB = _pvSenseCalculator.getValue(swap.getFloatingLeg(), curves);

    final Map<String, List<DoublesPair>> result = new HashMap<String, List<DoublesPair>>();
    for (final String name : curves.getAllNames()) {
      boolean flag = false;
      final List<DoublesPair> temp = new ArrayList<DoublesPair>();
      if (senseA.containsKey(name)) {
        flag = true;
        for (final DoublesPair pair : senseA.get(name)) {
          final double t = pair.getFirst();
          final DoublesPair newPair = new DoublesPair(t, -bOveraSq * pair.getSecond());
          temp.add(newPair);
        }
      }
      if (senseB.containsKey(name)) {
        flag = true;
        for (final DoublesPair pair : senseB.get(name)) {
          final double t = pair.getFirst();
          final DoublesPair newPair = new DoublesPair(t, pair.getSecond() / a);
          temp.add(newPair);
        }
      }
      if (flag) {
        result.put(name, temp);
      }
    }
    return result;
  }

  @Override
  public Map<String, List<DoublesPair>> visitBasisSwap(final BasisSwap swap, final YieldCurveBundle curves) {
    return visitSwap(swap, curves);
  }

  @Override
  public Map<String, List<DoublesPair>> visitFloatingRateNote(final FloatingRateNote frn, final YieldCurveBundle curves) {
    return visitSwap(frn, curves);
  }

  @Override
  public Map<String, List<DoublesPair>> visitBond(final Bond bond, final YieldCurveBundle curves) {
    final YieldAndDiscountCurve curve = curves.getCurve(bond.getCurveName());
    final FixedAnnuity ann = bond.getFixedAnnuity().withUnitCoupons();
    final double a = _pvCalculator.getValue(ann, curves);
    final Map<String, List<DoublesPair>> senseA = _pvSenseCalculator.getValue(ann, curves);
    final Map<String, List<DoublesPair>> result = new HashMap<String, List<DoublesPair>>();

    final double maturity = bond.getMaturity();
    final double df = curve.getDiscountFactor(maturity);
    final double factor = -(1 - df) / a / a;

    for (final String name : curves.getAllNames()) {
      if (senseA.containsKey(name)) {
        final List<DoublesPair> temp = new ArrayList<DoublesPair>();
        final List<DoublesPair> list = senseA.get(name);
        final int n = list.size();
        for (int i = 0; i < (n - 1); i++) {
          final DoublesPair pair = list.get(i);
          temp.add(new DoublesPair(pair.getFirst(), factor * pair.getSecond()));
        }
        final DoublesPair pair = list.get(n - 1);
        temp.add(new DoublesPair(pair.getFirst(), maturity * df / a + factor * pair.getSecond()));
        result.put(name, temp);
      }
    }
    return result;
  }

  @Override
  public Map<String, List<DoublesPair>> visitFixedAnnuity(final FixedAnnuity annuity, final YieldCurveBundle curves) {
    throw new NotImplementedException();
  }

  @Override
  public Map<String, List<DoublesPair>> visitVariableAnnuity(final VariableAnnuity annuity, final YieldCurveBundle curves) {
    throw new NotImplementedException();
  }

  @Override
  public Map<String, List<DoublesPair>> visitConstantCouponAnnuity(final ConstantCouponAnnuity annuity, final YieldCurveBundle curves) {
    throw new NotImplementedException();
  }

}
