package org.isda.cdm.functions;

import java.math.BigDecimal;

import org.isda.cdm.ForwardPayout;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class InterpolateForwardRateImpl extends InterpolateForwardRate {
	private BigDecimal rate = new BigDecimal("0.8675");

	static public final class Factory {
		@Inject
		Provider<InterpolateForwardRateImpl> provider;

		public InterpolateForwardRate create(BigDecimal rate) {
			InterpolateForwardRateImpl rateImpl = provider.get();
			rateImpl.rate = rate;
			return rateImpl;
		}
	}

	@Override
	protected BigDecimal doEvaluate(ForwardPayout forward) {
		return rate;
	}
}
