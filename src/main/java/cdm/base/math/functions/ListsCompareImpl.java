package cdm.base.math.functions;

import cdm.base.math.CompareOp;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

public class ListsCompareImpl extends ListsCompare {

	@Override
	protected Boolean doEvaluate(CompareOp compareOp, List<? extends BigDecimal> left, List<? extends BigDecimal> right, BigDecimal rightNumber) {
		if (left == null)
			return false;
		if (rightNumber != null) {
			return left.stream().allMatch(leftNumber -> operation(compareOp).apply(leftNumber, rightNumber));
		}
		if (right != null) {
			for (int i = 0; i < left.size(); i++) {
				if (i < right.size()) {
					if (!operation(compareOp).apply(left.get(i), right.get(i))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private BiFunction<BigDecimal, BigDecimal, Boolean> operation(CompareOp compareOp) {
		switch (compareOp) {
			case GREATER:
				return (b1, b2) -> b1.compareTo(b2) > 0;
			case EQUAL:
				return (b1, b2) -> b1.compareTo(b2) == 0;
			default:
				throw new IllegalArgumentException(String.format("Unknown CompareOp %s", compareOp));
		}
	}
}
