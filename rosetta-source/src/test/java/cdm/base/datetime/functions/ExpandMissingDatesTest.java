package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpandMissingDatesTest extends AbstractFunctionTest {

	@Inject
	private ExpandMissingDates func;

	@Test
	void shouldFillGapsInDateList() {
		List<Date> inputDates = Arrays.asList(
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 13));

		List<Date> expectedList = Arrays.asList(
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 13));

		List<Date> results = func.evaluate(inputDates);

		assertEquals(expectedList, results);
	}

	@Test
	void shouldReturnUnchangedListWhenNoDailyGaps() {
		List<Date> inputDates = Arrays.asList(
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 11),
				Date.of(2021, 5, 12));

		List<Date> results = func.evaluate(inputDates);

		assertEquals(inputDates, results);
	}

	@Test
	void shouldFillMultipleGaps() {
		List<Date> inputDates = Arrays.asList(
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 15));

		List<Date> expectedList = Arrays.asList(
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 10),
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 15));

		List<Date> results = func.evaluate(inputDates);

		assertEquals(expectedList, results);
	}

	@Test
	void shouldReturnSingleDateUnchanged() {
		List<Date> inputDates = Collections.singletonList(Date.of(2021, 5, 10));

		List<Date> results = func.evaluate(inputDates);

		assertEquals(inputDates, results);
	}

	@Test
	void shouldHandleEmptyList() {
		List<Date> results = func.evaluate(new ArrayList<>());

		assertEquals(Collections.emptyList(), results);
	}

	@Test
	void shouldHandleNullList() {
		List<Date> results = func.evaluate(null);

		assertEquals(Collections.emptyList(), results);
	}
}
