package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.CounterpartyOrRelatedParty;
import cdm.base.staticdata.party.RelatedPartyEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
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
public abstract class CounterpartyOrRelatedPartyMappingProcessor extends MappingProcessor {

	public CounterpartyOrRelatedPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		setValueAndOptionallyUpdateMappings(synonymPath,
				partyExternalReference ->
				{
					Optional<RelatedPartyEnum> relatedPartyEnum = getRelatedPartyEnum();
					relatedPartyEnum.ifPresent(p -> PartyMappingHelper.getInstance(getContext())
							.orElseThrow(() -> new IllegalStateException("PartyMappingHelper not found."))
							.setCounterpartyOrRelatedParty(
									(CounterpartyOrRelatedParty.CounterpartyOrRelatedPartyBuilder) builder,
									partyExternalReference,
									p));
					return relatedPartyEnum.isPresent();
				},
				getMappings(),
				getModelPath());
	}

	protected abstract Optional<RelatedPartyEnum> getRelatedPartyEnum();
}
