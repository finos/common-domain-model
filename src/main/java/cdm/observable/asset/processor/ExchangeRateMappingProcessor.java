package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static cdm.observable.asset.metafields.FieldWithMetaPrice.FieldWithMetaPriceBuilder;
import static cdm.observable.asset.processor.PriceHelper.toPriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Mapper required due to mapping issues with multiple prices.
 * <p>
 * Fix synonym mapping logic and remove these mappers.
 */
@SuppressWarnings("unused")
public class ExchangeRateMappingProcessor extends MappingProcessor {

	public ExchangeRateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		PriceQuantity.PriceQuantityBuilder priceQuantityBuilder = (PriceQuantity.PriceQuantityBuilder) parent;
		List<FieldWithMetaPriceBuilder> priceBuilders = emptyIfNull((List<FieldWithMetaPriceBuilder>) builders);
		UnitType.UnitTypeBuilder unitOfAmount = getUnitOfAmount(priceBuilders);
		UnitType.UnitTypeBuilder perUnitOfAmount = getPerUnitOfAmount(priceBuilders);

		AtomicInteger priceIndex = new AtomicInteger(priceBuilders.size());
		Path mappedModelPath = toPath(getModelPath()).addElement("amount");

		Path spotRatePath = synonymPath.addElement("spotRate");
		getNonNullMapping(getMappings(), spotRatePath).ifPresent(spotRateMapping -> {
			String spotRateAmount = String.valueOf(spotRateMapping.getXmlValue());
			priceQuantityBuilder.addPriceBuilder(
					toPriceBuilder(new BigDecimal(spotRateAmount),
							unitOfAmount,
							perUnitOfAmount,
							PriceTypeEnum.SPOT));
			// update price index to ensure unique model path, otherwise any references will break
			Path spotRateModelPath = PriceHelper.incrementPricePathElementIndex(mappedModelPath, priceIndex.getAndIncrement());
			updateMappings(spotRatePath, spotRateModelPath, spotRateAmount);
		});

		Path forwardPointsPath = synonymPath.addElement("forwardPoints");
		getNonNullMapping(getMappings(), forwardPointsPath).ifPresent(forwardPointsMapping -> {
			String forwardPointsAmount = String.valueOf(forwardPointsMapping.getXmlValue());
			priceQuantityBuilder.addPriceBuilder(
					toPriceBuilder(new BigDecimal(String.valueOf(forwardPointsAmount)),
							unitOfAmount,
							perUnitOfAmount,
							PriceTypeEnum.FORWARD_POINTS));
			// update price index to ensure unique model path, otherwise any references will break
			Path forwardPointsModelPath = PriceHelper.incrementPricePathElementIndex(mappedModelPath, priceIndex.getAndIncrement());
			updateMappings(forwardPointsPath, forwardPointsModelPath, forwardPointsAmount);
		});

		Path pointsValuePath = synonymPath.addElement("pointValue");
		getNonNullMapping(getMappings(), pointsValuePath).ifPresent(pointsValueMapping -> {
			String pointsValueAmount = String.valueOf(pointsValueMapping.getXmlValue());
			priceQuantityBuilder.addPriceBuilder(
					toPriceBuilder(new BigDecimal(String.valueOf(pointsValueAmount)),
							unitOfAmount,
							perUnitOfAmount,
							null));
			// update price index to ensure unique model path, otherwise any references will break
			Path pointsValueModelPath = PriceHelper.incrementPricePathElementIndex(mappedModelPath, priceIndex.getAndIncrement());
			updateMappings(pointsValuePath, pointsValueModelPath, pointsValueAmount);
		});
	}

	private UnitType.UnitTypeBuilder getUnitOfAmount(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(Price.PriceBuilder::getUnitOfAmount)
				.orElse(null);
	}

	private UnitType.UnitTypeBuilder getPerUnitOfAmount(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(Price.PriceBuilder::getPerUnitOfAmount)
				.orElse(null);
	}

	@NotNull
	private Optional<Price.PriceBuilder> getExchangeRatePrice(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return priceBuilders.stream()
				.map(FieldWithMetaPriceBuilder::getValue)
				.filter(p -> p.getPriceType() == PriceTypeEnum.EXCHANGE_RATE)
				.findFirst();
	}

	private void updateMappings(Path xmlPath, Path mappedModelPath, String mappedValue) {
		filterMappings(getMappings(), xmlPath).forEach(m -> {
			m.setRosettaPath(mappedModelPath);
			m.setRosettaValue(mappedValue);
			// clear errors
			m.setError(null);
			m.setCondition(true);
		});
	}
}
