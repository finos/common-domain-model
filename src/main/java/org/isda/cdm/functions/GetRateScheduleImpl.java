package org.isda.cdm.functions;

import org.isda.cdm.SpreadSchedule;
import  org.isda.cdm.functions.GetRateSchedule._Result;

import java.math.BigDecimal;
import java.util.List;

public class GetRateScheduleImpl {

	public _Result execute(org.isda.cdm.FloatingRateCalculation floatingRateCalculation) {
		List<SpreadSchedule> spreadSchedules = floatingRateCalculation.getSpreadSchedule();

		if (spreadSchedules != null && !spreadSchedules.isEmpty()) {
			return new _Result().setSchedule(spreadSchedules.get(0));
		} else {
			return new _Result().setSchedule((SpreadSchedule) SpreadSchedule.builder()
					.setInitialValue(new BigDecimal("0")).build());
		}
	}
}
