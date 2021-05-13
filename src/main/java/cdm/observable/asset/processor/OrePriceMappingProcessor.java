package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;
import java.util.Objects;

import static cdm.observable.asset.metafields.FieldWithMetaPrice.FieldWithMetaPriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * ORE mapper to enrich the mapped price with unitOfAmount and perUnitOfAmount.
 */
@SuppressWarnings("unused")
public class OrePriceMappingProcessor extends MappingProcessor {

	public OrePriceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		subPath("LegData", synonymPath)
				.map(subPath -> subPath.addElement("Currency"))
				.flatMap(this::getValueAndUpdateMappings)
				.map(this::toUnitType)
				.ifPresent(unitType -> emptyIfNull((List<FieldWithMetaPriceBuilder>) builders).stream()
						.map(FieldWithMetaPriceBuilder::getValue)
						.filter(Objects::nonNull)
						// if mapped price has priceType interestRate or Spread then update the unitOfAmount and perUnitOfAmount with currency.
						.filter(p -> p.getPriceType() == PriceTypeEnum.INTEREST_RATE || p.getPriceType() == PriceTypeEnum.SPREAD)
						.forEach(priceBuilder -> priceBuilder
								.setUnitOfAmount(unitType)
								.setPerUnitOfAmount(unitType)));
	}

	private UnitType.UnitTypeBuilder toUnitType(String currency) {
		return UnitType.builder()
				.setCurrency(FieldWithMetaString.builder().setValue(currency));
	}
}
