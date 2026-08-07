package cdm.event.instructioncomposition.reset.functions;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.instructioncomposition.CompositionStepInstructions;
import cdm.event.instructioncomposition.reset.AdjustDateInstruction;
import cdm.event.instructioncomposition.reset.CollectFloatingRateOptionInstruction;
import cdm.event.instructioncomposition.reset.DetermineUnadjustedCalculationPeriodInstruction;
import cdm.event.instructioncomposition.reset.ResetInstructionState;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UpdateResetCompositionStateTest extends AbstractFunctionTest {

    @Inject
    private UpdateResetCompositionState updateResetCompositionState;

    @Nested
    @DisplayName("Step 1 - Floating Rate Index and Trade Date")
    class CollectFloatingRateOptionTest {

        @Test
        @DisplayName("Sets floatingRateIndex and tradeDate from the step instruction when collectFloatingRateOption is present")
        void shouldSetFloatingRateIndexAndTradeDateFromStepWhenCollectFloatingRateOptionIsPresent() {
            CompositionStepInstructions nextStep = CompositionStepInstructions.builder()
                    .setCollectFloatingRateOption(CollectFloatingRateOptionInstruction.builder()
                            .setFloatingRateIndex(FloatingRateIndexEnum.USD_SOFR)
                            .setTradeDate(Date.of(2024, 1, 15))
                            .build())
                    .build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStep);

            assertEquals(FloatingRateIndexEnum.USD_SOFR, result.getFloatingRateIndex(),
                    "floatingRateIndex must be taken from the step instruction");
            assertEquals(Date.of(2024, 1, 15), result.getTradeDate(),
                    "tradeDate must be taken from the step instruction");
        }

        @Test
        @DisplayName("Carries forward floatingRateIndex and tradeDate from the current state when collectFloatingRateOption is absent")
        void shouldCarryForwardFloatingRateIndexAndTradeDateFromCurrentStateWhenCollectFloatingRateOptionIsAbsent() {
            ResetInstructionState currentState = ResetInstructionState.builder()
                    .setFloatingRateIndex(FloatingRateIndexEnum.GBP_SONIA)
                    .setTradeDate(Date.of(2024, 1, 15))
                    .build();
            CompositionStepInstructions nextStepWithoutCollectFloatingRateOption = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(currentState, nextStepWithoutCollectFloatingRateOption);

            assertEquals(FloatingRateIndexEnum.GBP_SONIA, result.getFloatingRateIndex(),
                    "floatingRateIndex must be carried forward from the current state");
            assertEquals(Date.of(2024, 1, 15), result.getTradeDate(),
                    "tradeDate must be carried forward from the current state");
        }

        @Test
        @DisplayName("Produces null floatingRateIndex and tradeDate when collectFloatingRateOption is absent and current state is absent")
        void shouldProduceNullFieldsWhenCollectFloatingRateOptionAndCurrentStateAreBothAbsent() {
            CompositionStepInstructions nextStepWithoutCollectFloatingRateOption = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStepWithoutCollectFloatingRateOption);

            assertNull(result.getFloatingRateIndex(),
                    "floatingRateIndex must be null when both current state and step instruction are absent");
            assertNull(result.getTradeDate(),
                    "tradeDate must be null when both current state and step instruction are absent");
        }
    }

    @Nested
    @DisplayName("Step 2 - Unadjusted Calculation Period & Reset Date")
    class DetermineUnadjustedCalculationPeriodAndResetDateTests {

        @Test
        @DisplayName("Takes unadjusted calculation period and reset date from the next step when present")
        void shouldTakeUnadjustedCalculationPeriodAndResetDateFromNextStepWhenPresent() {
            CalculationPeriodBase period = CalculationPeriodBase.builder()
                    .setAdjustedStartDate(Date.of(2023, 1, 3))
                    .setAdjustedEndDate(Date.of(2023, 4, 3))
                    .build();
            Date resetDate = Date.of(2023, 2, 15);

            CompositionStepInstructions nextStep = CompositionStepInstructions.builder()
                    .setDetermineUnadjustedCalculationPeriod(
                            DetermineUnadjustedCalculationPeriodInstruction.builder()
                                    .setUnadjustedCalculationPeriod(period)
                                    .setUnadjustedResetDate(resetDate))
                    .build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStep);

            assertEquals(period, result.getUnadjustedCalculationPeriod(), "Unadjusted calculation period should be set");
            assertEquals(resetDate, result.getUnadjustedResetDate(), "Unadjusted reset date should be set");
        }

        @Test
        @DisplayName("Preserves unadjusted calculation period and reset date from current state when absent from next step")
        void shouldPreserveUnadjustedCalculationPeriodAndResetDateFromCurrentStateWhenAbsentFromNextStep() {
            CalculationPeriodBase existingPeriod = CalculationPeriodBase.builder()
                    .setAdjustedStartDate(Date.of(2022, 7, 1))
                    .setAdjustedEndDate(Date.of(2022, 10, 1))
                    .build();
            Date existingResetDate = Date.of(2022, 8, 10);

            ResetInstructionState currentState = ResetInstructionState.builder()
                    .setUnadjustedCalculationPeriod(existingPeriod)
                    .setUnadjustedResetDate(existingResetDate)
                    .build();

            CompositionStepInstructions nextStepWithoutStep2 = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(currentState, nextStepWithoutStep2);

            assertEquals(existingPeriod, result.getUnadjustedCalculationPeriod());
            assertEquals(existingResetDate, result.getUnadjustedResetDate());
        }

        @Test
        @DisplayName("Returns null fields when absent from both next step and current state")
        void shouldReturnNullUnadjustedCalculationPeriodAndResetDateWhenAbsentFromBothNextStepAndCurrentState() {
            ResetInstructionState result = updateResetCompositionState.evaluate(null, CompositionStepInstructions.builder().build());

            assertNull(result.getUnadjustedCalculationPeriod());
            assertNull(result.getUnadjustedResetDate());
        }
    }

    @Nested
    @DisplayName("Step 4 - Adjusted Date")
    class AdjustDateTest {

        @Test
        @DisplayName("Sets adjustedResetDate from the step instruction when adjustDate is present")
        void shouldSetAdjustedResetDateFromStepWhenAdjustDateIsPresent() {
            CompositionStepInstructions nextStep = CompositionStepInstructions.builder()
                    .setAdjustDate(AdjustDateInstruction.builder()
                            .setAdjustedDate(Date.of(2024, 3, 20))
                            .build())
                    .build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStep);

            assertEquals(Date.of(2024, 3, 20), result.getAdjustedResetDate(),
                    "adjustedResetDate must be taken from the step instruction");
        }

        @Test
        @DisplayName("Carries forward adjustedResetDate from the current state when adjustDate is absent")
        void shouldCarryForwardAdjustedResetDateFromCurrentStateWhenAdjustDateIsAbsent() {
            ResetInstructionState currentState = ResetInstructionState.builder()
                    .setAdjustedResetDate(Date.of(2024, 3, 20))
                    .build();
            CompositionStepInstructions nextStepWithoutAdjustDate = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(currentState, nextStepWithoutAdjustDate);

            assertEquals(Date.of(2024, 3, 20), result.getAdjustedResetDate(),
                    "adjustedResetDate must be carried forward from the current state");
        }

        @Test
        @DisplayName("Produces null adjustedResetDate when adjustDate is absent and current state is absent")
        void shouldProduceNullAdjustedResetDateWhenAdjustDateAndCurrentStateAreBothAbsent() {
            CompositionStepInstructions nextStepWithoutAdjustDate = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStepWithoutAdjustDate);

            assertNull(result.getAdjustedResetDate(),
                    "adjustedResetDate must be null when both current state and step instruction are absent");
        }
    }
}
