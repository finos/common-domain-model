package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.MinimumTransferAmount.MinimumTransferAmountBuilder;

import java.util.List;

import static org.isda.cdm.processor.MappingProcessorUtils.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class MinimumTransferAmountMappingProcessor extends MappingProcessor {

	private final ElectiveAmountElectionMappingHelper helper;

	public MinimumTransferAmountMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, List<Mapping> mappings) {
		super(rosettaPath, synonymPaths, mappings);
		this.helper = new ElectiveAmountElectionMappingHelper(getPath(), getMappings());
	}

	@Override
	protected void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		MinimumTransferAmountBuilder minimumTransferAmountBuilder = (MinimumTransferAmountBuilder) builder;
		PARTIES.forEach(party -> helper.getElectiveAmountElection(synonymPath, party).ifPresent(minimumTransferAmountBuilder::addPartyElection));
	}
}