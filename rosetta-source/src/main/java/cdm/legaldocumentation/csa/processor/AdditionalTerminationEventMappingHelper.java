package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.SpecifiedOrAccessConditionPartyElection;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getValueAndUpdateMappings;

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

    public void mapAdditionalTerminationEvent(SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder partyElectionBuilder, Path accessConditionsPath, String party) {
        Path basePath = accessConditionsPath.addElement("additional_termination_event");
        int index = 0;
        while (true) {
            List<String> terminationEvents = getSpecifiedAdditionalTerminationEvents(basePath, "name", index++, party);
            if (terminationEvents.isEmpty()) {
                break;
            } else {
                partyElectionBuilder.addSpecifiedAdditionalTerminationEvent(terminationEvents);
            }
        }

        List<String> specifyEvents = getSpecifiedAdditionalTerminationEvents(accessConditionsPath, "specify", null, party);
        partyElectionBuilder.addSpecifiedAdditionalTerminationEvent(specifyEvents);
    }

    private List<String> getSpecifiedAdditionalTerminationEvents(Path basePath, String synonym, Integer index, String party) {
        List<String> events = new ArrayList<>();

        // first get the event name
        Path eventNameSynonymPath = basePath.addElement(synonym, index);
        getValueAndUpdateMappings(eventNameSynonymPath, mappings, path)
                .ifPresent(eventName ->
                        // then check if the event is applicable for the given party
                        SUFFIXES.forEach(suffix ->
                        {
                            Path eventApplicableToPartySynonymPath = basePath.addElement(party + suffix, index);
                            getValueAndUpdateMappings(eventApplicableToPartySynonymPath, mappings, path)
                                    .ifPresent(applicable -> {
                                        if (isApplicable(applicable)) {
                                            events.add(eventName);
                                        }
                                    });
                        }));

        return events;
    }

    private static boolean isApplicable(String value) {
        return APPLICABLE.equals(value);
    }
}
