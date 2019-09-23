package org.isda.cdm.functions;

import org.isda.cdm.Contract;
import org.isda.cdm.Event;
import org.isda.cdm.Event.EventBuilder;

public class EquityResetEventImpl extends EquityResetEvent {
	
	@Override
	protected EventBuilder doEvaluate(Contract contract, Event observation) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
