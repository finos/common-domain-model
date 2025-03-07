package cdm.observable.asset.calculatedrate.functions;

import cdm.observable.asset.calculatedrate.ObservationPeriodDatesEnum;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.period;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initResetDates;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComputeCalculationPeriodTest extends AbstractFunctionTest {

    @Inject
    private ComputeCalculationPeriod func;

    @Test
    void shouldDeterminePeriod() {
        CalculationPeriodBase calcPeriod = period(Date.of(2020,12,10), Date.of(2021, 3, 10));
        CalculationPeriodBase priorCalcPeriod = period(Date.of(2020,9,10), Date.of(2020, 12, 10));
        ResetDates resetDates = initResetDates("GBLO", 3, 2, true);

        CalculationPeriodBase expected = period(Date.of(2020, 12, 10), Date.of(2021, 3, 10));
        check (expected, func.evaluate(calcPeriod, priorCalcPeriod, ObservationPeriodDatesEnum.STANDARD, null));

        expected = period(Date.of(2020, 9, 10), Date.of(2020, 12, 10));
        check (expected, func.evaluate(calcPeriod, priorCalcPeriod, ObservationPeriodDatesEnum.SET_IN_ADVANCE, null));

        expected = period(Date.of(2020, 9, 8), Date.of(2020, 12, 8));
        check (expected, func.evaluate(calcPeriod, priorCalcPeriod, ObservationPeriodDatesEnum.FIXING_DATE, resetDates));
    }

    private void check(CalculationPeriodBase expected, CalculationPeriodBase actual) {
        assertEquals(expected.getAdjustedStartDate(), actual.getAdjustedStartDate());
        assertEquals(expected.getAdjustedEndDate(), actual.getAdjustedEndDate());
    }
}
