package cdm.legalagreement.common.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

public class RelatedAgreementMappingProcessor extends org.isda.cdm.processor.RelatedAgreementMappingProcessor {

    public RelatedAgreementMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }
}
