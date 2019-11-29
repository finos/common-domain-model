package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.MinimumTransferAmount.MinimumTransferAmountBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * ISDA Create mapping processor.
 *
 * If partyElection.customElection is "zero" then set partyElection.noAmount to 0.
 */
public class MinimumTransferAmountMappingProcessor extends MappingProcessor {

	private static final String ZERO = "zero";

	public MinimumTransferAmountMappingProcessor(RosettaPath rosettaPath, List<Mapping> mappings) {
		super(rosettaPath, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		MinimumTransferAmountBuilder minimumTransferAmountBuilder = (MinimumTransferAmountBuilder) builder;

		Optional.ofNullable(minimumTransferAmountBuilder)
				.map(MinimumTransferAmountBuilder::getPartyElection)
				.orElse(Collections.emptyList())
				.forEach(partyElection -> {
					if (ZERO.equals(partyElection.getCustomElection())) {
						partyElection.setNoAmount(0);
					}
				});
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}
}