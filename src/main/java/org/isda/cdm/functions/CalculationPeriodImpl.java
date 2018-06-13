package org.isda.cdm.functions;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.*;
import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.functions.CalculationPeriod._Result;
import com.rosetta.model.lib.records.DateImpl;

import static org.isda.cdm.functions.CdmToStrataMapper.getFrequency;
import static org.isda.cdm.functions.CdmToStrataMapper.getRollConvention;

import java.time.LocalDate;

public class CalculationPeriodImpl {

	public _Result execute(CalculationPeriodDates calculationPeriodDates) {
		return execute(calculationPeriodDates, LocalDate.now());
	}

	_Result execute(CalculationPeriodDates calculationPeriodDates, LocalDate referenceDate) {
		PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
				calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate(),
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

		return new _Result()
				.setStartDate(new DateImpl(targetPeriod.getStartDate()))
				.setEndDate(new DateImpl(targetPeriod.getEndDate()));
	}
}
