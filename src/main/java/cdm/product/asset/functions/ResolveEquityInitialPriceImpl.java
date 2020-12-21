package cdm.product.asset.functions;

import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.asset.common.Security;
import cdm.observable.asset.*;
import cdm.product.template.Product;
import cdm.product.template.Underlier;

import java.util.List;
import java.util.Optional;

import static cdm.observable.asset.Price.PriceBuilder;

/**
 * To be replaced by full resolve price function implementation.
 */
public class ResolveEquityInitialPriceImpl extends ResolveEquityInitialPrice {

	@Override
	protected PriceBuilder doEvaluate(Underlier underlier, List<PriceQuantity> priceQuantity) {
//		List<ProductIdentifier> underlierProductIdentifiers = Optional.ofNullable(underlier)
//				.map(Underlier::getUnderlyingProduct)
//				.map(Product::getSecurity)
//				.map(Security::getProductIdentifier)
//				.orElseThrow(() -> new RuntimeException("No product identifier found for Equity underlier"));
//
//		return priceQuantity.stream()
//				.filter(pq -> matches(pq, underlierProductIdentifiers))
//				.map(PriceQuantity::getPrice)
//				.map(Price::toBuilder)
//				.findFirst()
//				.orElseThrow(() -> new RuntimeException("No price found for product identifier " + underlierProductIdentifiers));
		return null;
	}

	private boolean matches(PriceQuantity priceQuantity, List<ProductIdentifier> underlierProductIdentifier) {
		return Optional.ofNullable(priceQuantity)
				.map(PriceQuantity::getObservable)
				.map(Observable::getProductIdentifier)
				.map(underlierProductIdentifier::contains)
				.orElse(false);
	}
}
