package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.UmbrellaAgreement.UmbrellaAgreementBuilder;
import org.isda.cdm.UmbrellaAgreementEntity;
import org.isda.cdm.UmbrellaAgreementEntity.UmbrellaAgreementEntityBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.toFieldWithMetaString;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class UmbrellaAgreementEntityMappingProcessor extends MappingProcessor {

	public UmbrellaAgreementEntityMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		UmbrellaAgreementBuilder umbrellaAgreementBuilder = (UmbrellaAgreementBuilder) parent;
		umbrellaAgreementBuilder.clearParties();

		int index = 0;
		while (true) {
			Optional<UmbrellaAgreementEntity> umbrellaAgreementEntity = getUmbrellaAgreementEntity(synonymPath, index++);
			if (umbrellaAgreementEntity.isPresent()) {
				umbrellaAgreementBuilder.addParties(umbrellaAgreementEntity.get());
			} else {
				break;
			}
		}
	}

	@NotNull
	private Optional<UmbrellaAgreementEntity> getUmbrellaAgreementEntity(Path synonymPath, Integer index) {
		UmbrellaAgreementEntityBuilder umbrellaAgreementEntityBuilder = UmbrellaAgreementEntity.builder();

		setValueAndUpdateMappings(synonymPath.addElement("principal_name", index),
				umbrellaAgreementEntityBuilder::setNameRef);

		setValueAndUpdateMappings(synonymPath.addElement("lei", index),
				(value) -> umbrellaAgreementEntityBuilder.addEntityId(toFieldWithMetaString(value)));

		setValueAndUpdateMappings(synonymPath.addElement("additional", index),
				umbrellaAgreementEntityBuilder::setTerms);

		return umbrellaAgreementEntityBuilder.hasData() ? Optional.of(umbrellaAgreementEntityBuilder.build()) : Optional.empty();
	}
}