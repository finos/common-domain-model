package org.isda.cdm.functions.testing;

import cdm.event.common.ExecutionInstruction;
import cdm.event.workflow.Workflow;
import com.regnosys.rosetta.common.testing.ExecutableFunction;

public class RunCreateAllocationWorkflow implements ExecutableFunction<ExecutionInstruction, Workflow> {
    @Override
    public Workflow execute(ExecutionInstruction executionInstruction) {
        return null;
    }

    @Override
    public Class<ExecutionInstruction> getInputType() {
        return ExecutionInstruction.class;
    }

    @Override
    public Class<Workflow> getOutputType() {
        return Workflow.class;
    }
}
