package cdm.base.math.functions;

import cdm.base.math.ArithmeticOperationEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class VectorOperationImpl extends VectorOperation {

	private static final Logger LOGGER = LoggerFactory.getLogger(VectorOperationImpl.class);

	// apply an arithmetic operation on two supplied vectors, applying the operation pairwise.
	// If one vector is shorter than the other, pad out the shorter one with 0s.
	@Override
	protected List<BigDecimal> doEvaluate(ArithmeticOperationEnum arithmeticOp, List<BigDecimal> left, List<BigDecimal> right) {
		return doEval(arithmeticOp, emptyIfNull(left), emptyIfNull(right));
	}

	protected List<BigDecimal> doEval(ArithmeticOperationEnum arithmeticOp, List<BigDecimal> left, List<BigDecimal> right) {
		BiFunction<BigDecimal, BigDecimal, BigDecimal> eval = ArithmeticOpImpl.operation(arithmeticOp);

		int leftSize = left == null ? 0 : left.size();
		int rightSize = right == null ? 0 : right.size();

		int num = Math.max(leftSize, rightSize);
		List<BigDecimal> result = new ArrayList<>(num);

		// go through the pairs of values, defaulting with 0 if past the end of the list,
		// and apply the operator to each pair
		for (int i = 0; i < num; i++) {
			BigDecimal lhs = i < leftSize ? left.get(i) : new BigDecimal("0.0");  // get left value, default 0
			BigDecimal rhs = i < rightSize ? right.get(i) : new BigDecimal("0.0");   // get right value, default 0
			try {
				BigDecimal val = eval.apply(lhs, rhs);      // apply the operation
				result.add(val);
			} catch (ArithmeticException e) {
				// Should this re-throw?
				LOGGER.error("Arithmetic operation failed: lhs {}, rhs {}", lhs, rhs, e);
			}
		}
		return result;
	}
}


