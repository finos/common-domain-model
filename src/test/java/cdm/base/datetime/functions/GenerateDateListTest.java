package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

		assertNull(func.evaluate(last, first, TARGET_BC));
		assertEquals(targetExpected, func.evaluate(first, last, TARGET_BC).getDates());
		assertEquals(londonTargetExpected, func.evaluate(first, last, LONDON_TARGET_BC).getDates());
		assertEquals(londonTargetUsExpected, func.evaluate(first, last, LONDON_TARGET_US_BC).getDates());
		assertEquals(londonTargetUsExpected, func.evaluate(DateImpl.of(2021, 12, 20), DateImpl.of(2021, 12, 30), LONDON_TARGET_US_BC).getDates());
	}
}
