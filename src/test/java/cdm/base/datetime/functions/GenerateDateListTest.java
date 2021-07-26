package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateDateListTest extends AbstractFunctionTest {

	@Inject
	private GenerateDateList func;

	@Test
	void shouldGenerateList() {
		Date first = DateImpl.of(2021, 12, 20);
		Date last = DateImpl.of(2021, 12, 30);

		List<Date> targetExpected = Arrays.asList(
				DateImpl.of(2021, 12, 20),
				DateImpl.of(2021, 12, 21),
				DateImpl.of(2021, 12, 22),
				DateImpl.of(2021, 12, 23),
				DateImpl.of(2021, 12, 24),
				DateImpl.of(2021, 12, 27),
				DateImpl.of(2021, 12, 28),
				DateImpl.of(2021, 12, 29),
				DateImpl.of(2021, 12, 30));

		List<Date> londonTargetExpected = Arrays.asList(
				DateImpl.of(2021, 12, 20),
				DateImpl.of(2021, 12, 21),
				DateImpl.of(2021, 12, 22),
				DateImpl.of(2021, 12, 23),
				DateImpl.of(2021, 12, 24),
				DateImpl.of(2021, 12, 29),
				DateImpl.of(2021, 12, 30));

		List<Date> londonTargetUsExpected = Arrays.asList(
				DateImpl.of(2021, 12, 20),
				DateImpl.of(2021, 12, 21),
				DateImpl.of(2021, 12, 22),
				DateImpl.of(2021, 12, 23),
				DateImpl.of(2021, 12, 29),
				DateImpl.of(2021, 12, 30));

		check(Arrays.asList(), func.evaluate(last, first, TARGET_BC));
		check(targetExpected, func.evaluate(first, last, TARGET_BC));
		check(londonTargetExpected, func.evaluate(first, last, LONDON_TARGET_BC));
		check(londonTargetUsExpected, func.evaluate(first, last, LONDON_TARGET_US_BC));
		check(londonTargetUsExpected, func.evaluate(DateImpl.of(2021, 12, 20), DateImpl.of(2021, 12, 30), LONDON_TARGET_US_BC));
	}

	void check(List<Date> expected, DateGroup actualList) {
		if (actualList == null || expected.size() == 0)
			return;
		List<? extends Date> actual = actualList.getDates();
		assertEquals(expected.size(), actual.size());
		int n = expected.size();
		for (int i = 0; i < n; i++) {
			assertEquals(expected.get(i), actual.get(i));
		}
	}
}
