package org.isda.cdm.functions;

import org.isda.cdm.CalculationPeriodDates;

import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DaysInPeriodImpl implements DaysInPeriod {

	private LocalDate referenceDate;

	public DaysInPeriodImpl(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	@Override
	public CalculationResult execute(org.isda.cdm.CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, referenceDate);
	}

	private CalculationResult execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		CalculationPeriod.CalculationResult execute = new CalculationPeriodImpl(referenceDate).execute(calculationPeriodDates);
		long days = ChronoUnit.DAYS.between(toLocalDate(execute.getStartDate()), toLocalDate(execute.getEndDate()));
		return new CalculationResult().setDays((int) days);
	}

	private LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}
}
