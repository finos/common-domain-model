package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShiftBusinessDaysTest extends AbstractFunctionTest {

    @Inject
    private ShiftBusinessDays func;

    @Test
    void shouldShiftForwardSkippingNonBusinessDays() {
        Date startDate = Date.of(2021, 12, 24);
        List<Date> nonBusinessDates = Arrays.asList(
                Date.of(2021, 12, 25),
                Date.of(2021, 12, 26));

        Date actual = func.evaluate(startDate, 1, nonBusinessDates);

        assertEquals(Date.of(2021, 12, 27), actual);
    }

    @Test
    void shouldShiftBackwardSkippingNonBusinessDays() {
        Date startDate = Date.of(2021, 12, 28);
        List<Date> nonBusinessDates = Collections.singletonList(Date.of(2021, 12, 27));

        Date actual = func.evaluate(startDate, -1, nonBusinessDates);

        assertEquals(Date.of(2021, 12, 26), actual);
    }

    @Test
    void shouldShiftForwardAcrossNonBusinessDayGap() {
        Date startDate = Date.of(2021, 5, 10);
        List<Date> nonBusinessDates = Arrays.asList(
                Date.of(2021, 5, 11),
                Date.of(2021, 5, 13));

        Date actual = func.evaluate(startDate, 2, nonBusinessDates);

        assertEquals(Date.of(2021, 5, 14), actual);
    }

    @Test
    void shouldShiftBackwardAcrossNonBusinessDayGap() {
        Date startDate = Date.of(2021, 5, 14);
        List<Date> nonBusinessDates = Arrays.asList(
                Date.of(2021, 5, 13),
                Date.of(2021, 5, 11));

        Date actual = func.evaluate(startDate, -2, nonBusinessDates);

        assertEquals(Date.of(2021, 5, 10), actual);
    }

    @Test
    void shouldShiftForwardWhenAllDaysAreBusinessDays() {
        Date startDate = Date.of(2021, 12, 20);

        Date actual = func.evaluate(startDate, 3, Collections.emptyList());

        assertEquals(Date.of(2021, 12, 23), actual);
    }

    @Test
    void shouldShiftBackwardWhenAllDaysAreBusinessDays() {
        Date startDate = Date.of(2021, 12, 20);

        Date actual = func.evaluate(startDate, -3, Collections.emptyList());

        assertEquals(Date.of(2021, 12, 17), actual);
    }

    @Test
    void shouldReturnStartDateWhenOffsetIsZero() {
        Date startDate = Date.of(2021, 12, 20);
        List<Date> nonBusinessDates = Collections.singletonList(Date.of(2021, 12, 21));

        Date actual = func.evaluate(startDate, 0, nonBusinessDates);

        assertEquals(Date.of(2021, 12, 20), actual);
    }

    @Test
    void shouldHandleNullNonBusinessDates() {
        Date startDate = Date.of(2021, 12, 20);

        Date actual = func.evaluate(startDate, 1, null);

        assertEquals(Date.of(2021, 12, 21), actual);
    }
}
