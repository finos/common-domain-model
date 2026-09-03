package cdm.event.instructioncomposition.reset.functions;

import cdm.base.math.DatedValue;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.instructioncomposition.CompositionStepInstructions;
import cdm.event.instructioncomposition.reset.AdjustDateInstruction;
import cdm.event.instructioncomposition.reset.AdjustPeriodInstruction;
import cdm.event.instructioncomposition.reset.CalculateResetValueInstruction;
import cdm.event.instructioncomposition.reset.CollectFloatingRateOptionInstruction;
import cdm.event.instructioncomposition.reset.DetermineUnadjustedCalculationPeriodInstruction;
import cdm.event.instructioncomposition.reset.ResetInstructionState;
import cdm.observable.asset.Price;
import cdm.observable.asset.calculatedrate.CalculationMethodEnum;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

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
    @DisplayName("Step 3 - Adjusted Calculation Period")
    class AdjustPeriodTest {

        @Test
        @DisplayName("Sets adjustedCalculationPeriod from the step instruction when adjustPeriod is present")
        void shouldSetAdjustedCalculationPeriodFromStepWhenAdjustPeriodIsPresent() {
            CalculationPeriodBase adjustedPeriod = CalculationPeriodBase.builder()
                    .setAdjustedStartDate(Date.of(2024, 1, 1))
                    .setAdjustedEndDate(Date.of(2024, 3, 31))
                    .build();
            CompositionStepInstructions nextStep = CompositionStepInstructions.builder()
                    .setAdjustPeriod(AdjustPeriodInstruction.builder()
                            .setAdjustedPeriod(adjustedPeriod)
                            .build())
                    .build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStep);

            assertEquals(Date.of(2024, 1, 1), result.getAdjustedCalculationPeriod().getAdjustedStartDate(),
                    "adjustedStartDate must be taken from the step instruction");
            assertEquals(Date.of(2024, 3, 31), result.getAdjustedCalculationPeriod().getAdjustedEndDate(),
                    "adjustedEndDate must be taken from the step instruction");
        }

        @Test
        @DisplayName("Carries forward adjustedCalculationPeriod from the current state when adjustPeriod is absent")
        void shouldCarryForwardAdjustedCalculationPeriodFromCurrentStateWhenAdjustPeriodIsAbsent() {
            CalculationPeriodBase existingPeriod = CalculationPeriodBase.builder()
                    .setAdjustedStartDate(Date.of(2024, 4, 1))
                    .setAdjustedEndDate(Date.of(2024, 6, 30))
                    .build();
            ResetInstructionState currentState = ResetInstructionState.builder()
                    .setAdjustedCalculationPeriod(existingPeriod)
                    .build();
            CompositionStepInstructions nextStepWithoutAdjustPeriod = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(currentState, nextStepWithoutAdjustPeriod);

            assertEquals(Date.of(2024, 4, 1), result.getAdjustedCalculationPeriod().getAdjustedStartDate(),
                    "adjustedStartDate must be carried forward from the current state");
            assertEquals(Date.of(2024, 6, 30), result.getAdjustedCalculationPeriod().getAdjustedEndDate(),
                    "adjustedEndDate must be carried forward from the current state");
        }

        @Test
        @DisplayName("Produces null adjustedCalculationPeriod when adjustPeriod is absent and current state is absent")
        void shouldProduceNullAdjustedCalculationPeriodWhenAdjustPeriodAndCurrentStateAreBothAbsent() {
            CompositionStepInstructions nextStepWithoutAdjustPeriod = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStepWithoutAdjustPeriod);

            assertNull(result.getAdjustedCalculationPeriod(),
                    "adjustedCalculationPeriod must be null when both current state and step instruction are absent");
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

    @Nested
    @DisplayName("Step 7 - Final Calculation Results")
    class CalculateResetValueTest {

        @Test
        @DisplayName("Sets resetValue, calculationMethod and observedRateDates from the step instruction when calculateResetValue is present")
        void shouldSetResetValueCalculationMethodAndObservedRateDatesFromStepWhenCalculateResetValueIsPresent() {
            Price resetValue = Price.builder()
                    .setValue(new BigDecimal("0.055"))
                    .build();
            DatedValue obs1 = DatedValue.builder().setDate(Date.of(2024, 1, 1)).setValue(new BigDecimal("0.055")).build();
            DatedValue obs2 = DatedValue.builder().setDate(Date.of(2024, 1, 2)).setValue(new BigDecimal("0.055")).build();
            CompositionStepInstructions nextStep = CompositionStepInstructions.builder()
                    .setCalculateResetValue(CalculateResetValueInstruction.builder()
                            .setResetValue(resetValue)
                            .setCalculationMethod(CalculationMethodEnum.COMPOUNDING)
                            .setObservedRateDates(Arrays.asList(obs1, obs2))
                            .build())
                    .build();

            ResetInstructionState result = updateResetCompositionState.evaluate(null, nextStep);

            assertEquals(new BigDecimal("0.055"), result.getResetValue().getValue(),
                    "resetValue must be taken from the step instruction");
            assertEquals(CalculationMethodEnum.COMPOUNDING, result.getCalculationMethod(),
                    "calculationMethod must be taken from the step instruction");
            assertEquals(2, result.getObservedRateDates().size(),
                    "observedRateDates must be taken from the step instruction");
            assertEquals(Date.of(2024, 1, 1), result.getObservedRateDates().get(0).getDate());
            assertEquals(Date.of(2024, 1, 2), result.getObservedRateDates().get(1).getDate());
        }

        @Test
        @DisplayName("Carries forward resetValue, calculationMethod and observedRateDates from the current state when calculateResetValue is absent")
        void shouldCarryForwardResetValueCalculationMethodAndObservedRateDatesFromCurrentStateWhenCalculateResetValueIsAbsent() {
            Price existingResetValue = Price.builder()
                    .setValue(new BigDecimal("0.060"))
                    .build();
            DatedValue existingObs = DatedValue.builder()
                    .setDate(Date.of(2024, 1, 5))
                    .setValue(new BigDecimal("0.060"))
                    .build();
            ResetInstructionState currentState = ResetInstructionState.builder()
                    .setResetValue(existingResetValue)
                    .setCalculationMethod(CalculationMethodEnum.COMPOUNDED_INDEX)
                    .setObservedRateDates(Collections.singletonList(existingObs))
                    .build();
            CompositionStepInstructions nextStepWithoutCalculateResetValue = CompositionStepInstructions.builder().build();

            ResetInstructionState result = updateResetCompositionState.evaluate(currentState, nextStepWithoutCalculateResetValue);

            assertEquals(new BigDecimal("0.060"), result.getResetValue().getValue(),
                    "resetValue must be carried forward from the current state");
            assertEquals(CalculationMethodEnum.COMPOUNDED_INDEX, result.getCalculationMethod(),
                    "calculationMethod must be carried forward from the current state");
            assertEquals(1, result.getObservedRateDates().size(),
                    "observedRateDates must be carried forward from the current state");
            assertEquals(Date.of(2024, 1, 5), result.getObservedRateDates().get(0).getDate());
        }

        @Test
        @DisplayName("Returns null resetValue, null calculationMethod and empty observedRateDates when absent from both next step and current state")
        void shouldReturnNullFieldsWhenAbsentFromBothNextStepAndCurrentState() {
            ResetInstructionState result = updateResetCompositionState.evaluate(null, CompositionStepInstructions.builder().build());

            assertNull(result.getResetValue(),
                    "resetValue must be null when both current state and step instruction are absent");
            assertNull(result.getCalculationMethod(),
                    "calculationMethod must be null when both current state and step instruction are absent");
            assertNull(result.getObservedRateDates(),
                    "observedRateDates must be null when both current state and step instruction are absent");
        }
    }
}
