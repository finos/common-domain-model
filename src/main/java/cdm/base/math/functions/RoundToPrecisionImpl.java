package cdm.base.math.functions;

import cdm.base.math.RoundingDirectionEnum;
import cdm.base.math.RoundingModeEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundToPrecisionImpl extends RoundToPrecision {
    @Override
    protected BigDecimal doEvaluate(BigDecimal value, Integer precision, RoundingDirectionEnum roundingMode) {
        double scale = Math.pow(10.0, 1.0 * precision);
        BigDecimal nearest = BigDecimal.valueOf(scale);
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
