package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.ContractFormationInstruction;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_ContractFormation;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;

import javax.inject.Inject;

public class RunFormContract implements ExecutableFunction<TradeState, BusinessEvent> {


    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(TradeState tradeState) {
        ContractFormationInstruction contractFormationInstruction = ContractFormationInstruction.builder()
                .setExecution(tradeState)
                .build();
        return formContract.evaluate(contractFormationInstruction, new DateImpl(15, 3, 2021));
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
