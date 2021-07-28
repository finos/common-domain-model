package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrieveBusinessCenterHolidaysImplTest extends AbstractFunctionTest {

	@Inject
	private RetrieveBusinessCenterHolidays func;

	@Test
	void shouldRetrieve() {
		check(LONDON_HOLIDAYS_2021, func.evaluate(LONDON_BC));
		check(LONDON_AND_TARGET_HOLIDAYS_2021, func.evaluate(LONDON_TARGET_BC));
		check(LONDON_AND_US_GS_AND_TARGET_HOLIDAYS_2021, func.evaluate(LONDON_TARGET_US_BC));
		check(TARGET_HOLIDAYS_2021, func.evaluate(TARGET_BC));

		check(LONDON_AND_TARGET_HOLIDAYS_2021, func.evaluate(LONDON_TARGET_BC_REF));
		check(LONDON_AND_US_GS_HOLIDAYS_2021, func.evaluate(LONDON_US_BC_REF));
		check(TARGET_HOLIDAYS_2021, func.evaluate(TARGET_BC_REF));
	}

	void check(List<? extends Date> expected, DateGroup actual) {
		assertEquals(expected.size(), actual.getDates().size());
		assertThat(actual.getDates(), equalTo(expected));
	}
}
