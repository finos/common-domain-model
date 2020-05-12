package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.Path.PathElement;
import static com.regnosys.rosetta.common.translation.Path.parse;
import static org.isda.cdm.processor.MappingProcessorUtils.findMappedValue;
import static org.isda.cdm.processor.MappingProcessorUtils.updateMapping;

class RegimeMappingHelper {

	static final List<String> PARTIES = Arrays.asList("partyA", "partyB");
	static final List<String> SUFFIXES = Arrays.asList("_secured_party", "_security_taker");
	static final Path BASE_PATH = parse("answers.partyA");

	private final RosettaPath path;
	private final List<Mapping> mappings;

	RegimeMappingHelper(RosettaPath path, List<Mapping> mappings) {
		this.path = path;
		this.mappings = mappings;
	}

	Optional<RegimeTerms.RegimeTermsBuilder> getRegimeTerms(Path regimePath, String party, String suffix, Integer index) {
		RegimeTerms.RegimeTermsBuilder regimeTermsBuilder = RegimeTerms.builder();

		List<Mapping> applicableMappings = findMappings(regimePath, party, suffix, index);
		Optional<String> applicable = findMappedValue(applicableMappings);

		if (applicable.isPresent()) {
			regimeTermsBuilder.setParty(party);
			switch (applicable.get()) {
			case "applicable":
				regimeTermsBuilder.setIsApplicable(true);
				break;
			case "not_applicable":
				regimeTermsBuilder.setIsApplicable(false);
				break;
			}
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

		List<Mapping> retrospectiveMappings = findMappings(regimePath, party, "_retrospective", index);
		Optional<String> retrospective = findMappedValue(retrospectiveMappings);
		retrospective.ifPresent(xmlValue -> {
			regimeTermsBuilder.setRetrospectiveEffect("applicable".equals(xmlValue));
			retrospectiveMappings.forEach(m -> updateMapping(m, path));
		});

		List<Mapping> additionalTypeMappings = findMappings(regimePath, "", "additional_type", index);
		Optional<String> additionalType = findMappedValue(additionalTypeMappings);
		additionalType.ifPresent(xmlValue -> {
			switch (xmlValue) {
			case "not_applicable":
				regimeTermsBuilder.setAdditionalType(AdditionalTypeEnum.NOT_APPLICABLE);
				break;
			case "other":
				regimeTermsBuilder.setAdditionalType(AdditionalTypeEnum.OTHER);
				break;
			}
			additionalTypeMappings.forEach(m -> updateMapping(m, path));
		});

		getSimmException(regimePath, party, index).ifPresent(regimeTermsBuilder::setSimmExceptionBuilder);

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
		Optional<String> simm = findMappedValue(simmMappings);

		if (simm.isPresent()) {
			switch (simm.get()) {
			case "applicable":
				simmExceptionBuilder.setStandardisedException(SimmExceptionEnum.APPLICABLE);
				break;
			case "not_applicable":
				simmExceptionBuilder.setStandardisedException(SimmExceptionEnum.NOT_APPLICABLE);
				break;
			case "other":
				simmExceptionBuilder.setStandardisedException(SimmExceptionEnum.OTHER);
				break;
			}
			simmMappings.forEach(m -> updateMapping(m, path));
		} else {
			return Optional.empty();
		}

		List<Mapping> simmExceptionMappings = findMappings(regimePath, party, "_fallback", index);
		Optional<String> simmException = findMappedValue(simmExceptionMappings);
		simmException.ifPresent(xmlValue -> {
			switch (xmlValue) {
			case "fallback":
				simmExceptionBuilder.setSimmExceptionApplicable(SimmExceptionApplicableEnum.FALL_BACK_TO_MANDATORY_METHOD);
				break;
			case "mandatory":
				simmExceptionBuilder.setSimmExceptionApplicable(SimmExceptionApplicableEnum.MANDATORY_METHOD);
				break;
			case "other":
				simmExceptionBuilder.setSimmExceptionApplicable(SimmExceptionApplicableEnum.OTHER_METHOD);
				break;
			}
			simmExceptionMappings.forEach(m -> updateMapping(m, path));
		});

		List<Mapping> simmSpecifyMappings = findMappings(regimePath, party, "_SIMM_specify", index);
		Optional<String> simmSpecify = findMappedValue(simmSpecifyMappings);
		simmSpecify.ifPresent(xmlValue -> {
			simmExceptionBuilder.setAsSpecified(xmlValue);
			simmSpecifyMappings.forEach(m -> updateMapping(m, path));
		});

		List<Mapping> fallbackSpecifyMappings = findMappings(regimePath, party, "_fallback_specify", index);
		Optional<String> fallbackSpecify = findMappedValue(fallbackSpecifyMappings);
		fallbackSpecify.ifPresent(xmlValue -> {
			simmExceptionBuilder.setAsSpecified(xmlValue);
			fallbackSpecifyMappings.forEach(m -> updateMapping(m, path));
		});

		List<Mapping> simmApplicableSpecifyMappings = findMappings(regimePath, party, "_SIMM_applicable_specify", index);
		Optional<String> simmApplicableSpecify = findMappedValue(simmApplicableSpecifyMappings);
		simmApplicableSpecify.ifPresent(xmlValue -> {
			simmExceptionBuilder.setAsSpecified(xmlValue);
			simmApplicableSpecifyMappings.forEach(m -> updateMapping(m, path));
		});

		return Optional.of(simmExceptionBuilder);
	}

	List<Mapping> findMappings(Path basePath, String synonym, Integer index) {
		return findMappings(basePath, "", synonym, index);
	}

	List<Mapping> findMappings(Path basePath, String party, String synonym, Integer index) {
		Path path = getMappedPath(basePath, party, synonym, index);
		return MappingProcessorUtils.findMappings(mappings, path);
	}

	Path getMappedPath(Path basePath, String party, String synonym, Integer index) {
		PathElement element = Optional.ofNullable(index)
				.map(i -> new PathElement(party + synonym, i))
				.orElse(new PathElement(party + synonym));
		return basePath.addElement(element);
	}
}
