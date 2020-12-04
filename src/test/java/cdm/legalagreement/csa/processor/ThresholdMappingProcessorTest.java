package cdm.legalagreement.csa.processor;

import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.legalagreement.csa.ElectiveAmountElection;
import cdm.legalagreement.csa.Threshold;
import cdm.legalagreement.csa.Threshold.ThresholdBuilder;
import cdm.observable.asset.Money;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThresholdMappingProcessorTest {

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

		// test
		Path synonymPath = Path.parse("answers.partyA.threshold");
		ThresholdMappingProcessor processor = new ThresholdMappingProcessor(rosettaPath, Collections.singletonList(synonymPath), context);
		processor.map(synonymPath, builder, null);
		Threshold threshold = builder.build();

		// assert

		ElectiveAmountElection partyA = getPartyElection(threshold, CounterpartyEnum.PARTY_1);
		assertNull(partyA.getCustomElection());
		Money amount = partyA.getAmount();
		assertEquals(10, amount.getAmount().intValue());
		assertEquals("EUR", amount.getCurrency().getValue());

		ElectiveAmountElection partyB = getPartyElection(threshold, CounterpartyEnum.PARTY_2);
		assertNull(partyB.getCustomElection());
		assertNull(partyB.getAmount());
		assertTrue(partyB.getZeroAmount());
	}

	private ElectiveAmountElection getPartyElection(Threshold threshold, CounterpartyEnum party) {
		return threshold.getPartyElection().stream()
				.filter(e -> party == e.getParty())
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No partyElection found for " + party));
	}
}