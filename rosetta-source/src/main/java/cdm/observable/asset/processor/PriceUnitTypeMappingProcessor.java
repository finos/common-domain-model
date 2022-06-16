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
import java.util.Collections;
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


	private final RosettaPath unitOfAmountCurrencyModelPath;
	private final RosettaPath perUnitOfAmountCurrencyModelPath;
	private final RosettaPath perUnitOfAmountCapacityModelPath;

	public PriceUnitTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
		unitOfAmountCurrencyModelPath = getModelPath().getParent().newSubPath("unitOfAmount").newSubPath("currency").newSubPath("value");
		perUnitOfAmountCurrencyModelPath = getModelPath().getParent().newSubPath("perUnitOfAmount").newSubPath("currency").newSubPath("value");
		perUnitOfAmountCapacityModelPath = getModelPath().getParent().newSubPath("perUnitOfAmount").newSubPath("capacityUnit");
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
		boolean result =
				// Rates
				updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "swapStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "capFloorStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "bondOption", "notionalAmount", "currency")
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "fra", "notional", "currency")
				// Credit
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "fixedAmountCalculation", "calculationAmount", "currency")
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "creditDefaultSwapOption", "notionalReference", "href")
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "creditDefaultSwapOption", "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
				// Equity
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "interestLeg", "notional", "relativeNotionalAmount", "href")
				|| updateCurrencyPerFinancialUnit(priceBuilder, synonymPath, "netPrice", Collections.singletonList("currency"), FinancialUnitEnum.SHARE)
				|| updateCurrencyPerFinancialUnit(priceBuilder, synonymPath, "returnLeg", Arrays.asList("notional", "notionalAmount", "currency"), FinancialUnitEnum.SHARE)
				|| updateCurrencyPerFinancialUnit(priceBuilder, synonymPath, "equityOption", Arrays.asList("strike", "currency"), getPerUnitOfAmountIndexOrShare())
				|| updateCurrencyPerFinancialUnit(priceBuilder, synonymPath, "equityOption", Arrays.asList("strikePricePerUnit", "currency"), getPerUnitOfAmountIndexOrShare())
				|| updateCurrencyPerFinancialUnit(priceBuilder, synonymPath, "equityOption", Arrays.asList("equityExercise", "settlementCurrency"), getPerUnitOfAmountIndexOrShare())
				// Fx
				|| updateFxOption(priceBuilder, synonymPath)
				|| updateCurrencyPerCurrencyUnitFromQuotedCurrencyPair(priceBuilder, synonymPath, "fxVarianceSwap",  Arrays.asList("quotedCurrencyPair", "quoteBasis"))
				|| updateCurrencyPerCurrencyUnitFromQuotedCurrencyPair(priceBuilder, synonymPath, "fxVolatilitySwap",  Arrays.asList("quotedCurrencyPair", "quoteBasis"))
				// Repo
				|| updateCurrencyPerCurrencyUnit(priceBuilder, synonymPath, "repo", "nearLeg", "settlementAmount", "currency")
				// Commodity
				|| updateCurrencyPerCapacityUnit(priceBuilder, synonymPath, "commodityOption", Arrays.asList("strikePricePerUnit", "currency"), Arrays.asList("notionalQuantity", "quantityUnit"));
	}

	@NotNull
	private FinancialUnitEnum getPerUnitOfAmountIndexOrShare() {
		return exists(Arrays.asList("underlyer", "singleUnderlyer", "index", "instrumentId")) ? FinancialUnitEnum.INDEX_UNIT : FinancialUnitEnum.SHARE;
	}

	private boolean updateCurrencyPerCurrencyUnit(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		Optional<Mapping> mapping = subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, endsWith));
		return mapping
				.map(m -> m.getXmlPath().endsWith("href") ? findReference(synonymPath, m) : m)
				.map(this::toCurrencyUnitType)
				.map(u -> {
					// Update builder
					updateBuilder(builder, u, u);
					// Update mappings
					updateEmptyMappings(mapping.get().getXmlPath(), getMappings(), unitOfAmountCurrencyModelPath);
					return true;
				})
				.orElse(false);
	}

	private Mapping findReference(Path synonymPath, Mapping referenceMapping) {
		return Optional.ofNullable(referenceMapping)
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.flatMap(notionalHef -> getNonNullMappingId(getBasePath(synonymPath), notionalHef))
				.map(Mapping::getXmlPath)
				.map(Path::getParent)
				.flatMap(referencedPath -> getNonNullMapping(getMappings(), referencedPath, "currency"))
				.orElse(null);
	}

	private boolean updateCurrencyPerCurrencyUnitFromQuotedCurrencyPair(PriceBuilder builder, Path synonymPath, String basePathElement, List<String> quoteBasisRelativePath) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, toArray(quoteBasisRelativePath)))
				.map(quoteBasisMapping -> {
					Path quoteBasisPath = quoteBasisMapping.getXmlPath();
					Path quotedCurrencyPairPath = quoteBasisPath.getParent();
					// currency 1
					Optional<Mapping> currency1Mapping = getNonNullMapping(getMappings(), quotedCurrencyPairPath.addElement("currency1"));
					Optional<UnitTypeBuilder> currency1 = currency1Mapping.map(this::toCurrencyUnitType);
					// currency 2
					Optional<Mapping> currency2Mapping = getNonNullMapping(getMappings(), quotedCurrencyPairPath.addElement("currency2"));
					Optional<UnitTypeBuilder> currency2 = currency2Mapping .map(this::toCurrencyUnitType);

					if (currency1.isPresent() && currency2.isPresent()) {
						String quoteBasis = String.valueOf(quoteBasisMapping.getXmlValue());
						if (quoteBasis.equals("Currency1PerCurrency2")) {
							// Update builder
							updateBuilder(builder, currency1.get(), currency2.get());
							// Update mappings
							updateEmptyMappings(currency1Mapping.get().getXmlPath(), getMappings(), unitOfAmountCurrencyModelPath);
							updateEmptyMappings(currency2Mapping.get().getXmlPath(), getMappings(), perUnitOfAmountCurrencyModelPath);
							return true;
						} else if (quoteBasis.equals("Currency2PerCurrency1")) {
							// Update builder
							updateBuilder(builder, currency2.get(), currency1.get());
							// Update mappings
							updateEmptyMappings(currency2Mapping.get().getXmlPath(), getMappings(), unitOfAmountCurrencyModelPath);
							updateEmptyMappings(currency1Mapping.get().getXmlPath(), getMappings(), perUnitOfAmountCurrencyModelPath);
							return true;
						}
					}
					return false;
				})
				.orElse(false);
	}

	private boolean updateCurrencyPerFinancialUnit(PriceBuilder builder, Path synonymPath, String basePathElement, List<String> endsWith, FinancialUnitEnum perUnitOfAmount) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, toArray(endsWith)))
				.map(currencyMapping -> {
					// Update builder
					updateBuilder(builder,
						toCurrencyUnitType(currencyMapping),
						UnitType.builder().setFinancialUnit(perUnitOfAmount));
					// Update mappings
					updateEmptyMappings(currencyMapping.getXmlPath(), getMappings(), unitOfAmountCurrencyModelPath);
					return true;
				})
				.orElse(false);
	}

	private boolean updateCurrencyPerCapacityUnit(PriceBuilder builder,
												  Path synonymPath,
												  String basePathElement,
												  List<String> unitOfAmountEndsWith,
												  List<String> perUnitOfAmountEndsWith) {
		Optional<Path> basePath = subPath(basePathElement, synonymPath);
		Optional<Mapping> unitOfAmountMapping = basePath
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, toArray(unitOfAmountEndsWith)));
		Optional<UnitTypeBuilder> unitOfAmount = unitOfAmountMapping.map(this::toCurrencyUnitType);
		Optional<Mapping> perUnitOfAmountMapping = basePath
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, toArray(perUnitOfAmountEndsWith)));
		Optional<UnitTypeBuilder> perUnitOfAmount = perUnitOfAmountMapping .map(this::toCapacityUnitEnumType);
		return unitOfAmount
				.flatMap(uoa -> perUnitOfAmount
						.map(puoa -> {
							// Update builder
							updateBuilder(builder, uoa, puoa);
							// Update mappings
							updateEmptyMappings(unitOfAmountMapping.get().getXmlPath(), getMappings(), unitOfAmountCurrencyModelPath);
							updateEmptyMappings(perUnitOfAmountMapping.get().getXmlPath(), getMappings(), perUnitOfAmountCapacityModelPath);
							return true;
						}))
				.orElse(false);
	}

	private void updateBuilder(PriceBuilder builder, UnitTypeBuilder unitOfAmount, UnitTypeBuilder perUnitOfAmount) {
		// unit of amount
		builder.setUnitOfAmount(unitOfAmount);
		// per unit of amount
		if (builder.getPriceExpression().getPriceType() != PriceTypeEnum.MULTIPLIER_OF_INDEX_VALUE) {
			builder.setPerUnitOfAmount(perUnitOfAmount);
		}
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

	private boolean exists(List<String> pathEndsWith) {
		return getMappings().stream()
				.filter(m -> m.getXmlPath().endsWith(toArray(pathEndsWith)))
				.anyMatch(m -> m.getXmlValue() != null);
	}

	private boolean updateFxOption(PriceBuilder builder, Path synonymPath) {
		Optional<Path> subPath = subPath("fxOption", synonymPath);
		return subPath
				.flatMap(p -> getValueAndUpdateMappings(p.addElement("strike").addElement("strikeQuoteBasis")))
				.map(quoteBasis -> {
					// Update builder
					setFxOptionRateUnits(builder, subPath.get(), quoteBasis);
					return true;
				})
				.orElse(false);
	}

	private void setFxOptionRateUnits(PriceBuilder builder, Path subPath, String quoteBasis) {
		UnitTypeBuilder callCurrency = getNonNullMapping(getMappings(), subPath, "callCurrencyAmount", "currency")
				.map(this::toCurrencyUnitType)
				.orElse(null);
		UnitTypeBuilder putCurrency = getNonNullMapping(getMappings(), subPath, "putCurrencyAmount", "currency")
				.map(this::toCurrencyUnitType)
				.orElse(null);
		if (quoteBasis.equals("CallCurrencyPerPutCurrency")) {
			builder.setUnitOfAmount(callCurrency).setPerUnitOfAmount(putCurrency);
		} else if (quoteBasis.equals("PutCurrencyPerCallCurrency")) {
			builder.setUnitOfAmount(putCurrency).setPerUnitOfAmount(callCurrency);
		}
	}

	private Path getBasePath(Path synonymPath) {
		Path.PathElement basePathElement = synonymPath.getElements().get(0);
		return new Path().addElement(basePathElement);
	}

	@NotNull
	private String[] toArray(List<String> a) {
		return a.toArray(new String[0]);
	}

	public static void updateEmptyMappings(Path synonymPath, List<Mapping> mappings, RosettaPath rosettaPath) {
		mappings.stream()
				.filter((p) -> synonymPath.fullStartMatches(p.getXmlPath()))
				.forEach((m) -> {
					// Update if it is not mapped yet (e.g. no model path), or has a mapping error
					if (m.getRosettaPath() == null || m.getError() != null)
						updateMappingSuccess(m, rosettaPath);
				});
	}
}
