package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.*;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;

@SuppressWarnings("unused")
public class ApplicableRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;

	public ApplicableRegimeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new RegimeMappingHelper(modelPath, mappingContext.getMappings(), mappingContext.getSynonymToEnumMap());
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		Regime.RegimeBuilder regimeBuilder = (Regime.RegimeBuilder) parent;
		if (synonymPath.endsWith("additional_regimes"))
			mapAdditionalRegimes(synonymPath, regimeBuilder);
		else
			mapApplicableRegime(synonymPath, regimeBuilder);
	}

	private void mapApplicableRegime(Path synonymPath, Regime.RegimeBuilder regimeBuilder) {
		List<RegimeTerms> termedParties = PARTIES.stream()
				.map(party -> helper.getRegimeTerms(synonymPath, party, null))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());

		if (termedParties.size() > 0) {
			ApplicableRegime.ApplicableRegimeBuilder applicableRegimeBuilder = ApplicableRegime.builder();
			regimeBuilder.addApplicableRegime(applicableRegimeBuilder);

			getSynonymToEnumMap().getEnumValueOptional(RegulatoryRegimeEnum.class, synonymPath.getLastElement().getPathName())
					.ifPresent(applicableRegimeBuilder::setRegime);

			//Path regimePath = getSynonymPath(BASE_PATH, synonymPath);
			termedParties.forEach(applicableRegimeBuilder::addRegimeTerms);

			setValueAndUpdateMappings(synonymPath.addElement("additional_type"),
					(value) -> getSynonymToEnumMap().getEnumValueOptional(AdditionalTypeEnum.class, value).ifPresent(applicableRegimeBuilder::setAdditionalType));

			setValueAndUpdateMappings(synonymPath.addElement("additional_type_specify"), applicableRegimeBuilder::setAdditionalTerms);
		}
	}

	private void mapAdditionalRegimes(Path additionalRegimesPath, Regime.RegimeBuilder regimeBuilder) {
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
			Optional<ApplicableRegime.ApplicableRegimeBuilder> additionalRegime = getAdditionalRegime(regimesPath, index++);
			if (additionalRegime.isPresent()) {
				regimeBuilder.addApplicableRegime(additionalRegime.get());
			} else {
				break;
			}
		}

		// clean up mappings
		updateMappings(additionalRegimesPath, getMappings(), getModelPath());
	}

	private Optional<ApplicableRegime.ApplicableRegimeBuilder> getAdditionalRegime(Path regimesPath, Integer index) {
		ApplicableRegime.ApplicableRegimeBuilder additionalRegimeBuilder = ApplicableRegime.builder();

		setValueAndUpdateMappings(regimesPath.addElement("regime_name", index),
				(value) -> {
					additionalRegimeBuilder.setAdditionalRegime(value);
					PARTIES.forEach(party -> helper.getRegimeTerms(regimesPath, party, index).ifPresent(additionalRegimeBuilder::addRegimeTerms));
				});

		setValueAndUpdateMappings(regimesPath.addElement("additional_type", index),
				(value) -> getSynonymToEnumMap().getEnumValueOptional(AdditionalTypeEnum.class, value).ifPresent(additionalRegimeBuilder::setAdditionalType));

		setValueAndUpdateMappings(regimesPath.addElement("additional_type_specify", index),
				additionalRegimeBuilder::setAdditionalTerms);

		return additionalRegimeBuilder.hasData() ? Optional.of(additionalRegimeBuilder) : Optional.empty();
	}
}