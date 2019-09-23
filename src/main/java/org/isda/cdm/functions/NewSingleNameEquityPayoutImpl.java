package org.isda.cdm.functions;

import org.isda.cdm.Equity;
import org.isda.cdm.EquityPayout.EquityPayoutBuilder;
import org.isda.cdm.EquitySwapMasterConfirmation2018;

public class NewSingleNameEquityPayoutImpl extends NewSingleNameEquityPayout {
	
	@Override
	protected EquityPayoutBuilder doEvaluate(Equity underlier, EquitySwapMasterConfirmation2018 masterConfirmation) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
