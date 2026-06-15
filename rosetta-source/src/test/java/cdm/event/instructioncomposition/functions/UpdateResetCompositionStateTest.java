package cdm.event.instructioncomposition.functions;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.instructioncomposition.CompositionStepInstructions;
import cdm.event.instructioncomposition.reset.CollectFloatingRateOptionInstruction;
import cdm.event.instructioncomposition.reset.ResetInstructionState;
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
}
