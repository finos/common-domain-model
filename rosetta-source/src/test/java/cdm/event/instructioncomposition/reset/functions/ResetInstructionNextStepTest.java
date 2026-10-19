package cdm.event.instructioncomposition.reset.functions;

import cdm.base.math.DatedValue;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.common.ResetInstruction;
import cdm.event.instructioncomposition.InstructionCompositionOutput;
import cdm.event.instructioncomposition.reset.ResetInstructionCompositionStepsEnum;
import cdm.event.instructioncomposition.reset.ResetInstructionState;
import cdm.event.instructioncomposition.reset.ResetInstructionSteps;
import cdm.observable.asset.Price;
import cdm.observable.asset.fro.FloatingRateIndexDefinition;
import cdm.observable.asset.fro.FloatingRateIndexIdentification;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

import static cdm.event.instructioncomposition.reset.ResetInstructionCompositionStepsEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResetInstructionNextStepTest extends AbstractFunctionTest {

    @Inject
    private ResetInstructionNextStep resetInstructionNextStep;

    @Nested
    @DisplayName("Step 1 - returned when there is no state or Step 1 data is missing")
    class NextStepIsStep1 {

        @Test
        @DisplayName("Returns Step 1 when both the state and the output are absent")
        void shouldReturnStep1WhenStateAndOutputAreAbsent() {
            assertNextStep(COLLECT_FLOATING_RATE_OPTION_INSTRUCTION, resetInstructionNextStep.evaluate(null, null));
        }

        @Test
        @DisplayName("Returns Step 1 when the state is empty")
        void shouldReturnStep1WhenStateIsEmpty() {
            assertNextStep(COLLECT_FLOATING_RATE_OPTION_INSTRUCTION,
                    resetInstructionNextStep.evaluate(ResetInstructionState.builder().build(), null));
        }

        @Test
        @DisplayName("Returns Step 1 when only floatingRateIndex is present")
        void shouldReturnStep1WhenOnlyFloatingRateIndexIsPresent() {
            ResetInstructionState state = ResetInstructionState.builder()
                    .setFloatingRateIndex(FloatingRateIndexEnum.USD_SOFR)
                    .build();

            assertNextStep(COLLECT_FLOATING_RATE_OPTION_INSTRUCTION, resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Returns Step 1 when only tradeDate is present")
        void shouldReturnStep1WhenOnlyTradeDateIsPresent() {
            ResetInstructionState state = ResetInstructionState.builder()
                    .setTradeDate(Date.of(2024, 1, 15))
                    .build();

            assertNextStep(COLLECT_FLOATING_RATE_OPTION_INSTRUCTION, resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Returns Step 1 when later steps are present but Step 1 is incomplete")
        void shouldReturnStep1WhenLaterStepsArePresentButStep1IsIncomplete() {
            ResetInstructionState state = stateThroughStep(7).setFloatingRateIndex(null).build();

            assertNextStep(COLLECT_FLOATING_RATE_OPTION_INSTRUCTION, resetInstructionNextStep.evaluate(state, null));
        }
    }

    @Nested
    @DisplayName("Steps 2-7 - returns the earliest step whose mandatory data is missing")
    class EarliestMissingStep {

        @Test
        @DisplayName("Returns Step 2 when Step 1 is complete")
        void shouldReturnStep2WhenStep1IsComplete() {
            assertNextStep(DETERMINE_UNADJUSTED_CALCULATION_PERIOD_INSTRUCTION,
                    resetInstructionNextStep.evaluate(stateThroughStep(1).build(), null));
        }

        @Test
        @DisplayName("Returns Step 2 when only unadjustedCalculationPeriod is present")
        void shouldReturnStep2WhenOnlyUnadjustedCalculationPeriodIsPresent() {
            ResetInstructionState state = stateThroughStep(2).setUnadjustedResetDate(null).build();

            assertNextStep(DETERMINE_UNADJUSTED_CALCULATION_PERIOD_INSTRUCTION,
                    resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Returns Step 2 when only unadjustedResetDate is present")
        void shouldReturnStep2WhenOnlyUnadjustedResetDateIsPresent() {
            ResetInstructionState state = stateThroughStep(2).setUnadjustedCalculationPeriod(null).build();

            assertNextStep(DETERMINE_UNADJUSTED_CALCULATION_PERIOD_INSTRUCTION,
                    resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Returns Step 3 when Steps 1-2 are complete")
        void shouldReturnStep3WhenSteps1To2AreComplete() {
            assertNextStep(ADJUST_PERIOD_INSTRUCTION,
                    resetInstructionNextStep.evaluate(stateThroughStep(2).build(), null));
        }

        @Test
        @DisplayName("Returns Step 4 when Steps 1-3 are complete")
        void shouldReturnStep4WhenSteps1To3AreComplete() {
            assertNextStep(ADJUST_DATE_INSTRUCTION,
                    resetInstructionNextStep.evaluate(stateThroughStep(3).build(), null));
        }

        @Test
        @DisplayName("Returns Step 5 when Steps 1-4 are complete")
        void shouldReturnStep5WhenSteps1To4AreComplete() {
            assertNextStep(DETERMINE_UNADJUSTED_OBSERVATION_DATES_INSTRUCTION,
                    resetInstructionNextStep.evaluate(stateThroughStep(4).build(), null));
        }

        @Test
        @DisplayName("Returns Step 5 when floatingRateOptionData is missing")
        void shouldReturnStep5WhenFloatingRateOptionDataIsMissing() {
            ResetInstructionState state = stateThroughStep(5).setFloatingRateOptionData(null).build();

            assertNextStep(DETERMINE_UNADJUSTED_OBSERVATION_DATES_INSTRUCTION,
                    resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Returns Step 5 when unadjustedObservationDates is missing")
        void shouldReturnStep5WhenUnadjustedObservationDatesIsMissing() {
            ResetInstructionState state = stateThroughStep(5).setUnadjustedObservationDates(java.util.Collections.emptyList()).build();

            assertNextStep(DETERMINE_UNADJUSTED_OBSERVATION_DATES_INSTRUCTION,
                    resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Returns Step 6 when Steps 1-5 are complete")
        void shouldReturnStep6WhenSteps1To5AreComplete() {
            assertNextStep(ADJUST_OBSERVATION_DATES_INSTRUCTION,
                    resetInstructionNextStep.evaluate(stateThroughStep(5).build(), null));
        }

        @Test
        @DisplayName("Returns Step 7 when Steps 1-6 are complete")
        void shouldReturnStep7WhenSteps1To6AreComplete() {
            assertNextStep(CALCULATE_RESET_VALUE_INSTRUCTION,
                    resetInstructionNextStep.evaluate(stateThroughStep(6).build(), null));
        }

        @Test
        @DisplayName("Returns Step 7 when observedRateDates is missing")
        void shouldReturnStep7WhenObservedRateDatesIsMissing() {
            ResetInstructionState state = stateThroughStep(7).setObservedRateDates(java.util.Collections.emptyList()).build();

            assertNextStep(CALCULATE_RESET_VALUE_INSTRUCTION, resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Returns Step 7 when resetValue is missing")
        void shouldReturnStep7WhenResetValueIsMissing() {
            ResetInstructionState state = stateThroughStep(7).setResetValue(null).build();

            assertNextStep(CALCULATE_RESET_VALUE_INSTRUCTION, resetInstructionNextStep.evaluate(state, null));
        }

        @Test
        @DisplayName("Does not skip a missing step when a later step is already populated")
        void shouldNotSkipMissingStepWhenLaterStepIsPopulated() {
            ResetInstructionState state = stateThroughStep(4).setAdjustedCalculationPeriod(null).build();

            assertNextStep(ADJUST_PERIOD_INSTRUCTION, resetInstructionNextStep.evaluate(state, null));
        }
    }

    @Nested
    @DisplayName("Final output generation")
    class FinalOutputGeneration {

        @Test
        @DisplayName("Returns ResetInstructionOutput when Steps 1-7 are complete and the output is absent")
        void shouldReturnResetInstructionOutputWhenOutputIsAbsent() {
            assertNextStep(RESET_INSTRUCTION_OUTPUT,
                    resetInstructionNextStep.evaluate(stateThroughStep(7).build(), null));
        }

        @Test
        @DisplayName("Returns ResetInstructionOutput when Steps 1-7 are complete and the output has no resetInstruction")
        void shouldReturnResetInstructionOutputWhenOutputHasNoResetInstruction() {
            ResetInstructionSteps result = resetInstructionNextStep.evaluate(
                    stateThroughStep(7).build(),
                    InstructionCompositionOutput.builder().build());

            assertNextStep(RESET_INSTRUCTION_OUTPUT, result);
        }

        @Test
        @DisplayName("Returns ResetInstructionOutput when Steps 1-7 are complete and the output resetInstruction is empty")
        void shouldReturnResetInstructionOutputWhenResetInstructionIsEmpty() {
            ResetInstructionSteps result = resetInstructionNextStep.evaluate(
                    stateThroughStep(7).build(),
                    InstructionCompositionOutput.builder()
                            .setResetInstruction(ResetInstruction.builder().build())
                            .build());

            assertNextStep(RESET_INSTRUCTION_OUTPUT, result);
        }

        @Test
        @DisplayName("Returns nothing when Steps 1-7 are complete and the output already holds a resetInstruction")
        void shouldReturnNothingWhenResetInstructionAlreadyRecorded() {
            ResetInstructionSteps result = resetInstructionNextStep.evaluate(
                    stateThroughStep(7).build(),
                    InstructionCompositionOutput.builder()
                            .setResetInstruction(ResetInstruction.builder()
                                    .addReset(cdm.event.common.Reset.builder()
                                            .setResetDate(Date.of(2024, 3, 20))
                                            .build())
                                    .build())
                            .build());

            assertNull(result.getResetInstructionCompositionSteps(),
                    "no further step is expected once the reset instruction has been recorded");
        }

        @Test
        @DisplayName("Ignores the output while earlier steps are still incomplete")
        void shouldIgnoreOutputWhileEarlierStepsAreIncomplete() {
            ResetInstructionSteps result = resetInstructionNextStep.evaluate(
                    stateThroughStep(3).build(),
                    InstructionCompositionOutput.builder()
                            .setResetInstruction(ResetInstruction.builder()
                                    .addReset(cdm.event.common.Reset.builder()
                                            .setResetDate(Date.of(2024, 3, 20))
                                            .build())
                                    .build())
                            .build());

            assertNextStep(ADJUST_DATE_INSTRUCTION, result);
        }
    }

    // -------- Assertion helpers -----------
    private static void assertNextStep(ResetInstructionCompositionStepsEnum expected, ResetInstructionSteps actual) {
        assertNotNull(actual, "a next step must be returned");
        assertEquals(expected, actual.getResetInstructionCompositionSteps(), "unexpected next step");
    }

    // -------- Builder helpers -----------

    /**
     * Builds a state with every field populated for Steps 1..{@code step}.
     */
    private static ResetInstructionState.ResetInstructionStateBuilder stateThroughStep(int step) {
        ResetInstructionState.ResetInstructionStateBuilder state = ResetInstructionState.builder();
        if (step >= 1) {
            state.setFloatingRateIndex(FloatingRateIndexEnum.USD_SOFR)
                    .setTradeDate(Date.of(2024, 1, 15));
        }
        if (step >= 2) {
            state.setUnadjustedCalculationPeriod(period(Date.of(2024, 3, 20), Date.of(2024, 6, 20)))
                    .setUnadjustedResetDate(Date.of(2024, 3, 18));
        }
        if (step >= 3) {
            state.setAdjustedCalculationPeriod(period(Date.of(2024, 3, 20), Date.of(2024, 6, 20)));
        }
        if (step >= 4) {
            state.setAdjustedResetDate(Date.of(2024, 3, 18));
        }
        if (step >= 5) {
            state.addUnadjustedObservationDates(Date.of(2024, 3, 18))
                    .setFloatingRateOptionData(FloatingRateIndexDefinition.builder()
                            .setFro(FloatingRateIndexIdentification.builder()
                                    .setFroType("OIS")
                                    .build())
                            .build());
        }
        if (step >= 6) {
            state.addAdjustedObservationDates(Date.of(2024, 3, 18));
        }
        if (step >= 7) {
            state.setResetValue(Price.builder().setValue(new BigDecimal("0.05")).build())
                    .addObservedRateDates(DatedValue.builder()
                            .setDate(Date.of(2024, 3, 18))
                            .setValue(new BigDecimal("0.05"))
                            .build());
        }
        return state;
    }

    private static CalculationPeriodBase period(Date start, Date end) {
        return CalculationPeriodBase.builder()
                .setAdjustedStartDate(start)
                .setAdjustedEndDate(end)
                .build();
    }
}
