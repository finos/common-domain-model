package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.*;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.*;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.toCounterpartyEnum;

class RegimeMappingHelper {

	private static final List<String> SUFFIXES = Arrays.asList("_secured_party", "_security_taker", "_obligee");

	private final RosettaPath path;
	private final List<Mapping> mappings;
	private final Map<String, SimmExceptionApplicableEnum> synonymToSimmExceptionApplicableEnumMap;
	private final Map<String, ExceptionEnum> synonymToExceptionEnumMap;

	RegimeMappingHelper(RosettaPath path, List<Mapping> mappings) {
		this.path = path;
		this.mappings = mappings;
		this.synonymToSimmExceptionApplicableEnumMap = synonymToEnumValueMap(SimmExceptionApplicableEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
		this.synonymToExceptionEnumMap = synonymToEnumValueMap(ExceptionEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	Optional<RegimeTerms> getRegimeTerms(Path regimePath, String party, Integer index) {
		RegimeTerms.RegimeTermsBuilder regimeTermsBuilder = RegimeTerms.builder();

		// only one suffix should exist
		SUFFIXES.forEach(suffix -> {
			setValueAndUpdateMappings(regimePath.addElement(party + suffix, index),
					(value) -> getEnumValue(synonymToExceptionEnumMap, value, ExceptionEnum.class).ifPresent(
							enumValue -> regimeTermsBuilder.setParty(toCounterpartyEnum(party)).setIsApplicable(enumValue)),
					mappings, path);

			setValueAndUpdateMappings(regimePath.addElement(party + suffix + "_specify", index),
					regimeTermsBuilder::setAsSpecified,
					mappings, path);
		});

		getSimmException(regimePath, party, index).ifPresent(regimeTermsBuilder::setSimmException);
		getRetrospectiveEffect(regimePath, party, index).ifPresent(regimeTermsBuilder::setRetrospectiveEffect);

		setValueAndUpdateMappings(regimePath.addElement("other", index),
				regimeTermsBuilder::setAsSpecified,
				mappings, path);

		return regimeTermsBuilder.hasData() ? Optional.of(regimeTermsBuilder.build()) : Optional.empty();
	}

	private Optional<SimmException> getSimmException(Path regimePath, String party, Integer index) {
		SimmException.SimmExceptionBuilder simmExceptionBuilder = SimmException.builder();

		setValueAndUpdateMappings(regimePath.addElement(party + "_SIMM", index),
				(value) -> getEnumValue(synonymToExceptionEnumMap, value, ExceptionEnum.class)
						.ifPresent(simmExceptionBuilder::setStandardisedException),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_fallback", index),
				(value) -> getEnumValue(synonymToSimmExceptionApplicableEnumMap, value, SimmExceptionApplicableEnum.class)
						.ifPresent(simmExceptionBuilder::setSimmExceptionApplicable),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_SIMM_specify", index),
				simmExceptionBuilder::setAsSpecified,
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_fallback_specify", index),
				simmExceptionBuilder::setAsSpecified,
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_SIMM_applicable_specify", index),
				simmExceptionBuilder::setAsSpecified,
				mappings, path);

		return simmExceptionBuilder.hasData() ? Optional.of(simmExceptionBuilder.build()) : Optional.empty();
	}

	private Optional<RetrospectiveEffect> getRetrospectiveEffect(Path regimePath, String party, Integer index) {
		RetrospectiveEffect.RetrospectiveEffectBuilder retrospectiveEffectBuilder = RetrospectiveEffect.builder();

		setValueAndUpdateMappings(regimePath.addElement(party + "_retrospective", index),
				(value) -> getEnumValue(synonymToExceptionEnumMap, value, ExceptionEnum.class)
						.ifPresent(retrospectiveEffectBuilder::setStandardisedException),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_retrospective_specify", index),
				retrospectiveEffectBuilder::setAsSpecified,
				mappings, path);

		return Optional.of(retrospectiveEffectBuilder.build());
	}
}
