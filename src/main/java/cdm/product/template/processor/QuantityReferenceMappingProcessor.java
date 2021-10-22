package cdm.product.template.processor;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradeLot;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMapping;

public class QuantityReferenceMappingProcessor extends MappingProcessor {
    public QuantityReferenceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        Path relativeNotionalAmountPath = synonymPath.append(Path.parse("notional.relativeNotionalAmount.href"));

        getNonNullMapping(getMappings(), relativeNotionalAmountPath)
                .filter(mapping -> "EquityNotionalAmount".equals(mapping.getXmlValue()))
                .ifPresent((equityNotionalAmountMapping -> {

                    TradeLot.TradeLotBuilder tradeLotBuilder = (TradeLot.TradeLotBuilder) parent;
                    PriceQuantity.PriceQuantityBuilder priceQuantityBuilder = tradeLotBuilder.getPriceQuantity().get(0);
                    Optional<Quantity.QuantityBuilder> maybeIrQuantity = priceQuantityBuilder.getQuantity()
                            .stream()
                            .map(FieldWithMetaQuantity.FieldWithMetaQuantityBuilder::getValue)
                            .filter(q -> !q.getUnitOfAmount().equals(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE)))
                            .findFirst();

                    maybeIrQuantity.ifPresent(irQuantity -> {
                        tradeLotBuilder.getOrCreatePriceQuantity(1)
                                .getOrCreateQuantity(0)
                                .getValue()
                                .setAmount(irQuantity.getAmount());
                    });
                }));
    }
}
