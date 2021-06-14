package cdm.product.asset.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.functions.RetrieveBusinessCenterHolidaysImplTest;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.observable.asset.FloatingRateOption;
import cdm.product.asset.*;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.ResetDates;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.CalculationPeriodImplTest;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloatingAmountCalculationTest  extends AbstractFunctionTest {

    @Inject private FloatingAmountCalculation func;
    @Inject private CalculationPeriod calculationPeriod;
    @Inject private DayCountFraction dayCountFraction;

    @Test
    void shouldEvaluateRate() {

        FloatingRateOption fro = IndexValueObservationImplTest.initFro();
        IndexValueObservationImplTest.initIndexData(fro);
        InterestRatePayout interestRatePayout = initInterestPayout(fro, DayCountFractionEnum.ACT_360);
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();
        CalculationPeriodDates calculationPeriodDates = interestRatePayout.getCalculationPeriodDates();

        CalculationPeriodData usingStartDate = calculationPeriod.evaluate(calculationPeriodDates, date(2020, 12, 10));

        assertThat(usingStartDate.getStartDate(), is(date(2020, 12, 10)));
        assertThat(usingStartDate.getEndDate(), is(date(2021, 3, 10)));

        CalculationPeriodData usingAnyDate = calculationPeriod.evaluate(calculationPeriodDates, DateImpl.of(2021, 2, 14));
        CalculationPeriodData usingEndDate = calculationPeriod.evaluate(calculationPeriodDates, DateImpl.of(2021, 3, 9));

        assertThat(usingStartDate, allOf(is(usingAnyDate), is(usingEndDate)));

        assertEquals(BigDecimal.valueOf((31+31+28)/360.0), dayCountFraction.evaluate(interestRatePayout, interestRatePayout.getDayCountFraction().getValue(), date(2020, 12, 10)));


        CalculationPeriodBase calcPeriod = period(date(2020,12,10), date(2021,3,10));
        double expectedYF = (31+31+28)/360.0;
        double expectedRate = 0.0118;
        double expectedNotional = 9_000_000;
        double expected = expectedNotional * expectedRate * expectedYF;
        check(func.evaluate(interestRatePayout, calcPeriod), expectedNotional, expectedRate, expectedYF);

        calcPeriod = period(date(2021,9,29), date(2021,12,29));
        expected = 10_000_000 * 0.01 * (30+31+30)/360.0;
       // check(func.evaluate(interestRatePayout, calcPeriod),  expected);
    }

    private void check(FloatingAmountCalculationDetails result, double expectedNotional, double expectedRate, double expectedYearFrac) {
        double expectedAmount = expectedNotional * expectedRate * expectedYearFrac;
        assertEquals(expectedRate, result.getAppliedRate().doubleValue());
        assertEquals(expectedYearFrac, result.getYearFraction().doubleValue());
        assertEquals(expectedNotional, result.getCalculationPeriodNotionalAmount().getMultiplier().doubleValue(), 0.00001);
        assertEquals(expectedAmount, result.getCalculatedAmount().doubleValue(), 0.00001);
    }

    public static InterestRatePayout initInterestPayout(FloatingRateOption fro, DayCountFractionEnum dcf) {
        FloatingRate rate = GetFloatingRateConditionParametersTest.initFloatingRate(fro);
        ResetDates resetDates = EvaluateTermRateTest.initResetDates(BusinessCenterEnum.EUTA, 3, 2);
        CalculationPeriodDates calculationPeriodDates = initCalculationPeriodDates();

        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setCalculationPeriodDates(calculationPeriodDates)
                .setResetDates(resetDates)
                .setPayoutQuantity(LookupNotionalAmountTest.initNotionalSchedule())
                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRate(GetFloatingRateConditionParametersTest.initFloatingRate(fro)).build())
                .setDayCountFractionValue(DayCountFractionEnum.ACT_360)
                .build();
        return interestRatePayout;
    }

    private static CalculationPeriodDates initCalculationPeriodDates() {
        return CalculationPeriodDates.builder()
                .setEffectiveDate(AdjustableOrRelativeDate.builder()
                        .setAdjustableDate(AdjustableDate.builder()
                                .setUnadjustedDate(DateImpl.of(2020,12, 10))
                                .setDateAdjustments(BusinessDayAdjustments.builder()
                                        .setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                                        .build())
                                .build()))
                .setTerminationDate(AdjustableOrRelativeDate.builder()
                        .setAdjustableDate(AdjustableDate.builder()
                                .setUnadjustedDate(DateImpl.of(2022,3, 10))
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

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }

}
