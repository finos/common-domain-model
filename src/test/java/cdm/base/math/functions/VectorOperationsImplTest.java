package cdm.base.math.functions;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.Vector;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorOperationsImplTest  extends AbstractFunctionTest {

    @Inject
    private VectorOperation vectorOp;

    @Test
    void shouldHandleNulls() {
        List<BigDecimal> emptyList = new ArrayList<>();
        List<BigDecimal> shortList = Arrays.asList(BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0));
        Vector.VectorBuilder vb = Vector.builder().setValues(shortList);

        check(emptyList, vectorOp.evaluate(ArithmeticOperationEnum.ADD, null, null).getValues());
        check(shortList, vectorOp.evaluate(ArithmeticOperationEnum.ADD, vb, null).getValues());
        check(shortList, vectorOp.evaluate(ArithmeticOperationEnum.ADD, null, vb).getValues());
    }

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
        Vector.VectorBuilder leftv = Vector.builder().setValues(left);
        Vector.VectorBuilder rightv = Vector.builder().setValues(right);
        Vector.VectorBuilder shortv = Vector.builder().setValues(shortList);



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

        check(addExpected, vectorOp.evaluate(ArithmeticOperationEnum.ADD, leftv, rightv));
        check(subExpected, vectorOp.evaluate(ArithmeticOperationEnum.SUBTRACT, leftv, rightv));
        check(mulExpected, vectorOp.evaluate(ArithmeticOperationEnum.MULTIPLY, leftv, rightv));
        check(divExpected, vectorOp.evaluate(ArithmeticOperationEnum.DIVIDE, leftv, rightv));
        check(maxExpected, vectorOp.evaluate(ArithmeticOperationEnum.MAX, leftv, rightv));
        check(minExpected, vectorOp.evaluate(ArithmeticOperationEnum.MIN, leftv, rightv));
        check(addShortLeftExpected, vectorOp.evaluate(ArithmeticOperationEnum.ADD, shortv, rightv));
        check(addShortRightExpected, vectorOp.evaluate(ArithmeticOperationEnum.ADD, leftv, shortv));

    }
    void check(List<BigDecimal> expected, Vector actual) {
        check(expected, actual.getValues());
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
