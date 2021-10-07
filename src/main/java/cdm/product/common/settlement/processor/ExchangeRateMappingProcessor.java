package cdm.product.common.settlement.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.SpreadTypeEnum;
import cdm.product.common.settlement.PriceQuantity;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.observable.asset.metafields.FieldWithMetaPrice.FieldWithMetaPriceBuilder;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.toReferencablePriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * FpML mapper required due to issues with multiple FX rates (e.g. rate, spotRate, forwardPoints, pointValue) to the same PriceQuantity.price.
 */
@SuppressWarnings("unused")
public class ExchangeRateMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateMappingProcessor.class);

	public ExchangeRateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		PriceQuantity.PriceQuantityBuilder priceQuantityBuilder = (PriceQuantity.PriceQuantityBuilder) parent;
		List<FieldWithMetaPriceBuilder> priceBuilders = emptyIfNull((List<FieldWithMetaPriceBuilder>) builders);
		UnitType unitOfAmount = getUnitOfAmount(priceBuilders);
		UnitTypeBuilder perUnitOfAmount = getPerUnitOfAmount(priceBuilders);

		AtomicInteger priceIndex = new AtomicInteger(priceBuilders.size());

		getNonNullMapping(getMappings(), synonymPath.addElement("spotRate"))
				.map(m -> getSpotRate(m, unitOfAmount, perUnitOfAmount, priceIndex.getAndIncrement()))
				.ifPresent(priceQuantityBuilder::addPrice);

		getNonNullMapping(getMappings(), synonymPath.addElement("forwardPoints"))
				.map(m -> getForwardPoints(m, unitOfAmount, perUnitOfAmount, priceIndex.getAndIncrement(), synonymPath.addElement("pointValue")))
				.ifPresent(priceQuantityBuilder::addPrice);
	}

	@NotNull
	private FieldWithMetaPriceBuilder getSpotRate(Mapping mapping, UnitType unitOfAmount, UnitTypeBuilder perUnitOfAmount, int index) {
		// update price index to ensure unique model path, otherwise any references will break
		Path baseModelPath = toPath(getModelPath()).addElement("amount");
		Path mappedModelPath = incrementPathElementIndex(baseModelPath, "price", index);
		String spotRate = String.valueOf(mapping.getXmlValue());
		updateMappings(mapping, mappedModelPath, spotRate);
		return toReferencablePriceBuilder(new BigDecimal(spotRate),
				unitOfAmount,
				perUnitOfAmount,
				getPriceExpression(SpreadTypeEnum.BASE));
	}

	@NotNull
	private FieldWithMetaPriceBuilder getForwardPoints(Mapping mapping, UnitType unitOfAmount, UnitTypeBuilder perUnitOfAmount, int index, Path pointValuePath) {
		// update price index to ensure unique model path, otherwise any references will break
		Path baseModelPath = toPath(getModelPath()).addElement("amount");
		Path mappedModelPath = incrementPathElementIndex(baseModelPath, "price", index);
		String forwardPoints = String.valueOf(mapping.getXmlValue());
		updateMappings(mapping, mappedModelPath, forwardPoints);

		BigDecimal calculatedForwardPoints = new BigDecimal(forwardPoints);

		Optional<Mapping> pointValueMapping = getNonNullMapping(getMappings(), pointValuePath);
		if (pointValueMapping.isPresent()) {
			String pointValue = String.valueOf(pointValueMapping.get().getXmlValue());
			updateMappings(pointValueMapping.get(), mappedModelPath, pointValue);

			calculatedForwardPoints = calculatedForwardPoints.multiply(new BigDecimal(pointValue));
			LOGGER.info("Calculated forward points amount [forwardPoints={}, pointValue={}, calculatedForwardPoints={}]", forwardPoints, pointValue, calculatedForwardPoints);
		}

		return toReferencablePriceBuilder(calculatedForwardPoints,
				unitOfAmount,
				perUnitOfAmount,
				getPriceExpression(SpreadTypeEnum.SPREAD));
	}

	private PriceExpression.PriceExpressionBuilder getPriceExpression(SpreadTypeEnum spreadType) {
		return PriceExpression.builder()
				.setPriceType(PriceTypeEnum.EXCHANGE_RATE)
				.setSpreadType(spreadType);
	}

	private UnitType getUnitOfAmount(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(Price.PriceBuilder::getUnitOfAmount)
				.orElse(null);
	}

	private UnitTypeBuilder getPerUnitOfAmount(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(Price.PriceBuilder::getPerUnitOfAmount)
				.orElse(null);
	}

	@NotNull
	private Optional<Price.PriceBuilder> getExchangeRatePrice(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return priceBuilders.stream()
				.map(FieldWithMetaPriceBuilder::getValue)
				.filter(p -> Optional.ofNullable(p)
						.map(Price::getPriceExpression)
						.map(PriceExpression::getPriceType)
						.map(PriceTypeEnum.EXCHANGE_RATE::equals)
						.orElse(false))
				.findFirst();
	}

	private void updateMappings(Mapping mapping, Path modelPath, String mappedValue) {
		mapping.setRosettaPath(modelPath);
		mapping.setRosettaValue(mappedValue);
		// clear errors
		mapping.setError(null);
		mapping.setCondition(true);
		mapping.setDuplicate(false);
	}
}
