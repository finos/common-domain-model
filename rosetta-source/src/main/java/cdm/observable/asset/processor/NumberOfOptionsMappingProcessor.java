package cdm.observable.asset.processor;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.observable.asset.PriceQuantity;
import com.regnosys.rosetta.common.translation.*;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;

import static cdm.observable.asset.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.observable.asset.processor.PriceQuantityHelper.toReferencableQuantityBuilder;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;

/**
 * FpML mapper:
 * - maps numberOfOptions to Quantity.amount
 * - sets Quantity.unitOfAmount to FinancialUnitEnum.Contract
 * - optionEntitlement to Quantity.multiplier
 * - sets/maps the appropriate Quantity.multiplierUnit depending on underlying product
 */
@SuppressWarnings("unused")
public class NumberOfOptionsMappingProcessor extends MappingProcessor {

    private static final Path EQUITY_UNDERLIER_PATH = Path.parse("underlyer.singleUnderlyer.equity.instrumentId");
    private static final Path INDEX_UNDERLIER_PATH = Path.parse("underlyer.singleUnderlyer.index.instrumentId");

    public NumberOfOptionsMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder quantity = NonNegativeQuantitySchedule.builder();
        setAmountAndUnit(synonymPath, quantity, builder.size());
        setMultiplierAndUnit(synonymPath, quantity);

        if (quantity.hasData()) {
            ((PriceQuantity.PriceQuantityBuilder) parent).addQuantity(toReferencableQuantityBuilder(quantity));
        }
    }

    private void setAmountAndUnit(Path synonymPath, NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder quantityBuilder, int index) {
        Path baseModelPath = toPath(getModelPath());
        Path mappedModelPath = incrementPathElementIndex(baseModelPath, "quantity", index);

        

        MappingProcessorUtils.getNonNullMappedValue(synonymPath, getMappings()).ifPresent(xmlValue -> {
            quantityBuilder
                    .setValue(new BigDecimal(xmlValue))
                    .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.CONTRACT));
            // add new mapping rather than updating, otherwise the referencing breaks
            addMapping(synonymPath, xmlValue, mappedModelPath, xmlValue);
            // clean up errors
            updateReferenceMapping(synonymPath);
        });
    }

    private void setMultiplierAndUnit(Path synonymPath, NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder quantity) {
        Path parentSynonymPath = synonymPath.getParent();

        setValueAndUpdateMappings(parentSynonymPath.addElement("optionEntitlement"),
                (xmlValue) -> quantity.getOrCreateMultiplier().setValue(new BigDecimal(xmlValue)));

        // bond option multiplier unit
        setValueAndUpdateMappings(parentSynonymPath.addElement("entitlementCurrency"),
                (xmlValue) -> quantity.getOrCreateMultiplier().setUnit(UnitType.builder().setCurrencyValue(xmlValue)));
        setValueAndUpdateMappings(parentSynonymPath.addElement("entitlementCurrency").addElement("currencyScheme"),
                (xmlValue) -> quantity.getOrCreateMultiplier().getOrCreateUnit().getOrCreateCurrency().getOrCreateMeta().setScheme(xmlValue));
        // equity multiplier unit
        if (pathExists(EQUITY_UNDERLIER_PATH)) {
            quantity.getOrCreateMultiplier().setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE));
        }
        // index multiplier unit
        if (pathExists(INDEX_UNDERLIER_PATH)) {
            quantity.getOrCreateMultiplier().setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.INDEX_UNIT));
        }
    }

    private boolean pathExists(Path endsWith) {
        return getMappings().stream()
                .filter(m -> m.getXmlPath().endsWith(endsWith))
                .anyMatch(m -> m.getXmlValue() != null);
    }

    private void addMapping(Path xmlPath, Object xmlValue, Path modelPath, Object modelValue) {
        getMappings().add(new Mapping(xmlPath, xmlValue, modelPath, modelValue, null, true, true, false));
    }

    private void updateReferenceMapping(Path synonymPath) {
        getMappings().stream()
                .filter(m -> m.getXmlPath().equals(synonymPath))
                .filter(m -> m.getRosettaValue() instanceof Reference.ReferenceBuilder)
                .filter(m -> m.getError() != null)
                .forEach(m -> {
                    m.setDuplicate(false);
                    m.setError(null);
                });
    }
}
