package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.party.Address;
import cdm.legaldocumentation.common.NoticeContactInformation;
import cdm.legaldocumentation.common.NoticeInformationElection;
import com.regnosys.rosetta.common.translation.*;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;


import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.removeHtml;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

public class NoticeInformationElectionMappingHelper {
    private final RosettaPath path;
    private final List<Mapping> mappings;

    public NoticeInformationElectionMappingHelper(RosettaPath path, List<Mapping> mappings) {
        this.path = path;
        this.mappings = mappings;
    }

    public Optional<NoticeInformationElection> getNoticeInformationElection(Path synonymPath, String party) {
        NoticeInformationElection.NoticeInformationElectionBuilder noticeInformationElectionBuilder = NoticeInformationElection.builder();
        setValueAndUpdateMappings(
                synonymPath.addElement(party + "_specify"),
                address -> noticeInformationElectionBuilder
                        .setPartyReference(toCounterpartyRoleEnum(party))
                        .setPrimaryContactInformation(NoticeContactInformation.builder()
                                        .addAddress(Address.builder().addStreet(removeHtml(address)).build())
                                        .build()), mappings, path);
        return noticeInformationElectionBuilder.hasData()
                ? Optional.of(noticeInformationElectionBuilder.build())
                : Optional.empty();
    }

}
