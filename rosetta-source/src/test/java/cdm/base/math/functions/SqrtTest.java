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
 * Standard tests for Sqrt function covering typical financial modeling scenarios.
 *
 * These tests validate the square root function implementation for common use cases
 * in financial calculations, including time scaling, volatility calculations, and
 * exact mathematical identities.
 */
final class SqrtTest extends AbstractFunctionTest {

    private final Logger logger = LoggerFactory.getLogger(SqrtTest.class);

    /**
     * 1e-12  High      Fundamental properties, small inputs near the series center.
     * 1e-5   Moderate  General purpose engineering accuracy.
     * 1e-1   Loose     Boundary testing where the approximation expectation starts failing.
     */
    private static final double DELTA_HIGH = 1e-12;
    private static final double DELTA_MODERATE = 1e-5;
    private static final double DELTA_LOOSE = 1e-1;

    @Inject
    Sqrt sqrtFunc;

    /**
     * Tests exact mathematical values: sqrt(0) = 0 and sqrt(1) = 1.
     *
     * Purpose: Validates fundamental mathematical identities that must hold exactly.
     * - sqrt(0) = 0: Square root of zero is zero
     * - sqrt(1) = 1: Square root of one is one
     *
     * Why this matters: These are fundamental properties that must be exact (within
     * floating-point precision). Any deviation indicates a serious implementation error.
     */
    @Test
    void shouldReturnExactValuesForBaseIdentities() {
        logger.info("Test sqrt exact values");

        // sqrt(0) = 0 (fundamental identity)
        assertEquals(0.0, sqrtFunc.evaluate(BigDecimal.ZERO).doubleValue(), DELTA_HIGH,
                "sqrt(0) should equal 0");

        // sqrt(1) = 1 (fundamental identity)
        assertEquals(1.0, sqrtFunc.evaluate(BigDecimal.ONE).doubleValue(), DELTA_HIGH,
                "sqrt(1) should equal 1");
    }

    /**
     * Tests sqrt(x) with very small positive values.
     *
     * Purpose: Validates that the implementation correctly handles tiny positive inputs.
     * As x approaches 0, sqrt(x) approaches 0, but remains positive for any positive x.
     *
     * Why this matters: In financial models, very small time steps or variance values
     * can occur. The implementation must return small but accurate positive values.
     */
    @Test
    void shouldMaintainPrecisionForTinyInputs() {
        logger.info("Test sqrt very small values");

        BigDecimal Ttiny = new BigDecimal("1e-8");
        double sTiny = sqrtFunc.evaluate(Ttiny).doubleValue();

        assertTrue(sTiny > 0.0, "sqrt(1e-8) should be positive");
        assertTrue(sTiny < 1e-3, "sqrt(1e-8) should be less than 1e-3 (sqrt(1e-8) = 1e-4)");
    }

    /**
     * Tests multiplicative property: sqrt(a * b) = sqrt(a) * sqrt(b) for a,b >= 0.
     *
     * Purpose: Validates that the implementation preserves the fundamental multiplicative
     * property of square roots. This property is used extensively in financial calculations.
     *
     * Why this matters: This property is used in many derivations and calculations. Any
     * deviation from this property can lead to systematic errors.
     */
    @Test
    void shouldSatisfyMultiplicativeProperty() {
        logger.info("Test sqrt multiplicative property: sqrt(a * b) = sqrt(a) * sqrt(b)");

        BigDecimal a = new BigDecimal("0.25");  // >= 0
        BigDecimal b = new BigDecimal("0.81");  // >= 0

        double sqrtAB = sqrtFunc.evaluate(a.multiply(b)).doubleValue();
        double sqrtA = sqrtFunc.evaluate(a).doubleValue();
        double sqrtB = sqrtFunc.evaluate(b).doubleValue();

        assertEquals(sqrtAB, sqrtA * sqrtB, DELTA_HIGH,
                "Multiplicative property: sqrt(a*b) should equal sqrt(a) * sqrt(b)");
    }

