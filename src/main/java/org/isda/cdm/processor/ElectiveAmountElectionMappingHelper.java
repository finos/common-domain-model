package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.setValueFromMappings;
import static org.isda.cdm.processor.MappingProcessorUtils.toFieldWithMetaString;

public class ElectiveAmountElectionMappingHelper {

	private static final String ZERO = "zero";

	private final RosettaPath path;
	private final List<Mapping> mappings;

	ElectiveAmountElectionMappingHelper(RosettaPath path, List<Mapping> mappings) {
		this.path = path;
		this.mappings = mappings;
	}

	Optional<ElectiveAmountElection> getElectiveAmountElection(String synonymValue, String party) {
		ElectiveAmountElection.ElectiveAmountElectionBuilder electiveAmountElectionBuilder = ElectiveAmountElection.builder();
		Money.MoneyBuilder moneyBuilder = Money.builder();

		setValueFromMappings(Path.parse(String.format("answers.partyA.%s.%s_amount", synonymValue, party)),
				(value) -> moneyBuilder.setAmount(new BigDecimal(value)), mappings, path);

		setValueFromMappings(Path.parse(String.format("answers.partyA.%s.%s_currency", synonymValue, party)),
				(value) -> moneyBuilder.setCurrency(toFieldWithMetaString(value)), mappings, path);

		if (moneyBuilder.hasData()) {
			electiveAmountElectionBuilder.setAmountBuilder(moneyBuilder);
		}

		setValueFromMappings(Path.parse(String.format("answers.partyA.%s.%s_%s", synonymValue, party, synonymValue)),
				(value) -> {
					electiveAmountElectionBuilder.setParty(party);
					if (ZERO.equals(value)) {
						electiveAmountElectionBuilder.setZeroAmount(true);
					}
				}, mappings, path);

		setValueFromMappings(Path.parse(String.format("answers.partyA.%s.%s_specify", synonymValue, party)),
				electiveAmountElectionBuilder::setCustomElection, mappings, path);

		return electiveAmountElectionBuilder.hasData() ? Optional.of(electiveAmountElectionBuilder.build()) : Optional.empty();
	}
}
