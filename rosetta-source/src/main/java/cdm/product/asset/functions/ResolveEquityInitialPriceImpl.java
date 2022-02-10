package cdm.product.asset.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import cdm.observable.asset.Observable;
import cdm.observable.asset.Price;

import java.util.List;
import java.util.Optional;

import static cdm.observable.asset.Price.PriceBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * To be replaced by full resolve price function implementation.
 */
public class ResolveEquityInitialPriceImpl extends ResolveEquityInitialPrice {

	@Override
	protected PriceBuilder doEvaluate(List<? extends Price> price, Observable observable) {
		return emptyIfNull(price).stream()
				.filter(p -> Optional.ofNullable(p)
						.map(Price::getPerUnitOfAmount)
						.map(UnitType::getFinancialUnit)
						.map(FinancialUnitEnum.SHARE::equals)
						.orElse(false))
				.map(Price::toBuilder)
				.findFirst()
				.orElse(null);
	}
}
