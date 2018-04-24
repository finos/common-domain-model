package com.rosetta.model.functions;

import  com.rosetta.model.functions.ResolveRateIndex.Result;

import java.math.BigDecimal;

public class ResolveRateIndexImpl {
	
	public Result execute(com.rosetta.model.FloatingRateIndexEnum index){
		// TODO: implement some in-memory persistence and retrieve values from there instead
		BigDecimal rate = new BigDecimal("0.0875");
		return new Result().setRate(rate);
	}
}
