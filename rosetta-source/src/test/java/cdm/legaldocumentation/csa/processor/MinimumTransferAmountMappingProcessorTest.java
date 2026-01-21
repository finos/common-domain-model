package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legaldocumentation.csa.ElectiveAmountElection;
import cdm.legaldocumentation.csa.ElectiveAmountEnum;
import cdm.legaldocumentation.csa.MinimumTransferAmount;
import cdm.observable.asset.Money;
import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MinimumTransferAmountMappingProcessorTest {

	private static final String ZERO = "zero";

	@Test
	void shouldMapMta() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.csdInitialMargin2016EnglishLaw.creditSupportObligations.minimumTransferAmount");
		List<Mapping> mappings = new ArrayList<>();
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.minimum_transfer_amount.partyA_minimum_transfer_amount"), "specify"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.minimum_transfer_amount.partyA_amount"), "10000"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.minimum_transfer_amount.partyA_currency"), "Euro"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.minimum_transfer_amount.partyB_minimum_transfer_amount"), ZERO));
		Map<Class<?>, Map<String, Enum<?>>> synonymToEnumMap = ImmutableMap.<Class<?>, Map<String, Enum<?>>>builder()
				.put(ISOCurrencyCodeEnum.class, ImmutableMap.<String, Enum<?>>builder()
						.put("Euro", ISOCurrencyCodeEnum.EUR)
						.build())
				.build();
		MappingContext context = new MappingContext(mappings, Collections.emptyMap(), synonymToEnumMap);

		MinimumTransferAmount.MinimumTransferAmountBuilder builder = MinimumTransferAmount.builder();

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
		assertEquals(10000, amount.getValue().intValue());
		assertEquals("EUR", amount.getUnit().getCurrency().getValue());

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

	private Mapping getEmptyMapping(Path xmlPath, String xmlValue) {
		return new Mapping(xmlPath, xmlValue, null, null, "no destination", false, false, false);
	}
}