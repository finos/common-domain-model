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
	void shouldSubtractDaysMultipleYearsNoLeapYear() {
		Date first = DateImpl.of(2022, 7, 1);
		Date second = DateImpl.of(2023, 7, 1);
		int res1 = func.evaluate(first, second);
		int res2 = func.evaluate(second, first);

		assertEquals(365, res1);
		assertEquals(-365, res2);
	}

	@Test
	void shouldSubtractDaysMultipleYearsIncludingLeapYear() {
		Date first = DateImpl.of(2023, 7, 1);
		Date second = DateImpl.of(2024, 7, 1);
		int res1 = func.evaluate(first, second);
		int res2 = func.evaluate(second, first);

		assertEquals(366, res1);
		assertEquals(-366, res2);
	}

	@Test
	void shouldSubtractDaysMultipleYearsIncludingLeapYear2() {
		Date first = DateImpl.of(2023, 7, 1);
		Date second = DateImpl.of(2025, 7, 1);
		int res1 = func.evaluate(first, second);
		int res2 = func.evaluate(second, first);

		assertEquals(731, res1);
		assertEquals(-731, res2);
	}

	@Test
	void shouldHandleNulls() {
		Date baseDate = DateImpl.of(1, 1, 2020);

		assertNull(func.evaluate(baseDate, null));
		assertNull(func.evaluate(null, null));
		assertNull(func.evaluate(null, baseDate));
	}
}
