package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.AdditionalRegime.AdditionalRegimeBuilder;
import cdm.legalagreement.csa.AdditionalTypeEnum;
import cdm.legalagreement.csa.Regime;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cdm.legalagreement.csa.AdditionalRegime.builder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.getEnumValue;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.ISDA_CREATE_SYNONYM_SOURCE;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.PARTIES;

@SuppressWarnings("unused")
public class AdditionalRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;
	private final Map<String, AdditionalTypeEnum> synonymToAdditionalTypeEnumMap;

	public AdditionalRegimeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new RegimeMappingHelper(modelPath, mappingContext.getMappings());
		this.synonymToAdditionalTypeEnumMap = synonymToEnumValueMap(AdditionalTypeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	public void map(Path additionalRegimesPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		Regime.RegimeBuilder regimeBuilder = (Regime.RegimeBuilder) parent;
		regimeBuilder.clearAdditionalRegime();

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
			Optional<AdditionalRegimeBuilder> additionalRegime = getAdditionalRegime(regimesPath, index++);
			if (additionalRegime.isPresent()) {
				regimeBuilder.addAdditionalRegimeBuilder(additionalRegime.get());
			} else {
				break;
			}
		}

		// clean up mappings
		updateMappings(additionalRegimesPath, getMappings(), getModelPath());
	}

	private Optional<AdditionalRegimeBuilder> getAdditionalRegime(Path regimesPath, Integer index) {
		AdditionalRegimeBuilder additionalRegimeBuilder = builder();

		setValueAndUpdateMappings(regimesPath.addElement("regime_name", index),
				(value) -> {
					additionalRegimeBuilder.setRegime(value);
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