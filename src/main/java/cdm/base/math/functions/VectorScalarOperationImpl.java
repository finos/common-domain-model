package cdm.base.math.functions;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class VectorScalarOperationImpl extends VectorScalarOperation {

	private static final Logger LOGGER = LoggerFactory.getLogger(VectorScalarOperationImpl.class);

	// apply an arithmetic operation on a supplied vector and a scalar, applying the operation to each
	// element of the vector and to the scalar.
	@Override
	protected Vector.VectorBuilder doEvaluate(ArithmeticOperationEnum arithmeticOp, Vector left, BigDecimal right) {
		List<? extends BigDecimal> leftVals = left == null ? null : left.getValues();

		List<BigDecimal> res = doEval(arithmeticOp, leftVals, right);
		Vector.VectorBuilder ret = Vector.builder();
		ret.setValues(res);
		return ret;
	}

	protected List<BigDecimal> doEval(ArithmeticOperationEnum arithmeticOpEnum, List<? extends BigDecimal> left, BigDecimal right) {
		BiFunction<BigDecimal, BigDecimal, BigDecimal> eval = ArithmeticOpImpl.operation(arithmeticOpEnum);
		int num = left == null ? 0 : left.size();
		List<BigDecimal> result = new ArrayList<>(num);
		BigDecimal rightVal = right == null ? BigDecimal.valueOf(0.0) : right;

		// go through the vector and apply the operator and the scalar to each element in turn
		for (int i = 0; i < num; i++) {
			BigDecimal lhs = i < left.size() ? left.get(i) : null;
			try {
				BigDecimal val = eval.apply(lhs, rightVal);
				result.add(val);
			} catch (ArithmeticException e) {
				// Should this re-throw?
				LOGGER.error("Arithmetic operation failed: lhs {}, rightVal {}", lhs, rightVal, e);
			}
		}

		return result;
	}
}
