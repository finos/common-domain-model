package cdm.base.math.functions;

import cdm.base.math.RoundingDirectionEnum;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class RoundToSignificantFiguresImpl extends RoundToSignificantFigures {

    // round a supplied value to the specified significant figures
    @Override
    protected BigDecimal doEvaluate(BigDecimal value, Integer significantFigures, RoundingDirectionEnum roundingMode) {
        if (value == null) return null;
        if (significantFigures == null || roundingMode == null) return value;
        
        return value.round(new MathContext(significantFigures, toRoundingMode(roundingMode)));
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
