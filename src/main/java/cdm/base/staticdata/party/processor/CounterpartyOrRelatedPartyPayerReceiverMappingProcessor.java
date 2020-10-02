package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.CounterpartyOrRelatedParty;
import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.base.staticdata.party.processor.CounterpartyOrRelatedPartyMappingProcessor;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.PartyMappingHelper;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class CounterpartyOrRelatedPartyPayerReceiverMappingProcessor extends CounterpartyOrRelatedPartyMappingProcessor {

	public CounterpartyOrRelatedPartyPayerReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		super.map(synonymPath, builder, parent);
	}

	@Override
	public Optional<RelatedPartyEnum> getRelatedPartyEnum() {
		return Optional.of(RelatedPartyEnum.CASHFLOW_PAYMENT_PARTY);
	}
}
