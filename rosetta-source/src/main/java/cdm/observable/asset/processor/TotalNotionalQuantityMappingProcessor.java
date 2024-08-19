package cdm.observable.asset.processor;

import cdm.base.math.CapacityUnitEnum;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.MappingProcessorUtils;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static cdm.observable.asset.processor.PriceQuantityHelper.incrementPathElementIndex;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class TotalNotionalQuantityMappingProcessor extends MappingProcessor {

    public TotalNotionalQuantityMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        List<FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder> quantityBuilders =
                (List<FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder>) builders;

        getTotalNotionalQuantity(synonymPath, quantityBuilders.size())
                .map(PriceQuantityHelper::toReferencableQuantityBuilder)
                .ifPresent(quantityBuilders::add);
    }

    private Optional<NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder> getTotalNotionalQuantity(Path synonymPath, int index) {
        NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder quantity = NonNegativeQuantitySchedule.builder();

        Path baseModelPath = toPath(getModelPath()).addElement("amount");
        Path mappedModelPath = incrementPathElementIndex(baseModelPath, "quantity", index);

        MappingProcessorUtils.setValueAndUpdateMappings(synonymPath,
                (xmlValue) -> quantity
                        .setValue(new BigDecimal(xmlValue))
                        .setUnit(UnitType.builder().setCapacityUnit(findCapacityUnitEnum(synonymPath.getParent()))),
                getMappings(),
                PathUtils.toRosettaPath(mappedModelPath));

        return quantity.hasData() ? Optional.of(quantity) : Optional.empty();
    }

    private CapacityUnitEnum findCapacityUnitEnum(Path legSynonymPath) {
        // check both notionalQuantity and notionalQuantitySchedule paths
        return getCapacityUnitEnum(legSynonymPath.addElement("notionalQuantity").addElement("quantityUnit"))
                .orElse(getCapacityUnitEnum(legSynonymPath.addElement("notionalQuantitySchedule").addElement("notionalStep").addElement("quantityUnit"))
                        .orElse(null));
    }

    private Optional<CapacityUnitEnum> getCapacityUnitEnum(Path quantityUnitPath) {
        return getNonNullMappedValue(quantityUnitPath, getMappings())
                .flatMap(xmlValue -> getSynonymToEnumMap().getEnumValueOptional(CapacityUnitEnum.class, xmlValue));
    }
}
