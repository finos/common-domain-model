package cdm.base.datetime.functions;


import cdm.base.datetime.BusinessCenters;
import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusinessCenterHolidaysMultipleTest extends AbstractFunctionTest {

	@Inject
	private GetAllBusinessCenters businessCentersFunc;

	@Inject
	private BusinessCenterHolidaysMultiple func;

	@Test
	void shouldRetrieve() {
		assertEquals(LONDON_HOLIDAYS_2021, func.evaluate(getBusinessCenters(LONDON_BC)));
		assertEquals(LONDON_AND_TARGET_HOLIDAYS_2021, func.evaluate(getBusinessCenters(LONDON_TARGET_BC)));
		assertEquals(LONDON_AND_US_GS_AND_TARGET_HOLIDAYS_2021, func.evaluate(getBusinessCenters(LONDON_TARGET_US_BC)));
		assertEquals(TARGET_HOLIDAYS_2021, func.evaluate(getBusinessCenters(TARGET_BC)));

		assertEquals(LONDON_AND_TARGET_HOLIDAYS_2021, func.evaluate(getBusinessCenters(LONDON_TARGET_BC_REF)));
		assertEquals(LONDON_AND_US_GS_HOLIDAYS_2021, func.evaluate(getBusinessCenters(LONDON_US_BC_REF)));
		assertEquals(TARGET_HOLIDAYS_2021, func.evaluate(getBusinessCenters(TARGET_BC_REF)));
	}

	private List<String> getBusinessCenters(BusinessCenters businessCenters) {
		return businessCentersFunc.evaluate(businessCenters);
	}
}
