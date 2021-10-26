package cdm.product.common.settlement.processor;

import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
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
        //synonymPath=dataDocument.trade.returnSwap.interestLeg.notional.relativeNotionalAmount
        getNotionalAmountIdMapping(synonymPath)
                .ifPresent(notionalAmountIdMapping -> {
                    Path returnNotionalAmountPath = notionalAmountIdMapping.getXmlPath().getParent();
                    FieldWithMetaQuantity.FieldWithMetaQuantityBuilder quantityBuilder = (FieldWithMetaQuantity.FieldWithMetaQuantityBuilder) builder.get(0);

                    mapAmount(returnNotionalAmountPath, quantityBuilder);

                    mapCurrency(returnNotionalAmountPath, quantityBuilder);
                });
    }

    @NotNull
    private Optional<Mapping> getNotionalAmountIdMapping(Path synonymPath) {
        return getNonNullMapping(getMappings(), synonymPath, "href")
                .flatMap(relativeNotionalAmountHrefMapping -> getMappings().stream()
                        .filter(mapping -> mapping.getXmlPath().endsWith(Path.parse("notionalAmount.id")))
                        .filter(m -> m.getXmlValue().equals(relativeNotionalAmountHrefMapping.getXmlValue()))
                        .findFirst());
    }

    private void mapCurrency(Path returnNotionalAmountPath, FieldWithMetaQuantity.FieldWithMetaQuantityBuilder quantityBuilder) {
        Path currencyDummyPath = returnNotionalAmountPath.addElement("currency");

        Consumer<String> currencySetter = value -> quantityBuilder.getOrCreateValue().setUnitOfAmount(UnitType.builder().setCurrencyValue(value).build());

        MappingProcessorUtils.setValueAndUpdateMappings(currencyDummyPath, currencySetter, getMappings(), getModelPath());
    }

    private void mapAmount(Path returnNotionalAmountPath, FieldWithMetaQuantity.FieldWithMetaQuantityBuilder quantityBuilder) {
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


    // 1: Fix model path to have the index for quantity - done
    // 2: Get reference object stamped onto interest rate payout - done
    // 3: Hack synonym path so reference engine can differentiate between the two references we have now (ie quantity-3 and quantity-2) - done
    // 4: Map the currency - done
}
