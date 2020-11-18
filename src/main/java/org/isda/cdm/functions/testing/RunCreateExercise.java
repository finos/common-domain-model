package org.isda.cdm.functions.testing;

import cdm.event.common.*;
import cdm.event.common.functions.Create_Exercise;
import cdm.legalagreement.contract.Contract;
import com.regnosys.rosetta.common.testing.ExecutableFunction;

import javax.inject.Inject;

public class RunCreateExercise implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Create_Exercise func;

    @Override
    public BusinessEvent execute(Contract contract) {
        return func.evaluate(
                BusinessEvent.builder()
                        .addPrimitivesBuilder(PrimitiveEvent.builder()
                                .setContractFormationBuilder(ContractFormationPrimitive.builder()
                                        .setAfterBuilder(PostContractFormationState.builder()
                                                .setContract(contract)))).build(),
                ExerciseInstruction.builder().build());
    }

    @Override
    public Class<Contract> getInputType() {
        return Contract.class;
    }

    @Override
    public Class<BusinessEvent> getOutputType() {
        return BusinessEvent.class;
    }
}
