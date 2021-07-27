package cdm.product.common.settlement.functions;

import cdm.base.datetime.Period;
import cdm.base.math.MeasureBase;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.Observable;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.metafields.FieldWithMetaFloatingRateOption;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.common.settlement.PriceQuantitySettlement;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class FilterPriceQuantitySettlementImpl extends FilterPriceQuantitySettlement {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilterPriceQuantitySettlementImpl.class);

	@Override 
	protected PriceQuantitySettlement.PriceQuantitySettlementBuilder doEvaluate(
			List<? extends PriceQuantitySettlement> priceQuantitySettlements, 
			String currency, 
			Period rateOptionIndexTenor) {
		if (currency == null || rateOptionIndexTenor == null || !rateOptionIndexTenor.toBuilder().hasData()) {
			return null;
		}
		List<? extends PriceQuantitySettlement> filteredPriceQuantities = emptyIfNull(priceQuantitySettlements).stream()
				.filter(pq -> rateOptionIndexTenorMatches(pq, rateOptionIndexTenor))
				.filter(pq -> quantityUnitOfAmountMatches(pq, currency) || priceUnitOfAmountMatches(pq, currency))
				.collect(Collectors.toList());

		if (filteredPriceQuantities.size() == 1) {
			return filteredPriceQuantities.get(0).toBuilder();
		} else if (filteredPriceQuantities.size() > 1) {
			LOGGER.warn("Multiple priceQuantity instances found with unitOfAmount.currency {} and rateOption.indexTenor {}", currency, rateOptionIndexTenor);
		}
		return null;
	}
	
	private boolean rateOptionIndexTenorMatches(PriceQuantity priceQuantity, Period indexTenor) {
		return Optional.ofNullable(priceQuantity)
				.map(PriceQuantity::getObservable)
				.map(Observable::getRateOption)
				.map(FieldWithMetaFloatingRateOption::getValue)
				.map(FloatingRateOption::getIndexTenor)
				.map(t -> Objects.equals(t.getPeriod(), indexTenor.getPeriod()) && Objects.equals(t.getPeriodMultiplier(), indexTenor.getPeriodMultiplier()))
				.orElse(false);
	}

	private boolean quantityUnitOfAmountMatches(PriceQuantity priceQuantity, String currency) {
		return emptyIfNull(priceQuantity.getQuantity()).stream()
				.map(FieldWithMetaQuantity::getValue)
				.map(MeasureBase::getUnitOfAmount)
				.map(UnitType::getCurrency)
				.map(FieldWithMetaString::getValue)
				.anyMatch(currency::equals);
	}

	private boolean priceUnitOfAmountMatches(PriceQuantity priceQuantity, String currency) {
		return emptyIfNull(priceQuantity.getPrice()).stream()
				.map(FieldWithMetaPrice::getValue)
				.map(MeasureBase::getUnitOfAmount)
				.map(UnitType::getCurrency)
				.map(FieldWithMetaString::getValue)
				.anyMatch(currency::equals);
	}
}
