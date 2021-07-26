package cdm.base.math.functions;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.Vector;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorScalarOperationsImplTest extends AbstractFunctionTest {

    @Inject
    private VectorScalarOperation vectorScalarOp;

    @Test
    void shouldHandleNulls() {
        List<BigDecimal> emptyList = new ArrayList<>();
        List<BigDecimal> shortList = Arrays.asList(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0));
        Vector.VectorBuilder vb = Vector.builder().setValues(shortList);

        check(emptyList, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, null, null).getValues());
        check(shortList, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, vb, null).getValues());
        check(emptyList, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, null, BigDecimal.valueOf(10.0)).getValues());
    }

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
        check(addExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.ADD, leftv,  ten));
        check(subExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.SUBTRACT, leftv,  ten));
        check(mulExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.MULTIPLY, leftv,  ten));
        check(divExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.DIVIDE, leftv,  ten));
        check(maxExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.MAX, leftv,  ten));
        check(minExpected, vectorScalarOp.evaluate(ArithmeticOperationEnum.MIN, leftv,  ten));


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
