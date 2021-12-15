package org.isda.cdm.functions.testing;

import cdm.event.common.*;
import cdm.event.common.functions.Create_ContractFormation;
import cdm.event.position.PositionStatusEnum;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;

import javax.inject.Inject;

public class RunFormContract implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_ContractFormation formContract;

    @Override
    public BusinessEvent execute(TradeState tradeState) {
        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
        tradeStateBuilder.getOrCreateState().setPositionState(PositionStatusEnum.EXECUTED);
        tradeStateBuilder.getTrade().setContractDetails(ContractDetails.builder());
        tradeStateBuilder.prune();

        return formContract.evaluate(ContractFormationInstruction.builder(), tradeStateBuilder, new DateImpl(15, 3, 2021));
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
