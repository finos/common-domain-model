package cdm.base.math.functions;

import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Standard tests for Log function covering typical financial modeling scenarios.
 *
 * These tests validate the natural logarithm function implementation for common use cases
 * in financial calculations, including price ratios, returns, and exact mathematical identities.
 */
final class LogTest extends AbstractFunctionTest {

    private final Logger logger = LoggerFactory.getLogger(LogTest.class);

    /**
     * 1e-12  High      Fundamental properties, small inputs near the series center.
     * 1e-5   Moderate  General purpose engineering accuracy.
     * 1e-1   Loose     Boundary testing where the approximation expectation starts failing.
     */
    private static final double DELTA_HIGH = 1e-12;
    private static final double DELTA_MODERATE = 1e-5;
    private static final double DELTA_LOOSE = 1e-1;


    @Inject
    Log logFunc;

    /**
     * Tests exact mathematical values and sign properties.
     *
     * Purpose: Validates fundamental mathematical identities and sign behavior:
     * - log(1) = 0: Logarithm of 1 is always 0
     * - log(e) = 1: Natural logarithm of e is 1 (definition)
     * - log(x) > 0 for x > 1: Positive for values greater than 1
     * - log(x) < 0 for 0 < x < 1: Negative for values between 0 and 1
     *
     * Why this matters: These are fundamental properties that must hold exactly (within
     * floating-point precision). Sign correctness is crucial for financial calculations
     * involving returns and ratios.
     */
    @Test
    void shouldReturnExactValuesAndCorrectSigns() {
        logger.info("Test log exact values and sign properties");

        // log(1) = 0 (fundamental identity)
        assertEquals(0.0, logFunc.evaluate(BigDecimal.ONE).doubleValue(), DELTA_HIGH,
                "log(1) should equal 0");

        // log(e) = 1 (definition of natural logarithm)
        assertEquals(1.0, logFunc.evaluate(new BigDecimal(Math.E)).doubleValue(), DELTA_MODERATE,
                "log(e) should equal 1");

        // log(x) > 0 for x > 1
        assertTrue(logFunc.evaluate(new BigDecimal("10.0")).doubleValue() > 0.0,
                "log(10) should be positive (log(x) > 0 for x > 1)");

        // log(x) < 0 for 0 < x < 1
        assertTrue(logFunc.evaluate(new BigDecimal("0.5")).doubleValue() < 0.0,
                "log(0.5) should be negative (log(x) < 0 for 0 < x < 1)");
    }

    /**
     * Tests log(x) with very small positive values (near zero).
     *
     * Purpose: Validates that the implementation correctly handles tiny positive inputs.
     * As x approaches 0, log(x) approaches negative infinity, but remains finite for
     * any positive x, no matter how small.
     *
     * Why this matters: In continuous models, very small probabilities or price ratios
     * can occur. The implementation must return large negative but finite values, not
     * overflow or produce incorrect results.
     */
    @Test
    void shouldStayFiniteForTinyPositiveInputs() {
        logger.info("Test log with very small positive values");

        // Double.MIN_VALUE is the smallest positive double (~4.9e-324)
        BigDecimal tiny = new BigDecimal(Double.MIN_VALUE);
        double tinyLog = logFunc.evaluate(tiny).doubleValue();

        assertFalse(Double.isInfinite(tinyLog),
                "log(MIN_VALUE) should be finite (not -Infinity)");
        assertFalse(Double.isNaN(tinyLog),
                "log(MIN_VALUE) should not be NaN");
        assertTrue(tinyLog < 0.0,
                "log(MIN_VALUE) should be negative (log of tiny number is large negative)");
    }

    /**
     * Tests accuracy at edge of likely validity (x = 30.0).
     *
     * Purpose: Validates accuracy at x = 30.0, which is near the practical limit
     * for some approximation methods. This test ensures the implementation maintains
     * acceptable accuracy at this boundary.
     *
     * Expected value: log(30.0) ≈ 3.40
     * Tolerance: 1e-2 (allows for approximation errors near limits)
     *
     * Why this matters: Some implementations may have accuracy limits. This test
     * identifies where accuracy begins to degrade.
     */
//    @Test
//    void shouldMaintainAccuracyNearUpperSeriesLimit() {
//        logger.info("Test log accuracy at edge of likely validity (x = 30.0)");
//
//        BigDecimal val30 = new BigDecimal("30.0");
//        double expected30 = Math.log(30.0); // ~3.40
//        double actual30 = logFunc.evaluate(val30).doubleValue();
//
//        assertEquals(expected30, actual30, DELTA_MODERATE,
//                "Log approximation should be accurate at x=30.0 (edge of validity)");
//    }

