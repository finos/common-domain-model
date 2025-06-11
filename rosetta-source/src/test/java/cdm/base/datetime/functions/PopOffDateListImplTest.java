package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PopOffDateListImplTest extends AbstractFunctionTest {

	@Inject
	private PopOffDateList func;

	@Test
	void shouldRemove() {
		List<Date> dates = Arrays.asList(
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 13),
				Date.of(2021, 5, 14));

		List<Date> expectedList = Arrays.asList(
				Date.of(2021, 5, 12),
				Date.of(2021, 5, 13));

		List<Date> actualList = func.evaluate(dates);

		assertEquals(expectedList, actualList);
	}

	@Test
	void shouldHandleEmptyList() {
		List<Date> actualList = func.evaluate(new ArrayList<>());

		assertEquals(Collections.emptyList(), actualList);
	}

	@Test
	void shouldHandleNullList() {
		assertEquals(Collections.emptyList(), func.evaluate(null));
	}
}
