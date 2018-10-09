package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.isda.cdm.Contract;
import org.isda.cdm.PartyRole;
import org.isda.cdm.PartyRoleEnum;
import org.isda.cdm.meta.ContractMeta;
import org.junit.jupiter.api.Test;

import com.rosetta.model.lib.validation.ValidationResult;

public class ModelClassValidationTest {

	@Test
	void checkErrorMessages() {
		Contract contract = 
				Contract.builder()
					.addPartyRole(PartyRole.builder()
							.setPartyReference("party1")
							.setRole(PartyRoleEnum.DETERMINING_PARTY)
							.build())
					.addPartyRole(PartyRole.builder()
							.setPartyReference("party1")
							.setRole(PartyRoleEnum.DETERMINING_PARTY)
							.build())
					.build();

		ValidationResult<Contract> result = new ContractMeta().validator().validate(contract);
		assertEquals(
				"contractIdentifier - Expected cardinality lower bound of [1] found [0]; "
				+ "tradeDate - Expected cardinality lower bound of [1] found [0]; "
				+ "contractualProduct - Expected cardinality lower bound of [1] found [0]",
				result.getFailureReason().orElse("No error message"));
	}

}
