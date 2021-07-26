package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateDifferenceImplTest extends AbstractFunctionTest {

	@Inject
	private DateDifference func;

	@Test
	void shouldSubtractDays() {
		Date first = DateImpl.of(2021, 5, 12);
		Date second = DateImpl.of(2021, 6, 12);
		int res1 = func.evaluate(first, second);
		int res2 = func.evaluate(second, first);

		assertEquals(31, res1);
		assertEquals(-31, res2);
	}

	@Test
	void shouldHandleNulls() {
		Date baseDate = DateImpl.of(1, 1, 2020);

		assertNull(func.evaluate(baseDate, null));
		assertNull(func.evaluate(null, null));
		assertNull(func.evaluate(null, baseDate));
	}
}
