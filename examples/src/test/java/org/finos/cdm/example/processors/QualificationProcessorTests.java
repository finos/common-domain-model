package org.finos.cdm.example.processors;

import cdm.event.common.BusinessEvent;
import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationResult;
import com.rosetta.model.lib.qualify.QualifyResult;
import org.finos.cdm.example.util.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

final class QualificationProcessorTests extends AbstractProcessorTest {

    private static final Logger logger = LoggerFactory.getLogger(QualificationProcessorTests.class);

    /**
     * This test verifies that a valid TradeState qualifies correctly as a valid product.
     * The test ensures that the QualificationReport correctly identifies the TradeState,
     * assigns the expected label, and matches the expected structure and taxonomy.
     */
    @Test
    public void mustQualifyValidProduct() {
        // Path to the sample JSON file representing a valid TradeState (Interest Rate FRA)
        String filePath = "ingest/output/fpml-confirmation-to-trade-state/fpml-5-13-products-interest-rate-derivatives/ird-ex08-fra.json";

        // Load the TradeState object from the file and resolve any references
        TradeState sample = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, filePath);

        // Ensure the loaded TradeState is not null
        assertNotNull(sample, "The input trade state should not be null");

        // Qualify the TradeState and generate a qualification report
        QualificationReport report = qualify(sample.toBuilder());

        // Verify that exactly one object is uniquely qualified
        assertEquals(1, report.getUniquelyQualifiedObjectsCount(), "Should have uniquely qualified object");

        // Verify that the product taxonomy contains the expected label
        //assertEquals(((TradeState) (report.getResultObject().build())).getTrade().getProduct().getTaxonomy().stream().map(ProductTaxonomy::getProductQualifier).filter(it -> it.equalsIgnoreCase(expectedLabel)).findFirst().orElse(""), expectedLabel, "This test should have retrieved the expected label");

        // Verify that there is exactly one result in the qualification report
        assertEquals(1, report.getResults().size(), "There should be exactly one result in the qualification report");

        // Verify that the qualification result indicates success
        assertTrue(report.getResults().iterator().next().isSuccess(), "The qualification result should indicate success");

        // Verify that the result object name matches the expected label
        //assertEquals(report.getResults().iterator().next().getUniqueSuccessQualifyResult().get().getName(), expectedLabel, "The qualified result should match the expected label");

