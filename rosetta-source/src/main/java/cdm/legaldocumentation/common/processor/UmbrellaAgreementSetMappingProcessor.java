package cdm.legaldocumentation.common.processor;

import cdm.legaldocumentation.common.Agreement;
import cdm.legaldocumentation.common.UmbrellaAgreement.UmbrellaAgreementBuilder;
import cdm.legaldocumentation.common.UmbrellaAgreementParty;
import cdm.legaldocumentation.common.UmbrellaAgreementSet;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.removeHtml;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class UmbrellaAgreementSetMappingProcessor extends MappingProcessor {

    public UmbrellaAgreementSetMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        UmbrellaAgreementBuilder umbrellaAgreementBuilder = (UmbrellaAgreementBuilder) parent;
        umbrellaAgreementBuilder.setAgreementSet(new ArrayList<>());

        int index = 0;
        while (true) {
            //We want to construct an agreementSet that holds party:
            //party is part of umbrellaAgreementParty
            Optional<UmbrellaAgreementSet> umbrellaAgreementSet = getUmbrellaAgreementSet(synonymPath, index++);
            if (umbrellaAgreementSet.isPresent()) {
                umbrellaAgreementBuilder.addAgreementSet(umbrellaAgreementSet.get());
            } else {
                break;
            }
        }
    }

    //TODO: finish building the mapper:
    private Optional<UmbrellaAgreementSet> getUmbrellaAgreementSet(Path synonymPath, Integer index) {
        UmbrellaAgreementSet.UmbrellaAgreementSetBuilder umbrellaAgreementSetBuilder = UmbrellaAgreementSet.builder();

//		setValueAndUpdateMappings(synonymPath.addElement("principal_name", index),
//				umbrellaAgreementSetBuilder::setNameValue);
//
//		setValueAndUpdateMappings(synonymPath.addElement("lei", index),
//				value -> umbrellaAgreementSetBuilder.addEntityId(toFieldWithMetaString(value)));
//
//		setValueAndUpdateMappings(synonymPath.addElement("additional", index),
//				value -> umbrellaAgreementSetBuilder.setTerms(removeHtml(value)));

        //aggreement obj
        Agreement agreement = getAgreement(synonymPath, index);
        setValueAndUpdateMappings(synonymPath.addElement("agreement", index),
                value -> umbrellaAgreementSetBuilder.setAgreement(agreement));

        //UmbrellaAggreementParty
        List<UmbrellaAgreementParty> parties = getUmbrellaAgreementParties(synonymPath, index);
        setValueAndUpdateMappings(synonymPath.addElement("agreementParty", index),
                value -> {
                    UmbrellaAgreementParty umbrellaAgreementParty = UmbrellaAgreementParty.builder().build();
                    umbrellaAgreementSetBuilder.setUmbrellaAgreementParty(parties);
                });

        //additional_language
        setValueAndUpdateMappings(synonymPath.addElement("additionallanguage", index),
                value -> umbrellaAgreementSetBuilder.setAdditionalLanguage(removeHtml(value)));


        return umbrellaAgreementSetBuilder.hasData() ? Optional.of(umbrellaAgreementSetBuilder.build()) : Optional.empty();
    }

    private Agreement getAgreement(Path synonymPath, Integer index) {
        Agreement agreement = Agreement.builder().build();
        return Agreement.builder().build();
    }

    private List<UmbrellaAgreementParty> getUmbrellaAgreementParties(Path synonymPath, Integer index) {
        List<UmbrellaAgreementParty> parties = new ArrayList<>();
        UmbrellaAgreementParty umbrellaAgreementParty = UmbrellaAgreementParty.builder().build();
        parties.add(umbrellaAgreementParty);
        return parties;
    }
}