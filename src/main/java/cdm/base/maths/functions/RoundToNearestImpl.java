package cdm.base.maths.functions;

import java.math.BigDecimal;

import cdm.base.maths.RoundingModeEnum;

public class RoundToNearestImpl extends RoundToNearest {

	@Override
	protected BigDecimal doEvaluate(BigDecimal amount, BigDecimal nearest, RoundingModeEnum roundingMode) {
		// TODO: implement rounding
		return amount;
	}

}
