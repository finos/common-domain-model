package cdm.event.common.functions;

import cdm.event.common.*;
import cdm.legalagreement.contract.Contract;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.getJson;
import static util.ResourcesUtils.getObject;

public class Create_ExerciseTest extends AbstractFunctionTest {

	@Inject
	private Create_Exercise func;

	@Test
	void shouldCreatePhysicalExercise() throws IOException {
		Contract swaption = getObject(Contract.class,"result-json-files/products/rates/ird-ex09-euro-swaption-explicit-physical-exercise.json");

		BusinessEvent contractFormation = BusinessEvent.builder()
				.addPrimitivesBuilder(PrimitiveEvent.builder()
					.setContractFormationBuilder(ContractFormationPrimitive.builder()
						.setAfterBuilder(PostContractFormationState.builder()
							.setContract(swaption))))
				.build();

		BusinessEvent businessEvent = func.evaluate(contractFormation, ExerciseInstruction.builder().build());

		assertEquals(getJson("expected-physical-exercise-business-event.json"), toJson(businessEvent));
	}

	@Test
	void shouldCreateExercise() throws IOException {
		Contract swaption = getObject(Contract.class,"result-json-files/products/rates/ird-ex09-euro-swaption-explicit-versioned.json");

		BusinessEvent contractFormation = BusinessEvent.builder()
				.addPrimitivesBuilder(PrimitiveEvent.builder()
						.setContractFormationBuilder(ContractFormationPrimitive.builder()
								.setAfterBuilder(PostContractFormationState.builder()
										.setContract(swaption))))
				.build();

		BusinessEvent businessEvent = func.evaluate(contractFormation, ExerciseInstruction.builder().build());

		assertEquals(getJson("expected-exercise-business-event.json"), toJson(businessEvent));
	}

	private String toJson(BusinessEvent businessEvent) throws JsonProcessingException {
		return RosettaObjectMapper.getNewRosettaObjectMapper()
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(businessEvent);
	}
}
