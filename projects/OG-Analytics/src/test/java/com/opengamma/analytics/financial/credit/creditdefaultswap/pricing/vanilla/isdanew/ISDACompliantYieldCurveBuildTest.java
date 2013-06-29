/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.creditdefaultswap.pricing.vanilla.isdanew;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import com.opengamma.analytics.financial.credit.isdayieldcurve.ISDAInstrumentTypes;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.util.ArgumentChecker;

/**
 * 
 */
public class ISDACompliantYieldCurveBuildTest {

  private static final DayCount ACT365 = DayCountFactory.INSTANCE.getDayCount("ACT/365");
  private static final DayCount ACT360 = DayCountFactory.INSTANCE.getDayCount("ACT/360");
  private static final DayCount D30360 = DayCountFactory.INSTANCE.getDayCount("30/360");
  private static final DayCount ACT_ACT = DayCountFactory.INSTANCE.getDayCount("Actual/Actual ISDA");

  private static final Calendar DEFAULT_CALENDAR = new MondayToFridayCalendar("Weekend_Only");
  private static final BusinessDayConvention MOD_FOLLOWING = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Modified Following");

  @SuppressWarnings("unused")
  @Test
  public void Test() {
    final boolean print = false;
    if (print) {
      System.out.println("ISDACompliantYieldCurveBuildTest: print should be false for commit");
    }

    // date from ISDA excel
    double[] sampleTimes = new double[] {0.0767123287671233, 0.167123287671233, 0.249315068493151, 0.498630136986301, 0.747945205479452, 0.997260273972603, 1.4958904109589, 1.99452054794521,
        2.5013698630137, 3.0027397260274, 3.5041095890411, 4.0027397260274, 4.5041095890411, 5.0027397260274, 5.5041095890411, 6.0027397260274, 6.5013698630137, 7, 7.50684931506849, 8.00547945205479,
        8.50684931506849, 9.00547945205479, 9.50684931506849, 10.0054794520548, 10.5068493150685, 11.0082191780822, 11.5068493150685, 12.0054794520548, 12.5041095890411, 13.0027397260274,
        13.5095890410959, 14.0082191780822, 14.5095890410959, 15.0109589041096, 15.5123287671233, 16.0109589041096, 16.5123287671233, 17.0109589041096, 17.5095890410959, 18.0082191780822,
        18.5068493150685, 19.013698630137, 19.5150684931507, 20.013698630137, 20.5150684931507, 21.013698630137, 21.5150684931507, 22.013698630137, 22.5150684931507, 23.013698630137,
        23.5123287671233, 24.0109589041096, 24.5178082191781, 25.0164383561644, 25.5178082191781, 26.0164383561644, 26.5178082191781, 27.0191780821918, 27.5205479452055, 28.0191780821918,
        28.5178082191781, 29.0164383561644, 29.5150684931507, 30.013698630137 };
    double[] zeroRates = new double[] {0.00344732957665484, 0.00645427070262317, 0.010390833731528, 0.0137267241507424, 0.016406009142171, 0.0206548075787697, 0.0220059788254565, 0.0226815644487997,
        0.0241475224808774, 0.0251107341245228, 0.0263549710022889, 0.0272832610741453, 0.0294785565070328, 0.0312254350680597, 0.0340228731758456, 0.0363415444446394, 0.0364040719835966,
        0.0364576914896066, 0.0398713425199977, 0.0428078389323812, 0.0443206903065534, 0.0456582004054368, 0.0473373527805339, 0.0488404232471453, 0.0496433764260127, 0.0503731885238783,
        0.0510359350109291, 0.0516436290741354, 0.0526405492486405, 0.0535610094687589, 0.05442700569164, 0.0552178073994544, 0.0559581527041068, 0.0566490425640605, 0.0572429526830672,
        0.0577967261153023, 0.0583198210222109, 0.0588094750567186, 0.0592712408001043, 0.0597074348516306, 0.0601201241459759, 0.0605174325075768, 0.0608901411604128, 0.0612422922398251,
        0.0618707980423834, 0.0624661234885966, 0.0630368977571603, 0.0635787665840882, 0.064099413535239, 0.0645947156962813, 0.0650690099353217, 0.0655236050526131, 0.0659667431709796,
        0.0663851731522577, 0.0668735344788778, 0.0673405584796377, 0.0677924400667054, 0.0682275513575991, 0.0686468089170376, 0.0690488939824011, 0.0694369182384849, 0.06981160656508,
        0.0701736348572483, 0.0705236340943412 };

    final ISDACompliantYieldCurveBuild bob = new ISDACompliantYieldCurveBuild();
    final LocalDate spotDate = LocalDate.of(2013, 5, 31);

    final int nMoneyMarket = 6;
    final int nSwaps = 14;
    final int nInstruments = nMoneyMarket + nSwaps;

    final ISDAInstrumentTypes[] types = new ISDAInstrumentTypes[nInstruments];
    Period[] tenors = new Period[nInstruments];
    final int[] mmMonths = new int[] {1, 2, 3, 6, 9, 12 };
    final int[] swapYears = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 20, 25, 30 };
    // check
    ArgumentChecker.isTrue(mmMonths.length == nMoneyMarket, "mmMonths");
    ArgumentChecker.isTrue(swapYears.length == nSwaps, "swapYears");

