package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
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
import static cdm.observable.asset.processor.PriceHelper.toPriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

/**
 * Mapper required due to mapping issues with multiple prices.
 * <p>
 * Fix synonym mapping logic and remove these mappers.
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
					((PriceQuantity.PriceQuantityBuilder) parent)
							.addPriceBuilder(toPriceBuilder(floorRate, unitType, unitType, PriceTypeEnum.FLOOR_RATE));
					// update price index, e.g. floorRate and capRate were previously mapped to the same field so the price index
					// must be incremented otherwise any references will break
					frm.setRosettaPath(PriceHelper.incrementPricePathElementIndex(frm.getRosettaPath(), 1));
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
		return UnitType.builder()
				.setCurrencyBuilder(FieldWithMetaString.builder()
						.setValue(currency)
						.setMeta(MetaFields.builder()
								.setScheme(currencyScheme)
								.build()));
	}
}
