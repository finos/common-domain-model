package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.ExecutionInstruction;
import cdm.event.common.functions.Create_Execution;
import cdm.event.workflow.Workflow;
import com.regnosys.rosetta.common.testing.ExecutableFunction;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.isda.cdm.functions.testing.FunctionUtils.dateTime;

public class RunCreateAllocationWorkflow implements ExecutableFunction<ExecutionInstruction, Workflow> {
    @Inject
    WorkflowFunctionHelper workflows;

    @Inject
    LineageUtils lineageUtils;

    @Inject
    Create_Execution create_execution;

    @Override
    public Workflow execute(ExecutionInstruction executionInstruction) {
        LocalDate tradeDate = executionInstruction.getTradeDate().toLocalDate();

        BusinessEvent execution = createExecution(executionInstruction);
        workflows.createWorkflowStep(execution, dateTime(tradeDate, 9, 0));

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


    public BusinessEvent createExecution(ExecutionInstruction executionInstruction) {
        ExecutionInstruction executionInstructionWithRefs = lineageUtils
                .withGlobalReference(ExecutionInstruction.class, executionInstruction);

        BusinessEvent businessEvent = create_execution.evaluate(executionInstructionWithRefs);
        return lineageUtils.withGlobalReference(BusinessEvent.class, businessEvent);
    }


}
