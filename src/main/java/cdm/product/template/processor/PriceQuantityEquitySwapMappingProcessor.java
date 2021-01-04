package cdm.product.template.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PriceQuantityEquitySwapMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceQuantityEquitySwapMappingProcessor.class);

	public PriceQuantityEquitySwapMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		LOGGER.info("EquitySwap path {} builders {}", synonymPath, builders.size());
	}
}
