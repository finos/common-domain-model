package cdm.product.asset.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.template.Underlier;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cdm.observable.asset.Price.PriceBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * To be replaced by full resolve price function implementation.
 */
public class ResolveEquityInitialPriceImpl extends ResolveEquityInitialPrice {

	@Override
	protected PriceBuilder doEvaluate(List<PriceQuantity> priceQuantity) {
		return emptyIfNull(priceQuantity).stream()
				.map(PriceQuantity::getPrice)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaPrice::getValue)
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
