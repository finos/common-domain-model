package org.isda.cdm.functions;

import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.AllocationPrimitive.AllocationPrimitiveBuilder;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.isda.cdm.processor.EventEffectProcessStep;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sample Allocate implementation, should be used as a simple example only.
 *
 * TODO move to CDM exmaples repo
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

		Set<ReferenceWithMetaParty> eventParties = new HashSet<>();

		for(AllocationPrimitiveBuilder allocationBuilder : eventBuilder.getPrimitive().getAllocation()) {
			Set<ReferenceWithMetaParty> blockExecutionParties = new HashSet<>(allocationBuilder.build().getBefore().getExecution().getParty());

			eventParties.addAll(blockExecutionParties);

			// replace execution parties with references
			// before -> execution
			allocationBuilder.getBefore().getExecution()
					.clearParty()
					.addParty(replacePartyWithReference(blockExecutionParties));
			// after -> original trade
			allocationBuilder.getAfter().getOriginalTrade().getExecution()
					.clearParty()
					.addParty(replacePartyWithReference(blockExecutionParties));
			// after -> allocated trades
			allocationBuilder.getAfter().getAllocatedTrade().forEach(allocatedTradeBuilder -> {
				List<ReferenceWithMetaParty> allocatedExecutionParties = Optional.ofNullable(allocatedTradeBuilder.build().getExecution())
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
				.addParty(eventParties.stream().map(ReferenceWithMetaParty::getValue).collect(Collectors.toList()))
				.addTimestamp(getEventCreationTimestamp(ZonedDateTime.now()));

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Event.class, eventBuilder));

		return eventBuilder;
	}

	private List<ReferenceWithMetaParty> replacePartyWithReference(Collection<ReferenceWithMetaParty> parties) {
		return parties.stream()
				.map(p -> ReferenceWithMetaParty.builder()
						.setGlobalReference(p.getValue().getMeta().getGlobalKey())
						.setExternalReference(p.getValue().getMeta().getExternalKey())
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
