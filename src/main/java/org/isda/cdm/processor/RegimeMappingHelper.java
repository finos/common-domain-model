package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.Path.PathElement;
import static com.regnosys.rosetta.common.translation.Path.parse;
import static java.util.Optional.ofNullable;
import static org.isda.cdm.processor.MappingProcessorUtils.*;

class RegimeMappingHelper {

	static final List<String> PARTIES = Arrays.asList("partyA", "partyB");
	static final List<String> SUFFIXES = Arrays.asList("_secured_party", "_security_taker", "_obligee");
	static final Path BASE_PATH = parse("answers.partyA");

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

	Optional<RegimeTerms.RegimeTermsBuilder> getRegimeTerms(Path regimePath, String party, String suffix, Integer index) {
		RegimeTerms.RegimeTermsBuilder regimeTermsBuilder = RegimeTerms.builder();

		List<Mapping> applicableMappings = findMappings(regimePath, party, suffix, index);
		Optional<ExceptionEnum> applicable = findMappedValue(applicableMappings).flatMap(syn -> ofNullable(synonymToExceptionEnumMap.get(syn)));
		if (applicable.isPresent()) {
			regimeTermsBuilder.setParty(party);
			regimeTermsBuilder.setIsApplicable(applicable.get());
			applicableMappings.forEach(m -> updateMapping(m, path));
		} else {
			return Optional.empty();
		}

		List<Mapping> specifyMappings = findMappings(regimePath, party, suffix + "_specify", index);
		Optional<String> specify = findMappedValue(specifyMappings);
		specify.ifPresent(xmlValue -> {
			regimeTermsBuilder.setAsSpecified(xmlValue);
			specifyMappings.forEach(m -> updateMapping(m, path));
		});

		getSimmException(regimePath, party, index).ifPresent(regimeTermsBuilder::setSimmExceptionBuilder);
		getRetrospectiveEffect(regimePath, party, index).ifPresent(regimeTermsBuilder::setRetrospectiveEffectBuilder);

		List<Mapping> otherMappings = findMappings(regimePath, "", "other", index);
		Optional<String> other = findMappedValue(otherMappings);
		other.ifPresent(xmlValue -> {
			regimeTermsBuilder.setAsSpecified(xmlValue);
			otherMappings.forEach(m -> updateMapping(m, path));
		});

		return Optional.of(regimeTermsBuilder);
	}

	private Optional<SimmException.SimmExceptionBuilder> getSimmException(Path regimePath, String party, Integer index) {
		SimmException.SimmExceptionBuilder simmExceptionBuilder = SimmException.builder();

		List<Mapping> simmMappings = findMappings(regimePath, party, "_SIMM", index);
		Optional<ExceptionEnum> simm = findMappedValue(simmMappings).flatMap(syn -> ofNullable(synonymToExceptionEnumMap.get(syn)));
		if (simm.isPresent()) {
			simmExceptionBuilder.setStandardisedException(simm.get());
			simmMappings.forEach(m -> updateMapping(m, path));
		} else {
			return Optional.empty();
		}

		List<Mapping> simmExceptionMappings = findMappings(regimePath, party, "_fallback", index);
		findMappedValue(simmExceptionMappings).ifPresent(xmlValue -> {
			ofNullable(synonymToSimmExceptionApplicableEnumMap.get(xmlValue)).ifPresent(simmExceptionBuilder::setSimmExceptionApplicable);
			simmExceptionMappings.forEach(m -> updateMapping(m, path));
		});

		List<Mapping> simmSpecifyMappings = findMappings(regimePath, party, "_SIMM_specify", index);
		findMappedValue(simmSpecifyMappings).ifPresent(xmlValue -> {
			simmExceptionBuilder.setAsSpecified(xmlValue);
			simmSpecifyMappings.forEach(m -> updateMapping(m, path));
		});

		List<Mapping> fallbackSpecifyMappings = findMappings(regimePath, party, "_fallback_specify", index);
		findMappedValue(fallbackSpecifyMappings).ifPresent(xmlValue -> {
			simmExceptionBuilder.setAsSpecified(xmlValue);
			fallbackSpecifyMappings.forEach(m -> updateMapping(m, path));
		});

		List<Mapping> simmApplicableSpecifyMappings = findMappings(regimePath, party, "_SIMM_applicable_specify", index);
		findMappedValue(simmApplicableSpecifyMappings).ifPresent(xmlValue -> {
			simmExceptionBuilder.setAsSpecified(xmlValue);
			simmApplicableSpecifyMappings.forEach(m -> updateMapping(m, path));
		});

		return Optional.of(simmExceptionBuilder);
	}

	private Optional<RetrospectiveEffect.RetrospectiveEffectBuilder> getRetrospectiveEffect(Path regimePath, String party, Integer index) {
		RetrospectiveEffect.RetrospectiveEffectBuilder retrospectiveEffectBuilder = RetrospectiveEffect.builder();

		List<Mapping> standardisedExceptionMappings = findMappings(regimePath, party, "_retrospective", index);
		Optional<ExceptionEnum> standardisedException = findMappedValue(standardisedExceptionMappings).flatMap(syn -> ofNullable(synonymToExceptionEnumMap.get(syn)));
		if (standardisedException.isPresent()) {
			retrospectiveEffectBuilder.setStandardisedException(standardisedException.get());
			standardisedExceptionMappings.forEach(m -> updateMapping(m, path));
		} else {
			return Optional.empty();
		}

		List<Mapping> specifyMappings = findMappings(regimePath, party, "_retrospective_specify", index);
		findMappedValue(specifyMappings).ifPresent(xmlValue -> {
			retrospectiveEffectBuilder.setAsSpecified(xmlValue);
			specifyMappings.forEach(m -> updateMapping(m, path));
		});

		return Optional.of(retrospectiveEffectBuilder);
	}

	List<Mapping> findMappings(Path basePath, String synonym) {
		return findMappings(basePath, "", synonym, null);
	}

	List<Mapping> findMappings(Path basePath, String party, String synonym, Integer index) {
		Path path = getSynonymPath(basePath, party, synonym, index);
		return MappingProcessorUtils.findMappings(mappings, path);
	}

	Path getSynonymPath(Path basePath, String synonym) {
		return getSynonymPath(basePath, "", synonym, null);
	}

	Path getSynonymPath(Path basePath, String synonym, Integer index) {
		return getSynonymPath(basePath, "", synonym, index);
	}

	Path getSynonymPath(Path basePath, String partyPrefix, String synonym) {
		return getSynonymPath(basePath, partyPrefix, synonym, null);
	}

	Path getSynonymPath(Path basePath, String partyPrefix, String synonym, Integer index) {
		PathElement element = ofNullable(index)
				.map(i -> new PathElement(partyPrefix + synonym, i))
				.orElse(new PathElement(partyPrefix + synonym));
		return basePath.addElement(element);
	}
}
