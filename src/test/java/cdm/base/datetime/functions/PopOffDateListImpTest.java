package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PopOffDateListImpTest extends AbstractFunctionTest {

	@Inject
	private PopOffDateList func;

	@Test
	void shouldRemove() {
		List<Date> dates = Arrays.asList(
				DateImpl.of(2021, 5, 12),
				DateImpl.of(2021, 5, 13),
				DateImpl.of(2021, 5, 14));

		List<Date> expectedList = Arrays.asList(
				DateImpl.of(2021, 5, 12),
				DateImpl.of(2021, 5, 13));

		DateGroup actualList = func.evaluate(DateGroup.builder().setDates(dates));

		check(expectedList, actualList);
	}

	@Test
	void shouldHandleEmptyList() {
		List<Date> dates = new ArrayList<>();
		List<Date> expectedList = new ArrayList<>();

		DateGroup actualList = func.evaluate(DateGroup.builder().setDates(dates));

		check(expectedList, actualList);
	}

	@Test
	void shouldHandleNullList() {
		DateGroup.DateGroupBuilder empty = DateGroup.builder();
		assertEquals(empty, func.evaluate(null));
	}

	void check(List<? extends Date> expected, DateGroup actualDateCollection) {
		List<? extends Date> actual = actualDateCollection.getDates();
		assertEquals(expected.size(), actual.size());
		int n = expected.size();
		for (int i = 0; i < n; i++) {
			assertEquals(expected.get(i), actual.get(i));
		}
	}
}
