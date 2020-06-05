package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.MinimumTransferAmount.MinimumTransferAmountBuilder;

import java.util.List;

/**
 * ISDA Create mapping processor.
 */
public class MinimumTransferAmountMappingProcessor extends MappingProcessor {

	private final ElectiveAmountElectionMappingHelper helper;

	public MinimumTransferAmountMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.helper = new ElectiveAmountElectionMappingHelper(getPath(), getMappings());
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			MinimumTransferAmountBuilder minimumTransferAmountBuilder = (MinimumTransferAmountBuilder) builder;
			helper.getElectiveAmountElection(v, "partyA").ifPresent(minimumTransferAmountBuilder::addPartyElection);
			helper.getElectiveAmountElection(v, "partyB").ifPresent(minimumTransferAmountBuilder::addPartyElection);
		});
	}
}