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
		List<Mapping> partyAmountMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.threshold.%s_amount", party)));
		findMappedValue(partyAmountMappings)
				.map(BigDecimal::new)
				.ifPresent(xmlValue -> {
					moneyBuilder.setAmount(xmlValue);
					partyAmountMappings.forEach(m -> updateMapping(m, getPath()));
				});
		List<Mapping> partyCurrencyMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.threshold.%s_currency", party)));
		findMappedValue(partyCurrencyMappings)
				.ifPresent(xmlValue -> {
					moneyBuilder.setCurrency(toFieldWithMetaString(xmlValue));
					partyCurrencyMappings.forEach(m -> updateMapping(m, getPath()));
				});
		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder().setAmountBuilder(moneyBuilder);

		List<Mapping> thresholdMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.threshold.%s_threshold", party)));
		findMappedValue(thresholdMappings).ifPresent(xmlValue -> {
			electiveAmountElectionBuilder.setParty(party);
			if (ZERO.equals(xmlValue)) {
				moneyBuilder.setAmount(BigDecimal.ZERO);
			}
			thresholdMappings.forEach(m -> updateMapping(m, getPath()));
		});
		List<Mapping> specifyMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.threshold.%s_specify", party)));
		findMappedValue(specifyMappings).ifPresent(xmlValue -> {
			electiveAmountElectionBuilder.setCustomElection(xmlValue);
			specifyMappings.forEach(m -> updateMapping(m, getPath()));
		});
		thresholdBuilder.addPartyElectionBuilder(electiveAmountElectionBuilder);
	}
}