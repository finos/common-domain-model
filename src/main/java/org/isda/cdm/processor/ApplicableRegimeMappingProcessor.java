package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
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
import static org.isda.cdm.processor.CdmMappingProcessorUtils.*;

@SuppressWarnings("unused")
public class ApplicableRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;
	private final Map<String, RegulatoryRegimeEnum> synonymToRegulatoryRegimeEnumMap;
	private final Map<String, AdditionalTypeEnum> synonymToAdditionalTypeEnumMap;

	public ApplicableRegimeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new RegimeMappingHelper(modelPath, mappingContext.getMappings());
		this.synonymToRegulatoryRegimeEnumMap = synonymToEnumValueMap(RegulatoryRegimeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
		this.synonymToAdditionalTypeEnumMap = synonymToEnumValueMap(AdditionalTypeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		RegimeBuilder regimeBuilder = (RegimeBuilder) parent;

		ApplicableRegimeBuilder applicableRegimeBuilder = ApplicableRegime.builder();
		regimeBuilder.addApplicableRegimeBuilder(applicableRegimeBuilder);

		Optional.ofNullable(synonymToRegulatoryRegimeEnumMap.get(synonymPath.getLastElement().getPathName()))
				.ifPresent(applicableRegimeBuilder::setRegime);

		//Path regimePath = getSynonymPath(BASE_PATH, synonymPath);
		PARTIES.forEach(party -> helper.getRegimeTerms(synonymPath, party, null).ifPresent(applicableRegimeBuilder::addRegimeTerms));

		setValueAndUpdateMappings(synonymPath.addElement("additional_type"),
				(value) -> getEnumValue(synonymToAdditionalTypeEnumMap, value, AdditionalTypeEnum.class)
						.ifPresent(applicableRegimeBuilder::setAdditionalType));

		setValueAndUpdateMappings(synonymPath.addElement("additional_type_specify"),
				applicableRegimeBuilder::setAdditionalTerms);
	}
}