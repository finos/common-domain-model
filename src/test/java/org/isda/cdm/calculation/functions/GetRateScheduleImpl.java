package org.isda.cdm.calculation.functions;

import org.isda.cdm.FloatingRateSpecification;
import org.isda.cdm.SpreadSchedule;
import org.isda.cdm.SpreadSchedule.SpreadScheduleBuilder;
import org.isda.cdm.functions.GetRateSchedule;

import java.math.BigDecimal;
import java.util.List;

public class GetRateScheduleImpl extends GetRateSchedule {

	@Override
	protected SpreadScheduleBuilder doEvaluate(FloatingRateSpecification floatingRateCalculation) {
		List<SpreadSchedule> spreadSchedules = floatingRateCalculation.getSpreadSchedule();
		if (spreadSchedules != null && !spreadSchedules.isEmpty()) {
			return spreadSchedules.get(0).toBuilder();
		} else {
			return SpreadSchedule.builder().setInitialValue(new BigDecimal("0"));
		}
	}
}
