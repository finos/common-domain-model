package org.finos.cdm.example.functions;

import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.event.common.*;
import cdm.event.workflow.EventInstruction;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.functions.Create_AcceptedWorkflowStepFromInstruction;
import cdm.observable.asset.PriceQuantity;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.finos.cdm.example.AbstractExampleTest;
import org.finos.cdm.example.util.ResourcesUtils;
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test demonstrates how to create a WorkflowStep from an input TradeState and Instructions.
 */
public class CreateNovationEventTest extends AbstractExampleTest {

    private static final String CURRENCY_SCHEME = "http://www.fpml.org/coding-scheme/external/iso4217";

    @Inject
    Create_AcceptedWorkflowStepFromInstruction createWorkflowStep;

    @Inject
    WorkflowPostProcessor postProcessor;

    @Test
    void createNovationEvent() throws IOException {
        // Create function input that contains workflow step instructions (i.e. WorkflowStep containing a proposed EventInstruction)
        WorkflowStep workflowStepInstruction = getWorkflowStepInstruction();

        // Run function to create a fully-specified event WorkflowStep (i.e. WorkflowStep containing a BusinessEvent) from the input
        WorkflowStep eventWorkflowStep = runFunctionAndPostProcess(workflowStepInstruction);

        // Assert
        assertWorkflowStep(eventWorkflowStep);
    }

    /**
     * Creates function input.
     *
     * @return WorkflowStep containing a proposed EventInstruction
     * @throws IOException
     */
    private WorkflowStep getWorkflowStepInstruction() throws IOException {
        // Trade to be novated.  Note that all references are resolved here.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "result-json-files/fpml-5-10/products/rates/USD-Vanilla-swap.json");

        Date eventDate = Date.of(2013, 2, 12);

        // SplitInstruction contains two split breakdowns
        SplitInstruction splitInstruction = SplitInstruction.builder()
                // Split breakdown for party change, new trade id etc
                .addBreakdown(PrimitiveInstruction.builder()
                        .setPartyChange(PartyChangeInstruction.builder()
                                .setCounterparty(Counterparty.builder()
                                        .setPartyReferenceValue(Party.builder()
                                                .setMeta(MetaFields.builder().setExternalKey("party3"))
                                                .setNameValue("Bank Z")
                                                .addPartyId(PartyIdentifier.builder()
                                                        .setIdentifierType(PartyIdentifierTypeEnum.LEI)
                                                        .setIdentifierValue("LEI-PARTY-3")))
                                        .setRole(CounterpartyRoleEnum.PARTY_2))
                                .setTradeId(Lists.newArrayList(TradeIdentifier.builder()
                                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                                .setIdentifierValue("UTI-Trade-Party-3"))
                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                                        .setIssuerValue("LEI-PARTY-3")))))
                // Split breakdown to terminate the original trade
                .addBreakdown(PrimitiveInstruction.builder()
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantityValue(NonNegativeQuantitySchedule.builder()
                                                .setValue(BigDecimal.valueOf(0.0))
                                                .setUnit(UnitType.builder()
                                                        .setCurrency(FieldWithMetaString.builder()
                                                                .setValue("USD")
                                                                .setMeta(MetaFields.builder()
                                                                        .setScheme(CURRENCY_SCHEME))))))));

        // Create an Instruction that contains:
        // - before TradeState
        // - PrimitiveInstruction containing a SplitInstruction
        Instruction tradeStateInstruction = Instruction.builder()
                .setBeforeValue(beforeTradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setSplit(splitInstruction));

        // Create a workflow step instruction containing the EventInstruction, EventTimestamp and EventIdentifiers
        return WorkflowStep.builder()
                .setProposedEvent(EventInstruction.builder()
                        .addInstruction(tradeStateInstruction)
                        .setIntent(EventIntentEnum.NOVATION)
                        .setEventDate(eventDate))
                .addTimestamp(EventTimestamp.builder()
                        .setDateTime(ZonedDateTime.of(eventDate.toLocalDate(), LocalTime.of(9, 0), ZoneOffset.UTC.normalized()))
                        .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME))
                .addEventIdentifier(Identifier.builder()
                        .addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierValue("NovationExample")))
                .build(); // ensure you call build() on the function input
    }

    /**
     * Invoke function and post-process result (e.g. qualify etc).
     *
     * @param workflowStepInstruction - WorkflowStep containing a proposed EventInstruction
     * @return Qualified WorkflowStep - containing a BusinessEvent
     */
    private WorkflowStep runFunctionAndPostProcess(WorkflowStep workflowStepInstruction) {
        // Invoke function to create a fully-specified event WorkflowStep (that contains a BusinessEvent)
        WorkflowStep eventWorkflowStep = createWorkflowStep.evaluate(workflowStepInstruction);

        // Post-process the eventWorkflowStep to qualify, re-resolve references etc.
        // This post-process step is optional depending on how you intend to process the workflow step.
        return postProcess(eventWorkflowStep);
    }

    /**
     * Assert the result.
     */
    private void assertWorkflowStep(WorkflowStep eventWorkflowStep) {
        // The fully-specified event WorkflowStep should contain a newly created BusinessEvent
        BusinessEvent businessEvent = eventWorkflowStep.getBusinessEvent();
        assertNotNull(businessEvent);

        // BusinessEvent qualifies as a Novation
        assertEquals("Novation", businessEvent.getEventQualifier());

        // Two after TradeStates expected - i.e. the novated TradeState (i.e. with new party etc), and the terminated original TradeState
        assertEquals(2, businessEvent.getAfter().size());

        // Novated TradeState
        TradeState novatedTradeState = businessEvent.getAfter().get(0);
        assertNull(novatedTradeState.getState());

        // Terminated TradeState
        TradeState terminatedTradeState = businessEvent.getAfter().get(1);
        assertEquals(ClosedStateEnum.TERMINATED, terminatedTradeState.getState().getClosedState().getState());
    }

    private <T extends RosettaModelObject> T postProcess(T o) {
        RosettaModelObjectBuilder builder = o.toBuilder();
        postProcessor.postProcess(builder.getType(), builder);
        return (T) builder;
    }
}
