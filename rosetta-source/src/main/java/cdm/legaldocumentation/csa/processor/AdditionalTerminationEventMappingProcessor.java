package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.AdditionalTerminationEvent;
import cdm.legaldocumentation.csa.SpecifiedConditionOrAccessCondition;
import cdm.legaldocumentation.csa.SpecifiedOrAccessConditionPartyElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.*;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class AdditionalTerminationEventMappingProcessor extends MappingProcessor {

	private static final String APPLICABLE = "applicable";
	private static final List<String> SUFFIXES = Arrays.asList("_additional_termination_event", "_additional_termination_events");

	public AdditionalTerminationEventMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path accessConditionsPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		/*SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder specifiedConditionOrAccessConditionBuilder = (SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder) builder;
		specifiedConditionOrAccessConditionBuilder.setPartyElection(new ArrayList<>()); //.setAdditionalTerminationEvent(new ArrayList<>());

        SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder specifiedOrAccessConditionPartyElection =  (SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder) builder;
		Path eventsPath = accessConditionsPath.addElement("additional_termination_event");
		int index = 0;
		while (true) {

			Optional<AdditionalTerminationEvent> additionalTerminationEventBuilder = getAdditionalTerminationEvent(eventsPath, "name", index++);
			if (additionalTerminationEventBuilder.isPresent()) {
                specifiedOrAccessConditionPartyElection.addAdditionalTerminationEvent()
                AdditionalTerminationEvent additionalTerminationEvent = additionalTerminationEventBuilder.get();
                specifiedOrAccessConditionPartyElection.setAdditionalTerminationEvent(additionalTerminationEvent.getName())
                specifiedConditionOrAccessConditionBuilder.addPartyElection(specifiedOrAccessConditionPartyElection);// addAdditionalTerminationEvent(additionalTerminationEvent);
			} else {
				break;
			}
		}
		getAdditionalTerminationEvent(accessConditionsPath, "specify", null)
				.ifPresent(specifiedConditionOrAccessConditionBuilder::addPartyElection);*/
	}

	private Optional<AdditionalTerminationEvent> getAdditionalTerminationEvent(Path basePath, String synonym, Integer index) {
		AdditionalTerminationEvent.AdditionalTerminationEventBuilder eventBuilder = AdditionalTerminationEvent.builder();

		setValueAndUpdateMappings(basePath.addElement(synonym, index), eventBuilder::setName);

		boolean nameSet = eventBuilder.hasData();

		PARTIES.forEach(party ->
				SUFFIXES.forEach(suffix ->
						setValueAndUpdateMappings(basePath.addElement(party + suffix, index),
								(value) -> addIfApplicable(eventBuilder, party, value, nameSet))));

		boolean applicablePartySet = !Optional.ofNullable(eventBuilder.getApplicableParty())
				.map(Collection::isEmpty)
				.orElse(true);

		if (nameSet || applicablePartySet) {
			updateMappings(basePath, getMappings(), getModelPath());
		}

		return eventBuilder.hasData() && applicablePartySet ? Optional.of(eventBuilder.build()) : Optional.empty();
	}

	private void addIfApplicable(AdditionalTerminationEvent.AdditionalTerminationEventBuilder eventBuilder, String party, String value, boolean nameSet) {
		if (APPLICABLE.equals(value) && nameSet) {
			eventBuilder.addApplicableParty(toCounterpartyRoleEnum(party));
		}
	}
}
