package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.MinimumTransferAmount.MinimumTransferAmountBuilder;
import org.isda.cdm.Money;

import java.math.BigDecimal;
import java.util.List;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
public class MinimumTransferAmountMappingProcessor extends MappingProcessor {

	private static final String ZERO = "zero";

	public MinimumTransferAmountMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
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

		List<Mapping> partyAmountMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.minimum_transfer_amount.%s_amount", party)));
		findMappedValue(partyAmountMappings)
				.map(BigDecimal::new)
				.ifPresent(xmlValue -> {
					moneyBuilder.setAmount(xmlValue);
					partyAmountMappings.forEach(m -> updateMapping(m, getPath()));
				});
		List<Mapping> partyCurrencyMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.minimum_transfer_amount.%s_currency", party)));
		findMappedValue(partyCurrencyMappings)
				.ifPresent(xmlValue -> {
					moneyBuilder.setCurrency(toFieldWithMetaString(xmlValue));
					partyCurrencyMappings.forEach(m -> updateMapping(m, getPath()));
				});
		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder().setAmountBuilder(moneyBuilder);

		List<Mapping> mtaMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.minimum_transfer_amount.%s_minimum_transfer_amount", party)));
		findMappedValue(mtaMappings).ifPresent(xmlValue -> {
			electiveAmountElectionBuilder.setParty(party);
			if (ZERO.equals(xmlValue)) {
				moneyBuilder.setAmount(BigDecimal.ZERO);
			}
			mtaMappings.forEach(m -> updateMapping(m, getPath()));
		});
		List<Mapping> specifyMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.minimum_transfer_amount.%s_specify", party)));
		findMappedValue(specifyMappings).ifPresent(xmlValue -> {
			electiveAmountElectionBuilder.setCustomElection(xmlValue);
			specifyMappings.forEach(m -> updateMapping(m, getPath()));
		});

		minimumTransferAmountBuilder.addPartyElectionBuilder(electiveAmountElectionBuilder);
	}
}