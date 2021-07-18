package cdm.product.asset.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.functions.RetrieveBusinessCenterHolidaysImplTest;
import cdm.product.asset.ObservationPeriodDatesEnum;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComputeCalculationPeriodTest extends AbstractFunctionTest {

    @Inject
    private ComputeCalculationPeriod func;

    @Test
    void shouldDeterminePeriod() {
        CalculationPeriodBase calcPeriod = period(date(2020,12,10), date(2021, 3, 10));
        CalculationPeriodBase priorCalcPeriod = period(date(2020,9,10), date(2020, 12, 10));
        BusinessCenters bc = BusinessCenters.builder().addBusinessCenterValue(BusinessCenterEnum.GBLO).build();
        ResetDates resetDates = EvaluateScreenRateTest.initResetDates(BusinessCenterEnum.GBLO, 3, 2, true);
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();

        CalculationPeriodBase expected = period(date(2020, 12, 10), date(2021, 3, 10));
        check (expected, func.evaluate(calcPeriod, priorCalcPeriod, ObservationPeriodDatesEnum.STANDARD, null));

        expected = period(date(2020, 9, 10), date(2020, 12, 10));
        check (expected, func.evaluate(calcPeriod, priorCalcPeriod, ObservationPeriodDatesEnum.SETINADVANCE, null));

        expected = period(date(2020, 9, 8), date(2020, 12, 8));
        check (expected, func.evaluate(calcPeriod, priorCalcPeriod, ObservationPeriodDatesEnum.FIXINGDATE, resetDates));
    }

    private void check(CalculationPeriodBase expected, CalculationPeriodBase actual) {
        assertEquals(expected.getAdjustedStartDate(), actual.getAdjustedStartDate());
        assertEquals(expected.getAdjustedEndDate(), actual.getAdjustedEndDate());
    }

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }

}
