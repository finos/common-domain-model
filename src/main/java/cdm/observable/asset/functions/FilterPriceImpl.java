package cdm.observable.asset.functions;

import cdm.observable.asset.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Filters list of prices by price type.
 */
public class FilterPriceImpl extends FilterPrice {

	@Override
	protected Price.PriceBuilder doEvaluate(List<? extends Price> prices,
			PriceTypeEnum priceType,
			PriceExpressionEnum priceExpression,
			GrossOrNetEnum grossOrNet,
			CashPriceTypeEnum cashPriceType,
			CleanOrDirtyPriceEnum cleanOrDirty,
			CapFloorEnum capFloor,
			SpreadTypeEnum spreadType) {
		return emptyIfNull(prices).stream()
				.filter(p -> filter(p, priceType, PriceExpression::getPriceType))
				.filter(p -> filter(p, priceExpression, PriceExpression::getPriceExpression))
				.filter(p -> filter(p, grossOrNet, PriceExpression::getGrossOrNet))
				.filter(p -> filter(p, cashPriceType, PriceExpression::getCashPrice))
				.filter(p -> filter(p, cleanOrDirty, PriceExpression::getCleanOrDirty))
				.filter(p -> filter(p, capFloor, PriceExpression::getCapFloor))
				.filter(p -> filter(p, spreadType, PriceExpression::getSpreadType))
				.map(Price::toBuilder)
				.findFirst()
				.orElse(null);
	}

	@NotNull
	private <E> boolean filter(Price price, E e1, Function<PriceExpression, E> getter) {
		return Optional.ofNullable(e1)
				.flatMap(pe -> Optional.ofNullable(price.getPriceExpression()).map(getter).map(pe::equals))
				.orElse(true);
	}
}
