package com.rosetta.model.functions;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.*;
import com.rosetta.model.CalculationPeriodDates;
import com.rosetta.model.functions.CalculationPeriod.Result;
import com.rosetta.model.lib.records.DateImpl;

import java.time.LocalDate;

import static com.rosetta.model.functions.CdmToStrataMapper.getFrequency;
import static com.rosetta.model.functions.CdmToStrataMapper.getRollConvention;

public class CalculationPeriodImpl {

	public Result execute(CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, LocalDate.now());
	}

	Result execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
				calculationPeriodDates.getEffectiveDate().getUnadjustedDate(),
				calculationPeriodDates.getTerminationDate().getUnadjustedDate(),
				getFrequency(calculationPeriodDates),
				BusinessDayAdjustment.NONE,
				StubConvention.NONE,
				getRollConvention(calculationPeriodDates));

		Schedule schedule = periodicSchedule.createSchedule(ReferenceData.minimal());

		SchedulePeriod targetPeriod = schedule.getPeriods().stream()
				.filter(period -> period.getStartDate().compareTo(referenceDate) == 0 || period.getStartDate().isBefore(referenceDate))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Date " + referenceDate.toString() + "not within schedule"));

		return new Result()
				.setStartDate(new DateImpl(targetPeriod.getStartDate()))
				.setEndDate(new DateImpl(targetPeriod.getEndDate()));
	}
}
