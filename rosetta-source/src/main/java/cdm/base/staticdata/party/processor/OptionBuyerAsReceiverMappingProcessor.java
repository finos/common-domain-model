package cdm.base.staticdata.party.processor;

import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
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

/**
 * FpML mapping processor.
 * <p>
 * Sets option buyer as the FX call receiver.
 */
@SuppressWarnings("unused")
public class OptionBuyerAsReceiverMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(OptionBuyerAsReceiverMappingProcessor.class);

	public OptionBuyerAsReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		PartyMappingHelper.getInstanceOrThrow(getContext())
				.setCounterpartyRoleEnum(getModelPath(),
						synonymPath.getParent().addElement("buyerPartyReference"),
						((PayerReceiverBuilder) parent)::setReceiver);
	}
}