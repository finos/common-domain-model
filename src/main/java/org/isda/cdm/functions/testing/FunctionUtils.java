package org.isda.cdm.functions.testing;

import java.util.List;
import java.util.Optional;

import cdm.event.common.ExecutionInstruction;
import cdm.event.common.TradeState;
import com.google.common.collect.Lists;
import com.rosetta.model.metafields.FieldWithMetaDate;

public class FunctionUtils {

    public static <T> List<T> guard(List<T> list) {
        return Optional.ofNullable(list).orElse(Lists.newArrayList());
    }

    public static ExecutionInstruction createExecutionInstructionFromTradeState(TradeState tradeState) {
        return ExecutionInstruction.builder()
                .addQuantityNotation(guard(tradeState.getTrade().getTradableProduct().getQuantityNotation()))
                .addPriceNotation(guard(tradeState.getTrade().getTradableProduct().getPriceNotation()))
                .addCounterparty(guard(tradeState.getTrade().getTradableProduct().getCounterparty()))
                .addAncillaryParty(guard(tradeState.getTrade().getTradableProduct().getAncillaryParty()))
                .addParties(guard(tradeState.getTrade().getParty()))
                .addPartyRoles(guard(tradeState.getTrade().getPartyRole()))
                .setTradeDate(Optional.ofNullable(tradeState.getTrade().getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null))
                .addTradeIdentifier(guard(tradeState.getTrade().getTradeIdentifier()))
                .build();
    }

}
