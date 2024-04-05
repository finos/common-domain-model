package cdm.product.common.settlement.processor;

import cdm.product.common.settlement.SettlementTerms;
import cdm.product.common.settlement.SettlementTypeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;
import java.util.Optional;

public class SettlementTypeHelper {

    private final List<Mapping> mappings;

    public SettlementTypeHelper(List<Mapping> mappings) {
        this.mappings = mappings;
    }

    public void setSettlementType(Path settlementCurrencySynonymPath, SettlementTerms.SettlementTermsBuilder settlementTermsBuilder) {
        if (isCommoditySwap(settlementCurrencySynonymPath)
                || settlementCurrencySynonymPath.endsWith("exercise", "settlementCurrency")) {
            Optional.ofNullable(settlementTermsBuilder)
                    .filter(this::settlementCurrencyExists)
                    .ifPresent(builder -> {
                        if (builder.getSettlementType() == null) {
                            builder.setSettlementType(SettlementTypeEnum.CASH);
                        }
                    });
        }
    }

    private boolean isCommoditySwap(Path settlementCurrencySynonymPath) {
        return settlementCurrencySynonymPath.endsWith("commoditySwap", "settlementCurrency")
                // does not have a physical leg
                && mappings.stream()
                .map(Mapping::getXmlPath)
                .map(String::valueOf)
                .noneMatch(p -> p.contains("commoditySwap.coalPhysicalLeg")
                        || p.contains("commoditySwap.electricityPhysicalLeg")
                        || p.contains("commoditySwap.environmentalPhysicalLeg")
                        || p.contains("commoditySwap.gasPhysicalLeg")
                        || p.contains("commoditySwap.oilPhysicalLeg"));
    }

    private boolean settlementCurrencyExists(SettlementTerms.SettlementTermsBuilder builder) {
        return Optional.ofNullable(builder.getSettlementCurrency()).map(FieldWithMetaString::getValue).isPresent();
    }
}
