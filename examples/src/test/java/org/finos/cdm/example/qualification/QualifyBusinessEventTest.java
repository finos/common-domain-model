package org.finos.cdm.example.qualification;

import cdm.event.common.BusinessEvent;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.rosetta.model.lib.qualify.QualifyResult;
import org.finos.cdm.example.processors.AbstractProcessorTest;
import org.finos.cdm.example.util.ResourcesUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A test suite for validating business event qualification using the Common Domain Model (CDM).
 * This class demonstrates how to extract and qualify events from workflow steps based on CDM samples.
 */
final class QualifyBusinessEventTest extends AbstractProcessorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualifyBusinessEventTest.class);

    /**
     * Validates the qualification of a BusinessEvent derived from a WorkflowStep in a JSON file.
     * Loads a WorkflowStep from JSON, converts it to a BusinessEvent, qualifies and validates it.
     *
     * @param workflowStepPath Path to the JSON file representing the WorkflowStep.
     * @param expectedQualifier The expected qualification result for the BusinessEvent.
     * @throws IOException If there is an error reading the JSON file or resolving references.
     */
    private void runQualifyEventFromWorkflowStep(String workflowStepPath, String expectedQualifier) throws IOException {
        // Load the proposed WorkflowStep from the specified JSON file.
        WorkflowStep proposedWorkflowStep = ResourcesUtils.getObjectAndResolveReferences(WorkflowStep.class, workflowStepPath);

        // Transform the proposed WorkflowStep into one containing a BusinessEvent.
        WorkflowStep businessEventWorkflowStep = createAcceptedWorkflowStepFromInstructionFunc.evaluate(proposedWorkflowStep);

        // Extract the BusinessEvent from the processed WorkflowStep.
        BusinessEvent businessEvent = businessEventWorkflowStep.getBusinessEvent();
        assertNotNull(businessEvent, "BusinessEvent must not be null");

        // Run the qualification functions for BusinessEvent objects.
        qualifyBusinessEvent(businessEvent, expectedQualifier);
    }

    /**
     * Validates the qualification of a BusinessEvent derived from a BusinessEvent in a JSON file.
     *
     * @param businessEventPath Path to the JSON file representing the BusinessEvent.
     * @param expectedQualifier The expected qualification result for the BusinessEvent.
     * @throws IOException If there is an error reading the JSON file or resolving references.
     */
    private void runQualifyEventFromBusinessEvent(String businessEventPath, String expectedQualifier) throws IOException {
        // Load the proposed BusinessEvent from the specified JSON file.
        BusinessEvent businessEvent = ResourcesUtils.getObjectAndResolveReferences(BusinessEvent.class, businessEventPath);
        assertNotNull(businessEvent, "BusinessEvent must not be null");

        qualifyBusinessEvent(businessEvent, expectedQualifier);
    }

    /**
     * Validates the qualification of an already instantiated (deserialized) BusinessEvent.
     *
     * @param businessEvent Path to the JSON file representing the BusinessEvent.
     * @param expectedQualifier The expected qualification result for the BusinessEvent.
     * @throws IOException If qualification fails.
     */
    private void qualifyBusinessEvent(BusinessEvent businessEvent, String expectedQualifier) throws IOException{
        // Retrieve the qualification functions for BusinessEvent objects.
        QualificationReport qualificationReport = qualify(businessEvent.toBuilder());

        // Extract the successful qualification result, if any.
        //The expected number of uniquely qualified objects for a WorkflowStep(businessEvent) is 3
        LOGGER.info("Qualified/Uniquely qualified objects count: {}/{}",
                qualificationReport.getQualifiableObjectsCount(),
                qualificationReport.getUniquelyQualifiedObjectsCount()
        );

        //Store only event qualifier results -> type = BusinessEvent
        List<String> eventQualifiers = new ArrayList<>();

        //Walk through multiple qualified objects
        qualificationReport.getResults().forEach(it -> {

            final String eventQualifier = it.getUniqueSuccessQualifyResult().map(QualifyResult::getName).orElse("Failed to qualify");
            final String qualifierType = it.getQualifiedRosettaObjectType().getSimpleName();
            LOGGER.info("** Qualification Result **");
            LOGGER.info("Success={}; Type={}; Qualification={}",
                    it.isSuccess(),
                    qualifierType,
                    eventQualifier
            );

            if (qualifierType.equalsIgnoreCase(BusinessEvent.class.getSimpleName()))
                eventQualifiers.add(eventQualifier);
        });

        //Assert only 1 eventQualifier is found : e.g eventQualifiers.size() == 1

        // Assert that the derived qualifier matches the expected qualifier.
        assertEquals(expectedQualifier, eventQualifiers.stream().findFirst().orElse("Failed to qualify"),
                "Event qualifier mismatch: expected '" + expectedQualifier + "', but got '" + eventQualifiers.stream().findFirst().orElse("Failed to qualify") + "'");

        LOGGER.info("Qualification Result: {}", eventQualifiers.stream().findFirst().orElse("Failed to qualify"));
    }

    // Test cases for qualifying specific events from CDM samples resulting from the ingestion.
    @Test
    @Disabled // disabled until remaining FpML function ingestion mapping task is completed, see https://github.com/finos/common-domain-model/issues/4030
    void testPartialTermination() throws IOException {
        // Test for qualifying a "PartialTermination" event.
        runQualifyEventFromWorkflowStep("ingest/output/fpml-confirmation-to-workflow-step/fpml-5-10-processes/msg-partial-termination.json", "PartialTermination");
    }

    @Test
    void testContractFormation() throws IOException {
        // Test for qualifying a "ContractFormation" event.
        runQualifyEventFromWorkflowStep("ingest/output/fpml-confirmation-to-workflow-step/fpml-5-10-processes/msg-ex58-execution-advice-trade-initiation-F01-00.json", "ContractFormation");
    }

    @Test
    @Disabled // disabled until remaining FpML function ingestion mapping task is completed, see https://github.com/finos/common-domain-model/issues/4030
    void testPartialNovation() throws IOException {
        // Test for qualifying a "PartialNovation" event.
        runQualifyEventFromWorkflowStep("ingest/output/fpml-confirmation-to-workflow-step/fpml-5-10-processes/msg-ex52-execution-advice-trade-partial-novation-C02-00.json", "PartialNovation");
    }

    // Test cases for qualifying specific events from CDM-native samples.
    @Test
    void testAllocation() throws IOException {
        // Test for qualifying a "Allocation" event.
        runQualifyEventFromBusinessEvent("functions/business-event/allocation/allocation-func-output.json", "Allocation");
    }

    @Test
    void testClearing() throws IOException {
        // Test for qualifying a "ClearedTrade" event.
        runQualifyEventFromBusinessEvent("functions/business-event/clearing/clearing-func-output.json", "ClearedTrade");
    }

    @Test
    void testCompression() throws IOException {
        // Test for qualifying a "Compression" event.
        runQualifyEventFromBusinessEvent("functions/business-event/compression/compression-func-output.json", "Compression");
    }

    @Test
    void testCorporateAction() throws IOException {
        // Test for qualifying a "CorporateActionDetermined" event.
        runQualifyEventFromBusinessEvent("functions/business-event/corporate-actions/corporate-actions-func-output.json", "CorporateActionDetermined");
    }

    @Test
    void testCreditEvent() throws IOException {
        // Test for qualifying a "CreditEventDetermined" event.
        runQualifyEventFromBusinessEvent("functions/business-event/credit-event/credit-event-func-output.json", "CreditEventDetermined");
    }

    @Test
    void testExecution() throws IOException {
        // Test for qualifying a "Execution" event.
        runQualifyEventFromBusinessEvent("functions/business-event/execution/execution-basis-swap-func-output.json", "Execution");
    }

    @Test
    void testExercise() throws IOException {
        // Test for qualifying a "Exercise" event.
        runQualifyEventFromBusinessEvent("functions/business-event/exercise/exercise-cancellable-option-func-output.json", "Exercise");
    }

    @Test
    void testNovation() throws IOException {
        // Test for qualifying a "Novation" event.
        runQualifyEventFromBusinessEvent("functions/business-event/novation/full-novation-func-output.json", "Novation");
    }

    @Test
    void testIndexTransition() throws IOException {
        // Test for qualifying a "IndexTransition" event.
        runQualifyEventFromBusinessEvent("functions/business-event/index-transition/index-transition-vanilla-swap-func-output.json", "IndexTransition");
    }

    @Test
    @Disabled // disabled until remaining FpML function ingestion mapping task is completed, see https://github.com/finos/common-domain-model/issues/4030
    void testTermination() throws IOException {
        // Test for qualifying a "Termination" event.
        runQualifyEventFromBusinessEvent("functions/business-event/quantity-change/full-termination-equity-swap-func-output.json", "Termination");
    }

    @Test
    void testIncrease() throws IOException {
        // Test for qualifying a "Increase" event.
        runQualifyEventFromBusinessEvent("functions/business-event/quantity-change/increase-equity-swap-func-output.json", "Increase");
    }

    @Test
    @Disabled // disabled until remaining FpML function ingestion mapping task is completed, see https://github.com/finos/common-domain-model/issues/4030
    void testStockSplit() throws IOException {
        // Test for qualifying a "StockSplit" event.
        runQualifyEventFromBusinessEvent("functions/business-event/stock-split/stock-split-equity-swap-func-output.json", "StockSplit");
    }

    /*
     * Placeholder tests for other events.
     * Add corresponding test data to enable validation for these scenarios:
     * - Reset
     * - Transfer
     */
}
