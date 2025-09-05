package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.common.TransferInformationElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.CreateiQMappingProcessorUtils;

import java.util.List;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

public class TransferInformationElectionMappingProcessor extends MappingProcessor {
    public TransferInformationElectionMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        TransferInformationElection.TransferInformationElectionBuilder TransferInformationElectionBuilder = (TransferInformationElection.TransferInformationElectionBuilder) builder;
        PARTIES.forEach(party -> TransferInformationElectionBuilder.setPartyReference(CreateiQMappingProcessorUtils.toCounterpartyRoleEnum(party)).build());
    }

   /* private Optional<ContactInformationElection> getContactInformationElection(Path synonymPath, String party) {
        ContactInformationElection.ContactInformationElectionBuilder ContactInformationElectionBuilder = ContactInformationElection.builder();
        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
                address -> ContactInformationElectionBuilder
                        .setPartyReference(CreateiQMappingProcessorUtils.toCounterpartyRoleEnum(party))
                        .setContactInformation(ContactInformation.builder()
                                .addAddress(Address.builder()
                                        .addStreet(removeHtml(address)))));
        return ContactInformationElectionBuilder.hasData() ? Optional.of(ContactInformationElectionBuilder.build()) : Optional.empty();
    }*/
}
