package com.rosetta.model.functions;

import com.rosetta.model.SpreadSchedule;
import com.rosetta.model.functions.CurrentSchedule.Result;

import java.util.List;

public class CurrentScheduleImpl {

	public Result execute(com.rosetta.model.FloatingRateCalculation floatingRateCalculation) {
        List<SpreadSchedule> spreadSchedules = floatingRateCalculation.getSpreadSchedule();

        if (spreadSchedules != null && !spreadSchedules.isEmpty()) {
            return new Result().setSchedule(spreadSchedules.get(0));
        } else {
            throw new CurrentScheduleImplException("No rate schedule found on leg");
        }
	}

	private static class CurrentScheduleImplException extends RuntimeException {
        private CurrentScheduleImplException(String message) {
            super(message);
        }
    }
}
