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

import static com.regnosys.rosetta.common.translation.Path.PathElement;
import static org.isda.cdm.AdditionalRegime.AdditionalRegimeBuilder;
import static org.isda.cdm.AdditionalRegime.builder;
import static org.isda.cdm.processor.MappingProcessorUtils.*;
import static org.isda.cdm.processor.RegimeMappingHelper.*;

@SuppressWarnings("unused")
public class AdditionalRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;
	private final Map<String, AdditionalTypeEnum> synonymToAdditionalTypeEnumMap;

	public AdditionalRegimeMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.helper = new RegimeMappingHelper(rosettaPath, mappings);
		this.synonymToAdditionalTypeEnumMap = synonymToEnumValueMap(AdditionalTypeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		Regime.RegimeBuilder regimeBuilder = (Regime.RegimeBuilder) parent;
		regimeBuilder.clearAdditionalRegime();

		Path additionalRegimesPath = BASE_PATH.addElement(new PathElement("additional_regimes"));
		Path regimesPath = additionalRegimesPath.addElement(new PathElement("regimes"));

		List<Mapping> applicableMappings = findMappings(getMappings(), helper.getSynonymPath(additionalRegimesPath, "is_applicable"));
		Optional<String> applicable = findMappedValue(applicableMappings);
		if (applicable.isPresent()) {
			applicableMappings.forEach(m -> updateMapping(m, getPath()));
		}
		if (!applicable.isPresent() || applicable.get().equals("not_applicable")) {
			// if additional_regimes are not applicable (or not present) remove all related mappings
			updateAdditionalRegimesMappings(regimesPath);
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
		getAdditionalRegime(regimesPath, null).ifPresent(regimeBuilder::addAdditionalRegimeBuilder);

		// clean up mappings
		updateAdditionalRegimesMappings(additionalRegimesPath);
	}

	private void updateAdditionalRegimesMappings(Path regimesPath) {
		MappingProcessorUtils.findMappings(getMappings(), regimesPath).forEach(m -> updateMapping(m, getPath()));
	}

	private Optional<AdditionalRegimeBuilder> getAdditionalRegime(Path regimesPath, Integer index) {
		AdditionalRegimeBuilder additionalRegimeBuilder = builder();

		setValueFromMappings(helper.getSynonymPath(regimesPath,"regime_name", index),
				(value) -> {
					additionalRegimeBuilder.setRegime(value);
					helper.getRegimeTerms(regimesPath, "partyA", index).ifPresent(additionalRegimeBuilder::addRegimeTerms);
					helper.getRegimeTerms(regimesPath, "partyB", index).ifPresent(additionalRegimeBuilder::addRegimeTerms);
				});

		setValueFromMappings(helper.getSynonymPath(regimesPath, "additional_type", index),
				(value) -> Optional.ofNullable(synonymToAdditionalTypeEnumMap.get(value)).ifPresent(additionalRegimeBuilder::setAdditionalType));

		setValueFromMappings(helper.getSynonymPath(regimesPath, "additional_type_specify", index),
				additionalRegimeBuilder::setAdditionalTerms);

		return additionalRegimeBuilder.hasData() ? Optional.of(additionalRegimeBuilder) : Optional.empty();
	}
}