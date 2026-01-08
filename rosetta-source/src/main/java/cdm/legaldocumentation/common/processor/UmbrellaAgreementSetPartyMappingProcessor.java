package cdm.legaldocumentation.common.processor;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyIdentifier;
import cdm.legaldocumentation.common.UmbrellaAgreementParty;
import cdm.legaldocumentation.common.UmbrellaAgreementSet;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toFieldWithMetaString;

/**
 * CreateiQ mapping processor.
 */
public class UmbrellaAgreementSetPartyMappingProcessor extends MappingProcessor {
    public UmbrellaAgreementSetPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        UmbrellaAgreementSet.UmbrellaAgreementSetBuilder umbrellaAgreementPartyBuilder = (UmbrellaAgreementSet.UmbrellaAgreementSetBuilder) parent;
        int index = 0;
        while (true) {
            Optional<UmbrellaAgreementParty> umbrellaAgreementParty = getUmbrellaAgreementParty(synonymPath, index++);
            if (umbrellaAgreementParty.isPresent()) {
                umbrellaAgreementPartyBuilder.addParty(umbrellaAgreementParty.get());
            } else {
                break;
            }
        }
    }

    private Optional<UmbrellaAgreementParty> getUmbrellaAgreementParty(Path synonymPath, Integer index) {
        Party.PartyBuilder partyBuilder = Party.builder();

        setValueAndUpdateMappings(synonymPath.addElement("lei", index),
                value -> partyBuilder.addPartyId(
                        new PartyIdentifier.PartyIdentifierBuilderImpl()
                                .setIdentifier(toFieldWithMetaString(value))
                                .build()));

        setValueAndUpdateMappings(synonymPath.addElement("principal_name", index),
                value -> partyBuilder.setName(toFieldWithMetaString(value)));

        return partyBuilder.hasData() ? Optional.of(UmbrellaAgreementParty.builder().setParty(partyBuilder.build())) : Optional.empty();
    }
}
