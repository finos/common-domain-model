package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static cdm.observable.asset.metafields.FieldWithMetaPrice.*;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

@SuppressWarnings("unused")
public class ExchangeRateMappingProcessor extends MappingProcessor {

	public ExchangeRateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		PriceQuantity.PriceQuantityBuilder priceQuantityBuilder = (PriceQuantity.PriceQuantityBuilder) parent;
		List<FieldWithMetaPriceBuilder> priceBuilders = emptyIfNull((List<FieldWithMetaPriceBuilder>) builders);
		UnitType.UnitTypeBuilder unitOfAmount = getUnitOfAmount(priceBuilders);
		UnitType.UnitTypeBuilder perUnitOfAmount = getPerUnitOfAmount(priceBuilders);

		setValueAndUpdateMappings(synonymPath.addElement("spotRate"),
				(xmlValue) ->
					priceQuantityBuilder.addPriceBuilder(builder()
							.setValueBuilder(Price.builder()
									.setAmount(new BigDecimal(xmlValue))
									.setPriceType(PriceTypeEnum.SPOT)
									.setUnitOfAmountBuilder(unitOfAmount)
									.setPerUnitOfAmountBuilder(perUnitOfAmount))));

		setValueAndUpdateMappings(synonymPath.addElement("forwardPoints"),
				(xmlValue) ->
						priceQuantityBuilder.addPriceBuilder(builder()
								.setValueBuilder(Price.builder()
										.setAmount(new BigDecimal(xmlValue))
										.setPriceType(PriceTypeEnum.FORWARD_POINTS)
										.setUnitOfAmountBuilder(unitOfAmount)
										.setPerUnitOfAmountBuilder(perUnitOfAmount))));
	}

	private UnitType.UnitTypeBuilder getUnitOfAmount(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return priceBuilders.stream()
				.map(FieldWithMetaPriceBuilder::getValue)
				.filter(p -> p.getPriceType() == PriceTypeEnum.SPOT)
				.findFirst()
				.map(Price.PriceBuilder::getUnitOfAmount)
				.orElse(null);
	}

	private UnitType.UnitTypeBuilder getPerUnitOfAmount(List<FieldWithMetaPriceBuilder> priceBuilders) {
		return priceBuilders.stream()
				.map(FieldWithMetaPriceBuilder::getValue)
				.filter(p -> p.getPriceType() == PriceTypeEnum.SPOT)
				.findFirst()
				.map(Price.PriceBuilder::getPerUnitOfAmount)
				.orElse(null);
	}
}
