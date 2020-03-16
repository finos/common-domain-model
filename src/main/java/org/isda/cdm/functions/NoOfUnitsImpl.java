package org.isda.cdm.functions;

import java.util.List;
import java.util.Optional;

import org.isda.cdm.AssetIdentifier;
import org.isda.cdm.QuantityNotation;

/**
 * Extracts the quantity amount associated with the product identifier.
 */
public class NoOfUnitsImpl extends NoOfUnits {

	@Override
	protected QuantityNotation.QuantityNotationBuilder doEvaluate(List<QuantityNotation> quantityNotations) {
		return quantityNotations.stream()
				.filter(this::isProductAssetIdentifier)
				.findFirst()
				.map(QuantityNotation::toBuilder)
				.orElse(null);
	}

	private boolean isProductAssetIdentifier(QuantityNotation quantityNotation) {
		return Optional.ofNullable(quantityNotation)
				.map(QuantityNotation::getAssetIdentifier)
				.map(AssetIdentifier::getProductIdentifier)
				.isPresent();
	}
}
