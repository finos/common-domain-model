package cdm.observable.asset.processor;

import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.observable.asset.CalculationAgent;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class CalculationAgentPartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalculationAgentPartyMappingProcessor.class);

	private static final RosettaPath OPTIONAL_EARLY_TERMINATION_SUB_PATH = RosettaPath.valueOf("optionalEarlyTermination");
	private static final RosettaPath MANDATORY_EARLY_TERMINATION_SUB_PATH = RosettaPath.valueOf("mandatoryEarlyTermination");
	private static final RosettaPath FALLBACK_REFERENCE_PRICE_SUB_PATH = RosettaPath.valueOf("fallbackReferencePrice");
	private static final RosettaPath ECONOMIC_TERMS_ENDS_WITH = RosettaPath.valueOf("economicTerms");

	public CalculationAgentPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		getAncillaryRoleEnum().ifPresent(role ->
				PartyMappingHelper.getInstanceOrThrow(getContext())
						.setAncillaryRoleEnum(getModelPath(),
								synonymPath,
								((CalculationAgent.CalculationAgentBuilder) parent)::setCalculationAgentParty, 
								role));
	}

	protected Optional<AncillaryRoleEnum> getAncillaryRoleEnum() {
		if (getModelPath().containsPath(OPTIONAL_EARLY_TERMINATION_SUB_PATH)) {
			return Optional.of(AncillaryRoleEnum.CALCULATION_AGENT_OPTIONAL_EARLY_TERMINATION);
		} else if (getModelPath().containsPath(MANDATORY_EARLY_TERMINATION_SUB_PATH)) {
			return Optional.of(AncillaryRoleEnum.CALCULATION_AGENT_MANDATORY_EARLY_TERMINATION);
		} else if (getModelPath().containsPath(FALLBACK_REFERENCE_PRICE_SUB_PATH)) {
			return Optional.of(AncillaryRoleEnum.CALCULATION_AGENT_FALLBACK);
		} else if (getModelPath().getParent().getParent().endsWith(ECONOMIC_TERMS_ENDS_WITH)) {
			return Optional.of(AncillaryRoleEnum.CALCULATION_AGENT_INDEPENDENT);
		} else {
			return Optional.empty();
		}
	}
}
