package cdm.product.template.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * FRAIRP mapping processor.
 *
 * Mapper must be in same namespace as the type it is used for.
 */
@SuppressWarnings("unused")
public class FRAIRPSplitterMappingProcessor extends org.isda.cdm.processor.FRAIRPSplitterMappingProcessor {

	public FRAIRPSplitterMappingProcessor(RosettaPath path, List<Path> synonymPaths, MappingContext context) {
		super(path, synonymPaths, context);
	}

}
