package org.isda.cdm.calculation.functions;

import com.google.inject.Singleton;
import org.isda.cdm.ForwardPayout;
import org.isda.cdm.functions.InterpolateForwardRate;

import java.math.BigDecimal;

@Singleton
public class TestableInterpolateForwardRate extends InterpolateForwardRate {

	private BigDecimal value = new BigDecimal("0.8675");

	@Override
	protected BigDecimal doEvaluate(ForwardPayout forward) {
		return value;
	}

	public void setValue(BigDecimal defaultRate) {
		this.value = defaultRate;
	}
}