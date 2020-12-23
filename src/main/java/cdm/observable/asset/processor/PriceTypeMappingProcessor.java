package cdm.observable.asset.processor;

import cdm.observable.asset.Price;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

public class PriceTypeMappingProcessor extends MappingProcessor {

	public PriceTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		PriceTypeHelper.getPriceTypeEnum(synonymPath)
				.ifPresent(((Price.PriceBuilder) parent)::setPriceType);

	}
}
