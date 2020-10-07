package cdm.base.staticdata.party.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import static cdm.legalagreement.contract.processor.PartyMappingHelper.PRODUCT_SUB_PATH;

/**
 * FpML mapping processor.
 */
public abstract class PayerReceiverMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverMappingProcessor.class);

	PayerReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		if (!getModelPath().containsPath(PRODUCT_SUB_PATH)) {
			LOGGER.warn("PayerReceiver used outside of the Product definition {}", getModelPath().buildPath());
			setPartyReference(synonymPath, (PayerReceiverBuilder) parent);
		} else if (getModelPath().toIndexless().containsPath(RosettaPath.valueOf("economicTerms.payout.cashflow.payerReceiver"))) {
			setCashflowCounterpartyOrRelatedParty(synonymPath, (PayerReceiverBuilder) parent);
		} else {
			setCounterparty(synonymPath, (PayerReceiverBuilder) parent);
		}
	}

	abstract void setCounterparty(Path synonymPath, PayerReceiverBuilder builder);

	abstract void setCashflowCounterpartyOrRelatedParty(Path synonymPath, PayerReceiverBuilder builder);

	abstract void setPartyReference(Path synonymPath, PayerReceiverBuilder builder);
}
