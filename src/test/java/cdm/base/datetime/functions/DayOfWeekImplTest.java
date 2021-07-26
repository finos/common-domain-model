package cdm.base.datetime.functions;

import cdm.base.datetime.DayOfWeekEnum;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DayOfWeekImplTest extends AbstractFunctionTest {

	@Inject
	private DayOfWeek func;

	@Test
	void shouldGetDow() {
		assertEquals(DayOfWeekEnum.MON, func.evaluate(DateImpl.of(2021, 5, 17)));
		assertEquals(DayOfWeekEnum.TUE, func.evaluate(DateImpl.of(2021, 5, 18)));
		assertEquals(DayOfWeekEnum.WED, func.evaluate(DateImpl.of(2021, 5, 19)));
		assertEquals(DayOfWeekEnum.THU, func.evaluate(DateImpl.of(2021, 5, 20)));
		assertEquals(DayOfWeekEnum.FRI, func.evaluate(DateImpl.of(2021, 5, 21)));
		assertEquals(DayOfWeekEnum.SAT, func.evaluate(DateImpl.of(2021, 5, 22)));
		assertEquals(DayOfWeekEnum.SUN, func.evaluate(DateImpl.of(2021, 5, 23)));
	}

	@Test
	void shouldHandleNulls() {
		assertNull(func.evaluate(null));
	}
}
