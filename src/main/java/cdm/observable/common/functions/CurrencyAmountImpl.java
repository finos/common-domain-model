package cdm.observable.common.functions;

/**
 * Extracts the quantity amount associated with the currency.
 */
//public class CurrencyAmountImpl extends CurrencyAmount {
//
//	@Override
//	protected BigDecimal doEvaluate(List<QuantityNotation> quantityNotations, String currency) {
//		return quantityNotations.stream()
//				.filter(q -> isCurrencyAssetIdentifier(q, Optional.ofNullable(currency)))
//				.map(QuantityNotation::getQuantity)
//				.map(NonNegativeQuantity::getAmount)
//				.distinct()
//				.findFirst()
//				.orElse(null);
//	}
//
//	private boolean isCurrencyAssetIdentifier(QuantityNotation quantityNotation, Optional<String> currency) {
//		return Optional.ofNullable(quantityNotation)
//				.map(QuantityNotation::getAssetIdentifier)
//				.map(AssetIdentifier::getCurrency)
//				.map(FieldWithMeta::getValue)
//				.filter(c -> currency.map(c::equals).orElse(true))
//				.isPresent();
//	}
//}
