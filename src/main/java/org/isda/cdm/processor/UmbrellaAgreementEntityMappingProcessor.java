package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.UmbrellaAgreement.UmbrellaAgreementBuilder;
import org.isda.cdm.UmbrellaAgreementEntity;
import org.isda.cdm.UmbrellaAgreementEntity.UmbrellaAgreementEntityBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.getSynonymPath;
import static org.isda.cdm.processor.MappingProcessorUtils.toFieldWithMetaString;

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
			Optional<UmbrellaAgreementEntity> umbrellaAgreementEntity = getUmbrellaAgreementEntity(i++);
			if (umbrellaAgreementEntity.isPresent()) {
				umbrellaAgreementBuilder.addParties(umbrellaAgreementEntity.get());
			} else {
				break;
			}
		}
	}

	@NotNull
	private Optional<UmbrellaAgreementEntity> getUmbrellaAgreementEntity(Integer index) {
		UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder = UmbrellaAgreementEntity.builder();

		setValueAndUpdateMappings(getSynonymPath(BASE_PATH,"principal_name", index),
				umbrellaAgreementEntityBuilder::setNameRef);

		setValueAndUpdateMappings(getSynonymPath(BASE_PATH,"lei", index),
				(value) -> umbrellaAgreementEntityBuilder.addEntityId(toFieldWithMetaString(value)));

		setValueAndUpdateMappings(getSynonymPath(BASE_PATH,"additional", index),
				umbrellaAgreementEntityBuilder::setTerms);

		return umbrellaAgreementEntityBuilder.hasData() ? Optional.of(umbrellaAgreementEntityBuilder.build()) : Optional.empty();
	}
}