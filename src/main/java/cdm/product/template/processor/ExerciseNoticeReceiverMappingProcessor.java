package cdm.product.template.processor;

import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.product.template.ExerciseNotice;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.PartyMappingHelper;
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

	public ExerciseNoticeReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		setRelatedParty(synonymPath, (ExerciseNotice.ExerciseNoticeBuilder) parent, getRelatedPartyEnum());
	}

	private RelatedPartyEnum getRelatedPartyEnum() {
		// TODO check path and return associated enum
		return RelatedPartyEnum.MANUAL_EXERCISE_NOTICE_RECEIVER_PARTY;
	}

	private void setRelatedParty(Path synonymPath, ExerciseNotice.ExerciseNoticeBuilder builder, RelatedPartyEnum relatedPartyEnum) {
		setValueAndUpdateMappings(synonymPath,
				partyExternalReference -> {
					// set related party enum (inside product)
					builder.setExerciseNoticeReceiver(relatedPartyEnum);
					// add to related parties list (outside product)
					PartyMappingHelper.getInstance(getContext())
							.orElseThrow(() -> new IllegalStateException("PartyMappingHelper not found."))
							.addRelatedParties(partyExternalReference, relatedPartyEnum);
				});
	}
}
