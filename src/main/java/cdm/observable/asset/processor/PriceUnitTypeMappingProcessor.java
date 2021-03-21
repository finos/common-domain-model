package cdm.observable.asset.processor;

import cdm.base.math.FinancialUnitEnum;
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

import java.util.List;
import java.util.Optional;

import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.observable.asset.Price.PriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

/**
 * FpML mapper to enrich the mapped price with unitOfAmount and perUnitOfAmount.
 */
public class PriceUnitTypeMappingProcessor extends MappingProcessor {

	public PriceUnitTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		PriceBuilder priceBuilder = (PriceBuilder) parent;

		if (priceBuilder.getPriceType() == null) {
			return;
		}
		UnitTypeBuilder unitOfAmount = priceBuilder.getUnitOfAmount();
		if (unitOfAmount != null && unitOfAmount.hasData()) {
			return;
		}
		if (updateRateUnits(priceBuilder, synonymPath, "swapStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateRateUnits(priceBuilder, synonymPath, "capFloorStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateRateUnits(priceBuilder, synonymPath, "bondOption", "notionalAmount", "currency")
				|| updateRateUnits(priceBuilder, synonymPath, "fra", "notional", "currency")
				// Credit
				|| updateRateUnits(priceBuilder, synonymPath, "fixedAmountCalculation", "calculationAmount", "currency")
				|| updateRateUnits(priceBuilder, synonymPath, "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
				|| updateRateUnits(priceBuilder, synonymPath, "creditDefaultSwapOption", "notionalReference", "href")
				// Equity
				|| updateRateUnits(priceBuilder, synonymPath, "interestLeg", "notional", "relativeNotionalAmount", "href")
				|| updatePriceUnits(priceBuilder, synonymPath, "netPrice", "currency")
				|| updatePriceUnits(priceBuilder, synonymPath, "returnLeg", "notional", "notionalAmount", "currency")
				// Fx
				|| updateFxOption(priceBuilder, synonymPath)
				// Repo
				|| updateRateUnits(priceBuilder, synonymPath, "repo", "nearLeg", "settlementAmount", "currency")) {
			return;
		}
	}

	@NotNull
	private boolean updateRateUnits(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
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
				.map(m -> m.getXmlPath())
				.map(p -> p.getParent())
				.flatMap(referencedPath -> getNonNullMapping(getMappings(), referencedPath, "currency"))
				.orElse(null);
	}

	private boolean updatePriceUnits(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, endsWith))
				.map(currencyMapping -> updateBuilder(builder,
						toCurrencyUnitType(currencyMapping),
						UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARES)))
				.orElse(false);
	}

	@NotNull
	private Boolean updateBuilder(PriceBuilder builder, UnitTypeBuilder unitOfAmount, UnitTypeBuilder perUnitOfAmount) {
		// unit of amount
		builder.setUnitOfAmountBuilder(unitOfAmount);
		// per unit of amount
		if (builder.getPriceType() != PriceTypeEnum.MULTIPLIER_OF_INDEX_VALUE) {
			builder.setPerUnitOfAmountBuilder(perUnitOfAmount);
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

	private Optional<Mapping> getNonNullMappingId(Path startsWith, String id) {
		return getMappings().stream()
				.filter(m -> startsWith.fullStartMatches(m.getXmlPath()))
				.filter(m -> m.getXmlPath().endsWith("id"))
				.filter(m -> id.equals(m.getXmlValue()))
				.findFirst();
	}

	private boolean updateFxOption(PriceBuilder builder, Path synonymPath) {
		if (builder.getPriceType() != PriceTypeEnum.RATE_PRICE) {
			return false;
		}
		Optional<Path> subPath = subPath("fxOption", synonymPath);
		return subPath.flatMap(p -> getValueAndUpdateMappings(p.addElement("strike").addElement("strikeQuoteBasis")))
				.map(quoteBasis -> setFxOptionRateUnits(builder, subPath.get(), quoteBasis))
				.orElse(false);
	}

	private boolean setFxOptionRateUnits(PriceBuilder builder, Path subPath, String quoteBasis) {
		UnitTypeBuilder callCurrency = getNonNullMapping(getMappings(), subPath, "callCurrencyAmount", "currency").map(this::toCurrencyUnitType).orElse(null);
		UnitTypeBuilder putCurrency = getNonNullMapping(getMappings(), subPath, "putCurrencyAmount", "currency").map(this::toCurrencyUnitType).orElse(null);
		if (quoteBasis.equals("CallCurrencyPerPutCurrency")) {
			builder.setUnitOfAmountBuilder(callCurrency).setPerUnitOfAmountBuilder(putCurrency);
		} else if (quoteBasis.equals("PutCurrencyPerCallCurrency")) {
			builder.setUnitOfAmountBuilder(putCurrency).setPerUnitOfAmountBuilder(callCurrency);
		}
		return true;
	}

	private Path getBasePath(Path synonymPath) {
		Path.PathElement basePathElement = synonymPath.getElements().get(0);
		return new Path().addElement(basePathElement);
	}
}
