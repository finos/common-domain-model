package cdm.product.common.settlement.processor;

import cdm.observable.asset.CapFloorEnum;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceTypeEnum;
import cdm.product.common.settlement.PriceQuantity;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;
import java.util.List;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.base.math.UnitType.builder;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.product.common.settlement.processor.PriceQuantityHelper.toReferencablePriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

/**
 * FpML mapper required due to issues with multiple rates (e.g. capRate and floor) to the same PriceQuantity.price.
 * <p>
 * Both rates are mapped to the same price instance.  Add floorRate as a new Price instance (on the same PriceQuantity), and update mapping stats.
 */
@SuppressWarnings("unused")
public class PriceCollarMappingProcessor extends MappingProcessor {

	public PriceCollarMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		subPath("capFloorStream", synonymPath).ifPresent(subPath -> {
			// If both floorRate and capRate mappings exist, then we need to resolve the mapping issue..
			if (getNonNullMapping(getMappings(), getModelPath(), subPath, "capRateSchedule", "initialValue").isPresent()) {
				getNonNullMapping(getMappings(), getModelPath(), subPath, "floorRateSchedule", "initialValue").ifPresent(frm -> {
					UnitTypeBuilder unitType = toCurrencyUnitType(subPath);
					BigDecimal floorRate = new BigDecimal(String.valueOf(frm.getXmlValue()));
					PriceExpression.PriceExpressionBuilder priceExpression = PriceExpression.builder()
							.setPriceType(PriceTypeEnum.INTEREST_RATE)
							.setCapFloor(CapFloorEnum.FLOOR);
					((PriceQuantity.PriceQuantityBuilder) parent).addPrice(toReferencablePriceBuilder(floorRate, unitType, unitType, priceExpression));
					// update price index, e.g. floorRate and capRate were previously mapped to the same field so the price index
					// must be incremented otherwise any references will break
					frm.setRosettaPath(incrementPathElementIndex(frm.getRosettaPath(), "price", 1));
					// clear errors
					frm.setError(null);
					frm.setCondition(true);
				});
			}
		});
	}

	private UnitTypeBuilder toCurrencyUnitType(Path startsWithPath) {
		String currency = getNonNullMappedValue(getMappings(), startsWithPath, "notionalStepSchedule", "currency").orElse(null);
		String currencyScheme = getNonNullMappedValue(getMappings(), startsWithPath, "notionalStepSchedule", "currency", "currencyScheme").orElse(null);
		return builder()
				.setCurrency(FieldWithMetaString.builder()
						.setValue(currency)
						.setMeta(MetaFields.builder()
								.setScheme(currencyScheme)
								.build()));
	}
}
