package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.SpecifiedOrAccessConditionPartyElection;
import com.regnosys.rosetta.common.translation.*;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.*;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * CreateiQ mapping processor helper.
 */
public class AdditionalTerminationEventMappingHelper {

    private static final String APPLICABLE = "applicable";
    private static final List<String> SUFFIXES = Arrays.asList("_additional_termination_event", "_additional_termination_events");
    private final RosettaPath path;
    private final List<Mapping> mappings;

    public AdditionalTerminationEventMappingHelper(RosettaPath path, List<Mapping> mappings) {
        this.path = path;
        this.mappings = mappings;
    }

    public void map(Path accessConditionsPath, SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder builder, String party) {
        Path eventsPath = accessConditionsPath.addElement("additional_termination_event");
        int index = 0;
        while (true) {
            Optional<List<String>> event = getSpecifiedAdditionalTerminationEvent(eventsPath, "name", index++, party);
            if (event.isPresent()) {
                builder.addSpecifiedAdditionalTerminationEvent(event.get());
            } else {
                break;
            }
        }
        getSpecifiedAdditionalTerminationEvent(accessConditionsPath, "specify", null, party)
                .ifPresent(builder::addSpecifiedAdditionalTerminationEvent);
    }

    private Optional<List<String>> getSpecifiedAdditionalTerminationEvent(Path basePath, String synonym, Integer index, String party) {
        SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder builder = SpecifiedOrAccessConditionPartyElection.builder();
        setValueAndUpdateMappings(basePath.addElement(synonym, index), builder::addSpecifiedAdditionalTerminationEvent, mappings, path);
        boolean nameSet = builder.hasData();

        SUFFIXES.forEach(suffix ->
                setValueAndUpdateMappings(basePath.addElement(party + suffix, index),
                        (value) -> addIfApplicable(builder, party, value, nameSet), mappings, path));

        boolean applicablePartySet = builder.getParty() != null;

        if (nameSet || applicablePartySet) {
            updateMappings(basePath, mappings, path);
        }

        return builder.hasData() && applicablePartySet ? Optional.of(builder.getSpecifiedAdditionalTerminationEvent()) : Optional.empty();
    }

    private void addIfApplicable(SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder builder, String party, String value, boolean nameSet) {
        if (APPLICABLE.equals(value) && nameSet) {
            builder.setParty(toCounterpartyRoleEnum(party));
        }
    }

}
