package cdm.base.datetime.functions;

import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ToTimeImplTest extends AbstractFunctionTest {

    @Inject
    private ToTime func;

    @Test
    void shouldCreateTime() {
        LocalTime localTime = func.evaluate(10, 2, 5);
        assertEquals("10:02:05", localTime.toString());
    }

    @Test
    void shouldDefaultTime() {
        LocalTime localTime = func.evaluate(null, null, null);
        assertEquals("00:00", localTime.toString());
    }
}