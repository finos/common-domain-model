package cdm.observable.asset.processor;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.CapacityUnitEnum;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.PriceTypeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.SynonymToEnumMap;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

public class PriceUnitTypeHelper {

    protected final RosettaPath modelPath;
    protected final List<Mapping> mappings;
    protected final SynonymToEnumMap synonymToEnumMap;

    protected final RosettaPath unitCurrencyModelPath;
    protected final RosettaPath perUnitOfCurrencyModelPath;
    protected final RosettaPath perUnitOfCapacityModelPath;

    public PriceUnitTypeHelper(RosettaPath modelPath, MappingContext context) {
        this.modelPath = modelPath;
        this.mappings = context.getMappings();
        this.synonymToEnumMap = context.getSynonymToEnumMap();
        this.unitCurrencyModelPath = modelPath.getParent().newSubPath("unit").newSubPath("currency").newSubPath("value");
        this.perUnitOfCurrencyModelPath = modelPath.getParent().newSubPath("perUnitOf").newSubPath("currency").newSubPath("value");
        this.perUnitOfCapacityModelPath = modelPath.getParent().newSubPath("perUnitOf").newSubPath("capacityUnit");
    }

    public boolean mapUnitType(Path synonymPath, PriceSchedule.PriceScheduleBuilder priceScheduleBuilder) {
        if (!Optional.ofNullable(priceScheduleBuilder.getPriceType()).isPresent()) {
            return false;
        }
        return
                // Rates
                updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "swapStream", "notionalSchedule", "notionalStepSchedule", "currency")
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "capFloorStream", "notionalSchedule", "notionalStepSchedule", "currency")
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "bondOption", "notionalAmount", "currency")
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "fra", "notional", "currency")
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "swapStream", "calculationPeriodAmount", "knownAmountSchedule", "currency")
                        // Credit
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "fixedAmountCalculation", "calculationAmount", "currency")
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "creditDefaultSwapOption", "notionalReference", "href")
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "creditDefaultSwapOption", "creditDefaultSwap", "protectionTerms", "calculationAmount", "currency")
                        // Equity
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "interestLeg", "notional", "relativeNotionalAmount", "href")
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "netPrice", Collections.singletonList("currency"), FinancialUnitEnum.SHARE)
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "returnLeg", Arrays.asList("notional", "notionalAmount", "currency"), FinancialUnitEnum.SHARE)
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "pricePerOption", Arrays.asList("currency"), FinancialUnitEnum.SHARE)
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "equityOption", Arrays.asList("strike", "currency"), getPerUnitOfIndexOrShare())
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "equityOption", Arrays.asList("strikePricePerUnit", "currency"), getPerUnitOfIndexOrShare())
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "equityOption", Arrays.asList("equityExercise", "settlementCurrency"), getPerUnitOfIndexOrShare())
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "brokerEquityOption", Arrays.asList("strike", "currency"), getPerUnitOfIndexOrShare())
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "brokerEquityOption", Arrays.asList("strikePricePerUnit", "currency"), getPerUnitOfIndexOrShare())
                        || updateCurrencyPerFinancialUnit(priceScheduleBuilder, synonymPath, "brokerEquityOption", Arrays.asList("equityExercise", "settlementCurrency"), getPerUnitOfIndexOrShare())
                        // Fx
                        || updateFxOption(priceScheduleBuilder, synonymPath)
                        || updateCurrencyPerCurrencyUnitFromQuotedCurrencyPair(priceScheduleBuilder, synonymPath, "fxVarianceSwap",  Arrays.asList("quotedCurrencyPair", "quoteBasis"))
                        || updateCurrencyPerCurrencyUnitFromQuotedCurrencyPair(priceScheduleBuilder, synonymPath, "fxVolatilitySwap",  Arrays.asList("quotedCurrencyPair", "quoteBasis"))
                        // Repo
                        || updateCurrencyPerCurrencyUnit(priceScheduleBuilder, synonymPath, "repo", "nearLeg", "settlementAmount", "currency")
                        // Commodity
                        || updateCurrencyPerCapacityUnit(priceScheduleBuilder, synonymPath, "commodityOption", Arrays.asList("strikePricePerUnit", "currency"), Arrays.asList("notionalQuantity", "quantityUnit"))
                        || updateCurrencyPerCapacityUnit(priceScheduleBuilder, synonymPath, "commodityOption", Arrays.asList("strikePricePerUnit", "currency"), Arrays.asList("notionalQuantitySchedule", "notionalStep", "quantityUnit"))
                        || updateCurrencyPerCapacityUnit(priceScheduleBuilder, synonymPath, "floatingLeg", Arrays.asList("calculation", "spread", "currency"), Arrays.asList("notionalQuantity", "quantityUnit"))
                        || updateCurrencyPerCapacityUnit(priceScheduleBuilder, synonymPath, "floatingLeg", Arrays.asList("calculation", "spread", "currency"), Arrays.asList("notionalQuantitySchedule", "notionalStep", "quantityUnit"))
                        // Package
                        || updatePackagePrice(priceScheduleBuilder, synonymPath)
                        || updatePackageSpread(priceScheduleBuilder, synonymPath);

    }

    protected FinancialUnitEnum getPerUnitOfIndexOrShare() {
        return exists(Arrays.asList("underlyer", "singleUnderlyer", "index", "instrumentId")) ? FinancialUnitEnum.INDEX_UNIT : FinancialUnitEnum.SHARE;
    }

    protected boolean updateCurrencyPerCurrencyUnit(PriceSchedule.PriceScheduleBuilder builder, Path synonymPath, String basePathElement, String... endsWith) {
        Optional<Mapping> mapping = subPath(basePathElement, synonymPath)
                .flatMap(subPath -> getNonNullMapping(mappings, subPath, endsWith));
        return mapping
                .map(m -> m.getXmlPath().endsWith("href") ? findReference(synonymPath, m) : m)
                .map(this::toCurrencyUnitType)
                .map(u -> {
                    // Update builder
                    updateBuilder(builder, u, u);
                    // Update mappings
                    updateEmptyMappings(mapping.get().getXmlPath(), mappings, unitCurrencyModelPath);
                    return true;
                })
                .orElse(false);
    }

    protected Mapping findReference(Path synonymPath, Mapping referenceMapping) {
        return Optional.ofNullable(referenceMapping)
                .map(Mapping::getXmlValue)
                .map(String::valueOf)
                .flatMap(notionalHef -> getNonNullMappingId(getBasePath(synonymPath), notionalHef))
                .map(Mapping::getXmlPath)
                .map(Path::getParent)
                .flatMap(referencedPath -> getNonNullMapping(mappings, referencedPath, "currency"))
                .orElse(null);
    }

    protected boolean updateCurrencyPerCurrencyUnitFromQuotedCurrencyPair(PriceSchedule.PriceScheduleBuilder builder, Path synonymPath, String basePathElement, List<String> quoteBasisRelativePath) {
        return subPath(basePathElement, synonymPath)
                .flatMap(subPath -> getNonNullMapping(mappings, subPath, toArray(quoteBasisRelativePath)))
                .map(quoteBasisMapping -> {
                    Path quoteBasisPath = quoteBasisMapping.getXmlPath();
                    Path quotedCurrencyPairPath = quoteBasisPath.getParent();
                    // currency 1
                    Optional<Mapping> currency1Mapping = getNonNullMapping(mappings, quotedCurrencyPairPath.addElement("currency1"));
                    Optional<UnitType.UnitTypeBuilder> currency1 = currency1Mapping.map(this::toCurrencyUnitType);
                    // currency 2
                    Optional<Mapping> currency2Mapping = getNonNullMapping(mappings, quotedCurrencyPairPath.addElement("currency2"));
                    Optional<UnitType.UnitTypeBuilder> currency2 = currency2Mapping .map(this::toCurrencyUnitType);

                    if (currency1.isPresent() && currency2.isPresent()) {
                        String quoteBasis = String.valueOf(quoteBasisMapping.getXmlValue());
                        if (quoteBasis.equals("Currency1PerCurrency2")) {
                            // Update builder
                            updateBuilder(builder, currency1.get(), currency2.get());
                            // Update mappings
                            updateEmptyMappings(currency1Mapping.get().getXmlPath(), mappings, unitCurrencyModelPath);
                            updateEmptyMappings(currency2Mapping.get().getXmlPath(), mappings, perUnitOfCurrencyModelPath);
                            return true;
                        } else if (quoteBasis.equals("Currency2PerCurrency1")) {
                            // Update builder
                            updateBuilder(builder, currency2.get(), currency1.get());
                            // Update mappings
                            updateEmptyMappings(currency2Mapping.get().getXmlPath(), mappings, unitCurrencyModelPath);
                            updateEmptyMappings(currency1Mapping.get().getXmlPath(), mappings, perUnitOfCurrencyModelPath);
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    protected boolean updateCurrencyPerFinancialUnit(PriceSchedule.PriceScheduleBuilder builder, Path synonymPath, String basePathElement, List<String> endsWith, FinancialUnitEnum perUnitOf) {
        return subPath(basePathElement, synonymPath)
                .flatMap(subPath -> getNonNullMapping(mappings, subPath, toArray(endsWith)))
                .map(currencyMapping -> {
                    // Update builder
                    updateBuilder(builder,
                            toCurrencyUnitType(currencyMapping),
                            UnitType.builder().setFinancialUnit(perUnitOf));
                    // Update mappings
                    updateEmptyMappings(currencyMapping.getXmlPath(), mappings, unitCurrencyModelPath);
                    return true;
                })
                .orElse(false);
    }

    protected boolean updateCurrencyPerCapacityUnit(PriceSchedule.PriceScheduleBuilder builder,
                                                  Path synonymPath,
                                                  String basePathElement,
                                                  List<String> unitEndsWith,
                                                  List<String> perUnitOfEndsWith) {
        Optional<Path> basePath = subPath(basePathElement, synonymPath);
        Optional<Mapping> unitMapping = basePath
                .flatMap(subPath -> getNonNullMapping(mappings, subPath, toArray(unitEndsWith)));
        Optional<UnitType.UnitTypeBuilder> unit = unitMapping.map(this::toCurrencyUnitType);
        Optional<Mapping> perUnitOfMapping = basePath
                .flatMap(subPath -> getNonNullMapping(mappings, subPath, toArray(perUnitOfEndsWith)));
        Optional<UnitType.UnitTypeBuilder> perUnitOf = perUnitOfMapping .map(this::toCapacityUnitEnumType);
        return unit
                .flatMap(uoa -> perUnitOf
                        .map(puoa -> {
                            // Update builder
                            updateBuilder(builder, uoa, puoa);
                            // Update mappings
                            updateEmptyMappings(unitMapping.get().getXmlPath(), mappings, unitCurrencyModelPath);
                            updateEmptyMappings(perUnitOfMapping.get().getXmlPath(), mappings, perUnitOfCapacityModelPath);
                            return true;
                        }))
                .orElse(false);
    }

    protected boolean updatePackagePrice(PriceSchedule.PriceScheduleBuilder builder, Path valueSynonymPath) {
        if (valueSynonymPath.endsWith("quote", "value")) {
            PriceTypeEnum priceType = builder.getPriceType();
            if (priceType == PriceTypeEnum.CASH_PRICE) {
                Path quoteSynonymPath = valueSynonymPath.getParent();
                Path currencySynonymPath = quoteSynonymPath.addElement("currency");
                Optional<Mapping> unitMapping = getNonNullMapping(mappings, currencySynonymPath);
                Optional<UnitType.UnitTypeBuilder> unit = unitMapping.map(this::toCurrencyUnitType);
                return unit.map(u -> {
                            // Update builder
                            updateBuilder(builder, u, UnitType.builder().setFinancialUnit(FinancialUnitEnum.CONTRACT));
                            // Update mappings
                            updateEmptyMappings(unitMapping.get().getXmlPath(), mappings, unitCurrencyModelPath);
                            return true;
                        })
                        .orElse(false);
            }
        }
        return false;
    }

    protected boolean updatePackageSpread(PriceSchedule.PriceScheduleBuilder builder, Path valueSynonymPath) {
        if (valueSynonymPath.endsWith("quote", "value")) {
            PriceTypeEnum priceType = builder.getPriceType();
            ArithmeticOperationEnum arithmeticOperator = builder.getArithmeticOperator();
            if (priceType == PriceTypeEnum.INTEREST_RATE && arithmeticOperator == ArithmeticOperationEnum.ADD) {
                Optional<Mapping> unitMapping = getPackageSpreadCurrency(valueSynonymPath.getParent());
                Optional<UnitType.UnitTypeBuilder> unit = unitMapping.map(this::toCurrencyUnitType);
                return unit.map(u -> {
                            // Update builder
                            updateBuilder(builder, u, u);
                            // Update mappings
                            updateEmptyMappings(unitMapping.get().getXmlPath(), mappings, unitCurrencyModelPath);
                            return true;
                        })
                        .orElse(false);
            }
        }
        return false;
    }

    protected Optional<Mapping> getPackageSpreadCurrency(Path quoteSynonymPath) {
        Optional<Mapping> quoteCurrencyMapping = getNonNullMapping(mappings, quoteSynonymPath.addElement("currency"));
        if (quoteCurrencyMapping.isPresent()) {
            return quoteCurrencyMapping;
        }
        return getNonNullMapping(mappings, quoteSynonymPath.getParent(), "notionalStepSchedule", "currency");
    }

    protected void updateBuilder(PriceSchedule.PriceScheduleBuilder builder, UnitType.UnitTypeBuilder unit, UnitType.UnitTypeBuilder perUnitOf) {
        // unit of amount
        builder.setUnit(unit);
        // per unit of amount
        if (builder.getArithmeticOperator() != ArithmeticOperationEnum.MULTIPLY) {
            builder.setPerUnitOf(perUnitOf);
        }
    }

    protected UnitType.UnitTypeBuilder toCurrencyUnitType(Mapping currencyMapping) {
        String currency = String.valueOf(currencyMapping.getXmlValue());
        String currencyScheme = getNonNullMappedValue(currencyMapping.getXmlPath().addElement("currencyScheme"), mappings).orElse(null);
        return UnitType.builder()
                .setCurrency(FieldWithMetaString.builder()
                        .setValue(currency)
                        .setMeta(MetaFields.builder()
                                .setScheme(currencyScheme).build())
                        .build());
    }

    protected UnitType.UnitTypeBuilder toCapacityUnitEnumType(Mapping capacityUnitMapping) {
        String value = String.valueOf(capacityUnitMapping.getXmlValue());
        UnitType.UnitTypeBuilder builder = UnitType.builder();
        try {
            synonymToEnumMap.getEnumValueOptional(CapacityUnitEnum.class, value)
                    .ifPresent(builder::setCapacityUnit);
        } catch (IllegalArgumentException e) { /* ignored */ }
        return builder;
    }

    protected Optional<Mapping> getNonNullMappingId(Path startsWith, String id) {
        return mappings.stream()
                .filter(m -> startsWith.fullStartMatches(m.getXmlPath()))
                .filter(m -> m.getXmlPath().endsWith("id"))
                .filter(m -> id.equals(m.getXmlValue()))
                .findFirst();
    }

    protected boolean exists(List<String> pathEndsWith) {
        return mappings.stream()
                .filter(m -> m.getXmlPath().endsWith(toArray(pathEndsWith)))
                .anyMatch(m -> m.getXmlValue() != null);
    }

    protected boolean updateFxOption(PriceSchedule.PriceScheduleBuilder builder, Path synonymPath) {
        Optional<Path> subPath = subPath("fxOption", synonymPath);
        return subPath
                .flatMap(p -> getValueAndUpdateMappings(p.addElement("strike").addElement("strikeQuoteBasis"), mappings, modelPath))
                .map(quoteBasis -> {
                    // Update builder
                    setFxOptionRateUnits(builder, subPath.get(), quoteBasis);
                    return true;
                })
                .orElse(false);
    }

    protected void setFxOptionRateUnits(PriceSchedule.PriceScheduleBuilder builder, Path subPath, String quoteBasis) {
        UnitType.UnitTypeBuilder callCurrency = getNonNullMapping(mappings, subPath, "callCurrencyAmount", "currency")
                .map(this::toCurrencyUnitType)
                .orElse(null);
        UnitType.UnitTypeBuilder putCurrency = getNonNullMapping(mappings, subPath, "putCurrencyAmount", "currency")
                .map(this::toCurrencyUnitType)
                .orElse(null);
        if (quoteBasis.equals("CallCurrencyPerPutCurrency")) {
            builder.setUnit(callCurrency).setPerUnitOf(putCurrency);
        } else if (quoteBasis.equals("PutCurrencyPerCallCurrency")) {
            builder.setUnit(putCurrency).setPerUnitOf(callCurrency);
        }
    }

    protected Path getBasePath(Path synonymPath) {
        Path.PathElement basePathElement = synonymPath.getElements().get(0);
        return new Path().addElement(basePathElement);
    }

    protected String[] toArray(List<String> a) {
        return a.toArray(new String[0]);
    }

    protected static void updateEmptyMappings(Path synonymPath, List<Mapping> mappings, RosettaPath rosettaPath) {
        mappings.stream()
                .filter((p) -> synonymPath.fullStartMatches(p.getXmlPath()))
                .forEach((m) -> {
                    // Update if it is not mapped yet (e.g. no model path), or has a mapping error
                    if (m.getRosettaPath() == null || m.getError() != null)
                        updateMappingSuccess(m, rosettaPath);
                });
    }
}