    /**
     * Tests convergence speed for large inputs: sqrt(1,000,000) = 1,000.
     *
     * Purpose: Validates that the implementation converges quickly and accurately for
     * large inputs. For sqrt(1,000,000), the initial guess in some algorithms might be
     * 500,000, requiring multiple iterations to converge to 1,000.
     *
     * Why this matters: Large inputs are common in financial calculations (e.g., large
     * time periods or variance values). The implementation must converge quickly without
     * accuracy loss.
     */
    @Test
    void shouldConvergeAccuratelyForLargeMagnitudes() {
        logger.info("Test sqrt convergence speed for large inputs");

        BigDecimal bigNumber = new BigDecimal("1000000.0");
        double expected = 1000.0;

        // Verify accuracy
        assertEquals(expected, Math.sqrt(bigNumber.doubleValue()), DELTA_HIGH,
                "Sqrt iteration count insufficient for large inputs");

        // Verify implementation matches
        double actual = sqrtFunc.evaluate(bigNumber).doubleValue();
        assertEquals(expected, actual, DELTA_HIGH,
                "sqrt(1,000,000) should equal 1,000");
    }

    /**
     * Tests parity with the standard Java Math.sqrt() function across a representative range of values.
     *
     * Purpose: Validates that the native DSL implementation produces results numerically
     * equivalent to the standard IEEE 754 floating-point implementation.
     */
    @ParameterizedTest
    @ValueSource(doubles = {0.0001, 0.5, 1.0, 2.0, 10.0, 156.25, 1000.0})
    void shouldMaintainParityWithStandardLibrary(double value) {
        logger.info("Test sqrt parity with Math.sqrt for value: {}", value);
        assertSqrtParity(value, DELTA_HIGH);
    }

    /**
     * Tests parity for extremely large positive values to ensure convergence stability.
     *
     * Why this matters: Large inputs stress-test the initial guess logic. If the guess is too far
     * off, the iterative method may fail to converge within the allotted cycles.
     */
    @Test
    void shouldMaintainParityForLargePositiveMagnitudes() {
        double largePos = 1_000_000_000.0;
        logger.info("Test sqrt parity for large positive: {}", largePos);
        assertSqrtParity(largePos, DELTA_HIGH);
    }

    /**
     * Tests handling for large negative values to ensure the "Null-Out" contract is maintained.
     * This test verifies the intentional divergence between the DSL (Null) and Native Java (NaN).
     *
     * Why this matters: While Java's Math.sqrt returns Double.NaN for negative inputs, this
     * DSL implementation is designed to return null to satisfy the domain's safety contract
     * and prevent NaN propagation in financial pipelines.
     */
    @Test
    void shouldReturnNullForLargeNegativeMagnitudes() {
        BigDecimal largeNeg = new BigDecimal("-1000000.0");
        double nativeInput = largeNeg.doubleValue();

        logger.info("Verifying domain parity divergence for input: {}", largeNeg);

        double nativeResult = Math.sqrt(nativeInput);
        assertTrue(Double.isNaN(nativeResult), "Native Math.sqrt should return NaN for negatives.");

        BigDecimal dslResult = sqrtFunc.evaluate(largeNeg);
        assertNull(dslResult, "DSL sqrt must return null for negatives, diverging from Native NaN.");
    }

    /**
     * Private helper to encapsulate the assertion logic between DSL and Java Math.
     */
    private void assertSqrtParity(double value, double delta) {
        double expected = Math.sqrt(value);

        BigDecimal input = BigDecimal.valueOf(value);
        BigDecimal result = sqrtFunc.evaluate(input);

        logger.debug("Validation: Sqrt({}) | Delta Allowed: {} | Standard Result: {} | DSL Result: {}",
                value, delta, expected, result);

        assertNotNull(result, "Result should not be null for positive input: " + value);
        assertEquals(expected, result.doubleValue(), delta,
                String.format("DSL sqrt(%f) did not match Math.sqrt(%f)", value, expected));
    }

    /**
     * Tests the handling of negative input values.
     *
     * Purpose: Validates that the implementation returns null for inputs outside
     * the real-number domain of the square root function (x < 0).
     *
     * Why this matters: In series approximation and data pipelines, returning null
     * is a graceful way to signal that a value is mathematically undefined without
     * halting the execution thread. This ensures consistency with the null-handling
     * behavior of other transcendental functions in this library.
     */
    @Test
    void shouldReturnNullForNegativeInputs() {
        logger.info("Test negative input handling: expecting null output");
        assertNull(sqrtFunc.evaluate(new BigDecimal("-1.0")),
                "sqrt(-1) should return null as it is undefined for negative real numbers.");
    }
}
