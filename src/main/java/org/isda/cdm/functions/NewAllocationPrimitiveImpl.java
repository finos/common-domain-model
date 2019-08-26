package org.isda.cdm.functions;

import java.math.BigDecimal;
import org.isda.cdm.AllocationInstructions;
import org.isda.cdm.AllocationPrimitive;
import org.isda.cdm.Trade;

public class NewAllocationPrimitiveImpl extends NewAllocationPrimitive {
	
	@Override
	protected AllocationPrimitive doEvaluate(Trade trade, AllocationInstructions allocationInstructions, BigDecimal precision) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
