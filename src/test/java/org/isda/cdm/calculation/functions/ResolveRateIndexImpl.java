package org.isda.cdm.calculation.functions;

import org.isda.cdm.functions.ResolveRateIndex;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;

import java.math.BigDecimal;

public class ResolveRateIndexImpl extends ResolveRateIndex {

	@Override
	protected BigDecimal doEvaluate(FloatingRateIndexEnum index) {
		return new BigDecimal("0.0875");
	}
}
