package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.SpecifiedOrAccessConditionPartyElection;
import com.regnosys.rosetta.common.translation.*;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.*;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class AdditionalTerminationEventMappingHelper {

    private static final String APPLICABLE = "applicable";
    private static final List<String> SUFFIXES = Arrays.asList("_additional_termination_event", "_additional_termination_events");
    private final RosettaPath path;
    private final List<Mapping> mappings;

    public AdditionalTerminationEventMappingHelper(RosettaPath path, List<Mapping> mappings, SynonymToEnumMap synonymToEnumMap) {
        this.path = path;
        this.mappings = mappings;
    }

    public void map(Path accessConditionsPath, SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder accessConditionsBuilder, RosettaModelObjectBuilder parent) {
        if (accessConditionsBuilder.getSpecifiedAdditionalTerminationEvent() == null) {
            accessConditionsBuilder.setSpecifiedAdditionalTerminationEvent(new ArrayList<>());
        }
        Path eventsPath = accessConditionsPath.addElement("additional_termination_event");
        int index = 0;
        while (true) {
            Optional<List<String>> additionalTerminationEventBuilder = getSpecifiedAdditionalTerminationEvent(eventsPath, "name", index++);
            if (additionalTerminationEventBuilder.isPresent()) {
                accessConditionsBuilder.addSpecifiedAdditionalTerminationEvent(additionalTerminationEventBuilder.get());
            } else {
                break;
            }
        }
        getSpecifiedAdditionalTerminationEvent(accessConditionsPath, "specify", null)
                .ifPresent(accessConditionsBuilder::addSpecifiedAdditionalTerminationEvent);
    }

    private Optional<List<String>> getSpecifiedAdditionalTerminationEvent(Path basePath, String synonym, Integer index) {
        SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder eventBuilder = SpecifiedOrAccessConditionPartyElection.builder();
        setValueAndUpdateMappings(basePath.addElement(synonym, index), eventBuilder::addSpecifiedAdditionalTerminationEvent, mappings, path);
        boolean nameSet = eventBuilder.hasData();

        PARTIES.forEach(party ->
                SUFFIXES.forEach(suffix ->
                        setValueAndUpdateMappings(basePath.addElement(party + suffix, index),
                                (value) -> addIfApplicable(eventBuilder, party, value, nameSet), mappings, path)));

        boolean applicablePartySet = Optional.ofNullable(eventBuilder.getParty())
                .isPresent();

        if (nameSet || applicablePartySet) {
            updateMappings(basePath, mappings, path);
        }
        return eventBuilder.hasData() && applicablePartySet ? Optional.of(eventBuilder.getSpecifiedAdditionalTerminationEvent()) : Optional.empty();
    }

    private void addIfApplicable(SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder eventBuilder, String party, String value, boolean nameSet) {
        if (APPLICABLE.equals(value) && nameSet) {
            eventBuilder.setParty(toCounterpartyRoleEnum(party));
        }
    }
}
