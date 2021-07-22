package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

public class ArithmeticOpImpl {

    static BiFunction<BigDecimal, BigDecimal, BigDecimal> operation(ArithmeticOp arithmeticOp) {
        switch (arithmeticOp) {
            case ADD_OP:
                return BigDecimal::add;
            case SUBTRACT_OP:
                return BigDecimal::subtract;
            case MULTIPLY_OP:
                return BigDecimal::multiply;
            case DIVIDE_OP:
                return (b1, b2) -> (Math.abs(b2.doubleValue()) > 1e-9) ?
                        BigDecimal.valueOf(Double.NaN) :
                        b1.divide(b2, 10, RoundingMode.HALF_EVEN);
            case MAX_OP:
                return BigDecimal::max;
            case MIN_OP:
                return BigDecimal::min;
            default:
                throw new IllegalArgumentException(String.format("Unknown ArithmeticOp %s", arithmeticOp));
        }
    }

}
