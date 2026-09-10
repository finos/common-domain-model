package cdm.event.instructioncomposition.reset.functions;

import cdm.event.instructioncomposition.reset.DetermineUnadjustedObservationDatesInstruction;
import cdm.observable.asset.fro.FloatingRateIndexCalculationDefaults;
import cdm.observable.asset.fro.FloatingRateIndexCalculationMethodEnum;
import cdm.observable.asset.fro.FloatingRateIndexCategoryEnum;
import cdm.observable.asset.fro.FloatingRateIndexDefinition;
import cdm.observable.asset.fro.FloatingRateIndexStyleEnum;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.functions.ConditionValidator;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Create_DetermineUnadjustedObservationDatesInstructionTest extends AbstractFunctionTest {

    @Inject
    private Create_DetermineUnadjustedObservationDatesInstruction createDetermineUnadjustedObservationDatesInstruction;

    @Test
    void shouldSetUnadjustedObservationDateToAdjustedResetDateForSingleObservation() {
        Date startDate = Date.of(2024, 1, 15);
        Date endDate = Date.of(2024, 1, 17);
        Date resetDate = Date.of(2024, 1, 14);

        DetermineUnadjustedObservationDatesInstruction result =
                createDetermineUnadjustedObservationDatesInstruction.evaluate(
                        buildCalculationPeriod(startDate, endDate),
                        resetDate,
                        buildFroData(FloatingRateIndexCategoryEnum.SCREEN_RATE, FloatingRateIndexStyleEnum.AVERAGE_FRO, null),
                        null);

        assertEquals(Collections.singletonList(resetDate), result.getUnadjustedObservationDates());
        assertEquals(buildFroData(FloatingRateIndexCategoryEnum.SCREEN_RATE, FloatingRateIndexStyleEnum.AVERAGE_FRO, null), result.getFloatingRateIndexData());
    }

    @Test
    void shouldGenerateCalendarDateListForMultipleObservation() {
        Date startDate = Date.of(2024, 1, 15);
        Date endDate = Date.of(2024, 1, 17);

        DetermineUnadjustedObservationDatesInstruction result =
                createDetermineUnadjustedObservationDatesInstruction.evaluate(
                        buildCalculationPeriod(startDate, endDate),
                        Date.of(2024, 1, 14),
                        buildFroData(FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.AVERAGE_FRO, FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND),
                        null);

        List<Date> expectedDates = Arrays.asList(Date.of(2024, 1, 15), Date.of(2024, 1, 16), Date.of(2024, 1, 17));
        assertEquals(expectedDates, result.getUnadjustedObservationDates());
    }

    @Test
    void shouldThrowWhenScreenRateOvernightHasNoConfirmationMethod() {
        ConditionValidator.ConditionException exception = assertThrows(ConditionValidator.ConditionException.class, () ->
                createDetermineUnadjustedObservationDatesInstruction.evaluate(
                        buildCalculationPeriod(Date.of(2024, 1, 15), Date.of(2024, 1, 17)),
                        Date.of(2024, 1, 14),
                        buildFroData(FloatingRateIndexCategoryEnum.SCREEN_RATE, FloatingRateIndexStyleEnum.OVERNIGHT, null),
                        null));
        assertEquals("If the Floating Rate Index has a Style of Overnight, then the calculation methodology originating from the Workflow Step must be present.",
                exception.getMessage());
    }

    @Test
    void shouldSetStartAndEndDatesForCompoundedIndexObservation() {
        Date startDate = Date.of(2024, 1, 15);
        Date endDate = Date.of(2024, 1, 17);

        DetermineUnadjustedObservationDatesInstruction result =
                createDetermineUnadjustedObservationDatesInstruction.evaluate(
                        buildCalculationPeriod(startDate, endDate),
                        Date.of(2024, 1, 14),
                        buildFroData(FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_INDEX, FloatingRateIndexCalculationMethodEnum.COMPOUNDED),
                        null);

        assertEquals(Arrays.asList(startDate, endDate), result.getUnadjustedObservationDates());
    }

    // -------- Helper methods -----------

    private static CalculationPeriodBase buildCalculationPeriod(Date startDate, Date endDate) {
        return CalculationPeriodBase.builder()
                .setAdjustedStartDate(startDate)
                .setAdjustedEndDate(endDate)
                .build();
    }

    private static FloatingRateIndexDefinition buildFroData(
            FloatingRateIndexCategoryEnum category,
            FloatingRateIndexStyleEnum style,
            FloatingRateIndexCalculationMethodEnum method) {
        return FloatingRateIndexDefinition.builder()
                .setCalculationDefaults(FloatingRateIndexCalculationDefaults.builder()
                        .setCategory(category)
                        .setIndexStyle(style)
                        .setMethod(method)
                        .build())
                .build();
    }
}
