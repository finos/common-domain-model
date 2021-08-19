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

		assertEquals(expectedList, actualList.getDates());
	}

	@Test
	void shouldHandleEmptyList() {
		DateGroup actualList = func.evaluate(DateGroup.builder().setDates(new ArrayList<>()));

		assertEquals(Collections.emptyList(), actualList.getDates());
	}

	@Test
	void shouldHandleNullList() {
		assertEquals(DateGroup.builder(), func.evaluate(null));
	}
}