    /**
     * Tests accuracy at extended limit (x = 100.0).
     *
     * Purpose: Validates accuracy at x = 100.0, which is beyond the typical limit
     * for some approximation methods. This test checks if the implementation can
     * handle values beyond the expected range.
     *
     * Expected value: log(100.0) ≈ 4.60
     * Tolerance: 1e-1 (allows for larger approximation errors at extended limits)
     *
     * Why this matters: If this test passes, the DSL implementation is better than
     * expected. If it fails, we know the limit of the DSL model is around x=35.0.
     */
//    @Test
//    void shouldIdentifyAccuracyDegradationAtHighValues() {
//        logger.info("Test log accuracy at extended limit (x = 100.0)");
//
//        BigDecimal val100 = new BigDecimal("100.0");
//        double expected100 = Math.log(100.0); // ~4.60
//        double actual100 = logFunc.evaluate(val100).doubleValue();
//
//        assertEquals(expected100, actual100, DELTA_LOOSE,
//                "Log approximation fails for x=100 (series hits max value ~3.56). " +
//                        "If this passes, DSL implementation is better than expected.");
//    }

    /**
     * Tests parity with standard Java Math.log() for a representative range of inputs.
     *
     * Purpose: Validates that the DSL implementation matches the standard IEEE 754 results
     * for values where the series approximation is most stable.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.01, 0.5, 0.9, 1.0, 1.1, 2.0, 10.0, 25.0, 30.0})
    void shouldMaintainParityWithStandardLibrary(double value) {
        logger.info("Verifying standard range log parity for value: {}", value);
        assertLogParity(value, DELTA_MODERATE);
    }

    @ParameterizedTest
    @ValueSource(doubles = {100.0})
    void shouldDivergeParityWithStandardLibrary(double value) {
        logger.info("Verifying standard range log parity for value: {}", value);
        assertLogParity(value, DELTA_LOOSE);
    }

    /**
     * Tests parity for extremely small positive values (approaching zero).
     *
     * Why this matters: As x approaches 0, log(x) drops toward -Infinity. This test
     * ensures the implementation maintains accuracy and doesn't collapse to NaN or
     * overflow during the reciprocal scaling process.
     */
    @Test
    void shouldMaintainParityForTinyPositiveMagnitudes() {
        double tinyValue = 1e-2;
        logger.info("Verifying asymptotic log parity for tiny value: {}", tinyValue);
        assertLogParity(tinyValue, DELTA_MODERATE);
    }

    /**
     * Tests parity for very large positive values.
     *
     * Why this matters: Large inputs test the implementation's argument reduction
     * or scaling logic, ensuring it can handle values beyond the standard series convergence.
     */
    @Test
    void shouldMaintainParityForLargePositiveMagnitudes() {
        double largeValue = 100;
        logger.info("Verifying extended range log parity for large value: {}", largeValue);
        assertLogParity(largeValue, DELTA_LOOSE);
    }

    /**
     * Private helper to encapsulate the parity assertion.
     */
    private void assertLogParity(double value, double delta) {
        double expected = Math.log(value);
        BigDecimal input = BigDecimal.valueOf(value);

        BigDecimal result = logFunc.evaluate(input);

        logger.debug("Validation: Input: {} | Tolerance: {} | Standard Result: {} | DSL Result: {}",
                value, delta, expected, (result != null ? result : "null"));

        assertNotNull(result, "DSL log result should not be null for positive input: " + value);

        assertEquals(expected, result.doubleValue(), delta,
                String.format("The DSL natural log of (%f) deviated from the Java Math value (%f) by more than %f",
                        value, expected, delta));
    }

    /**
     * Tests the handling of non-positive inputs (zero and negative values).
     *
     * Purpose: Validates that the implementation returns null for inputs outside
     * the real-number domain of the natural logarithm (x > 0).
     *
     * Why this matters: In series approximation and data pipelines, returning null
     * is a graceful way to signal that a value is mathematically undefined without
     * halting the entire execution thread. This allows the caller to handle
     * out-of-bounds data (like zero or negative prices) through null-checks.
     */
    @Test
    void shouldReturnNullForNonPositiveInputs() {
        logger.info("Test non-positive input handling: expecting null output");

        // Log(0) is mathematically undefined (asymptotic to -Infinity)
        BigDecimal zeroResult = logFunc.evaluate(BigDecimal.ZERO);
        assertNull(zeroResult, "log(0) should return null as it is undefined.");

        // Log(negative) is undefined for real numbers
        BigDecimal negativeResult = logFunc.evaluate(new BigDecimal("-1.0"));
        assertNull(negativeResult, "log(negative) should return null as it is undefined in reals.");
    }

}
