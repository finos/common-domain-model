package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.party.Address;
import cdm.legaldocumentation.common.NoticeContactInformation;
import cdm.legaldocumentation.common.TransferInformationElection;
import com.regnosys.rosetta.common.translation.*;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;


import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.removeHtml;

public class TransferInformationElectionMappingHelper {
    private final RosettaPath path;
    private final List<Mapping> mappings;

    public TransferInformationElectionMappingHelper(RosettaPath path, List<Mapping> mappings) {
        this.path = path;
        this.mappings = mappings;
    }

    public Optional<TransferInformationElection> getTransferInformationElection(Path synonymPath, String party) {
        TransferInformationElection.TransferInformationElectionBuilder TransferInformationElectionBuilder = TransferInformationElection.builder();
        setValueAndUpdateMappings(
                synonymPath.addElement(party + "_specify"),
                address -> TransferInformationElectionBuilder.setPrimaryContactInformation(
                        NoticeContactInformation.builder()
                                .addAddress(Address.builder().addStreet(removeHtml(address)).build())
                                .build()
                )
                , mappings, path);

        return TransferInformationElectionBuilder.hasData()
                ? Optional.of(TransferInformationElectionBuilder.build())
                : Optional.empty();
    }

}
