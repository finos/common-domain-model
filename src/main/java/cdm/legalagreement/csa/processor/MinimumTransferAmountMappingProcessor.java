package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.MinimumTransferAmount.MinimumTransferAmountBuilder;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class MinimumTransferAmountMappingProcessor extends MappingProcessor {

	private final ElectiveAmountElectionMappingHelper helper;

	public MinimumTransferAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new ElectiveAmountElectionMappingHelper(getModelPath(), getMappings());
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		MinimumTransferAmountBuilder minimumTransferAmountBuilder = (MinimumTransferAmountBuilder) builder;
		PARTIES.forEach(party -> helper.getElectiveAmountElection(synonymPath, party).ifPresent(minimumTransferAmountBuilder::addPartyElection));
	}
}