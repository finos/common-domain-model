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
public class EquityNotionalImpl extends EquityNotional {

	@Override
	protected BigDecimal doEvaluate(List<QuantityNotation> quantityNotations) {
		return quantityNotations.stream()
				.filter(this::isCurrencyAssetIdentifier)
				.map(QuantityNotation::getQuantity)
				.map(NonNegativeQuantity::getAmount)
				.findFirst()
				.orElse(BigDecimal.ZERO);
	}

	private boolean isCurrencyAssetIdentifier(QuantityNotation quantityNotation) {
		return Optional.ofNullable(quantityNotation)
				.map(QuantityNotation::getAssetIdentifier)
				.map(AssetIdentifier::getCurrency)
				.map(FieldWithMeta::getValue)
				.isPresent();
	}
}
