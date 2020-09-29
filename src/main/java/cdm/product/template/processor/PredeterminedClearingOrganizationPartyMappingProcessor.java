package cdm.product.template.processor;

import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.base.staticdata.party.processor.CounterpartyOrRelatedPartyMappingProcessor;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class PredeterminedClearingOrganizationPartyMappingProcessor extends CounterpartyOrRelatedPartyMappingProcessor {

	public PredeterminedClearingOrganizationPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context, RelatedPartyEnum.PREDETERMINED_CLEARING_ORGANIZATION_PARTY);
	}
}
