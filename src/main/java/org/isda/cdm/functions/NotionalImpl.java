package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.isda.cdm.AssetIdentifier;
import org.isda.cdm.NonNegativeQuantity;
import org.isda.cdm.QuantityNotation;

import com.rosetta.model.lib.meta.FieldWithMeta;

/**
 * Extracts the quantity amount associated with the currency.
 */
public class NotionalImpl extends Notional {

	@Override
	protected BigDecimal doEvaluate(List<QuantityNotation> quantityNotations, String currency) {
		return quantityNotations.stream()
				.filter(q -> isCurrencyAssetIdentifier(q, Optional.ofNullable(currency)))
				.map(QuantityNotation::getQuantity)
				.map(NonNegativeQuantity::getAmount)
				.distinct()
				.findFirst()
				.orElse(BigDecimal.ZERO);
	}

	private boolean isCurrencyAssetIdentifier(QuantityNotation quantityNotation, Optional<String> currency) {
		return Optional.ofNullable(quantityNotation)
				.map(QuantityNotation::getAssetIdentifier)
				.map(AssetIdentifier::getCurrency)
				.map(FieldWithMeta::getValue)
				.filter(c -> currency.map(c::equals).orElse(true))
				.isPresent();
	}
}
