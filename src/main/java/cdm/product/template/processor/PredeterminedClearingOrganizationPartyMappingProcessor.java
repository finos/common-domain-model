package cdm.product.template.processor;

import cdm.base.staticdata.party.RelatedPartyRoleEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.processor.PartyMappingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static cdm.base.staticdata.party.CounterpartyOrRelatedParty.CounterpartyOrRelatedPartyBuilder;

@SuppressWarnings("unused")
public class PredeterminedClearingOrganizationPartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PredeterminedClearingOrganizationPartyMappingProcessor.class);

	private final ExecutorService executor;

	public PredeterminedClearingOrganizationPartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
		this.executor = context.getExecutor();
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		setValueAndUpdateMappings(synonymPath,
				partyExternalReference ->
						PartyMappingHelper.getInstance(getContext())
								.orElseThrow(() -> new IllegalStateException("PartyMappingHelper not found."))
								.setCounterpartyOrRelatedParty(
										(CounterpartyOrRelatedPartyBuilder) builder,
										partyExternalReference,
										RelatedPartyRoleEnum.PREDETERMINED_CLEARING_ORGANIZATION_PARTY));
	}
}
