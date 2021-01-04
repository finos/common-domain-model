package cdm.observable.asset.processor;

import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappingSuccess;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappings;

/**
 * Mapper required due to mapping issues with multiple prices.
 * <p>
 * Fix synonym mapping logic and remove these mappers.
 */
@SuppressWarnings("unused")
public class PriceCollarMappingProcessor extends MappingProcessor {

	private final PriceHelper helper;

	public PriceCollarMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
		this.helper = new PriceHelper(context.getMappings());
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		Path startsWithPath = helper.startsWithPath("capFloorStream", synonymPath);
		Optional<Mapping> floorRateMapping = helper.getNonNullMapping(startsWithPath, "floorRateSchedule", "initialValue");
		Optional<Mapping> capRateMapping = helper.getNonNullMapping(startsWithPath, "capRateSchedule", "initialValue");
		if (floorRateMapping.isPresent() && capRateMapping.isPresent()) {
			UnitTypeBuilder unitType = helper.getUnitTypeNotionalCurrency(startsWithPath);
			((PriceQuantity.PriceQuantityBuilder) parent)
					.clearPrice()
					.addPriceBuilder(helper.getPrice(capRateMapping, unitType, unitType, PriceTypeEnum.CAP_RATE))
					.addPriceBuilder(helper.getPrice(floorRateMapping, unitType, unitType, PriceTypeEnum.FLOOR_RATE));
			updateMappingSuccess(capRateMapping.get(), getModelPath());
			updateMappingSuccess(floorRateMapping.get(), getModelPath());
		}
	}
}
