package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppendDateToListImplTest extends AbstractFunctionTest {

	@Inject
	private AppendDateToList func;

	@Test
	void shouldAppend() {
		List<Date> dateList = Arrays.asList(
				DateImpl.of(2021, 5, 12),
				DateImpl.of(2021, 5, 13),
				DateImpl.of(2021, 5, 14));

		Date newVal = DateImpl.of(2021, 5, 15);

		List<Date> expectedList = Arrays.asList(
				DateImpl.of(2021, 5, 12),
				DateImpl.of(2021, 5, 13),
				DateImpl.of(2021, 5, 14),
				DateImpl.of(2021, 5, 15));

		List<? extends Date> actualList = func.evaluate(dateList, newVal);

		assertEquals(expectedList, actualList);
	}

	@Test
	void shouldHandleEmptyList() {
		Date newVal = DateImpl.of(2021, 5, 15);
		List<? extends Date> actualList = func.evaluate(new ArrayList<>(), newVal);

		assertEquals(Collections.singletonList(DateImpl.of(2021, 5, 15)), actualList);
	}

	@Test
	void shouldHandleNulls() {
		assertEquals(Collections.emptyList(), func.evaluate(null, null));
		assertEquals(Collections.emptyList(), func.evaluate(Collections.emptyList(), null));

		List<Date> zeroList = Collections.singletonList(DateImpl.of(1, 1, 2020));
		assertEquals(zeroList, func.evaluate(null, DateImpl.of(1, 1, 2020)));
	}
}
