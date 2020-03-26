package org.isda.cdm.functions;

import java.math.BigDecimal;

import cdm.base.maths.functions.Abs;

public class AbsImpl extends Abs {

	@Override
	protected BigDecimal doEvaluate(BigDecimal x) {
		return x.abs();
	}
}
