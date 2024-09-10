package cdm.observable.asset.processor;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.DatedValue;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
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
import static cdm.observable.asset.PriceQuantity.PriceQuantityBuilder;
import static cdm.observable.asset.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.observable.asset.processor.PriceQuantityHelper.toReferencablePriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * FpML mapper required due to issues with multiple rates (e.g. cap / floor / spread) to the same PriceQuantity.price.
 */
@SuppressWarnings("unused")
public class FloatingRateCalculationMappingProcessor extends MappingProcessor {

    public FloatingRateCalculationMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        subPath("capFloorStream", synonymPath)
                .ifPresent(subPath -> {
                    PriceQuantityBuilder priceQuantityBuilder = (PriceQuantityBuilder) parent;

                    getNonNullMapping(getMappings(), subPath, "capRateSchedule", "initialValue")
                            .ifPresent(m ->
                                    addPrice(subPath, priceQuantityBuilder, m, ArithmeticOperationEnum.MIN));

                    getNonNullMapping(getMappings(), subPath, "floorRateSchedule", "initialValue")
                            .ifPresent(m ->
                                    addPrice(subPath, priceQuantityBuilder, m, ArithmeticOperationEnum.MAX));
                });

        subPath("swapStream", synonymPath)
                .ifPresent(subPath -> {
                    PriceQuantityBuilder priceQuantityBuilder = (PriceQuantityBuilder) parent;

                    getNonNullMapping(getMappings(), subPath, "floatingRateMultiplierSchedule", "initialValue")
                            .ifPresent(m ->
                                    addPrice(subPath, priceQuantityBuilder, m, ArithmeticOperationEnum.MULTIPLY));
                });
    }

    private void addPrice(Path subPath, PriceQuantityBuilder priceQuantityBuilder, Mapping mapping, ArithmeticOperationEnum operator) {
        UnitTypeBuilder unitType = toCurrencyUnitType(subPath);
        BigDecimal rate = new BigDecimal(String.valueOf(mapping.getXmlValue()));
        PriceTypeEnum priceType = PriceTypeEnum.INTEREST_RATE;

        FieldWithMetaPriceSchedule.FieldWithMetaPriceScheduleBuilder fieldWithPriceScheduleBuilder =
                toReferencablePriceBuilder(rate, unitType, unitType, priceType, operator, null);
        PriceSchedule.PriceScheduleBuilder priceScheduleBuilder = fieldWithPriceScheduleBuilder.getValue();
        // price index must be incremented otherwise any references will break
        Path baseModelPath = toPath(getModelPath());
        Path amountModelPath = incrementPathElementIndex(baseModelPath, "price", emptyIfNull(priceQuantityBuilder.getPrice()).size());
        addMapping(mapping.getXmlPath(), mapping.getXmlValue(), amountModelPath, mapping.getXmlValue());
        // add schedule (if exists)
        priceScheduleBuilder.setDatedValue(getSteps(mapping.getXmlPath().getParent(), amountModelPath.getParent()));
        // add to PriceQuantity
        priceQuantityBuilder.addPrice(fieldWithPriceScheduleBuilder);
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

    private void addMapping(Path xmlPath, Object xmlValue, Path modelPath, Object modelValue) {
        getMappings().add(new Mapping(xmlPath, xmlValue, modelPath, modelValue, null, true, true, false));
    }
}
