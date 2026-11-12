package cdm.base.math.functions;

import javax.inject.Inject;

import com.rosetta.model.lib.functions.ConditionValidator;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepeatNumberTest extends AbstractFunctionTest {

    @Inject
    private RepeatNumber func;

    @Test
    void shouldRepeatNumberMultipleTimes() {
        BigDecimal value = BigDecimal.valueOf(5.0);
        List<BigDecimal> result = func.evaluate(value, 3);
        assertEquals(Arrays.asList(value, value, value), result);
    }

    @Test
    void shouldReturnSingleElementWhenTimesIsOne() {
        BigDecimal value = BigDecimal.valueOf(7.5);
        List<BigDecimal> result = func.evaluate(value, 1);
        assertEquals(Collections.singletonList(value), result);
    }

    @Test
    void shouldReturnEmptyListWhenTimesIsZero() {
        BigDecimal value = BigDecimal.valueOf(42.0);
        List<BigDecimal> result = func.evaluate(value, 0);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldRejectNegativeTimes() {
        BigDecimal value = BigDecimal.valueOf(1.0);
        assertThrows(ConditionValidator.ConditionException.class, () ->
                func.evaluate(value, -1));
    }
}
