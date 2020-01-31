package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.isda.cdm.Quantity;
import org.isda.cdm.Quantity.QuantityBuilder;
import org.isda.cdm.UnitEnum;

/**
 * Sums the given quantities together if the currencies and units are equal.  Make no attempt to convert between units or currencies.
 *
 * Note that the buy/sell direction of the trade is not accounted for.
 */
public class SumImpl extends Sum {

	@Override
	protected QuantityBuilder doEvaluate(List<Quantity> quantities) {
		QuantityBuilder builder = Quantity.builder();

		getUnits(quantities).ifPresent(u -> builder.setUnit(u));

		builder.setAmount(quantities.stream()
				.map(Quantity::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add));

		return builder;
	}

	private Optional<UnitEnum> getUnits(List<Quantity> quantities) {
		Set<UnitEnum> units = quantities.stream()
				.map(Quantity::getUnit)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		if (units.size() > 1) {
			throw new IllegalArgumentException("Cannot sum different units " + units);
		}
		return units.stream().findFirst();
	}
}
