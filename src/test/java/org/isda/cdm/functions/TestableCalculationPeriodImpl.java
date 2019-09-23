package org.isda.cdm.functions;

import org.isda.cdm.CalculationPeriodData;
import org.isda.cdm.CalculationPeriodDates;

import com.google.inject.Singleton;

@Singleton
public class TestableCalculationPeriodImpl extends CalculationPeriodImpl {
	CalculationPeriod delegate;

	@Override
	public CalculationPeriodData evaluate(CalculationPeriodDates calculationPeriodDates) {
		if (delegate != null) {
			return delegate.evaluate(calculationPeriodDates);
		}
		return super.evaluate(calculationPeriodDates);
	}

	public void setDelegate(CalculationPeriod delegate) {
		this.delegate = delegate;
	}
}
