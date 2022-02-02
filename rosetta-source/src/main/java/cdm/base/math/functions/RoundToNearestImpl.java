package cdm.base.math.functions;

import cdm.base.math.RoundingModeEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundToNearestImpl extends RoundToNearest {

	@Override
	protected BigDecimal doEvaluate(BigDecimal value, BigDecimal nearest, RoundingModeEnum roundingMode) {
		return value.divide(nearest)
				.setScale(0, toRoundingMode(roundingMode))
				.multiply(nearest);
	}

	private RoundingMode toRoundingMode(RoundingModeEnum roundingMode) {
		switch (roundingMode) {
		case UP:
			return RoundingMode.UP;
		case DOWN:
			return RoundingMode.DOWN;
		default:
			throw new IllegalArgumentException("Unsupported RoundingModeEnum " + roundingMode);
		}
	}
}
