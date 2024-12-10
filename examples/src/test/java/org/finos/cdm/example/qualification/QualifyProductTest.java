package org.finos.cdm.example.qualification;

import cdm.event.common.TradeState;
import cdm.product.template.EconomicTerms;
import cdm.product.template.NonTransferableProduct;
import cdm.product.template.meta.EconomicTermsMeta;
import com.google.inject.Inject;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.qualify.QualifyResult;
import com.rosetta.model.lib.qualify.QualifyResultsExtractor;
import org.finos.cdm.example.AbstractExampleTest;
import org.finos.cdm.example.util.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * A test suite to validate the qualification of financial products using the Common Domain Model (CDM).
 * Each test case verifies that a specific product type is correctly qualified based on its economic terms.
 */
final class QualifyProductTest extends AbstractExampleTest {

    // Logger instance for debugging and reporting metrics
    private static final Logger LOGGER = LoggerFactory.getLogger(QualifyProductTest.class);

    // Factory to retrieve qualification functions for economic terms.
    @Inject
    private QualifyFunctionFactory.Default qualifyFunctionFactory;

    // Metadata for economic terms, providing access to qualification logic.
    @Inject
    private EconomicTermsMeta economicTermsMeta;

    /**
     * Qualifies a product using its CDM sample and verifies the qualification result.
     *
     * @param filePath      Path to the CDM sample file representing the product.
     * @param expectedLabel Expected qualification label for the product.
     * @return The actual qualification result label.
     */
    private String qualifyProduct(String filePath, String expectedLabel) {

        // Deserialize the CDM sample into a TradeState object and resolve any references.
        TradeState tradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, filePath);

        // Ensure the deserialized TradeState is not null.
        assertNotNull(tradeState, "TradeState must not be null");

        // Retrieve the NonTransferableProduct from the TradeState.
        NonTransferableProduct product = tradeState.getTrade().getProduct();

        // Extract economic terms, which are essential for qualification.
        EconomicTerms economicTerms = product.getEconomicTerms();

        // Retrieve the list of qualification functions for the economic terms.
        List<Function<? super EconomicTerms, QualifyResult>> qualifyFunctions =
                economicTermsMeta.getQualifyFunctions(qualifyFunctionFactory);

        // Execute qualification functions and extract the unique successful result.
        String qualificationResult = new QualifyResultsExtractor<>(qualifyFunctions, economicTerms)
                .getOnlySuccessResult() // Retrieve the only successful result, if any.
                .map(QualifyResult::getName) // Extract the name of the qualification.
                .orElse("Failed to qualify"); // Default if no successful qualification is found.

        // Verify that the qualification result matches the expected label.
        assertEquals(expectedLabel, qualificationResult);
        return qualificationResult;
    }

    // Test cases to validate qualification for various financial products.

    @Test
    void mustQualifyIRFra() {
        // Test qualification for an Interest Rate FRA.
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex08-fra.json";
        String expectedLabel = "InterestRate_Fra";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRSwaption() {
        // Test qualification for an Interest Rate Swaption.
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex14-berm-swaption.json";
        String expectedLabel = "InterestRate_Option_Swaption";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRCrossCurrency() {
        // Test qualification for an Interest Rate Cross Currency Swap.
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex06-xccy-swap.json";
        String expectedLabel = "InterestRate_CrossCurrency_FixedFloat";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRCap() {
        // Test qualification for an Interest Rate Cap.
        String filePath = "result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex22-cap.json";
        String expectedLabel = "InterestRate_CapFloor";
        String qualificationResult = qualifyProduct(filePath, expectedLabel);
        LOGGER.info("Qualification Result: {}", qualificationResult);
    }

    @Test
    void mustQualifyIRFloor() {
        // Test qualification for an Interest Rate Floor.
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
