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

import static cdm.legaldocumentation.csa.SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

@SuppressWarnings("unused") //used in generated code
public class SpecifiedOrAccessConditionPartyElectionMappingProcessor extends MappingProcessor {

    private final AdditionalTerminationEventMappingHelper helper;

    public SpecifiedOrAccessConditionPartyElectionMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
        this.helper = new AdditionalTerminationEventMappingHelper(getModelPath(), getMappings());
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder parentBuilder = (SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder) builder;
        PARTIES.forEach(party -> {
            SpecifiedOrAccessConditionPartyElectionBuilder partyElectionBuilder = SpecifiedOrAccessConditionPartyElection.builder();
            mapSpecifiedOrAccessConditionPartyElection(partyElectionBuilder, synonymPath, party);
            helper.mapAdditionalTerminationEvent(partyElectionBuilder, synonymPath, party);

            if (partyElectionBuilder.hasData()) {
                partyElectionBuilder.setParty(toCounterpartyRoleEnum(party));
                parentBuilder.addPartyElection(partyElectionBuilder);
            }
        });
    }

    private void mapSpecifiedOrAccessConditionPartyElection(SpecifiedOrAccessConditionPartyElectionBuilder partyElectionBuilder, Path synonymPath, String party) {
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_illegality", party)),
                (value) -> setSpecifiedOrAccessCondition(value, partyElectionBuilder, CSASpecifiedOrAccessConditionEnum.ILLEGALITY));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_force_majeure", party)),
                (value) -> setSpecifiedOrAccessCondition(value, partyElectionBuilder, CSASpecifiedOrAccessConditionEnum.FORCE_MAJEURE_EVENT));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_tax_event", party)),
                (value) -> setSpecifiedOrAccessCondition(value, partyElectionBuilder, CSASpecifiedOrAccessConditionEnum.TAX_EVENT));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_tax_event_upon_merger", party)),
                (value) -> setSpecifiedOrAccessCondition(value, partyElectionBuilder, CSASpecifiedOrAccessConditionEnum.TAX_EVENT_UPON_MERGER));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_credit_event_upon_merger", party)),
                (value) -> setSpecifiedOrAccessCondition(value, partyElectionBuilder, CSASpecifiedOrAccessConditionEnum.CREDIT_EVENT_UPON_MERGER));
    }

    private void setSpecifiedOrAccessCondition(String value, SpecifiedOrAccessConditionPartyElectionBuilder builder, CSASpecifiedOrAccessConditionEnum csaSpecifiedOrAccessConditionEnum) {
        if (isApplicable(value)) {
            builder.addSpecifiedOrAccessCondition(csaSpecifiedOrAccessConditionEnum);
        }
    }

    private boolean isApplicable(String value) {
        return "applicable".equals(value);
    }
}
