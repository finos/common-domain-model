package org.isda.cdm.functions;

import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.*;
import org.isda.cdm.TransferPrimitive.TransferPrimitiveBuilder;
import org.isda.cdm.metafields.FieldWithMetaDate;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.util.*;

import static org.isda.cdm.PartyRoleEnum.*;

/**
 * Sample NewTransferPrimitive implementation, should be used as a simple example only.
 *
 * TODO move to CDM exmaples repo
 */
public class NewTransferPrimitiveImpl extends NewTransferPrimitive {

	private final List<PostProcessStep> postProcessors;

	public NewTransferPrimitiveImpl() {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep, // Calculates rosetta keys
				new RosettaKeyValueProcessStep(RosettaKeyValueHashFunction::new), // Calculates rosetta key values
				new ReKeyProcessStep(rosettaKeyProcessStep)); // Uses external key/references to populate global reference (which refer to keys generated in earlier steps)
	}

	@Override
	protected TransferPrimitiveBuilder doEvaluate(Execution execution) {
		TransferPrimitiveBuilder transferPrimitiveBuilder = super.doEvaluate(execution);
		
		if (!isDeliveryVsPayment(execution)) {
			throw new IllegalArgumentException("Only executions with transferSettlementType of DELIVERY_VERSUS_PAYMENT are supported");
		}

		ReferenceWithMetaParty clientParty = getPartyReference(execution, CLIENT);
		ReferenceWithMetaParty counterpartyBrokerParty = getPartyReference(execution, COUNTERPARTY);

		transferPrimitiveBuilder
				.setIdentifier(getIdentifier(execution))
				.setSettlementDate(getSettlementDate(execution))
				.setSettlementType(TransferSettlementEnum.DELIVERY_VERSUS_PAYMENT)
				.addCashTransfer(getCashTransfer(execution, clientParty, counterpartyBrokerParty))
				.addSecurityTransfer(getSecurityTransfer(execution, clientParty, counterpartyBrokerParty));

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(TransferPrimitive.class, transferPrimitiveBuilder));

		return transferPrimitiveBuilder;
	}

	private Boolean isDeliveryVsPayment(Execution execution) {
		return Optional.ofNullable(execution.getSettlementTerms())
				.map(SettlementTerms::getTransferSettlementType)
				.map(t -> t == TransferSettlementEnum.DELIVERY_VERSUS_PAYMENT)
				.orElse(false);
	}

	private FieldWithMetaString getIdentifier(Execution execution) {
		String tradeId = execution.getIdentifier()
				.stream()
				.map(Identifier::getAssignedIdentifier)
				.flatMap(Collection::stream)
				.map(AssignedIdentifier::getIdentifier)
				.map(FieldWithMetaString::getValue)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException(("No identifier found on execution " + execution)));

		return FieldWithMetaString.builder()
				.setValue("transfer_" + tradeId)
				.build();
	}

	private AdjustableOrAdjustedOrRelativeDate getSettlementDate(Execution execution) {
		FieldWithMetaDate settlementDate = Optional.ofNullable(execution.getSettlementTerms())
				.map(SettlementTerms::getSettlementDate)
				.map(AdjustableOrRelativeDate::getAdjustableDate)
				.map(AdjustableDate::getAdjustedDate)
				.orElseThrow(() -> new IllegalArgumentException("No settlementDate found on execution " + execution));

		return AdjustableOrAdjustedOrRelativeDate.builder()
				.setAdjustedDate(settlementDate)
				.build();
	}

	private CashTransferComponent getCashTransfer(Execution execution, ReferenceWithMetaParty clientParty, ReferenceWithMetaParty counterpartyBrokerParty) {
		boolean isCounterpartyBuyer = isBuyer(execution, counterpartyBrokerParty);

		return CashTransferComponent.builder()
				.setAmount(execution.getSettlementTerms().getSettlementAmount())
				.setPayerReceiverBuilder(PayerReceiver.builder()
						.setPayerPartyReference(isCounterpartyBuyer ? counterpartyBrokerParty : clientParty)
						.setReceiverPartyReference(isCounterpartyBuyer ? clientParty : counterpartyBrokerParty))
				.build();
	}

	private ReferenceWithMetaParty getPartyReference(Execution execution, PartyRoleEnum partyRole) {
		String partyExternalReference = execution.getPartyRole()
				.stream()
				.filter(r -> r.getRole() == partyRole)
				.collect(MoreCollectors.onlyElement())
				.getPartyReference()
				.getExternalReference();
		ReferenceWithMetaParty party = execution.getParty()
				.stream()
				.filter(p -> p.getValue().getMeta().getExternalKey().equals(partyExternalReference))
				.collect(MoreCollectors.onlyElement());

		if (!isAccountSet(party)) {
			throw new IllegalArgumentException("No account found on party " + party);
		}

		return party;
	}

	private Boolean isAccountSet(ReferenceWithMetaParty partyReference) {
		return Optional.ofNullable(partyReference.getValue())
				.map(Party::getAccount).map(Objects::nonNull)
				.orElse(false);
	}

	private SecurityTransferComponent getSecurityTransfer(Execution execution, ReferenceWithMetaParty clientParty, ReferenceWithMetaParty counterpartyBrokerParty) {
		boolean isCounterpartyBuyer = isBuyer(execution, counterpartyBrokerParty);

		return SecurityTransferComponent.builder()
				.setQuantity(execution.getQuantity().getAmount())
				.setSecurity(execution.getProduct().getSecurity())
				.setTransferorTransfereeBuilder(TransferorTransferee.builder()
						.setTransfereePartyReference(isCounterpartyBuyer ? counterpartyBrokerParty : clientParty)
						.setTransferorPartyReference(isCounterpartyBuyer ? clientParty : counterpartyBrokerParty))
				.build();
	}

	private boolean isBuyer(Execution execution, ReferenceWithMetaParty partyReference) {
		PartyRoleEnum buyOrSell = execution.getPartyRole().stream()
				.filter(p -> p.getPartyReference().getExternalReference().equals(partyReference.getValue().getMeta().getExternalKey()))
				.map(r -> r.getRole())
				.filter(r -> r == BUYER || r == SELLER)
				.collect(MoreCollectors.toOptional())
				.orElseThrow(() -> new IllegalArgumentException("Party does not have either BUYER or SELLER roles " + partyReference.getExternalReference()));
		return buyOrSell == BUYER;
	}
}
