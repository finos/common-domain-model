package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.legalagreement.contract.processor.PartyMappingHelper;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class PayerMappingProcessor extends PayerReceiverMappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PayerMappingProcessor.class);

	public PayerMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	void setCounterparty(Path synonymPath, PayerReceiverBuilder builder) {
		PartyMappingHelper.getInstance(getContext())
				.ifPresent(helper ->
						helper.setCounterpartyRoleEnum(getModelPath(), synonymPath, builder::setPayer));
	}

	@Override
	void setCashflowParty(Path synonymPath, PayerReceiverBuilder builder, AncillaryRoleEnum role) {
		PartyMappingHelper.getInstance(getContext())
				.ifPresent(helper ->
						helper.computeCashflowParty(getModelPath(),
								synonymPath,
								builder::setPayer,
								builder::setPayerAncillaryRole,
								role));
	}

	@Override
	void setPartyReference(Path synonymPath, PayerReceiverBuilder builder) {
		setValueAndUpdateMappings(synonymPath,
				partyExternalReference ->
						builder.setPayerPartyReference(ReferenceWithMetaParty.builder()
										.setExternalReference(partyExternalReference)
										.build()));
	}
}
