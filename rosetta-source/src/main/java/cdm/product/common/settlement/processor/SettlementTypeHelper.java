package cdm.product.common.settlement.processor;

import cdm.product.common.settlement.SettlementTerms;
import cdm.product.common.settlement.SettlementTypeEnum;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.Optional;

public class SettlementTypeHelper {

    public void setSettlementType(Path settlementCurrencySynonymPath, SettlementTerms.SettlementTermsBuilder settlementTermsBuilder) {
        if (settlementCurrencySynonymPath.endsWith("commoditySwap", "settlementCurrency")
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

    private boolean settlementCurrencyExists(SettlementTerms.SettlementTermsBuilder builder) {
        return Optional.ofNullable(builder.getSettlementCurrency()).map(FieldWithMetaString::getValue).isPresent();
    }
}
