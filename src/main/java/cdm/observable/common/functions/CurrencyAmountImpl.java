package cdm.observable.common.functions;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.observable.asset.PriceQuantity;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Extracts the quantity amount associated with the currency.
 */
public class CurrencyAmountImpl extends CurrencyAmount {

	@Override
	protected BigDecimal doEvaluate(List<? extends Quantity> quantity, String currency) {
		Set<BigDecimal> notionals = emptyIfNull(quantity).stream()
				.filter(Objects::nonNull)
				.filter(q -> Optional.ofNullable(q)
						.map(Quantity::getUnitOfAmount)
						.map(UnitType::getCurrency)
						.map(FieldWithMetaString::getValue)
						.map(c -> Optional.ofNullable(currency).map(c::equals).orElse(false))
						.orElse(false))
				.map(Quantity::getAmount)
				.collect(Collectors.toSet());
		if (notionals.size() > 1) {
			throw new IllegalArgumentException(String.format("Multiple Quantity instances found with unitOfAmount currency of %s, expected only one.", currency));
		}
		return notionals.stream().findFirst().orElse(null);
	}
}
