package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.isda.cdm.AssetIdentifier;
import org.isda.cdm.NonNegativeQuantity;
import org.isda.cdm.QuantityNotation;

/**
 * Extracts the quantity amount associated with the product identifier.
 */
public class NoOfUnitsImpl extends NoOfUnits {

	@Override
	protected BigDecimal doEvaluate(List<QuantityNotation> quantityNotations) {
		return quantityNotations.stream()
				.filter(this::isProductAssetIdentifier)
				.map(QuantityNotation::getQuantity)
				.map(NonNegativeQuantity::getAmount)
				.findFirst()
				.orElse(BigDecimal.ZERO);
	}

	private boolean isProductAssetIdentifier(QuantityNotation quantityNotation) {
		return Optional.ofNullable(quantityNotation)
				.map(QuantityNotation::getAssetIdentifier)
				.map(AssetIdentifier::getProductIdentifier)
				.isPresent();
	}
}
