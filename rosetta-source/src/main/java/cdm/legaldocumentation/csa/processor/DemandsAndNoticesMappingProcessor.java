package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.party.Address;
import cdm.base.staticdata.party.ContactInformation;
import cdm.base.staticdata.party.PartyContactInformation;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.legaldocumentation.csa.DemandsAndNotices;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.removeHtml;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class DemandsAndNoticesMappingProcessor extends MappingProcessor {
    public DemandsAndNoticesMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        DemandsAndNotices.DemandsAndNoticesBuilder demandsAndNoticesBuilder = (DemandsAndNotices.DemandsAndNoticesBuilder) builder;
        demandsAndNoticesBuilder.setPartyElection(new ArrayList<>())
                .setDeemedEffectiveNextLBD(false);
        PARTIES.forEach(party -> getPartyContactInformation(synonymPath, party).ifPresent(demandsAndNoticesBuilder::addPartyElection));
    }

    private Optional<PartyContactInformation> getPartyContactInformation(Path synonymPath, String party) {
        PartyContactInformation.PartyContactInformationBuilder partyContactInformationBuilder = PartyContactInformation.builder();
        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
                address -> partyContactInformationBuilder
                        .setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(party).build())
                        .setContactInformation(ContactInformation.builder()
                                .addAddress(Address.builder()
                                        .addStreet(removeHtml(address)))));
        return partyContactInformationBuilder.hasData() ? Optional.of(partyContactInformationBuilder.build()) : Optional.empty();
    }
}
