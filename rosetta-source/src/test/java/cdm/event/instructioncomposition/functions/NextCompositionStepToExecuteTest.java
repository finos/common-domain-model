package cdm.event.instructioncomposition.functions;

import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.event.instructioncomposition.CompositionState;
import cdm.event.instructioncomposition.InstructionComposition;
import cdm.event.instructioncomposition.InstructionCompositionOutput;
import cdm.event.instructioncomposition.InstructionCompositionSteps;
import cdm.event.instructioncomposition.InstructionCompositionTypeEnum;
import cdm.event.instructioncomposition.reset.ResetInstructionCompositionStepsEnum;
import cdm.event.instructioncomposition.reset.ResetInstructionState;
import cdm.event.instructioncomposition.reset.ResetInstructionSteps;
import com.google.inject.Binder;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

class NextCompositionStepToExecuteTest extends AbstractFunctionTest {

    @Inject
    private NextCompositionStepToExecute nextCompositionStepToExecute;

    private ResetInstructionSteps mockResetInstructionSteps;
    private ResetInstructionState capturedResetInstructionState;
    private InstructionCompositionOutput capturedInstructionCompositionOutput;
    private boolean resetInstructionNextStepCalled;

    @Override
    protected void bindTestingMocks(Binder binder) {
        mockResetInstructionSteps = ResetInstructionSteps.builder()
                .setResetInstructionCompositionSteps(ResetInstructionCompositionStepsEnum.values()[0])
                .build();
        binder.bind(ResetInstructionNextStep.class).toInstance(new ResetInstructionNextStep() {
            @Override
            protected ResetInstructionSteps.ResetInstructionStepsBuilder doEvaluate(
                    ResetInstructionState resetInstructionState,
                    InstructionCompositionOutput instructionCompositionOutput) {
                capturedResetInstructionState = resetInstructionState;
                capturedInstructionCompositionOutput = instructionCompositionOutput;
                resetInstructionNextStepCalled = true;
                return mockResetInstructionSteps.toBuilder();
            }
        });
    }

    @BeforeEach
    void resetCapturedArgs() {
        capturedResetInstructionState = null;
        capturedInstructionCompositionOutput = null;
        resetInstructionNextStepCalled = false;
    }

    @Test
    @DisplayName("Delegates to ResetInstructionNextStep with the ResetInstructionState and InstructionCompositionOutput when type is RESET_INSTRUCTION")
    void shouldDelegateToResetInstructionNextStepWithCorrectArgs() {
        ResetInstructionState resetState = ResetInstructionState.builder()
                .setFloatingRateIndex(FloatingRateIndexEnum.USD_SOFR)
                .build();
        InstructionCompositionOutput output = InstructionCompositionOutput.builder().build();
        InstructionComposition instructionComposition = InstructionComposition.builder()
                .setInstructionCompositionType(InstructionCompositionTypeEnum.RESET_INSTRUCTION)
                .setCompositionState(CompositionState.builder()
                        .setResetInstructionState(resetState)
                        .build())
                .setInstructionCompositionOutput(output)
                .build();

        InstructionCompositionSteps result = nextCompositionStepToExecute.evaluate(instructionComposition);

        assertTrue(resetInstructionNextStepCalled, "ResetInstructionNextStep must be called");
        assertEquals(resetState, capturedResetInstructionState,
                "ResetInstructionNextStep must receive the ResetInstructionState from the composition state");
        assertEquals(output, capturedInstructionCompositionOutput,
                "ResetInstructionNextStep must receive the InstructionCompositionOutput");
        assertEquals(mockResetInstructionSteps, result.getResetInstructionSteps(),
                "result must contain the ResetInstructionSteps returned by ResetInstructionNextStep");
    }

    @Test
    @DisplayName("Passes null ResetInstructionState to ResetInstructionNextStep when composition state has no ResetInstructionState")
    void shouldPassNullResetInstructionStateWhenCompositionStateHasNone() {
        InstructionComposition instructionComposition = InstructionComposition.builder()
                .setInstructionCompositionType(InstructionCompositionTypeEnum.RESET_INSTRUCTION)
                .setInstructionCompositionOutput(InstructionCompositionOutput.builder().build())
                .build();

        nextCompositionStepToExecute.evaluate(instructionComposition);

        assertTrue(resetInstructionNextStepCalled, "ResetInstructionNextStep must be called");
        assertNull(capturedResetInstructionState,
                "ResetInstructionNextStep must receive null when there is no ResetInstructionState");
    }

    @Test
    @DisplayName("Passes null InstructionCompositionOutput to ResetInstructionNextStep when it is absent")
    void shouldPassNullInstructionCompositionOutputWhenAbsent() {
        InstructionComposition instructionComposition = InstructionComposition.builder()
                .setInstructionCompositionType(InstructionCompositionTypeEnum.RESET_INSTRUCTION)
                .setCompositionState(CompositionState.builder()
                        .setResetInstructionState(ResetInstructionState.builder()
                                .setFloatingRateIndex(FloatingRateIndexEnum.USD_SOFR)
                                .build())
                        .build())
                .build();

        nextCompositionStepToExecute.evaluate(instructionComposition);

        assertTrue(resetInstructionNextStepCalled, "ResetInstructionNextStep must be called");
        assertNull(capturedInstructionCompositionOutput,
                "ResetInstructionNextStep must receive null when there is no InstructionCompositionOutput");
    }

    @Test
    @DisplayName("Returns null and does not call ResetInstructionNextStep when type is not RESET_INSTRUCTION")
    void shouldReturnNullAndNotDelegateWhenTypeIsNotResetInstruction() {
        InstructionComposition instructionComposition = InstructionComposition.builder().build();

        InstructionCompositionSteps result = nextCompositionStepToExecute.evaluate(instructionComposition);

        assertFalse(resetInstructionNextStepCalled, "ResetInstructionNextStep must not be called for non-reset types");
        assertNull(result, "result must be null for non-reset types");
    }
}

