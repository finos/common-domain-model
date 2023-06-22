package cdm.product.common.settlement.processor;

import cdm.base.math.DatedValue;
import cdm.observable.asset.CapFloorEnum;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.product.common.settlement.PriceQuantity;
import com.regnosys.rosetta.common.translation.*;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.base.math.UnitType.builder;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.toReferencablePriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

/**
 * FpML mapper required due to issues with multiple rates (e.g. capRate and floor) to the same PriceQuantity.price.
 * <p>
 * Both rates are mapped to the same price instance.  Add floorRate as a new Price instance (on the same PriceQuantity), and update mapping stats.
 */
@SuppressWarnings("unused")
public class PriceCollarMappingProcessor extends MappingProcessor {

	public PriceCollarMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		subPath("capFloorStream", synonymPath).ifPresent(subPath -> {
			// If both floorRate and capRate mappings exist, then we need to resolve the mapping issue
			if (getNonNullMapping(getMappings(), getModelPath(), subPath, "capRateSchedule", "initialValue").isPresent()) {
				getNonNullMapping(getMappings(), getModelPath(), subPath, "floorRateSchedule", "initialValue").ifPresent(frm -> {
					UnitTypeBuilder unitType = toCurrencyUnitType(subPath);
					BigDecimal floorRate = new BigDecimal(String.valueOf(frm.getXmlValue()));
					PriceExpression.PriceExpressionBuilder priceExpression = PriceExpression.builder()
							.setPriceType(PriceTypeEnum.INTEREST_RATE)
							.setCapFloor(CapFloorEnum.FLOOR);
					FieldWithMetaPriceSchedule.FieldWithMetaPriceScheduleBuilder fieldWithPriceScheduleBuilder =
							toReferencablePriceBuilder(floorRate, unitType, unitType, priceExpression);
					PriceSchedule.PriceScheduleBuilder priceScheduleBuilder = fieldWithPriceScheduleBuilder.getValue();
					// update price index, e.g. floorRate and capRate were previously mapped to the same field so the price index
					// must be incremented otherwise any references will break
					Path amountModelPath = incrementPathElementIndex(frm.getRosettaPath(), "price", 1);
					updateMapping(frm, amountModelPath);
					// add schedule (if exists)
					priceScheduleBuilder.setDatedValue(getSteps(frm.getXmlPath().getParent(), amountModelPath.getParent()));
					// add to PriceQuantity
					((PriceQuantity.PriceQuantityBuilder) parent).addPrice(fieldWithPriceScheduleBuilder);

				});
			}
		});
	}

	private UnitTypeBuilder toCurrencyUnitType(Path startsWithPath) {
		String currency = getNonNullMappedValue(getMappings(), startsWithPath, "notionalStepSchedule", "currency").orElse(null);
		String currencyScheme = getNonNullMappedValue(getMappings(), startsWithPath, "notionalStepSchedule", "currency", "currencyScheme").orElse(null);
		return builder()
				.setCurrency(FieldWithMetaString.builder()
						.setValue(currency)
						.setMeta(MetaFields.builder()
								.setScheme(currencyScheme)
								.build()));
	}

	private void updateMapping(Mapping mapping, Path modelPath) {
		mapping.setRosettaPath(modelPath);
		// clear errors
		mapping.setError(null);
		mapping.setCondition(true);
		mapping.setDuplicate(false);
	}

	private List<DatedValue.DatedValueBuilder> getSteps(Path floorScheduleSynonymPath, Path priceScheduleModelPath) {
		List<DatedValue.DatedValueBuilder> steps = new ArrayList<>();
		int index = 0;
		while (true) {
			Optional<DatedValue.DatedValueBuilder> step = getStep(floorScheduleSynonymPath, priceScheduleModelPath, index++);
			if (!step.isPresent()) {
				break;
			}
			steps.add(step.get());
		}
		return steps;
	}

	private Optional<DatedValue.DatedValueBuilder> getStep(Path floorScheduleSynonymPath, Path priceScheduleModelPath, int index) {
		DatedValue.DatedValueBuilder stepBuilder = DatedValue.builder();

		Path synonymPath = floorScheduleSynonymPath.addElement("step", index);
		Path modelPath = priceScheduleModelPath.addElement("step", index);

		MappingProcessorUtils.setValueAndUpdateMappings(synonymPath.addElement("stepValue"),
				(xmlValue) -> stepBuilder.setValue(new BigDecimal(xmlValue)),
				getMappings(),
				PathUtils.toRosettaPath(modelPath.addElement("stepValue")));

		MappingProcessorUtils.setValueAndUpdateMappings(synonymPath.addElement("stepDate"),
				(xmlValue) -> stepBuilder.setDate(Date.parse(xmlValue)),
				getMappings(),
				PathUtils.toRosettaPath(modelPath.addElement("stepDate")));

		return stepBuilder.hasData() ? Optional.of(stepBuilder) : Optional.empty();
	}
}
