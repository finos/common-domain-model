package org.isda.cdm.functions;

import  org.isda.cdm.functions.ResolveRateIndex._Result;

import java.math.BigDecimal;

public class ResolveRateIndexImpl {
	
	public _Result execute(org.isda.cdm.FloatingRateIndexEnum index){
		// TODO: implement some in-memory persistence and retrieve values from there instead
		BigDecimal rate = new BigDecimal("0.0875");
		return new _Result().setRate(rate);
	}
}
