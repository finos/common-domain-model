package cdm.security.lending.functions;

import cdm.event.common.BusinessEvent;
import cdm.event.common.Instruction;
import cdm.event.workflow.EventInstruction;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import org.isda.cdm.functions.testing.FunctionUtils;
import org.isda.cdm.functions.testing.WorkflowFunctionHelper;

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
        WorkflowStep returnWorkflowStep = workflows.createWorkflowStep(returnBusinessEvent, FunctionUtils.dateTime(returnDate, 9, 0));

        // step 2 on trade date PM
        LocalDate settlementDate = returnDate.plus(1, ChronoUnit.DAYS);
        EventInstruction transferInstruction = settlements.createReturnTransferInstruction(returnWorkflowStep.getBusinessEvent(),
                input.getReturnInstruction().getQuantity(),
                settlementDate);
        WorkflowStep proposedTransferWorkflowStep = workflows.createProposedWorkflowStep(returnWorkflowStep, transferInstruction, FunctionUtils.dateTime(returnDate, 15, 0));

        // step 3 on settle date
        BusinessEvent transferBusinessEvent = settlements.createTransferBusinessEvent(returnWorkflowStep, proposedTransferWorkflowStep, settlementDate);
        WorkflowStep acceptedTransferWorkflowStep = workflows.createAcceptedWorkflowStep(proposedTransferWorkflowStep, transferBusinessEvent, FunctionUtils.dateTime(settlementDate, 18, 0));

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
