package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_Execution;
import com.regnosys.rosetta.common.testing.ExecutableFunction;

import javax.inject.Inject;

import static org.isda.cdm.functions.testing.FunctionUtils.createExecutionInstructionFromTradeState;

public class RunExecute implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Override
    public BusinessEvent execute(TradeState tradeState) {
        return execute.evaluate(createExecutionInstructionFromTradeState(tradeState));
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
