package cdm.observable.common.functions;

/**
 * Extracts the quantity amount associated with the product identifier.
 */
//public class NoOfUnitsImpl extends NoOfUnits {
//
//	@Override
//	protected BigDecimal doEvaluate(List<QuantityNotation> quantityNotations) {
//		return quantityNotations.stream()
//				.filter(this::isProductAssetIdentifier)
//				.map(QuantityNotation::getQuantity)
//				.map(NonNegativeQuantity::getAmount)
//				.findFirst()
//				.orElse(null);
//	}
//
//	private boolean isProductAssetIdentifier(QuantityNotation quantityNotation) {
//		return Optional.ofNullable(quantityNotation)
//				.map(QuantityNotation::getAssetIdentifier)
//				.map(AssetIdentifier::getProductIdentifier)
//				.isPresent();
//	}
//}
