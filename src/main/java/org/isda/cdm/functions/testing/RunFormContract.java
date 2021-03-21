package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_ContractFormation;
import cdm.event.common.functions.Create_Execution;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static org.isda.cdm.functions.testing.FunctionUtils.createExecutionInstructionFromTradeState;
import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunFormContract implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(TradeState tradeState) {
        BusinessEvent executeBusinessEvent = execute.evaluate(createExecutionInstructionFromTradeState(tradeState));
        return formContract.evaluate(executeBusinessEvent, null);
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
