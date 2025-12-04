package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.CSASpecifiedOrAccessConditionEnum;
import cdm.legaldocumentation.csa.SpecifiedConditionOrAccessCondition;
import cdm.legaldocumentation.csa.SpecifiedOrAccessConditionPartyElection;
import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

public class SpecifiedOrAccessConditionPartyElectionMappingProcessor extends MappingProcessor {

    public SpecifiedOrAccessConditionPartyElectionMappingProcessor(
            RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder parentBuilder =
                (SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder) builder;

        PARTIES.forEach(party -> {
            SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder partyBuilder =
                    SpecifiedOrAccessConditionPartyElection.builder();

            getSpecifiedOrAccessConditionPartyElection(synonymPath, party)
                    .ifPresent(existing -> {
                        partyBuilder.setParty(existing.getParty());
                        partyBuilder.addSpecifiedOrAccessCondition(existing.getSpecifiedOrAccessCondition());
                    });

            // pass current party to helper
            AdditionalTerminationEventMappingHelper helper =
                    new AdditionalTerminationEventMappingHelper(getModelPath(), getMappings(), null);
            helper.map(synonymPath, partyBuilder, parent, party);

            if (partyBuilder.hasData()) {
                parentBuilder.addPartyElection(partyBuilder.build());
            }
        });


    }


    private Optional<SpecifiedOrAccessConditionPartyElection> getSpecifiedOrAccessConditionPartyElection(Path synonymPath, String party) {
        SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder builder =
                SpecifiedOrAccessConditionPartyElection.builder();

        // Map the standard party-specific enumerations
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_illegality", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable -> {
                    builder.setParty(toCounterpartyRoleEnum(party));
                    builder.addSpecifiedOrAccessCondition(CSASpecifiedOrAccessConditionEnum.ILLEGALITY);
                }));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_force_majeure", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable ->
                        builder.addSpecifiedOrAccessCondition(Lists.newArrayList(CSASpecifiedOrAccessConditionEnum.FORCE_MAJEURE_EVENT))));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_tax_event", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable ->
                        builder.addSpecifiedOrAccessCondition(Lists.newArrayList(CSASpecifiedOrAccessConditionEnum.TAX_EVENT))));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_tax_event_upon_merger", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable ->
                        builder.addSpecifiedOrAccessCondition(Lists.newArrayList(CSASpecifiedOrAccessConditionEnum.TAX_EVENT_UPON_MERGER))));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_credit_event_upon_merger", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable ->
                        builder.addSpecifiedOrAccessCondition(Lists.newArrayList(CSASpecifiedOrAccessConditionEnum.CREDIT_EVENT_UPON_MERGER))));

        return builder.hasData() ? Optional.of(builder.build()) : Optional.empty();
    }

    private Optional<Boolean> applicableToBoolean(String applicable) {
        if ("applicable".equals(applicable)) return Optional.of(true);
        if ("not_applicable".equals(applicable)) return Optional.of(false);
        return Optional.empty();
    }
}
