package cdm.product.common.schedule.functions;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.CalculationPeriodFrequency;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodData.CalculationPeriodDataBuilder;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.StubPeriodTypeEnum;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.*;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Implements calculation period evaluation logic for a given date within a schedule.
 *
 * <p>This class computes the specific {@link CalculationPeriodData} for a target date based on the
 * {@link CalculationPeriodDates} definition. It handles roll conventions, frequency parsing, stub
 * periods, and leap year adjustments.
 */
public class CalculationPeriodImpl extends CalculationPeriod {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationPeriod.class);

    @Override
    protected CalculationPeriodDataBuilder doEvaluate(CalculationPeriodDates calculationPeriodDates, Date date) {
        validateRequiredInput(calculationPeriodDates, date);

        LocalDate effectiveDate = extractDate(calculationPeriodDates.getEffectiveDate());
        LocalDate terminationDate = extractDate(calculationPeriodDates.getTerminationDate());

        validateDateRange(effectiveDate, terminationDate);

        if (isTargetDateOutOfRange(date, effectiveDate, terminationDate)) {
            LOGGER.warn("Date {} is out of schedule range [{}, {}]", date, effectiveDate, terminationDate);
            return CalculationPeriodData.builder();
        }

        RollConvention rollConvention = CdmToStrataMapper.getRollConvention(calculationPeriodDates);
        validateStubConfiguration(calculationPeriodDates, effectiveDate, terminationDate, rollConvention);

        Schedule schedule = getSchedule(calculationPeriodDates, effectiveDate, terminationDate, rollConvention);

        return findPeriodContainingDate(schedule.getPeriods(), date, effectiveDate, terminationDate);
    }

    private void validateRequiredInput(CalculationPeriodDates calculationPeriodDates, Date date) {
        checkNotNull(calculationPeriodDates, "calculationPeriodDates");
        checkNotNull(date, "date");

        CalculationPeriodFrequency freq =
                checkNotNull(calculationPeriodDates.getCalculationPeriodFrequency(), "calculationPeriodFrequency");
        checkNotNull(freq.getPeriod(), "calculationPeriodFrequency.period");
        checkNotNull(freq.getPeriodMultiplier(), "calculationPeriodFrequency.periodMultiplier");
        checkNotNull(freq.getRollConvention(), "calculationPeriodFrequency.rollConvention");

        validateDateField(calculationPeriodDates.getEffectiveDate(), "effectiveDate");
        validateDateField(calculationPeriodDates.getTerminationDate(), "terminationDate");
    }

    private void validateDateField(AdjustableOrRelativeDate adjustableOrRelativeDate, String fieldName) {
        checkNotNull(adjustableOrRelativeDate, fieldName);
        AdjustableDate adjustableDate =
                checkNotNull(adjustableOrRelativeDate.getAdjustableDate(), fieldName + ".adjustableDate");

        Date unadjusted = adjustableDate.getUnadjustedDate();
        Date adjusted =
                Optional.ofNullable(adjustableDate.getAdjustedDate())
                        .map(FieldWithMetaDate::getValue)
                        .orElse(null);

        if (unadjusted == null && adjusted == null || unadjusted != null && adjusted != null) {
            throw new IllegalArgumentException(
                    String.format("%s must have exactly either unadjustedDate or adjustedDate", fieldName));
        }
    }

    private static <T> T checkNotNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Missing required field: " + fieldName);
        }
        return value;
    }

    private LocalDate extractDate(AdjustableOrRelativeDate adjustableOrRelativeDate) {
        AdjustableDate adjustableDate = adjustableOrRelativeDate.getAdjustableDate();
        Date date = adjustableDate.getUnadjustedDate();
        if (date == null) {
            date = adjustableDate.getAdjustedDate().getValue();
        }
        return date.toLocalDate();
    }

    private void validateDateRange(LocalDate effectiveDate, LocalDate terminationDate) {
        if (effectiveDate.isAfter(terminationDate)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Effective date must be before termination date: effective=%s, termination=%s",
                            effectiveDate, terminationDate));
        }
    }

    // Checks if the target date is outside the range of effectiveDate (inclusive) and terminationDate (exclusive).
    private boolean isTargetDateOutOfRange(Date date, LocalDate effectiveDate, LocalDate terminationDate) {
        LocalDate targetDate = date.toLocalDate();
        return targetDate.isBefore(effectiveDate) || !targetDate.isBefore(terminationDate);
    }

   // Validates that the stub configuration is present if stub periods are implied by the effective and termination dates not aligning with the roll convention.
   // Throws an exception if the configuration is invalid.
    private void validateStubConfiguration(CalculationPeriodDates calculationPeriodDates,
                                           LocalDate effectiveDate, LocalDate terminationDate, RollConvention rollConvention) {

        if (isOnRoll(effectiveDate, rollConvention) && isOnRoll(terminationDate, rollConvention)) {
            return; // no stub implied
        }

        List<StubPeriodTypeEnum> stubTypes = calculationPeriodDates.getStubPeriodType();
        boolean hasShortInitial = stubTypes != null && stubTypes.contains(StubPeriodTypeEnum.SHORT_INITIAL);
        boolean hasLongInitial = stubTypes != null && stubTypes.contains(StubPeriodTypeEnum.LONG_INITIAL);
        boolean hasShortFinal = stubTypes != null && stubTypes.contains(StubPeriodTypeEnum.SHORT_FINAL);
        boolean hasLongFinal = stubTypes != null && stubTypes.contains(StubPeriodTypeEnum.LONG_FINAL);
        boolean hasInitialStubType = hasShortInitial || hasLongInitial;
        boolean hasFinalStubType = hasShortFinal || hasLongFinal;
        boolean hasFirstRegularStart = calculationPeriodDates.getFirstRegularPeriodStartDate() != null;
        boolean hasLastRegularEnd = calculationPeriodDates.getLastRegularPeriodEndDate() != null;

        if (hasShortInitial && hasLongInitial) {
            throw new IllegalArgumentException(
                    "stubPeriodType cannot contain both SHORT_INITIAL and LONG_INITIAL.");
        }
        if (hasShortFinal && hasLongFinal) {
            throw new IllegalArgumentException(
                    "stubPeriodType cannot contain both SHORT_FINAL and LONG_FINAL.");
        }

        if (!isOnRoll(effectiveDate, rollConvention) && !hasInitialStubType && !hasFirstRegularStart) {
            throw new IllegalArgumentException(String.format(
                    "effectiveDate %s is not aligned with roll convention %s, implying an initial stub. " +
                            "Either stubPeriodType or firstRegularPeriodStartDate must be specified.",
                    effectiveDate, rollConvention));
        }
        if (!isOnRoll(terminationDate, rollConvention) && !hasFinalStubType && !hasLastRegularEnd) {
            throw new IllegalArgumentException(String.format(
                    "terminationDate %s is not aligned with roll convention %s, implying a final stub. " +
                            "Either stubPeriodType or lastRegularPeriodEndDate must be specified.",
                    terminationDate, rollConvention));
        }
    }

    // checks if the given date is aligned with the specified roll convention.
    private static boolean isOnRoll(LocalDate date, RollConvention rollConvention) {
        return rollConvention.adjust(date).equals(date);
    }

    private Schedule getSchedule(
            CalculationPeriodDates calculationPeriodDates,
            LocalDate startDate,
            LocalDate endDate,
            RollConvention rollConvention) {

        Frequency frequency = CdmToStrataMapper.getFrequency(calculationPeriodDates);
        List<StubPeriodTypeEnum> stubTypes = calculationPeriodDates.getStubPeriodType();
        StubConvention stubConvention = CdmToStrataMapper.getStubConvention(stubTypes);

        LocalDate firstRegularStartDate = Optional.ofNullable(calculationPeriodDates.getFirstRegularPeriodStartDate())
                .map(Date::toLocalDate).orElse(null);
        LocalDate lastRegularEndDate = Optional.ofNullable(calculationPeriodDates.getLastRegularPeriodEndDate())
                .map(Date::toLocalDate).orElse(null);

        // For BOTH: auto-derive any missing boundary dates from the roll convention.
        // Strata requires the boundary dates to be specified explicitly for BOTH stub types.
        if (stubConvention == StubConvention.BOTH) {
            StubPeriodTypeEnum initialStubType = stubTypes.stream()
                    .filter(s -> s == StubPeriodTypeEnum.SHORT_INITIAL || s == StubPeriodTypeEnum.LONG_INITIAL)
                    .findFirst().orElse(null);
            StubPeriodTypeEnum finalStubType = stubTypes.stream()
                    .filter(s -> s == StubPeriodTypeEnum.SHORT_FINAL || s == StubPeriodTypeEnum.LONG_FINAL)
                    .findFirst().orElse(null);

            if (firstRegularStartDate == null && !isOnRoll(startDate, rollConvention)) {
                firstRegularStartDate = deriveFirstRegularBoundary(
                        startDate, frequency, rollConvention, initialStubType);
            }
            if (lastRegularEndDate == null && !isOnRoll(endDate, rollConvention)) {
                lastRegularEndDate = deriveLastRegularBoundary(
                        endDate, frequency, rollConvention, finalStubType);
            }
        }

        PeriodicSchedule periodicSchedule =
                PeriodicSchedule.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .frequency(frequency)
                        .businessDayAdjustment(BusinessDayAdjustment.NONE)
                        .stubConvention(stubConvention)
                        .rollConvention(rollConvention)
                        .lastRegularEndDate(lastRegularEndDate)
                        .firstRegularStartDate(firstRegularStartDate)
                        .build();

        return periodicSchedule.createSchedule(ReferenceData.minimal());
    }

    private static LocalDate deriveFirstRegularBoundary(
            LocalDate startDate, Frequency frequency, RollConvention rollConvention, StubPeriodTypeEnum stubType) {
        LocalDate firstRoll = rollConvention.next(startDate, frequency);
        return (stubType == StubPeriodTypeEnum.LONG_INITIAL)
                ? rollConvention.next(firstRoll, frequency)
                : firstRoll;
    }

    private static LocalDate deriveLastRegularBoundary(
            LocalDate endDate, Frequency frequency, RollConvention rollConvention, StubPeriodTypeEnum stubType) {
        LocalDate lastRoll = rollConvention.previous(endDate, frequency);
        return (stubType == StubPeriodTypeEnum.LONG_FINAL)
                ? rollConvention.previous(lastRoll, frequency)
                : lastRoll;
    }

    private CalculationPeriodDataBuilder findPeriodContainingDate(
            List<SchedulePeriod> periods, Date date, LocalDate scheduleStart, LocalDate scheduleEnd) {
        return periods.stream()
                .filter(p -> isPeriodContainingDate(p, date))
                .peek(p -> LOGGER.debug("Date {} found in period {} - {}", date, p.getStartDate(), p.getEndDate()))
                .findFirst()
                .map(p -> buildCalculationPeriodData(p, scheduleStart, scheduleEnd))
                .orElseThrow(() -> new IllegalArgumentException("Date " + date + " does not fall within any schedule period."));
    }

    // Period: [startDate, endDate)
    private boolean isPeriodContainingDate(SchedulePeriod period, Date date) {
        LocalDate localDate = date.toLocalDate();
        return !period.getStartDate().isAfter(localDate) && period.getEndDate().isAfter(localDate);
    }

    private CalculationPeriodDataBuilder buildCalculationPeriodData(
            SchedulePeriod targetPeriod, LocalDate scheduleStart, LocalDate scheduleEnd) {
        // CalculationPeriod is inclusive of period startDate and exclusive of endDate.
        return CalculationPeriodData.builder()
                .setStartDate(Date.of(targetPeriod.getStartDate()))
                .setEndDate(Date.of(targetPeriod.getEndDate()))
                .setDaysInLeapYearPeriod(getDaysThatAreInLeapYear(targetPeriod))
                .setDaysInPeriod(
                        (int) ChronoUnit.DAYS.between(targetPeriod.getStartDate(), targetPeriod.getEndDate()))
                .setIsFirstPeriod(targetPeriod.getStartDate().equals(scheduleStart))
                .setIsLastPeriod(targetPeriod.getEndDate().equals(scheduleEnd));
    }

    private int getDaysThatAreInLeapYear(SchedulePeriod targetPeriod) {
        int daysThatAreInLeapYear = 0;
        for (LocalDate date = targetPeriod.getStartDate(); date.isBefore(targetPeriod.getEndDate()); date = date.plusDays(1)) {
            if (IsoChronology.INSTANCE.isLeapYear(date.getYear())) {
                daysThatAreInLeapYear++;
            }
        }
        return daysThatAreInLeapYear;
    }

}
