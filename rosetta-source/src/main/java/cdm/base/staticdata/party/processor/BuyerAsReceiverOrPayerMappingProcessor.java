package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import static cdm.base.staticdata.party.processor.BuyerSellerPartyHelper.isBuyerAsReceiver;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class BuyerAsReceiverOrPayerMappingProcessor extends PayerReceiverMappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BuyerAsReceiverOrPayerMappingProcessor.class);

	public BuyerAsReceiverOrPayerMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	void setCounterparty(Path synonymPath, PayerReceiverBuilder builder, PartyMappingHelper helper) {
		Consumer<CounterpartyRoleEnum> setter =
				isBuyerAsReceiver(synonymPath, getModelPath()) ? builder::setReceiver : builder::setPayer;
		helper.setCounterpartyRoleEnum(getModelPath(), synonymPath, setter);
	}
}
