package com.rosetta.model.functions;

import com.rosetta.model.SpreadSchedule;
import  com.rosetta.model.functions.GetRateSchedule.Result;

import java.math.BigDecimal;
import java.util.List;

public class GetRateScheduleImpl {

	public Result execute(com.rosetta.model.FloatingRateCalculation floatingRateCalculation) {
		List<SpreadSchedule> spreadSchedules = floatingRateCalculation.getSpreadSchedule();

		if (spreadSchedules != null && !spreadSchedules.isEmpty()) {
			return new Result().setSchedule(spreadSchedules.get(0));
		} else {
			return new Result().setSchedule((SpreadSchedule) SpreadSchedule.builder()
					.setInitialValue(new BigDecimal("0")).build());
		}
	}
}
