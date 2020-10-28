package cdm.product.asset.processor;

import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.base.staticdata.party.processor.CounterpartyOrRelatedPartyMappingProcessor;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class ExtraordinaryDividendsPartyMappingProcessor extends CounterpartyOrRelatedPartyMappingProcessor {

	public ExtraordinaryDividendsPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	protected Optional<RelatedPartyEnum> getRelatedPartyEnum() {
		return Optional.of(RelatedPartyEnum.EXTRAORDINARY_DIVIDENDS_PARTY);
	}
}
