package cdm.base.math.functions;

import cdm.base.math.ArithmeticOp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

public  class ArithmeticOpImpl {

    BiFunction<BigDecimal, BigDecimal, BigDecimal>  myOp;

    public ArithmeticOpImpl(ArithmeticOp op) {
        myOp = operation(op);
    }

    protected BigDecimal apply(BigDecimal left, BigDecimal right) {
        return myOp.apply(left,right);
    }

    private static BiFunction<BigDecimal, BigDecimal, BigDecimal> operation(ArithmeticOp arithmeticOp) {
        BiFunction<BigDecimal, BigDecimal, BigDecimal> op = null;
        switch (arithmeticOp) {
            case ADD_OP:
                op = BigDecimal::add;
                break;
            case SUBTRACT_OP:
                op = BigDecimal::subtract;
                break;
            case MULTIPLY_OP:
                op = BigDecimal::multiply;
                break;
            case DIVIDE_OP:
                op = (b1, b2) -> (b2.doubleValue() == 0.0) ? BigDecimal.valueOf(Double.NaN) : b1.divide(b2,10, RoundingMode.HALF_EVEN);
                break;
            case MAX_OP:
                op = BigDecimal::max;
                break;
            case MIN_OP:
                op = BigDecimal::min;
                break;
            default:
                break;
        }
        return op;
    }

}
