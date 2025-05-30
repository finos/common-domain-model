package cdm.product.asset.calculation.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.fro.functions.IndexValueObservation;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.asset.floatingrate.FloatingAmountCalculationDetails;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.ResetDates;
import cdm.product.common.schedule.functions.CalculationPeriod;
import com.google.inject.Binder;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.period;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloatingAmountCalculationTest  extends AbstractFunctionTest {

    @Inject private FloatingAmountCalculation func;
    @Inject private CalculationPeriod calculationPeriod;
    @Inject private Create_CalculationPeriodBase createCalculationPeriodBase;
    @Inject private CalculateYearFraction calculateYearFraction;

    @Override
    protected void bindTestingMocks(Binder binder) {
        binder.bind(IndexValueObservation.class).toInstance(initIndexData(initFro()));
    }

    @Test
    void shouldEvaluateRate() {
        InterestRatePayout interestRatePayout = initInterestPayout(initFro(), DayCountFractionEnum.ACT_360);
        CalculationPeriodDates calculationPeriodDates = interestRatePayout.getCalculationPeriodDates();

        CalculationPeriodData usingStartDate = calculationPeriod.evaluate(calculationPeriodDates, Date.of(2020, 12, 10));

        assertThat(usingStartDate.getStartDate(), is(Date.of(2020, 12, 10)));
        assertThat(usingStartDate.getEndDate(), is(Date.of(2021, 3, 10)));

        CalculationPeriodData usingAnyDate = calculationPeriod.evaluate(calculationPeriodDates, Date.of(2021, 2, 14));
        CalculationPeriodData usingEndDate = calculationPeriod.evaluate(calculationPeriodDates, Date.of(2021, 3, 9));

        CalculationPeriodBase period = createCalculationPeriodBase.evaluate(usingStartDate);
        assertThat(usingStartDate, allOf(is(usingAnyDate), is(usingEndDate)));

        assertEquals(BigDecimal.valueOf((31+31+28)/360.0), calculateYearFraction.evaluate(interestRatePayout, interestRatePayout.getDayCountFraction().getValue(), period));


        CalculationPeriodBase calcPeriod = period(Date.of(2020,12,10), Date.of(2021,3,10));
        check(func.evaluate(interestRatePayout, calcPeriod, false, null, null), 9_000_000, 0.0118, (31+31+28)/360.0);
        calcPeriod = period(Date.of(2021,9,10), Date.of(2021,12, 10));
        check(func.evaluate(interestRatePayout, calcPeriod, false, null, null), 12_000_000, 0.015, (30+31+30)/360.0);
    }

    private void check(FloatingAmountCalculationDetails result, double expectedNotional, double expectedRate, double expectedYearFrac) {
        double expectedAmount = expectedNotional * expectedRate * expectedYearFrac;
        assertEquals(expectedRate, result.getAppliedRate().doubleValue());
        assertEquals(expectedYearFrac, result.getYearFraction().doubleValue());
        assertEquals(expectedNotional, result.getCalculationPeriodNotionalAmount().getValue().doubleValue(), 0.00001);
        assertEquals(expectedAmount, result.getCalculatedAmount().doubleValue(), 0.00001);
    }

    private InterestRatePayout initInterestPayout(FloatingRateOption fro, DayCountFractionEnum dcf) {
        ResetDates resetDates = initResetDates(BusinessCenterEnum.EUTA, 3, 2, false);
        CalculationPeriodDates calculationPeriodDates = initCalculationPeriodDates();

        return InterestRatePayout.builder()
                .setCalculationPeriodDates(calculationPeriodDates)
                .setResetDates(resetDates)
                .setPriceQuantity(GetNotionalAmountTest.initNotionalSchedule())
                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRate(initFloatingRate(fro)).build())
                .setDayCountFractionValue(DayCountFractionEnum.ACT_360)
                .build();
    }

    private CalculationPeriodDates initCalculationPeriodDates() {
        return CalculationPeriodDates.builder()
                .setEffectiveDate(AdjustableOrRelativeDate.builder()
                        .setAdjustableDate(AdjustableDate.builder()
                                .setUnadjustedDate(Date.of(2020,12, 10))
                                .setDateAdjustments(BusinessDayAdjustments.builder()
                                        .setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                                        .build())
                                .build()))
                .setTerminationDate(AdjustableOrRelativeDate.builder()
                        .setAdjustableDate(AdjustableDate.builder()
                                .setUnadjustedDate(Date.of(2022,3, 10))
                                .setDateAdjustments(BusinessDayAdjustments.builder()
                                        .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                        .setBusinessCenters(BusinessCenters.builder()
                                                .addBusinessCenter(FieldWithMetaBusinessCenterEnum.builder()
                                                        .setValue(BusinessCenterEnum.EUTA)
                                                        .build())
                                                .build())
                                        .build())
                                .build()))
                .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                        .setPeriodMultiplier(3)
                        .setPeriod(PeriodExtendedEnum.M)
                        .setRollConvention(RollConventionEnum._10)
                        .build())
                .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                         .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                        .setBusinessCenters(BusinessCenters.builder()
                                .addBusinessCenter(FieldWithMetaBusinessCenterEnum.builder()
                                        .setValue(BusinessCenterEnum.EUTA)
                                        .build())
                                .build())
                        .build())

                .build();
    }
}
