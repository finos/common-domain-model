package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.AccessConditions;
import cdm.legalagreement.csa.AdditionalTerminationEvent;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappings;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * ISDA Create mapping processor.
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
		AccessConditions.AccessConditionsBuilder accessConditionsBuilder = (AccessConditions.AccessConditionsBuilder) builder;
		accessConditionsBuilder.setAdditionalTerminationEvent(new ArrayList<>());

		Path eventsPath = accessConditionsPath.addElement("additional_termination_event");
		int index = 0;
		while (true) {
			Optional<AdditionalTerminationEvent> additionalTerminationEventBuilder = getAdditionalTerminationEvent(eventsPath, "name", index++);
			if (additionalTerminationEventBuilder.isPresent()) {
				accessConditionsBuilder.addAdditionalTerminationEvent(additionalTerminationEventBuilder.get());
			} else {
				break;
			}
		}
		getAdditionalTerminationEvent(accessConditionsPath, "specify", null)
				.ifPresent(accessConditionsBuilder::addAdditionalTerminationEvent);
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
