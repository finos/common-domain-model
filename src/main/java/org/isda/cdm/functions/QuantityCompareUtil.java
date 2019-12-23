package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.isda.cdm.QuantityGroups;

public class QuantityCompareUtil {

	enum CompareOp {
		GREATER, EQUAL
	}

	/**
	 * Partially implemented. This only takes in account for the amounts of the
	 * quantities in the correct order. This is due to a limitation on the function
	 * syntax to create a structure to represent group-by mappings.
	 * 
	 * This implementation should be replaced with generated code.
	 */
	static boolean compareQuantities(List<QuantityGroups> beforeQuantity, List<QuantityGroups> afterQuantity,
			CompareOp compareOp) {

		if (beforeQuantity.isEmpty())
			return false;

		if (!afterQuantity.isEmpty()) {
			List<BigDecimal> numbersLeft = beforeQuantity.stream().map(x -> x.getQuantityGroups())
					.flatMap(Collection::stream).map(x -> x.getAmount()).flatMap(Collection::stream)
					.collect(Collectors.toList());

			List<BigDecimal> numbersRight = afterQuantity.stream().map(x -> x.getQuantityGroups())
					.flatMap(Collection::stream).map(x -> x.getAmount()).flatMap(Collection::stream)
					.collect(Collectors.toList());

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
	
	public static boolean quantitiesCompareToZero(List<QuantityGroups> quantity, CompareOp compareOp) {
		List<BigDecimal> numbersLeft = quantity.stream().map(x -> x.getQuantityGroups())
				.flatMap(Collection::stream).map(x -> x.getAmount()).flatMap(Collection::stream)
				.collect(Collectors.toList());
		return numbersLeft.stream().allMatch(leftNumber -> operation(compareOp).apply(leftNumber, BigDecimal.ZERO));
	}
	
	private static BiFunction<BigDecimal, BigDecimal, Boolean> operation(CompareOp compareOp) {
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
