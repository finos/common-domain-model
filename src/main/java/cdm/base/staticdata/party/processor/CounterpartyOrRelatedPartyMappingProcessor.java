package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.RelatedPartyEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import cdm.legalagreement.contract.processor.PartyMappingHelper;

import java.util.List;
import java.util.Optional;

import static cdm.base.staticdata.party.CounterpartyOrRelatedParty.CounterpartyOrRelatedPartyBuilder;

/**
 * FpML mapping processor.
 */
public abstract class CounterpartyOrRelatedPartyMappingProcessor extends MappingProcessor {

	public CounterpartyOrRelatedPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		PartyMappingHelper.getInstance(getContext())
				.ifPresent(helper -> {
					CounterpartyOrRelatedPartyBuilder counterpartyOrRelatedPartyBuilder = (CounterpartyOrRelatedPartyBuilder) builder;
					helper.setCounterpartyOrRelatedParty(getModelPath(),
							synonymPath,
							counterpartyOrRelatedPartyBuilder::setCounterparty,
							counterpartyOrRelatedPartyBuilder::setRelatedParty,
							getRelatedPartyEnum().get());
				});
	}

	protected abstract Optional<RelatedPartyEnum> getRelatedPartyEnum();
}
