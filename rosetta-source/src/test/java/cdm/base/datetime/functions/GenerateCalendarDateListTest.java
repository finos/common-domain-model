package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateCalendarDateListTest extends AbstractFunctionTest {

    @Inject
    private GenerateCalendarDateList func;

    @Test
    void shouldGenerateInclusiveRangeWithNullFlag() {
        // isEndDateExclusive null => defaults to inclusive
        List<Date> result = func.evaluate(Date.of(2024, 3, 1), Date.of(2024, 3, 5), null);
        List<Date> expected = Arrays.asList(
                Date.of(2024, 3, 1),
                Date.of(2024, 3, 2),
                Date.of(2024, 3, 3),
                Date.of(2024, 3, 4),
                Date.of(2024, 3, 5));
        assertEquals(expected, result);
    }

    @Test
    void shouldGenerateInclusiveRangeWithFalseFlag() {
        List<Date> result = func.evaluate(Date.of(2024, 3, 1), Date.of(2024, 3, 5), false);
        List<Date> expected = Arrays.asList(
                Date.of(2024, 3, 1),
                Date.of(2024, 3, 2),
                Date.of(2024, 3, 3),
                Date.of(2024, 3, 4),
                Date.of(2024, 3, 5));
        assertEquals(expected, result);
    }

    @Test
    void shouldGenerateExclusiveRange() {
        List<Date> result = func.evaluate(Date.of(2024, 3, 1), Date.of(2024, 3, 5), true);
        List<Date> expected = Arrays.asList(
                Date.of(2024, 3, 1),
                Date.of(2024, 3, 2),
                Date.of(2024, 3, 3),
                Date.of(2024, 3, 4));
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnSingleDateWhenStartEqualsEnd() {
        List<Date> result = func.evaluate(Date.of(2024, 6, 15), Date.of(2024, 6, 15), false);
        assertEquals(Collections.singletonList(Date.of(2024, 6, 15)), result);
    }

    @Test
    void shouldReturnEmptyWhenStartEqualsEndAndExclusive() {
        List<Date> result = func.evaluate(Date.of(2024, 6, 15), Date.of(2024, 6, 15), true);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldReturnEmptyWhenStartAfterEnd() {
        List<Date> result = func.evaluate(Date.of(2024, 3, 10), Date.of(2024, 3, 5), false);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldCrossMonthBoundary() {
        List<Date> result = func.evaluate(Date.of(2024, 1, 30), Date.of(2024, 2, 2), false);
        List<Date> expected = Arrays.asList(
                Date.of(2024, 1, 30),
                Date.of(2024, 1, 31),
                Date.of(2024, 2, 1),
                Date.of(2024, 2, 2));
        assertEquals(expected, result);
    }

    @Test
    void shouldCrossYearBoundary() {
        List<Date> result = func.evaluate(Date.of(2023, 12, 30), Date.of(2024, 1, 2), false);
        List<Date> expected = Arrays.asList(
                Date.of(2023, 12, 30),
                Date.of(2023, 12, 31),
                Date.of(2024, 1, 1),
                Date.of(2024, 1, 2));
        assertEquals(expected, result);
    }

    @Test
    void shouldIncludeWeekendsAndNotFilterHolidays() {
        // 2024-03-01 is a Friday, 2024-03-04 is a Monday — includes Sat/Sun
        List<Date> result = func.evaluate(Date.of(2024, 3, 1), Date.of(2024, 3, 4), false);
        List<Date> expected = Arrays.asList(
                Date.of(2024, 3, 1),
                Date.of(2024, 3, 2),
                Date.of(2024, 3, 3),
                Date.of(2024, 3, 4));
        assertEquals(expected, result);
    }

    @Test
    void shouldHandleLeapDayInclusive() {
        List<Date> result = func.evaluate(Date.of(2024, 2, 28), Date.of(2024, 3, 1), false);
        List<Date> expected = Arrays.asList(
                Date.of(2024, 2, 28),
                Date.of(2024, 2, 29),
                Date.of(2024, 3, 1));
        assertEquals(expected, result);
    }

    @Test
    void shouldHandleExclusiveEndOneDayRange() {
        List<Date> result = func.evaluate(Date.of(2024, 3, 5), Date.of(2024, 3, 6), true);
        assertEquals(Collections.singletonList(Date.of(2024, 3, 5)), result);
    }
}
