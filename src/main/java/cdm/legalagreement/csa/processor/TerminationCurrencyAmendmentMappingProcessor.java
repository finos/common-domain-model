package cdm.legalagreement.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.legalagreement.csa.TerminationCurrencyAmendment;
import cdm.legalagreement.csa.TerminationCurrencyElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.IsdaCreateMappingProcessorUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.setIsoCurrency;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class TerminationCurrencyAmendmentMappingProcessor extends MappingProcessor {

	private final Map<String, ISOCurrencyCodeEnum> synonymToIsoCurrencyCodeEnumMap;

	public TerminationCurrencyAmendmentMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.synonymToIsoCurrencyCodeEnumMap = synonymToEnumValueMap(ISOCurrencyCodeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		TerminationCurrencyAmendment.TerminationCurrencyAmendmentBuilder terminationCurrencyAmendmentBuilder =
				(TerminationCurrencyAmendment.TerminationCurrencyAmendmentBuilder) builder;

		List<CounterpartyEnum> counterparties = PARTIES.stream()
				.map(IsdaCreateMappingProcessorUtils::toCounterpartyEnum)
				.collect(Collectors.toList());

		if (synonymPath.endsWith("amendment_to_termination_currency")) {
			PARTIES.forEach(party ->
					getSpecifiedTerminationCurrencyElection(synonymPath, party + "_termination_currency", Collections.singletonList(toCounterpartyEnum(party)))
							.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection));
			getSpecifiedTerminationCurrencyElection(synonymPath, "both_parties_termination_currency", counterparties)
					.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
		}

		PARTIES.forEach(party -> {
			String isSpecifiedSynonym = party + "_amendment_to_termination_currency";
			String currencySynonym = party + "_currency";
			getOptionalTerminationCurrencyElection(synonymPath, isSpecifiedSynonym, currencySynonym, Collections.singletonList(toCounterpartyEnum(party)))
					.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
		});
		getOptionalTerminationCurrencyElection(synonymPath, "two_affected_parties", "two_affected_parties_currency", counterparties)
				.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
	}

	private Optional<TerminationCurrencyElection> getSpecifiedTerminationCurrencyElection(Path basePath, String currencySynonym, List<CounterpartyEnum> parties) {
		TerminationCurrencyElection.TerminationCurrencyElectionBuilder terminationCurrencyElectionBuilder = TerminationCurrencyElection.builder();

		setValueAndOptionallyUpdateMappings(basePath.addElement(currencySynonym),
				(value) -> {
					terminationCurrencyElectionBuilder.addParty(parties).setIsSpecified(true);
					return setIsoCurrency(synonymToIsoCurrencyCodeEnumMap, terminationCurrencyElectionBuilder::setCurrency, value);
				},
				getMappings(), getModelPath());

		return terminationCurrencyElectionBuilder.hasData() ? Optional.of(terminationCurrencyElectionBuilder.build()) : Optional.empty();
	}

	private Optional<TerminationCurrencyElection> getOptionalTerminationCurrencyElection(Path basePath, String isSpecifiedSynonym, String currencySynonym, List<CounterpartyEnum> parties) {
		TerminationCurrencyElection.TerminationCurrencyElectionBuilder terminationCurrencyElectionBuilder = TerminationCurrencyElection.builder();

		setValueAndUpdateMappings(basePath.addElement(isSpecifiedSynonym),
				(value) -> terminationCurrencyElectionBuilder.addParty(parties).setIsSpecified("specify".equals(value)));

		setValueAndOptionallyUpdateMappings(basePath.addElement(currencySynonym),
				(value) -> setIsoCurrency(synonymToIsoCurrencyCodeEnumMap, terminationCurrencyElectionBuilder::setCurrency, value),
				getMappings(), getModelPath());

		return terminationCurrencyElectionBuilder.hasData() ? Optional.of(terminationCurrencyElectionBuilder.build()) : Optional.empty();
	}
}
