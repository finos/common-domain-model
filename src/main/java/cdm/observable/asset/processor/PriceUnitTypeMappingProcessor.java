package cdm.observable.asset.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
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

import java.util.List;
import java.util.Optional;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.observable.asset.Price.PriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.subPath;

public class PriceUnitTypeMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PriceUnitTypeMappingProcessor.class);

	public PriceUnitTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		PriceBuilder priceBuilder = (PriceBuilder) parent;

		if (priceBuilder.getPriceType() == null) {
			LOGGER.info("!!!! No priceType found, cannot set unitOfAmount / perUnitOfAmount");
			return;
		}
		UnitTypeBuilder unitOfAmount = priceBuilder.getUnitOfAmount();
		if (unitOfAmount != null && unitOfAmount.hasData()) {
			LOGGER.info("yes! already set **** {} {} {}", synonymPath, priceBuilder.getPriceType(), unitOfAmount.getCurrency());

			return;
		}
		if (updateCurrencyUnitsFromNotional(priceBuilder, synonymPath, "swapStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateCurrencyUnitsFromNotional(priceBuilder, synonymPath, "capFloorStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateCurrencyUnitsFromNotional(priceBuilder, synonymPath, "bondOption", "notionalAmount", "currency")
				|| updateCurrencyUnitsFromNotional(priceBuilder, synonymPath, "fra", "notional", "currency")
				// Credit
				|| updateCurrencyUnitsFromNotional(priceBuilder, synonymPath, "fixedAmountCalculation", "calculationAmount", "currency")
				|| updateCurrencyUnitsFromNotional(priceBuilder, synonymPath, "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
				|| updateCurrencyUnitsFromNotionalReference(priceBuilder, synonymPath, "creditDefaultSwapOption", "notionalReference", "href")
				// Equity
				|| updateCurrencyUnitsFromNotionalReference(priceBuilder, synonymPath, "interestLeg", "notional", "relativeNotionalAmount", "href")
				|| updateCurrencyUnitsFromNotional(priceBuilder, synonymPath, "returnLeg", "notional", "notionalAmount", "currency")

		) {
			return;
		}
		LOGGER.info("no !!!! {} {}", synonymPath, priceBuilder.getPriceType());
	}

	@NotNull
	private Boolean updateCurrencyUnitsFromNotional(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(subPath, endsWith))
				.map(currencyMapping -> setCurrencyUnits(builder, currencyMapping))
				.orElse(false);
	}



	@NotNull
	private Boolean updateCurrencyUnitsFromNotionalReference(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		Optional<Path> subPath = subPath(basePathElement, synonymPath);
		return subPath.flatMap(p -> getNonNullMapping(p, endsWith))
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.flatMap(notionalHef -> getReferencedPath(synonymPath, subPath.get(), notionalHef))
				.flatMap(referencedPath -> getNonNullMapping(referencedPath, "currency"))
				.map(m -> setCurrencyUnits(builder, m))
				.orElse(false);
	}

	@NotNull
	private Optional<Path> getReferencedPath(Path synonymPath, Path subPath, String notionalHef) {
		return subPath(subPath.getParent().getLastElement().getPathName(), synonymPath)
				.flatMap(p -> getNonNullMappingId(p, notionalHef))
				.map(m -> m.getXmlPath())
				.map(p -> p.getParent());
	}

	@NotNull
	private Boolean setCurrencyUnits(PriceBuilder builder, Mapping currencyMapping) {
		String currency = String.valueOf(currencyMapping.getXmlValue());
		String currencyScheme = getNonNullMappedValue(currencyMapping.getXmlPath().addElement("currencyScheme"), getMappings()).orElse(null);
		UnitTypeBuilder unitType = UnitType.builder().setCurrency(FieldWithMetaString.builder()
				.setValue(currency)
				.setMeta(MetaFields.builder().setScheme(currencyScheme).build())
				.build());
		PriceTypeEnum priceType = builder.getPriceType();
		// unit of amount
		if (priceType != PriceTypeEnum.MULTIPLIER_OF_INDEX_VALUE) {
			builder.setUnitOfAmountBuilder(unitType);
		}
		// per unit of amount
		if (priceType == PriceTypeEnum.INTEREST_RATE
				|| priceType == PriceTypeEnum.SPREAD
				|| priceType == PriceTypeEnum.CAP_RATE
				|| priceType == PriceTypeEnum.FLOOR_RATE
				|| priceType == PriceTypeEnum.CLEAN_PRICE
				|| priceType == PriceTypeEnum.REFERENCE_PRICE) {
			builder.setPerUnitOfAmountBuilder(unitType);
		}
		LOGGER.info("yes! **** {} {} {}", currencyMapping.getXmlPath(), builder.getPriceType(), currency);
		return true;
	}

	private Optional<Mapping> getNonNullMapping(Path startsWith, String... endsWith) {
		return getMappings().stream()
				.filter(m -> startsWith.fullStartMatches(m.getXmlPath()))
				.filter(m -> m.getXmlPath().endsWith(endsWith))
				.filter(m -> m.getXmlValue() != null)
				.findFirst();
	}

	private Optional<Mapping> getNonNullMappingId(Path startsWith, String id) {
		return getMappings().stream()
				.filter(m -> startsWith.fullStartMatches(m.getXmlPath()))
				.filter(m -> m.getXmlPath().endsWith("id"))
				.filter(m -> id.equals(m.getXmlValue()))
				.findFirst();
	}
}
