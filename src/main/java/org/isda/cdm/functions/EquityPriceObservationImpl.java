package org.isda.cdm.functions;

import org.isda.cdm.AdjustableOrRelativeDate;
import org.isda.cdm.BusinessCenterTime;
import org.isda.cdm.DeterminationMethodEnum;
import org.isda.cdm.Equity;
import org.isda.cdm.ObservationPrimitive.ObservationPrimitiveBuilder;
import org.isda.cdm.TimeTypeEnum;

public class EquityPriceObservationImpl extends EquityPriceObservation {
	
	@Override
	protected ObservationPrimitiveBuilder doEvaluate(Equity equity, AdjustableOrRelativeDate valuationDate, BusinessCenterTime valuationTime, TimeTypeEnum timeType, DeterminationMethodEnum determinationMethod) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
