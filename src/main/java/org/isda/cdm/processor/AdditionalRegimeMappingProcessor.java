package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.AdditionalTypeEnum;
import org.isda.cdm.Regime;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.isda.cdm.AdditionalRegime.AdditionalRegimeBuilder;
import static org.isda.cdm.AdditionalRegime.builder;
import static org.isda.cdm.processor.MappingProcessorUtils.*;

@SuppressWarnings("unused")
public class AdditionalRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;
	private final Map<String, AdditionalTypeEnum> synonymToAdditionalTypeEnumMap;

	public AdditionalRegimeMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, List<Mapping> mappings) {
		super(rosettaPath, synonymPaths, mappings);
		this.helper = new RegimeMappingHelper(rosettaPath, mappings);
		this.synonymToAdditionalTypeEnumMap = synonymToEnumValueMap(AdditionalTypeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	protected void map(Path additionalRegimesPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		Regime.RegimeBuilder regimeBuilder = (Regime.RegimeBuilder) parent;
		regimeBuilder.clearAdditionalRegime();

		Path regimesPath = getSynonymPath(additionalRegimesPath, "regimes");

		List<Mapping> applicableMappings = findMappings(getMappings(), getSynonymPath(additionalRegimesPath, "is_applicable"));
		Optional<String> applicable = findMappedValue(applicableMappings);
		if (applicable.isPresent()) {
			applicableMappings.forEach(m -> updateMappingSuccess(m, getPath()));
		}

		if (!applicable.isPresent() || applicable.get().equals("not_applicable")) {
			// if additional_regimes are not applicable (or not present) remove all related mappings
			updateMappings(regimesPath, getMappings(), getPath());
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
		updateMappings(additionalRegimesPath, getMappings(), getPath());
	}

	private Optional<AdditionalRegimeBuilder> getAdditionalRegime(Path regimesPath, Integer index) {
		AdditionalRegimeBuilder additionalRegimeBuilder = builder();

		setValueAndUpdateMappings(getSynonymPath(regimesPath,"regime_name", index),
				(value) -> {
					additionalRegimeBuilder.setRegime(value);
					PARTIES.forEach(party -> helper.getRegimeTerms(regimesPath, party, index).ifPresent(additionalRegimeBuilder::addRegimeTerms));
				});

		setValueAndUpdateMappings(getSynonymPath(regimesPath, "additional_type", index),
				(value) -> getEnumValue(synonymToAdditionalTypeEnumMap, value, AdditionalTypeEnum.class)
						.ifPresent(additionalRegimeBuilder::setAdditionalType));

		setValueAndUpdateMappings(getSynonymPath(regimesPath, "additional_type_specify", index),
				additionalRegimeBuilder::setAdditionalTerms);

		return additionalRegimeBuilder.hasData() ? Optional.of(additionalRegimeBuilder) : Optional.empty();
	}
}