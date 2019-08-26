package org.isda.cdm.functions;

import java.math.BigDecimal;

import org.isda.cdm.EquityPayout;
import org.isda.cdm.ResetPrimitive;

import com.rosetta.model.lib.records.Date;

public class EquityResetImpl extends EquityReset {
	
	@Override
	protected ResetPrimitive doEvaluate(EquityPayout equityPayout, BigDecimal observation, Date date) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
