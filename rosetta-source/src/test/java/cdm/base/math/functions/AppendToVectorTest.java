package cdm.base.math.functions;

import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppendToVectorTest extends AbstractFunctionTest {

	@Inject
	private AppendToVector func;

	@Test
	void shouldAppend() {
		List<BigDecimal> valueList = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));
		BigDecimal newVal = BigDecimal.valueOf(13.0);
		List<BigDecimal> expectedList = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0),
				BigDecimal.valueOf(13.0));

		List<BigDecimal> actualVector = func.evaluate(valueList, newVal);

		check(expectedList, actualVector);
	}

	@Test
	void shouldHandleEmptyList() {
		List<BigDecimal> valueList = new ArrayList<>();

		BigDecimal newVal = BigDecimal.valueOf(13.0);
		List<BigDecimal> expectedList = Arrays.asList(BigDecimal.valueOf(13.0));

		List<BigDecimal> actualCVector = func.evaluate(valueList, newVal);

		check(expectedList, actualCVector);
	}

	@Test
	void shouldHandleNulls() {
		List<BigDecimal> emptyList = new ArrayList<>();
		check(emptyList, func.evaluate(null, null));
		check(emptyList, func.evaluate(emptyList, null));

		List<BigDecimal> zeroList = Arrays.asList(BigDecimal.valueOf(0.0));
		check(zeroList, func.evaluate(emptyList, BigDecimal.valueOf(0.0)));
	}

	void check(List<BigDecimal> expected, List<BigDecimal> actual) {
		assertEquals(expected.size(), actual.size());
		int n = expected.size();
		for (int i = 0; i < n; i++) {
			double delta = 0.00001;
			assertEquals(expected.get(i).doubleValue(), actual.get(i).doubleValue(), delta);
		}
	}
}
