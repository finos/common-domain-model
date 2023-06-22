package cdm.product.common.schedule.functions;

import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodData.CalculationPeriodDataBuilder;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.common.collect.ImmutableList;
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
import java.util.ArrayList;
import java.util.List;

import static cdm.product.common.schedule.functions.AdjustableDateUtils.adjustDate;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getFrequency;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getRollConvention;

public class CalculationPeriodsImpl extends CalculationPeriods {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationPeriod.class);

    @Override
    protected List<CalculationPeriodData.CalculationPeriodDataBuilder> doEvaluate(CalculationPeriodDates calculationPeriodDates) {

        Date adjustedStartDate = adjustDate(calculationPeriodDates.getEffectiveDate());
        Date adjustedEndDate = adjustDate(calculationPeriodDates.getTerminationDate());

        List<CalculationPeriodData.CalculationPeriodDataBuilder> returnVal = new ArrayList<>();

        if (adjustedStartDate == null) {
            LOGGER.warn("Can not build CalculationPeriodData as no adjusted start date specified.");
            return returnVal;
        }
        if (adjustedEndDate == null) {
            LOGGER.warn("Can not build CalculationPeriodData as no adjusted end date specified.");
            return returnVal;
        }

        Schedule schedule = getSchedule(calculationPeriodDates, adjustedStartDate.toLocalDate(), adjustedEndDate.toLocalDate());
        ImmutableList<SchedulePeriod> periods = schedule.getPeriods();

        for (SchedulePeriod period : periods) {
            CalculationPeriodDataBuilder calcPeriod = getCalcPeriod(period);
            returnVal.add(calcPeriod);
        }

        return returnVal;
    }

    private CalculationPeriodData.CalculationPeriodDataBuilder getCalcPeriod(SchedulePeriod targetPeriod) {
        CalculationPeriodDataBuilder builder = CalculationPeriodData.builder();
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


    private Schedule getSchedule(CalculationPeriodDates calculationPeriodDates, LocalDate adjustedStartDate, LocalDate adjustedEndDate) {
        // The api expects unadjusted dates, but we are passing in adjusted dates and using BusinessDayAdjustment.NONE.
        PeriodicSchedule periodicSchedule = PeriodicSchedule.of(
                adjustedStartDate,
                adjustedEndDate,
                getFrequency(calculationPeriodDates),
                BusinessDayAdjustment.NONE,
                StubConvention.NONE,
                getRollConvention(calculationPeriodDates));

        return periodicSchedule.createSchedule(ReferenceData.minimal());
    }
}
