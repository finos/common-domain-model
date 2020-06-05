package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static org.isda.cdm.Threshold.ThresholdBuilder;
import static org.isda.cdm.processor.RegimeMappingHelper.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class ThresholdMappingProcessor extends MappingProcessor {

	private final ElectiveAmountElectionMappingHelper helper;

	public ThresholdMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.helper = new ElectiveAmountElectionMappingHelper(getPath(), getMappings());
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			ThresholdBuilder thresholdBuilder = (ThresholdBuilder) builder;
			PARTIES.forEach(party -> helper.getElectiveAmountElection(v, party).ifPresent(thresholdBuilder::addPartyElection));
		});
	}
}