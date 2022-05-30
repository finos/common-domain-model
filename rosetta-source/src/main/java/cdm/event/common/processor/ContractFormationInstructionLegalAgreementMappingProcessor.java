package cdm.event.common.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.event.common.ContractFormationInstruction.ContractFormationInstructionBuilder;

public class ContractFormationInstructionLegalAgreementMappingProcessor extends MappingProcessor {

    private final DocumentationHelper helper;

    public ContractFormationInstructionLegalAgreementMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(rosettaPath, synonymPaths, mappingContext);
        this.helper = new DocumentationHelper(rosettaPath, mappingContext);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        ContractFormationInstructionBuilder contractDetailsBuilder = (ContractFormationInstructionBuilder) parent;
        contractDetailsBuilder.setLegalAgreement(helper.getDocumentation(synonymPath));
    }
}
