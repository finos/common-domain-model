package org.isda.cdm.functions;

import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.Event.EventBuilder;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.isda.cdm.processor.EventEffectProcessStep;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.isda.cdm.PartyRoleEnum.CLIENT;
import static org.isda.cdm.PartyRoleEnum.COUNTERPARTY;

/**
 * Sample Settle implementation, should be used as a simple example only.
 *
 * TODO move to CDM demo repo
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
	protected EventBuilder doEvaluate(Execution execution, Event previousEvent) {
		EventBuilder eventBuilder = Event.builder();

		if (!isDeliveryVsPayment(execution)) {
			throw new IllegalArgumentException("Only executions with transferSettlementType of DELIVERY_VERSUS_PAYMENT are supported");
		}

		Set<ReferenceWithMetaParty> eventParties = new HashSet<>();
		eventParties.add(getPartyReference(execution, CLIENT));
		eventParties.add(getPartyReference(execution, COUNTERPARTY));

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
