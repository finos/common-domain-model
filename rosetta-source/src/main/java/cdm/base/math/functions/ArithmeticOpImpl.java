package cdm.base.math.functions;

import cdm.base.math.ArithmeticOperationEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BiFunction;

public class ArithmeticOpImpl {

    static BiFunction<BigDecimal, BigDecimal, BigDecimal> operation(ArithmeticOperationEnum arithmeticOp) {
        switch (arithmeticOp) {
            case ADD:
                return BigDecimal::add;
            case SUBTRACT:
                return BigDecimal::subtract;
            case MULTIPLY:
                return BigDecimal::multiply;
            case DIVIDE:
                return (b1, b2) -> b1.divide(b2, 10, RoundingMode.HALF_EVEN);
            case MAX:
                return BigDecimal::max;
            case MIN:
                return BigDecimal::min;
            default:
                throw new IllegalArgumentException(String.format("Unknown ArithmeticOp %s", arithmeticOp));
        }
    }

}
