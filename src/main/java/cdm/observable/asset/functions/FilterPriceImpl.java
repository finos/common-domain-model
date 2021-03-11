package cdm.observable.asset.functions;

import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;

import java.util.List;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Filters list of prices by price type.
 */
public class FilterPriceImpl extends FilterPrice {

	@Override
	protected Price.PriceBuilder doEvaluate(List<? extends Price> prices, PriceTypeEnum priceType) {
		return emptyIfNull(prices).stream()
				.filter(p -> p.getPriceType() == priceType)
				.map(Price::toBuilder)
				.findFirst()
				.orElse(null);
	}
}
