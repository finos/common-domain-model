package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ToDateTimeTest extends AbstractFunctionTest {

    @Inject
    private ToDateTime func;

    @Test
    void shouldCreateZonedDateTime() {
        Date date = Date.of(2024, 2, 9);
        ZonedDateTime zonedDateTime = func.evaluate(date);
        assertEquals("2024-02-09T00:00Z", zonedDateTime.toString());
    }

    @Test
    void shouldDefaultZonedDateTime() {
        ZonedDateTime zonedDateTime = func.evaluate(null);
        assertNull(zonedDateTime);
    }
}