package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.MinimumTransferAmount;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class MinimumTransferAmountMappingProcessor extends MappingProcessor {

	private final MinimumTransferAmountElectionMappingHelper helper;

	public MinimumTransferAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new MinimumTransferAmountElectionMappingHelper(getModelPath(), getMappings(), mappingContext.getSynonymToEnumMap());
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		MinimumTransferAmount.MinimumTransferAmountBuilder minimumTransferAmountBuilder = (MinimumTransferAmount.MinimumTransferAmountBuilder) builder;
		PARTIES.forEach(party -> helper.getMinimumTransferAmountElection(synonymPath, party).ifPresent(minimumTransferAmountBuilder::addPartyElection));
	}
}