package org.isda.cdm.functions;

import java.math.BigDecimal;

import org.isda.cdm.Equity;
import org.isda.cdm.TimeZone;

import com.rosetta.model.lib.records.Date;

public class EquitySpotImpl extends EquitySpot {
	
	@Override
	protected BigDecimal doEvaluate(Equity equity, Date date, TimeZone time) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
