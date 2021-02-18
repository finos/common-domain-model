package cdm.observable.asset.processor;

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

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.observable.asset.metafields.FieldWithMetaPrice.FieldWithMetaPriceBuilder;
import static cdm.observable.asset.processor.PriceHelper.toReferencablePriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * FpML mapper required due to issues with multiple FX rates (e.g. rate, spotRate, forwardPoints, pointValue) to the same PriceQuantity.price.
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
		UnitTypeBuilder unitOfAmount = getUnitOfAmount(priceBuilders);
		UnitTypeBuilder perUnitOfAmount = getPerUnitOfAmount(priceBuilders);

		AtomicInteger priceIndex = new AtomicInteger(priceBuilders.size());

		getBuilder(synonymPath.addElement("spotRate"), priceIndex, unitOfAmount, perUnitOfAmount, PriceTypeEnum.SPOT)
				.ifPresent(priceQuantityBuilder::addPriceBuilder);
		getBuilder(synonymPath.addElement("forwardPoints"), priceIndex, unitOfAmount, perUnitOfAmount, PriceTypeEnum.FORWARD_POINTS)
				.ifPresent(priceQuantityBuilder::addPriceBuilder);
		getBuilder(synonymPath.addElement("pointValue"), priceIndex, unitOfAmount, perUnitOfAmount, null)
				.ifPresent(priceQuantityBuilder::addPriceBuilder);
	}

	@NotNull
	private Optional<FieldWithMetaPriceBuilder> getBuilder(Path synonymPath,
			AtomicInteger priceIndex,
			UnitTypeBuilder unitOfAmount,
			UnitTypeBuilder perUnitOfAmount,
			PriceTypeEnum priceType) {
		return getNonNullMapping(getMappings(), synonymPath).map(mapping -> {
			// update price index to ensure unique model path, otherwise any references will break
			Path baseModelPath = toPath(getModelPath()).addElement("amount");
			Path mappedModelPath = PriceHelper.incrementPricePathElementIndex(baseModelPath, priceIndex.getAndIncrement());
			String amount = String.valueOf(mapping.getXmlValue());
			updateMappings(synonymPath, mappedModelPath, amount);
			return toReferencablePriceBuilder(new BigDecimal(amount),
					unitOfAmount,
					perUnitOfAmount,
					priceType);
		});
	}

	private UnitTypeBuilder getUnitOfAmount(List<FieldWithMetaPriceBuilder> priceBuilders) {
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
