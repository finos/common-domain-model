package cdm.observable.asset.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * Mapping processors have to be in same package as the attribute.
 */
@SuppressWarnings("unused")
public class VegaNotionalAmountMappingProcessor extends cdm.base.math.processor.VegaNotionalAmountMappingProcessor {

    public VegaNotionalAmountMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }
}
