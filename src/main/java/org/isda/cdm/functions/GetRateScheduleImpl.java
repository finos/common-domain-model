package org.isda.cdm.functions;

import java.math.BigDecimal;
import java.util.List;

import org.isda.cdm.FloatingRateSpecification;
import org.isda.cdm.SpreadSchedule;
import org.isda.cdm.SpreadSchedule.SpreadScheduleBuilder;

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
