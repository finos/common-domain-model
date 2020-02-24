package org.isda.cdm.functions;

import com.rosetta.model.lib.validation.ValidationResult;

import org.isda.cdm.Contract;
import org.isda.cdm.PartyRole;
import org.isda.cdm.PartyRoleEnum;
import org.isda.cdm.meta.ContractMeta;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelClassValidationTest {

	@Test
	void checkErrorMessages() {
		Contract contract = 
				Contract.builder()
					.addPartyRole(PartyRole.builder()
							.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party1").build())
							.setRole(PartyRoleEnum.DETERMINING_PARTY)
							.build())
					.addPartyRole(PartyRole.builder()
							.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party1").build())
							.setRole(PartyRoleEnum.DETERMINING_PARTY)
							.build())
					.build();

		ValidationResult<? super Contract> result = new ContractMeta().validator().validate(null, contract);
		assertEquals(
				"contractIdentifier - Expected cardinality lower bound of [1] found [0]; tradeDate - Expected cardinality lower bound of [1] found [0]; tradableProduct - Expected cardinality lower bound of [1] found [0]",
				result.getFailureReason().orElse("No error message"));
	}

}
