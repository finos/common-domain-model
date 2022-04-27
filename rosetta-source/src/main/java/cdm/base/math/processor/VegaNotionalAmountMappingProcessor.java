package cdm.base.math.processor;

import cdm.base.math.Quantity;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

@SuppressWarnings("unused")
public class VegaNotionalAmountMappingProcessor extends MappingProcessor {

    public VegaNotionalAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        Consumer<String> setter = ((Quantity.QuantityBuilder) parent).getOrCreateUnitOfAmount().getOrCreateCurrency()::setValue;

        subPath("volatilityLeg", synonymPath)
                .flatMap(subPath -> getNonNullMappedValue(getMappings(), subPath, "settlementCurrency"))
                .ifPresent(setter);

        subPath("variance", synonymPath)
                .flatMap(subPath -> getNonNullMappedValue(getMappings(), subPath, "varianceAmount", "currency"))
                .ifPresent(setter);
    }
}
