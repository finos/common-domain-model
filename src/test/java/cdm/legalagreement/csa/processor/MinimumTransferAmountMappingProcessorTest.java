package cdm.legalagreement.csa.processor;

import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legalagreement.csa.ElectiveAmountElection;
import cdm.legalagreement.csa.ElectiveAmountEnum;
import cdm.legalagreement.csa.MinimumTransferAmount;
import cdm.legalagreement.csa.MinimumTransferAmount.MinimumTransferAmountBuilder;
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

class MinimumTransferAmountMappingProcessorTest {

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
		MappingContext context = new MappingContext(mappings, Collections.emptyMap());

		MinimumTransferAmountBuilder builder = MinimumTransferAmount.builder();

		// test
		Path synonymPath = Path.parse("answers.partyA.minimum_transfer_amount");
		MinimumTransferAmountMappingProcessor processor = new MinimumTransferAmountMappingProcessor(rosettaPath,
				Collections.singletonList(synonymPath),
				context);
		processor.map(synonymPath, builder, null);
		MinimumTransferAmount minimumTransferAmount = builder.build();

		// assert

		ElectiveAmountElection partyA = getPartyElection(minimumTransferAmount, CounterpartyRoleEnum.PARTY_1);
		assertNull(partyA.getCustomElection());
		Money amount = partyA.getAmount();
		assertEquals(10000, amount.getAmount().intValue());
		assertEquals("EUR", amount.getCurrency().getValue());

		ElectiveAmountElection partyB = getPartyElection(minimumTransferAmount, CounterpartyRoleEnum.PARTY_2);
		assertNull(partyB.getCustomElection());
		assertNull(partyB.getAmount());
		assertEquals(ElectiveAmountEnum.ZERO, partyB.getElectiveAmount());
	}

	private ElectiveAmountElection getPartyElection(MinimumTransferAmount minimumTransferAmount, CounterpartyRoleEnum party) {
		return minimumTransferAmount.getPartyElection().stream()
				.filter(e -> party == e.getParty())
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No partyElection found for " + party));
	}
}