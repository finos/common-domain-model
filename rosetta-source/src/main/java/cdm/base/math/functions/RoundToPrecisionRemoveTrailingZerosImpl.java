package cdm.base.math.functions;

import cdm.base.math.RoundingDirectionEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundToPrecisionRemoveTrailingZerosImpl extends RoundToPrecisionRemoveTrailingZeros {

    // round a supplied value to the specified precision (in decimal places).
    @Override
    protected BigDecimal doEvaluate(BigDecimal value, Integer precision, RoundingDirectionEnum roundingMode) {
        if (value == null) return null;
        if (precision == null || roundingMode == null) return value;

        return value.setScale(precision, toRoundingMode(roundingMode)).stripTrailingZeros();
    }

    private RoundingMode toRoundingMode(RoundingDirectionEnum roundingMode) {
        switch (roundingMode) {
            case UP:
                return RoundingMode.UP;
            case DOWN:
                return RoundingMode.DOWN;
            case NEAREST:
                return RoundingMode.HALF_UP;
            default:
                throw new IllegalArgumentException("Unsupported RoundingDirectionEnum " + roundingMode);
        }
    }
}
