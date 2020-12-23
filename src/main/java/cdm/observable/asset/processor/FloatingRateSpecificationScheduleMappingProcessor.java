package cdm.observable.asset.processor;

import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import cdm.product.asset.FloatingRateSpecification;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;

import static cdm.product.asset.FloatingRateSpecification.*;

/**
 * Mapper required due to mapping issues with prices, floorRateSchedule and floatingRateMultiplierSchedule.
 *
 * Fix synonym mapping logic and remove these mappers.
 */
@SuppressWarnings("unused")
public class FloatingRateSpecificationScheduleMappingProcessor extends MappingProcessor {

	public FloatingRateSpecificationScheduleMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		setValueAndUpdateMappings(synonymPath,
				xmlValue -> {
					FloatingRateSpecificationBuilder priceQuantityBuilder = (FloatingRateSpecificationBuilder) parent;
					Price.PriceBuilder priceBuilder = Price.builder().setAmountBuilder(new BigDecimal(xmlValue));
					PriceTypeHelper.getPriceTypeEnum(synonymPath).ifPresent(priceBuilder::setPriceType);
					priceQuantityBuilder.addResolvedPriceBuilder(priceBuilder);
				});
	}
}
