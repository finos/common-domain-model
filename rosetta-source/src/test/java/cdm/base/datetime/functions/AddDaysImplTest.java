package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddDaysImplTest extends AbstractFunctionTest {

	@Inject
	private AddDays func;

	@Test
	void shouldAddDays() {
		Date first = Date.of(2021, 5, 12);
		Date expected = Date.of(2021, 6, 12);
		Date actual = func.evaluate(first, 31);

		assertEquals(expected, actual);
	}

    @Test
    void shouldSubtractDays() {
        Date first = Date.of(2021, 5, 12);
        Date expected = Date.of(2021, 4, 12);
        Date actual = func.evaluate(first, -30);

        assertEquals(expected, actual);
    }

	@Test
	void shouldHandleNulls() {
		Date baseDate = Date.of(1, 1, 2020);

		assertNull(func.evaluate(baseDate, null));
		assertNull(func.evaluate(null, null));
		assertNull(func.evaluate(null, 2));
	}
}
