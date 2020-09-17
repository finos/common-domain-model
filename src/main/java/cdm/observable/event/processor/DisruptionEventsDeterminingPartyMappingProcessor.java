package cdm.observable.event.processor;

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
public class DisruptionEventsDeterminingPartyMappingProcessor extends CounterpartyOrRelatedPartyMappingProcessor {

	public DisruptionEventsDeterminingPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context, RelatedPartyEnum.DISRUPTION_EVENTS_DETERMINING_PARTY);
	}
}
