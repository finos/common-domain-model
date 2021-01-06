package cdm.product.template.processor;

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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.observable.asset.Price.PriceBuilder;

@SuppressWarnings("unused")
public class PriceQuantityFxOptionMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceQuantityFxOptionMappingProcessor.class);

	public PriceQuantityFxOptionMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		getValueAndUpdateMappings(synonymPath.addElement("strike").addElement("rate"))
				.flatMap(strikeRate -> getStrikeRatePriceBuilder((List<PriceQuantity.PriceQuantityBuilder>) builders, strikeRate))
				.ifPresent(strikeRateBuilder -> {
					getValueAndUpdateMappings(synonymPath.addElement("strike").addElement("strikeQuoteBasis"))
							.ifPresent(strikeQuoteBasis -> {
								if (strikeQuoteBasis.equals("CallCurrencyPerPutCurrency")) {
									setUnitOfAmountAndPerUnitOfAmount(strikeRateBuilder, getCallCurrency(synonymPath), getPutCurrency(synonymPath));
								} else if (strikeQuoteBasis.equals("PutCurrencyPerCallCurrency")) {
									setUnitOfAmountAndPerUnitOfAmount(strikeRateBuilder, getPutCurrency(synonymPath), getCallCurrency(synonymPath));
								}
							});
					strikeRateBuilder.setPriceType(PriceTypeEnum.RATE_PRICE);
				});
	}

	@NotNull
	private Optional<PriceBuilder> getStrikeRatePriceBuilder(List<PriceQuantity.PriceQuantityBuilder> priceQuantityBuilders, String strikeRate) {
		return priceQuantityBuilders.stream()
				.map(PriceQuantity.PriceQuantityBuilder::getPrice)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.filter(p -> p.getPriceType() == null)
				.filter(p -> strikeRate.equals(p.getAmount().toString()))
				.findFirst();
	}

	private void setUnitOfAmountAndPerUnitOfAmount(PriceBuilder priceBuilder, UnitTypeBuilder unitOfAmount, UnitTypeBuilder perUnitOfAmount) {
		priceBuilder
				.setUnitOfAmountBuilder(unitOfAmount)
				.setPerUnitOfAmountBuilder(perUnitOfAmount);
	}

	@NotNull
	private UnitTypeBuilder getCallCurrency(Path synonymPath) {
		return getUnitType(synonymPath.addElement("callCurrencyAmount"));
	}

	@NotNull
	private UnitTypeBuilder getPutCurrency(Path synonymPath) {
		return getUnitType(synonymPath.addElement("putCurrencyAmount"));
	}

	private UnitTypeBuilder getUnitType(Path synonymPath) {
		Path currencyPath = synonymPath.addElement("currency");
		FieldWithMetaString.FieldWithMetaStringBuilder currencyBuilder = FieldWithMetaString.builder();
		setValueAndUpdateMappings(currencyPath, currencyBuilder::setValue);
		setValueAndUpdateMappings(currencyPath.addElement("currencyScheme"), scheme ->
				currencyBuilder.setMeta(MetaFields.builder().setScheme(scheme).build()));
		return UnitType.builder().setCurrencyBuilder(currencyBuilder);
	}
}
