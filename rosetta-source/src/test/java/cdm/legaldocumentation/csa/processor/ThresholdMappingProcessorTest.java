package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legaldocumentation.csa.ElectiveAmountElection;
import cdm.legaldocumentation.csa.ElectiveAmountEnum;
import cdm.legaldocumentation.csa.Threshold;
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

class ThresholdMappingProcessorTest {

	private static final String ZERO = "zero";

	@Test
	void shouldMapThreshold() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.csdInitialMargin2016EnglishLaw.creditSupportObligations.threshold");
		List<Mapping> mappings = new ArrayList<>();
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.threshold.partyA_threshold"), "specify"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.threshold.partyA_amount"), "10"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.threshold.partyA_currency"), "Euro"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.threshold.partyB_threshold"), ZERO));
		Map<Class<?>, Map<String, Enum<?>>> synonymToEnumMap = ImmutableMap.<Class<?>, Map<String, Enum<?>>>builder()
				.put(ISOCurrencyCodeEnum.class, ImmutableMap.<String, Enum<?>>builder()
						.put("Euro", ISOCurrencyCodeEnum.EUR)
						.build())
				.build();
		MappingContext context = new MappingContext(mappings, Collections.emptyMap(), synonymToEnumMap);

		Threshold.ThresholdBuilder builder = Threshold.builder();

		// test
		Path synonymPath = Path.parse("answers.partyA.threshold");
		ThresholdMappingProcessor processor = new ThresholdMappingProcessor(rosettaPath, Collections.singletonList(synonymPath), context);
		processor.map(synonymPath, builder, null);
		Threshold threshold = builder.build();

		// assert

		ElectiveAmountElection partyA = getPartyElection(threshold, CounterpartyRoleEnum.PARTY_1);
		assertNull(partyA.getCustomElection());
		Money amount = partyA.getAmount();
		assertEquals(10, amount.getValue().intValue());
		assertEquals("EUR", amount.getUnit().getCurrency().getValue());

		ElectiveAmountElection partyB = getPartyElection(threshold, CounterpartyRoleEnum.PARTY_2);
		assertNull(partyB.getCustomElection());
		assertNull(partyB.getAmount());
		assertEquals(ElectiveAmountEnum.ZERO, partyB.getElectiveAmount());
	}

	private ElectiveAmountElection getPartyElection(Threshold threshold, CounterpartyRoleEnum party) {
		return threshold.getPartyElection().stream()
				.filter(e -> party == e.getParty())
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No partyElection found for " + party));
	}

	private Mapping getEmptyMapping(Path xmlPath, String xmlValue) {
		return new Mapping(xmlPath, xmlValue, null, null, "no destination", false, false, false);
	}
}