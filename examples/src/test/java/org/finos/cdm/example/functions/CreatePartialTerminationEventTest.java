package org.finos.cdm.example.functions;

import cdm.base.datetime.AdjustableOrAdjustedOrRelativeDate;
import cdm.base.math.NonNegativeQuantity;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.PartyReferencePayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.workflow.EventInstruction;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.functions.Create_AcceptedWorkflowStepFromInstruction;
import cdm.observable.asset.FeeTypeEnum;
import cdm.observable.asset.PriceQuantity;
import jakarta.inject.Inject;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test demonstrates how to create a WorkflowStep from an input TradeState and Instructions.
 */
public class CreatePartialTerminationEventTest extends AbstractExampleTest {

    private static final String CURRENCY_SCHEME = "http://www.fpml.org/coding-scheme/external/iso4217";

    @Inject
    Create_AcceptedWorkflowStepFromInstruction createWorkflowStep;

    @Inject
    WorkflowPostProcessor postProcessor;


    @Test
    void createPartialTerminationEvent() throws IOException {
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
        // Trade to be partially terminated.  Note that all references are resolved here.
        TradeState beforeTradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, "result-json-files/fpml-5-10/products/rates/USD-Vanilla-swap.json");

        Date eventDate = Date.of(2013, 2, 12);

        // QuantityChangeInstruction specifying a decrease in notional
        QuantityChangeInstruction quantityChangeInstruction =
                QuantityChangeInstruction.builder()
                        .setDirection(QuantityChangeDirectionEnum.DECREASE)
                        .addChange(PriceQuantity.builder()
                                .addQuantityValue(NonNegativeQuantitySchedule.builder()
                                                .setValue(BigDecimal.valueOf(7000000))
                                                .setUnit(UnitType.builder()
                                                        .setCurrency(FieldWithMetaString.builder()
                                                                .setValue("USD")
                                                                .setMeta(MetaFields.builder().setScheme(CURRENCY_SCHEME))))));

        // Transfer instruction specifying the partial termination fee
        ReferenceWithMetaParty payerPartyReference = beforeTradeState.getTrade().getCounterparty().get(0).getPartyReference();
        ReferenceWithMetaParty receiverPartyReference = beforeTradeState.getTrade().getCounterparty().get(1).getPartyReference();
        TransferInstruction transferInstruction = TransferInstruction.builder()
                .addTransferState(TransferState.builder()
                        .setTransfer(Transfer.builder()
                                .setTransferExpression(TransferExpression.builder()
                                        .setPriceTransfer(FeeTypeEnum.PARTIAL_TERMINATION))
                                .setPayerReceiver(PartyReferencePayerReceiver.builder()
                                        .setPayerPartyReference(payerPartyReference)
                                        .setReceiverPartyReference(receiverPartyReference))
                                .setQuantity(NonNegativeQuantity.builder()
                                        .setValue(BigDecimal.valueOf(2000.00))
                                        .setUnit(UnitType.builder()
                                                .setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder().setScheme(CURRENCY_SCHEME)))))
                                .setSettlementDate(AdjustableOrAdjustedOrRelativeDate.builder()
                                        .setAdjustedDateValue(eventDate))));

        // Create an Instruction that contains:
        // - before TradeState
        // - PrimitiveInstruction containing a QuantityChangeInstruction and TransferInstruction
        Instruction tradeStateInstruction = Instruction.builder()
                .setBeforeValue(beforeTradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstruction)
                        .setTransfer(transferInstruction));

        // Create a workflow step instruction containing the EventInstruction, EventTimestamp and EventIdentifiers
        return WorkflowStep.builder()
                .setProposedEvent(EventInstruction.builder()
                        .addInstruction(tradeStateInstruction)
                        .setEventDate(eventDate))
                .addTimestamp(EventTimestamp.builder()
                        .setDateTime(ZonedDateTime.of(eventDate.toLocalDate(), LocalTime.of(9, 0), ZoneOffset.UTC.normalized()))
                        .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME))
                .addEventIdentifier(Identifier.builder()
                        .addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierValue("PartialTerminationExample")))
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

        // BusinessEvent qualifies as a PartialTermination
        assertEquals("PartialTermination", businessEvent.getEventQualifier());

        // Only one after TradeState expected - i.e. the TradeState with the new decreased notional
        assertEquals(1, businessEvent.getAfter().size());
        TradeState afterTradeState = businessEvent.getAfter().get(0);

        // Assert new decreased notional
        NonNegativeQuantitySchedule quantity = afterTradeState.getTrade()
                .getTradeLot().get(0)
                .getPriceQuantity().get(0)
                .getQuantity().get(0).getValue();
        assertEquals(new BigDecimal("3000000.00"), quantity.getValue());

        // Only one transferHistory expected - i.e. the partial termination fee
        assertEquals(1, afterTradeState.getTransferHistory().size());

        // Assert transfer fee
        Transfer transfer = afterTradeState.getTransferHistory().get(0).getTransfer();
        assertEquals(FeeTypeEnum.PARTIAL_TERMINATION, transfer.getTransferExpression().getPriceTransfer());
        assertEquals(new BigDecimal("2000.0"), transfer.getQuantity().getValue());
    }

    private <T extends RosettaModelObject> T postProcess(T o) {
        RosettaModelObjectBuilder builder = o.toBuilder();
        postProcessor.postProcess(builder.getType(), builder);
        return (T) builder;
    }
}
