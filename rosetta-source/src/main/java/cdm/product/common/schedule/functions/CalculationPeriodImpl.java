package cdm.product.common.schedule.functions;

import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodData.CalculationPeriodDataBuilder;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.common.collect.Iterables;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.PeriodicSchedule;
import com.opengamma.strata.basics.schedule.Schedule;
import com.opengamma.strata.basics.schedule.SchedulePeriod;
import com.opengamma.strata.basics.schedule.StubConvention;
import com.rosetta.model.lib.records.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static cdm.product.common.schedule.functions.AdjustableDateUtils.adjustDate;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getFrequency;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getRollConvention;

public class CalculationPeriodImpl extends CalculationPeriod {

    private static Logger LOGGER = LoggerFactory.getLogger(CalculationPeriod.class);

    private final BiPredicate<Date, SchedulePeriod> STARTDATE_EQ_OR_AFTER = (date, period) -> toLocalDate(date).isEqual(period.getStartDate()) || toLocalDate(date).isAfter(period.getStartDate());

    @Override
    protected CalculationPeriodDataBuilder doEvaluate(CalculationPeriodDates calculationPeriodDates, Date date) {
        CalculationPeriodDataBuilder builder = CalculationPeriodData.builder();

        Date adjustedStartDate = adjustDate(calculationPeriodDates.getEffectiveDate());
        Date adjustedEndDate = adjustDate(calculationPeriodDates.getTerminationDate());

        if (adjustedStartDate == null) {
            LOGGER.warn("Can not build CalculationPeriodData as no adjusted start date specified.");
            return builder;
        }
        if (adjustedEndDate == null) {
            LOGGER.warn("Can not build CalculationPeriodData as no adjusted end date specified.");
            return builder;
        }

        Optional<SchedulePeriod> optionalSchedulePeriod =
                getSchedulePeriod(calculationPeriodDates, date, adjustedStartDate.toLocalDate(), adjustedEndDate.toLocalDate());
        if (!optionalSchedulePeriod.isPresent()) {
            LOGGER.warn("Can not build CalculationPeriodData as no targetPeriod could be found.");
            return builder;
        }
        SchedulePeriod targetPeriod = optionalSchedulePeriod.get();

        int daysThatAreInLeapYear = getDaysThatAreInLeapYear(targetPeriod);

        return builder
                .setStartDate(Date.of(targetPeriod.getStartDate()))
                .setEndDate(Date.of(targetPeriod.getEndDate()))
                .setDaysInLeapYearPeriod(daysThatAreInLeapYear)
                .setDaysInPeriod((int) ChronoUnit.DAYS.between(targetPeriod.getStartDate(), targetPeriod.getEndDate()))
                .setIsFirstPeriod(false)
                .setIsLastPeriod(false);

    }

    private int getDaysThatAreInLeapYear(SchedulePeriod targetPeriod) {
        int daysThatAreInLeapYear = 0;
        for (LocalDate startDate = targetPeriod.getStartDate(); startDate.isBefore(targetPeriod.getEndDate()); startDate = startDate.plusDays(1)) {
            if (IsoChronology.INSTANCE.isLeapYear(startDate.getYear())) {
                daysThatAreInLeapYear++;
            }
        }
        return daysThatAreInLeapYear;
    }

    private Optional<SchedulePeriod> getSchedulePeriod(CalculationPeriodDates calculationPeriodDates, Date date, LocalDate adjustedStartDate, LocalDate adjustedEndDate) {
        Schedule schedule = getSchedule(calculationPeriodDates, adjustedStartDate, adjustedEndDate);

        if (toLocalDate(date).isEqual(schedule.getEndDate())) {
            return Optional.ofNullable(Iterables.getLast(schedule.getPeriods()));
        }
        List<SchedulePeriod> targetPeriods = schedule.getPeriods().stream()
                .filter(period -> STARTDATE_EQ_OR_AFTER.test(date, period) && toLocalDate(date).isBefore(period.getEndDate()))
                .collect(Collectors.toList());

        if (targetPeriods.size() != 1) {
            LOGGER.warn("Date " + date.toString() + " not within schedule " + schedule.getPeriods());
            return Optional.empty();
        }

        return Optional.of(Iterables.getOnlyElement(targetPeriods));
    }


    private Schedule getSchedule(CalculationPeriodDates calculationPeriodDates, LocalDate adjustedStartDate, LocalDate adjustedEndDate) {
        // The api expects unadjusted dates but we are passing in adjusted dates and using BusinessDayAdjustment.NONE.
        PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
                adjustedStartDate,
                adjustedEndDate,
                getFrequency(calculationPeriodDates),
                BusinessDayAdjustment.NONE,
                StubConvention.NONE,
                getRollConvention(calculationPeriodDates));

        Schedule schedule = periodicSchedule.createSchedule(ReferenceData.minimal());
        return schedule;
    }

    private LocalDate toLocalDate(Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }
}
