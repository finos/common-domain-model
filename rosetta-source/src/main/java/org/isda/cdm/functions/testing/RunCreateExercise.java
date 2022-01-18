package org.isda.cdm.functions.testing;

import cdm.event.common.*;
import cdm.event.common.functions.Create_Exercise;
import com.regnosys.rosetta.common.testing.ExecutableFunction;

import javax.inject.Inject;

public class RunCreateExercise implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_Exercise func;

    @Override
    public BusinessEvent execute(TradeState tradeState) {
        return func.evaluate(tradeState, ExerciseInstruction.builder().build());
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
