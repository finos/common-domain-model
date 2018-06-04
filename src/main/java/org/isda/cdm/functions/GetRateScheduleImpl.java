package org.isda.cdm.functions;

import org.isda.cdm.SpreadSchedule;
import  org.isda.cdm.functions.GetRateSchedule.Result;

import java.math.BigDecimal;
import java.util.List;

public class GetRateScheduleImpl {

	public Result execute(org.isda.cdm.FloatingRateCalculation floatingRateCalculation) {
		List<SpreadSchedule> spreadSchedules = floatingRateCalculation.getSpreadSchedule();

		if (spreadSchedules != null && !spreadSchedules.isEmpty()) {
			return new Result().setSchedule(spreadSchedules.get(0));
		} else {
			return new Result().setSchedule((SpreadSchedule) SpreadSchedule.builder()
					.setInitialValue(new BigDecimal("0")).build());
		}
	}
}
