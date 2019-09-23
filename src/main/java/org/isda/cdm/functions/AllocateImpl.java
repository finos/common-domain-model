package org.isda.cdm.functions;

import java.math.BigDecimal;
import org.isda.cdm.AllocationInstructions;
import org.isda.cdm.Event;
import org.isda.cdm.Trade;

public class AllocateImpl extends Allocate {
	
	@Override
	protected Event.EventBuilder doEvaluate(Trade trade, AllocationInstructions allocationInstructions, BigDecimal precision) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
