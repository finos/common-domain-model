package org.isda.cdm.functions;

import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.functions.DaysInPeriod.Result;
import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DaysInPeriodImpl {
	
	public Result execute(org.isda.cdm.CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, LocalDate.now());
	}

	public Result execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		CalculationPeriod.Result execute = new CalculationPeriodImpl().execute(calculationPeriodDates, referenceDate);
		long days = ChronoUnit.DAYS.between(toLocalDate(execute.getStartDate()), toLocalDate(execute.getEndDate()));
		return new Result().setDays((int) days);
	}

	private LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}
}
