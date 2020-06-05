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
import static org.isda.cdm.processor.RegimeMappingHelper.*;

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
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		RegimeBuilder regimeBuilder = (RegimeBuilder) parent;
		regimeBuilder.clearApplicableRegime();

		getSynonymValues().forEach(synonymValue -> {
			ApplicableRegimeBuilder applicableRegimeBuilder = ApplicableRegime.builder();
			regimeBuilder.addApplicableRegimeBuilder(applicableRegimeBuilder);

			Optional.ofNullable(synonymToRegulatoryRegimeEnumMap.get(synonymValue)).ifPresent(applicableRegimeBuilder::setRegime);

			Path regimePath = BASE_PATH.addElement(new Path.PathElement(synonymValue));

			PARTIES.forEach(party ->
					SUFFIXES.forEach(suffix ->
							helper.getRegimeTerms(regimePath, party, suffix, null).ifPresent(applicableRegimeBuilder::addRegimeTermsBuilder)));

			List<Mapping> additionalTypeMappings = helper.findMappings(regimePath, "", "additional_type", null);
			Optional<String> additionalType = findMappedValue(additionalTypeMappings);
			additionalType.ifPresent(xmlValue -> {
				Optional.ofNullable(synonymToAdditionalTypeEnumMap.get(xmlValue)).ifPresent(applicableRegimeBuilder::setAdditionalType);
				additionalTypeMappings.forEach(m -> updateMapping(m, getPath()));
			});

			List<Mapping> additionalTypeSpecifyMappings = helper.findMappings(regimePath, "", "additional_type_specify", null);
			Optional<String> additionalTypeSpecify = findMappedValue(additionalTypeSpecifyMappings);
			additionalTypeSpecify.ifPresent(xmlValue -> {
				applicableRegimeBuilder.setAdditionalTerms(xmlValue);
				additionalTypeSpecifyMappings.forEach(m -> updateMapping(m, getPath()));
			});
		});
	}
}