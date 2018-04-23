package com.rosetta.model.functions;

import  com.rosetta.model.functions.CalculationPeriod.Result;
import com.rosetta.model.lib.records.DateImpl;

public class CalculationPeriodImpl {
	
	public Result execute(com.rosetta.model.CalculationPeriodDates dates){
		return new Result().setStartDate(new DateImpl(1,3,2018)).setEndDate(new DateImpl(1,7,2018));
	}
}
