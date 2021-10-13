package cdm.legalagreement.csa.processor;

import cdm.legalagreement.csa.RegulatoryRegimeEnum;
import cdm.legalagreement.csa.SubstitutedRegime;
import cdm.legalagreement.csa.SubstitutedRegimeTerms;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.*;

public class SubstitutedRegimeHelper {

	private final RosettaPath path;
	private final List<Mapping> mappings;
	private final Map<String, RegulatoryRegimeEnum> synonymToRegulatoryRegimeEnumMap;

	SubstitutedRegimeHelper(RosettaPath path, List<Mapping> mappings) {
		this.path = path;
		this.mappings = mappings;
		this.synonymToRegulatoryRegimeEnumMap = synonymToEnumValueMap(RegulatoryRegimeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@NotNull
	public List<SubstitutedRegime> getSubstitutedRegimes(Path synonymPath) {
		List<SubstitutedRegime> substitutedRegimes = mappings.stream()
				// find all sub-paths, e.g. partyA.answers.substitutedRegime.partyA_emir
				.filter(m -> synonymPath.nameStartMatches(m.getXmlPath()))
				// filter to only partyA
				.filter(m ->
						m.getXmlPath().getLastElement().getPathName().startsWith("partyA_")
								&& !m.getXmlPath().getLastElement().getPathName().endsWith("additional_substituted_regime")
				)
				// remove blanks
				.filter(m -> m.getXmlValue() != null)
				// build
				.map(m -> getSubstitutedRegime(m.getXmlPath()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());

		List<SubstitutedRegime> additionalSubstitutedRegimes = mappings.stream()
				.filter(m -> synonymPath.append(Path.parse("additional_substituted_regime.name")).nameStartMatches(m.getXmlPath()))
				.filter(m -> m.getXmlValue() != null)
				.map(this::getAdditionalSubstitutedRegime)
				.collect(Collectors.toList());

		List<SubstitutedRegime> results = new ArrayList<>();
		results.addAll(substitutedRegimes);
		results.addAll(additionalSubstitutedRegimes);
		return results;
	}

	private SubstitutedRegime getAdditionalSubstitutedRegime(Mapping additionalRegimeNameMapping) {
		SubstitutedRegime.SubstitutedRegimeBuilder substitutedRegimeBuilder = SubstitutedRegime.builder();
		setValueAndUpdateMappings(additionalRegimeNameMapping.getXmlPath(), substitutedRegimeBuilder::setAdditionalRegime, mappings, path);

		Integer index = additionalRegimeNameMapping.getXmlPath().getLastElement().getIndex().orElse(0);

		Path additionalSubstitutedRegimePath = additionalRegimeNameMapping.getXmlPath().getParent();

		PARTIES.forEach(party -> {
			getSubstitutedRegimeTerms(additionalSubstitutedRegimePath, party, "additional_substituted_regime[" + index + "]")
					.ifPresent(substitutedRegimeBuilder::addRegimeTerms);
		});

		return substitutedRegimeBuilder.build();
	}

	private Optional<SubstitutedRegime> getSubstitutedRegime(Path synonymPath) {
		SubstitutedRegime.SubstitutedRegimeBuilder substitutedRegimeBuilder = SubstitutedRegime.builder();

		String regulatoryRegime = removePartyPrefix(synonymPath.getLastElement().getPathName());
		PARTIES.forEach(party -> getSubstitutedRegimeTerms(synonymPath.getParent(), party, regulatoryRegime)
				.ifPresent(substitutedRegimeBuilder::addRegimeTerms));

		if (substitutedRegimeBuilder.hasData()) {
			RegulatoryRegimeEnum regulatoryRegimeEnum = synonymToRegulatoryRegimeEnumMap.get(regulatoryRegime);
			if (regulatoryRegimeEnum != null) {
				substitutedRegimeBuilder.setRegime(regulatoryRegimeEnum);
			} else {
				substitutedRegimeBuilder.setAdditionalRegime(regulatoryRegime);
			}
			return Optional.of(substitutedRegimeBuilder.build());
		}
		return Optional.empty();
	}

	private String removePartyPrefix(String pathName) {
		return pathName.substring(pathName.indexOf("_") + 1);
	}

	private Optional<SubstitutedRegimeTerms.SubstitutedRegimeTermsBuilder> getSubstitutedRegimeTerms(Path basePath, String party, String pathSuffix) {
		SubstitutedRegimeTerms.SubstitutedRegimeTermsBuilder substitutedRegimeTermsBuilder = SubstitutedRegimeTerms.builder();

		setValueAndUpdateMappings(basePath.append(Path.parse(party + "_" + pathSuffix)),
				(value) -> {
					substitutedRegimeTermsBuilder.setParty(toCounterpartyRoleEnum(party));
					substitutedRegimeTermsBuilder.setIsApplicable(value.equals("applicable"));
				},
				mappings,
				path);

		return substitutedRegimeTermsBuilder.hasData() ? Optional.of(substitutedRegimeTermsBuilder) : Optional.empty();
	}
}
