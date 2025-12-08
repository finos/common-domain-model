package cdm.legaldocumentation.contract.processor;

import cdm.event.common.Trade;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static cdm.product.template.TradableProduct.TradableProductBuilder;

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
		Optional.ofNullable((Trade.TradeBuilder) builder)
				.map(this::createHelper)
				.ifPresent(PartyMappingHelper::addCounterparties);
	}

	private PartyMappingHelper createHelper(TradableProductBuilder builder) {
		return (PartyMappingHelper) getContext().getMappingParams()
				// Create new instance (and add to map) on each call
				.compute(PartyMappingHelper.PARTY_MAPPING_HELPER_KEY, (key, value) -> new PartyMappingHelper(getContext(), builder, externalRefTranslator));
	}
}
