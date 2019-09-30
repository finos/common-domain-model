package org.isda.cdm.functions;

import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.Event.EventBuilder;
import org.isda.cdm.TransferPrimitive.TransferPrimitiveBuilder;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaAccount;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.isda.cdm.processor.EventEffectProcessStep;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.isda.cdm.SecurityTransferComponent.*;

/**
 * Sample Settle implementation, should be used as a simple example only.
 *
 * TODO move to CDM exmaples repo
 */
public class SettleImpl extends Settle {

	private final List<PostProcessStep> postProcessors;

	public SettleImpl() {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(RosettaKeyValueHashFunction::new),
				new ReKeyProcessStep(rosettaKeyProcessStep),
				new EventEffectProcessStep(rosettaKeyProcessStep));
	}

	@Override
	protected EventBuilder doEvaluate(Execution execution) {
		EventBuilder eventBuilder = super.doEvaluate(execution);

		if (!isDeliveryVsPayment(execution)) {
			throw new IllegalArgumentException("Only executions with transferSettlementType of DELIVERY_VERSUS_PAYMENT are supported");
		}

		Set<ReferenceWithMetaParty> eventParties = new HashSet<>();

		for(TransferPrimitiveBuilder transferPrimitiveBuilder : eventBuilder.getPrimitive().getTransfer()) {

			// extract cash transfer parties to stamp on event
			TransferPrimitive transferPrimitive = transferPrimitiveBuilder.build();
			List<CashTransferComponent> cashTransfer = transferPrimitive.getCashTransfer();
			Party payerParty = cashTransfer.stream()
					.map(CashTransferComponent::getPayerReceiver)
					.map(PayerReceiver::getPayerPartyReference)
					.map(ReferenceWithMetaParty::getValue)
					.collect(MoreCollectors.onlyElement());
			eventParties.add(ReferenceWithMetaParty.builder().setValue(payerParty).build());

			Party receiverParty = cashTransfer.stream()
					.map(CashTransferComponent::getPayerReceiver)
					.map(PayerReceiver::getReceiverPartyReference)
					.map(ReferenceWithMetaParty::getValue)
					.collect(MoreCollectors.onlyElement());
			eventParties.add(ReferenceWithMetaParty.builder().setValue(receiverParty).build());

			// add party / account references to cash transfer
			getPayerReceiver(transferPrimitiveBuilder)
					.setPayerPartyReference(getPartyReference(payerParty))
					.setPayerAccountReference(getAccountReference(payerParty))
					.setReceiverPartyReference(getPartyReference(receiverParty))
					.setReceiverAccountReference(getAccountReference(receiverParty));

			// extract security transfer parties to stamp on event
			List<SecurityTransferComponent> securityTransfer = transferPrimitive.getSecurityTransfer();
			Party transfereeParty = securityTransfer.stream()
					.map(SecurityTransferComponent::getTransferorTransferee)
					.map(TransferorTransferee::getTransfereePartyReference)
					.map(ReferenceWithMetaParty::getValue)
					.collect(MoreCollectors.onlyElement());
			eventParties.add(ReferenceWithMetaParty.builder().setValue(transfereeParty).build());

			Party transferorParty = securityTransfer.stream()
					.map(SecurityTransferComponent::getTransferorTransferee)
					.map(TransferorTransferee::getTransferorPartyReference)
					.map(ReferenceWithMetaParty::getValue)
					.collect(MoreCollectors.onlyElement());
			eventParties.add(ReferenceWithMetaParty.builder().setValue(transferorParty).build());

			// add party / account references to security transfer
			getTransferorTransferee(transferPrimitiveBuilder)
					.setTransfereePartyReference(getPartyReference(transfereeParty))
					.setTransfereeAccountReference(getAccountReference(transfereeParty))
					.setTransferorPartyReference(getPartyReference(transferorParty))
					.setTransferorAccountReference(getAccountReference(transferorParty));
		}

		eventBuilder
				.addEventIdentifier(getIdentifier("settleEvent1", 1))
				.setEventDate(DateImpl.of(LocalDate.now()))
				.addParty(eventParties.stream().map(ReferenceWithMetaParty::getValue).collect(Collectors.toList()))
				.addTimestamp(getEventCreationTimestamp(ZonedDateTime.now()));

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Event.class, eventBuilder));

		return eventBuilder;
	}

	private Boolean isDeliveryVsPayment(Execution execution) {
		return Optional.ofNullable(execution.getSettlementTerms())
				.map(SettlementTerms::getTransferSettlementType)
				.map(t -> t == TransferSettlementEnum.DELIVERY_VERSUS_PAYMENT)
				.orElse(false);
	}

	private PayerReceiver.PayerReceiverBuilder getPayerReceiver(TransferPrimitiveBuilder transferPrimitiveBuilder) {
		return transferPrimitiveBuilder.getCashTransfer().stream()
				.map(CashTransferComponent.CashTransferComponentBuilder::getPayerReceiver)
				.collect(MoreCollectors.onlyElement());
	}

	private TransferorTransferee.TransferorTransfereeBuilder getTransferorTransferee(TransferPrimitiveBuilder transferPrimitiveBuilder) {
		return transferPrimitiveBuilder.getSecurityTransfer().stream()
				.map(SecurityTransferComponentBuilder::getTransferorTransferee)
				.collect(MoreCollectors.onlyElement());
	}

	private ReferenceWithMetaAccount getAccountReference(Party party) {
		return ReferenceWithMetaAccount.builder()
				.setGlobalReference(party.getMeta().getGlobalKey())
				.setExternalReference(party.getMeta().getExternalKey())
				.build();
	}

	private ReferenceWithMetaParty getPartyReference(Party party) {
		return ReferenceWithMetaParty.builder()
				.setGlobalReference(party.getMeta().getGlobalKey())
				.setExternalReference(party.getMeta().getExternalKey())
				.build();
	}

	private EventTimestamp getEventCreationTimestamp(ZonedDateTime eventDateTime) {
		return EventTimestamp.builder()
				.setDateTime(eventDateTime)
				.setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME)
				.build();
	}

	private Identifier getIdentifier(String id, int version) {
		return Identifier.builder()
				.addAssignedIdentifierBuilder(AssignedIdentifier.builder()
						.setIdentifier(FieldWithMetaString.builder().setValue(id).build())
						.setVersion(version))
				.build();
	}
}
