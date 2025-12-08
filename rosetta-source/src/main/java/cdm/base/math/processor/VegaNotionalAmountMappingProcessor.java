package cdm.base.math.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static cdm.base.math.NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

@SuppressWarnings("unused")
public class VegaNotionalAmountMappingProcessor extends MappingProcessor {

    private static final String FX_VARIANCE_SWAP_PATH = "fxVarianceSwap";

    public VegaNotionalAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        NonNegativeQuantityScheduleBuilder quantityBuilder = (NonNegativeQuantityScheduleBuilder) parent;

        if (isFxVarianceSwapPath(synonymPath) && isPriceQuantityModelPath()) {
            // Update builder to be empty
            quantityBuilder.setValue(null);
            // Remove mapping
            getNonNullMapping(filterMappings(getMappings(), getModelPath()), synonymPath)
                    .ifPresent(getMappings()::remove);
            return;
        }

        // Set units
        Consumer<String> setter = quantityBuilder.getOrCreateUnit().getOrCreateCurrency()::setValue;

        subPath("volatilityLeg", synonymPath)
                .flatMap(subPath -> getNonNullMappedValue(getMappings(), subPath, "settlementCurrency"))
                .ifPresent(setter);

        subPath("variance", synonymPath)
                .flatMap(subPath -> getNonNullMappedValue(getMappings(), subPath, "varianceAmount", "currency"))
                .ifPresent(setter);

        subPath("fxVolatilitySwap", synonymPath)
                .flatMap(subPath -> getNonNullMappedValue(getMappings(), subPath, "vegaNotional", "currency"))
                .ifPresent(setter);
    }

    private boolean isPriceQuantityModelPath() {
        return PathUtils.toPath(getModelPath()).endsWith("priceQuantity", "quantity", "value");
    }

    private boolean isFxVarianceSwapPath(Path synonymPath) {
        return synonymPath.getElements().stream()
                .map(Path.PathElement::getPathName)
                .anyMatch(VegaNotionalAmountMappingProcessor.FX_VARIANCE_SWAP_PATH::equals);
    }
}
