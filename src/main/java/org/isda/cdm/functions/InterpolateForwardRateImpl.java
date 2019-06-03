package org.isda.cdm.functions;

import org.isda.cdm.ForwardPayout;
import org.isda.cdm.functions.InterpolateForwardRate.CalculationResult;

import java.math.BigDecimal;

public class InterpolateForwardRateImpl implements InterpolateForwardRate {
	
	public CalculationResult execute(ForwardPayout forward) {
		return new CalculationResult()
				.setResult(new BigDecimal("0.8675"));
	}
	
}
