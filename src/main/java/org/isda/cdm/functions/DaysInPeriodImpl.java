package org.isda.cdm.functions;

import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.functions.DaysInPeriod.CalculationResult;
import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DaysInPeriodImpl {
	
	public CalculationResult execute(org.isda.cdm.CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, LocalDate.now());
	}

	public CalculationResult execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		CalculationPeriod.CalculationResult execute = new CalculationPeriodImpl().execute(calculationPeriodDates, referenceDate);
		long days = ChronoUnit.DAYS.between(toLocalDate(execute.getStartDate()), toLocalDate(execute.getEndDate()));
		return new CalculationResult().setDays((int) days);
	}

	private LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}
}
