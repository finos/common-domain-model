package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.party.Address;
import cdm.legaldocumentation.common.NoticeContactInformation;
import cdm.legaldocumentation.common.NoticeInformationElection;
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

public class NoticeInformationElectionMappingProcessor extends MappingProcessor {
    public NoticeInformationElectionMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        NoticeInformationElection.NoticeInformationElectionBuilder noticeInformationElectionBuilder = (NoticeInformationElection.NoticeInformationElectionBuilder) builder;
        PARTIES.forEach(party -> {
            noticeInformationElectionBuilder.setPartyReference(CreateiQMappingProcessorUtils.toCounterpartyRoleEnum(party));
            getNoticeInformationElection(synonymPath, party).ifPresent(noticeInformationElectionBuilder::setPrimaryContactInformation);
        });
    }

    private Optional<NoticeContactInformation> getNoticeInformationElection(Path synonymPath, String party) {
        NoticeContactInformation.NoticeContactInformationBuilder noticeContactInformationBuilder = NoticeContactInformation.builder();
        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
                address -> {
                    noticeContactInformationBuilder.addAddress(Address.builder().addStreet(removeHtml(address)));
                });
        return noticeContactInformationBuilder.hasData() ? Optional.of(noticeContactInformationBuilder.build()) : Optional.empty();
    }
}
