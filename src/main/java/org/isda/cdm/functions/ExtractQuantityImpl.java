package org.isda.cdm.functions;

import org.isda.cdm.ContractualQuantity;
import org.isda.cdm.ContractualQuantity.ContractualQuantityBuilder;
import org.isda.cdm.Trade;

public class ExtractQuantityImpl extends ExtractQuantity {
	
	@Override
	protected ContractualQuantityBuilder doEvaluate(Trade trade) {
		return ContractualQuantity.builder();
	}
}
