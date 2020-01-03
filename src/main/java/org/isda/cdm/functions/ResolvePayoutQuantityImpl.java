package org.isda.cdm.functions;

import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.meta.MetaFieldsI;
import org.isda.cdm.*;
import org.isda.cdm.ResolvablePayoutQuantity.ResolvablePayoutQuantityBuilder;
import org.isda.cdm.metafields.ReferenceWithMetaResolvablePayoutQuantity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResolvePayoutQuantityImpl extends ResolvePayoutQuantity {

	@Override
	protected ResolvablePayoutQuantityBuilder doEvaluate(ResolvablePayoutQuantity resolvableQuantity, Contract contract) {
		NonNegativeQuantity quantity;

		if (resolvableQuantity.getAssetIdentifier() != null) {
			quantity = resolveQuantityFromAssetIdentifier(resolvableQuantity.getAssetIdentifier(), contract);
		} else if (resolvableQuantity.getQuantityReference() != null) {
			quantity = resolveQuantityFromReference(resolvableQuantity.getQuantityReference(), contract);
		} else {
			throw new RuntimeException("No assetIdentifier nor quantityReference found");
		}

		return resolvableQuantity.toBuilder()
				.setQuantitySchedule(NonNegativeQuantitySchedule.builder()
						.setQuantity(quantity)
						.build());
	}

	private List<PayoutBase> getPayouts(Contract contract) {
		return Optional.ofNullable(contract)
				.map(Contract::getContractualProduct)
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

	private NonNegativeQuantity resolveQuantityFromAssetIdentifier(AssetIdentifier assetIdentifier, Contract contract) {
		List<NonNegativeQuantity> matchingQuantities = contract.getContractualQuantity().getQuantityNotation()
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
		return matchingQuantities.get(0);
	}

	private NonNegativeQuantity resolveQuantityFromReference(ReferenceWithMetaResolvablePayoutQuantity quantityReference, Contract contract) {
		String globalReference = quantityReference.getGlobalReference();
		AssetIdentifier referencedAssetIdentifier = getPayouts(contract)
				.stream()
				.map(PayoutBase::getPayoutQuantity)
				.filter(q -> globalKeyMatches(q, globalReference))
				.map(ResolvablePayoutQuantity::getAssetIdentifier)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No assetIdentifier found for global reference " + globalReference));
		return resolveQuantityFromAssetIdentifier(referencedAssetIdentifier, contract);
	}

	private boolean globalKeyMatches(GlobalKey key, String reference) {
		return Optional.ofNullable(key)
				.map(GlobalKey::getMeta)
				.map(MetaFieldsI::getGlobalKey)
				.map(reference::equals)
				.orElse(false);
	}
}
