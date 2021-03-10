package cdm.base.math.functions;

import java.math.BigDecimal;
import java.util.List;

/**
 * Sums the given quantities together if the currencies and units are equal.  Make no attempt to convert between units or currencies.
 *
 * Note that the buy/sell direction of the trade is not accounted for.
 */
public class SumImpl extends Sum {

	@Override
	protected BigDecimal doEvaluate(List<? extends BigDecimal> x) {
		return x.stream().map(BigDecimal.class::cast).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
