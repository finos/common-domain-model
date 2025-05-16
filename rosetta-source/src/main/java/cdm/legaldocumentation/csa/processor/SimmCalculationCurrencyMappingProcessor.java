package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.legaldocumentation.csa.CalculationCurrencyElection;
import cdm.legaldocumentation.csa.SimmCalculationCurrency;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.*;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class SimmCalculationCurrencyMappingProcessor extends MappingProcessor {

	public SimmCalculationCurrencyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		SimmCalculationCurrency.SimmCalculationCurrencyBuilder simmCalculationCurrencyBuilder = (SimmCalculationCurrency.SimmCalculationCurrencyBuilder) builder;
		PARTIES.forEach(party -> getCalculationCurrencyElection(synonymPath, party).ifPresent(simmCalculationCurrencyBuilder::addPartyElection));

	}

	private Optional<CalculationCurrencyElection> getCalculationCurrencyElection(Path synonymPath, String party) {
		CalculationCurrencyElection.CalculationCurrencyElectionBuilder calculationCurrencyElectionBuilder = CalculationCurrencyElection.builder();

		setValueAndUpdateMappings(synonymPath.addElement(party + "_use_base_currency"),
				(value) -> calculationCurrencyElectionBuilder.setParty(toCounterpartyRoleEnum(party)).setIsBaseCurrency(Boolean.valueOf(value)));

		setValueAndOptionallyUpdateMappings(synonymPath.addElement(party + "_use_other_currency"),
				(value) -> setIsoCurrency(getSynonymToEnumMap().getEnumValue(ISOCurrencyCodeEnum.class, value),
								calculationCurrencyElectionBuilder::setCurrency),
				getMappings(),
				getModelPath());

		return calculationCurrencyElectionBuilder.hasData() ? Optional.of(calculationCurrencyElectionBuilder.build()) : Optional.empty();
	}
}
