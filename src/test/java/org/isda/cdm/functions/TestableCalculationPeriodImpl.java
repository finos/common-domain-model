package org.isda.cdm.functions;

import org.isda.cdm.CalculationPeriodData;
import org.isda.cdm.CalculationPeriodDates;

import com.google.inject.Singleton;
import com.rosetta.model.lib.records.Date;

@Singleton
public class TestableCalculationPeriodImpl extends CalculationPeriodImpl {
	CalculationPeriod delegate;

	@Override
	public CalculationPeriodData evaluate(CalculationPeriodDates calculationPeriodDates, Date date) {
		if (delegate != null) {
			return delegate.evaluate(calculationPeriodDates, date);
		}
		return super.evaluate(calculationPeriodDates, date);
	}

	public void setDelegate(CalculationPeriod delegate) {
		this.delegate = delegate;
	}
}
