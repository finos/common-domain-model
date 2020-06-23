package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static org.isda.cdm.Threshold.ThresholdBuilder;
import static org.isda.cdm.processor.MappingProcessorUtils.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class ThresholdMappingProcessor extends MappingProcessor {

	private final ElectiveAmountElectionMappingHelper helper;

	public ThresholdMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, List<Mapping> mappings) {
		super(rosettaPath, synonymPaths, mappings);
		this.helper = new ElectiveAmountElectionMappingHelper(getPath(), getMappings());
	}

	@Override
	protected void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		ThresholdBuilder thresholdBuilder = (ThresholdBuilder) builder;
		PARTIES.forEach(party -> helper.getElectiveAmountElection(synonymPath, party).ifPresent(thresholdBuilder::addPartyElection));
	}
}