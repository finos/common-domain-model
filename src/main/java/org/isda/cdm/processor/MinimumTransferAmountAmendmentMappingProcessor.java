package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.MinimumTransferAmountAmendment.MinimumTransferAmountAmendmentBuilder;
import org.isda.cdm.Money;

import java.math.BigDecimal;
import java.util.List;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
public class MinimumTransferAmountAmendmentMappingProcessor extends MappingProcessor {

	private static final String ZERO = "zero";

	public MinimumTransferAmountAmendmentMappingProcessor(RosettaPath rosettaPath, List<Mapping> mappings) {
		super(rosettaPath, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		MinimumTransferAmountAmendmentBuilder minimumTransferAmountAmendmentBuilder = (MinimumTransferAmountAmendmentBuilder) builder;
		addMinimumTransferAmountAmendment(minimumTransferAmountAmendmentBuilder, "partyA");
		addMinimumTransferAmountAmendment(minimumTransferAmountAmendmentBuilder, "partyB");
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	private void addMinimumTransferAmountAmendment(MinimumTransferAmountAmendmentBuilder minimumTransferAmountAmendmentBuilder, String party) {
		Money.MoneyBuilder moneyBuilder = Money.builder();

		List<Mapping> partyAmountMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.amendment_to_minimum_transfer_amount.%s_amount", party)));
		findMappedValue(partyAmountMappings)
				.map(BigDecimal::new)
				.ifPresent(xmlValue -> {
					moneyBuilder.setAmount(xmlValue);
					partyAmountMappings.forEach(m -> updateMapping(m, getPath()));
				});
		List<Mapping> partyCurrencyMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.amendment_to_minimum_transfer_amount.%s_currency", party)));
		findMappedValue(partyCurrencyMappings)
				.ifPresent(xmlValue -> {
					moneyBuilder.setCurrency(toFieldWithMetaString(xmlValue));
					partyCurrencyMappings.forEach(m -> updateMapping(m, getPath()));
				});
		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder().setAmountBuilder(moneyBuilder);

		List<Mapping> amtaMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.amendment_to_minimum_transfer_amount.%s_amendment_to_minimum_transfer_amount", party)));
		findMappedValue(amtaMappings).ifPresent(xmlValue -> {
			electiveAmountElectionBuilder.setParty(party);
			if (ZERO.equals(xmlValue)) {
				moneyBuilder.setAmount(BigDecimal.ZERO);
			}
			amtaMappings.forEach(m -> updateMapping(m, getPath()));
		});
		List<Mapping> specifyMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.amendment_to_minimum_transfer_amount.%s_specify", party)));
		findMappedValue(specifyMappings).ifPresent(xmlValue -> {
			electiveAmountElectionBuilder.setCustomElection(xmlValue);
			specifyMappings.forEach(m -> updateMapping(m, getPath()));
		});

		minimumTransferAmountAmendmentBuilder.addPartyElectionsBuilder(electiveAmountElectionBuilder);
	}
}