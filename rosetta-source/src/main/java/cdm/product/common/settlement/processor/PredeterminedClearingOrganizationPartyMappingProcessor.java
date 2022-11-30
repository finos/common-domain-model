package cdm.product.common.settlement.processor;

import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.product.common.settlement.PhysicalSettlementTerms;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class PredeterminedClearingOrganizationPartyMappingProcessor extends MappingProcessor {

	public PredeterminedClearingOrganizationPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		PartyMappingHelper.getInstanceOrThrow(getContext())
				.setAncillaryRoleEnum(getModelPath(),
						synonymPath,
						((PhysicalSettlementTerms.PhysicalSettlementTermsBuilder) parent)::setPredeterminedClearingOrganizationParty,
						AncillaryRoleEnum.PREDETERMINED_CLEARING_ORGANIZATION_PARTY);
	}
}
