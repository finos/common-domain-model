package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.Money;

import java.math.BigDecimal;
import java.util.List;

import static org.isda.cdm.Threshold.ThresholdBuilder;
import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
public class ThresholdMappingProcessor extends MappingProcessor {

	private static final String ZERO = "zero";

	private final ElectiveAmountElectionMappingHelper helper;

	public ThresholdMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.helper = new ElectiveAmountElectionMappingHelper(getPath(), getMappings());
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			ThresholdBuilder thresholdBuilder = (ThresholdBuilder) builder;
			helper.getElectiveAmountElection(v, "partyA").ifPresent(thresholdBuilder::addPartyElection);
			helper.getElectiveAmountElection(v, "partyB").ifPresent(thresholdBuilder::addPartyElection);
		});
	}
}