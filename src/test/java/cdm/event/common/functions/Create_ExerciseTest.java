package cdm.event.common.functions;

import cdm.event.common.*;
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
		TradeState swaption = getObject(TradeState.class,"result-json-files/products/rates/ird-ex09-euro-swaption-explicit-physical-exercise.json");

		BusinessEvent businessEvent = func.evaluate(swaption, ExerciseInstruction.builder().build());
		
		assertEquals(getJson("expected-physical-exercise-business-event.json"), toJson(businessEvent.build()));
		//assertThat(getJson("expected-physical-exercise-business-event.json"), new IsEqualIgnoringWhiteSpace(toJson(businessEvent)));
	}

	private String toJson(BusinessEvent businessEvent) throws JsonProcessingException {
		return RosettaObjectMapper.getNewRosettaObjectMapper()
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(businessEvent)
				.replace("\r", "");
	}
}
