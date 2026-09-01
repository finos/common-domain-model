package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdjustDateToFollowingBusinessDayTest extends AbstractFunctionTest {

    @Inject
    private AdjustDateToFollowingBusinessDay func;

    @Test
    void shouldReturnSameDateWhenDateIsABusinessDay() {
        List<Date> nonBusinessDays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 14), nonBusinessDays);
        assertEquals(Date.of(2024, 1, 14), result);
    }

    @Test
    void shouldReturnNextDayWhenDateIsNotABusinessDay() {
        List<Date> nonBusinessDays = Arrays.asList(Date.of(2024, 1, 15));
        Date result = func.evaluate(Date.of(2024, 1, 15), nonBusinessDays);
        assertEquals(Date.of(2024, 1, 16), result);
    }

    @Test
    void shouldSkipMultipleConsecutiveNonBusinessDays() {
        List<Date> nonBusinessDays = Arrays.asList(Date.of(2024, 1, 15), Date.of(2024, 1, 16));
        Date result = func.evaluate(Date.of(2024, 1, 15), nonBusinessDays);
        assertEquals(Date.of(2024, 1, 17), result);
    }

    @Test
    void shouldReturnSameDateWhenNonBusinessDayListIsEmpty() {
        Date result = func.evaluate(Date.of(2024, 1, 15), Collections.emptyList());
        assertEquals(Date.of(2024, 1, 15), result);
    }
}
