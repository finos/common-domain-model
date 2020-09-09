package org.isda.cdm.functions;

import com.google.inject.Singleton;
import com.rosetta.model.lib.records.Date;

import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.functions.CalculationPeriod;

/**
 * Used in DayCountFractionEnumTest
 */
@Singleton
public class TestableCalculationPeriod extends CalculationPeriodImpl {
	
	private CalculationPeriod delegate;

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
