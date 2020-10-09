package cdm.product.asset.functions;

import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.asset.common.Security;
import cdm.observable.asset.AssetIdentifier;
import cdm.observable.asset.CashPrice;
import cdm.observable.asset.CashPrice.CashPriceBuilder;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceNotation;
import cdm.product.template.Product;
import cdm.product.template.Underlier;

import java.util.List;
import java.util.Optional;

/**
 * To be replaced by full resolve price function implementation.
 */
public class ResolveEquityInitialPriceImpl extends ResolveEquityInitialPrice {

	@Override
	protected CashPriceBuilder doEvaluate(Underlier underlier, List<PriceNotation> priceNotations) {
		List<ProductIdentifier> underlierProductIdentifiers = Optional.ofNullable(underlier)
				.map(Underlier::getUnderlyingProduct)
				.map(Product::getSecurity)
				.map(Security::getProductIdentifier)
				.orElseThrow(() -> new RuntimeException("No product identifier found for Equity underlier"));
		
		return priceNotations.stream()
				.filter(n -> matches(n, underlierProductIdentifiers))
				.map(PriceNotation::getPrice)
				.map(Price::getCashPrice)
				.map(CashPrice::toBuilder)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No price found for product identifier " + underlierProductIdentifiers));
	}

	private boolean matches(PriceNotation priceNotation, List<ProductIdentifier> underlierProductIdentifier) {
		return Optional.ofNullable(priceNotation)
				.map(PriceNotation::getAssetIdentifier)
				.map(AssetIdentifier::getProductIdentifier)
				.map(underlierProductIdentifier::contains)
				.orElse(false);
	}
}
