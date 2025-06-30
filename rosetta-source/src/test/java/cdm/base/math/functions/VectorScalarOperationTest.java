package cdm.base.math.functions;

import cdm.base.math.ArithmeticOperationEnum;
import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorScalarOperationTest extends AbstractFunctionTest {

	@Inject
	private VectorScalarOperation vectorScalarOp;

	@Test
	void shouldHandleNulls() {
		List<BigDecimal> emptyList = new ArrayList<>();
		List<BigDecimal> shortList = Arrays.asList(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0));

		check(emptyList, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, null, null));
		check(shortList, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, shortList, null));
		check(emptyList, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, null, BigDecimal.valueOf(10.0)));
	}

	@Test
	void shouldApplyOperations() {
		List<BigDecimal> left = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));

		List<BigDecimal> addExpected = Arrays.asList(
				BigDecimal.valueOf(20.0),
				BigDecimal.valueOf(21.0),
				BigDecimal.valueOf(22.0));
		List<BigDecimal> subExpected = Arrays.asList(
				BigDecimal.valueOf(0.0),
				BigDecimal.valueOf(1.0),
				BigDecimal.valueOf(2.0));
		List<BigDecimal> divExpected = Arrays.asList(
				BigDecimal.valueOf(1.0),
				BigDecimal.valueOf(1.1),
				BigDecimal.valueOf(1.2));
		List<BigDecimal> mulExpected = Arrays.asList(
				BigDecimal.valueOf(10.0 * 10.0),
				BigDecimal.valueOf(11.0 * 10.0),
				BigDecimal.valueOf(12.0 * 10.0));
		List<BigDecimal> maxExpected = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(11.0),
				BigDecimal.valueOf(12.0));
		List<BigDecimal> minExpected = Arrays.asList(
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(10.0),
				BigDecimal.valueOf(10.0));

		BigDecimal ten = BigDecimal.valueOf(10.0);
		check(addExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, left, ten));
		check(subExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.SUBTRACT, left, ten));
		check(mulExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.MULTIPLY, left, ten));
		check(divExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.DIVIDE, left, ten));
		check(maxExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.MAX, left, ten));
		check(minExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.MIN, left, ten));

	}

	void check(List<BigDecimal> expected, List<BigDecimal> actual) {
		assertEquals(expected.size(), actual.size());
		int n = expected.size();
		double delta = 0.00001;
		for (int i = 0; i < n; i++) {
			assertEquals(expected.get(i).doubleValue(), actual.get(i).doubleValue(), delta);
		}
	}
}
