package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddDaysImplTest extends AbstractFunctionTest {

	@Inject
	private AddDays func;

	@Test
	void shouldAddDays() {
		Date first = DateImpl.of(2021, 5, 12);
		Date expected = DateImpl.of(2021, 6, 12);
		Date actual = func.evaluate(first, 31);

		assertEquals(expected, actual);
	}

    @Test
    void shouldSubtractDays() {
        Date first = DateImpl.of(2021, 5, 12);
        Date expected = DateImpl.of(2021, 4, 12);
        Date actual = func.evaluate(first, -30);

        assertEquals(expected, actual);
    }

	@Test
	void shouldHandleNulls() {
		Date baseDate = DateImpl.of(1, 1, 2020);

		assertNull(func.evaluate(baseDate, null));
		assertNull(func.evaluate(null, null));
		assertNull(func.evaluate(null, 2));
	}
}
