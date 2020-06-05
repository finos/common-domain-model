package org.isda.cdm.processor;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.CalculationDateLocation.CalculationDateLocationBuilder;
import org.isda.cdm.CalculationDateLocationElection;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.isda.cdm.CalculationDateLocationElection.CalculationDateLocationElectionBuilder;
import static org.isda.cdm.CalculationDateLocationElection.builder;
import static org.isda.cdm.processor.MappingProcessorUtils.ISDA_CREATE_SYNONYM_SOURCE;
import static org.isda.cdm.processor.MappingProcessorUtils.synonymToEnumValueMap;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CalculationDateLocationMappingProcessor extends MappingProcessor {

	private final Map<String, BusinessCenterEnum> synonymToBusinessCenterEnumMap;

	public CalculationDateLocationMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.synonymToBusinessCenterEnumMap = synonymToEnumValueMap(BusinessCenterEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			CalculationDateLocationBuilder calculationDateLocationBuilder = (CalculationDateLocationBuilder) builder;
			getCalculationDateLocation(v, "partyA").ifPresent(calculationDateLocationBuilder::addPartyElection);
			getCalculationDateLocation(v, "partyB").ifPresent(calculationDateLocationBuilder::addPartyElection);
		});
	}

	private Optional<CalculationDateLocationElection> getCalculationDateLocation(String parentSynonymValue, String party) {
		CalculationDateLocationElectionBuilder calculationDateLocationElectionBuilder = builder();

		String selectLocationSynonymValue = parentSynonymValue.equals("calculation_date") ? "calculation_date_location" : parentSynonymValue;
		setValueFromMappings(String.format("answers.partyA.%s.%s_%s", parentSynonymValue, party, selectLocationSynonymValue),
				(value) -> calculationDateLocationElectionBuilder.setParty(party));

		setValueFromMappings(String.format("answers.partyA.%s.%s_location", parentSynonymValue, party),
				(value) -> Optional.ofNullable(synonymToBusinessCenterEnumMap.get(value))
						.map(enumValue -> FieldWithMetaBusinessCenterEnum.builder().setValue(enumValue).build())
						.ifPresent(calculationDateLocationElectionBuilder::setBusinessCenter));

		setValueFromMappings(String.format("answers.partyA.%s.%s_specify", parentSynonymValue, party),
				calculationDateLocationElectionBuilder::setCustomLocation);

		return calculationDateLocationElectionBuilder.hasData() ? Optional.of(calculationDateLocationElectionBuilder.build()) : Optional.empty();
	}
}