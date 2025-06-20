package cdm.base.math.functions;

import cdm.base.math.ArithmeticOperationEnum;
import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorOperationImplTest extends AbstractFunctionTest {

	@Inject
	private VectorOperation vectorOp;

	@Test
	void shouldHandleNulls() {
		List<BigDecimal> emptyList = new ArrayList<>();
		check(emptyList, vectorOp.evaluate(ArithmeticOperationEnum.ADD, null, null));

		List<BigDecimal> shortList = Arrays.asList(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0));
		check(shortList, vectorOp.evaluate(ArithmeticOperationEnum.ADD, shortList, null));
		check(shortList, vectorOp.evaluate(ArithmeticOperationEnum.ADD, null, shortList));
	}

	@Test
	void shouldApplyOperations() {
		List<BigDecimal> left = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));
		List<BigDecimal> right = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(9.0),
				BigDecimal.valueOf(8.0));
		List<BigDecimal> shortList = Arrays.asList(
				BigDecimal.valueOf(10.0));

		List<BigDecimal> addExpected = Arrays.asList(
				BigDecimal.valueOf(20.0),
				BigDecimal.valueOf(20.0),
				BigDecimal.valueOf(20.0));
		List<BigDecimal> subExpected = Arrays.asList(
				BigDecimal.valueOf(0.0),
				BigDecimal.valueOf(2.0),
				BigDecimal.valueOf(4.0));
		List<BigDecimal> divExpected = Arrays.asList(
				BigDecimal.valueOf(1.0),
				BigDecimal.valueOf(11.0).divide(BigDecimal.valueOf(9.0), 10, RoundingMode.HALF_EVEN),
				BigDecimal.valueOf(12.0 / 8.0));
		List<BigDecimal> mulExpected = Arrays.asList(
				BigDecimal.valueOf(10.0 * 10.0),
				BigDecimal.valueOf(9.0 * 11.0),
				BigDecimal.valueOf(8.0 * 12.0));
		List<BigDecimal> maxExpected = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));
		List<BigDecimal> minExpected = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(9.0),
				BigDecimal.valueOf(8.0));

		List<BigDecimal> addShortRightExpected = Arrays.asList(
				BigDecimal.valueOf(20.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));
		List<BigDecimal> addShortLeftExpected = Arrays.asList(
				BigDecimal.valueOf(20.0),
				BigDecimal.valueOf(9.0),
				BigDecimal.valueOf(8.0));

		check(addExpected, vectorOp.evaluate(ArithmeticOperationEnum.ADD, left, right));
		check(subExpected, vectorOp.evaluate(ArithmeticOperationEnum.SUBTRACT, left, right));
		check(mulExpected, vectorOp.evaluate(ArithmeticOperationEnum.MULTIPLY, left, right));
		check(divExpected, vectorOp.evaluate(ArithmeticOperationEnum.DIVIDE, left, right));
		check(maxExpected, vectorOp.evaluate(ArithmeticOperationEnum.MAX, left, right));
		check(minExpected, vectorOp.evaluate(ArithmeticOperationEnum.MIN, left, right));
		check(addShortLeftExpected, vectorOp.evaluate(ArithmeticOperationEnum.ADD, shortList, right));
		check(addShortRightExpected, vectorOp.evaluate(ArithmeticOperationEnum.ADD, left, shortList));
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
