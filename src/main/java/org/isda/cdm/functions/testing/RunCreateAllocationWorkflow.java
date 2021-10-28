package org.isda.cdm.functions.testing;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.common.functions.Create_Allocation;
import cdm.event.common.functions.Create_Execution;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.isda.cdm.functions.testing.FunctionUtils.dateTime;

public class RunCreateAllocationWorkflow implements ExecutableFunction<ExecutionInstruction, Workflow> {
    @Inject
    WorkflowFunctionHelper workflows;

    @Inject
    LineageUtils lineageUtils;

    @Inject
    Create_Execution create_execution;

    @Inject
    Create_Allocation create_allocation;

    @Override
    public Workflow execute(ExecutionInstruction executionInstruction) {
        LocalDate tradeDate = executionInstruction.getTradeDate().toLocalDate();
        LocalDate returnDate = tradeDate.plusYears(1);

        // Execution
        BusinessEvent execution = createExecution(executionInstruction);
        WorkflowStep executionWorkflowStep = workflows.createWorkflowStep(execution, dateTime(tradeDate, 9, 0));

        // Propose
        AllocationInstruction allocationInstruction = createAllocationInstruction(executionWorkflowStep.getBusinessEvent());
        Instruction instruction = Instruction.builder()
                .setInstructionFunction(AllocationInstruction.class.getSimpleName())
                .setAllocation(allocationInstruction)
                .build();
        WorkflowStep proposedWorkflowStep = workflows.createProposedWorkflowStep(executionWorkflowStep, instruction, dateTime(returnDate, 15, 0));

        // Settle
        LocalDate settlementDate = returnDate.plus(1, ChronoUnit.DAYS);
        BusinessEvent allocation = createAllocation(proposedWorkflowStep, allocationInstruction);
        WorkflowStep acceptedWorkflowStep = workflows.createAcceptedWorkflowStep(proposedWorkflowStep, allocation, dateTime(settlementDate, 18, 0));

        return Workflow.builder()
                .addSteps(executionWorkflowStep)
                .addSteps(proposedWorkflowStep)
                .addSteps(acceptedWorkflowStep)
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

    private BusinessEvent createAllocation(WorkflowStep proposedWorkflowStep, AllocationInstruction allocationInstruction) {
        AllocationInstruction allocationInstructionWithRefs =
                lineageUtils.withGlobalReference(AllocationInstruction.class, allocationInstruction);

        BusinessEvent businessEvent = create_allocation.evaluate(proposedWorkflowStep.getBusinessEvent().getPrimitives().get(0).getExecution().getAfter(), allocationInstructionWithRefs);
        return lineageUtils.withGlobalReference(BusinessEvent.class, businessEvent);
    }


    private BusinessEvent createExecution(ExecutionInstruction executionInstruction) {
        ExecutionInstruction executionInstructionWithRefs = lineageUtils
                .withGlobalReference(ExecutionInstruction.class, executionInstruction);

        BusinessEvent businessEvent = create_execution.evaluate(executionInstructionWithRefs);
        return lineageUtils.withGlobalReference(BusinessEvent.class, businessEvent);
    }

    private AllocationInstruction createAllocationInstruction(BusinessEvent executionBusinessEvent) {
        TradeState executionAfterState = executionBusinessEvent.getPrimitives().get(0).getExecution().getAfter();
        FieldWithMetaString issuerIdentifier = executionAfterState.getTrade().getTradeIdentifier().get(0).getIssuer();
        return AllocationInstruction.builder()
                .addBreakdowns(createBreakdown(issuerIdentifier, "LEI1RPT001POST1", 1, "LEI2CP00A1", 7000))
                .addBreakdowns(createBreakdown(issuerIdentifier, "LEI1RPT001POST2", 2, "LEI3CP00A2", 3000))
                .build();
    }

    private AllocationBreakdown createBreakdown(FieldWithMetaString issuerIdentifier, String identifierValue, int counterpartyFundId, String partyId, int quantity) {
        return AllocationBreakdown.builder()
                .addAllocationTradeId(Identifier.builder()
                        .addAssignedIdentifier(createAssignedIdentifier(identifierValue, "http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                        .setIssuer(issuerIdentifier)
                        .build())
                .setCounterparty(createAllocationCounterparty(counterpartyFundId, partyId, CounterpartyRoleEnum.PARTY_2))
                .addQuantity(createAllocationQuantity(quantity, "USD"))
                .build();
    }

    private Quantity createAllocationQuantity(int amount, String currency) {
        return Quantity.builder()
                .setAmount(BigDecimal.valueOf(amount))
                .setUnitOfAmount(UnitType.builder()
                        .setCurrencyValue(currency)
                        .build())
                .build();
    }

    private Counterparty createAllocationCounterparty(int fundNumber, String partyId, CounterpartyRoleEnum partyRole) {
        return Counterparty.builder()
                .setPartyReference(ReferenceWithMetaParty.builder()
                        .setValue(Party.builder()
                                .setMeta(MetaFields.builder()
                                        .setExternalKey("fund-" + fundNumber)
                                        .build())
                                .setName(FieldWithMetaString.builder().setValue("Fund " + fundNumber).build())
                                .addPartyId(FieldWithMetaString.builder()
                                        .setValue(partyId)
                                        .setMeta(MetaFields.builder()
                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso17442")
                                                .build())
                                        .build())
                                .build())
                        .build())
                .setRole(partyRole)
                .build();
    }

    private AssignedIdentifier createAssignedIdentifier(String value, String scheme) {
        return AssignedIdentifier.builder()
                .setIdentifier(FieldWithMetaString.builder()
                        .setValue(value)
                        .setMeta(MetaFields.builder()
                                .setScheme(scheme)
                                .build())
                        .build())
                .build();
    }

}
