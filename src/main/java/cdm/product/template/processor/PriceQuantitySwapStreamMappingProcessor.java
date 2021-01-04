package cdm.product.template.processor;

import cdm.base.math.MeasureBase;
import cdm.base.math.UnitType;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.rosetta.model.metafields.FieldWithMetaString.FieldWithMetaStringBuilder;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class PriceQuantitySwapStreamMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceQuantitySwapStreamMappingProcessor.class);

	public PriceQuantitySwapStreamMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		LOGGER.info("SwapStream path {} builders {}", synonymPath, builders.size());
		List<PriceQuantity.PriceQuantityBuilder> pqBuilders = (List<PriceQuantity.PriceQuantityBuilder>) builders;
		List<FieldWithMetaStringBuilder> notionalCurrencies = emptyIfNull(pqBuilders)
				.stream()
				.map(PriceQuantity.PriceQuantityBuilder::getQuantity)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(MeasureBase.MeasureBaseBuilder::getUnitOfAmount)
				.map(UnitType.UnitTypeBuilder::getCurrency)
				.distinct()
				.collect(Collectors.toList());
		if (notionalCurrencies.size() == 1) {
			FieldWithMetaStringBuilder notionalCurrency = notionalCurrencies.get(0);
			pqBuilders.stream()
					.map(PriceQuantity.PriceQuantityBuilder::getPrice)
					.filter(Objects::nonNull)
					.flatMap(Collection::stream)
					.filter(RosettaModelObjectBuilder::hasData)
					.forEach(priceBuilder -> {
						PriceTypeEnum priceType = priceBuilder.getPriceType();
						if (priceType == PriceTypeEnum.INTEREST_RATE
								|| priceType == PriceTypeEnum.CAP_RATE
								|| priceType == PriceTypeEnum.FLOOR_RATE
								|| priceType == PriceTypeEnum.SPREAD) {
							priceBuilder.getOrCreateUnitOfAmount().setCurrencyBuilder(notionalCurrency);
							priceBuilder.getOrCreatePerUnitOfAmount().setCurrencyBuilder(notionalCurrency);
						}
					});
		} else if (notionalCurrencies.size() == 2) {
			LOGGER.info("XCCY SwapStream TODO");
		}
	}

}
