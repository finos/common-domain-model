package cdm.base.math.functions;

import java.math.BigDecimal;

public class AbsImpl extends Abs {

	@Override
	protected BigDecimal doEvaluate(BigDecimal x) {
		return x.abs();
	}
}
