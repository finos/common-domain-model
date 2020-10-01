package cdm.observable.asset.processor;

import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.base.staticdata.party.processor.CounterpartyOrRelatedPartyMappingProcessor;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class CalculationAgentPartyMappingProcessor extends CounterpartyOrRelatedPartyMappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalculationAgentPartyMappingProcessor.class);

	private static final RosettaPath OPTIONAL_EARLY_TERMINATION_SUB_PATH = RosettaPath.valueOf("optionalEarlyTermination");
	private static final RosettaPath MANDATORY_EARLY_TERMINATION_SUB_PATH = RosettaPath.valueOf("mandatoryEarlyTermination");
	private static final RosettaPath FALLBACK_REFERENCE_PRICE_SUB_PATH = RosettaPath.valueOf("fallbackReferencePrice");
	private static final RosettaPath ECONOMIC_TERMS_ENDS_WITH = RosettaPath.valueOf("product.contractualProduct.economicTerms");

	public CalculationAgentPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	protected Optional<RelatedPartyEnum> getRelatedPartyEnum() {
		if (getModelPath().containsPath(OPTIONAL_EARLY_TERMINATION_SUB_PATH)) {
			return Optional.of(RelatedPartyEnum.OPTIONAL_EARLY_TERMINATION_CALCULATION_AGENT);
		} else if (getModelPath().containsPath(MANDATORY_EARLY_TERMINATION_SUB_PATH)) {
			return Optional.of(RelatedPartyEnum.MANDATORY_EARLY_TERMINATION_CALCULATION_AGENT);
		} else if (getModelPath().containsPath(FALLBACK_REFERENCE_PRICE_SUB_PATH)) {
			return Optional.of(RelatedPartyEnum.FALLBACK_CALCULATION_AGENT);
		} else if (getModelPath().getParent().getParent().endsWith(ECONOMIC_TERMS_ENDS_WITH)) {
			return Optional.of(RelatedPartyEnum.INDEPENDENT_CALCULATION_AGENT);
		} else {
			return Optional.empty();
		}
	}
}
