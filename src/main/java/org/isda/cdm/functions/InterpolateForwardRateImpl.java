package org.isda.cdm.functions;

import java.math.BigDecimal;

import org.isda.cdm.ForwardPayout;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class InterpolateForwardRateImpl extends InterpolateForwardRate {

	@Override
	protected BigDecimal doEvaluate(ForwardPayout forward) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}
}
