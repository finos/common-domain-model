package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legaldocumentation.csa.TerminationCurrencyAmendment;
import cdm.legaldocumentation.csa.TerminationCurrencyElection;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.CreateiQMappingProcessorUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.setIsoCurrency;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

/**
 * CreateiQ mapping processor.
 */
@SuppressWarnings("unused")
public class TerminationCurrencyAmendmentMappingProcessor extends MappingProcessor {

	public TerminationCurrencyAmendmentMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		List<Mapping> notApplicableSubMappings = anySubPathValueMatches(synonymPath, "not_applicable",
				"_amendment_to_termination_currency", "two_affected_parties");

		if (!notApplicableSubMappings.isEmpty()) {
			notApplicableSubMappings.forEach(m -> setValueAndUpdateMappings(m.getXmlPath(), x -> {}));
			return;
		}


		TerminationCurrencyAmendment.TerminationCurrencyAmendmentBuilder terminationCurrencyAmendmentBuilder =
				(TerminationCurrencyAmendment.TerminationCurrencyAmendmentBuilder) builder;

		List<CounterpartyRoleEnum> counterparties = PARTIES.stream()
				.map(CreateiQMappingProcessorUtils::toCounterpartyRoleEnum)
				.collect(Collectors.toList());

		if (synonymPath.endsWith("amendment_to_termination_currency")) {
			PARTIES.forEach(party ->
					getSpecifiedTerminationCurrencyElection(synonymPath, party + "_termination_currency", Collections.singletonList(toCounterpartyRoleEnum(party)))
							.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection));
			getSpecifiedTerminationCurrencyElection(synonymPath, "both_parties_termination_currency", counterparties)
					.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
		}

		PARTIES.forEach(party -> {
			String isSpecifiedSynonym = party + "_amendment_to_termination_currency";
			String currencySynonym = party + "_currency";
			getOptionalTerminationCurrencyElection(synonymPath, isSpecifiedSynonym, currencySynonym, Collections.singletonList(toCounterpartyRoleEnum(party)))
					.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
		});
		getOptionalTerminationCurrencyElection(synonymPath, "two_affected_parties", "two_affected_parties_currency", counterparties)
				.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
	}

	private Optional<TerminationCurrencyElection> getSpecifiedTerminationCurrencyElection(Path basePath, String currencySynonym, List<CounterpartyRoleEnum> parties) {
		TerminationCurrencyElection.TerminationCurrencyElectionBuilder terminationCurrencyElectionBuilder = TerminationCurrencyElection.builder();

		setValueAndOptionallyUpdateMappings(basePath.addElement(currencySynonym),
				(value) -> {
					terminationCurrencyElectionBuilder.addParty(parties).setIsSpecified(true);
					return setIsoCurrency(getSynonymToEnumMap().getEnumValue(ISOCurrencyCodeEnum.class, value), terminationCurrencyElectionBuilder::setCurrency);
				},
				getMappings(),
				getModelPath());

		return terminationCurrencyElectionBuilder.hasData() ? Optional.of(terminationCurrencyElectionBuilder.build()) : Optional.empty();
	}

	private Optional<TerminationCurrencyElection> getOptionalTerminationCurrencyElection(Path basePath, String isSpecifiedSynonym, String currencySynonym, List<CounterpartyRoleEnum> parties) {
		TerminationCurrencyElection.TerminationCurrencyElectionBuilder terminationCurrencyElectionBuilder = TerminationCurrencyElection.builder();

		setValueAndUpdateMappings(basePath.addElement(isSpecifiedSynonym),
				(value) -> terminationCurrencyElectionBuilder.addParty(parties).setIsSpecified("specify".equals(value)));

		setValueAndOptionallyUpdateMappings(basePath.addElement(currencySynonym),
				(value) -> setIsoCurrency(getSynonymToEnumMap().getEnumValue(ISOCurrencyCodeEnum.class, value), terminationCurrencyElectionBuilder::setCurrency),
				getMappings(), getModelPath());

		return terminationCurrencyElectionBuilder.hasData() ? Optional.of(terminationCurrencyElectionBuilder.build()) : Optional.empty();
	}

	private List<Mapping> anySubPathValueMatches(Path synonymPath, String valueSearchTerm, String ...subPathEndsWith) {
		List<String> foo = Arrays.asList(subPathEndsWith);

		return getMappings().stream()
				.filter(m -> synonymPath.fullStartMatches(m.getXmlPath()))
				.filter(m -> foo.stream().anyMatch(m.getXmlPath().toString()::endsWith))
				.filter(m -> valueSearchTerm.equals(m.getXmlValue()))
				.collect(Collectors.toList());
	}
}
