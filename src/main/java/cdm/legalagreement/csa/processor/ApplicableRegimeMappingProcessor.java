package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.*;
import cdm.legalagreement.csa.ApplicableRegime.ApplicableRegimeBuilder;
import cdm.legalagreement.csa.Regime.RegimeBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.legalagreement.csa.ApplicableRegime.builder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;
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
		RegimeBuilder regimeBuilder = (RegimeBuilder) parent;
		if (synonymPath.endsWith("additional_regimes"))
			mapAdditionalRegimes(synonymPath, regimeBuilder);
		else
			mapApplicableRegime(synonymPath, regimeBuilder);
	}

	private void mapApplicableRegime(Path synonymPath, RegimeBuilder parent) {
		List<RegimeTerms> termedParties = PARTIES.stream()
				.map(party -> helper.getRegimeTerms(synonymPath, party, null))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());

		if (termedParties.size() > 0) {
			RegimeBuilder regimeBuilder = parent;

			ApplicableRegimeBuilder applicableRegimeBuilder = ApplicableRegime.builder();
			regimeBuilder.addApplicableRegime(applicableRegimeBuilder);

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

	private void mapAdditionalRegimes(Path additionalRegimesPath, Regime.RegimeBuilder parent) {
		Regime.RegimeBuilder regimeBuilder = parent;

		Path regimesPath = additionalRegimesPath.addElement("regimes");

		List<Mapping> applicableMappings = filterMappings(getMappings(), additionalRegimesPath.addElement("is_applicable"));
		Optional<String> applicable = getNonNullMappedValue(applicableMappings);
		if (applicable.isPresent()) {
			applicableMappings.forEach(m -> updateMappingSuccess(m, getModelPath()));
		}

		if (!applicable.isPresent() || applicable.get().equals("not_applicable")) {
			// if additional_regimes are not applicable (or not present) remove all related mappings
			updateMappings(regimesPath, getMappings(), getModelPath());
			return;
		}

		int index = 0;
		while (true) {
			Optional<ApplicableRegimeBuilder> additionalRegime = getAdditionalRegime(regimesPath, index++);
			if (additionalRegime.isPresent()) {
				regimeBuilder.addApplicableRegime(additionalRegime.get());
			} else {
				break;
			}
		}

		// clean up mappings
		updateMappings(additionalRegimesPath, getMappings(), getModelPath());
	}

	private Optional<ApplicableRegimeBuilder> getAdditionalRegime(Path regimesPath, Integer index) {
		ApplicableRegimeBuilder additionalRegimeBuilder = builder();

		setValueAndUpdateMappings(regimesPath.addElement("regime_name", index),
				(value) -> {
					additionalRegimeBuilder.setAdditionalRegime(value);
					PARTIES.forEach(party -> helper.getRegimeTerms(regimesPath, party, index).ifPresent(additionalRegimeBuilder::addRegimeTerms));
				});

		setValueAndUpdateMappings(regimesPath.addElement("additional_type", index),
				(value) -> getEnumValue(synonymToAdditionalTypeEnumMap, value, AdditionalTypeEnum.class)
						.ifPresent(additionalRegimeBuilder::setAdditionalType));

		setValueAndUpdateMappings(regimesPath.addElement("additional_type_specify", index),
				additionalRegimeBuilder::setAdditionalTerms);

		return additionalRegimeBuilder.hasData() ? Optional.of(additionalRegimeBuilder) : Optional.empty();
	}
}