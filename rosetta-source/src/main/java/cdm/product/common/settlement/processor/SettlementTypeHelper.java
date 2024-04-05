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
        if (settlementCurrencySynonymPath.endsWith("commoditySwap", "settlementCurrency")) {
            if (isCommoditySwapWithPhysicalLeg() && !isCommoditySwapWithFloatingLeg()) {
                settlementTermsBuilder.setSettlementCurrency(null);
            } else {
                setSettlementTypeToCash(settlementTermsBuilder);
            }
        }
        else if (settlementCurrencySynonymPath.endsWith("exercise", "settlementCurrency")) {
            setSettlementTypeToCash(settlementTermsBuilder);
        }
    }

    private void setSettlementTypeToCash(SettlementTerms.SettlementTermsBuilder settlementTermsBuilder) {
        Optional.ofNullable(settlementTermsBuilder)
                .filter(this::settlementCurrencyExists)
                .ifPresent(builder -> {
                    if (builder.getSettlementType() == null) {
                        builder.setSettlementType(SettlementTypeEnum.CASH);
                    }
                });
    }

    private boolean isCommoditySwapWithPhysicalLeg() {
        return mappings.stream()
                .map(Mapping::getXmlPath)
                .map(String::valueOf)
                .anyMatch(p -> p.contains("commoditySwap.coalPhysicalLeg")
                        || p.contains("commoditySwap.electricityPhysicalLeg")
                        || p.contains("commoditySwap.environmentalPhysicalLeg")
                        || p.contains("commoditySwap.gasPhysicalLeg")
                        || p.contains("commoditySwap.oilPhysicalLeg"));
    }

    private boolean isCommoditySwapWithFloatingLeg() {
        return mappings.stream()
                .map(Mapping::getXmlPath)
                .map(String::valueOf)
                .anyMatch(p -> p.contains("commoditySwap.floatingLeg"));
    }

    private boolean settlementCurrencyExists(SettlementTerms.SettlementTermsBuilder builder) {
        return Optional.ofNullable(builder.getSettlementCurrency()).map(FieldWithMetaString::getValue).isPresent();
    }
}
