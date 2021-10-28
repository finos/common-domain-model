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
import cdm.event.common.functions.Create_Execution;
import cdm.event.workflow.Workflow;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.isda.cdm.functions.testing.FunctionUtils.dateTime;

public class RunCreateAllocationWorkflow implements ExecutableFunction<ExecutionInstruction, Workflow> {
    @Inject
    WorkflowFunctionHelper workflows;

    @Inject
    LineageUtils lineageUtils;

    @Inject
    Create_Execution create_execution;

//    private static final ObjectMapper STRICT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper()
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
//            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
//            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));


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


    private BusinessEvent createExecution(ExecutionInstruction executionInstruction) {
        ExecutionInstruction executionInstructionWithRefs = lineageUtils
                .withGlobalReference(ExecutionInstruction.class, executionInstruction);

        BusinessEvent businessEvent = create_execution.evaluate(executionInstructionWithRefs);
        return lineageUtils.withGlobalReference(BusinessEvent.class, businessEvent);
    }

    private AllocationInstruction createAllocationInstruction(BusinessEvent executionBusinessEvent) {
        TradeState executionAfterState = executionBusinessEvent.getPrimitives().get(0).getExecution().getAfter();
        FieldWithMetaString issuerIdentifier = executionAfterState.getTrade().getTradeIdentifier().get(0).getIssuer();

        return  AllocationInstruction.builder()
                .addBreakdowns(AllocationBreakdown.builder()
                        .addAllocationTradeId(Identifier.builder()
                                .addAssignedIdentifier(createAssignedIdentifier("LEI1RPT001POST1", "http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                .setIssuer(issuerIdentifier)
                                .build())
                        .setCounterparty(createAllocationCounterparty(1, "LEI2CP00A1", CounterpartyRoleEnum.PARTY_2))
                        .addQuantity(createAllocationQuantity(7000, "USD"))
                        .build())
                .addBreakdowns(AllocationBreakdown.builder()
                        .addAllocationTradeId(Identifier.builder()
                                .addAssignedIdentifier(createAssignedIdentifier("LEI1RPT001POST2", "http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                .setIssuer(issuerIdentifier)
                                .build())
                        .setCounterparty(createAllocationCounterparty(2, "LEI3CP00A2", CounterpartyRoleEnum.PARTY_2))
                        .addQuantity(createAllocationQuantity(3000, "USD"))
                        .build())
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
