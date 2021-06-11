package cdm.product.asset.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.functions.RetrieveBusinessCenterHolidaysImplTest;
import cdm.observable.asset.FloatingRateOption;
import cdm.product.asset.*;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloatingAmountCalculationTest  extends AbstractFunctionTest {

    @Inject
    private FloatingAmountCalculation func;

    @Test
    void shouldEvaluateRate() {

        FloatingRateOption fro = IndexValueObservationImplTest.initFro();
        IndexValueObservationImplTest.initIndexData(fro);
        InterestRatePayout interestRatePayout = initInterestPayout(fro, DayCountFractionEnum.ACT_360);
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();

        CalculationPeriodBase calcPeriod = period(date(2020,12,10), date(2021,3,10));
        double expected = 9_000_000 * 0.01 * (31+31+28)/360.0;
        check(func.evaluate(interestRatePayout, calcPeriod), expected);

        calcPeriod = period(date(2021,9,29), date(2021,12,29));
        expected = 10_000_000 * 0.01 * (30+31+30)/360.0;
        check(func.evaluate(interestRatePayout, calcPeriod),  expected);
    }

    private void check(FloatingAmountCalculationDetails result, double expectedAmount) {
        assertEquals(BigDecimal.valueOf(expectedAmount), result.getCalculatedAmount());
    }

    public static InterestRatePayout initInterestPayout(FloatingRateOption fro, DayCountFractionEnum dcf) {
        FloatingRate rate = GetFloatingRateConditionParametersTest.initFloatingRate(fro);
        ResetDates resetDates = EvaluateTermRateTest.initResetDates(BusinessCenterEnum.GBLO, 3, 2);

        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setResetDates(resetDates)
                .setPayoutQuantity(LookupNotionalAmountTest.initNotionalSchedule())
                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRate(GetFloatingRateConditionParametersTest.initFloatingRate(fro)).build())
                .setDayCountFractionValue(dcf)
                .build();
        return interestRatePayout;
    }

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }

}
