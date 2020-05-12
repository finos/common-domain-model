package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.MinimumTransferAmount;
import org.isda.cdm.Money;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.isda.cdm.CreditSupportObligationsInitialMargin.CreditSupportObligationsInitialMarginBuilder;
import static org.isda.cdm.MinimumTransferAmount.MinimumTransferAmountBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class MinimumTransferAmountMappingProcessorTest {

	private static final String PARTY_A = "partyA";
	private static final String PARTY_B = "partyB";
	private static final String ZERO = "zero";

	@Test
	void shouldMapMta() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.csdInitialMargin2016EnglishLaw.creditSupportObligations.minimumTransferAmount");
		List<Mapping> mappings = new ArrayList<>();
		mappings.add(new Mapping(Path.parse("answers.partyA.minimum_transfer_amount.partyA_minimum_transfer_amount"), "specify", null, null, "no destination", false, false));
		mappings.add(new Mapping(Path.parse("answers.partyA.minimum_transfer_amount.partyA_amount"), "10000", null, null, "no destination", false, false));
		mappings.add(new Mapping(Path.parse("answers.partyA.minimum_transfer_amount.partyA_currency"), "Euro", null, null, "no destination", false, false));
		mappings.add(new Mapping(Path.parse("answers.partyA.minimum_transfer_amount.partyB_minimum_transfer_amount"), ZERO, null, null, "no destination", false, false));

		MinimumTransferAmountBuilder builder = MinimumTransferAmount.builder();
		CreditSupportObligationsInitialMarginBuilder parent = mock(CreditSupportObligationsInitialMarginBuilder.class);

		// test
		MinimumTransferAmountMappingProcessor processor = new MinimumTransferAmountMappingProcessor(rosettaPath, Collections.emptyList(), mappings);
		processor.map(builder, parent);
		MinimumTransferAmount minimumTransferAmount = builder.build();

		// assert

		ElectiveAmountElection partyA = getPartyElection(minimumTransferAmount, PARTY_A);
		assertNull(partyA.getCustomElection());
		Money amount = partyA.getAmount();
		assertEquals(10000, amount.getAmount().intValue());
		assertEquals("Euro", amount.getCurrency().getValue());

		ElectiveAmountElection partyB = getPartyElection(minimumTransferAmount, PARTY_B);
		assertNull(partyB.getCustomElection());
		assertEquals(0, partyB.getAmount().getAmount().intValue());
	}

	private ElectiveAmountElection getPartyElection(MinimumTransferAmount minimumTransferAmount, String party) {
		return minimumTransferAmount.getPartyElection().stream()
				.filter(e -> party.equals(e.getParty()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No partyElection found for " + party));
	}
}