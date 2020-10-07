package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.RelatedPartyEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import cdm.legalagreement.contract.processor.PartyMappingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class ReceiverMappingProcessor extends PayerReceiverMappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverMappingProcessor.class);

	public ReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	void setCounterparty(Path synonymPath, PayerReceiverBuilder builder) {
		PartyMappingHelper.getInstance(getContext())
				.ifPresent(helper -> helper.setCounterpartyEnum(getModelPath(), synonymPath, builder::setReceiver));
	}

	@Override
	void setCashflowCounterpartyOrRelatedParty(Path synonymPath, PayerReceiverBuilder builder) {
		PartyMappingHelper.getInstance(getContext())
				.ifPresent(helper -> helper.computeCounterpartyOrRelatedParty(getModelPath(),
						synonymPath,
						builder::setReceiver,
						builder::setReceiverRelatedParty,
						RelatedPartyEnum.CASHFLOW_PAYMENT_PARTY));
	}

	@Override
	void setPartyReference(Path synonymPath, PayerReceiverBuilder builder) {
		setValueAndUpdateMappings(synonymPath,
				partyExternalReference ->
						builder.setReceiverPartyReference(ReferenceWithMetaParty.builder()
								.setExternalReference(partyExternalReference)
								.build()));
	}
}
