package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.AccessConditions;
import org.isda.cdm.AdditionalTerminationEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class AdditionalTerminationEventMappingProcessor extends MappingProcessor {

	private static final String APPLICABLE = "applicable";
	private static final List<String> SUFFIXES = Arrays.asList("_additional_termination_event", "_additional_termination_events");

	public AdditionalTerminationEventMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	protected <R extends RosettaModelObject> void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		AccessConditions.AccessConditionsBuilder accessConditionsBuilder = (AccessConditions.AccessConditionsBuilder) builder;
		accessConditionsBuilder.clearAdditionalTerminationEvent();

		Path accessConditionsPath = Path.parse("answers.partyA.access_conditions");
		Path eventsPath = accessConditionsPath.addElement(Path.PathElement.parse("additional_termination_event"));

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

		setValueAndUpdateMappings(getSynonymPath(basePath, synonym, index), eventBuilder::setName);

		boolean nameSet = eventBuilder.hasData();

		PARTIES.forEach(party ->
				SUFFIXES.forEach(suffix ->
						setValueAndUpdateMappings(getSynonymPath(basePath, party, suffix, index),
								(value) -> addIfApplicable(eventBuilder, party, value, nameSet))));

		boolean applicablePartySet = !Optional.ofNullable(eventBuilder.getApplicableParty())
				.map(Collection::isEmpty)
				.orElse(true);

		if (nameSet || applicablePartySet) {
			updateMappings(basePath, getMappings(), getPath());
		}

		return eventBuilder.hasData() && applicablePartySet ? Optional.of(eventBuilder.build()) : Optional.empty();
	}

	private void addIfApplicable(AdditionalTerminationEvent.AdditionalTerminationEventBuilder eventBuilder, String party, String value, boolean nameSet) {
		if (APPLICABLE.equals(value) && nameSet) {
			eventBuilder.addApplicableParty(party);
		}
	}
}
