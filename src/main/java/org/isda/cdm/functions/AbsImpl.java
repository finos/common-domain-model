package org.isda.cdm.functions;

import java.math.BigDecimal;

public class AbsImpl implements Abs {
	
	public CalculationResult execute(BigDecimal x) {
		return new CalculationResult().setResult(x.abs());
	}
}
