package cdm.base.staticdata.party.processor;

import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;

/**
 * FpML mapping processor.
 */
public abstract class PayerReceiverMappingProcessor extends MappingProcessor {

	PayerReceiverMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		PartyMappingHelper.getInstance(getContext()).ifPresent(helper ->
				setCounterparty(synonymPath, (PayerReceiverBuilder) parent, helper));
	}

	abstract void setCounterparty(Path synonymPath, PayerReceiverBuilder builder, PartyMappingHelper helper);
}