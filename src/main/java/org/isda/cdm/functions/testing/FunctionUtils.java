package org.isda.cdm.functions.testing;

import cdm.event.common.ExecutionInstruction;
import cdm.event.common.TradeState;
import com.google.common.collect.Lists;
import com.rosetta.model.metafields.FieldWithMetaDate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FunctionUtils {

    public static <T> List<T> guard(List<T> list) {
        return Optional.ofNullable(list).orElse(Lists.newArrayList());
    }

    public static ExecutionInstruction createExecutionInstructionFromTradeState(TradeState tradeState) {
        return ExecutionInstruction.builder()
                .setProduct(tradeState.getTrade().getTradableProduct().getProduct())
                .setPriceQuantity(guard(tradeState.getTrade().getTradableProduct().getTradeLot()).stream().map(t -> guard(t.getPriceQuantity())).flatMap(Collection::stream).collect(Collectors.toList()))
                .addCounterparty(guard(tradeState.getTrade().getTradableProduct().getCounterparty()))
                .addAncillaryParty(guard(tradeState.getTrade().getTradableProduct().getAncillaryParty()))
                .addParties(guard(tradeState.getTrade().getParty()))
                .addPartyRoles(guard(tradeState.getTrade().getPartyRole()))
                .setTradeDate(Optional.ofNullable(tradeState.getTrade().getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null))
                .addTradeIdentifier(guard(tradeState.getTrade().getTradeIdentifier()))
                .build();
    }

}
