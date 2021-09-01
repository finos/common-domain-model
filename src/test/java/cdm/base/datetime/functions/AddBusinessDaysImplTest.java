package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBusinessDaysImplTest extends AbstractFunctionTest {

    @Inject
    private AddBusinessDays func;

    @Test
    void shouldAddDays() {
        Date first = DateImpl.of(2021, 12, 20);

        Date actual = func.evaluate(DateImpl.of(2021, 12, 27), -1, TARGET_BC);
        assertEquals(DateImpl.of(2021, 12, 24), actual);
        assertEquals(DateImpl.of(2021, 12, 27), func.evaluate(first, 5, TARGET_BC));
        assertEquals(DateImpl.of(2021, 12, 29), func.evaluate(first, 5, LONDON_TARGET_BC));
        assertEquals(DateImpl.of(2021, 12, 30), func.evaluate(first, 5, LONDON_TARGET_US_BC));
        assertEquals(DateImpl.of(2021, 12, 13), func.evaluate(first, -5, LONDON_TARGET_BC));
    }
}
