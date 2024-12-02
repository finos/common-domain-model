package org.finos.cdm.example.qualification;

import cdm.event.common.BusinessEvent;
import cdm.event.common.meta.BusinessEventMeta;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.functions.Create_AcceptedWorkflowStepFromInstruction;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.qualify.QualifyResult;
import com.rosetta.model.lib.qualify.QualifyResultsExtractor;
import org.finos.cdm.example.AbstractExampleTest;
import org.finos.cdm.example.util.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A test suite for validating business event qualification using the Common Domain Model (CDM).
 * This class demonstrates how to extract and qualify events from workflow steps based on CDM samples.
 */
public class QualifyBusinessEventTest extends AbstractExampleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(QualifyBusinessEventTest.class);

    // Function to create a WorkflowStep from an instruction, injecting BusinessEvent into the step.
    @Inject
    Create_AcceptedWorkflowStepFromInstruction createWorkflowStep;

    // Post-processor for refining and qualifying WorkflowStep objects.
    @Inject
    WorkflowPostProcessor postProcessor;

    // Factory to retrieve qualification functions for BusinessEvent objects.
    @Inject
    QualifyFunctionFactory.Default qualifyFunctionFactory;

    /**
     * Validates the qualification of a BusinessEvent derived from a WorkflowStep.
     *
     * @param workflowStepPath Path to the JSON file representing the WorkflowStep.
     * @param expectedQualifier The expected qualification result for the BusinessEvent.
     * @throws IOException If there is an error reading the JSON file or resolving references.
     */
    private void runQualifyEvent(String workflowStepPath, String expectedQualifier) throws IOException {
        // Load the proposed WorkflowStep from the specified JSON file.
        WorkflowStep proposedWorkflowStep = ResourcesUtils.getObjectAndResolveReferences(WorkflowStep.class, workflowStepPath);

        // Transform the proposed WorkflowStep into one containing a BusinessEvent.
        WorkflowStep businessEventWorkflowStep = runFunctionAndPostProcess(proposedWorkflowStep);

        // Extract the BusinessEvent from the processed WorkflowStep.
        BusinessEvent businessEvent = businessEventWorkflowStep.getBusinessEvent();
        assertNotNull(businessEvent, "BusinessEvent must not be null");

        // Retrieve the qualification functions for BusinessEvent objects.
        List<Function<? super BusinessEvent, QualifyResult>> qualifyFunctions =
                new BusinessEventMeta().getQualifyFunctions(qualifyFunctionFactory);

        // Extract the successful qualification result, if any.
        String eventQualifier = new QualifyResultsExtractor<>(qualifyFunctions, businessEvent)
                .getOnlySuccessResult() // Retrieve only one successful result; fail if multiple results exist.
                .map(QualifyResult::getName) // Extract the name of the qualification result.
                .orElse("Failed to qualify"); // Default if no qualification succeeded.

        // Assert that the derived qualifier matches the expected qualifier.
        assertEquals(expectedQualifier, eventQualifier,
                "Event qualifier mismatch: expected '" + expectedQualifier + "', but got '" + eventQualifier + "'");

        LOGGER.info("Qualification Result: " + eventQualifier);
    }

    /**
     * Transforms a WorkflowStep instruction into one containing a BusinessEvent.
     * Optionally post-processes the result for qualification and other refinements.
     *
     * @param workflowStepInstruction Input WorkflowStep instruction.
     * @return A WorkflowStep containing a BusinessEvent.
     */
    private WorkflowStep runFunctionAndPostProcess(WorkflowStep workflowStepInstruction) {
        // Create a new WorkflowStep containing a BusinessEvent.
        WorkflowStep eventWorkflowStep = createWorkflowStep.evaluate(workflowStepInstruction);

        // Post-process the WorkflowStep for validation, re-resolving references, etc.
        return postProcess(eventWorkflowStep);
    }

    /**
     * Post-processes a CDM object (e.g., WorkflowStep) to refine and validate it.
     *
     * @param o A RosettaModelObject to be post-processed.
     * @param <T> The type of the RosettaModelObject.
     * @return The post-processed object.
     */
    private <T extends RosettaModelObject> T postProcess(T o) {
        RosettaModelObjectBuilder builder = o.toBuilder();
        postProcessor.postProcess(builder.getType(), builder); // Apply post-processing logic.
        return (T) builder; // Return the post-processed object.
    }

    // Test cases for qualifying specific events from CDM samples.

    @Test
    void testPartialTermination() throws IOException {
        // Test for qualifying a "PartialTermination" event.
        runQualifyEvent("result-json-files/fpml-5-10/processes/msg-partial-termination.json", "PartialTermination");
    }

    @Test
    void testContractFormation() throws IOException {
        // Test for qualifying a "ContractFormation" event.
        runQualifyEvent("result-json-files/fpml-5-10/processes/msg-ex58-execution-advice-trade-initiation-F01-00.json", "ContractFormation");
    }

    @Test
    void testPartialNovation() throws IOException {
        // Test for qualifying a "PartialNovation" event.
        runQualifyEvent("result-json-files/fpml-5-10/processes/msg-ex52-execution-advice-trade-partial-novation-C02-00.json", "PartialNovation");
    }

    /*
     * Placeholder tests for other events.
     * Add corresponding test data to enable validation for these scenarios:
     * - Execution
     * - Allocation
     * - Clearing
     * - Novation
     * - Amendment
     * - Compression
     * - Reset
     * - Transfer
     * - Increase
     * - Full Termination
     * - Option Exercise
     * - Stock Split
     * - Corporate Action
     * - Credit Event
     */


}
