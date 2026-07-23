package cdm.event.instructioncomposition.functions;

import cdm.event.instructioncomposition.CompositionState;
import cdm.event.instructioncomposition.CompositionStepInstruction;
import cdm.event.instructioncomposition.CompositionStepInstructions;
import cdm.event.instructioncomposition.InstructionCompositionTypeEnum;
import cdm.event.instructioncomposition.reset.ResetInstructionState;
import com.google.inject.Binder;
import javax.inject.Inject;

import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddStepToCompositionStateTest extends AbstractFunctionTest {

    @Inject
    private AddStepToCompositionState addStepToCompositionState;

    private ResetInstructionState capturedInitialResetState;
    private CompositionStepInstructions capturedNextStep;
    private boolean resetCompositionStateUpdated;

    @Override
    protected void bindTestingMocks(Binder binder) {
        binder.bind(UpdateResetCompositionState.class).toInstance(new UpdateResetCompositionState() {
            @Override
            protected ResetInstructionState.ResetInstructionStateBuilder doEvaluate(
                    ResetInstructionState initialResetCompositionState,
                    CompositionStepInstructions nextStepToAdd) {
                capturedInitialResetState = initialResetCompositionState;
                capturedNextStep = nextStepToAdd;
                resetCompositionStateUpdated = true;
                return ResetInstructionState.builder();
            }
        });
    }

    @BeforeEach
    void resetCapturedArgs() {
        capturedInitialResetState = null;
        capturedNextStep = null;
        resetCompositionStateUpdated = false;
    }

    @Test
    @DisplayName("Updates reset composition state when adding a reset instruction without an initial composition state")
    void shouldUpdateResetCompositionStateWhenTypeIsResetInstructionAndInitialStateIsAbsent() {
        CompositionStepInstruction nextStep = buildResetStepInstruction();

        CompositionState result = addStepToCompositionState.evaluate(null, nextStep);

        assertNotNull(result, "result must not be null");
        assertTrue(resetCompositionStateUpdated, "UpdateResetCompositionState must be called");
        assertNull(capturedInitialResetState,  "UpdateResetCompositionState must receive null when no initial state");
        assertEquals(nextStep.getCompositionStepInstructions(), capturedNextStep,
                "UpdateResetCompositionState must receive the CompositionStepInstructions from nextStepToAdd");
    }

    @Test
    @DisplayName("Updates reset composition state when adding a reset instruction with the initial composition state with no reset instruction state")
    void shouldUpdateResetCompositionStateWhenTypeIsResetInstructionAndInitialCompositionStateHasNoResetInstructionState() {
        CompositionState initialStateWithNoResetState = CompositionState.builder().build();
        CompositionStepInstruction nextStep = buildResetStepInstruction();

        addStepToCompositionState.evaluate(initialStateWithNoResetState, nextStep);

        assertTrue(resetCompositionStateUpdated, "UpdateResetCompositionState must be called");
        assertNull(capturedInitialResetState,
                "UpdateResetCompositionState must receive null when CompositionState has no ResetInstructionState");
        assertEquals(nextStep.getCompositionStepInstructions(), capturedNextStep,
                "UpdateResetCompositionState must receive the CompositionStepInstructions from nextStepToAdd");
    }

    @Test
    @DisplayName("Updates reset composition state when adding a reset instruction with initial composition state that has reset instruction state")
    void shouldUpdateResetCompositionStateWhenTypeIsResetInstructionAndInitialCompositionStateHasResetInstructionState() {
        ResetInstructionState existingResetState = ResetInstructionState.builder().build();
        CompositionState initialState = CompositionState.builder()
                .setResetInstructionState(existingResetState)
                .build();
        CompositionStepInstruction nextStep = buildResetStepInstruction();

        addStepToCompositionState.evaluate(initialState, nextStep);

        assertTrue(resetCompositionStateUpdated, "UpdateResetCompositionState must be called");
        assertEquals(capturedInitialResetState, existingResetState,
                "UpdateResetCompositionState must receive the existing ResetInstructionState from initial CompositionState");
        assertEquals(nextStep.getCompositionStepInstructions(), capturedNextStep,
                "UpdateResetCompositionState must receive the CompositionStepInstructions from nextStepToAdd");
    }

    @Test
    @DisplayName("Does not update reset composition state when the next step is not a reset instruction")
    void shouldNotUpdateResetCompositionStateWhenInstructionTypeIsAbsent() {
        CompositionStepInstruction nextStepWithoutType = CompositionStepInstruction.builder()
                .setCompositionStepInstructions(CompositionStepInstructions.builder().build())
                .build();

        addStepToCompositionState.evaluate(null, nextStepWithoutType);
        assertFalse(resetCompositionStateUpdated,
                "UpdateResetCompositionState must not be called when type is absent with null initial state");

        CompositionState initialState = CompositionState.builder()
                .setResetInstructionState(ResetInstructionState.builder().build())
                .build();
        addStepToCompositionState.evaluate(initialState, nextStepWithoutType);
        assertFalse(resetCompositionStateUpdated,
                "UpdateResetCompositionState must not be called when type is absent, " +
                        "even with initial state having ResetInstructionState");
    }

    //-------- Helper methods -----------
    private static CompositionStepInstruction buildResetStepInstruction() {
        return CompositionStepInstruction.builder()
                .setInstructionCompositionType(InstructionCompositionTypeEnum.RESET_INSTRUCTION)
                .setCompositionStepInstructions(CompositionStepInstructions.builder().build())
                .build();
    }
}
