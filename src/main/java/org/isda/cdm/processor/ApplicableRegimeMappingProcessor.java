package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.AdditionalTypeEnum;
import org.isda.cdm.ApplicableRegime;
import org.isda.cdm.RegulatoryRegimeEnum;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.ApplicableRegime.ApplicableRegimeBuilder;
import static org.isda.cdm.Regime.RegimeBuilder;
import static org.isda.cdm.processor.MappingProcessorUtils.findMappedValue;
import static org.isda.cdm.processor.MappingProcessorUtils.updateMapping;
import static org.isda.cdm.processor.RegimeMappingHelper.*;

@SuppressWarnings("unused")
public class ApplicableRegimeMappingProcessor extends MappingProcessor {

	private final RegimeMappingHelper helper;

	public ApplicableRegimeMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		helper = new RegimeMappingHelper(rosettaPath, mappings);
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

			getRegulatoryRegimeEnum(synonymValue).ifPresent(applicableRegimeBuilder::setRegime);

			Path regimePath = BASE_PATH.addElement(new Path.PathElement(synonymValue));

			PARTIES.forEach(party ->
					SUFFIXES.forEach(suffix ->
							helper.getRegimeTerms(regimePath, party, suffix, null).ifPresent(applicableRegimeBuilder::addRegimeTermsBuilder)));

			List<Mapping> additionalTypeMappings = helper.findMappings(regimePath, "", "additional_type", null);
			Optional<String> additionalType = findMappedValue(additionalTypeMappings);
			additionalType.ifPresent(xmlValue -> {
				switch (xmlValue) {
				case "not_applicable":
					applicableRegimeBuilder.setAdditionalType(AdditionalTypeEnum.NOT_APPLICABLE);
					break;
				case "other":
					applicableRegimeBuilder.setAdditionalType(AdditionalTypeEnum.OTHER);
					break;
				}
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

	private Optional<RegulatoryRegimeEnum> getRegulatoryRegimeEnum(String synonymValue) {
		switch (synonymValue) {
		case "australia":
			return Optional.of(RegulatoryRegimeEnum.AUSTRALIA_MARGIN_RULES);
		case "canada":
			return Optional.of(RegulatoryRegimeEnum.CANADA_MARGIN_RULES);
		case "cftc":
			return Optional.of(RegulatoryRegimeEnum.CFTC_MARGIN_RULES);
		case "emir":
			return Optional.of(RegulatoryRegimeEnum.EMIR_MARGIN_RULES);
		case "hong_kong":
			return Optional.of(RegulatoryRegimeEnum.HONG_KONG_MARGIN_RULES);
		case "japan":
			return Optional.of(RegulatoryRegimeEnum.JAPAN_MARGIN_RULES);
		case "sec":
			return Optional.of(RegulatoryRegimeEnum.SEC_MARGIN_RULES);
		case "singapore":
			return Optional.of(RegulatoryRegimeEnum.SINGAPORE_MARGIN_RULES);
		case "switzerland":
			return Optional.of(RegulatoryRegimeEnum.SWITZERLAND_MARGIN_RULES);
		case "prudential":
			return Optional.of(RegulatoryRegimeEnum.US_PRUDENTIAL_MARGIN_RULES);
		default:
			return Optional.empty();
		}
	}
}