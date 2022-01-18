package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.LONDON_TARGET_BC;
import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.TARGET_BC;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsHolidayTest extends AbstractFunctionTest {

    @Inject
    private IsHoliday func;

    @Test
    void shouldGetResult() {
        assertEquals(Boolean.FALSE, func.evaluate(DateImpl.of(2021,12, 20), LONDON_TARGET_BC));
        assertEquals(Boolean.FALSE, func.evaluate(DateImpl.of(2021,12, 19), LONDON_TARGET_BC));
        assertEquals(Boolean.TRUE, func.evaluate(DateImpl.of(2021,12, 27), LONDON_TARGET_BC));
        assertEquals(Boolean.TRUE, func.evaluate(DateImpl.of(2021,12, 28), LONDON_TARGET_BC));
        assertEquals(Boolean.FALSE, func.evaluate(DateImpl.of(2021,12, 27), TARGET_BC));

    }
}
