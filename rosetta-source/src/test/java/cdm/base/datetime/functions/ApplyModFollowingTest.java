package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplyModFollowingTest extends AbstractFunctionTest {

    @Inject
    private ApplyModFollowing func;

    @Test
    void shouldReturnSameDateWhenNotAHoliday() {
        List<Date> holidays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 14), holidays);
        assertEquals(Date.of(2024, 1, 14), result);
    }

    @Test
    void shouldApplyFollowingWhenNextDayIsInSameMonth() {
        List<Date> holidays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 15), holidays);
        assertEquals(Date.of(2024, 1, 16), result);
    }

    @Test
    void shouldRevertToPrecedingWhenFollowingCrossesMonthBoundary() {
        // Jan 31 is a holiday; following = Feb 1 (different month) → revert to preceding = Jan 30
        List<Date> holidays = Arrays.asList(Date.of(2024, 1, 31));
        Date result = func.evaluate(Date.of(2024, 1, 31), holidays);
        assertEquals(Date.of(2024, 1, 30), result);
    }

    @Test
    void shouldSkipMultipleHolidaysAndRevertToPrecedingAtMonthEnd() {
        // Jan 30 and Jan 31 are holidays; following of Jan 30 = Feb 1 → revert to preceding = Jan 29
        List<Date> holidays = Arrays.asList(Date.of(2024, 1, 30), Date.of(2024, 1, 31));
        Date result = func.evaluate(Date.of(2024, 1, 30), holidays);
        assertEquals(Date.of(2024, 1, 29), result);
    }

    @Test
    void shouldReturnSameDateWhenHolidayListIsEmpty() {
        Date result = func.evaluate(Date.of(2024, 1, 31), Collections.emptyList());
        assertEquals(Date.of(2024, 1, 31), result);
    }
}
