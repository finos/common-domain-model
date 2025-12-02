
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

    public SpecifiedOrAccessConditionPartyElectionMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder specifiedConditionPartyElectionBuilder = (SpecifiedConditionOrAccessCondition.SpecifiedConditionOrAccessConditionBuilder) builder;
        PARTIES.forEach(party -> getSpecifiedOrAccessConditionPartyElection(synonymPath, party).ifPresent(specifiedConditionPartyElectionBuilder::addPartyElection));
    }

    private Optional<SpecifiedOrAccessConditionPartyElection> getSpecifiedOrAccessConditionPartyElection(Path synonymPath, String party) {
        SpecifiedOrAccessConditionPartyElection.SpecifiedOrAccessConditionPartyElectionBuilder builder = SpecifiedOrAccessConditionPartyElection.builder();
//
//        if(synonymPath.addElement(String.format("%s_illegality", party))
//
//        setValueAndUpdateMappings(synonymPath.addElement(party + "_" + synonymPath.getLastElement().getPathName()),
//                (value) -> {
//                    builder.setParty(toCounterpartyRoleEnum(party));
//                });
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_illegality", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable -> {
                    builder.setParty(toCounterpartyRoleEnum(party));
                    builder.addSpecifiedOrAccessCondition(CSASpecifiedOrAccessConditionEnum.ILLEGALITY);
                }));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_force_majeure", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable -> {
                    builder.addSpecifiedOrAccessCondition(Lists.newArrayList(CSASpecifiedOrAccessConditionEnum.FORCE_MAJEURE_EVENT));
                }));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s_tax_event", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable -> {
                    builder.addSpecifiedOrAccessCondition(Lists.newArrayList(CSASpecifiedOrAccessConditionEnum.TAX_EVENT_UPON_MERGER));
                }));
        setValueAndUpdateMappings(synonymPath.addElement(String.format("%s__credit_event_upon_merger", party)),
                (value) -> applicableToBoolean(value).ifPresent(applicable -> {
                    builder.addSpecifiedOrAccessCondition(Lists.newArrayList(CSASpecifiedOrAccessConditionEnum.CREDIT_EVENT_UPON_MERGER));
                }));

        return builder.hasData() ? Optional.of(builder.build()) : Optional.empty();
    }

    private Optional<Boolean> yesNoToBoolean(String yesNo) {
        if ("yes".equals(yesNo)) {
            return Optional.of(true);
        } else if ("no".equals(yesNo)) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }

    private Optional<Boolean> applicableToBoolean(String applicable) {
        if ("applicable".equals(applicable)) {
            return Optional.of(true);
        } else if ("not_applicable".equals(applicable)) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }
}

