package cdm.product.common.functions;

//public class ResolvePayoutQuantityImpl extends ResolvePayoutQuantity {
//
//	@Override
//	protected NonNegativeQuantityBuilder doEvaluate(
//			ResolvablePayoutQuantity resolvableQuantity,
//			List<QuantityNotation> quantityNotations,
//			ContractualProduct contractualProduct) {
//
//		if (quantityNotations == null) {
//			return NonNegativeQuantity.builder();
//		} else if (resolvableQuantity.getAssetIdentifier() != null) {
//			return resolveQuantityFromAssetIdentifier(resolvableQuantity.getAssetIdentifier(), quantityNotations);
//		} else if (resolvableQuantity.getQuantityReference() != null) {
//			return resolveQuantityFromReference(resolvableQuantity.getQuantityReference(), quantityNotations, contractualProduct);
//		} else {
//			throw new RuntimeException("No assetIdentifier nor quantityReference found");
//		}
//	}
//
//	/**
//	 * Get the QuantityNotation corresponding to the given assetIdentifier.
//	 */
//	private NonNegativeQuantityBuilder resolveQuantityFromAssetIdentifier(AssetIdentifier assetIdentifier, List<QuantityNotation> quantityNotations) {
//		List<NonNegativeQuantity> matchingQuantities = quantityNotations
//				.stream()
//				.filter(qn -> qn.getAssetIdentifier().equals(assetIdentifier))
//				.distinct()
//				.map(QuantityNotation::getQuantity)
//				.collect(Collectors.toList());
//
//		if (matchingQuantities.isEmpty()) {
//			throw new RuntimeException("No quantity found for assetIdentifier " + assetIdentifier);
//		}
//		if (matchingQuantities.size() > 1) {
//			throw new RuntimeException("Multiple quantity found for assetIdentifier " + assetIdentifier);
//		}
//
//		return matchingQuantities.get(0).toBuilder();
//	}
//
//	/**
//	 * Find referenced assetIdentifier, then get the QuantityNotation corresponding to the given assetIdentifier.
//	 */
//	private NonNegativeQuantityBuilder resolveQuantityFromReference(
//			ReferenceWithMetaResolvablePayoutQuantity quantityReference,
//			List<QuantityNotation> quantityNotations,
//			ContractualProduct contractualProduct) {
//
//		String globalReference = quantityReference.getGlobalReference();
//		return getResolvablePayoutQuantity(contractualProduct, globalReference)
//				.map(ResolvablePayoutQuantity::getAssetIdentifier)
//				.map(assetIdentifier -> resolveQuantityFromAssetIdentifier(assetIdentifier, quantityNotations))
//				.orElseThrow(() -> new RuntimeException("Failed to resolve payout quantity with global reference " + globalReference));
//	}
//
//	/**
//	 * Find the referenced assetIdentifier based on the given global reference.
//	 */
//	private Optional<ResolvablePayoutQuantity> getResolvablePayoutQuantity(ContractualProduct contractualProduct, String globalReference) {
//		return new ReferenceResolverProcessStep()
//				.runProcessStep(ContractualProduct.class, contractualProduct.toBuilder())
//				.getReferencedObject(ResolvablePayoutQuantity.class, globalReference);
//	}
//}
