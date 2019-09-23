package org.isda.cdm.functions;

import org.isda.cdm.Contract;
import org.isda.cdm.Event;
import org.isda.cdm.Event.EventBuilder;

public class CashFlowTransferEventImpl extends CashFlowTransferEvent {
	
	@Override
	protected EventBuilder doEvaluate(Contract contract, Event reset) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
