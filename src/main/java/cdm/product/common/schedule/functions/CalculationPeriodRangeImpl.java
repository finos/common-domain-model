package cdm.product.common.schedule.functions;

import cdm.base.datetime.BusinessDayAdjustments;
import cdm.product.common.schedule.CalculationPeriodData;
import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

public class CalculationPeriodRangeImpl extends CalculationPeriodRange {
    @Override
    protected CalculationPeriodData.CalculationPeriodDataBuilder doEvaluate(Date startDate, Date endDate, BusinessDayAdjustments dateAdjustments) {
        return CalculationPeriodData.builder()
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setDaysInLeapYearPeriod(getDaysThatAreInLeapYear(startDate.toLocalDate(), endDate.toLocalDate()))
                .setDaysInPeriod((int) DAYS.between(startDate.toLocalDate(), endDate.toLocalDate()))
                .setIsFirstPeriod(false)
                .setIsLastPeriod(false);
    }

    private int getDaysThatAreInLeapYear(LocalDate periodStartDate, LocalDate periodEndDate) {
        return (int) Stream.iterate(periodStartDate, date -> date.plusDays(1))
				.limit(DAYS.between(periodStartDate, periodEndDate))
                .filter(d -> IsoChronology.INSTANCE.isLeapYear(d.getYear())).count();
    }
}
