package org.isda.cdm.functions;

import org.isda.cdm.Quantity;
import org.isda.cdm.Quantity.QuantityBuilder;
import org.isda.cdm.UnitEnum;
import org.isda.cdm.metafields.FieldWithMetaString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Sums the given quantities together if the currencies and units are equal.  Make no attempt to convert between units or currencies.
 *
 * Note that the buy/sell direction of the trade is not accounted for.
 */
public class SumImpl extends Sum {

	@Override
	protected QuantityBuilder doEvaluate(List<Quantity> quantities) {
		QuantityBuilder builder = Quantity.builder();

		getCurrency(quantities).ifPresent(c -> builder.setCurrency(FieldWithMetaString.builder().setValue(c).build()));
		getUnits(quantities).ifPresent(u -> builder.setUnit(u));

		builder.setAmount(quantities.stream()
				.map(Quantity::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add));

		return builder;
	}

	private Optional<String> getCurrency(List<Quantity> quantities) {
		Set<String> currencies = quantities.stream()
				.map(Quantity::getCurrency)
				.filter(Objects::nonNull)
				.map(FieldWithMetaString::getValue)
				.collect(Collectors.toSet());
		if (currencies.size() > 1) {
			throw new IllegalArgumentException("Cannot sum different currencies " + currencies);
		}
		return currencies.stream().findFirst();
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
