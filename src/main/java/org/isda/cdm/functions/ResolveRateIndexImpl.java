package org.isda.cdm.functions;

import  org.isda.cdm.functions.ResolveRateIndex.CalculationResult;

import java.math.BigDecimal;

public class ResolveRateIndexImpl {
	
	public CalculationResult execute(org.isda.cdm.FloatingRateIndexEnum index){
		// TODO: implement some in-memory persistence and retrieve values from there instead
		BigDecimal rate = new BigDecimal("0.0875");
		return new CalculationResult().setRate(rate);
	}
}
