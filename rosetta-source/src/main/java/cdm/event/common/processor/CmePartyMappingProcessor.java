package cdm.event.common.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class CmePartyMappingProcessor extends cdm.legaldocumentation.contract.processor.CmePartyMappingProcessor {

	public CmePartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext context) {
		super(rosettaPath, synonymPaths, context);
	}
}
