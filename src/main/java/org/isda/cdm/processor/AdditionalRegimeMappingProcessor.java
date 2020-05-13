package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.AdditionalTypeEnum;
import org.isda.cdm.Regime;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.Path.PathElement;
import static org.isda.cdm.AdditionalRegime.AdditionalRegimeBuilder;
import static org.isda.cdm.AdditionalRegime.builder;
import static org.isda.cdm.processor.MappingProcessorUtils.findMappedValue;
import static org.isda.cdm.processor.MappingProcessorUtils.updateMapping;
import static org.isda.cdm.processor.RegimeMappingHelper.*;

@SuppressWarnings("unused")
public class AdditionalRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;

	public AdditionalRegimeMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		helper = new RegimeMappingHelper(rosettaPath, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		Regime.RegimeBuilder regimeBuilder = (Regime.RegimeBuilder) parent;
		regimeBuilder.clearAdditionalRegime();

		Path additionalRegimesPath = BASE_PATH.addElement(new PathElement("additional_regimes"));

		List<Mapping> applicableMappings = helper.findMappings(additionalRegimesPath, "is_applicable", null);
		Optional<String> applicable = findMappedValue(applicableMappings);

		if (applicable.isPresent()) {
			if (applicable.get().equals("not_applicable")) {
				applicableMappings.forEach(m -> updateMapping(m, getPath()));
				return;
			}
		} else {
			return;
		}

		int index = 0;
		while (true) {
			Optional<AdditionalRegimeBuilder> additionalRegime = getAdditionalRegime(additionalRegimesPath, index++);
			if (additionalRegime.isPresent()) {
				regimeBuilder.addAdditionalRegimeBuilder(additionalRegime.get());
			} else {
				break;
			}
		}
		getAdditionalRegime(additionalRegimesPath, null).ifPresent(regimeBuilder::addAdditionalRegimeBuilder);
	}

	private Optional<AdditionalRegimeBuilder> getAdditionalRegime(Path additionalRegimesPath, Integer index) {
		Path regimesPath = additionalRegimesPath.addElement(new PathElement("regimes"));
		Path namePath = helper.getMappedPath(regimesPath, "","regime_name", index);

		AdditionalRegimeBuilder additionalRegimeBuilder = builder();

		List<Mapping> nameMappings = MappingProcessorUtils.findMappings(getMappings(), namePath);
		Optional<String> name = findMappedValue(nameMappings);
		name.ifPresent(xmlValue -> {
			additionalRegimeBuilder.setRegime(xmlValue);
			nameMappings.forEach(m -> updateMapping(m, getPath()));
		});

		List<Mapping> additionalTypeMappings = helper.findMappings(regimesPath, "", "additional_type", index);
		Optional<String> additionalType = findMappedValue(additionalTypeMappings);
		additionalType.ifPresent(xmlValue -> {
			switch (xmlValue) {
			case "not_applicable":
				additionalRegimeBuilder.setAdditionalType(AdditionalTypeEnum.NOT_APPLICABLE);
				break;
			case "other":
				additionalRegimeBuilder.setAdditionalType(AdditionalTypeEnum.OTHER);
				break;
			}
			additionalTypeMappings.forEach(m -> updateMapping(m, getPath()));
		});

		List<Mapping> additionalTypeSpecifyMappings = helper.findMappings(regimesPath, "", "additional_type_specify", index);
		Optional<String> additionalTypeSpecify = findMappedValue(additionalTypeSpecifyMappings);
		additionalTypeSpecify.ifPresent(xmlValue -> {
			additionalRegimeBuilder.setAdditionalTerms(xmlValue);
			additionalTypeSpecifyMappings.forEach(m -> updateMapping(m, getPath()));
		});

		// If regime name is present then try to get the regime terms
		if (name.isPresent()) {
			PARTIES.forEach(party ->
					SUFFIXES.forEach(suffix ->
							helper.getRegimeTerms(regimesPath, party, suffix, index).ifPresent(additionalRegimeBuilder::addRegimeTermsBuilder)));
			return Optional.of(additionalRegimeBuilder);
		} else {
			return Optional.empty();
		}
	}
}