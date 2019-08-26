package org.isda.cdm.functions;

import static org.isda.cdm.functions.CdmToStrataMapper.getFrequency;
import static org.isda.cdm.functions.CdmToStrataMapper.getRollConvention;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;

import org.isda.cdm.CalculationPeriodDates;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.PeriodicSchedule;
import com.opengamma.strata.basics.schedule.Schedule;
import com.opengamma.strata.basics.schedule.SchedulePeriod;
import com.opengamma.strata.basics.schedule.StubConvention;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

public class CalculationPeriodImpl implements CalculationPeriod {

	private final Date referenceDate;

	public CalculationPeriodImpl(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	@Override
	public CalculationResult execute(CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, referenceDate.toLocalDate());
	}

	private CalculationResult execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
				calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate().toLocalDate(),
				calculationPeriodDates.getTerminationDate().getAdjustableDate().getUnadjustedDate().toLocalDate(),
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
