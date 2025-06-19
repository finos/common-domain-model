package cdm.base.datetime.functions;

import cdm.base.datetime.DayOfWeekEnum;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DayOfWeekImplTest extends AbstractFunctionTest {

	@Inject
	private DayOfWeek func;

	@Test
	void shouldGetDow() {
		assertEquals(DayOfWeekEnum.MON, func.evaluate(Date.of(2021, 5, 17)));
		assertEquals(DayOfWeekEnum.TUE, func.evaluate(Date.of(2021, 5, 18)));
		assertEquals(DayOfWeekEnum.WED, func.evaluate(Date.of(2021, 5, 19)));
		assertEquals(DayOfWeekEnum.THU, func.evaluate(Date.of(2021, 5, 20)));
		assertEquals(DayOfWeekEnum.FRI, func.evaluate(Date.of(2021, 5, 21)));
		assertEquals(DayOfWeekEnum.SAT, func.evaluate(Date.of(2021, 5, 22)));
		assertEquals(DayOfWeekEnum.SUN, func.evaluate(Date.of(2021, 5, 23)));
	}

	@Test
	void shouldHandleNulls() {
		assertNull(func.evaluate(null));
	}
}
