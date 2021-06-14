package cdm.product.common.schedule.functions;

import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodData.CalculationPeriodDataBuilder;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.*;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static cdm.product.common.schedule.functions.CdmToStrataMapper.getFrequency;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getRollConvention;

public class CalculationPeriodImpl extends CalculationPeriod {

    private final BiPredicate<Date, SchedulePeriod> STARTDATE_EQ_OR_AFTER = (date, period) -> toLocalDate(date).isEqual(period.getStartDate()) || toLocalDate(date).isAfter(period.getStartDate());

    @Override
    protected CalculationPeriodDataBuilder doEvaluate(CalculationPeriodDates calculationPeriodDates, Date date) {
        LocalDate effDate = calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate().toLocalDate();
        LocalDate termDate = calculationPeriodDates.getTerminationDate().getAdjustableDate().getUnadjustedDate().toLocalDate();
        Frequency freq = getFrequency(calculationPeriodDates);
        RollConvention roll = getRollConvention(calculationPeriodDates);

        PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
                effDate,
                termDate,
                freq,
                BusinessDayAdjustment.NONE,
                StubConvention.NONE,
                roll);
        /*
    	PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
				calculationPeriodDates.getEffectiveDate().getAdjustableDate().getUnadjustedDate().toLocalDate(),
				calculationPeriodDates.getTerminationDate().getAdjustableDate().getUnadjustedDate().toLocalDate(),
				getFrequency(calculationPeriodDates),
				BusinessDayAdjustment.NONE,
				StubConvention.NONE,
				getRollConvention(calculationPeriodDates));
         */

		Schedule schedule = periodicSchedule.createSchedule(ReferenceData.minimal());

        SchedulePeriod targetPeriod = schedule.getPeriods().stream()
                .filter(period -> STARTDATE_EQ_OR_AFTER.test(date, period) && toLocalDate(date).isBefore(period.getEndDate()))
                .collect(toSingleton(date));

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
                .setDaysInPeriod((int) ChronoUnit.DAYS.between(targetPeriod.getStartDate(), targetPeriod.getEndDate()))
                .setIsFirstPeriod(false)
                .setIsLastPeriod(false);

    }

    private static <T> Collector<T, ?, T> toSingleton(Date date) {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalArgumentException("Date " + date.toString() + " not within schedule");
                    }
                    return list.get(0);
                }
        );
    }

	private LocalDate toLocalDate(Date date) {
		return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
	}	
}
