package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplyFollowingTest extends AbstractFunctionTest {

    @Inject
    private ApplyFollowing func;

    @Test
    void shouldReturnSameDateWhenNotAHoliday() {
        List<Date> holidays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 14), holidays);
        assertEquals(Date.of(2024, 1, 14), result);
    }

    @Test
    void shouldReturnNextDayWhenDateIsHoliday() {
        List<Date> holidays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 15), holidays);
        assertEquals(Date.of(2024, 1, 16), result);
    }

    @Test
    void shouldSkipMultipleConsecutiveHolidays() {
        List<Date> holidays = Arrays.asList(Date.of(2024, 1, 15), Date.of(2024, 1, 16));
        Date result = func.evaluate(Date.of(2024, 1, 15), holidays);
        assertEquals(Date.of(2024, 1, 17), result);
    }

    @Test
    void shouldReturnSameDateWhenHolidayListIsEmpty() {
        Date result = func.evaluate(Date.of(2024, 1, 15), Collections.emptyList());
        assertEquals(Date.of(2024, 1, 15), result);
    }
}
