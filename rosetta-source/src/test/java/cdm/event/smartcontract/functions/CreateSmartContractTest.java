package cdm.event.smartcontract.functions;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.event.common.TradeIdentifier;
import cdm.event.smartcontract.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.ResourcesUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CreateSmartContractTest extends AbstractFunctionTest {

    private static final ObjectMapper OBJECT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper();
    private static final String SMART_CONTRACT_PATH = "functions/smart-contract/";

    @Inject
    Create_SmartContractStepFromInstruction createSmartContract;

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("collect-floating-rate-option", null, 1)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCases")
    @DisplayName("Create smart contract step with instruction")
    void shouldCreateSmartContractWithInstruction(String testStepName, String previousStepName, int stepNumber) throws IOException {
        // Given a TradeIdentifier, instruction, previous smart contract steps and history
        TradeIdentifier tradeId = TradeIdentifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifierValue("ExampleTradeId")
                        .build())
                .build();
        String smartContractIdentifier = "reset-smartContract";
        String stepIdentifier = "stepIdentifier-" + stepNumber;

        List<SmartContractStep> previousSmartContractSteps = getSmartContractSteps(previousStepName);
        SmartContractHistory previousSmartContractHistory = getSmartContractHistory(previousStepName);
        SmartContractInstructionSteps proposedSmartContractStepInstructions =
                getSmartContractInstructionSteps(testStepName);

        // When the function is evaluated
        SmartContract result =
                createSmartContract.evaluate(
                        previousSmartContractSteps,
                        previousSmartContractHistory,
                        smartContractIdentifier,
                        tradeId,
                        SmartContractTypeEnum.RESET_INSTRUCTION,
                        stepIdentifier,
                        proposedSmartContractStepInstructions);

        // Then a SmartContract is created with the expected properties
        assertNotNull(result, "Result should not be null");

        // Verify the instruction step is added
        assertEquals(stepNumber, result.getSmartContractStep().size(), String.format("Smart contract should have %d steps", stepNumber));
        assertEquals(proposedSmartContractStepInstructions, result.getSmartContractStep().get(stepNumber - 1).getSmartContractStepInstruction().getSmartContractStepInstructions(),
                "The proposed instructions should be added as a new step in the smart contract");
        assertAddedToHistory(result.getSmartContractHistory(), proposedSmartContractStepInstructions, testStepName);

        // Verify SmartContract properties
        assertEquals(tradeId, result.getTradeId());
        assertEquals(smartContractIdentifier, result.getSmartContractIdentifier());
        assertEquals(SmartContractTypeEnum.RESET_INSTRUCTION, result.getSmartContractType());
    }

    private static SmartContractInstructionSteps getSmartContractInstructionSteps(String previousStepName)
            throws IOException {
        String path = SMART_CONTRACT_PATH
                + "smart-contract-instruction-steps/smart-contract-instruction-steps-"
                + previousStepName
                + ".json";

        return ResourcesUtils.getObjectAndResolveReferences(SmartContractInstructionSteps.class, path);
    }

    private static SmartContractHistory getSmartContractHistory(String previousStepName)
            throws JsonProcessingException {
        if (previousStepName == null) {
            return null;
        }
        String path = SMART_CONTRACT_PATH + "smart-contract-history/smart-contract-history-" + previousStepName + ".json";

        return OBJECT_MAPPER.readValue(path, SmartContractHistory.class);
    }

    private static List<SmartContractStep> getSmartContractSteps(String previousStepName)
            throws IOException {
        if (previousStepName == null) {
            return null;
        }

        String previousStepsJson = SMART_CONTRACT_PATH
                + "smart-contract-steps/smart-contract-steps"
                + previousStepName
                + "_accepted.json";
        return ResourcesUtils.getObjectList(SmartContractStep.class, previousStepsJson);
    }

   private static void assertAddedToHistory(SmartContractHistory history, SmartContractInstructionSteps instructions, String stepName) {
        assertNotNull(history, "History should not be null");
        switch (stepName) {
            case "collect-floating-rate-option":
                assertNotNull(history.getCollectedFloatingRateOption(), "CollectedFloatingRateOption should be added to history");
                assertEquals(instructions.getCollectFloatingRateOption(), history.getCollectedFloatingRateOption(), "CollectedFloatingRateOption in history should match the instruction");
                break;
            default:
                fail("Unexpected step name: " + stepName);
        }
    }
}
