package org.isda.cdm.functions;

import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.meta.MetaFieldsI;
import org.isda.cdm.*;
import org.isda.cdm.NonNegativeQuantity.NonNegativeQuantityBuilder;
import org.isda.cdm.metafields.ReferenceWithMetaResolvablePayoutQuantity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResolvePayoutQuantityImpl extends ResolvePayoutQuantity {

	@Override
	protected NonNegativeQuantityBuilder doEvaluate(
			ResolvablePayoutQuantity resolvableQuantity,
			List<QuantityNotation> quantityNotations,
			ContractualProduct contractualProduct) {

		if (resolvableQuantity.getAssetIdentifier() != null) {
			return resolveQuantityFromAssetIdentifier(resolvableQuantity.getAssetIdentifier(), quantityNotations);
		} else if (resolvableQuantity.getQuantityReference() != null) {
			return resolveQuantityFromReference(resolvableQuantity.getQuantityReference(), quantityNotations, contractualProduct);
		} else {
			throw new RuntimeException("No assetIdentifier nor quantityReference found");
		}
	}

	/**
	 * Get the QuantityNotation corresponding to the given assetIdentifier.
	 */
	private NonNegativeQuantityBuilder resolveQuantityFromAssetIdentifier(AssetIdentifier assetIdentifier, List<QuantityNotation> quantityNotations) {
		List<NonNegativeQuantity> matchingQuantities = quantityNotations
				.stream()
				.filter(qn -> qn.getAssetIdentifier().equals(assetIdentifier))
				.distinct()
				.map(QuantityNotation::getQuantity)
				.collect(Collectors.toList());

		if (matchingQuantities.isEmpty()) {
			throw new RuntimeException("No quantity found for assetIdentifier " + assetIdentifier);
		}
		if (matchingQuantities.size() > 1) {
			throw new RuntimeException("Multiple quantity found for assetIdentifier " + assetIdentifier);
		}

		return matchingQuantities.get(0).toBuilder();
	}

	/**
	 * Find referenced assetIdentifier, then get the QuantityNotation corresponding to the given assetIdentifier.
	 */
	private NonNegativeQuantityBuilder resolveQuantityFromReference(
			ReferenceWithMetaResolvablePayoutQuantity quantityReference,
			List<QuantityNotation> quantityNotations,
			ContractualProduct contractualProduct) {
		return getReferencedAssetIdentifier(contractualProduct, quantityReference.getExternalReference())
				.map(assetIdentifier -> resolveQuantityFromAssetIdentifier(assetIdentifier, quantityNotations))
				.orElseThrow(() -> new RuntimeException("No assetIdentifier found for external reference " + quantityReference.getExternalReference()));
	}

	/**
	 * Find the referenced assetIdentifier based on the given external reference.
	 */
	private Optional<AssetIdentifier> getReferencedAssetIdentifier(ContractualProduct contractualProduct, String externalReference) {
		// Look in all the payouts that extend PayoutBase
		Optional<AssetIdentifier> assetIdentifierFromPayoutBase = getAssetIdentifierFromPayoutBase(contractualProduct, externalReference);
		if (assetIdentifierFromPayoutBase.isPresent()) {
			return assetIdentifierFromPayoutBase;
		}
		// Look in the CDS Protection Terms
		Optional<AssetIdentifier> assetIdentifierFromCdsProtectionTerms = getAssetIdentifierFromCdsProtectionTerms(contractualProduct, externalReference);
		if (assetIdentifierFromCdsProtectionTerms.isPresent()) {
			return assetIdentifierFromCdsProtectionTerms;
		}
		// Get all the ContractualProducts from any Underliers
		List<ContractualProduct> underlierContractualProducts = getUnderlierContractualProducts(contractualProduct.getEconomicTerms().getPayout());
		for (ContractualProduct underlierContractualProduct : underlierContractualProducts) {
			// Look in each ContractualProduct (E.g. recurse)
			Optional<AssetIdentifier> assetIdentifierFromUnderlier = getReferencedAssetIdentifier(underlierContractualProduct, externalReference);
			if (assetIdentifierFromUnderlier.isPresent()) {
				return assetIdentifierFromUnderlier;
			}
		}
		return Optional.empty();
	}

	private Optional<AssetIdentifier> getAssetIdentifierFromPayoutBase(ContractualProduct contractualProduct, String externalReference) {
		return getPayoutBases(contractualProduct)
				.stream()
				.map(PayoutBase::getPayoutQuantity)
				.filter(q -> externalKeyMatches(q, externalReference))
				.map(ResolvablePayoutQuantity::getAssetIdentifier)
				.findFirst();
	}

	private Optional<AssetIdentifier> getAssetIdentifierFromCdsProtectionTerms(ContractualProduct contractualProduct, String externalReference) {
		return Optional.ofNullable(contractualProduct)
				.map(ContractualProduct::getEconomicTerms)
				.map(EconomicTerms::getPayout)
				.map(Payout::getCreditDefaultPayout)
				.map(CreditDefaultPayout::getProtectionTerms)
				.orElse(Collections.emptyList())
				.stream()
				.map(ProtectionTerms::getPayoutQuantity)
				.filter(q -> externalKeyMatches(q, externalReference))
				.map(ResolvablePayoutQuantity::getAssetIdentifier)
				.findFirst();
	}

	/**
	 * Get all payouts that extend PayoutBase.
	 */
	private List<PayoutBase> getPayoutBases(ContractualProduct contractualProduct) {
		return Optional.ofNullable(contractualProduct)
				.map(ContractualProduct::getEconomicTerms)
				.map(EconomicTerms::getPayout)
				.map(p -> {
					List<PayoutBase> payouts = new ArrayList<>();
					Optional.ofNullable(p.getInterestRatePayout()).ifPresent(payouts::addAll);
					Optional.ofNullable(p.getEquityPayout()).ifPresent(payouts::addAll);
					Optional.ofNullable(p.getOptionPayout()).ifPresent(payouts::addAll);
					Optional.ofNullable(p.getCashflow()).ifPresent(payouts::addAll);
					return payouts;
				})
				.orElse(Collections.emptyList());
	}

	/**
	 * Get ContractualProduct for any Underliers
	 */
	private List<ContractualProduct> getUnderlierContractualProducts(Payout payout) {
		List<ContractualProduct> contractualProducts = new ArrayList<>();
		// Equity underlier
		Optional.ofNullable(payout)
				.map(Payout::getEquityPayout)
				.map(equityPayout -> equityPayout
						.stream()
						.map(EquityPayout::getUnderlier)
						.map(Underlier::getUnderlyingProduct)
						.map(Product::getContractualProduct)
						.collect(Collectors.toList()))
				.ifPresent(contractualProducts::addAll);
		// Option underlier
		Optional.ofNullable(payout)
				.map(Payout::getOptionPayout)
				.map(optionPayout -> optionPayout
						.stream()
						.map(OptionPayout::getUnderlier)
						.map(Underlier::getUnderlyingProduct)
						.map(Product::getContractualProduct)
						.collect(Collectors.toList()))
				.ifPresent(contractualProducts::addAll);
		// Forward underlier
		Optional.ofNullable(payout)
				.map(Payout::getForwardPayout)
				.map(forwardPayout -> forwardPayout
						.stream()
						.map(ForwardPayout::getUnderlier)
						.map(Underlier::getUnderlyingProduct)
						.map(Product::getContractualProduct)
						.collect(Collectors.toList()))
				.ifPresent(contractualProducts::addAll);

		return contractualProducts;
	}

	private boolean externalKeyMatches(GlobalKey key, String reference) {
		return Optional.ofNullable(key)
				.map(GlobalKey::getMeta)
				.map(MetaFieldsI::getExternalKey)
				.map(reference::equals)
				.orElse(false);
	}
}
