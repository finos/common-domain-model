package cdm.observable.event.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * FpML mapping processor.
 *
 * Mapper must be in same namespace as the type it is used for.
 */
@SuppressWarnings("unused")
public class BuyerMappingProcessor extends cdm.base.staticdata.party.processor.BuyerMappingProcessor {

	public BuyerMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}
}
