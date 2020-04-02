package org.isda.cdm.functions;

import java.util.List;
import java.util.Optional;

import org.isda.cdm.AssetIdentifier;
import org.isda.cdm.CashPrice;
import org.isda.cdm.CashPrice.CashPriceBuilder;
import org.isda.cdm.Price;
import org.isda.cdm.PriceNotation;
import org.isda.cdm.Product;
import org.isda.cdm.Underlier;

import cdm.base.staticdata.asset.common.Equity;
import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.asset.common.Security;

/**
 * To be replaced by full resolve price function implementation.
 */
public class ResolveEquityInitialPriceImpl extends ResolveEquityInitialPrice {

	@Override
	protected CashPriceBuilder doEvaluate(Underlier underlier, List<PriceNotation> priceNotations) {
		ProductIdentifier underlierProductIdentifier = Optional.ofNullable(underlier)
				.map(Underlier::getUnderlyingProduct)
				.map(Product::getSecurity)
				.map(Security::getEquity)
				.map(Equity::getProductIdentifier)
				.orElseThrow(() -> new RuntimeException("No product identifier found for Equity underlier"));
		
		return priceNotations.stream()
				.filter(n -> matches(n, underlierProductIdentifier))
				.map(PriceNotation::getPrice)
				.map(Price::getCashPrice)
				.map(CashPrice::toBuilder)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No price found for product identifier " + underlierProductIdentifier));
	}

	private boolean matches(PriceNotation priceNotation, ProductIdentifier underlierProductIdentifier) {
		return Optional.ofNullable(priceNotation)
				.map(PriceNotation::getAssetIdentifier)
				.map(AssetIdentifier::getProductIdentifier)
				.map(underlierProductIdentifier::equals)
				.orElse(false);
	}
}
