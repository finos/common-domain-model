package org.isda.cdm.calculation.functions;

import java.math.BigDecimal;

import com.google.inject.Singleton;

import cdm.event.position.functions.InterpolateForwardRate;
import cdm.product.template.ForwardPayout;

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