package cdm.product.common.settlement.processor;

import cdm.base.math.metafields.FieldWithMetaQuantity;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;

public class QuantityReferenceMappingProcessor extends MappingProcessor {
    public QuantityReferenceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        //synonymPath=dataDocument.trade.returnSwap.interestLeg.notional.relativeNotionalAmount
        getNonNullMapping(getMappings(), synonymPath, "href")
                .flatMap(hrefMapping -> getMappings().stream()
                        .filter(mapping -> mapping.getXmlPath().endsWith(Path.parse("notionalAmount.id")))
                        .filter(m -> m.getXmlValue().equals(hrefMapping.getXmlValue()))
                        .findFirst())
                .ifPresent(notionalAmountIdMapping -> {
                    Path amountPath = notionalAmountIdMapping.getXmlPath().getParent().addElement("amount");

                    FieldWithMetaQuantity.FieldWithMetaQuantityBuilder quantityBuilder = (FieldWithMetaQuantity.FieldWithMetaQuantityBuilder) builder.get(0);
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
                });
    }


    // 1: Fix model path to have the index for quantity - done
    // 2: Get reference object stamped onto interest rate payout - done
    // 3: Hack synonym path so reference engine can differentiate between the two references we have now (ie quantity-3 and quantity-2) - done
    // 4: Map the currency
}
