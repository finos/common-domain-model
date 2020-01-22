package org.isda.cdm.functions;

import java.util.List;
import java.util.Optional;

import org.isda.cdm.AssetIdentifier;
import org.isda.cdm.Equity;
import org.isda.cdm.NonNegativeQuantity;
import org.isda.cdm.NonNegativeQuantity.NonNegativeQuantityBuilder;
import org.isda.cdm.Product;
import org.isda.cdm.ProductIdentifier;
import org.isda.cdm.QuantityNotation;
import org.isda.cdm.Security;
import org.isda.cdm.Underlier;

public class ResolveEquityNoOfUnitsImpl extends ResolveEquityNoOfUnits {

	@Override
	protected NonNegativeQuantityBuilder doEvaluate(Underlier underlier, List<QuantityNotation> quantityNotations) {
		ProductIdentifier underlierProductIdentifier = Optional.ofNullable(underlier)
				.map(Underlier::getUnderlyingProduct)
				.map(Product::getSecurity)
				.map(Security::getEquity)
				.map(Equity::getProductIdentifier)
				.orElseThrow(() -> new RuntimeException("No product identifier found for Equity underlier"));
		
		return quantityNotations.stream()
				.filter(n -> matches(n, underlierProductIdentifier))
				.map(QuantityNotation::getQuantity)
				.map(NonNegativeQuantity::toBuilder)
				.findFirst()
				.orElse(NonNegativeQuantity.builder());
	}

	private boolean matches(QuantityNotation quantityNotation, ProductIdentifier underlierProductIdentifier) {
		return Optional.ofNullable(quantityNotation)
				.map(QuantityNotation::getAssetIdentifier)
				.map(AssetIdentifier::getProductIdentifier)
				.map(underlierProductIdentifier::equals)
				.orElse(false);
	}

}
