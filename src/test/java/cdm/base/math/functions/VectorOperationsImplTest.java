package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;
import cdm.base.math.RoundingModeEnum;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class VectorOperationsImplTest  extends AbstractFunctionTest {

    @Inject
    private VectorOperation vectorOp;

    @Test
    void shouldApplyOperations() {
        List<BigDecimal> left = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        List<BigDecimal> right = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(9.0),
                BigDecimal.valueOf(8.0));
        List<BigDecimal> shortList = Arrays.asList(
                BigDecimal.valueOf(10.0));


        List<BigDecimal> addExpected = Arrays.asList(
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(20.0));
        List<BigDecimal> subExpected = Arrays.asList(
                BigDecimal.valueOf(0.0),
                BigDecimal.valueOf(2.0),
                BigDecimal.valueOf(4.0));
        List<BigDecimal> divExpected = Arrays.asList(
                BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(11.0).divide(BigDecimal.valueOf(9.0), 10, RoundingMode.HALF_EVEN),
                BigDecimal.valueOf(12.0 / 8.0));
        List<BigDecimal> mulExpected = Arrays.asList(
                BigDecimal.valueOf(10.0 * 10.0),
                BigDecimal.valueOf(9.0 * 11.0),
                BigDecimal.valueOf(8.0 * 12.0));
        List<BigDecimal> maxExpected = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        List<BigDecimal> minExpected = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(9.0),
                BigDecimal.valueOf(8.0));

        List<BigDecimal> addShortRightExpected = Arrays.asList(
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        List<BigDecimal> addShortLeftExpected = Arrays.asList(
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(9.0),
                BigDecimal.valueOf(8.0));

        check(addExpected, vectorOp.evaluate(ArithmeticOp.ADD_OP, left, right));
        check(subExpected, vectorOp.evaluate(ArithmeticOp.SUBTRACT_OP, left, right));
        check(mulExpected, vectorOp.evaluate(ArithmeticOp.MULTIPLY_OP, left, right));
        check(divExpected, vectorOp.evaluate(ArithmeticOp.DIVIDE_OP, left, right));
        check(maxExpected, vectorOp.evaluate(ArithmeticOp.MAX_OP, left, right));
        check(minExpected, vectorOp.evaluate(ArithmeticOp.MIN_OP, left, right));
        check(addShortLeftExpected, vectorOp.evaluate(ArithmeticOp.ADD_OP, shortList, right));
        check(addShortRightExpected, vectorOp.evaluate(ArithmeticOp.ADD_OP, left, shortList));

    }

    void check(List<BigDecimal> expected, List<? extends BigDecimal> actual) {
        assertEquals(expected.size(), actual.size());
        int n = expected.size();
        for(int i = 0; i < n; i++) {
            double delta = 0.00001;
            assertEquals(expected.get(i).doubleValue(), actual.get(i).doubleValue(), delta);
        }
    }

}