    for (int i = 0; i < nMoneyMarket; i++) {
      types[i] = ISDAInstrumentTypes.MoneyMarket;
      tenors[i] = Period.ofMonths(mmMonths[i]);
    }
    for (int i = nMoneyMarket; i < nInstruments; i++) {
      types[i] = ISDAInstrumentTypes.Swap;
      tenors[i] = Period.ofYears(swapYears[i - nMoneyMarket]);
    }

    final double[] rates = new double[] {0.00340055550701297, 0.00636929056400781, 0.0102617798438113, 0.0135851258907251, 0.0162809551414651, 0.020583125112332, 0.0227369218210212,
        0.0251978805237614, 0.0273223815467694, 0.0310882447627048, 0.0358397743454067, 0.036047665095421, 0.0415916567616181, 0.044066373237682, 0.046708518178509, 0.0491196954851753,
        0.0529297239911766, 0.0562025436376854, 0.0589772202773522, 0.0607471217692999 };

    final DayCount moneyMarketDCC = ACT360;
    final DayCount swapDCC = D30360;
    final DayCount curveDCC = ACT365;
    final Period swapInterval = Period.ofMonths(6);

    ISDACompliantCurve hc = bob.build(spotDate, types, tenors, rates, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);

    final int nCurvePoints = hc.getNumberOfKnots();
    assertEquals(nInstruments, nCurvePoints);
    final int nSamplePoints = sampleTimes.length;
    for (int i = 0; i < nSamplePoints; i++) {
      double time = sampleTimes[i];
      double zr = hc.getZeroRate(time);
      assertEquals("time:" + time, zeroRates[i], zr, 1e-10);
    }

    if (print) {
      for (int i = 0; i < nCurvePoints; i++) {
        System.out.println(hc.getTimeAtIndex(i) + "\t" + hc.getZeroRateAtIndex(i));
      }
    }

    //TODO extract timing to another test
    final int warmup = 1;
    final int hotSpot = 0;

    for (int i = 0; i < warmup; i++) {
      ISDACompliantCurve hc1 = bob.build(spotDate, types, tenors, rates, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);
    }

