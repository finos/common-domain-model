package cdm.base.staticdata.party.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class CashPaymentBuyerMappingProcessor extends BuyerMappingProcessor {

	public CashPaymentBuyerMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		boolean isPaymentAmount = getNonNullMappedValue(synonymPath.getParent().addElement("paymentAmount")
				.addElement("amount"), getMappings()).isPresent();
		boolean isFixedAmount = getNonNullMappedValue(synonymPath.getParent().addElement("fixedAmount")
				.addElement("amount"), getMappings()).isPresent();
		if (isPaymentAmount || isFixedAmount) {
			super.mapBasic(synonymPath, instance, parent);
		}
	}
}
