package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.Path.PathElement;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.UmbrellaAgreement.UmbrellaAgreementBuilder;
import org.isda.cdm.UmbrellaAgreementEntity;
import org.isda.cdm.UmbrellaAgreementEntity.UmbrellaAgreementEntityBuilder;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class UmbrellaAgreementEntityMappingProcessor extends MappingProcessor {

	private static final Path BASE_PATH = Path.parse("answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule");
	
	public UmbrellaAgreementEntityMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		UmbrellaAgreementBuilder umbrellaAgreementBuilder = (UmbrellaAgreementBuilder) parent;
		umbrellaAgreementBuilder.clearParties();

		int i = 0;
		while (true) {
			UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder = UmbrellaAgreementEntity.builder();
			
			List<Mapping> principalNameMappings = findMappings("principal_name", i);
			Optional<String> principalName = setPrincpalName(umbrellaAgreementEntityBuilder, principalNameMappings);

			List<Mapping> leiMappings = findMappings("lei", i);
			Optional<String> lei = setLei(umbrellaAgreementEntityBuilder, leiMappings);

			List<Mapping> additionalMappings = findMappings("additional", i);
			Optional<String> additional = setAdditional(umbrellaAgreementEntityBuilder, additionalMappings);

			if (principalName.isPresent() || lei.isPresent() || additional.isPresent()) {
				umbrellaAgreementBuilder.addPartiesBuilder(umbrellaAgreementEntityBuilder);
				i++;
			} else {
				break;
			}
		}

		// TODO: the below code is necessary because if there is only one item in the list, then the path does not have an index.
		// e.g. answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule.principal_name rather
		// than answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule.principal_name(0).
		// The parser should be fixed to create paths consistently.

		UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder = UmbrellaAgreementEntity.builder();

		List<Mapping> principalNameMappings = findMappings("principal_name");
		Optional<String> principalName = setPrincpalName(umbrellaAgreementEntityBuilder, principalNameMappings);

		List<Mapping> leiMappings = findMappings("lei");
		Optional<String> lei = setLei(umbrellaAgreementEntityBuilder, leiMappings);

		List<Mapping> additionalMappings = findMappings("additional");
		Optional<String> additional = setAdditional(umbrellaAgreementEntityBuilder, additionalMappings);

		if (principalName.isPresent() || lei.isPresent() || additional.isPresent()) {
			umbrellaAgreementBuilder.addPartiesBuilder(umbrellaAgreementEntityBuilder);
		}
	}

	private Optional<String> setPrincpalName(UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder,
			List<Mapping> principalNameMappings) {
		Optional<String> principalName = findMappedValue(principalNameMappings);
		principalName.ifPresent(xmlValue -> {
			umbrellaAgreementEntityBuilder.setNameRef(xmlValue);
			principalNameMappings.forEach(m -> updateMapping(m, getPath()));
		});
		return principalName;
	}

	private Optional<String> setLei(UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder, List<Mapping> leiMappings) {
		Optional<String> lei = findMappedValue(leiMappings);
		lei.ifPresent(xmlValue -> {
			umbrellaAgreementEntityBuilder.addEntityId(toFieldWithMetaString(xmlValue));
			leiMappings.forEach(m -> updateMapping(m, getPath()));
		});
		return lei;
	}

	private Optional<String> setAdditional(UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder, List<Mapping> additionalMappings) {
		Optional<String> additional = findMappedValue(additionalMappings);
		additional.ifPresent(xmlValue -> {
			umbrellaAgreementEntityBuilder.setTerms(xmlValue);
			additionalMappings.forEach(m -> updateMapping(m, getPath()));
		});
		return additional;
	}

	private List<Mapping> findMappings(String attribute, int index) {
		return MappingProcessorUtils.findMappings(getMappings(), BASE_PATH.addElement(new PathElement(attribute, index)));
	}

	private List<Mapping> findMappings(String attribute) {
		return MappingProcessorUtils.findMappings(getMappings(), BASE_PATH.addElement(new PathElement(attribute)));
	}
}