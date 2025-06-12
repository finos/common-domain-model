package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateDateListTest extends AbstractFunctionTest {

	@Inject
	private GetAllBusinessCenters businessCentersFunc;

	@Inject
	private GenerateDateList func;

	@Test
	void shouldGenerateList() {
		Date first = Date.of(2021, 12, 20);
		Date last = Date.of(2021, 12, 30);

		List<Date> targetExpected = Arrays.asList(
				Date.of(2021, 12, 20),
				Date.of(2021, 12, 21),
				Date.of(2021, 12, 22),
				Date.of(2021, 12, 23),
				Date.of(2021, 12, 24),
				Date.of(2021, 12, 27),
				Date.of(2021, 12, 28),
				Date.of(2021, 12, 29),
				Date.of(2021, 12, 30));

		List<Date> londonTargetExpected = Arrays.asList(
				Date.of(2021, 12, 20),
				Date.of(2021, 12, 21),
				Date.of(2021, 12, 22),
				Date.of(2021, 12, 23),
				Date.of(2021, 12, 24),
				Date.of(2021, 12, 29),
				Date.of(2021, 12, 30));

		List<Date> londonTargetUsExpected = Arrays.asList(
				Date.of(2021, 12, 20),
				Date.of(2021, 12, 21),
				Date.of(2021, 12, 22),
				Date.of(2021, 12, 23),
				Date.of(2021, 12, 29),
				Date.of(2021, 12, 30));

		assertEquals(Collections.emptyList(), func.evaluate(last, first, getBusinessCenters(TARGET_BC)));
		assertEquals(targetExpected, func.evaluate(first, last, getBusinessCenters(TARGET_BC)));
		assertEquals(londonTargetExpected, func.evaluate(first, last, getBusinessCenters(LONDON_TARGET_BC)));
		assertEquals(londonTargetUsExpected, func.evaluate(first, last, getBusinessCenters(LONDON_TARGET_US_BC)));
		assertEquals(londonTargetUsExpected,
				func.evaluate(
						Date.of(2021, 12, 20),
						Date.of(2021, 12, 30),
						getBusinessCenters(LONDON_TARGET_US_BC)));
	}

	private List<BusinessCenterEnum> getBusinessCenters(BusinessCenters businessCenters) {
		return businessCentersFunc.evaluate(businessCenters);
	}
}
