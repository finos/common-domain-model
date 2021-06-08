package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;
import cdm.base.math.Vector;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorScalarOperationsImplTest extends AbstractFunctionTest {

    @Inject
    private VectorScalarOperation vectorScalarOp;

    @Test
    void shouldApplyOperations() {
        List<BigDecimal> left = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        Vector.VectorBuilder leftv = Vector.builder().setValues(left);

        List<BigDecimal> addExpected = Arrays.asList(
                BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(21.0),
                BigDecimal.valueOf(22.0));
        List<BigDecimal> subExpected = Arrays.asList(
                BigDecimal.valueOf(0.0),
                BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(2.0));
        List<BigDecimal> divExpected = Arrays.asList(
                BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(1.2));
        List<BigDecimal> mulExpected = Arrays.asList(
                BigDecimal.valueOf(10.0 * 10.0),
                BigDecimal.valueOf(11.0 * 10.0),
                BigDecimal.valueOf(12.0 * 10.0));
        List<BigDecimal> maxExpected = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        List<BigDecimal> minExpected = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(10.0));


        BigDecimal ten = BigDecimal.valueOf(10.0);
        check(addExpected, vectorScalarOp.evaluate(ArithmeticOp.ADD_OP, leftv,  ten));
        check(subExpected, vectorScalarOp.evaluate(ArithmeticOp.SUBTRACT_OP, leftv,  ten));
        check(mulExpected, vectorScalarOp.evaluate(ArithmeticOp.MULTIPLY_OP, leftv,  ten));
        check(divExpected, vectorScalarOp.evaluate(ArithmeticOp.DIVIDE_OP, leftv,  ten));
        check(maxExpected, vectorScalarOp.evaluate(ArithmeticOp.MAX_OP, leftv,  ten));
        check(minExpected, vectorScalarOp.evaluate(ArithmeticOp.MIN_OP, leftv,  ten));


    }
    void check(List<BigDecimal> expected, Vector actual) {
        check(expected, actual.getValues());
    }

    void check(List<BigDecimal> expected, List<? extends BigDecimal> actual) {
        assertEquals(expected.size(), actual.size());
        int n = expected.size();
        double delta = 0.00001;
        for(int i = 0; i < n; i++) {
            assertEquals(expected.get(i).doubleValue(), actual.get(i).doubleValue(), delta);
        }
    }
}
