package org.isda.cdm.functions;

import org.isda.cdm.ContractualQuantity;
import org.isda.cdm.ContractualQuantity.ContractualQuantityBuilder;

public class PlusImpl extends Plus {
	
	@Override
	protected ContractualQuantityBuilder doEvaluate(ContractualQuantity q1, ContractualQuantity q2) {
		return ContractualQuantity.builder();
	}
}
