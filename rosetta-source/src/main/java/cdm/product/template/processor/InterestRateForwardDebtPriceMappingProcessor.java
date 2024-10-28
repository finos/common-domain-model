package cdm.product.template.processor;

import cdm.base.math.UnitType;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cdm.base.math.UnitType.builder;
import static cdm.observable.asset.PriceQuantity.PriceQuantityBuilder;
import static cdm.observable.asset.processor.PriceQuantityHelper.incrementPathElementIndex;
import static cdm.observable.asset.processor.PriceQuantityHelper.toReferencablePriceBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;
import static com.rosetta.util.CollectionUtils.emptyIfNull;
import static java.util.stream.Collectors.groupingBy;

@SuppressWarnings("unused")
public class InterestRateForwardDebtPriceMappingProcessor extends MappingProcessor {

    public InterestRateForwardDebtPriceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path genenicProductPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        List<PriceQuantityBuilder> priceQuantityBuilders =
                (List<PriceQuantityBuilder>) builders;
        Path tradePath = genenicProductPath.getParent();
        Path topLevelPath = tradePath.getParent();

        findMappings(getMappings(), topLevelPath.addElement("quote"))
                .entrySet()
                .forEach(entry -> {
                    Path quotePath = entry.getKey();
                    List<Mapping> quoteMappings = entry.getValue();
                    addPrice(genenicProductPath,
                            getPriceQuantityBuilder(priceQuantityBuilders),
                            quotePath,
                            quoteMappings);
                });
    }

    private static PriceQuantityBuilder getPriceQuantityBuilder(List<PriceQuantityBuilder> priceQuantityBuilders) {
        return priceQuantityBuilders.stream()
                .filter(RosettaModelObjectBuilder::hasData)
                .findFirst()
                .orElse(null);
    }

    private Map<Path, List<Mapping>> findMappings(List<Mapping> mappings, Path synonymPath) {
        return mappings.stream()
                .filter(m -> synonymPath.nameStartMatches(m.getXmlPath()))
                .filter(m -> Objects.nonNull(m.getXmlValue()))
                .collect(groupingBy(m -> m.getXmlPath().getParent()));
    }

    private void addPrice(Path genenicProductPath, PriceQuantityBuilder priceQuantityBuilder, Path quotePath, List<Mapping> quoteMappings) {
        Mapping valueMapping = getNonNullMapping(quoteMappings, quotePath.addElement("value")).orElse(null);
        Mapping quoteUnitsMapping = getNonNullMapping(quoteMappings, quotePath.addElement("quoteUnits")).orElse(null);

        if (isPriceNotation(quotePath, quoteMappings) && valueMapping != null) {
            BigDecimal rate = new BigDecimal(String.valueOf(valueMapping.getXmlValue()));
            if (quoteUnitsMapping != null && String.valueOf(quoteUnitsMapping.getXmlValue()).equals("Percentage")){
                rate = rate.divide(BigDecimal.valueOf(100));
            }
            UnitType.UnitTypeBuilder unitType = toCurrencyUnitType(genenicProductPath);
            PriceTypeEnum priceType = PriceTypeEnum.ASSET_PRICE;

            FieldWithMetaPriceSchedule.FieldWithMetaPriceScheduleBuilder fieldWithPriceScheduleBuilder =
                    toReferencablePriceBuilder(rate, unitType, unitType, priceType, null, null);
            // price index must be incremented otherwise any references will break
            Path baseModelPath = toPath(getModelPath()).addElement("price", 0).addElement("value");
            Path amountModelPath = incrementPathElementIndex(baseModelPath, "price", emptyIfNull(priceQuantityBuilder.getPrice()).size());
            updateMapping(valueMapping, amountModelPath);
            // add to PriceQuantity
            priceQuantityBuilder.addPrice(fieldWithPriceScheduleBuilder);
        }
    }

    private static Boolean isPriceNotation(Path quotePath, List<Mapping> quoteMappings) {
        return getNonNullMappedValue(quotePath.addElement("measureType"), quoteMappings)
                .map("PriceNotation"::equals)
                .orElse(false);
    }

    private UnitType.UnitTypeBuilder toCurrencyUnitType(Path startsWithPath) {
        String currency = getNonNullMappedValue(getMappings(), startsWithPath, "notional", "currency").orElse(null);
        String currencyScheme = getNonNullMappedValue(getMappings(), startsWithPath, "notional", "currency", "currencyScheme").orElse(null);
        return builder()
                .setCurrency(FieldWithMetaString.builder()
                        .setValue(currency)
                        .setMeta(MetaFields.builder()
                                .setScheme(currencyScheme)
                                .build()));
    }

    private void updateMapping(Mapping mapping, Path modelPath) {
        mapping.setRosettaPath(modelPath);
        // clear errors
        mapping.setError(null);
        mapping.setCondition(true);
        mapping.setDuplicate(false);
    }
}
