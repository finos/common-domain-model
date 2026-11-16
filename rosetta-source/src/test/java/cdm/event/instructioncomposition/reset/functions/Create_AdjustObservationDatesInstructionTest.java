package cdm.event.instructioncomposition.reset.functions;

import cdm.base.datetime.Offset;
import cdm.base.datetime.PeriodEnum;
import cdm.event.instructioncomposition.reset.AdjustObservationDatesInstruction;
import cdm.observable.asset.calculatedrate.OffsetCalculation;
import cdm.observable.asset.fro.FloatingRateIndexCalculationMethodEnum;
import cdm.observable.asset.fro.FloatingRateIndexCategoryEnum;
import cdm.observable.asset.fro.FloatingRateIndexStyleEnum;
import com.rosetta.model.lib.functions.ConditionValidator;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Create_AdjustObservationDatesInstructionTest extends AbstractFunctionTest {

    @Inject
    private Create_AdjustObservationDatesInstruction func;

    @Nested
    @DisplayName("Fixing Day Offset Validation")
    class FixingDayOffsetValidationTests {

        @ParameterizedTest(name = "Rejects fixing day offset period {0}")
        @EnumSource(value = PeriodEnum.class, names = {"W", "M", "Y"})
        void shouldRejectNonDailyFixingDayOffsetPeriods(PeriodEnum period) {
            Offset fixingDayOffset = Offset.builder()
                    .setPeriod(period)
                    .setPeriodMultiplier(-1)
                    .build();

            ConditionValidator.ConditionException exception = assertThrows(
                    ConditionValidator.ConditionException.class,
                    () -> func.evaluate(
                            Collections.singletonList(Date.of(2024, 1, 10)),
                            fixingDayOffset,
                            null,
                            FloatingRateIndexCategoryEnum.SCREEN_RATE,
                            FloatingRateIndexStyleEnum.TERM_RATE,
                            null,
                            null,
                            Collections.emptyList(),
                            Collections.emptyList()
                    )
            );

            assertEquals("Validates that the Fixing Day Offset period is defined in Days.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Single Observation")
    class SingleObservationTests {

        @Test
        @DisplayName("Returns the observation date unchanged when no fixing offset is provided")
        void shouldReturnObservationDateUnchangedWhenNoFixingOffset() {
            List<Date> unadjustedDates = Collections.singletonList(Date.of(2024, 1, 10));

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    null,
                    null,
                    FloatingRateIndexCategoryEnum.SCREEN_RATE,
                    FloatingRateIndexStyleEnum.TERM_RATE,
                    null,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
            );

            assertEquals(Collections.singletonList(Date.of(2024, 1, 10)), result.getAdjustedObservationDates(),
                    "Observation date must be returned unchanged when no fixing offset is provided");
        }

        @Test
        @DisplayName("Applies fixing day offset to shift the observation date back by the specified business days")
        void shouldApplyFixingDayOffsetToShiftObservationDate() {
            List<Date> unadjustedDates = Collections.singletonList(Date.of(2024, 1, 10));
            Offset fixingDayOffset = Offset.builder()
                    .setPeriod(PeriodEnum.D)
                    .setPeriodMultiplier(-2)
                    .build();

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    fixingDayOffset,
                    null,
                    FloatingRateIndexCategoryEnum.SCREEN_RATE,
                    FloatingRateIndexStyleEnum.TERM_RATE,
                    null,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
            );

            assertEquals(Collections.singletonList(Date.of(2024, 1, 8)), result.getAdjustedObservationDates(),
                    "Observation date must be shifted back by 2 business days");
        }

        @Test
        @DisplayName("Skips non-business dates when applying fixing day offset")
        void shouldSkipNonBusinessDatesWhenApplyingFixingDayOffset() {
            List<Date> unadjustedDates = Collections.singletonList(Date.of(2024, 1, 10));
            Offset fixingDayOffset = Offset.builder()
                    .setPeriod(PeriodEnum.D)
                    .setPeriodMultiplier(-2)
                    .build();
            List<Date> nonBusinessDatesAdjustedResetDate = Collections.singletonList(Date.of(2024, 1, 8));

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    fixingDayOffset,
                    null,
                    FloatingRateIndexCategoryEnum.SCREEN_RATE,
                    FloatingRateIndexStyleEnum.TERM_RATE,
                    null,
                    null,
                    Collections.emptyList(),
                    nonBusinessDatesAdjustedResetDate
            );

            assertEquals(Collections.singletonList(Date.of(2024, 1, 7)), result.getAdjustedObservationDates(),
                    "Fixing offset must skip over non-business dates in the reset date calendar");
        }
    }

    @Nested
    @DisplayName("Compounded Index Observation")
    class CompoundedIndexObservationTests {

        @Test
        @DisplayName("Returns the two unadjusted observation dates unchanged for Compounded Index")
        void shouldReturnTwoUnadjustedDatesUnchangedForCompoundedIndex() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 2),
                    Date.of(2024, 1, 5)
            );

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    null,
                    null,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_INDEX,
                    FloatingRateIndexCalculationMethodEnum.COMPOUNDED,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
            );

            assertEquals(Arrays.asList(Date.of(2024, 1, 2), Date.of(2024, 1, 5)), result.getAdjustedObservationDates(),
                    "The two unadjusted observation dates must be returned unchanged for Compounded Index");
        }
    }

    @Nested
    @DisplayName("Multiple Observation")
    class MultipleObservationTests {

        @Test
        @DisplayName("Returns all calendar dates between start and end for Calculated/CompoundedFRO with no offset or lookback")
        void shouldReturnAllCalendarDatesWhenNoOffsetOrLookback() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 2),
                    Date.of(2024, 1, 3),
                    Date.of(2024, 1, 4),
                    Date.of(2024, 1, 5)
            );

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    null,
                    null,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                    FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
            );

            assertEquals(
                    Arrays.asList(
                            Date.of(2024, 1, 2),
                            Date.of(2024, 1, 3),
                            Date.of(2024, 1, 4),
                            Date.of(2024, 1, 5)),
                    result.getAdjustedObservationDates(),
                    "All calendar dates between start and end must be returned when no offset or lookback is applied"
            );
        }

        @Test
        @DisplayName("Applies lookback shift to move the observation window back by the specified business days")
        void shouldApplyLookbackShiftToMoveObservationWindowBack() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 8),
                    Date.of(2024, 1, 9),
                    Date.of(2024, 1, 10),
                    Date.of(2024, 1, 11),
                    Date.of(2024, 1, 12)
            );
            OffsetCalculation lookbackShift = OffsetCalculation.builder()
                    .setOffsetDays(2)
                    .build();

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    null,
                    lookbackShift,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                    FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
            );

            assertEquals(
                    Arrays.asList(
                            Date.of(2024, 1, 6),
                            Date.of(2024, 1, 7),
                            Date.of(2024, 1, 8),
                            Date.of(2024, 1, 9),
                            Date.of(2024, 1, 10)
                    ),
                    result.getAdjustedObservationDates(),
                    "Lookback shift must move the observation window back by 2 business days"
            );
        }

        @Test
        @DisplayName("Collapses non-business dates to preceding business day and fills missing calendar days")
        void shouldCollapseNonBusinessDatesToPrecedingAndFillMissingDays() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 5),
                    Date.of(2024, 1, 6),
                    Date.of(2024, 1, 7),
                    Date.of(2024, 1, 8)
            );
            List<Date> nonBusinessDatesObservationDates = Arrays.asList(
                    Date.of(2024, 1, 6),
                    Date.of(2024, 1, 7)
            );

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    null,
                    null,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                    FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND,
                    null,
                    nonBusinessDatesObservationDates,
                    Collections.emptyList()
            );

            assertEquals(
                    Arrays.asList(
                            Date.of(2024, 1, 5),
                            Date.of(2024, 1, 5),
                            Date.of(2024, 1, 5),
                            Date.of(2024, 1, 8)
                    ),
                    result.getAdjustedObservationDates(),
                    "Weekend dates must collapse to preceding Friday and missing calendar days must be filled"
            );
        }

        @Test
        @DisplayName("Applies both fixing day offset and lookback shift to the observation window")
        void shouldApplyBothFixingDayOffsetAndLookbackShift() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 8),
                    Date.of(2024, 1, 9),
                    Date.of(2024, 1, 10),
                    Date.of(2024, 1, 11),
                    Date.of(2024, 1, 12)
            );
            Offset fixingDayOffset = Offset.builder()
                    .setPeriod(PeriodEnum.D)
                    .setPeriodMultiplier(-1)
                    .build();
            OffsetCalculation lookbackShift = OffsetCalculation.builder()
                    .setOffsetDays(1)
                    .build();

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    fixingDayOffset,
                    lookbackShift,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                    FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND,
                    null,
                    Collections.emptyList(),
                    Collections.emptyList()
            );

            assertEquals(
                    Arrays.asList(
                            Date.of(2024, 1, 6),
                            Date.of(2024, 1, 7),
                            Date.of(2024, 1, 8),
                            Date.of(2024, 1, 9),
                            Date.of(2024, 1, 10)
                    ),
                    result.getAdjustedObservationDates(),
                    "Both fixing day offset and lookback shift must be applied sequentially to the observation window"
            );
        }

        @Test
        @DisplayName("Applies fixing day offset to multiple observation boundaries using the observation calendar")
        void shouldApplyFixingDayOffsetToMultipleObservationBoundariesUsingObservationCalendar() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 8),
                    Date.of(2024, 1, 9),
                    Date.of(2024, 1, 10),
                    Date.of(2024, 1, 11),
                    Date.of(2024, 1, 12)
            );
            Offset fixingDayOffset = Offset.builder()
                    .setPeriod(PeriodEnum.D)
                    .setPeriodMultiplier(-2)
                    .build();
            List<Date> nonBusinessDatesObservation = Collections.singletonList(Date.of(2024, 1, 7));

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    fixingDayOffset,
                    null,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                    FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND,
                    null,
                    nonBusinessDatesObservation,
                    Collections.emptyList()
            );

            assertEquals(
                    Arrays.asList(
                            Date.of(2024, 1, 5),
                            Date.of(2024, 1, 6),
                            Date.of(2024, 1, 6),
                            Date.of(2024, 1, 8),
                            Date.of(2024, 1, 9),
                            Date.of(2024, 1, 10)
                    ),
                    result.getAdjustedObservationDates(),
                    "Fixing day offset for Multiple Observation must use the observation calendar to shift boundaries"
            );
        }

        @Test
        @DisplayName("Lookback shift that lands on a non-business date skips over it when shifting boundaries")
        void shouldSkipNonBusinessDatesWhenLookbackShiftLandsOnHoliday() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 9),
                    Date.of(2024, 1, 10),
                    Date.of(2024, 1, 11),
                    Date.of(2024, 1, 12)
            );
            OffsetCalculation lookbackShift = OffsetCalculation.builder()
                    .setOffsetDays(1)
                    .build();
            List<Date> nonBusinessDatesObservation = Collections.singletonList(Date.of(2024, 1, 8));

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    null,
                    lookbackShift,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                    FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND,
                    null,
                    nonBusinessDatesObservation,
                    Collections.emptyList()
            );

            assertEquals(
                    Arrays.asList(
                            Date.of(2024, 1, 7), Date.of(2024, 1, 7),
                            Date.of(2024, 1, 9), Date.of(2024, 1, 10), Date.of(2024, 1, 11)
                    ),
                    result.getAdjustedObservationDates(),
                    "Lookback shift must skip over non-business dates when shifting boundaries"
            );
        }

        @Test
        @DisplayName("Collapses non-business dates that fall inside the lookback-shifted period")
        void shouldCollapseNonBusinessDatesInPeriodShiftedByLookback() {
            List<Date> unadjustedDates = Arrays.asList(
                    Date.of(2024, 1, 15),
                    Date.of(2024, 1, 16),
                    Date.of(2024, 1, 17)
            );
            OffsetCalculation lookbackShift = OffsetCalculation.builder()
                    .setOffsetDays(2)
                    .build();
            List<Date> nonBusinessDatesObservation = Arrays.asList(
                    Date.of(2024, 1, 13),
                    Date.of(2024, 1, 14)
            );

            AdjustObservationDatesInstruction result = func.evaluate(
                    unadjustedDates,
                    null,
                    lookbackShift,
                    FloatingRateIndexCategoryEnum.CALCULATED,
                    FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                    FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND,
                    null,
                    nonBusinessDatesObservation,
                    Collections.emptyList()
            );

            assertEquals(
                    Arrays.asList(
                            Date.of(2024, 1, 11), Date.of(2024, 1, 12), Date.of(2024, 1, 12),
                            Date.of(2024, 1, 12), Date.of(2024, 1, 15)
                    ),
                    result.getAdjustedObservationDates(),
                    "Non-business dates inside the lookback-shifted period must collapse to preceding business day with gap filling"
            );
        }
    }
}
