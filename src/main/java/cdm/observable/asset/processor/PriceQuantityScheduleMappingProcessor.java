package cdm.observable.asset.processor;

import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper required due to mapping issues with prices, floorRateSchedule and floatingRateMultiplierSchedule.
 *
 * Fix synonym mapping logic and remove these mappers.
 */
@SuppressWarnings("unused")
public class PriceQuantityScheduleMappingProcessor extends MappingProcessor {

	public PriceQuantityScheduleMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		setValueAndUpdateMappings(synonymPath,
				xmlValue -> {
					PriceQuantity.PriceQuantityBuilder priceQuantityBuilder = (PriceQuantity.PriceQuantityBuilder) parent;
					Price.PriceBuilder priceBuilder = Price.builder().setAmountBuilder(new BigDecimal(xmlValue));
					PriceTypeHelper.getPriceTypeEnum(synonymPath).ifPresent(priceBuilder::setPriceType);
					//priceQuantityBuilder.addPriceBuilder(FieldWithMetaPrice.builder().setValueBuilder(priceBuilder));
					priceQuantityBuilder.addPriceBuilder(priceBuilder);
				});
	}
}
