package cdm.product.asset.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.DateGroup;
import cdm.base.datetime.functions.RetrieveBusinessCenterHolidaysImplTest;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateObservationDatesTest extends AbstractFunctionTest {

    @Inject
    private GenerateObservationDates func;

    @Test
    void shouldDeterminedDates() {
        CalculationPeriodBase obsPeriod = period(date(2020,12,7), date(2021, 3, 5));
        BusinessCenters bc = BusinessCenters.builder().addBusinessCenterValue(BusinessCenterEnum.GBLO).build();
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();

        Integer lockout = 1;

        List<Date> expected = Arrays.asList(
                date(2020,12, 7),
                date(2020,12, 8),
                date(2020,12, 9),
                date(2020,12, 10),
                date(2020,12, 11),
                date(2020,12, 14),
                date(2020,12, 15),
                date(2020,12, 16),
                date(2020,12, 17),
                date(2020,12, 18),
                date(2020,12, 21),
                date(2020,12, 22),
                date(2020,12, 23),
                date(2020,12, 24),
                date(2020,12, 29),
                date(2020,12, 30),
                date(2020,12, 31),
                date(2021,1,4),
                date(2021,1,5),
                date(2021,1,6),
                date(2021,1,7),
                date(2021,1,8),
                date(2021,1,11),
                date(2021,1,12),
                date(2021,1,13),
                date(2021,1,14),
                date(2021,1,15),
                date(2021,1,18),
                date(2021,1,19),
                date(2021,1,20),
                date(2021,1,21),
                date(2021,1,22),
                date(2021,1,25),
                date(2021,1,26),
                date(2021,1,27),
                date(2021,1,28),
                date(2021,1,29),
                date(2021,2,1),
                date(2021,2,2),
                date(2021,2,3),
                date(2021,2,4),
                date(2021,2,5),
                date(2021,2,8),
                date(2021,2,9),
                date(2021,2,10),
                date(2021,2,11),
                date(2021,2,12),
                date(2021,2,15),
                date(2021,2,16),
                date(2021,2,17),
                date(2021,2,18),
                date(2021,2,19),
                date(2021,2,22),
                date(2021,2,23),
                date(2021,2,24),
                date(2021,2,25),
                date(2021,2,26),
                date(2021,3,1),
                date(2021,3,2),
                date(2021,3,3)
        );

        check (expected, func.evaluate(obsPeriod, bc, lockout));
    }

    private void check(List<Date> expected, DateGroup actual) {
        List<? extends Date>act = actual.getDates();
        for (int i=0; i< expected.size() && i < act.size(); i++) {
            assertEquals(expected.get(i), act.get(i));
        }
        assertEquals(expected.size(), act.size());

    }

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }

}
