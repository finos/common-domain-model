package cdm.product.asset.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.functions.RetrieveBusinessCenterHolidaysImplTest;
import cdm.observable.asset.FloatingRateOption;
import cdm.product.asset.FloatingRate;
import cdm.product.asset.FloatingRateSettingDetails;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetermineFloatingRateResetTest extends AbstractFunctionTest {

    @Inject
    private DetermineFloatingRateReset func;

    @Test
    void shouldEvaluateRate() {

        FloatingRateOption fro = IndexValueObservationImplTest.initFro();
        IndexValueObservationImplTest.initIndexData(fro);
        InterestRatePayout interestRatePayout = initInterestPayout(fro);
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();

        CalculationPeriodBase calcPeriod = period(date(2020,12,10), date(2021,3,10));
        check(func.evaluate(interestRatePayout, calcPeriod), 0.01, date(2021, 3, 8));

        calcPeriod = period(date(2021,9,29), date(2021,12,29));
        check(func.evaluate(interestRatePayout, calcPeriod), 0.01, date(2021, 12, 23));
    }

    private void check(FloatingRateSettingDetails result, double expectedRate, Date fixingDate) {
        assertEquals(BigDecimal.valueOf(expectedRate), result.getFloatingRate());
        assertEquals(fixingDate, result.getObservationDate());
    }

    public static InterestRatePayout initInterestPayout(FloatingRateOption fro) {
        FloatingRate rate = GetFloatingRateConditionParametersTest.initFloatingRate(fro);
        ResetDates resetDates = EvaluateTermRateTest.initResetDates(BusinessCenterEnum.GBLO, 3, 2, false);

        return InterestRatePayout.builder()
                .setResetDates(resetDates)
                .setPayoutQuantity(LookupNotionalAmountTest.initNotionalSchedule())
                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRate(GetFloatingRateConditionParametersTest.initFloatingRate(fro)).build())
                .build();
    }

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }

}
