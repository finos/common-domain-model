package org.isda.cdm.functions;

import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import cdm.event.common.meta.TradeMeta;
import com.rosetta.model.lib.validation.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelClassValidationTest {

	@Test
	void checkErrorMessages() {
		Trade tradeState =
            Trade.builder()
                    .addPartyRole(PartyRole.builder()
                            .setPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party1").build())
                            .setRole(PartyRoleEnum.DETERMINING_PARTY)
                            .build())
                    .addPartyRole(PartyRole.builder()
                            .setPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party1").build())
                            .setRole(PartyRoleEnum.DETERMINING_PARTY)
                            .build())
                .build();

		ValidationResult<? super Trade> result = new TradeMeta().validator().validate(null, tradeState);
		assertEquals(
				"tradableProduct - Expected cardinality lower bound of [1] found [0]; tradeDate - Expected cardinality lower bound of [1] found [0]; tradeIdentifier - Expected cardinality lower bound of [1] found [0]",
				result.getFailureReason().orElse("No error message"));
	}

}
