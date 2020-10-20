package org.isda.cdm.functions.testing;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import org.isda.cdm.BusinessEvent;
import org.isda.cdm.Contract;
import org.isda.cdm.TradeDate;
import org.isda.cdm.TradeState;
import org.isda.cdm.functions.Create_Execution;

import com.regnosys.rosetta.common.testing.ExecutableFunction;

public class RunExecute implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Override
    public BusinessEvent execute(TradeState tradeState) {
        return execute.evaluate(tradeState.getTrade().getTradableProduct().getProduct(),
                guard(tradeState.getTrade().getTradableProduct().getQuantityNotation()),
                guard(tradeState.getTrade().getTradableProduct().getPriceNotation()),
                guard(tradeState.getTrade().getTradableProduct().getCounterparties()),
                guard(tradeState.getTrade().getParty()),
                guard(tradeState.getTrade().getPartyRole()),
                Collections.emptyList(),
                null,
                Optional.ofNullable(tradeState.getTrade().getTradeDate()).map(TradeDate::getDate).orElse(null),
                guard(tradeState.getTrade().getIdentifier()));
    }

    @Override
    public Class<TradeState> getInputType() {
        return TradeState.class;
    }

    @Override
    public Class<BusinessEvent> getOutputType() {
        return BusinessEvent.class;
    }
}
