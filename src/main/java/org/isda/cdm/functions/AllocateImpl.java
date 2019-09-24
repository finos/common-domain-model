package org.isda.cdm.functions;

import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.AllocationPrimitive.AllocationPrimitiveBuilder;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.processor.EventEffectProcessStep;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sample Allocate implementation, should be used as a simple example only.
 */
public class AllocateImpl extends Allocate {

	private final List<PostProcessStep> postProcessors;

	public AllocateImpl() {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(RosettaKeyValueHashFunction::new),
				new ReKeyProcessStep(rosettaKeyProcessStep),
				new EventEffectProcessStep(rosettaKeyProcessStep));
	}

	@Override
	protected Event.EventBuilder doEvaluate(Execution execution, AllocationInstructions allocationInstructions) {
		Event.EventBuilder eventBuilder = super.doEvaluate(execution, allocationInstructions);

		Set<Party> eventParties = new HashSet<>();

		for(AllocationPrimitiveBuilder allocationBuilder : eventBuilder.getPrimitive().getAllocation()) {
			Set<Party> blockExecutionParties = new HashSet<>(allocationBuilder.build().getBefore().getExecution().getParty());

			eventParties.addAll(blockExecutionParties);

			// replace execution parties with references
			allocationBuilder.getBefore().getExecution()
					.clearParty()
					.addParty(replacePartyWithReference(blockExecutionParties));
			allocationBuilder.getAfter().getOriginalTrade().getExecution()
					.clearParty()
					.addParty(replacePartyWithReference(blockExecutionParties));
			allocationBuilder.getAfter().getAllocatedTrade().forEach(allocatedTradeBuilder -> {
				List<Party> allocatedExecutionParties = Optional.ofNullable(allocatedTradeBuilder.build().getExecution())
						.map(Execution::getParty)
						.orElse(Collections.emptyList());

				eventParties.addAll(allocatedExecutionParties);

				allocatedTradeBuilder.getExecution()
						.clearParty()
						.addParty(replacePartyWithReference(allocatedExecutionParties));
			});
		}

		eventBuilder
				.addEventIdentifier(getIdentifier("allocationEvent1", 1))
				.setEventDate(DateImpl.of(LocalDate.now()))
				.addParty(new ArrayList<>(eventParties))
				.addTimestamp(getEventCreationTimestamp(ZonedDateTime.now()));

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Event.class, eventBuilder));

		return eventBuilder;
	}

	private List<Party> replacePartyWithReference(Collection<Party> parties) {
		return parties.stream()
				.map(p -> Party.builder().setMetaBuilder(MetaFields.builder()
						.setGlobalKey(p.getMeta().getGlobalKey())
						.setExternalKey(p.getMeta().getExternalKey()))
						.build())
				.collect(Collectors.toList());
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
