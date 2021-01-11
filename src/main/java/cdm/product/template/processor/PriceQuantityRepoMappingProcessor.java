package cdm.product.template.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
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

import static cdm.base.math.UnitType.*;
import static cdm.observable.asset.Price.*;
import static cdm.observable.asset.PriceQuantity.*;

@SuppressWarnings("unused")
public class PriceQuantityRepoMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceQuantityRepoMappingProcessor.class);

	public PriceQuantityRepoMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		getValueAndUpdateMappings(synonymPath.addElement("fixedRateSchedule").addElement("initialValue"))
				.flatMap(fixedRate -> getPriceBuilder((List<PriceQuantityBuilder>) builders, fixedRate, PriceTypeEnum.INTEREST_RATE))
				.ifPresent(fixedRateBuilder -> {
					UnitTypeBuilder unitType = getUnitType(synonymPath.addElement("nearLeg").addElement("settlementAmount"));
					setUnitOfAmountAndPerUnitOfAmount(fixedRateBuilder, unitType, unitType);
				});
		getValueAndUpdateMappings(synonymPath.addElement("floatingRateCalculation").addElement("spreadSchedule").addElement("initialValue"))
				.flatMap(spread -> getPriceBuilder((List<PriceQuantityBuilder>) builders, spread, PriceTypeEnum.SPREAD))
				.ifPresent(spreadBuilder -> {
					UnitTypeBuilder unitType = getUnitType(synonymPath.addElement("nearLeg").addElement("settlementAmount"));
					setUnitOfAmountAndPerUnitOfAmount(spreadBuilder, unitType, unitType);
				});
	}

	@NotNull
	private Optional<PriceBuilder> getPriceBuilder(List<PriceQuantityBuilder> priceQuantityBuilders, String price, PriceTypeEnum priceType) {
		return priceQuantityBuilders.stream()
				.map(PriceQuantityBuilder::getPrice)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaPrice.FieldWithMetaPriceBuilder::getValue)
				.filter(Objects::nonNull)
				.filter(p -> p.getPriceType() == priceType)
				.filter(p -> price.equals(String.valueOf(p.getAmount())))
				.findFirst();
	}

	private void setUnitOfAmountAndPerUnitOfAmount(PriceBuilder priceBuilder, UnitTypeBuilder unitOfAmount, UnitTypeBuilder perUnitOfAmount) {
		priceBuilder
				.setUnitOfAmountBuilder(unitOfAmount)
				.setPerUnitOfAmountBuilder(perUnitOfAmount);
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
