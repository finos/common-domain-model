package cdm.product.common.settlement.processor;

import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.SpreadTypeEnum;
import cdm.product.common.settlement.PriceQuantity;
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
import static cdm.observable.asset.metafields.FieldWithMetaPriceSchedule.FieldWithMetaPriceScheduleBuilder;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.toReferencablePriceBuilder;
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
		List<FieldWithMetaPriceScheduleBuilder> priceBuilders = emptyIfNull((List<FieldWithMetaPriceScheduleBuilder>) builders);
		UnitTypeBuilder unitOfAmount = getUnit(priceBuilders);
		UnitTypeBuilder perUnitOfAmount = getPerUnitOf(priceBuilders);

		AtomicInteger priceIndex = new AtomicInteger(priceBuilders.size());

		getBuilder(synonymPath.addElement("spotRate"), priceIndex, unitOfAmount, perUnitOfAmount, getPriceExpression(SpreadTypeEnum.BASE))
				.ifPresent(priceQuantityBuilder::addPrice);
		getBuilder(synonymPath.addElement("forwardPoints"), priceIndex, unitOfAmount, perUnitOfAmount, getPriceExpression(SpreadTypeEnum.SPREAD))
				.ifPresent(priceQuantityBuilder::addPrice);
		getBuilder(synonymPath.addElement("pointValue"), priceIndex, unitOfAmount, perUnitOfAmount, getPriceExpression(SpreadTypeEnum.SPREAD))
				.ifPresent(priceQuantityBuilder::addPrice);
	}

	private PriceExpression.PriceExpressionBuilder getPriceExpression(SpreadTypeEnum base) {
		return PriceExpression.builder().setPriceType(PriceTypeEnum.EXCHANGE_RATE).setSpreadType(base);
	}

	@NotNull
	private Optional<FieldWithMetaPriceScheduleBuilder> getBuilder(Path synonymPath,
			AtomicInteger priceIndex,
			UnitTypeBuilder unitOfAmount,
			UnitTypeBuilder perUnitOfAmount,
			PriceExpression priceExpression) {
		return getNonNullMapping(getMappings(), synonymPath).map(mapping -> {
			// update price index to ensure unique model path, otherwise any references will break
			Path mappedModelPath = toPath(getModelPath()).addElement("value");
			String amount = String.valueOf(mapping.getXmlValue());
			updateMappings(synonymPath, mappedModelPath, amount);
			return toReferencablePriceBuilder(new BigDecimal(amount),
					unitOfAmount,
					perUnitOfAmount,
					priceExpression);
		});
	}

	private UnitTypeBuilder getUnit(List<FieldWithMetaPriceScheduleBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(p -> p.getUnit())
				.orElse(null);
	}

	private UnitTypeBuilder getPerUnitOf(List<FieldWithMetaPriceScheduleBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(p -> p.getPerUnitOf())
				.orElse(null);
	}

	@NotNull
	private Optional<PriceSchedule.PriceScheduleBuilder> getExchangeRatePrice(List<FieldWithMetaPriceScheduleBuilder> priceBuilders) {
		return priceBuilders.stream()
				.map(FieldWithMetaPriceScheduleBuilder::getValue)
				.filter(p -> Optional.ofNullable(p)
						.map(PriceSchedule::getPriceExpression)
						.map(PriceExpression::getPriceType)
						.map(PriceTypeEnum.EXCHANGE_RATE::equals)
						.orElse(false))
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
