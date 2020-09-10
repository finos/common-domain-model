package cdm.product.template.processor;

import cdm.product.template.ExerciseNoticeGiverEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.product.template.ExerciseNotice.ExerciseNoticeBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.util.PathUtils.toRosettaPath;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class ExerciseNoticeGiverMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseNoticeGiverMappingProcessor.class);

	private static final String[] OPTION_BUYER_PATH = { "optionPayout", "buyerSeller", "buyer" };
	private static final String[] OPTION_SELLER_PATH = { "optionPayout", "buyerSeller", "seller" };

	public ExerciseNoticeGiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		setValueAndOptionallyUpdateMappings(
				synonymPath,
				(noticeGiverParty) -> {
					Optional<ExerciseNoticeGiverEnum> noticeGiverEnum = getExerciseNoticeGiverEnum(noticeGiverParty);
					if (noticeGiverEnum.isPresent()) {
						LOGGER.info("Exercise notice giver party [{}] was mapped to option {}.", noticeGiverParty, noticeGiverEnum.get());
						// set attribute
						((ExerciseNoticeBuilder) parent).setExerciseNoticeGiver(noticeGiverEnum.get());
						// return true to update mappings to success
						return true;
					} else {
						LOGGER.info("Exercise notice giver party [{}] could not be mapped to either option BUYER or SELLER.", noticeGiverParty);
						// return false to update mappings to failure
						return false;
					}
				},
				getMappings(),
				getModelPath());
	}

	private Optional<ExerciseNoticeGiverEnum> getExerciseNoticeGiverEnum(String noticeGiver) {
		if (noticeGiver.equals(getNonNullMappedValue(OPTION_BUYER_PATH))) {
			return Optional.of(ExerciseNoticeGiverEnum.BUYER);
		} else if (noticeGiver.equals(getNonNullMappedValue(OPTION_SELLER_PATH))) {
			return Optional.of(ExerciseNoticeGiverEnum.SELLER);
		} else {
			return Optional.empty();
		}
	}

	private String getNonNullMappedValue(String... cdmPathEndsWith) {
		String cdmPath = getModelPath().buildPath();
		Path tradableProductPath = PathUtils.toPath(RosettaPath.valueOf(cdmPath.substring(0, cdmPath.indexOf(".product."))));
		return MappingProcessorUtils.getNonNullMappedValue(
				getMappings().stream()
						.filter(m -> m.getRosettaPath() != null && m.getRosettaValue() != null)
						.filter(m -> tradableProductPath.fullStartMatches(m.getRosettaPath()))
						.filter(m -> m.getRosettaPath().endsWith(cdmPathEndsWith))
						.collect(Collectors.toList()))
				.orElse(null);
	}
}
