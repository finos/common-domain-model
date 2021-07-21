package cdm.product.asset.calculatedrate.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.product.asset.floatingrate.functions.GetFloatingRateConditionParametersTest;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateObservationPeriodTest extends AbstractFunctionTest {

    @Inject
    private GenerateObservationPeriod func;

    @Test
    void shouldDeterminePeriod() {
        CalculationPeriodBase calcPeriod = period(date(2020,12,10), date(2021, 3, 10));
        BusinessCenters bc = BusinessCenters.builder().addBusinessCenterValue(BusinessCenterEnum.GBLO).build();
        Integer shift = 3;

        CalculationPeriodBase expected = period(date(2020, 12, 7), date(2021, 3, 5));
        check (expected, func.evaluate(calcPeriod, bc, shift));
    }

    private void check(CalculationPeriodBase expected, CalculationPeriodBase actual) {
        assertEquals(expected.getAdjustedStartDate(), actual.getAdjustedStartDate());
        assertEquals(expected.getAdjustedEndDate(), actual.getAdjustedEndDate());
    }

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }

}
