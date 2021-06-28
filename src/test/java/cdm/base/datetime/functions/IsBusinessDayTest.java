package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsBusinessDayTest extends AbstractFunctionTest {

    @Inject
    private IsBusinessDay func;

    //private static RetrieveBusinessCenterHolidaysImplTest tester;

    @Test
    void shouldGetResult() {
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();

        BusinessCenters londonTargetBC = RetrieveBusinessCenterHolidaysImplTest.londonTargetBC;
        BusinessCenters targetBC = RetrieveBusinessCenterHolidaysImplTest.targetBC;

        assertEquals(Boolean.TRUE, func.evaluate(DateImpl.of(2021,12, 20), londonTargetBC));
        assertEquals(Boolean.FALSE, func.evaluate(DateImpl.of(2021,12, 19), londonTargetBC));
        assertEquals(Boolean.FALSE, func.evaluate(DateImpl.of(2021,12, 27), londonTargetBC));
        assertEquals(Boolean.FALSE, func.evaluate(DateImpl.of(2021,12, 28), londonTargetBC));
        assertEquals(Boolean.TRUE, func.evaluate(DateImpl.of(2021,12, 27), targetBC));

    }

}
