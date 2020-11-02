package cdm.legalagreement.contract.processor;

import cdm.product.template.TradableProduct;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static cdm.legalagreement.contract.processor.PartyMappingHelper.PARTY_MAPPING_HELPER_KEY;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class PartyMappingProcessor extends MappingProcessor {

	private final Function<String, Optional<String>> externalRefTranslator;

	public PartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext mappingContext) {
		this(rosettaPath, synonymPaths, mappingContext, null);
	}

	public PartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext mappingContext, Function<String, Optional<String>> externalRefTranslator) {
		super(rosettaPath, synonymPaths, mappingContext);
		this.externalRefTranslator = externalRefTranslator;
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		PartyMappingHelper partyMappigHelper = createHelper((TradableProduct.TradableProductBuilder) builder);
		partyMappigHelper.addCounterparties();
	}

	@NotNull
	private PartyMappingHelper createHelper(TradableProduct.TradableProductBuilder tradableProductBuilder) {
		return (PartyMappingHelper) getContext().getMappingParams()
				// Create new instance (and add to map) on each call
				.compute(PARTY_MAPPING_HELPER_KEY, (key, value) -> new PartyMappingHelper(getContext(), tradableProductBuilder, externalRefTranslator));
	}
}
