package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
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

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappingSuccess;

/**
 * Mapper required due to mapping issues with multiple prices.
 * <p>
 * Fix synonym mapping logic and remove these mappers.
 */
@SuppressWarnings("unused")
public class PriceMultiplierMappingProcessor extends MappingProcessor {

	private final PriceHelper helper;

	public PriceMultiplierMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
		this.helper = new PriceHelper(context.getMappings());
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		Path startsWithPath = helper.startsWithPath("floatingRateCalculation", synonymPath);
		Optional<Mapping> multiplierMapping = helper.getNonNullMapping(startsWithPath, "floatingRateMultiplierSchedule", "initialValue");
		Optional<Mapping> spreadMapping = helper.getNonNullMapping(startsWithPath, "spreadSchedule", "initialValue");
		if (multiplierMapping.isPresent() && spreadMapping.isPresent()) {
			UnitType.UnitTypeBuilder unitType = helper.getUnitTypeNotionalCurrency(helper.startsWithPath("swapStream", synonymPath));
			((PriceQuantity.PriceQuantityBuilder) parent)
					.clearPrice()
					.addPriceBuilder(helper.getPrice(spreadMapping, unitType, unitType, PriceTypeEnum.SPREAD))
					.addPriceBuilder(helper.getPrice(multiplierMapping, null, null, PriceTypeEnum.MULTIPLIER_OF_INDEX_VALUE));
			updateMappingSuccess(spreadMapping.get(), getModelPath());
			updateMappingSuccess(multiplierMapping.get(), getModelPath());
		}
	}
}
