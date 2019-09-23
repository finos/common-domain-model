package org.isda.cdm.services;

import java.math.BigDecimal;

import org.isda.cdm.functions.InterpolateForwardRateImpl.InterpolateForwardRateService;

import com.google.inject.Singleton;

@Singleton
public class TestableInterpolateForwardRateService implements InterpolateForwardRateService {
	private BigDecimal defaultRate = new BigDecimal("0.8675");

	@Override
	public BigDecimal get() {
		return defaultRate;
	}

	public void setDefaultRate(BigDecimal defaultRate) {
		this.defaultRate = defaultRate;
	}
}