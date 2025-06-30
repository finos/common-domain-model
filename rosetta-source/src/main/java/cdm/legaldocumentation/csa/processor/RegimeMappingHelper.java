package cdm.legaldocumentation.csa.processor;

import cdm.legaldocumentation.csa.*;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.SynonymToEnumMap;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.removeHtml;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

class RegimeMappingHelper {

	private static final List<String> SUFFIXES = Arrays.asList("_secured_party", "_security_taker", "_obligee");

	private final RosettaPath path;
	private final List<Mapping> mappings;
	private final SynonymToEnumMap synonymToEnumMap;

	RegimeMappingHelper(RosettaPath path, List<Mapping> mappings, SynonymToEnumMap synonymToEnumMap) {
		this.path = path;
		this.mappings = mappings;
		this.synonymToEnumMap = synonymToEnumMap;
	}

	Optional<RegimeTerms> getRegimeTerms(Path regimePath, String party, Integer index) {
		RegimeTerms.RegimeTermsBuilder regimeTermsBuilder = RegimeTerms.builder();

		// only one suffix should exist
		SUFFIXES.forEach(suffix -> {
			setValueAndUpdateMappings(regimePath.addElement(party + suffix, index),
					(value) -> synonymToEnumMap.getEnumValueOptional(ExceptionEnum.class, value)
							.ifPresent(enumValue -> regimeTermsBuilder.setParty(toCounterpartyRoleEnum(party)).setIsApplicable(enumValue)),
					mappings, path);

			setValueAndUpdateMappings(regimePath.addElement(party + suffix + "_specify", index),
					value -> regimeTermsBuilder.setAsSpecified(removeHtml(value)),
					mappings, path);
		});

		getSimmException(regimePath, party, index).ifPresent(regimeTermsBuilder::setSimmException);
		getRetrospectiveEffect(regimePath, party, index).ifPresent(regimeTermsBuilder::setRetrospectiveEffect);

		setValueAndUpdateMappings(regimePath.addElement("other", index),
				value -> regimeTermsBuilder.setAsSpecified(removeHtml(value)),
				mappings, path);

		return regimeTermsBuilder.hasData() ? Optional.of(regimeTermsBuilder.build()) : Optional.empty();
	}

	private Optional<SimmException> getSimmException(Path regimePath, String party, Integer index) {
		SimmException.SimmExceptionBuilder simmExceptionBuilder = SimmException.builder();

		setValueAndUpdateMappings(regimePath.addElement(party + "_SIMM", index),
				(value) -> synonymToEnumMap.getEnumValueOptional(ExceptionEnum.class, value)
						.ifPresent(simmExceptionBuilder::setStandardisedException),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_fallback", index),
				(value) -> synonymToEnumMap.getEnumValueOptional(SimmExceptionApplicableEnum.class, value)
						.ifPresent(simmExceptionBuilder::setSimmExceptionApplicable),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_SIMM_specify", index),
				value -> simmExceptionBuilder.setAsSpecified(removeHtml(value)),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_fallback_specify", index),
				(value) -> simmExceptionBuilder.setAsSpecified(removeHtml(value)),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_SIMM_applicable_specify", index),
				value -> simmExceptionBuilder.setAsSpecified(removeHtml(value)),
				mappings, path);

		return simmExceptionBuilder.hasData() ? Optional.of(simmExceptionBuilder.build()) : Optional.empty();
	}

	private Optional<RetrospectiveEffect> getRetrospectiveEffect(Path regimePath, String party, Integer index) {
		RetrospectiveEffect.RetrospectiveEffectBuilder retrospectiveEffectBuilder = RetrospectiveEffect.builder();

		setValueAndUpdateMappings(regimePath.addElement(party + "_retrospective", index),
				(value) -> synonymToEnumMap.getEnumValueOptional(ExceptionEnum.class, value)
						.ifPresent(retrospectiveEffectBuilder::setStandardisedException),
				mappings, path);

		setValueAndUpdateMappings(regimePath.addElement(party + "_retrospective_specify", index),
				value -> retrospectiveEffectBuilder.setAsSpecified(removeHtml(value)),
				mappings, path);

		return Optional.of(retrospectiveEffectBuilder.build());
	}
}
