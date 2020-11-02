package cdm.product.template.processor;

import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.legalagreement.contract.processor.PartyMappingHelper;
import cdm.product.template.ExerciseNotice;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class ExerciseNoticeReceiverMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseNoticeReceiverMappingProcessor.class);

	private static final RosettaPath CANCELABLE_PROVISION_SUB_PATH = RosettaPath.valueOf("cancelableProvision");
	private static final RosettaPath EXTENDIBLE_PROVISION_SUB_PATH = RosettaPath.valueOf("extendibleProvision");
	private static final RosettaPath OPTIONAL_EARLY_TERMINATION_SUB_PATH = RosettaPath.valueOf("earlyTerminationProvision");
	private static final RosettaPath MANUAL_EXERCISE_SUB_PATH = RosettaPath.valueOf("manualExercise");

	public ExerciseNoticeReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		getAncillaryRoleEnum().ifPresent(role ->
				PartyMappingHelper.getInstanceOrThrow(getContext())
						.setAncillaryRoleEnum(getModelPath(),
								synonymPath.addElement("href"),
								((ExerciseNotice.ExerciseNoticeBuilder) parent)::setExerciseNoticeReceiver,
								role));
	}

	private Optional<AncillaryRoleEnum> getAncillaryRoleEnum() {
		if (getModelPath().containsPath(CANCELABLE_PROVISION_SUB_PATH)) {
			return Optional.of(AncillaryRoleEnum.CANCELABLE_PROVISION_EXERCISE_NOTICE_RECEIVER_PARTY);
		} else if (getModelPath().containsPath(EXTENDIBLE_PROVISION_SUB_PATH)) {
			return Optional.of(AncillaryRoleEnum.EXTENDIBLE_PROVISION_EXERCISE_NOTICE_RECEIVER_PARTY);
		} else if (getModelPath().containsPath(OPTIONAL_EARLY_TERMINATION_SUB_PATH)) {
			return Optional.of(AncillaryRoleEnum.OPTIONAL_EARLY_TERMINATION_EXERCISE_NOTICE_RECEIVER_PARTY);
		} else if (getModelPath().containsPath(MANUAL_EXERCISE_SUB_PATH)) {
			return Optional.of(AncillaryRoleEnum.MANUAL_EXERCISE_NOTICE_RECEIVER_PARTY);
		} else {
			return Optional.empty();
		}
	}
}
