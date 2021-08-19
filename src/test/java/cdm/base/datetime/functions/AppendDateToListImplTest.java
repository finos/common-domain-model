package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
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

		DateGroup actualList = func.evaluate(DateGroup.builder().setDates(dateList), newVal);

		assertEquals(expectedList, actualList.getDates());
	}

	@Test
	void shouldHandleEmptyList() {
		Date newVal = DateImpl.of(2021, 5, 15);
		DateGroup actualList = func.evaluate(DateGroup.builder().setDates(new ArrayList<>()), newVal);

		assertEquals(Collections.singletonList(DateImpl.of(2021, 5, 15)), actualList.getDates());
	}

	@Test
	void shouldHandleNulls() {
		assertEquals(Collections.emptyList(), func.evaluate(null, null).getDates());
		assertEquals(Collections.emptyList(), func.evaluate(DateGroup.builder(), null).getDates());

		List<Date> zeroList = Collections.singletonList(DateImpl.of(1, 1, 2020));
		assertEquals(zeroList, func.evaluate(null, DateImpl.of(1, 1, 2020)).getDates());
	}
}
