package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.DemandsAndNotices;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.ArrayList;
import java.util.List;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class DemandsAndNoticesMappingProcessor extends MappingProcessor {
    private final NoticeInformationElectionMappingHelper helper;

    public DemandsAndNoticesMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
        this.helper = new NoticeInformationElectionMappingHelper(getModelPath(), getMappings());
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        DemandsAndNotices.DemandsAndNoticesBuilder demandsAndNoticesBuilder = (DemandsAndNotices.DemandsAndNoticesBuilder) builder;
        demandsAndNoticesBuilder.setPartyElection(new ArrayList<>())
                .setDeemedEffectiveNextLBD(false);
        PARTIES.forEach(party -> helper.getNoticeInformationElection(synonymPath, party).ifPresent(demandsAndNoticesBuilder::addPartyElection));
    }

}
