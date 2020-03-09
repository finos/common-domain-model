package org.isda.cdm.calculation.functions;

import java.math.BigDecimal;

import org.isda.cdm.FloatingRateSpecification;
import org.isda.cdm.functions.GetRateSchedule;

public class GetRateScheduleImpl extends GetRateSchedule {

	@Override
	protected BigDecimal doEvaluate(FloatingRateSpecification floatingRateCalculation) {
		return new BigDecimal("0");
	}
}
