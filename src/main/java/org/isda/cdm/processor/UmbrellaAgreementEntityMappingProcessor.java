package org.isda.cdm.processor;

import static org.isda.cdm.processor.MappingProcessorUtils.findMappedValue;
import static org.isda.cdm.processor.MappingProcessorUtils.updateMapping;

import java.util.List;
import java.util.Optional;

import org.isda.cdm.UmbrellaAgreement.UmbrellaAgreementBuilder;
import org.isda.cdm.UmbrellaAgreementEntity;
import org.isda.cdm.UmbrellaAgreementEntity.UmbrellaAgreementEntityBuilder;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.Path.PathElement;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class UmbrellaAgreementEntityMappingProcessor extends MappingProcessor {

	private static final Path BASE_PATH = Path.parse("answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule");
	
	public UmbrellaAgreementEntityMappingProcessor(RosettaPath rosettaPath, List<Mapping> mappings) {
		super(rosettaPath, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		UmbrellaAgreementBuilder umbrellaAgreementBuilder = (UmbrellaAgreementBuilder) parent;
		umbrellaAgreementBuilder.clearParties();
		
		int i = 0;
		while (true) {
			UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder = UmbrellaAgreementEntity.builder();
			
			List<Mapping> principalNameMappings = findMappings("principal_name", i);
			Optional<String> principalName = findMappedValue(principalNameMappings);
			principalName.ifPresent(xmlValue -> {
				umbrellaAgreementEntityBuilder.setNameRef(xmlValue);
				principalNameMappings.forEach(m -> updateMapping(m, getPath()));
			});
			
			List<Mapping> leiMappings = findMappings("lei", i);
			Optional<String> lei = findMappedValue(leiMappings);
			lei.ifPresent(xmlValue -> {
				umbrellaAgreementEntityBuilder.addEntityId(FieldWithMetaString.builder().setValue(xmlValue).build());
				leiMappings.forEach(m -> updateMapping(m, getPath()));
			});
			
			List<Mapping> additionalMappings = findMappings("additional", i);
			Optional<String> additional = findMappedValue(additionalMappings);
			additional.ifPresent(xmlValue -> {
				umbrellaAgreementEntityBuilder.setTerms(xmlValue);
				additionalMappings.forEach(m -> updateMapping(m, getPath()));
			});
			
			if (principalName.isPresent() || lei.isPresent() || additional.isPresent()) {
				umbrellaAgreementBuilder.addPartiesBuilder(umbrellaAgreementEntityBuilder);
				i++;
			} else {
				break;
			}
		}
	}

	private List<Mapping> findMappings(String attribute, int index) {
		return MappingProcessorUtils.findMappings(getMappings(), BASE_PATH.addElement(new PathElement(attribute, index)));
	}
}