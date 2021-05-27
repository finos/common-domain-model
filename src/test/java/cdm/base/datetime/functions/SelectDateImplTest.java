package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SelectDateImplTest extends AbstractFunctionTest {

	@Inject
	private SelectDate func;

	@Test
	void shouldGetDateByIndex() {
		List<Date> dateList = List.of(
				DateImpl.of(2021, 5, 12),
				DateImpl.of(2021, 5, 13),
				DateImpl.of(2021, 5, 14));
		Date date = func.evaluate(dateList, 1);

		assertEquals(DateImpl.of(2021, 5, 13), date);
	}
	@Test
	void shouldHandleIndexOutOfRange() {
		List<Date> dateList = List.of(
				DateImpl.of(2021, 5, 12),
				DateImpl.of(2021, 5, 13),
				DateImpl.of(2021, 5, 14));
		Date date = func.evaluate(dateList, 10);

		assertNull(date);
	}
}