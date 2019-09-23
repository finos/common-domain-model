package org.isda.cdm.functions;

import org.isda.cdm.Equity;
import org.isda.cdm.EquitySwapMasterConfirmation2018;
import org.isda.cdm.Product.ProductBuilder;

public class NewEquitySwapProductImpl extends NewEquitySwapProduct {
	
	@Override
	protected ProductBuilder doEvaluate(Equity underlier, EquitySwapMasterConfirmation2018 masterConfirmation) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
