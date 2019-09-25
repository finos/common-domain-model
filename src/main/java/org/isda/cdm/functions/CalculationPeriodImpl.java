package org.isda.cdm.functions;

import static org.isda.cdm.functions.CdmToStrataMapper.getFrequency;
import static org.isda.cdm.functions.CdmToStrataMapper.getRollConvention;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;

import org.isda.cdm.CalculationPeriodData;
import org.isda.cdm.CalculationPeriodData.CalculationPeriodDataBuilder;
import org.isda.cdm.CalculationPeriodDates;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.PeriodicSchedule;
import com.opengamma.strata.basics.schedule.Schedule;
import com.opengamma.strata.basics.schedule.SchedulePeriod;
import com.opengamma.strata.basics.schedule.StubConvention;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

public class CalculationPeriodImpl extends CalculationPeriod {
	
	@Override
	protected CalculationPeriodDataBuilder doEvaluate(CalculationPeriodDates calculationPeriodDates, Date date) {	
		PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
				calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate().toLocalDate(),
				calculationPeriodDates.getTerminationDate().getAdjustableDate().getUnadjustedDate().toLocalDate(),
				getFrequency(calculationPeriodDates),
				BusinessDayAdjustment.NONE,
				StubConvention.NONE,
				getRollConvention(calculationPeriodDates));

		Schedule schedule = periodicSchedule.createSchedule(ReferenceData.minimal());

		SchedulePeriod targetPeriod = schedule.getPeriods().stream()
				.filter(period -> !(toLocalDate(date).isBefore(period.getStartDate())) && !toLocalDate(date).isAfter(period.getEndDate()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Date " + date.toString() + " not within schedule"));

		int daysThatAreInLeapYear = 0;
		for (LocalDate startDate = targetPeriod.getStartDate(); startDate.isBefore(targetPeriod.getEndDate()); startDate = startDate.plusDays(1)) {
			if (IsoChronology.INSTANCE.isLeapYear(startDate.getYear())) {
				daysThatAreInLeapYear++;
			}
		}
		
		return CalculationPeriodData.builder()
			.setStartDate(new DateImpl(targetPeriod.getStartDate()))
			.setEndDate(new DateImpl(targetPeriod.getEndDate()))
			.setDaysInLeapYearPeriod(daysThatAreInLeapYear)
			.setDaysInPeriod((int) ChronoUnit.DAYS.between(targetPeriod.getStartDate(), targetPeriod.getEndDate()));
	}
	
	private LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}	
}
