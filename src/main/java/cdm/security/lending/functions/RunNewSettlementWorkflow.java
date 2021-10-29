package cdm.security.lending.functions;

import cdm.event.common.*;
import cdm.event.workflow.*;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import org.isda.cdm.functions.testing.WorkflowFunctionHelper;

import javax.inject.Inject;
import java.time.*;

import static org.isda.cdm.functions.testing.FunctionUtils.dateTime;

public class RunNewSettlementWorkflow implements ExecutableFunction<ExecutionInstruction, Workflow> {

    @Inject
    SettlementFunctionHelper settlements;
    @Inject
    WorkflowFunctionHelper workflows;

    @Override
    public Workflow execute(ExecutionInstruction executionInstruction) {
        LocalDate tradeDate = executionInstruction.getTradeDate().toLocalDate();

        // step 1 on trade date AM
        BusinessEvent executionBusinessEvent = settlements.createExecution(executionInstruction);
        WorkflowStep executionWorkflowStep = workflows.createWorkflowStep(executionBusinessEvent, dateTime(tradeDate, 9, 0));

        // step 2 on trade date PM
        Instruction transferInstruction = settlements.createTransferInstruction(executionWorkflowStep.getBusinessEvent());
        WorkflowStep proposedTransferWorkflowStep = workflows.createProposedWorkflowStep(executionWorkflowStep, transferInstruction, dateTime(tradeDate, 15, 0));

        // step 3 on settle date
        LocalDate settlementDate = settlements.nearSettlementDate(executionWorkflowStep.getBusinessEvent());
        BusinessEvent transferBusinessEvent = settlements.createTransferBusinessEvent(executionWorkflowStep, proposedTransferWorkflowStep, settlementDate);
        WorkflowStep acceptedTransferWorkflowStep = workflows.createAcceptedWorkflowStep(proposedTransferWorkflowStep, transferBusinessEvent, dateTime(settlementDate, 18, 0));

        return Workflow.builder()
                .addSteps(executionWorkflowStep)
                .addSteps(proposedTransferWorkflowStep)
                .addSteps(acceptedTransferWorkflowStep)
                .build();
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
