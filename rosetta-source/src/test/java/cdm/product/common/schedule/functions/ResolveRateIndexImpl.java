package cdm.product.common.schedule.functions;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.product.asset.functions.ResolveRateIndex;

import java.math.BigDecimal;

public class ResolveRateIndexImpl extends ResolveRateIndex {

	@Override
	protected BigDecimal doEvaluate(FloatingRateIndexEnum index) {
		return new BigDecimal("0.0875");
	}
}
