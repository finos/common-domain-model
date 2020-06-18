package org.isda.cdm.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.TerminationCurrencyAmendment;
import org.isda.cdm.TerminationCurrencyElection;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class TerminationCurrencyAmendmentMappingProcessor extends MappingProcessor {

	private final Map<String, ISOCurrencyCodeEnum> synonymToIsoCurrencyCodeEnumMap;

	public TerminationCurrencyAmendmentMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.synonymToIsoCurrencyCodeEnumMap = synonymToEnumValueMap(ISOCurrencyCodeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			TerminationCurrencyAmendment.TerminationCurrencyAmendmentBuilder terminationCurrencyAmendmentBuilder =
					(TerminationCurrencyAmendment.TerminationCurrencyAmendmentBuilder) builder;
			Path basePath = Path.parse("answers.partyA." + v);

			if ("amendment_to_termination_currency".equals(v)) {
				PARTIES.forEach(party ->
						getSpecifiedTerminationCurrencyElection(basePath, party + "_termination_currency", Collections.singletonList(party))
								.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection));
				getSpecifiedTerminationCurrencyElection(basePath, "both_parties_termination_currency", PARTIES)
						.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
			}

			PARTIES.forEach(party -> {
				String isSpecifiedSynonym = party + "_amendment_to_termination_currency";
				String currencySynonym = party + "_currency";
				getOptionalTerminationCurrencyElection(basePath, isSpecifiedSynonym, currencySynonym, Collections.singletonList(party))
						.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
			});
			getOptionalTerminationCurrencyElection(basePath, "two_affected_parties", "two_affected_parties_currency", PARTIES)
					.ifPresent(terminationCurrencyAmendmentBuilder::addPartyElection);
		});
	}

	private Optional<TerminationCurrencyElection> getSpecifiedTerminationCurrencyElection(Path basePath, String currencySynonym, List<String> parties) {
		TerminationCurrencyElection.TerminationCurrencyElectionBuilder terminationCurrencyElectionBuilder = TerminationCurrencyElection.builder();

		setValueAndOptionallyUpdateMappings(getSynonymPath(basePath, currencySynonym),
				(value) -> {
					terminationCurrencyElectionBuilder.addParty(parties);
					terminationCurrencyElectionBuilder.setIsSpecified(true);
					return setIsoCurrency(synonymToIsoCurrencyCodeEnumMap, terminationCurrencyElectionBuilder::setCurrency, value);
				},
				getMappings(), getPath());

		return terminationCurrencyElectionBuilder.hasData() ? Optional.of(terminationCurrencyElectionBuilder.build()) : Optional.empty();
	}

	private Optional<TerminationCurrencyElection> getOptionalTerminationCurrencyElection(Path basePath, String isSpecifiedSynonym, String currencySynonym, List<String> parties) {
		TerminationCurrencyElection.TerminationCurrencyElectionBuilder terminationCurrencyElectionBuilder = TerminationCurrencyElection.builder();

		setValueAndUpdateMappings(getSynonymPath(basePath, isSpecifiedSynonym),
				(value) -> {
					terminationCurrencyElectionBuilder.addParty(parties);
					terminationCurrencyElectionBuilder.setIsSpecified("specify".equals(value));
				});

		setValueAndOptionallyUpdateMappings(getSynonymPath(basePath, currencySynonym),
				(value) -> setIsoCurrency(synonymToIsoCurrencyCodeEnumMap, terminationCurrencyElectionBuilder::setCurrency, value),
				getMappings(), getPath());

		return terminationCurrencyElectionBuilder.hasData() ? Optional.of(terminationCurrencyElectionBuilder.build()) : Optional.empty();
	}
}
