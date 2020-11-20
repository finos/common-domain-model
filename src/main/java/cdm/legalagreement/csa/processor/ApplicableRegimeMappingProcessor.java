package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.AdditionalTypeEnum;
import cdm.legalagreement.csa.ApplicableRegime;
import cdm.legalagreement.csa.ApplicableRegime.ApplicableRegimeBuilder;
import cdm.legalagreement.csa.Regime.RegimeBuilder;
import cdm.legalagreement.csa.RegimeTerms;
import cdm.legalagreement.csa.RegulatoryRegimeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.getEnumValue;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.ISDA_CREATE_SYNONYM_SOURCE;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.PARTIES;

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
		List<RegimeTerms> termedParties = PARTIES.stream().map(party->helper.getRegimeTerms(synonymPath, party, null)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
		
		if (termedParties.size()>0) {
			RegimeBuilder regimeBuilder = (RegimeBuilder) parent;
	
			ApplicableRegimeBuilder applicableRegimeBuilder = ApplicableRegime.builder();
			regimeBuilder.addApplicableRegimeBuilder(applicableRegimeBuilder);
	
			Optional.ofNullable(synonymToRegulatoryRegimeEnumMap.get(synonymPath.getLastElement().getPathName()))
					.ifPresent(applicableRegimeBuilder::setRegime);
	
			//Path regimePath = getSynonymPath(BASE_PATH, synonymPath);
			termedParties.forEach(applicableRegimeBuilder::addRegimeTerms);
	
			setValueAndUpdateMappings(synonymPath.addElement("additional_type"),
					(value) -> getEnumValue(synonymToAdditionalTypeEnumMap, value, AdditionalTypeEnum.class)
							.ifPresent(applicableRegimeBuilder::setAdditionalType));
	
			setValueAndUpdateMappings(synonymPath.addElement("additional_type_specify"),
					applicableRegimeBuilder::setAdditionalTerms);
		}
	}
}