package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.MinimumTransferAmount.MinimumTransferAmountBuilder;
import org.isda.cdm.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
		addMinimumTransferAmount(minimumTransferAmountBuilder, "partyA");
		addMinimumTransferAmount(minimumTransferAmountBuilder, "partyB");
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	private void addMinimumTransferAmount(MinimumTransferAmountBuilder minimumTransferAmountBuilder, String party) {
		Money.MoneyBuilder moneyBuilder = Money.builder();
		findMapping(Path.parse(String.format("answers.partyA.minimum_transfer_amount.%s_amount", party))).map(BigDecimal::new).ifPresent(moneyBuilder::setAmount);
		findMapping(Path.parse(String.format("answers.partyA.minimum_transfer_amount.%s_currency", party))).map(this::toFieldWithMetaString).ifPresent(moneyBuilder::setCurrency);

		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder().setAmountBuilder(moneyBuilder);
		findMapping(Path.parse(String.format("answers.partyA.minimum_transfer_amount.%s_minimum_transfer_amount", party))).ifPresent(t -> {
			electiveAmountElectionBuilder.setParty(party);
			electiveAmountElectionBuilder.setCustomElection(t);
			if (ZERO.equals(t)) {
				electiveAmountElectionBuilder.setNoAmount(0);
			}
		});

		minimumTransferAmountBuilder.addPartyElectionBuilder(electiveAmountElectionBuilder);
	}

	private FieldWithMetaString toFieldWithMetaString(String c) {
		return FieldWithMetaString.builder()
				.setValue(c)
				.build();
	}

	private Optional<String> findMapping(Path path) {
		return getMappings().stream()
				.filter(p -> path.fullStartMatches(p.getXmlPath()))
				.map(Mapping::getXmlValue)
				.filter(Objects::nonNull)
				.map(String::valueOf)
				.findFirst();
	}
}