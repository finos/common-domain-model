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

/**
 * FpML mapping processor.
 */
public abstract class CounterpartyOrRelatedPartyMappingProcessor extends MappingProcessor {

	private final RelatedPartyEnum relatedPartyEnum;

	public CounterpartyOrRelatedPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context, RelatedPartyEnum relatedPartyEnum) {
		super(modelPath, synonymPaths, context);
		this.relatedPartyEnum = relatedPartyEnum;
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		setValueAndUpdateMappings(synonymPath,
				partyExternalReference ->
						PartyMappingHelper.getInstance(getContext())
								.orElseThrow(() -> new IllegalStateException("PartyMappingHelper not found."))
								.setCounterpartyOrRelatedParty(
										(CounterpartyOrRelatedParty.CounterpartyOrRelatedPartyBuilder) builder,
										partyExternalReference,
										relatedPartyEnum));
	}
}
