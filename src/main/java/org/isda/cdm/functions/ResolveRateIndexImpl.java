package org.isda.cdm.functions;

import  org.isda.cdm.functions.ResolveRateIndex.Result;

import java.math.BigDecimal;

public class ResolveRateIndexImpl {
	
	public Result execute(org.isda.cdm.FloatingRateIndexEnum index){
		// TODO: implement some in-memory persistence and retrieve values from there instead
		BigDecimal rate = new BigDecimal("0.0875");
		return new Result().setRate(rate);
	}
}
