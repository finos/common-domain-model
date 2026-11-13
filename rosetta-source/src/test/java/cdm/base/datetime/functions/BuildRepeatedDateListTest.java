package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.functions.ConditionValidator;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuildRepeatedDateListTest extends AbstractFunctionTest {

	@Inject
	private BuildRepeatedDateList func;

	@Test
	void shouldRepeatDateMultipleTimes() {
		Date date = Date.of(2021, 5, 12);

		List<Date> expectedList = Arrays.asList(
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 12));

		List<Date> results = func.evaluate(date, 3);

		assertEquals(expectedList, results);
	}

	@Test
	void shouldReturnSingleElementListForOneRepeat() {
		Date date = Date.of(2021, 5, 12);

		List<Date> results = func.evaluate(date, 1);

		assertEquals(Collections.singletonList(Date.of(2021, 5, 12)), results);
	}

	@Test
	void shouldThrowForZeroRepeats() {
		Date date = Date.of(2021, 5, 12);

		ConditionValidator.ConditionException exception = assertThrows(ConditionValidator.ConditionException.class, () -> func.evaluate(date, 0));
		assertEquals("The number of repeats must be greater than zero.", exception.getMessage());
	}

	@Test
	void shouldThrowForNegativeRepeats() {
		Date date = Date.of(2021, 5, 12);

		ConditionValidator.ConditionException exception = assertThrows(ConditionValidator.ConditionException.class, () -> func.evaluate(date, -1));
		assertEquals("The number of repeats must be greater than zero.", exception.getMessage());
	}
}
