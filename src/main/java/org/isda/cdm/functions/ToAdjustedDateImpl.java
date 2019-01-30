package org.isda.cdm.functions;

import org.isda.cdm.AdjustableOrRelativeDate;
import org.isda.cdm.functions.ToAdjustedDate.CalculationResult;

import com.rosetta.model.lib.records.DateImpl;;

public class ToAdjustedDateImpl {
	
	public CalculationResult execute(AdjustableOrRelativeDate terminationDate) {
        return new CalculationResult().setAdjustedDate(new DateImpl(terminationDate.getAdjustableDate().getAdjustedDate().getValue()));
	}
}
