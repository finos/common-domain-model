package cdm.security.lending.functions;

import cdm.event.common.BusinessEvent;
import cdm.event.common.ExecutionInstruction;
import cdm.event.workflow.EventInstruction;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.Date;

import jakarta.inject.Inject;
import java.time.LocalDate;

import static cdm.security.lending.functions.WorkflowFunctionHelper.dateTime;

public class RunNewSettlementWorkflow implements ExecutableFunction<ExecutionInstruction, Workflow> {

    @Inject
    SettlementFunctionHelper settlements;
    @Inject
    WorkflowFunctionHelper workflows;

    @Override
    public Workflow execute(ExecutionInstruction executionInstruction) {
        Date tradeDate = executionInstruction.getTradeDate().getValue();

        // step 1 on trade date AM
        BusinessEvent executionBusinessEvent = settlements.createExecution(executionInstruction, tradeDate);
        WorkflowStep executionWorkflowStep = workflows.createWorkflowStep(executionBusinessEvent, dateTime(tradeDate.toLocalDate(), 9, 0));

        // step 2 on trade date PM
        LocalDate settlementDate = settlements.nearSettlementDate(executionWorkflowStep.getBusinessEvent());
        EventInstruction transferInstruction = settlements.createTransferInstruction(executionWorkflowStep.getBusinessEvent(), settlementDate);
        WorkflowStep proposedTransferWorkflowStep = workflows.createProposedWorkflowStep(executionWorkflowStep, transferInstruction, dateTime(tradeDate.toLocalDate(), 15, 0));

        // step 3 on settle date
        BusinessEvent transferBusinessEvent = settlements.createTransferBusinessEvent(transferInstruction);
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
