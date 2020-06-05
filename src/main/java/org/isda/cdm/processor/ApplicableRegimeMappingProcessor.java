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
import static org.isda.cdm.processor.MappingProcessorUtils.ISDA_CREATE_SYNONYM_SOURCE;
import static org.isda.cdm.processor.MappingProcessorUtils.synonymToEnumValueMap;
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

			setValueFromMappings(helper.getSynonymPath(regimePath, "additional_type"),
					(value) -> Optional.ofNullable(synonymToAdditionalTypeEnumMap.get(value)).ifPresent(applicableRegimeBuilder::setAdditionalType));

			setValueFromMappings(helper.getSynonymPath(regimePath, "additional_type_specify"),
					applicableRegimeBuilder::setAdditionalTerms);
		});
	}
}