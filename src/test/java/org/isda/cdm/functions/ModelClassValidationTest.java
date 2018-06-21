package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.isda.cdm.Contract;
import org.isda.cdm.meta.ContractMeta;
import org.junit.jupiter.api.Test;

import com.rosetta.model.lib.validation.ValidationResult;

public class ModelClassValidationTest {

	@Test
	void checkErrorMessages() {
		Contract contract = 
				Contract.builder()
					.addBrokerParty("Party1")
					.addBrokerParty("Party2")
					.addBrokerParty("Party3")
					.addDeterminingParty("Party1")
					.addDeterminingParty("Party2")
					.addDeterminingParty("Party3")
					.build();

		ValidationResult<Contract> result = new ContractMeta().validator().validate(contract);
		assertEquals(
				  "contractIdentifier - Expected cardinality lower bound of [1] found [0]; "
				+ "tradeDate - Expected cardinality lower bound of [1] found [0]; "
				+ "contractualProduct - Expected cardinality lower bound of [1] found [0]; "
				+ "calculationAgent - Expected cardinality lower bound of [1] found [0]; "
				+ "determiningParty - Expected cardinality upper bound of [2] found [3]; "
				+ "party - Expected cardinality lower bound of [2] found [0]",
				result.getFailureReason().orElse("No error message"));
	}

}
