package org.isda.cdm.processor;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.CalculationDateLocation.CalculationDateLocationBuilder;
import org.isda.cdm.CalculationDateLocationElection;

import java.util.*;

import static org.isda.cdm.CalculationDateLocationElection.CalculationDateLocationElectionBuilder;
import static org.isda.cdm.CalculationDateLocationElection.builder;
import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CalculationDateLocationMappingProcessor extends MappingProcessor {

	private final Map<String, BusinessCenterEnum> synonymToBusinessCenterEnumMap;

	public CalculationDateLocationMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.synonymToBusinessCenterEnumMap = synonymToEnumValueMap(BusinessCenterEnum.values());
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			CalculationDateLocationBuilder calculationDateLocationBuilder = (CalculationDateLocationBuilder) builder;
			getCalculationDateLocation(v, "partyA").ifPresent(calculationDateLocationBuilder::addPartyElection);
			getCalculationDateLocation(v, "partyB").ifPresent(calculationDateLocationBuilder::addPartyElection);
		});
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	private Optional<CalculationDateLocationElection> getCalculationDateLocation(String parentSynonymValue, String party) {
		CalculationDateLocationElectionBuilder calculationDateLocationElectionBuilder = builder();

		String selectLocationSynonymValue = parentSynonymValue.equals("calculation_date") ? "calculation_date_location" : parentSynonymValue;
		List<Mapping> selectLocationMappings = findMappings(getMappings(),
				Path.parse(String.format("answers.partyA.%s.%s_%s", parentSynonymValue, party, selectLocationSynonymValue)));
		Optional<String> selectLocation = findMappedValue(selectLocationMappings);
		selectLocation.ifPresent(xmlValue -> {
			calculationDateLocationElectionBuilder.setParty(party);
			selectLocationMappings.forEach(m -> updateMapping(m, getPath()));
		});

		List<Mapping> businessCenterMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.%s.%s_location", parentSynonymValue, party)));
		findMappedValue(businessCenterMappings)
				.flatMap(xmlValue -> Optional.ofNullable(synonymToBusinessCenterEnumMap.get(xmlValue)))
				.ifPresent(enumValue -> {
					calculationDateLocationElectionBuilder.setBusinessCenter(
							FieldWithMetaBusinessCenterEnum.builder().setValue(enumValue).build());
					businessCenterMappings.forEach(m -> updateMapping(m, getPath()));
				});

		List<Mapping> otherLocationMappings = findMappings(getMappings(), Path.parse(String.format("answers.partyA.%s.%s_specify", parentSynonymValue, party)));
		findMappedValue(otherLocationMappings).ifPresent(xmlValue -> {
			calculationDateLocationElectionBuilder.setCustomLocation(xmlValue);
			otherLocationMappings.forEach(m -> updateMapping(m, getPath()));
		});

		return calculationDateLocationElectionBuilder.hasData() ? Optional.of(calculationDateLocationElectionBuilder.build()) : Optional.empty();
	}
}