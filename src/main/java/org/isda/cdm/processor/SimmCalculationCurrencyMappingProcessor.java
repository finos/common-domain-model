package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.CalculationCurrencyElection;
import org.isda.cdm.SimmCalculationCurrency;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class SimmCalculationCurrencyMappingProcessor extends MappingProcessor {

	private final Map<String, ISOCurrencyCodeEnum> synonymToIsoCurrencyCodeEnumMap;

	public SimmCalculationCurrencyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, List<Mapping> mappings) {
		super(rosettaPath, synonymPaths, mappings);
		this.synonymToIsoCurrencyCodeEnumMap = synonymToEnumValueMap(ISOCurrencyCodeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	protected void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		SimmCalculationCurrency.SimmCalculationCurrencyBuilder simmCalculationCurrencyBuilder = (SimmCalculationCurrency.SimmCalculationCurrencyBuilder) builder;
		PARTIES.forEach(party -> getCalculationCurrencyElection(synonymPath, party).ifPresent(simmCalculationCurrencyBuilder::addPartyElection));

	}

	private Optional<CalculationCurrencyElection> getCalculationCurrencyElection(Path synonymPath, String party) {
		CalculationCurrencyElection.CalculationCurrencyElectionBuilder calculationCurrencyElectionBuilder = CalculationCurrencyElection.builder();

		setValueAndUpdateMappings(getSynonymPath(synonymPath, party, "_use_base_currency"),
				(value) -> {
					calculationCurrencyElectionBuilder.setParty(party);
					calculationCurrencyElectionBuilder.setIsBaseCurrency(Boolean.valueOf(value));
				});

		setValueAndOptionallyUpdateMappings(getSynonymPath(synonymPath, party, "_use_other_currency"),
				(value) -> setIsoCurrency(synonymToIsoCurrencyCodeEnumMap, calculationCurrencyElectionBuilder::setCurrency, value),
				getMappings(), getPath());

		return calculationCurrencyElectionBuilder.hasData() ? Optional.of(calculationCurrencyElectionBuilder.build()) : Optional.empty();
	}
}
