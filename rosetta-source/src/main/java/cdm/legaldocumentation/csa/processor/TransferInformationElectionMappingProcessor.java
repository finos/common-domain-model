package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.party.Address;
import cdm.legaldocumentation.common.TransferContactInformation;
import cdm.legaldocumentation.common.TransferInformationElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.CreateiQMappingProcessorUtils;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.removeHtml;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

public class TransferInformationElectionMappingProcessor extends MappingProcessor {
    public TransferInformationElectionMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        PARTIES.forEach(party -> getTransferInformationElection(synonymPath, party));
    }

    private Optional<TransferInformationElection> getTransferInformationElection(Path synonymPath, String party) {
        TransferInformationElection.TransferInformationElectionBuilder transferInformationElection = TransferInformationElection.builder();
        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
                address -> transferInformationElection
                        .setPartyReference(CreateiQMappingProcessorUtils.toCounterpartyRoleEnum(party))
                        .setContactInformation(TransferContactInformation.builder()
                                .addAddress(Address.builder()
                                        .addStreet(removeHtml(address)))));
        return transferInformationElection.hasData() ? Optional.of(transferInformationElection.build()) : Optional.empty();
    }
}
