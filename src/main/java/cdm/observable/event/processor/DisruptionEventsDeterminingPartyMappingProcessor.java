package cdm.observable.event.processor;

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
public class DisruptionEventsDeterminingPartyMappingProcessor extends CounterpartyOrRelatedPartyMappingProcessor {

	public DisruptionEventsDeterminingPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public Optional<RelatedPartyEnum> getRelatedPartyEnum() {
		return Optional.of(RelatedPartyEnum.DISRUPTION_EVENTS_DETERMINING_PARTY);
	}
}
