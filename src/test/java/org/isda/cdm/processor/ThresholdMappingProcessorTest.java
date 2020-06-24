package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.ElectiveAmountElection;
import org.isda.cdm.Money;
import org.isda.cdm.Threshold;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.isda.cdm.CreditSupportObligationsInitialMargin.CreditSupportObligationsInitialMarginBuilder;
import static org.isda.cdm.Threshold.ThresholdBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ThresholdMappingProcessorTest {

	private static final String PARTY_A = "partyA";
	private static final String PARTY_B = "partyB";
	private static final String ZERO = "zero";

	@Test
	void shouldMapThreshold() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.csdInitialMargin2016EnglishLaw.creditSupportObligations.threshold");
		List<Mapping> mappings = new ArrayList<>();
		mappings.add(new Mapping(Path.parse("answers.partyA.threshold.partyA_threshold"), "specify", null, null, "no destination", false, false));
		mappings.add(new Mapping(Path.parse("answers.partyA.threshold.partyA_amount"), "10", null, null, "no destination", false, false));
		mappings.add(new Mapping(Path.parse("answers.partyA.threshold.partyA_currency"), "Euro", null, null, "no destination", false, false));
		mappings.add(new Mapping(Path.parse("answers.partyA.threshold.partyB_threshold"), ZERO, null, null, "no destination", false, false));
		MappingContext context = new MappingContext(mappings, Collections.emptyMap());

		ThresholdBuilder builder = Threshold.builder();
		CreditSupportObligationsInitialMarginBuilder parent = mock(CreditSupportObligationsInitialMarginBuilder.class);

		// test
		Path synonymPath = Path.parse("answers.partyA.threshold");
		ThresholdMappingProcessor processor = new ThresholdMappingProcessor(rosettaPath, Collections.singletonList(synonymPath), context);
		processor.map(synonymPath, builder, parent);
		Threshold threshold = builder.build();

		// assert

		ElectiveAmountElection partyA = getPartyElection(threshold, PARTY_A);
		assertNull(partyA.getCustomElection());
		Money amount = partyA.getAmount();
		assertEquals(10, amount.getAmount().intValue());
		assertEquals("EUR", amount.getCurrency().getValue());

		ElectiveAmountElection partyB = getPartyElection(threshold, PARTY_B);
		assertNull(partyB.getCustomElection());
		assertNull(partyB.getAmount());
		assertTrue(partyB.getZeroAmount());
	}

	private ElectiveAmountElection getPartyElection(Threshold threshold, String party) {
		return threshold.getPartyElection().stream()
				.filter(e -> party.equals(e.getParty()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No partyElection found for " + party));
	}
}