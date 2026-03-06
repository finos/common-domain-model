package cdm.event.smartcontract.functions.resetinstruction.functions;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.smartcontract.resetinstruction.CollectFloatingRateOptionInstruction;
import cdm.event.smartcontract.resetinstruction.functions.Create_CollectFloatingRateOptionInstruction;
import cdm.product.asset.InterestRatePayout;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import util.ResourcesUtils;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCollectFloatingRateOptionInstructionTest extends AbstractFunctionTest {

    private static final String INTEREST_RATE_PAYOUT_PATH = "functions/smart-contract/input/payout/interest-rate-payout-GBP-SONIA.json";

    @Inject
    Create_CollectFloatingRateOptionInstruction createCollectFloatingRateOptionInstruction;

    @DisplayName("Create CollectFloatingRateOption instruction from InterestRatePayout")
    @Test
    void shouldCreateCollectFloatingRateOptionInstruction()
            throws IOException {
        InterestRatePayout floatingLeg = ResourcesUtils.getObjectAndResolveReferences(InterestRatePayout.class, INTEREST_RATE_PAYOUT_PATH);
        Date tradeDate = Date.of(2026, 1, 5);

        // Call the function with the three inputs
        CollectFloatingRateOptionInstruction result =
                createCollectFloatingRateOptionInstruction.evaluate(floatingLeg, tradeDate);

        assertNotNull(result, "Result should not be null");
        assertEquals(FloatingRateIndexEnum.GBP_SONIA, result.getFloatingRateIndex());
        assertEquals(tradeDate, result.getTradeDate());
    }

    @Nested
    @DisplayName("Negative test cases - documenting null-handling behaviour and edge cases")
    class NegativeTests {

        // Note: Both inputs to the function are required with cardinality (1..1)
        // However, there are no validation to enforce the cardinality
        // These tests document the current behaviour of the function when nulls are passed in
        // which is to handle gracefully and return null output rather than throwing an exception
        @Test
        @DisplayName("Should handle null floatingLeg")
        void shouldHandleNullFloatingLeg() {
            Date tradeDate = Date.of(2026, 1, 5);

            CollectFloatingRateOptionInstruction result = assertDoesNotThrow(
                    () ->
                            createCollectFloatingRateOptionInstruction.evaluate(
                                    null, // null floatingLeg
                                    tradeDate
                            ),
                    "Function should handle null floatingLeg");
            assertNotNull(result, "Result should not be null when floatingLeg is null");
            assertNull(result.getFloatingRateIndex(), "Floating rate index should be null when input floatingLeg is null");
            assertEquals(tradeDate, result.getTradeDate(), "Trade date should be set as the input trade date");
        }

        @Test
        @DisplayName("Should handle null tradeDate")
        void shouldHandleNullTradeDate() throws IOException {
            InterestRatePayout floatingLeg =
                    ResourcesUtils.getObjectAndResolveReferences(InterestRatePayout.class, INTEREST_RATE_PAYOUT_PATH);

            CollectFloatingRateOptionInstruction result = assertDoesNotThrow(
                    () ->
                            createCollectFloatingRateOptionInstruction.evaluate(
                                    floatingLeg,
                                    null // null tradeDate
                            ),
                    "Function should handle null tradeDate");
            assertNotNull(result, "Result should not be null when tradeDate is null");
            assertEquals(FloatingRateIndexEnum.GBP_SONIA, result.getFloatingRateIndex(), "Floating rate index should be set based on the input floatingLeg");
            assertNull(result.getTradeDate(), "Trade date should be null when input tradeDate is null");
        }

        @Test
        @DisplayName("Should handle floating leg without rate option")
        void shouldHandleFloatingLegWithoutRateOption() {
            // Create an empty/minimal InterestRatePayout without a rate option
            InterestRatePayout emptyFloatingLeg = InterestRatePayout.builder().build();

            Date tradeDate = Date.of(2024, 1, 15);

            CollectFloatingRateOptionInstruction result = assertDoesNotThrow(
                    () ->
                            createCollectFloatingRateOptionInstruction.evaluate(
                                    emptyFloatingLeg, tradeDate),
                    "Function should handle empty floatingLeg");

            assertNotNull(result, "Result should not be null when floatingRateOption is missing");
            assertNull(result.getFloatingRateIndex(), "Floating rate index should be null when input rate option is missing");
            assertEquals(tradeDate, result.getTradeDate(), "Trade date should be set as the input trade date");
        }

        @Test
        @DisplayName("Should handle all null parameters")
        void testEvaluate_withAllNullParameters_handlesGracefully() {
            CollectFloatingRateOptionInstruction result = assertDoesNotThrow(
                    () -> createCollectFloatingRateOptionInstruction.evaluate(null, null),
                    "Function should handle all null parameters gracefully");

            assertNotNull(result, "Result should not be null when all parameters are null");
            assertNull(result.getFloatingRateIndex(), "Floating rate index should be null when all parameter are null");
            assertNull(result.getTradeDate(), "Trade date should be null when all parameter are null");
        }
    }
}
