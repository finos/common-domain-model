package org.isda.cdm.functions;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.*;
import org.isda.cdm.CalculationPeriodDates;

import com.rosetta.model.lib.records.DateImpl;

import static org.isda.cdm.functions.CdmToStrataMapper.getFrequency;
import static org.isda.cdm.functions.CdmToStrataMapper.getRollConvention;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;

public class CalculationPeriodImpl implements CalculationPeriod {

	private final LocalDate referenceDate;

	public CalculationPeriodImpl(LocalDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	@Override
	public CalculationResult execute(CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, referenceDate);
	}

	private CalculationResult execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
				calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate(),
				calculationPeriodDates.getTerminationDate().getAdjustableDate().getUnadjustedDate(),
				getFrequency(calculationPeriodDates),
				BusinessDayAdjustment.NONE,
				StubConvention.NONE,
				getRollConvention(calculationPeriodDates));

		Schedule schedule = periodicSchedule.createSchedule(ReferenceData.minimal());

		SchedulePeriod targetPeriod = schedule.getPeriods().stream()
				.filter(period -> !(referenceDate.isBefore(period.getStartDate())) && referenceDate.isBefore(period.getEndDate()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Date " + referenceDate.toString() + "not within schedule"));

		int daysThatAreInLeapYear = 0;
		for (LocalDate date = targetPeriod.getStartDate(); date.isBefore(targetPeriod.getEndDate()); date = date.plusDays(1)) {
			if (IsoChronology.INSTANCE.isLeapYear(date.getYear())) {
				daysThatAreInLeapYear++;
			}
		}
		
		return new CalculationResult()
				.setStartDate(new DateImpl(targetPeriod.getStartDate()))
				.setEndDate(new DateImpl(targetPeriod.getEndDate()))
				.setDaysInLeapYearPeriod(daysThatAreInLeapYear)
				.setDaysInPeriod((int) ChronoUnit.DAYS.between(targetPeriod.getStartDate(), targetPeriod.getEndDate()));
	}

}
