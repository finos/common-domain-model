package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_Execution;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static org.isda.cdm.functions.testing.FunctionUtils.createExecutionInstructionFromTradeState;
import static org.isda.cdm.functions.testing.FunctionUtils.guard;

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
