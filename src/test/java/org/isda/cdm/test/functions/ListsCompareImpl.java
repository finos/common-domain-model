package org.isda.cdm.test.functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

import org.isda.cdm.test.CompareOp;
import org.isda.cdm.test.ListOfNumbers;

public class ListsCompareImpl extends ListsCompare {

	@Override
	protected Boolean doEvaluate(CompareOp compareOp, ListOfNumbers left, ListOfNumbers right, BigDecimal rightNumber) {
		if (left == null)
			return false;
		if (rightNumber != null) {
			return left.getNumbers().stream().allMatch(leftNumber -> operation(compareOp).apply(leftNumber, rightNumber));
		}
		if (right != null) {
			List<BigDecimal> numbersLeft = left.getNumbers();
			List<BigDecimal> numbersRight = right.getNumbers();
			for (int i = 0; i < numbersLeft.size(); i++) {
				if (i < numbersRight.size()) {
					if (!operation(compareOp).apply(numbersLeft.get(i), numbersRight.get(i))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private BiFunction<BigDecimal, BigDecimal, Boolean> operation(CompareOp compareOp) {
		BiFunction<BigDecimal, BigDecimal, Boolean> op = null;
		switch (compareOp) {
		case GREATER:
			op = (b1, b2) -> b1.compareTo(b2) > 0;
			break;
		case EQUAL:
			op = (b1, b2) -> b1.compareTo(b2) == 0;
			break;
		default:
			break;
		}
		return op;
	}
}
