package org.isda.cdm.functions;

import java.math.BigDecimal;

import org.isda.cdm.ContractualQuantity;

public class GreaterThanImpl extends GreaterThan {
	
	@Override
	protected Boolean doEvaluate(ContractualQuantity contractualQuantity, BigDecimal scalar) {
		return contractualQuantity != null;
	}
}
