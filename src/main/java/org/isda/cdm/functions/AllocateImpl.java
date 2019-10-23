package org.isda.cdm.functions;

import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.isda.cdm.Event.EventBuilder;

/**
 * Sample Allocate implementation, should be used as a simple example only.
 *
 * TODO move to CDM demo repo
 */
public class AllocateImpl extends Allocate {

	private final List<PostProcessStep> postProcessors;

	public AllocateImpl() {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(RosettaKeyValueHashFunction::new),
				new ReKeyProcessStep(rosettaKeyProcessStep));
	}

	@Override
	protected EventBuilder doEvaluate(Execution execution, AllocationInstructions allocationInstructions, Event previousEvent) {
		EventBuilder eventBuilder = Event.builder();

		Set<ReferenceWithMetaParty> eventParties = new HashSet<>();

		// add parties from block execution
		eventParties.addAll(execution.getParty());
		// add parties from the allocation instructions
		eventParties.addAll(allocationInstructions.getBreakdowns()
				.stream()
				.map(AllocationBreakdown::getPartyReference)
				.collect(Collectors.toSet()));

		eventBuilder
				.addEventIdentifier(getIdentifier("allocationEvent1", 1))
				.setEventDate(DateImpl.of(LocalDate.now()))
				.addParty(eventParties.stream().map(ReferenceWithMetaParty::getValue).collect(Collectors.toList()))
				.addTimestamp(getEventCreationTimestamp(ZonedDateTime.now()));

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Event.class, eventBuilder));

		return eventBuilder;
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
