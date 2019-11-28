package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.MinimumTransferAmount;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.isda.cdm.CreditSupportObligationsInitialMargin.CreditSupportObligationsInitialMarginBuilder;
import static org.isda.cdm.MinimumTransferAmount.MinimumTransferAmountBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class MinimumTransferAmountMappingProcessorTest {

	private static final String PARTY_A = "partyA";
	private static final String PARTY_B = "partyB";
	private static final String SPECIFY = "specify";
	private static final String ZERO = "zero";

	@Test
	void shouldSetNoAmountWhenCustomElectionIsZero() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.csdInitialMargin2016EnglishLaw.creditSupportObligations.minimumTransferAmount");
		List<Mapping> mappings = new ArrayList<>();
		MinimumTransferAmountBuilder builder = MinimumTransferAmount.builder()
				.addPartyElectionBuilder(ElectiveAmountElection.builder()
						.setParty(PARTY_A)
						.setCustomElection(SPECIFY))
				.addPartyElectionBuilder(ElectiveAmountElection.builder()
						.setParty(PARTY_B)
						.setCustomElection(ZERO));
		CreditSupportObligationsInitialMarginBuilder parent = mock(CreditSupportObligationsInitialMarginBuilder.class);

		// test
		MinimumTransferAmountMappingProcessor processor = new MinimumTransferAmountMappingProcessor(rosettaPath, mappings);
		processor.map(builder, parent);
		MinimumTransferAmount minimumTransferAmount = builder.build();

		// assert

		ElectiveAmountElection partyA = getPartyElection(minimumTransferAmount, PARTY_A);
		assertEquals(SPECIFY, partyA.getCustomElection());
		assertNull(partyA.getNoAmount());

		ElectiveAmountElection partyB = getPartyElection(minimumTransferAmount, PARTY_B);
		assertEquals(ZERO, partyB.getCustomElection());
		assertEquals(Integer.valueOf(0), partyB.getNoAmount());
	}

	private ElectiveAmountElection getPartyElection(MinimumTransferAmount minimumTransferAmount, String party) {
		return minimumTransferAmount.getPartyElection().stream()
				.filter(e -> party.equals(e.getParty()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No partyElection found for " + party));
	}
}