        // Verify that the qualified object type is as expected
        assertEquals(report.getResultObject().getClass(),TradeState.builder().getClass(), "The qualified object type should match the expected type");
    }

    /**
     * This test checks that an invalid TradeState does not qualify as a valid product.
     * The function also validates that the qualification report is either empty or
     * consistent with the expected behavior for invalid inputs.
     */
    @Test
    public void mustNotQualifyEmptyProduct() {

        // Create an invalid TradeState (empty object)
        TradeState emptyTradeState = TradeState.builder().build();

        // Attempt to qualify the invalid TradeState
        QualificationReport report = qualify(emptyTradeState.toBuilder());

        // Verify that the qualification report is not null, even if the TradeState is invalid.
        assertNotNull(report, "The report should not be null even for an invalid TradeState");

        // Verify that no objects were uniquely qualified since the TradeState is invalid.
        assertEquals(0, report.getUniquelyQualifiedObjectsCount(), "No uniquely qualified objects should exist for an invalid TradeState");

        // Verify that the results list in the report is empty, as no qualification should have occurred.
        assertTrue(report.getResults().isEmpty(), "The results list should be empty for an invalid TradeState");

        // Verify that the type of the result object in the report matches the expected TradeState type.
        assertEquals(report.getResultObject().getClass(), TradeState.builder().getClass(), "The qualified object type should match the expected type");
    }

    /**
     * This test checks that a null TradeState does not qualify as a valid product.
     * It handles exceptions using try and catch, ensuring the test does not fail abruptly but
     * logs any errors. The function also validates that the qualification report is either empty
     * or consistent with the expected behavior for invalid inputs.
     */
    @Test
    public void mustNotQualifyNullProduct() {
        QualificationReport report = null;        // Initialize the report outside the try block
        Exception caughtException = null;         // Variable to capture any exceptions that occur

        try {
            // Attempt to qualify the null TradeState
            report = qualify(null);

            // Verify that the qualification report is not null, even if the TradeState is invalid.
            assertNotNull(report, "The report should not be null even for an invalid TradeState");

            // Verify that no objects were uniquely qualified since the TradeState is invalid.
            assertEquals(0, report.getUniquelyQualifiedObjectsCount(), "No uniquely qualified objects should exist for an invalid TradeState");

            // Verify that the results list in the report is empty, as no qualification should have occurred.
            assertTrue(report.getResults().isEmpty(), "The results list should be empty for an invalid TradeState");

            // Verify that the type of the result object in the report matches the expected TradeState type.
            assertEquals(report.getResultObject().getClass(), TradeState.builder().getClass(), "The qualified object type should match the expected type");


        } catch (Exception e) {
            // If an exception occurs, capture and log it
            caughtException = e;
            logger.info("Exception occurred while qualifying an invalid TradeState", e);
        }

        // Final checks to determine if the test passes
        if (caughtException != null) {
            // Log that an expected exception was caught
            logger.info("An expected exception was caught for an invalid TradeState: " + caughtException.getMessage());
        } else if (report.getUniquelyQualifiedObjectsCount() == 0) {
            // Log that the invalid TradeState was handled correctly
            logger.info("The TradeState was invalid, and no objects were uniquely qualified as expected.");
        } else {
            // Fail the test if unexpected behavior occurs (e.g., the invalid TradeState qualified)
            fail("Unexpected behavior: The invalid TradeState qualified or no exception was thrown.");
        }
    }

    /**
     * This test check that a valid WorkflowStep qualifies correctly.
     * It uses a sample JSON file to load a valid TradeState, which is then wrapped
     * into a WorkflowStep. The test verifies that the qualification process
     * succeeds, and the resulting qualified object matches the expected type.
     */
    @Test
    public void mustQualifyValidWorkflowStep() {
        // Path to the sample JSON file representing a valid WorkflowStep
        String filePath = "ingest/output/fpml-confirmation-to-workflow-step/fpml-5-13-processes-execution-advice/msg-ex52-execution-advice-trade-partial-novation-C02-00.json";

        WorkflowStep sample = ResourcesUtils.getObjectAndResolveReferences(WorkflowStep.class, filePath);

        WorkflowStep acceptedWorkflow = createAcceptedWorkflowStepFromInstructionFunc.evaluate(sample);

        // Qualify the TradeState and generate a qualification report
        QualificationReport report = qualify(acceptedWorkflow.toBuilder());

        report.getResults().forEach(result -> {
            logger.debug("Qualified Object Type: " + result.getQualifiedRosettaObjectType());
            logger.debug("Qualified Object Details: " + result.getUniqueSuccessQualifyResult().orElse(null));
            logger.debug("Qualified Object Name: " + result.getUniqueSuccessQualifyResult().map(QualifyResult::getName).orElse("Unknown"));
            logger.debug("Is Success: " + result.isSuccess());
        });

        // Verify that the product taxonomy contains the expected label
        //assertEquals(((WorkflowStep) (report.getResultObject().build())).getBusinessEvent().getAfter().get(0).getTrade().getProduct().getTaxonomy().stream().map(ProductTaxonomy::getProductQualifier).filter(it -> it.equalsIgnoreCase(expectedLabel)).findFirst().orElse(""), expectedLabel, "This test should have retrieved the expected label");

        // Verify that there is exactly three results in the qualification report
        assertEquals(4, report.getResults().size(), "There should be exactly three results in the qualification report");

        // Verify that the qualification results indicate success
        assertTrue(report.getResults().stream().map(QualificationResult::isSuccess).allMatch(Predicate.isEqual(true)), "The qualification result should indicate success");

        // Verify that there is exactly one Business Event qualified
        assertEquals(1, report.getResults().stream().map(QualificationResult::getQualifiedRosettaObjectType).filter(reportType -> reportType.getName() == "cdm.event.common.BusinessEvent").count(), "There must be one Business event in the qualification results.");

        // Verify that there are exactly two Economic Terms qualified
        assertEquals(2, report.getResults().stream().map(QualificationResult::getQualifiedRosettaObjectType).filter(reportType -> reportType.getName() == "cdm.product.template.EconomicTerms").count(), "There must be two Economic Terms in the qualification results.");

        // Verify that the qualified object type is as expected
        assertEquals(report.getResultObject().getClass(),WorkflowStep.builder().getClass(), "The qualified object type should match the expected type");
    }

    /**
     * This test checks that an invalid WorkflowStep does not qualify any objects.
     * It ensures the qualification report behaves as expected (e.g., no uniquely qualified objects).
     */
    @Test
    public void mustNotQualifyInvalidWorkflowStep() {
        // Create an invalid Workflow with an empty or improperly configured WorkflowStep (empty "after" list makes it invalid)
        WorkflowStep invalidWorkflowStep = WorkflowStep.builder().setBusinessEvent(BusinessEvent.builder().setAfter(List.of()).build()).build(); //TradeState.builder().build()

        // Attempt to qualify the invalid Workflow
        QualificationReport report = qualify(invalidWorkflowStep.toBuilder());

        report.getResults().forEach(result -> {
            logger.debug("Qualified Object Type: " + result.getQualifiedRosettaObjectType());
            logger.debug("Qualified Object Details: " + result.getUniqueSuccessQualifyResult().orElse(null));
            logger.debug("Qualified Object Name: " + result.getUniqueSuccessQualifyResult().map(QualifyResult::getName).orElse("Unknown"));
            logger.debug("Is Success: " + result.isSuccess());
        });

        // Verify that the qualification report is not null, even if the WorkflowStep is invalid.
        assertNotNull(report, "The qualification report should not be null even for an invalid WorkflowStep");

        // Verify that no objects were uniquely qualified since the WorkflowStep is invalid.
        assertEquals(0, report.getUniquelyQualifiedObjectsCount(), "No uniquely qualified objects should exist for an invalid WorkflowStep");

        // Verify that at least one qualification result indicates failure
        assertFalse(report.getResults().stream().allMatch(QualificationResult::isSuccess), "At least one validation result should indicate failure");

        // Verify that there is exactly one result in the qualification report
        assertEquals(1, report.getResults().size(), "There should be exactly one result in the qualification report");

        // Verify that the qualified object type is as expected (should remain null or invalid)
        if (report.getResultObject() != null) {
            assertEquals(report.getResultObject().getClass(), WorkflowStep.builder().getClass(), "The qualified object type should match the WorkflowStep type");
        }
    }
}
