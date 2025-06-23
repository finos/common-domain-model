package cdm.base.math.functions;

import cdm.base.math.RoundingDirectionEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class RoundToPrecisionImpl extends RoundToPrecision {

    // round a supplied value to the specified precision (in decimal places).
    @Override
    protected BigDecimal doEvaluate(BigDecimal value, Integer precision, RoundingDirectionEnum roundingMode, Boolean removeTrailingZero) {
        if (value == null) return null;
        if (precision == null || roundingMode == null) return value;
        BigDecimal roundedValue = value.setScale(precision, toRoundingMode(roundingMode));
        return Optional.ofNullable(removeTrailingZero).orElse(false) ? roundedValue.stripTrailingZeros() : roundedValue;
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
