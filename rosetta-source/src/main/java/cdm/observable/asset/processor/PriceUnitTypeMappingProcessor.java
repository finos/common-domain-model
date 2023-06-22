package cdm.observable.asset.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.observable.asset.Price.PriceScheduleBuilder;

/**
 * FpML mapper to enrich the mapped price with unit and perUnitOf.
 */
@SuppressWarnings("unused")
public class PriceUnitTypeMappingProcessor extends MappingProcessor {


	private final PriceUnitTypeHelper helper;

	public PriceUnitTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
		this.helper = new PriceUnitTypeHelper(modelPath, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		helper.mapUnitType(synonymPath, (PriceScheduleBuilder) parent);
	}
}
