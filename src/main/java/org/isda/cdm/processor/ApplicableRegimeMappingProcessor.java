package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.AdditionalTypeEnum;
import org.isda.cdm.ApplicableRegime;
import org.isda.cdm.RegulatoryRegimeEnum;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.isda.cdm.ApplicableRegime.ApplicableRegimeBuilder;
import static org.isda.cdm.Regime.RegimeBuilder;
import static org.isda.cdm.processor.MappingProcessorUtils.*;
import static org.isda.cdm.processor.MappingProcessorUtils.BASE_PATH;
import static org.isda.cdm.processor.MappingProcessorUtils.PARTIES;

@SuppressWarnings("unused")
public class ApplicableRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;
	private final Map<String, RegulatoryRegimeEnum> synonymToRegulatoryRegimeEnumMap;
	private final Map<String, AdditionalTypeEnum> synonymToAdditionalTypeEnumMap;

	public ApplicableRegimeMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.helper = new RegimeMappingHelper(rosettaPath, mappings);
		this.synonymToRegulatoryRegimeEnumMap = synonymToEnumValueMap(RegulatoryRegimeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
		this.synonymToAdditionalTypeEnumMap = synonymToEnumValueMap(AdditionalTypeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		RegimeBuilder regimeBuilder = (RegimeBuilder) parent;
		regimeBuilder.clearApplicableRegime();

		getSynonymValues().forEach(synonymValue -> {
			ApplicableRegimeBuilder applicableRegimeBuilder = ApplicableRegime.builder();
			regimeBuilder.addApplicableRegimeBuilder(applicableRegimeBuilder);

			Optional.ofNullable(synonymToRegulatoryRegimeEnumMap.get(synonymValue)).ifPresent(applicableRegimeBuilder::setRegime);

			Path regimePath = getSynonymPath(BASE_PATH, synonymValue);
			PARTIES.forEach(party -> helper.getRegimeTerms(regimePath, party, null).ifPresent(applicableRegimeBuilder::addRegimeTerms));

			setValueAndUpdateMappings(getSynonymPath(regimePath, "additional_type"),
					(value) -> getEnumValue(synonymToAdditionalTypeEnumMap, value, AdditionalTypeEnum.class)
							.ifPresent(applicableRegimeBuilder::setAdditionalType));

			setValueAndUpdateMappings(getSynonymPath(regimePath, "additional_type_specify"),
					applicableRegimeBuilder::setAdditionalTerms);
		});
	}
}