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
 * Standard tests for Exp function covering typical financial modeling scenarios.
 *
 * These tests validate the exponential function implementation for common use cases
 * in financial calculations, including discounting, binomial models, and exact
 * mathematical identities.
 */
final class ExpTest extends AbstractFunctionTest {

    private final Logger logger = LoggerFactory.getLogger(ExpTest.class);

    /**
     * 1e-12  High      Fundamental properties, small inputs near the series center.
     * 1e-5   Moderate  General purpose engineering accuracy.
     * 1e-1   Loose     Boundary testing where the approximation expectation starts failing.
     */
    private static final double DELTA_HIGH = 1e-12;
    private static final double DELTA_MODERATE = 1e-5;
    private static final double DELTA_LOOSE = 1e-1;

    @Inject
    Exp expFunc;

    /**
     * Tests exact mathematical values: exp(0) = 1 and exp(1) = e.
     *
     * Purpose: Validates fundamental mathematical identities that must hold exactly.
     * - exp(0) = 1: Any number to the power of 0 is 1
     * - exp(1) = e: Euler's number (~2.71828...), the base of natural logarithms
     *
     * Why this matters: These are fundamental properties that must be exact (within
     * floating-point precision). Any deviation indicates a serious implementation error.
     */
    @Test
    void shouldReturnExactValuesForBaseIdentities() {
        logger.info("Test exp exact values");


        // exp(0) = 1 (fundamental identity)
        assertEquals(1.0, expFunc.evaluate(BigDecimal.ZERO).doubleValue(), DELTA_HIGH,
                "exp(0) should equal 1");

        // exp(1) = e (Euler's number)
        assertEquals(Math.E, expFunc.evaluate(BigDecimal.ONE).doubleValue(), DELTA_MODERATE,
                "exp(1) should equal e (~2.71828...)");
    }

    /**
     * Tests the inverse property: exp(x) * exp(-x) = 1.
     *
     * Purpose: Validates that the implementation maintains the fundamental inverse
     * property of the exponential function. This property is crucial for numerical
     * stability in many algorithms.
     *
     * Why this matters: This property is used extensively in financial calculations,
     * particularly in discounting (exp(-rT) * exp(rT) = 1). Any deviation from this
     * property can lead to systematic errors in pricing models.
     */
    @Test
    void shouldSatisfyInverseIdentityProperty() {
        logger.info("Test exp inverse property: exp(x) * exp(-x) = 1");

        BigDecimal x = new BigDecimal("0.12345");
        BigDecimal negX = x.negate();

        double expX = expFunc.evaluate(x).doubleValue();
        double expNegX = expFunc.evaluate(negX).doubleValue();
        double product = expX * expNegX;

        assertEquals(1.0, product, DELTA_HIGH,
                "Inverse property: exp(x) * exp(-x) should equal 1");
    }

    /**
     * Tests small exponent behavior: exp(-y) ≈ 1 - y for tiny y.
     *
     * Purpose: Validates that the implementation correctly approximates exp(-y) for
     * very small y using the first-order Taylor expansion: exp(-y) ≈ 1 - y.
     *
     * Why this matters: This approximation is used in many numerical algorithms and
     * must be accurate for small values to maintain overall numerical stability.
     */
    @Test
    void shouldMatchFirstOrderTaylorExpansionForTinyInputs() {
        logger.info("Test exp small exponent behavior: exp(-y) ≈ 1 - y for tiny y");

        BigDecimal tiny = new BigDecimal(DELTA_HIGH);
        double approx = 1.0 - tiny.doubleValue();
        double expTinyNeg = expFunc.evaluate(tiny.negate()).doubleValue();

        // Allow 10x tolerance for approximation
        assertEquals(approx, expTinyNeg, DELTA_HIGH,
                "Small exponent: exp(-y) should approximate 1 - y for tiny y");
    }

    /**
     * Tests boundary value accuracy for hand-off points between different algorithm branches.
     *
     * Purpose: Validates that the implementation maintains accuracy at boundary values
     * where different computational strategies may be used. These boundaries are critical
     * points where numerical methods might switch algorithms.
     *
     * Why this matters: Boundary conditions are common sources of numerical errors.
     * The implementation must be accurate at these points to avoid discontinuities or
     * sudden accuracy degradation.
     */
    @ParameterizedTest
    @ValueSource(doubles = {1.0, 2.0, 4.0, 8.0})
    void shouldBeConsistentAtAlgorithmSwitchPoints(double boundary) {
        logger.info("Test exp boundary value accuracy with boundary ={}", boundary);

        BigDecimal input = BigDecimal.valueOf(boundary);
        assertEquals(Math.exp(boundary), expFunc.evaluate(input).doubleValue(), DELTA_MODERATE,
                String.format("Boundary value exp(%f) should match Math.exp", boundary));

        // Test slightly above boundary
        double boundaryPlus = boundary + 0.01;
        BigDecimal inputPlus = BigDecimal.valueOf(boundaryPlus);
        assertEquals(Math.exp(boundaryPlus), expFunc.evaluate(inputPlus).doubleValue(), DELTA_MODERATE,
                String.format("Boundary value exp(%f) should match Math.exp", boundaryPlus));
    }

    /**
     * Tests parity with the standard Java Math.exp() function across a representative range of values.
     *
     * Purpose: Validates the core series approximation for standard inputs where discounting
     * and volatility scaling typically occur.
     */
    @ParameterizedTest
    @ValueSource(doubles = {-10.0, -5.0, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 5.0})
    void shouldMaintainParityWithStandardLibrary(double value) {
        logger.info("Verifying standard range exp parity for value: {}", value);
        assertExpParity(value, DELTA_MODERATE);
    }

    /**
     * Tests parity for large negative exponents.
     *
     *  Why this matters: As x becomes significantly negative, exp(x) approaches zero.
     * This test ensures that the implementation doesn't underflow prematurely or
     * produce negative results due to series oscillation.
     */
    @Test
    void shouldMaintainParityForLargeNegativeMagnitudes() {
        double largeNeg = -100.0; // Math.exp(-1000) is effectively 0.0 in double precision
        logger.info("Verifying decay parity for large negative: {}", largeNeg);
        assertExpParity(largeNeg, DELTA_HIGH);
    }

    /**
     * Tests parity for large positive exponents.
     *
     * Why this matters: The exponential function grows extremely fast. Large inputs
     * stress-test the argument reduction and range-scaling logic to ensure accuracy
     * before reaching the Double.MAX_VALUE limit.
     */
    @Test
    void shouldMaintainParityForLargePositiveMagnitudes() {
        double largePos = 10.0;
        logger.info("Verifying growth parity for large positive: {}", largePos);
        assertExpParity(largePos, DELTA_LOOSE);
    }

    /**
     * Private helper to encapsulate the parity assertion.
     */
    private void assertExpParity(double value, double delta) {
        double expected = Math.exp(value);
        BigDecimal input = BigDecimal.valueOf(value);

        BigDecimal result = expFunc.evaluate(input);

        logger.debug("Validation: Input: {} | Tolerance: {} | Standard Result: {} | DSL Result: {}",
                value, delta, expected, (result != null ? result : "null"));

        assertNotNull(result, "DSL exp result should not be null for input: " + value);

        assertEquals(expected, result.doubleValue(), delta,
                String.format("The DSL exponential of (%f) deviated from the Java Math value (%f) by more than %f",
                        value, expected, delta));
    }
}
