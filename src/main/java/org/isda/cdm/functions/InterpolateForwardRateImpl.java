package org.isda.cdm.functions;

import java.math.BigDecimal;

import org.isda.cdm.ForwardPayout;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class InterpolateForwardRateImpl extends InterpolateForwardRate {

	@Inject
	InterpolateForwardRateService valueProvider;

	@Override
	protected BigDecimal doEvaluate(ForwardPayout forward) {
		return valueProvider.get();
	}

	public interface InterpolateForwardRateService extends Provider<BigDecimal> {
	}

}
