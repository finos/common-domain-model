package org.isda.cdm.functions;

import cdm.event.common.ExecutionInstruction;
import cdm.event.common.TradeState;
import com.google.common.collect.Lists;
import com.rosetta.model.metafields.FieldWithMetaDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
                .setProduct(tradeState.getTrade().getProduct())
                .setPriceQuantity(guard(tradeState.getTrade().getTradeLot()).stream().map(t -> guard(t.getPriceQuantity())).flatMap(Collection::stream).collect(Collectors.toList()))
                .addCounterparty(guard(tradeState.getTrade().getCounterparty()))
                .addAncillaryParty(guard(tradeState.getTrade().getAncillaryParty()))
                .addParties(guard(tradeState.getTrade().getParty()))
                .addPartyRoles(guard(tradeState.getTrade().getPartyRole()))
                .setTradeDateValue(Optional.ofNullable(tradeState.getTrade().getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null))
                .addTradeIdentifier(guard(tradeState.getTrade().getTradeIdentifier()))
                .build();
    }

    public static ZonedDateTime dateTime(LocalDate tradeDate, int hour, int minute) {
        return ZonedDateTime.of(tradeDate, LocalTime.of(hour, minute), ZoneOffset.UTC.normalized());
    }
}
