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
		if (updateRateUnitsFromNotional(priceBuilder, synonymPath, "swapStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateRateUnitsFromNotional(priceBuilder, synonymPath, "capFloorStream", "notionalSchedule", "notionalStepSchedule", "currency")
				|| updateRateUnitsFromNotional(priceBuilder, synonymPath, "bondOption", "notionalAmount", "currency")
				|| updateRateUnitsFromNotional(priceBuilder, synonymPath, "fra", "notional", "currency")
				// Credit
				|| updateRateUnitsFromNotional(priceBuilder, synonymPath, "fixedAmountCalculation", "calculationAmount", "currency")
				|| updateRateUnitsFromNotional(priceBuilder, synonymPath, "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
				|| updateRateUnitsFromNotionalReference(priceBuilder, synonymPath, "creditDefaultSwapOption", "notionalReference", "href")
				// Equity
				|| updateRateUnitsFromNotionalReference(priceBuilder, synonymPath, "interestLeg", "notional", "relativeNotionalAmount", "href")
				|| updatePriceUnitsFromNotional(priceBuilder, synonymPath, "returnLeg", "notional", "notionalAmount", "currency")
				// Fx
				|| updateFxOption(priceBuilder, synonymPath)
				// Repo
				|| updateRateUnitsFromNotional(priceBuilder, synonymPath, "repo", "nearLeg", "settlementAmount", "currency")) {
			return;
		}
	}

	@NotNull
	private boolean updateRateUnitsFromNotional(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, endsWith))
				.map(currencyMapping -> setRateUnits(builder, currencyMapping))
				.orElse(false);
	}

	@NotNull
	private boolean updateRateUnitsFromNotionalReference(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		Optional<Path> subPath = subPath(basePathElement, synonymPath);
		return subPath.flatMap(p -> getNonNullMapping(getMappings(), p, endsWith))
				.map(Mapping::getXmlValue)
				.map(String::valueOf)
				.flatMap(notionalHef -> getReferencedPath(synonymPath, subPath.get(), notionalHef))
				.flatMap(referencedPath -> getNonNullMapping(getMappings(), referencedPath, "currency"))
				.map(m -> setRateUnits(builder, m))
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
	private Boolean setRateUnits(PriceBuilder builder, Mapping currencyMapping) {
		UnitTypeBuilder unitType = toUnitType(currencyMapping);
		PriceTypeEnum priceType = builder.getPriceType();
		// unit of amount
		builder.setUnitOfAmountBuilder(unitType);
		// per unit of amount
		if (priceType == PriceTypeEnum.INTEREST_RATE
				|| priceType == PriceTypeEnum.SPREAD
				|| priceType == PriceTypeEnum.CAP_RATE
				|| priceType == PriceTypeEnum.FLOOR_RATE
				|| priceType == PriceTypeEnum.CLEAN_PRICE
				|| priceType == PriceTypeEnum.NET_PRICE
				|| priceType == PriceTypeEnum.REFERENCE_PRICE) {
			builder.setPerUnitOfAmountBuilder(unitType);
		}
		return true;
	}

	private boolean updatePriceUnitsFromNotional(PriceBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
		return subPath(basePathElement, synonymPath)
				.flatMap(subPath -> getNonNullMapping(getMappings(), subPath, endsWith))
				.map(currencyMapping -> setPriceUnits(builder, currencyMapping, FinancialUnitEnum.SHARES))
				.orElse(false);
	}

	@NotNull
	private Boolean setPriceUnits(PriceBuilder builder, Mapping currencyMapping, FinancialUnitEnum financialUnit) {
		builder
				.setUnitOfAmountBuilder(toUnitType(currencyMapping))
				.setPerUnitOfAmountBuilder(UnitType.builder().setFinancialUnit(financialUnit));
		return true;
	}

	private UnitTypeBuilder toUnitType(Mapping currencyMapping) {
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
		UnitTypeBuilder callCurrency = getNonNullMapping(getMappings(), subPath, "callCurrencyAmount", "currency").map(this::toUnitType).orElse(null);
		UnitTypeBuilder putCurrency = getNonNullMapping(getMappings(), subPath, "putCurrencyAmount", "currency").map(this::toUnitType).orElse(null);
		if (quoteBasis.equals("CallCurrencyPerPutCurrency")) {
			builder.setUnitOfAmountBuilder(callCurrency).setPerUnitOfAmountBuilder(putCurrency);
		} else if (quoteBasis.equals("PutCurrencyPerCallCurrency")) {
			builder.setUnitOfAmountBuilder(putCurrency).setPerUnitOfAmountBuilder(callCurrency);
		}
		return true;
	}
}
