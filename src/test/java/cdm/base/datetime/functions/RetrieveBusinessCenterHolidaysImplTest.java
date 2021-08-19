package cdm.base.datetime.functions;

import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrieveBusinessCenterHolidaysImplTest extends AbstractFunctionTest {

	@Inject
	private RetrieveBusinessCenterHolidays func;

	@Test
	void shouldRetrieve() {
		assertEquals(LONDON_HOLIDAYS_2021, func.evaluate(LONDON_BC).getDates());
		assertEquals(LONDON_AND_TARGET_HOLIDAYS_2021, func.evaluate(LONDON_TARGET_BC).getDates());
		assertEquals(LONDON_AND_US_GS_AND_TARGET_HOLIDAYS_2021, func.evaluate(LONDON_TARGET_US_BC).getDates());
		assertEquals(TARGET_HOLIDAYS_2021, func.evaluate(TARGET_BC).getDates());

		assertEquals(LONDON_AND_TARGET_HOLIDAYS_2021, func.evaluate(LONDON_TARGET_BC_REF).getDates());
		assertEquals(LONDON_AND_US_GS_HOLIDAYS_2021, func.evaluate(LONDON_US_BC_REF).getDates());
		assertEquals(TARGET_HOLIDAYS_2021, func.evaluate(TARGET_BC_REF).getDates());
	}
}
