package org.finos.cdm.example.qualification;

import cdm.event.common.TradeState;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.rosetta.model.lib.qualify.QualifyResult;
import org.finos.cdm.example.processors.AbstractProcessorTest;
import org.finos.cdm.example.util.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test suite to validate the qualification of financial products using the Common Domain Model (CDM).
 * Each test case verifies that a specific product type is correctly qualified based on its economic terms.
 */
final class QualifyProductTest extends AbstractProcessorTest {

    // Logger instance for debugging and reporting metrics
    private static final Logger LOGGER = LoggerFactory.getLogger(QualifyProductTest.class);

    /**
     * Qualifies a product using the CDM framework and verifies the qualification result.
     *
     * @param filePath      Path to the CDM sample file representing the product.
     * @param expectedLabel The expected qualification label for the product (e.g., ProductQualifier).
     * @return The actual qualification result label from the qualification report.
     */
    private String qualifyProduct (String filePath, String expectedLabel) {
        return qualifyProduct(filePath, expectedLabel, 1);
    }

    /**
     *
     * @param filePath      Path to the CDM sample file representing the product.
     * @param expectedLabel The expected qualification label for the product (e.g., ProductQualifier).
     * @param expectedProductCount The number of product qualified objects (EconomicTermsMeta)
     * @return
     */
    private String qualifyProduct(String filePath, String expectedLabel, int expectedProductCount) {
        // Load and resolve references for the TradeState object from the CDM sample file
        TradeState tradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, filePath);

        // Ensure the TradeState is not null
        assertNotNull(tradeState, "TradeState must not be null");

        // Use QualifyProcessorStep to qualify the TradeState object
        QualificationReport report = qualify(tradeState.toBuilder());

        // Log the results for debugging purposes
        report.getResults().forEach(result -> {
            LOGGER.debug("Qualified Object Type: {}", result.getQualifiedRosettaObjectType());
            LOGGER.debug("Qualified Object Details: {}", result.getUniqueSuccessQualifyResult().orElse(null));
            LOGGER.debug("Qualified Object Name: {}", result.getUniqueSuccessQualifyResult().map(QualifyResult::getName).orElse("Unknown"));
            LOGGER.debug("Is Success: {}", result.isSuccess());
        });

        // Verify the qualification was successful and uniquely qualified
        assertEquals(expectedProductCount, report.getUniquelyQualifiedObjectsCount(), "Should have uniquely qualified object");

        // Extract the unique qualification result name
        String qualificationResult = report.getResults().iterator().next().getUniqueSuccessQualifyResult()
                .map(QualifyResult::getName)
                .stream().findFirst()
                .orElse("Failed to qualify");

        // Assert that the result matches the expected label
        assertTrue(qualificationResult.matches(expectedLabel), "Qualification label should match");
        //assertEquals(expectedLabel, qualificationResult);
        return qualificationResult;
    }

    // Additional test methods for other product types can follow the same pattern...

    @Test
    void mustQualifyIRFra() {
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex08-fra.json";
        String expectedLabel = "InterestRate_Fra";

        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRSwaption() {
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex15-amer-swaption.json";
        String expectedLabel = "InterestRate_Option_Swaption|InterestRate_IRSwap_FixedFloat";
        final int expectedProductCount = 2;

        String qualificationResult = qualifyProduct(filePath, expectedLabel, expectedProductCount);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRCrossCurrency() {
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex06-xccy-swap.json";
        String expectedLabel = "InterestRate_CrossCurrency_FixedFloat";

        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRCap() {
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex22-cap.json";
        String expectedLabel = "InterestRate_CapFloor";

        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRFloor() {
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex23-floor.json";
        String expectedLabel = "InterestRate_CapFloor";

        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRFixedFloatSwap() {
        // Test qualification for an Interest Rate Fixed-Float Swap.
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex01-vanilla-swap.json";
        String expectedLabel = "InterestRate_IRSwap_FixedFloat";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyCRSingleNameCDS() {
        // Test qualification for a Credit Default Swap on a Single Name.
        String filePath = "result-json-files/fpml-5-13/products/credit-derivatives/cd-ex03-long-aussie-corp-fixreg.json";
        String expectedLabel = "CreditDefaultSwap_SingleName";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyCRCreditIndex() {
        // Test qualification for a Credit Default Swap on an Index.
        String filePath = "result-json-files/fpml-5-13/products/credit-derivatives/cdindex-ex01-cdx.json";
        String expectedLabel = "CreditDefaultSwap_Index";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyCRBasket() {
        // Test qualification for a Credit Default Swap on a Basket.
        String filePath = "result-json-files/fpml-5-13/products/credit-derivatives/cds-basket.json";
        String expectedLabel = "CreditDefaultSwap_Basket";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyEQSwap() {
        // Test qualification for an Equity Swap.
        String filePath = "result-json-files/fpml-5-13/products/equity-swaps/eqs-ex01-single-underlyer-execution-long-form.json";
        String expectedLabel = "EquitySwap_TotalReturnBasicPerformance_SingleName";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyEQVarianceSwap() {
        // Test qualification for an Equity Variance Swap.
        String filePath = "result-json-files/fpml-5-13/products/variance-swaps/eqvs-ex02-variance-swap-single-stock.json";
        String expectedLabel = "EquitySwap_ParameterReturnVariance_SingleName";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    // Additional tests for other products follow the same pattern...
}
