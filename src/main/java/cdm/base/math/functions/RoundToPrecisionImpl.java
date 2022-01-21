package cdm.base.math.functions;

import cdm.base.math.RoundingDirectionEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundToPrecisionImpl extends RoundToPrecision {

    // round a supplied value to the specified precision (in decimal places).
    @Override
    protected BigDecimal doEvaluate(BigDecimal value, Integer precision, RoundingDirectionEnum roundingMode) {
        if (value == null) return null;
        if (precision == null || roundingMode == null) return value;

        // calculate the scale factor based on the supplied precision
        double scale = Math.pow(10.0, -1.0 * precision);
        BigDecimal nearest = BigDecimal.valueOf(scale);

        // divide by the scale factor using the rounding mode the multiply by it again
        return value.divide(nearest)
                .setScale(0, toRoundingMode(roundingMode))
                .multiply(nearest);
    }

    private RoundingMode toRoundingMode(RoundingDirectionEnum roundingMode) {
        switch (roundingMode) {
            case UP:
                return RoundingMode.UP;
            case DOWN:
                return RoundingMode.DOWN;
            case NEAREST:
                return RoundingMode.HALF_EVEN;
            default:
                throw new IllegalArgumentException("Unsupported RoundingDirectionEnum " + roundingMode);
        }
    }
}
