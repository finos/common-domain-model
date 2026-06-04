package org.finos.cdm.functions;

import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import cdm.event.common.meta.TradeMeta;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.lib.validation.ValidatorFactory;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelClassValidationTest {
	@Inject
	ValidatorFactory validatorFactory;

	private static Injector injector;

	@BeforeAll
	static void setUpOnce() {
		injector = Guice.createInjector(new CdmRuntimeModule());
	}

	@BeforeEach
	void setUp() {
		injector.injectMembers(this);
	}

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

		List<ValidationResult<?>> results = new TradeMeta().validator(validatorFactory).getValidationResults(null, tradeState);
		String resultCombined = results.stream()
				.map(ValidationResult::getFailureReason)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.joining("; "));

		assertEquals("'product' is a required field but does not exist.; " +
						"'tradeLot' is a required field but does not exist.; " +
						"'counterparty' is a required field but does not exist.; " +
						"'tradeIdentifier' is a required field but does not exist.; " +
						"'tradeDate' is a required field but does not exist.",
				resultCombined);
	}

}
