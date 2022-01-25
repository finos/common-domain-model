package cdm.observable.asset.processor;

import cdm.base.math.CapacityUnitEnum;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import cdm.observable.asset.PriceExpression;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.observable.asset.Price.PriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

/**
 * FpML mapper to enrich the mapped price with unitOfAmount and perUnitOfAmount.
 */
@SuppressWarnings("unused")
public class PriceUnitTypeMappingProcessor extends MappingProcessor {

	public PriceUnitTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		PriceBuilder priceBuilder = (PriceBuilder) parent;

		if (!Optional.ofNullable(priceBuilder.getPriceExpression()).map(PriceExpression::getPriceType).isPresent()) {
			return;
		}
		UnitTypeBuilder unitOfAmount = priceBuilder.getUnitOfAmount();
		if (unitOfAmount != null && unitOfAmount.hasData()) {
			return;
		}
		if (updateCurrencyUnits(priceBuilder, synonymPath, "swapStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateCurrencyUnits(priceBuilder, synonymPath, "capFloorStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateCurrencyUnits(priceBuilder, synonymPath, "bondOption", "notionalAmount", "currency")
				|| updateCurrencyUnits(priceBuilder, synonymPath, "fra", "notional", "currency")
				// Credit
				|| updateCurrencyUnits(priceBuilder, synonymPath, "fixedAmountCalculation", "calculationAmount", "currency")
				|| updateCurrencyUnits(priceBuilder, synonymPath, "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
				|| updateCurrencyUnits(priceBuilder, synonymPath, "creditDefaultSwapOption", "notionalReference", "href")
				|| updateCurrencyUnits(priceBuilder, synonymPath, "creditDefaultSwapOption", "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
				// Equity
				|| updateCurrencyUnits(priceBuilder, synonymPath, "interestLeg", "notional", "relativeNotionalAmount", "href")
				|| updatePriceUnits(priceBuilder, synonymPath, "netPrice", Arrays.asList("currency"), FinancialUnitEnum.SHARE)
				|| updatePriceUnits(priceBuilder, synonymPath, "returnLeg", Arrays.asList("notional", "notionalAmount", "currency"), FinancialUnitEnum.SHARE)
				|| updatePriceUnits(priceBuilder, synonymPath, "equityOption", Arrays.asList("equityExercise", "settlementCurrency"), FinancialUnitEnum.SHARE)
				// Fx
				|| updateFxOption(priceBuilder, synonymPath)
				// Repo
				|| updateCurrencyUnits(priceBuilder, synonymPath, "repo", "nearLeg", "settlementAmount", "currency")
				// Commodity
				|| updatePriceUnits(priceBuilder, synonymPath, "commodityOption", Arrays.asList("strikePricePerUnit", "currency"), Arrays.asList("notionalQuantity", "quantityUnit"))) {
			return;
		}
	}

	private boolean updateCurrencyUnits(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, endsWith))
				.map(m -> m.getXmlPath().endsWith("href") ? findReference(synonymPath, m) : m)
				.map(this::toCurrencyUnitType)
				.map(u -> updateBuilder(builder, u, u))
				.orElse(false);
	}

	private Mapping findReference(Path synonymPath, Mapping referenceMapping) {
		return Optional.ofNullable(referenceMapping)
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.flatMap(notionalHef -> getNonNullMappingId(getBasePath(synonymPath), notionalHef))
				.map(Mapping::getXmlPath)
				.map(p -> p.getParent())
				.flatMap(referencedPath -> getNonNullMapping(getMappings(), referencedPath, "currency"))
				.orElse(null);
	}

