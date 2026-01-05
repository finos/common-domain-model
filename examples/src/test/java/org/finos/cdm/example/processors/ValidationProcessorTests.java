package org.finos.cdm.example.processors;

import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.validation.ValidationResult;
import org.finos.cdm.example.util.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

final class ValidationProcessorTests extends AbstractProcessorTest {

    private static final Logger logger = LoggerFactory.getLogger(ValidationProcessorTests.class);
    /**
     * This test checks that a valid product represented as a TradeState object validates correctly.
     * The test ensures that the TradeState loaded from a sample JSON file meets
     * the expected validation criteria.
     */
    //@Test
    public void mustValidateValidProduct() {
        // Path to the sample JSON file representing a valid TradeState (Interest Rate FRA)
        String filePath = "ingest/output/fpml-confirmation-to-trade-state/fpml-5-13-products-inflation-swaps/inflation-swap-ex01-yoy.json";

        // Load the TradeState object from the file and resolve any references
        TradeState sample = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, filePath);

        // Ensure the loaded TradeState is not null
        assertNotNull(sample, "The input trade state should not be null");

        // Validate the TradeState and generate a validation report
        ValidationReport report = validate(sample);
        report.logReport();

        // Verify that the qualification result indicates success
        assertTrue(report.success(), "The validation result should indicate success");

        // Verify that all validation results indicate success
        assertTrue(report.getValidationResults().stream().allMatch(ValidationResult::isSuccess), "All validation results should indicate success");

        // Verify that the qualified object type is as expected
        assertEquals(report.getResultObject().getType(),TradeState.class, "The validation result object type should match the expected type");
    }

    /**
     * This test checks that an invalid product, represented as a TradeState object with
     * missing or incorrect data, fails validation.
     */
    @Test
    public void mustNotValidateInvalidProduct() {
        // Create an invalid TradeState (missing required fields or incorrect data)
        String filePath = "ingest/output/fpml-confirmation-to-trade-state/fpml-5-13-products-interest-rate-derivatives/ird-ex08-fra.json";

        // Load the TradeState object from the file and resolve any references
        TradeState sample = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, filePath);

        // Validate the invalid TradeState and generate a validation report
        ValidationReport report = validate(sample);
        report.logReport();

        // Verify that the validation process did not succeed
        assertFalse(report.success(), "The validation report should indicate failure for an invalid product");

        // Verify that the report contains validation results indicating errors
        assertFalse(report.getValidationResults().isEmpty(), "The validation results should not be empty for an invalid product");

        // Verify that at least one validation result indicates failure
        assertFalse(report.getValidationResults().stream().allMatch(result -> result.isSuccess()), "At least one validation result should indicate failure");

        // Verify that the result object type is as expected
        assertEquals(report.getResultObject().getType(), TradeState.class, "The validation result object type should still match the expected type");
    }

    /**
     * This test checks that a valid WorkflowStep passes all validation rules.
     * It uses a sample JSON file to load a valid TradeState, wraps it into a WorkflowStep,
     * and ensures that the validation process succeeds with no errors.
     */
    //@Test
    public void mustValidateValidWorkflowStep() {
        // Path to the sample JSON file representing a valid WorkflowStep
        String filePath = "ingest/output/fpml-confirmation-to-workflow-step/fpml-5-13-processes-execution-advice/msg-ex52-execution-advice-trade-partial-novation-C02-00.json";

        // Load the WorkflowStep object from the file
        WorkflowStep sample = ResourcesUtils.getObjectAndResolveReferences(WorkflowStep.class, filePath);

        // Validate the WorkflowStep and generate a validation report
        ValidationReport report = validate(sample);
        report.logReport();

        // Verify that the validation process succeeded
        assertTrue(report.success(), "The validation report should indicate success");

        // Verify that all validation results indicate success
        assertTrue(report.getValidationResults().stream().allMatch(ValidationResult::isSuccess), "All validation results should indicate success");

        // Verify that the validated object type is as expected
        assertEquals(report.getResultObject().getType(), WorkflowStep.class, "The validated object type should match the expected type");
    }

    /**
     * This test checks that an invalid WorkflowStep fails validation.
     * It uses an empty WorkflowStep, then verifies that the validation
     * process detects errors and does not succeed.
     */
    @Test
    public void mustNotValidateEmptyWorkflowStep() {

        // Attempt to create an accepted WorkflowStep using the invalid TradeState
        WorkflowStep ws = WorkflowStep.builder().build();

        // Validate the WorkflowStep and generate a validation report
        ValidationReport report = validate(ws);
        report.logReport();

        // Verify that the validation process did not succeed
        assertFalse(report.success(), "The validation report should indicate failure");

        // Ensure that there are validation errors in the report
        assertFalse(report.getValidationResults().isEmpty(), "The validation results should not be empty for an invalid WorkflowStep");

        // Verify that at least one qualification result indicates failure
        assertFalse(report.getValidationResults().stream().allMatch(ValidationResult::isSuccess), "At least one validation result should indicate failure");

        // Verify that the invalid object type is still WorkflowStep
        assertEquals(report.getResultObject().getType(), WorkflowStep.class, "The validated object type should still be WorkflowStep");
    }

    /**
     * This test ensures that attempting to validate a null WorkflowStep
     * fails. It verifies that the system handles null inputs without
     * crashing, by throwing a NullPointerException.
     */
    @Test
    public void mustNotValidateNullWorkflowStep() {
        try {
            // Validate the null WorkflowStep (this should throw an exception or fail)
            ValidationReport report = validate(null);

            // If no exception is thrown, fail the test explicitly
            fail("Validation of a null WorkflowStep should throw an exception or fail validation.");
        } catch (NullPointerException e) {
            // Verify that a NullPointerException is thrown
            logger.info("NullPointerException caught as expected: " + e.getMessage());
            logger.info("NullPointerException was correctly thrown for a null WorkflowStep.");
        } catch (Exception e) {
            // Catch any other exceptions and fail the test if unexpected
            fail("An unexpected exception was thrown: " + e.getMessage());
        }
    }
}
