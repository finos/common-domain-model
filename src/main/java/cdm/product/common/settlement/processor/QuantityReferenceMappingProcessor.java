package cdm.product.common.settlement.processor;

import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity.FieldWithMetaQuantityBuilder;
import com.regnosys.rosetta.common.translation.*;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;

public class QuantityReferenceMappingProcessor extends MappingProcessor {
    public QuantityReferenceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    /*
     * This mapping is performing the following two tasks on equity return swap products:
     *
     * 1. On the TradeLot there are two PriceQuantities one for each leg. The PriceQuantity that represents
     * the equity return leg actually has the quantities for both legs mapped to it. However, the second PriceQuantity
     * which represents the interest rate leg only has a price with no quantity. So the first task of this mapper
     * is to map the interest rate quantity (the currency and the amount) from the source FPML onto the interest
     * rate leg PQ of the TradeLot.
     *
     * 2. In the `payout -> interestRatePayout -> payoutQuantity` we now need to add a reference to the quantity mapped
     * in step 1 which will appear as a quantitySchedule inside payoutQuantity
     *
     */
    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        getNotionalAmountIdMapping(synonymPath)
                .ifPresent(notionalAmountIdMapping -> {
                    Path returnNotionalAmountPath = notionalAmountIdMapping.getXmlPath().getParent();
                    getQuantityFromBuilder(builder).ifPresent(quantityBuilder -> {
                        mapAmount(returnNotionalAmountPath, quantityBuilder);

                        mapCurrency(returnNotionalAmountPath, quantityBuilder);
                    });
                });
    }

    private Optional<FieldWithMetaQuantityBuilder> getQuantityFromBuilder(List<? extends RosettaModelObjectBuilder> builder) {
        return builder.stream()
                .filter(element -> element instanceof FieldWithMetaQuantityBuilder)
                .map(element -> (FieldWithMetaQuantityBuilder) element)
                .findFirst();
    }

    @NotNull
    private Optional<Mapping> getNotionalAmountIdMapping(Path synonymPath) {
        return getNonNullMapping(getMappings(), synonymPath, "href")
                .flatMap(relativeNotionalAmountHrefMapping -> getMappings().stream()
                        .filter(mapping -> mapping.getXmlPath().endsWith(Path.parse("notionalAmount.id")))
                        .filter(m -> m.getXmlValue().equals(relativeNotionalAmountHrefMapping.getXmlValue()))
                        .findFirst());
    }

    /*
     * This method is creating a completely new pair of mappings with a dummy path. One of these mappings is the new
     * reference mapping and the other is a new amount mapping.
     *
     * The reason for using a dummy path is so that the
     * reference in the interestRatePayout can be resolved directly to it's corresponding value in the TradeLot. If we
     * did not use a dummy path then payoutQuantity reference will resolve incorrectly back to the TradeLot
     * equity payout instead of the interest rate payout.
     */
    private void mapAmount(Path returnNotionalAmountPath, FieldWithMetaQuantityBuilder quantityBuilder) {
        Path amountPath = returnNotionalAmountPath.addElement("amount");
        getNonNullMappedValue(amountPath, getMappings()).ifPresent(amount -> {
            quantityBuilder.getOrCreateValue().setAmount(new BigDecimal(amount));

            Path modelPath = PriceQuantityHelper.incrementPathElementIndex(PathUtils.toPath(getModelPath()), "quantity", 0);

            Path dummyAmountPath = Path.parse("dummy").append(amountPath);
            getMappings()
                    .stream()
                    .filter(x -> x.getXmlPath().endsWith("relativeNotionalAmount", "href"))
                    .filter(x -> x.getRosettaValue() instanceof Reference.ReferenceBuilder)
                    .findFirst()
                    .ifPresent(reference -> {
                        getMappings().add(new Mapping(dummyAmountPath, amount, reference.getRosettaPath(), reference.getRosettaValue(), null, false, true, false));
                        getMappings().remove(reference);
                    });

            getMappings().add(new Mapping(dummyAmountPath, amount, modelPath, amount, null, false, true, false));
        });
    }

    private void mapCurrency(Path returnNotionalAmountPath, FieldWithMetaQuantityBuilder quantityBuilder) {
        Path currencyPath = returnNotionalAmountPath.addElement("currency");

        Consumer<String> currencySetter = value -> quantityBuilder.getOrCreateValue().setUnitOfAmount(UnitType.builder().setCurrencyValue(value).build());

        MappingProcessorUtils.setValueAndUpdateMappings(currencyPath, currencySetter, getMappings(), getModelPath());
    }
}