	private boolean updatePriceUnits(PriceBuilder builder, Path synonymPath, String basePathElement, List<String> endsWith, FinancialUnitEnum perUnitOfAmount) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, toArray(endsWith)))
				.map(currencyMapping -> updateBuilder(builder,
						toCurrencyUnitType(currencyMapping),
						UnitType.builder().setFinancialUnit(perUnitOfAmount)))
				.orElse(false);
	}

	private boolean updatePriceUnits(PriceBuilder builder, Path synonymPath, String basePathElement, List<String> unitOfAmountEndsWith, List<String> perUnitOfAmountEndsWith) {
		Optional<Path> basePath = subPath(basePathElement, synonymPath);
		Optional<UnitTypeBuilder> unitOfAmount = basePath
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, toArray(unitOfAmountEndsWith)))
				.map(this::toCurrencyUnitType);
		Optional<UnitTypeBuilder> perUnitOfAmount = basePath
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, toArray(perUnitOfAmountEndsWith)))
				.map(this::toCapacityUnitEnumType);
		return unitOfAmount.flatMap(uoa -> perUnitOfAmount.map(puoa -> updateBuilder(builder, uoa, puoa)))
				.orElse(false);
	}

	@NotNull
	private Boolean updateBuilder(PriceBuilder builder, UnitTypeBuilder unitOfAmount, UnitTypeBuilder perUnitOfAmount) {
		// unit of amount
		builder.setUnitOfAmount(unitOfAmount);
		// per unit of amount
		if (builder.getPriceExpression().getPriceType() != PriceTypeEnum.MULTIPLIER_OF_INDEX_VALUE) {
			builder.setPerUnitOfAmount(perUnitOfAmount);
		}
		return true;
	}

	private UnitTypeBuilder toCurrencyUnitType(Mapping currencyMapping) {
		String currency = String.valueOf(currencyMapping.getXmlValue());
		String currencyScheme = getNonNullMappedValue(currencyMapping.getXmlPath().addElement("currencyScheme"), getMappings()).orElse(null);
		return UnitType.builder()
				.setCurrency(FieldWithMetaString.builder()
						.setValue(currency)
						.setMeta(MetaFields.builder()
								.setScheme(currencyScheme).build())
						.build());
	}

	private UnitTypeBuilder toCapacityUnitEnumType(Mapping capacityUnitMapping) {
		String value = String.valueOf(capacityUnitMapping.getXmlValue());
		UnitTypeBuilder builder = UnitType.builder();
		try {
			// This is a hack.  It should be looked up based on synonyms, however for CapacityUnitEnum the
			// FpML values match the CDM enum values.
			builder.setCapacityUnit(CapacityUnitEnum.valueOf(value.toUpperCase()));
		} catch (IllegalArgumentException e) { /* ignored */ }
		return builder;
	}

	private Optional<Mapping> getNonNullMappingId(Path startsWith, String id) {
		return getMappings().stream()
				.filter(m -> startsWith.fullStartMatches(m.getXmlPath()))
				.filter(m -> m.getXmlPath().endsWith("id"))
				.filter(m -> id.equals(m.getXmlValue()))
				.findFirst();
	}

	private boolean updateFxOption(PriceBuilder builder, Path synonymPath) {
//		if (builder.getPriceExpression().getPriceType() == PriceTypeEnum.EXCHANGE_RATE && builder.getPriceExpression().getSpreadType() != PriceTypeEnum.SPOT) {
//			return false;
//		}
		Optional<Path> subPath = subPath("fxOption", synonymPath);
		return subPath.flatMap(p -> getValueAndUpdateMappings(p.addElement("strike").addElement("strikeQuoteBasis")))
				.map(quoteBasis -> setFxOptionRateUnits(builder, subPath.get(), quoteBasis))
				.orElse(false);
	}

	private boolean setFxOptionRateUnits(PriceBuilder builder, Path subPath, String quoteBasis) {
		UnitTypeBuilder callCurrency = getNonNullMapping(getMappings(), subPath, "callCurrencyAmount", "currency").map(this::toCurrencyUnitType).orElse(null);
		UnitTypeBuilder putCurrency = getNonNullMapping(getMappings(), subPath, "putCurrencyAmount", "currency").map(this::toCurrencyUnitType).orElse(null);
		if (quoteBasis.equals("CallCurrencyPerPutCurrency")) {
			builder.setUnitOfAmount(callCurrency).setPerUnitOfAmount(putCurrency);
		} else if (quoteBasis.equals("PutCurrencyPerCallCurrency")) {
			builder.setUnitOfAmount(putCurrency).setPerUnitOfAmount(callCurrency);
		}
		return true;
	}

	private Path getBasePath(Path synonymPath) {
		Path.PathElement basePathElement = synonymPath.getElements().get(0);
		return new Path().addElement(basePathElement);
	}

	@NotNull
	private String[] toArray(List<String> a) {
		return a.toArray(new String[a.size()]);
	}
}
