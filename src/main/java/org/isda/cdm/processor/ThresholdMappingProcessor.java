package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.isda.cdm.Threshold.ThresholdBuilder;

/**
 * ISDA Create mapping processor.
 */
public class ThresholdMappingProcessor extends MappingProcessor {

	public ThresholdMappingProcessor(RosettaPath rosettaPath, List<Mapping> mappings) {
		super(rosettaPath, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		ThresholdBuilder thresholdBuilder = (ThresholdBuilder) builder;
		addThreshold(thresholdBuilder, "partyA");
		addThreshold(thresholdBuilder, "partyB");
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	private void addThreshold(ThresholdBuilder thresholdBuilder, String party) {
		Money.MoneyBuilder moneyBuilder = Money.builder();
		findMapping(Path.parse(String.format("answers.partyA.threshold.%s_amount", party))).map(BigDecimal::new).ifPresent(moneyBuilder::setAmount);
		findMapping(Path.parse(String.format("answers.partyA.threshold.%s_currency", party))).map(this::toFieldWithMetaString).ifPresent(moneyBuilder::setCurrency);

		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder().setAmountBuilder(moneyBuilder);
		findMapping(Path.parse(String.format("answers.partyA.threshold.%s_threshold", party))).ifPresent(t -> {
			electiveAmountElectionBuilder.setParty(party);
			electiveAmountElectionBuilder.setCustomElection(t);
		});

		thresholdBuilder.addPartyElectionBuilder(electiveAmountElectionBuilder);
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