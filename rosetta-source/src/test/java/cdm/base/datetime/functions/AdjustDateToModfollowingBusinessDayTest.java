package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdjustDateToModfollowingBusinessDayTest extends AbstractFunctionTest {

    @Inject
    private AdjustDateToModfollowingBusinessDay func;

    @Test
    void shouldReturnSameDateWhenDateIsABusinessDay() {
        List<Date> nonBusinessDays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 14), nonBusinessDays);
        assertEquals(Date.of(2024, 1, 14), result);
    }

    @Test
    void shouldApplyFollowingWhenNextDayIsInSameMonth() {
        List<Date> nonBusinessDays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 15), nonBusinessDays);
        assertEquals(Date.of(2024, 1, 16), result);
    }

    @Test
    void shouldRevertToPrecedingWhenFollowingCrossesMonthBoundary() {
        // Jan 31 is a non-business day; following = Feb 1 (different month) → revert to preceding = Jan 30
        List<Date> nonBusinessDays = Arrays.asList(Date.of(2024, 1, 31));
        Date result = func.evaluate(Date.of(2024, 1, 31), nonBusinessDays);
        assertEquals(Date.of(2024, 1, 30), result);
    }

    @Test
    void shouldSkipMultipleNonBusinessDaysAndRevertToPrecedingAtMonthEnd() {
        // Jan 30 and Jan 31 are non-business days; following of Jan 30 = Feb 1 → revert to preceding = Jan 29
        List<Date> nonBusinessDays = Arrays.asList(Date.of(2024, 1, 30), Date.of(2024, 1, 31));
        Date result = func.evaluate(Date.of(2024, 1, 30), nonBusinessDays);
        assertEquals(Date.of(2024, 1, 29), result);
    }

    @Test
    void shouldReturnSameDateWhenNonBusinessDayListIsEmpty() {
        Date result = func.evaluate(Date.of(2024, 1, 31), Collections.emptyList());
        assertEquals(Date.of(2024, 1, 31), result);
    }
}
