package org.isda.cdm.functions;

import java.math.BigDecimal;

import org.isda.cdm.FloatingRateIndexEnum;

public class ResolveRateIndexImpl extends ResolveRateIndex {

	@Override
	protected BigDecimal doEvaluate(FloatingRateIndexEnum index) {
		return new BigDecimal("0.0875");
	}
}
