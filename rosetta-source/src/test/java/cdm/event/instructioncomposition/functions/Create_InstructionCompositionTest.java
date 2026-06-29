package cdm.event.instructioncomposition.functions;

import cdm.event.common.TradeIdentifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.event.instructioncomposition.*;

import javax.inject.Inject;

import com.rosetta.model.lib.functions.ConditionValidator;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Create_InstructionCompositionTest extends AbstractFunctionTest {

    @Inject
    private Create_InstructionComposition createInstructionComposition;

    @Nested
    @DisplayName("First step scenarios - where no previous InstructionComposition state exists")
    class FirstStepScenarios {

        @Test
        @DisplayName("Creates a first step InstructionComposition with the supplied identifier, type, and step instruction")
        void shouldCreateInstructionCompositionOnFirstStepWithInstructionCompositionIdentifierAndType() {
            String instructionCompositionId = "IC-001";
            String stepId = "step-1";
            CompositionStepInstruction stepInstruction = buildResetStepInstruction();

            InstructionComposition result = createInstructionComposition.evaluate(
                    null,
                    instructionCompositionId,
                    null,
                    InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    stepId,
                    stepInstruction
            );

            assertNotNull(result, "InstructionComposition should be created");

            assertEquals(instructionCompositionId, result.getIdentifier(), "identifier must be set on the new InstructionComposition");
            assertEquals(InstructionCompositionTypeEnum.RESET_INSTRUCTION, result.getInstructionCompositionType());
            assertNull(result.getTradeId(), "tradeId should be null when not provided on the first step");

            assertEquals(1, result.getCompositionStep().size(), "compositionStep must be stored on the new InstructionComposition");
            CompositionStep step = result.getCompositionStep().get(0);
            assertEquals(stepId, step.getIdentifier(), "identifier must be set on the new CompositionStep");
            assertNotNull(step.getCompositionStepInstruction(),
                    "compositionStepInstruction must be stored on the new CompositionStep");
            assertEquals(InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    step.getCompositionStepInstruction().getInstructionCompositionType(), "type must be set on the new CompositionStep");
            assertNotNull(step.getTimestamp(), "timestamp must be set by Now()");
        }

        @Test
        @DisplayName("Attaches the supplied trade identifier when present on the first step")
        void shouldAttachTradeIdOnFirstStepWhenProvided() {
            TradeIdentifier tradeId = buildTradeIdentifier();

            InstructionComposition result = createInstructionComposition.evaluate(
                    null,
                    "IC-001",
                    tradeId,
                    InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    "step-1",
                    buildResetStepInstruction()
            );

            assertNotNull(result.getTradeId(), "tradeId must be set when provided on the first step");
            assertEquals(tradeId, result.getTradeId(), "tradeId must be set to the one provided on the first step");
        }

        @ParameterizedTest(name = "{0}")
        @CsvSource(
                value = {
                        "Missing identifier, NULL, RESET_INSTRUCTION",
                        "Missing type, IC-001, NULL",
                        "Missing both identifier and type, NULL, NULL"
                },
                nullValues = "NULL")
        @DisplayName("Fails validation when the first step omits required identifier or type values")
        void shouldFailWhenFirstStepIsMissingRequiredIdentifierOrType(
                String scenario,
                String instructionCompositionIdentifier,
                InstructionCompositionTypeEnum instructionCompositionType) {
            ConditionValidator.ConditionException exception = assertThrows(ConditionValidator.ConditionException.class, () ->
                    createInstructionComposition.evaluate(
                            null,
                            instructionCompositionIdentifier,
                            null,
                            instructionCompositionType,
                            "step-1",
                            buildResetStepInstruction()
                    )
            );
            assertEquals("Validation rule ensuring that for the initial execution (where no previous state exists), " +
                            "both the composition identifier and the composition type must be explicitly defined.",
                    exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Subsequent step scenarios - where a previous InstructionComposition state exists and should be inherited from")
    class SubsequentStepScenarios {

        @Test
        @DisplayName("Creates an InstructionComposition for the subsequent step and inherits identifier and type from the previous one")
        void shouldInheritIdentifierTypeOnSubsequentStep() {
            String instructionCompositionId = "IC-001";
            String step1Id = "step-1";
            String step2Id = "step-2";

            InstructionComposition first = createInstructionComposition.evaluate(
                    null,
                    instructionCompositionId,
                    null,
                    InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    step1Id,
                    buildResetStepInstruction());

            InstructionComposition result = createInstructionComposition.evaluate(
                    first,
                    null,
                    null,
                    null,
                    step2Id,
                    buildResetStepInstruction());

            assertNotNull(result, "InstructionComposition should be created");

            assertEquals(instructionCompositionId, result.getIdentifier(),
                    "identifier must be inherited from previous InstructionComposition");
            assertEquals(InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    result.getInstructionCompositionType(), "type must be inherited");
            assertNull(result.getTradeId(), "null tradeId must remain null across steps");

            assertEquals(2, result.getCompositionStep().size(),
                    "compositionStep must be stored on the new InstructionComposition");
            assertEquals(step1Id, result.getCompositionStep().get(0).getIdentifier(),
                    "Previous step must be carried forward");
            assertEquals(step2Id, result.getCompositionStep().get(1).getIdentifier(),
                    "identifier must be set on the new CompositionStep");
            result.getCompositionStep().forEach(step ->
                    assertNotNull(step.getTimestamp(), "every step must have a timestamp set by Now()"));
        }

        @Test
        @DisplayName("Carries forward the existing identifier and type and ignores newly supplied ones on later steps")
        void shouldCarryForwardIdentifierAndTypeAndIgnoreNewlySuppliedOnSubsequentStep() {
            String instructionCompositionId = "IC-001";

            InstructionComposition first = createInstructionComposition.evaluate(
                    null,
                    instructionCompositionId,
                    null,
                    InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    "step-1",
                    buildResetStepInstruction());

            InstructionComposition result = createInstructionComposition.evaluate(
                    first,
                    "IC-002",
                    null,
                    InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    "step-2",
                    buildResetStepInstruction()
            );

            assertEquals(instructionCompositionId, result.getIdentifier(),
                    "identifier from previous composition must take precedence over a newly supplied one");
            assertEquals(InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    result.getInstructionCompositionType(),
                    "type from previous composition must take precedence over a newly supplied one");
        }

        @Test
        @DisplayName("Carries forward the existing trade identifier and ignores a newly supplied one on later steps")
        void shouldCarryForwardTradeIdAndIgnoreNewlySuppliedOnSubsequentStep() {
            TradeIdentifier originalTradeId = buildTradeIdentifier();
            TradeIdentifier differentTradeId = TradeIdentifier.builder()
                    .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_SWAP_IDENTIFIER)
                    .build();

            InstructionComposition first = createInstructionComposition.evaluate(
                    null,
                    "IC-001",
                    originalTradeId,
                    InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    "step-1",
                    buildResetStepInstruction());

            InstructionComposition second = createInstructionComposition.evaluate(
                    first,
                    null,
                    null,
                    null,
                    "step-2",
                    buildResetStepInstruction());

            assertNotNull(second.getTradeId(), "tradeId must be carried forward when not re-supplied");
            assertEquals(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER,
                    second.getTradeId().getIdentifierType());

            InstructionComposition third = createInstructionComposition.evaluate(
                    second,
                    null,
                    differentTradeId,
                    null,
                    "step-3",
                    buildResetStepInstruction());
            assertEquals(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER,
                    third.getTradeId().getIdentifierType(),
                    "tradeId from previous composition must take precedence over a newly supplied one");
        }

        @Test
        @DisplayName("Accumulates steps in execution order while timestamping each step")
        void shouldAccumulateStepsWithIdentifiersAndTimestampsAcrossMultipleExecutions() {
            String step1Id = "step-1";
            String step2Id = "step-2";
            String step3Id = "step-3";

            InstructionComposition step1 = createInstructionComposition.evaluate(
                    null,
                    "IC-001",
                    null,
                    InstructionCompositionTypeEnum.RESET_INSTRUCTION,
                    step1Id,
                    buildResetStepInstruction());
            InstructionComposition step2 = createInstructionComposition.evaluate(
                    step1,
                    null,
                    null,
                    null,
                    step2Id,
                    buildResetStepInstruction());
            InstructionComposition step3 = createInstructionComposition.evaluate(
                    step2,
                    null,
                    null,
                    null,
                    step3Id,
                    buildResetStepInstruction());

            assertEquals(3, step3.getCompositionStep().size(), "all steps must be accumulated on the new InstructionComposition");
            assertEquals(step1Id, step3.getCompositionStep().get(0).getIdentifier(), "steps must be stored in the order they were executed");
            assertEquals(step2Id, step3.getCompositionStep().get(1).getIdentifier(), "steps must be stored in the order they were executed");
            assertEquals(step3Id, step3.getCompositionStep().get(2).getIdentifier(), "steps must be stored in the order they were executed");
            step3.getCompositionStep().forEach(step ->
                    assertNotNull(step.getTimestamp(), "every step must have a timestamp set by Now()"));
        }
    }

    //-------- Helper methods -----------
    private static CompositionStepInstruction buildResetStepInstruction() {
        return CompositionStepInstruction.builder()
                .setInstructionCompositionType(InstructionCompositionTypeEnum.RESET_INSTRUCTION)
                .setCompositionStepInstructions(CompositionStepInstructions.builder().build())
                .build();
    }

    private static TradeIdentifier buildTradeIdentifier() {
        return TradeIdentifier.builder()
                .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                .build();
    }
}
