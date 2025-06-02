package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppendDateToListTest extends AbstractFunctionTest {

	@Inject
	private AppendDateToList func;

	@Test
	void shouldAppend() {
		List<Date> dateList = Arrays.asList(
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 13),
				Date.of(2021, 5, 14));

		Date newVal = Date.of(2021, 5, 15);

		List<Date> expectedList = Arrays.asList(
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 13),
				Date.of(2021, 5, 14),
				Date.of(2021, 5, 15));

		List<Date> actualList = func.evaluate(dateList, newVal);

		assertEquals(expectedList, actualList);
	}

	@Test
	void shouldHandleEmptyList() {
		Date newVal = Date.of(2021, 5, 15);
		List<Date> actualList = func.evaluate(new ArrayList<>(), newVal);

		assertEquals(Collections.singletonList(Date.of(2021, 5, 15)), actualList);
	}

	@Test
	void shouldHandleNulls() {
		assertEquals(Collections.emptyList(), func.evaluate(null, null));
		assertEquals(Collections.emptyList(), func.evaluate(new ArrayList<>(), null));

		List<Date> zeroList = Collections.singletonList(Date.of(1, 1, 2020));
		assertEquals(zeroList, func.evaluate(null, Date.of(1, 1, 2020)));
	}
}
