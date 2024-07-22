package cdm.observable.asset.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * Empty extension of cdm.base.staticdata.asset.common.processor.BasketConstituentMappingProcessor because the mapper must be in the same package as the type it's mapped to.
 */
@SuppressWarnings("unused") // used in generated code
public class BasketConstituentMappingProcessor extends cdm.base.staticdata.asset.common.processor.BasketConstituentMappingProcessor {
    
    public BasketConstituentMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }
}
