package cdm.product.common.settlement.processor;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.MeasureBase;
import cdm.observable.asset.*;
import cdm.product.common.settlement.PriceQuantity;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

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
		@SuppressWarnings("unchecked")
		List<FieldWithMetaPriceScheduleBuilder> priceBuilders = emptyIfNull((List<FieldWithMetaPriceScheduleBuilder>) builders);
		UnitTypeBuilder unitOfAmount = getUnit(priceBuilders);
		UnitTypeBuilder perUnitOfAmount = getPerUnitOf(priceBuilders);

		AtomicInteger priceIndex = new AtomicInteger(priceBuilders.size());

		PriceComposite.PriceCompositeBuilder priceCompositeBuilder = PriceComposite.builder();
		getNonNullMapping(getMappings(), synonymPath.addElement("spotRate")).ifPresent(mapping -> {
			Path mappedModelPath = toPath(getModelPath());
			String amount = String.valueOf(mapping.getXmlValue());
			updateMappings(synonymPath, mappedModelPath, amount);
			priceCompositeBuilder.setBaseValue(new BigDecimal(amount));
		});

		getNonNullMapping(getMappings(), synonymPath.addElement("forwardPoints")).ifPresent(mapping -> {
			Path mappedModelPath = toPath(getModelPath());
			String amount = String.valueOf(mapping.getXmlValue());
			updateMappings(synonymPath, mappedModelPath, amount);
			priceCompositeBuilder.setOperand(new BigDecimal(amount));
			priceCompositeBuilder.setArithmeticOperator(ArithmeticOperationEnum.ADD);
			priceCompositeBuilder.setOperandType(PriceOperandEnum.FORWARD_POINT);
		});

		getBuilder(synonymPath.addElement("pointValue"), priceIndex, unitOfAmount, perUnitOfAmount, priceCompositeBuilder.build())
				.ifPresent(priceQuantityBuilder::addPrice);
	}

	private Optional<FieldWithMetaPriceScheduleBuilder> getBuilder(Path synonymPath,
																   AtomicInteger priceIndex,
																   UnitTypeBuilder unitOfAmount,
																   UnitTypeBuilder perUnitOfAmount,
																   PriceComposite priceComposite) {
		return getNonNullMapping(getMappings(), synonymPath).map(mapping -> {
			// update price index to ensure unique model path, otherwise any references will break
			Path mappedModelPath = toPath(getModelPath()).addElement("value");
			String amount = String.valueOf(mapping.getXmlValue());
			updateMappings(synonymPath, mappedModelPath, amount);
			return toReferencablePriceBuilder(new BigDecimal(amount),
					unitOfAmount,
					perUnitOfAmount,
					PriceTypeEnum.EXCHANGE_RATE,
					null,
					priceComposite);
		});
	}

	private UnitTypeBuilder getUnit(List<FieldWithMetaPriceScheduleBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(MeasureBase.MeasureBaseBuilder::getUnit)
				.orElse(null);
	}

	private UnitTypeBuilder getPerUnitOf(List<FieldWithMetaPriceScheduleBuilder> priceBuilders) {
		return getExchangeRatePrice(priceBuilders)
				.map(PriceSchedule.PriceScheduleBuilder::getPerUnitOf)
				.orElse(null);
	}

	private Optional<PriceSchedule.PriceScheduleBuilder> getExchangeRatePrice(List<FieldWithMetaPriceScheduleBuilder> priceBuilders) {
		return priceBuilders.stream()
				.map(FieldWithMetaPriceScheduleBuilder::getValue)
				.filter(p -> Optional.ofNullable(p)
						.map(PriceSchedule::getPriceType)
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
