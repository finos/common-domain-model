package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public abstract class BuyerSellerMappingProcessor extends MappingProcessor {

	public BuyerSellerMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
		PartyMappingHelper.getInstance(getContext())
				.ifPresent(helper -> getSetter(parent)
						.ifPresent(setter -> helper.setCounterpartyRoleEnum(getModelPath(), synonymPath, setter)));
	}

	protected abstract Optional<Consumer<CounterpartyRoleEnum>> getSetter(RosettaModelObjectBuilder builder);
}
