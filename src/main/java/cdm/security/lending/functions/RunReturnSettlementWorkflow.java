package cdm.security.lending.functions;

import cdm.event.common.BusinessEvent;
import cdm.event.common.Instruction;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.testing.ExecutableFunction;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RunReturnSettlementWorkflow implements ExecutableFunction<RunReturnSettlementWorkflowInput, Workflow> {

    @Inject
    SettlementFunctionHelper settlements;

    @Inject
    WorkflowFunctionHelper workflows;

    @Override
    public Workflow execute(RunReturnSettlementWorkflowInput input) {
        LocalDate returnDate = input.getReturnDate().toLocalDate();

        // step 1 on return date AM
        BusinessEvent returnBusinessEvent = settlements.createReturn(input.getTradeState(), input.getReturnInstruction(), input.getReturnDate());
        WorkflowStep returnWorkflowStep = workflows.createWorkflowStep(returnBusinessEvent, settlements.dateTime(returnDate, 9, 0));

        // step 2 on return date PM
        Instruction transferInstruction = settlements.createReturnTransferInstruction(returnWorkflowStep.getBusinessEvent(), input.getReturnInstruction().getQuantity());
        WorkflowStep proposedTransferWorkflowStep = workflows.createProposedWorkflowStep(returnWorkflowStep, transferInstruction, settlements.dateTime(returnDate, 15, 0));

        // step 3 on settle date
        LocalDate settlementDate = returnDate.plus(1, ChronoUnit.DAYS);
        BusinessEvent transferBusinessEvent = settlements.createTransferBusinessEvent(returnWorkflowStep, proposedTransferWorkflowStep, settlementDate);
        WorkflowStep acceptedTransferWorkflowStep = workflows.createAcceptedWorkflowStep(proposedTransferWorkflowStep, transferBusinessEvent, settlements.dateTime(settlementDate, 18, 0));

        return Workflow.builder()
                .addSteps(returnWorkflowStep)
                .addSteps(proposedTransferWorkflowStep)
                .addSteps(acceptedTransferWorkflowStep)
                .build();
    }

    @Override
    public Class<RunReturnSettlementWorkflowInput> getInputType() {
        return RunReturnSettlementWorkflowInput.class;
    }

    @Override
    public Class<Workflow> getOutputType() {
        return Workflow.class;
    }
}
