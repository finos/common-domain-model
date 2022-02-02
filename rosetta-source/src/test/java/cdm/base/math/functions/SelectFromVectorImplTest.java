package cdm.base.math.functions;

import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SelectFromVectorImplTest extends AbstractFunctionTest {

	@Inject
	private SelectFromVector func;

	@Test
	void shouldGetValueByIndex() {
		List<BigDecimal> valueList = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));
		BigDecimal val = func.evaluate(valueList, 1);

		assertEquals(BigDecimal.valueOf((11.0)), val);
	}

	@Test
	void shouldHandleIndexOutOfRange() {
		List<BigDecimal> valueList = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));
		BigDecimal value = func.evaluate(valueList, 10);

		assertNull(value);
	}

	@Test
	void shouldHandleNulls() {
		List<BigDecimal> emptyList = new ArrayList<>();

		assertNull(func.evaluate(null, null));
		assertNull(func.evaluate(null, 3));
		assertNull(func.evaluate(emptyList, null));
	}
}
