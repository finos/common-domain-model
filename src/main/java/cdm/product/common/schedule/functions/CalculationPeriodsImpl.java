package cdm.product.common.schedule.functions;

import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodData.CalculationPeriodDataBuilder;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.PeriodicSchedule;
import com.opengamma.strata.basics.schedule.Schedule;
import com.opengamma.strata.basics.schedule.SchedulePeriod;
import com.opengamma.strata.basics.schedule.StubConvention;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static cdm.product.common.schedule.functions.AdjustableDateUtils.adjustDate;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getFrequency;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getRollConvention;

/**
 * TODO - Move this to the CDM
 */
public class CalculationPeriodsImpl extends CalculationPeriods {

    private static Logger LOGGER = LoggerFactory.getLogger(CalculationPeriod.class);

    private final BiPredicate<Date, SchedulePeriod> STARTDATE_EQ_OR_AFTER = (date, period) -> toLocalDate(date).isEqual(period.getStartDate()) || toLocalDate(date).isAfter(period.getStartDate());

    @Override
    protected List<CalculationPeriodData.CalculationPeriodDataBuilder> doEvaluate(CalculationPeriodDates calculationPeriodDates) {

        LocalDate adjustedStartDate = adjustDate(calculationPeriodDates.getEffectiveDate());
        LocalDate adjustedEndDate = adjustDate(calculationPeriodDates.getTerminationDate());

        List<CalculationPeriodData.CalculationPeriodDataBuilder> returnVal = new ArrayList<>();

        if (adjustedStartDate == null) {
            LOGGER.warn("Can not build CalculationPeriodData as no adjusted start date specified.");
            return returnVal;
        }
        if (adjustedEndDate == null) {
            LOGGER.warn("Can not build CalculationPeriodData as no adjusted end date specified.");
            return returnVal;
        }

        Schedule schedule = getSchedule(calculationPeriodDates, adjustedStartDate, adjustedEndDate);
        ImmutableList<SchedulePeriod> periods = schedule.getPeriods();

        for (SchedulePeriod period : periods) {
            CalculationPeriodDataBuilder calcPeriod = getCalcPeriod(period);
            returnVal.add(calcPeriod);
        }

        return returnVal;

    }

    private CalculationPeriodData.CalculationPeriodDataBuilder getCalcPeriod (SchedulePeriod targetPeriod) {
        CalculationPeriodDataBuilder builder = CalculationPeriodData.builder();
        int daysThatAreInLeapYear = getDaysThatAreInLeapYear(targetPeriod);

        return builder
                .setStartDate(new DateImpl(targetPeriod.getStartDate()))
                .setEndDate(new DateImpl(targetPeriod.getEndDate()))
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



    @NotNull
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
