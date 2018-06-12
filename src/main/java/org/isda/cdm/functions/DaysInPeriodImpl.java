package org.isda.cdm.functions;

import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.functions.DaysInPeriod._Result;
import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DaysInPeriodImpl {
	
	public _Result execute(org.isda.cdm.CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, LocalDate.now());
	}

	public _Result execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		CalculationPeriod._Result execute = new CalculationPeriodImpl().execute(calculationPeriodDates, referenceDate);
		long days = ChronoUnit.DAYS.between(toLocalDate(execute.getStartDate()), toLocalDate(execute.getEndDate()));
		return new _Result().setDays((int) days);
	}

	private LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}
}
