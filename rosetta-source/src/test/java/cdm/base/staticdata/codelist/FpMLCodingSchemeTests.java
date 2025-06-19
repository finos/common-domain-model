package cdm.base.staticdata.codelist;

import cdm.base.datetime.BusinessCenterTime;
import cdm.event.common.TradeState;
import cdm.product.template.EconomicTerms;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FpML (Financial products Markup Language) coding schemes validation.
 * This class tests whether reference data codes conform to expected validation rules.
 */
final class FpMLCodingSchemeTests {

    // Logger for recording test results and validation reports
    private static final Logger logger = LoggerFactory.getLogger(FpMLCodingSchemeTests.class);
    private static Injector injector;

    @Inject
    RosettaTypeValidator validator;

    private static ObjectMapper mapper;

    /**
     * Sets up shared resources before running any test cases.
     * Initializes the ObjectMapper for JSON processing and the dependency injection container.
     */
    @BeforeAll
    public static void setUpOnce() {
        mapper = RosettaObjectMapper.getNewRosettaObjectMapper(); // Create a new Rosetta-specific ObjectMapper
        injector = Guice.createInjector(new CdmRuntimeModule()); // Initialize Guice injector with CDM runtime module
    }

    /**
     * Sets up dependencies before each test.
     * Injects required members using Guice.
     */
    @BeforeEach
    public void setUp() {
        injector.injectMembers(this); // Inject dependencies into the test instance
    }

    /**
     * Test case to verify that an invalid business center code ("DUMMY") fails validation.
     * Ensures that the validation correctly detects a TYPE_FORMAT failure with a specific error message.
     */
    @Test
    void mustFailValidatingBusinessCenterCodingScheme() {
        // Create a BusinessCenterTime object with an invalid business center code ("DUMMY")
        BusinessCenterTime bct = BusinessCenterTime.builder()
                .setBusinessCenter(FieldWithMetaString.builder()
                        .setValue("DUMMY") // Invalid business center code
                        .build())
                .build();

        // Run the validation process on the created object
        ValidationReport report = validator.runProcessStep(BusinessCenterTime.class, bct);

        // Log all validation results
        report.getValidationResults().stream()
                .map(ValidationResult::toString)
                .forEach(logger::info);

        // Assert that exactly one TYPE_FORMAT validation failure occurs
        assertEquals(1, report.getValidationResults().stream()
                .filter(it -> it.getValidationType() == ValidationResult.ValidationType.DATA_RULE)
                .count(), "A single DATA_RULE failure is expected");

        // Assert that the validation failure message matches the expected error
        assertTrue(report.getValidationResults().stream()
                        .filter(it -> it.getValidationType() == ValidationResult.ValidationType.DATA_RULE)
                        .findFirst()
                        .flatMap(ValidationResult::getFailureReason)
                        .map(reason -> reason.equalsIgnoreCase("Condition has failed."))
                        .orElse(false),
                "The expected DATA_RULE rule must fail with a specific message");
    }

    /**
     * Test case to validate reference data codes in a sample FpML trade state.
     * Reads a sample JSON file, parses it into a TradeState object, and validates its economic terms.
     *
     * @throws IOException If an error occurs while reading the JSON file.
     */
    @Disabled
    @Test
    void mustValidateCodesInTradeStateSample() throws IOException {
        // Load the sample JSON file containing a trade state definition
        final URL source = FpMLCodingSchemeTests.class.getClassLoader()
                .getResource("result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex01-vanilla-swap.json");

        // Ensure that the file was found
        assertNotNull(source, "Test JSON file must be available");

        // Deserialize the JSON file into a TradeState object
        TradeState tradeState = mapper.readValue(source, TradeState.class);

        // Ensure that the TradeState object was properly deserialized
        assertNotNull(tradeState, "TradeState object must not be null");

        // Validate the economic terms of the trade state
        ValidationReport report = validator.runProcessStep(EconomicTerms.class,
                tradeState.getTrade().getProduct().getEconomicTerms());

        // Log validation failures (if any)
        report.getValidationResults().stream()
                .filter(it -> !it.isSuccess())
                .map(ValidationResult::toString)
                .forEach(logger::info);
    }
}

