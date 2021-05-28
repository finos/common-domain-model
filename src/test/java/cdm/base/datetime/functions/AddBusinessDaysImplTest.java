package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBusinessDaysImplTest extends AbstractFunctionTest {

    @Inject
    private AddBusinessDays func;

    @Test
    void shouldAddDays() {
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();

        BusinessCenters londonTargetBC = RetrieveBusinessCenterHolidaysImplTest.londonTargetBC;
        BusinessCenters targetBC = RetrieveBusinessCenterHolidaysImplTest.targetBC;
        BusinessCenters londonTargetUsBC = RetrieveBusinessCenterHolidaysImplTest.londonTargetUSBC;


        Date first = DateImpl.of(2021, 12, 20);

        Date actual = func.evaluate(DateImpl.of(2021, 12, 27), -1, targetBC);
        assertEquals(DateImpl.of(2021, 12, 24), actual);
        assertEquals(DateImpl.of(2021, 12, 27), func.evaluate(first, 5, targetBC));
        assertEquals(DateImpl.of(2021, 12, 29), func.evaluate(first, 5, londonTargetBC));
        assertEquals(DateImpl.of(2021, 12, 30), func.evaluate(first, 5, londonTargetUsBC));
        assertEquals(DateImpl.of(2021, 12, 13), func.evaluate(first, -5, londonTargetBC));

    }
}
