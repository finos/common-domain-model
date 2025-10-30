package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.AddressesForTransfer;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

public class AddressesForTransferMappingProcessor extends MappingProcessor {
    private final TransferInformationElectionMappingHelper helper;

    public AddressesForTransferMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
        this.helper = new TransferInformationElectionMappingHelper(getModelPath(), getMappings());
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        AddressesForTransfer.AddressesForTransferBuilder addressesForTransferBuilder = (AddressesForTransfer.AddressesForTransferBuilder) builder;
        PARTIES.forEach(party -> helper.getTransferInformationElection(synonymPath, party).ifPresent(addressesForTransferBuilder::addPartyElection));
    }
}
