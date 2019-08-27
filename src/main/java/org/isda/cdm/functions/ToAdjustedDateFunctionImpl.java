package org.isda.cdm.functions;

import org.isda.cdm.AdjustableOrRelativeDate;

import com.rosetta.model.lib.records.Date;

public class ToAdjustedDateFunctionImpl extends ToAdjustedDateFunction {
	
	@Override
	protected Date doEvaluate(AdjustableOrRelativeDate terminationDate) {
		return terminationDate.getAdjustableDate().getAdjustedDate().getValue();
	}
}
