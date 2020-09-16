package org.isda.cdm.processor;

import static org.isda.cdm.processor.PartyMappingHelper.PARTY_MAPPING_HELPER_KEY;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import cdm.product.template.TradableProduct;

/**
 * FpML mapping processor.
 */
@SuppressWarnings("unused")
public class PartyMappingProcessor extends MappingProcessor {

	public PartyMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(rosettaPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		PartyMappingHelper partyMappigHelper = createHelper();
		partyMappigHelper.supplyTradableProductBuilder((TradableProduct.TradableProductBuilder) builder);
		partyMappigHelper.addCounterparties();
	}

	@NotNull
	private PartyMappingHelper createHelper() {
		return (PartyMappingHelper) getContext().getMappingParams()
				// Create new instance (and add to map) on each call
				.compute(PARTY_MAPPING_HELPER_KEY, (key, value) -> new PartyMappingHelper(getContext()));
	}
}
