package cdm.legaldocumentation.contract.processor;

import cdm.base.staticdata.party.processor.TradeSideToPartyMappingHelper;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class CmePartyMappingProcessor extends PartyMappingProcessor {

	public CmePartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext context) {
		super(rosettaPath, synonymPaths, context, new TradeSideToPartyMappingHelper(context.getMappings()));
	}
}