    if (hotSpot > 0) {
      long t0 = System.nanoTime();
      for (int i = 0; i < warmup; i++) {
        ISDACompliantCurve hc1 = bob.build(spotDate, types, tenors, rates, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);
      }
      System.out.println("time to build yield curve: " + (System.nanoTime() - t0) / 1e6 / hotSpot + "ms");
    }

  }

  /**
   * 
   */
  @Test
  public void differentSpotDatesTest() {

    double[] sampleTimes = new double[] {0.0767123287671233, 0.167123287671233, 0.249315068493151, 0.498630136986301, 0.747945205479452, 0.997260273972603, 1.4958904109589, 1.99452054794521,
        2.5013698630137, 3.0027397260274, 3.5041095890411, 4.0027397260274, 4.5041095890411, 5.0027397260274, 5.5041095890411, 6.0027397260274, 6.5013698630137, 7, 7.50684931506849, 8.00547945205479,
        8.50684931506849, 9.00547945205479, 9.50684931506849, 10.0054794520548, 10.5068493150685, 11.0082191780822, 11.5068493150685, 12.0054794520548, 12.5041095890411, 13.0027397260274,
        13.5095890410959, 14.0082191780822, 14.5095890410959, 15.0109589041096, 15.5123287671233, 16.0109589041096, 16.5123287671233, 17.0109589041096, 17.5095890410959, 18.0082191780822,
        18.5068493150685, 19.013698630137, 19.5150684931507, 20.013698630137, 20.5150684931507, 21.013698630137, 21.5150684931507, 22.013698630137, 22.5150684931507, 23.013698630137,
        23.5123287671233, 24.0109589041096, 24.5178082191781, 25.0164383561644, 25.5178082191781, 26.0164383561644, 26.5178082191781, 27.0191780821918, 27.5205479452055, 28.0191780821918,
        28.5178082191781, 29.0164383561644, 29.5150684931507, 30.013698630137 };

    final ISDACompliantYieldCurveBuild bob = new ISDACompliantYieldCurveBuild();
    final LocalDate[] spotDate = new LocalDate[] {LocalDate.of(2007, 5, 21), LocalDate.of(2013, 5, 31), LocalDate.of(2015, 1, 29), LocalDate.of(2023, 11, 29) };
    final int nDates = spotDate.length;

    final int nMoneyMarket = 6;
    final int nSwaps = 14;
    final int nInstruments = nMoneyMarket + nSwaps;

    final ISDAInstrumentTypes[] types = new ISDAInstrumentTypes[nInstruments];
    Period[] tenors = new Period[nInstruments];
    final int[] mmMonths = new int[] {1, 2, 3, 6, 9, 12 };
    final int[] swapYears = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 20, 25, 30 };
    // check
    ArgumentChecker.isTrue(mmMonths.length == nMoneyMarket, "mmMonths");
    ArgumentChecker.isTrue(swapYears.length == nSwaps, "swapYears");

    for (int i = 0; i < nMoneyMarket; i++) {
      types[i] = ISDAInstrumentTypes.MoneyMarket;
      tenors[i] = Period.ofMonths(mmMonths[i]);
    }
    for (int i = nMoneyMarket; i < nInstruments; i++) {
      types[i] = ISDAInstrumentTypes.Swap;
      tenors[i] = Period.ofYears(swapYears[i - nMoneyMarket]);
    }

    final double[] rates = new double[] {0.00340055550701297, 0.00636929056400781, 0.0102617798438113, 0.0135851258907251, 0.0162809551414651, 0.020583125112332, 0.0227369218210212,
        0.0251978805237614, 0.0273223815467694, 0.0310882447627048, 0.0358397743454067, 0.036047665095421, 0.0415916567616181, 0.044066373237682, 0.046708518178509, 0.0491196954851753,
        0.0529297239911766, 0.0562025436376854, 0.0589772202773522, 0.0607471217692999 };

    final DayCount moneyMarketDCC = ACT360;
    final DayCount swapDCC = D30360;
    final DayCount curveDCC = ACT365;
    final Period swapInterval = Period.ofMonths(6);
    final int nSamplePoints = sampleTimes.length;

    final double[][] zr = new double[nSamplePoints][nDates];
    for (int k = 0; k < nDates; ++k) {
      ISDACompliantCurve hc = bob.build(spotDate[k], types, tenors, rates, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);

      final int nCurvePoints = hc.getNumberOfKnots();
      assertEquals(nInstruments, nCurvePoints);
      for (int i = 0; i < nSamplePoints; i++) {
        double time = sampleTimes[i];
        zr[i][k] = hc.getZeroRate(time);
      }
    }

    for (int i = 0; i < nSamplePoints - 1; ++i) {
      for (int k = 0; k < nDates - 1; ++k) {
        assertEquals(zr[i][k], zr[i][k + 1], 1.e-3);
        assertTrue(zr[i + 1][k] > zr[i][k]);
      }
    }
  }

  /**
   * 
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void OverlappingInstrumentsTest() {
    final ISDACompliantYieldCurveBuild bob = new ISDACompliantYieldCurveBuild();
    final LocalDate spotDate = LocalDate.of(2013, 5, 31);

    final int nMoneyMarket = 6;
    final int nSwaps = 14;
    final int nInstruments = nMoneyMarket + nSwaps;

    final ISDAInstrumentTypes[] types = new ISDAInstrumentTypes[nInstruments];
    Period[] tenors = new Period[nInstruments];
    final int[] mmMonths = new int[] {1, 2, 3, 6, 9, 12 };
    final int[] swapYears = new int[] {1, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 20, 25, 30 };
    // check
    ArgumentChecker.isTrue(mmMonths.length == nMoneyMarket, "mmMonths");
    ArgumentChecker.isTrue(swapYears.length == nSwaps, "swapYears");

    for (int i = 0; i < nMoneyMarket; i++) {
      types[i] = ISDAInstrumentTypes.MoneyMarket;
      tenors[i] = Period.ofMonths(mmMonths[i]);
    }
    for (int i = nMoneyMarket; i < nInstruments; i++) {
      types[i] = ISDAInstrumentTypes.Swap;
      tenors[i] = Period.ofYears(swapYears[i - nMoneyMarket]);
    }

    final double[] rates = new double[] {0.00340055550701297, 0.00636929056400781, 0.0102617798438113, 0.0135851258907251, 0.0162809551414651, 0.020583125112332, 0.0227369218210212,
        0.0251978805237614, 0.0273223815467694, 0.0310882447627048, 0.0358397743454067, 0.036047665095421, 0.0415916567616181, 0.044066373237682, 0.046708518178509, 0.0491196954851753,
        0.0529297239911766, 0.0562025436376854, 0.0589772202773522, 0.0607471217692999 };

    final DayCount moneyMarketDCC = ACT360;
    final DayCount swapDCC = D30360;
    final DayCount curveDCC = ACT365;
    final Period swapInterval = Period.ofMonths(6);

    bob.build(spotDate, types, tenors, rates, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);
  }

  /**
   * 
   */
  @Test
  public void dayCountTest() {

    double[] sampleTimes = new double[] {0.0767123287671233, 0.167123287671233, 0.249315068493151, 0.498630136986301, 0.747945205479452, 0.997260273972603, 1.4958904109589, 1.99452054794521,
        2.5013698630137, 3.0027397260274, 3.5041095890411, 4.0027397260274, 4.5041095890411, 5.0027397260274, 5.5041095890411, 6.0027397260274, 6.5013698630137, 7, 7.50684931506849, 8.00547945205479,
        8.50684931506849, 9.00547945205479, 9.50684931506849, 10.0054794520548, 10.5068493150685, 11.0082191780822, 11.5068493150685, 12.0054794520548, 12.5041095890411, 13.0027397260274,
        13.5095890410959, 14.0082191780822, 14.5095890410959, 15.0109589041096, 15.5123287671233, 16.0109589041096, 16.5123287671233, 17.0109589041096, 17.5095890410959, 18.0082191780822,
        18.5068493150685, 19.013698630137, 19.5150684931507, 20.013698630137, 20.5150684931507, 21.013698630137, 21.5150684931507, 22.013698630137, 22.5150684931507, 23.013698630137,
        23.5123287671233, 24.0109589041096, 24.5178082191781, 25.0164383561644, 25.5178082191781, 26.0164383561644, 26.5178082191781, 27.0191780821918, 27.5205479452055, 28.0191780821918,
        28.5178082191781, 29.0164383561644, 29.5150684931507, 30.013698630137 };
    double[] zeroRates = new double[] {0.00344732957665484, 0.00645427070262317, 0.010390833731528, 0.0137267241507424, 0.016406009142171, 0.0206548075787697, 0.0220059788254565, 0.0226815644487997,
        0.0241475224808774, 0.0251107341245228, 0.0263549710022889, 0.0272832610741453, 0.0294785565070328, 0.0312254350680597, 0.0340228731758456, 0.0363415444446394, 0.0364040719835966,
        0.0364576914896066, 0.0398713425199977, 0.0428078389323812, 0.0443206903065534, 0.0456582004054368, 0.0473373527805339, 0.0488404232471453, 0.0496433764260127, 0.0503731885238783,
        0.0510359350109291, 0.0516436290741354, 0.0526405492486405, 0.0535610094687589, 0.05442700569164, 0.0552178073994544, 0.0559581527041068, 0.0566490425640605, 0.0572429526830672,
        0.0577967261153023, 0.0583198210222109, 0.0588094750567186, 0.0592712408001043, 0.0597074348516306, 0.0601201241459759, 0.0605174325075768, 0.0608901411604128, 0.0612422922398251,
        0.0618707980423834, 0.0624661234885966, 0.0630368977571603, 0.0635787665840882, 0.064099413535239, 0.0645947156962813, 0.0650690099353217, 0.0655236050526131, 0.0659667431709796,
        0.0663851731522577, 0.0668735344788778, 0.0673405584796377, 0.0677924400667054, 0.0682275513575991, 0.0686468089170376, 0.0690488939824011, 0.0694369182384849, 0.06981160656508,
        0.0701736348572483, 0.0705236340943412 };

    final ISDACompliantYieldCurveBuild bob = new ISDACompliantYieldCurveBuild();
    final LocalDate spotDate = LocalDate.of(2013, 5, 31);

    final int nMoneyMarket = 6;
    final int nSwaps = 14;
    final int nInstruments = nMoneyMarket + nSwaps;

    final ISDAInstrumentTypes[] types = new ISDAInstrumentTypes[nInstruments];
    Period[] tenors = new Period[nInstruments];
    final int[] mmMonths = new int[] {1, 2, 3, 6, 9, 12 };
    final int[] swapYears = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 20, 25, 30 };
    // check
    ArgumentChecker.isTrue(mmMonths.length == nMoneyMarket, "mmMonths");
    ArgumentChecker.isTrue(swapYears.length == nSwaps, "swapYears");

    for (int i = 0; i < nMoneyMarket; i++) {
      types[i] = ISDAInstrumentTypes.MoneyMarket;
      tenors[i] = Period.ofMonths(mmMonths[i]);
    }
    for (int i = nMoneyMarket; i < nInstruments; i++) {
      types[i] = ISDAInstrumentTypes.Swap;
      tenors[i] = Period.ofYears(swapYears[i - nMoneyMarket]);
    }

    final double[] rates = new double[] {0.00340055550701297, 0.00636929056400781, 0.0102617798438113, 0.0135851258907251, 0.0162809551414651, 0.020583125112332, 0.0227369218210212,
        0.0251978805237614, 0.0273223815467694, 0.0310882447627048, 0.0358397743454067, 0.036047665095421, 0.0415916567616181, 0.044066373237682, 0.046708518178509, 0.0491196954851753,
        0.0529297239911766, 0.0562025436376854, 0.0589772202773522, 0.0607471217692999 };

    final DayCount[] moneyMarketDCC = new DayCount[] {ACT360, D30360, ACT365, ACT_ACT };
    final DayCount[] swapDCC = new DayCount[] {ACT360, D30360, ACT365, ACT_ACT };
    final DayCount[] curveDCC = new DayCount[] {ACT360, D30360, ACT365, ACT_ACT };
    final Period swapInterval = Period.ofMonths(6);

    for (int ii = 0; ii < 4; ++ii) {
      for (int jj = 0; jj < 4; ++jj) {
        for (int kk = 0; kk < 4; ++kk) {
          ISDACompliantCurve hc = bob.build(spotDate, types, tenors, rates, moneyMarketDCC[ii], swapDCC[ii], swapInterval, curveDCC[ii], MOD_FOLLOWING);

          final int nCurvePoints = hc.getNumberOfKnots();
          assertEquals(nInstruments, nCurvePoints);
          final int nSamplePoints = sampleTimes.length;
          double ref = 0.;
          for (int i = 0; i < nSamplePoints; i++) {
            double time = sampleTimes[i];
            double zr = hc.getZeroRate(time);
            assertTrue(zr >= ref);
            ref = zr;
            assertEquals(zeroRates[i], zr, 1.e-3);
          }
        }
      }
    }
  }

  /**
   * 
   */
  @Test
  public void onlyMoneyOrSwapTest() {

    // date from ISDA excel
    double[] sampleTimes = new double[] {0.0767123287671233, 0.167123287671233, 0.249315068493151, 0.498630136986301, 0.747945205479452, 0.997260273972603, 1.4958904109589, 1.99452054794521,
        2.5013698630137, 3.0027397260274, 3.5041095890411, 4.0027397260274, 4.5041095890411, 5.0027397260274, 5.5041095890411, 6.0027397260274, 6.5013698630137, 7, 7.50684931506849, 8.00547945205479,
        8.50684931506849, 9.00547945205479, 9.50684931506849, 10.0054794520548, 10.5068493150685, 11.0082191780822, 11.5068493150685, 12.0054794520548, 12.5041095890411, 13.0027397260274,
        13.5095890410959, 14.0082191780822, 14.5095890410959, 15.0109589041096, 15.5123287671233, 16.0109589041096, 16.5123287671233, 17.0109589041096, 17.5095890410959, 18.0082191780822,
        18.5068493150685, 19.013698630137, 19.5150684931507, 20.013698630137, 20.5150684931507, 21.013698630137, 21.5150684931507, 22.013698630137, 22.5150684931507, 23.013698630137,
        23.5123287671233, 24.0109589041096, 24.5178082191781, 25.0164383561644, 25.5178082191781, 26.0164383561644, 26.5178082191781, 27.0191780821918, 27.5205479452055, 28.0191780821918,
        28.5178082191781, 29.0164383561644, 29.5150684931507, 30.013698630137 };
    double[] zeroRates = new double[] {0.00344732957665484, 0.00645427070262317, 0.010390833731528, 0.0137267241507424, 0.016406009142171, 0.0206548075787697, 0.0220059788254565, 0.0226815644487997,
        0.0241475224808774, 0.0251107341245228, 0.0263549710022889, 0.0272832610741453, 0.0294785565070328, 0.0312254350680597, 0.0340228731758456, 0.0363415444446394, 0.0364040719835966,
        0.0364576914896066, 0.0398713425199977, 0.0428078389323812, 0.0443206903065534, 0.0456582004054368, 0.0473373527805339, 0.0488404232471453, 0.0496433764260127, 0.0503731885238783,
        0.0510359350109291, 0.0516436290741354, 0.0526405492486405, 0.0535610094687589, 0.05442700569164, 0.0552178073994544, 0.0559581527041068, 0.0566490425640605, 0.0572429526830672,
        0.0577967261153023, 0.0583198210222109, 0.0588094750567186, 0.0592712408001043, 0.0597074348516306, 0.0601201241459759, 0.0605174325075768, 0.0608901411604128, 0.0612422922398251,
        0.0618707980423834, 0.0624661234885966, 0.0630368977571603, 0.0635787665840882, 0.064099413535239, 0.0645947156962813, 0.0650690099353217, 0.0655236050526131, 0.0659667431709796,
        0.0663851731522577, 0.0668735344788778, 0.0673405584796377, 0.0677924400667054, 0.0682275513575991, 0.0686468089170376, 0.0690488939824011, 0.0694369182384849, 0.06981160656508,
        0.0701736348572483, 0.0705236340943412 };

    final ISDACompliantYieldCurveBuild bob = new ISDACompliantYieldCurveBuild();
    final LocalDate spotDate = LocalDate.of(2013, 5, 31);
    final DayCount moneyMarketDCC = ACT360;
    final DayCount swapDCC = D30360;
    final DayCount curveDCC = ACT365;
    final Period swapInterval = Period.ofMonths(6);

    final int nMoneyMarket1 = 0;
    final int nSwaps1 = 14;
    final int nInstruments1 = nMoneyMarket1 + nSwaps1;

    final ISDAInstrumentTypes[] types1 = new ISDAInstrumentTypes[nInstruments1];
    Period[] tenors1 = new Period[nInstruments1];
    final int[] mmMonths1 = new int[] {};
    final int[] swapYears1 = new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 20, 25, 30 };
    // check
    ArgumentChecker.isTrue(mmMonths1.length == nMoneyMarket1, "mmMonths");
    ArgumentChecker.isTrue(swapYears1.length == nSwaps1, "swapYears");
    for (int i = 0; i < nMoneyMarket1; i++) {
      types1[i] = ISDAInstrumentTypes.MoneyMarket;
      tenors1[i] = Period.ofMonths(mmMonths1[i]);
    }
    for (int i = nMoneyMarket1; i < nInstruments1; i++) {
      types1[i] = ISDAInstrumentTypes.Swap;
      tenors1[i] = Period.ofYears(swapYears1[i - nMoneyMarket1]);
    }

    final int nMoneyMarket2 = 6;
    final int nSwaps2 = 0;
    final int nInstruments2 = nMoneyMarket2 + nSwaps2;

    final ISDAInstrumentTypes[] types2 = new ISDAInstrumentTypes[nInstruments2];
    Period[] tenors2 = new Period[nInstruments2];
    final int[] mmMonths2 = new int[] {1, 2, 3, 6, 9, 12 };
    final int[] swapYears2 = new int[] {};
    // check
    ArgumentChecker.isTrue(mmMonths2.length == nMoneyMarket2, "mmMonths");
    ArgumentChecker.isTrue(swapYears2.length == nSwaps2, "swapYears");
    for (int i = 0; i < nMoneyMarket2; i++) {
      types2[i] = ISDAInstrumentTypes.MoneyMarket;
      tenors2[i] = Period.ofMonths(mmMonths2[i]);
    }
    for (int i = nMoneyMarket2; i < nInstruments2; i++) {
      types2[i] = ISDAInstrumentTypes.Swap;
      tenors2[i] = Period.ofYears(swapYears2[i - nMoneyMarket2]);
    }

    final double[] rates1 = new double[] {0.0227369218210212, 0.0251978805237614, 0.0273223815467694, 0.0310882447627048, 0.0358397743454067, 0.036047665095421, 0.0415916567616181, 0.044066373237682,
        0.046708518178509, 0.0491196954851753, 0.0529297239911766, 0.0562025436376854, 0.0589772202773522, 0.0607471217692999 };
    final double[] rates2 = new double[] {0.00340055550701297, 0.00636929056400781, 0.0102617798438113, 0.0135851258907251, 0.0162809551414651, 0.020583125112332 };

    ISDACompliantCurve hc1 = bob.build(spotDate, types1, tenors1, rates1, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);
    final int nCurvePoints1 = hc1.getNumberOfKnots();
    assertEquals(nInstruments1, nCurvePoints1);
    ISDACompliantCurve hc2 = bob.build(spotDate, types2, tenors2, rates2, moneyMarketDCC, swapDCC, swapInterval, curveDCC, MOD_FOLLOWING);
    final int nCurvePoints2 = hc2.getNumberOfKnots();
    assertEquals(nInstruments2, nCurvePoints2);

    double ref1 = 0.;
    double ref2 = 0.;
    final int nSamplePoints = sampleTimes.length;
    for (int i = 0; i < nSamplePoints; i++) {
      double time = sampleTimes[i];
      double zr1 = hc1.getZeroRate(time);
      double zr2 = hc2.getZeroRate(time);
      if (time < 1.) {
        assertEquals(zeroRates[i], zr2, 1e-10);
        if (i > 0) {
          assertTrue(zr1 == ref1);
        }
      }
      assertTrue(zr1 >= ref1);
      assertTrue(zr2 >= ref2);
      if (time > 1.) {
        assertTrue(zr2 == ref2);
      }
      ref1 = zr1;
      ref2 = zr2;
    }
  }
}