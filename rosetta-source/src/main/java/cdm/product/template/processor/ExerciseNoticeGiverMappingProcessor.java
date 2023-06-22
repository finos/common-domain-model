package cdm.product.template.processor;

import cdm.product.template.ExerciseNoticeGiverEnum;
import com.regnosys.rosetta.common.translation.*;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cdm.product.template.ExerciseNotice.ExerciseNoticeBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class ExerciseNoticeGiverMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseNoticeGiverMappingProcessor.class);

	private static final String[] OPTION_PAYOUT_BUYER_CDM_PATH = { "optionPayout", "buyerSeller", "buyer" };
	private static final String[] OPTION_PAYOUT_SELLER_CDM_PATH = { "optionPayout", "buyerSeller", "seller" };

	public ExerciseNoticeGiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
			setValueAndOptionallyUpdateMappings(
					synonymPath.addElement("href"),
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
		List<Mapping> tradableProductMappings = filterMappingsToTradableProduct();
		if (getModelPath().containsPath(RosettaPath.valueOf("exerciseProcedure.manualExercise"))) {
			String buyerParty = getXmlValue(tradableProductMappings, m -> m.getRosettaPath().endsWith(OPTION_PAYOUT_BUYER_CDM_PATH));
			String sellerParty = getXmlValue(tradableProductMappings, m -> m.getRosettaPath().endsWith(OPTION_PAYOUT_SELLER_CDM_PATH));
			return noticeGiver.equals(buyerParty) ? Optional.of(ExerciseNoticeGiverEnum.BUYER)
					: noticeGiver.equals(sellerParty) ? Optional.of(ExerciseNoticeGiverEnum.SELLER)
					: Optional.empty();
		}
		return Optional.empty();
	}

	/**
	 * If there are multiple contracts (e.g. in an event) then ensure the buyer / seller is in the same
	 * tradable product as the exercise notice.
	 */
	private List<Mapping> filterMappingsToTradableProduct() {
		String cdmPath = getModelPath().buildPath();
		Path tradableProductPath = PathUtils.toPath(RosettaPath.valueOf(cdmPath.substring(0, cdmPath.indexOf(".product."))));
		return getMappings().stream()
				.filter(m -> m.getRosettaPath() != null)
				.filter(m -> tradableProductPath.fullStartMatches(m.getRosettaPath()))
				.collect(Collectors.toList());
	}

	private String getXmlValue(List<Mapping> tradableProductMappings, Function<Mapping, Boolean> mappingFilter) {
		return MappingProcessorUtils.getNonNullMappedValue(
				tradableProductMappings.stream()
						.filter(mappingFilter::apply)
						.collect(Collectors.toList()))
				.orElse(null);
	}
}
