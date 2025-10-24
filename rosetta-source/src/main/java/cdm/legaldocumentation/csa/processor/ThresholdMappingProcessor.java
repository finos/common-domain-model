package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.Threshold;
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
public class ThresholdMappingProcessor extends MappingProcessor {

	private final ThresholdElectionMappingHelper helper;

	public ThresholdMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new ThresholdElectionMappingHelper(getModelPath(), getMappings(), getSynonymToEnumMap());
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		Threshold.ThresholdBuilder thresholdBuilder = (Threshold.ThresholdBuilder) builder;
		PARTIES.forEach(party -> helper.getThresholdElection(synonymPath, party).ifPresent(thresholdBuilder::addPartyElection));
	}
}