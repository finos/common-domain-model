package org.finos.cdm.example.functions.impls;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.product.asset.functions.ResolveRateIndex;

import java.math.BigDecimal;

/**
 * Used in calculation tests
 */
public class NoOpResolveRateIndexImpl extends ResolveRateIndex {

	@Override
	protected BigDecimal doEvaluate(FloatingRateIndexEnum index) {
		return new BigDecimal("0.0875");
	}
}
