package org.isda.cdm.functions;

import com.google.inject.Singleton;
import org.isda.cdm.ForwardPayout;

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