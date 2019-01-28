package org.isda.cdm.functions;

import org.isda.cdm.SpreadSchedule;
import  org.isda.cdm.functions.GetRateSchedule.CalculationResult;

import java.math.BigDecimal;
import java.util.List;

public class GetRateScheduleImpl implements GetRateSchedule {

	public CalculationResult execute(org.isda.cdm.FloatingRateSpecification floatingRateCalculation) {
		List<SpreadSchedule> spreadSchedules = floatingRateCalculation.getSpreadSchedule();

		if (spreadSchedules != null && !spreadSchedules.isEmpty()) {
			return new CalculationResult().setSchedule(spreadSchedules.get(0));
		} else {
			return new CalculationResult().setSchedule((SpreadSchedule) SpreadSchedule.builder()
					.setInitialValue(new BigDecimal("0")).build());
		}
	}
}
