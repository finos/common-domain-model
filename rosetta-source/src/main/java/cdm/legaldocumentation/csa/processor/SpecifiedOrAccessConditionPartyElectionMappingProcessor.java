package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.CSASpecifiedOrAccessConditionEnum;
import cdm.legaldocumentation.csa.SpecifiedConditionOrAccessCondition;
import cdm.legaldocumentation.csa.SpecifiedOrAccessConditionPartyElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

@SuppressWarnings("unused") //used in generated code
public class SpecifiedOrAccessConditionPartyElectionMappingProcessor extends MappingProcessor {

    public SpecifiedOrAccessConditionPartyElectionMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder parentBuilder = (SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder) builder;
        PARTIES.forEach(party -> {
            SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder partyBuilder = SpecifiedOrAccessConditionPartyElection.builder();
            getSpecifiedOrAccessConditionPartyElection(synonymPath, party)
                    .ifPresent(specifiedOrAccessConditionPartyElection -> {
                        partyBuilder.setParty(specifiedOrAccessConditionPartyElection.getParty());
                        partyBuilder.addSpecifiedOrAccessCondition(specifiedOrAccessConditionPartyElection.getSpecifiedOrAccessCondition());
                    });
            AdditionalTerminationEventMappingHelper helper = new AdditionalTerminationEventMappingHelper(getModelPath(), getMappings());
            helper.map(synonymPath, partyBuilder, party);
            if (partyBuilder.hasData()) {
                parentBuilder.addPartyElection(partyBuilder.build());
            }
        });
    }

    private Optional<SpecifiedOrAccessConditionPartyElection> getSpecifiedOrAccessConditionPartyElection(Path synonymPath, String party) {
        SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder builder = SpecifiedOrAccessConditionPartyElection.builder();

        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_illegality", party)),
                (value) -> {
                    setSpecifiedOrAccessCondition(value, builder, CSASpecifiedOrAccessConditionEnum.ILLEGALITY);
                    builder.setParty(toCounterpartyRoleEnum(party));
                });
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_force_majeure", party)),
                (value) -> setSpecifiedOrAccessCondition(value, builder, CSASpecifiedOrAccessConditionEnum.FORCE_MAJEURE_EVENT));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_tax_event", party)),
                (value) -> setSpecifiedOrAccessCondition(value, builder, CSASpecifiedOrAccessConditionEnum.TAX_EVENT));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_tax_event_upon_merger", party)),
                (value) -> setSpecifiedOrAccessCondition(value, builder, CSASpecifiedOrAccessConditionEnum.TAX_EVENT_UPON_MERGER));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_credit_event_upon_merger", party)),
                (value) -> setSpecifiedOrAccessCondition(value, builder, CSASpecifiedOrAccessConditionEnum.CREDIT_EVENT_UPON_MERGER));

        return builder.hasData() ? Optional.of(builder.build()) : Optional.empty();
    }

    private void setSpecifiedOrAccessCondition(String value, SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder builder, CSASpecifiedOrAccessConditionEnum csaSpecifiedOrAccessConditionEnum) {
        if (isApplicable(value)) {
            builder.addSpecifiedOrAccessCondition(csaSpecifiedOrAccessConditionEnum);
        }
    }

    private boolean isApplicable(String value) {
        return "applicable".equals(value);
    }
}
