package cdm.legaldocumentation.csa.processor;

import cdm.base.staticdata.asset.common.ISOCurrencyCodeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.legaldocumentation.csa.CollateralTransferAgreementElections;

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
import static org.mockito.Mockito.mock;

class MinimumTransferAmountAmendmentMappingProcessorTest {

	@Test
	void shouldMapMtaa() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.csdInitialMargin2018EnglishLaw.minimumTransferAmountAmendment");
		List<Mapping> mappings = new ArrayList<>();
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.amendment_to_minimum_transfer_amount.partyA_amendment_to_minimum_transfer_amount"), "specify"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.amendment_to_minimum_transfer_amount.partyA_amount"), "1000"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.amendment_to_minimum_transfer_amount.partyA_currency"), "Japanese Yen"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.amendment_to_minimum_transfer_amount.partyB_amendment_to_minimum_transfer_amount"), "other"));
		mappings.add(getEmptyMapping(Path.parse("answers.partyA.amendment_to_minimum_transfer_amount.partyB_specify"), "foo"));
		Map<Class<?>, Map<String, Enum<?>>> synonymToEnumMap = ImmutableMap.<Class<?>, Map<String, Enum<?>>>builder()
				.put(ISOCurrencyCodeEnum.class, ImmutableMap.<String, Enum<?>>builder()
						.put("Japanese Yen", ISOCurrencyCodeEnum.JPY)
						.build())
				.build();
		MappingContext context = new MappingContext(mappings, Collections.emptyMap(), synonymToEnumMap);

		MinimumTransferAmountAmendment.MinimumTransferAmountAmendmentBuilder builder = MinimumTransferAmountAmendment.builder();
		CollateralTransferAgreementElections.CollateralTransferAgreementElectionsBuilder parent =
				mock(CollateralTransferAgreementElections.CollateralTransferAgreementElectionsBuilder.class);

		// test
		Path synonymPath = Path.parse("answers.partyA.amendment_to_minimum_transfer_amount");
		MinimumTransferAmountAmendmentMappingProcessor processor =
				new MinimumTransferAmountAmendmentMappingProcessor(rosettaPath,
						Collections.singletonList(synonymPath),
						context);
		processor.map(synonymPath, builder, parent);
		MinimumTransferAmountAmendment minimumTransferAmountAmendment = builder.build();

		// assert

		ElectiveAmountElection partyA = getPartyElection(minimumTransferAmountAmendment, CounterpartyRoleEnum.PARTY_1);
		assertNull(partyA.getCustomElection());
		Money partyAAmount = partyA.getAmount();
		assertEquals(1000, partyAAmount.getValue().intValue());
		assertEquals("JPY", partyAAmount.getUnit().getCurrency().getValue());

		ElectiveAmountElection partyB = getPartyElection(minimumTransferAmountAmendment, CounterpartyRoleEnum.PARTY_2);
		assertEquals("foo", partyB.getCustomElection());
		assertNull(partyB.getAmount());
		assertNull(partyB.getElectiveAmount());
	}

	private ElectiveAmountElection getPartyElection(MinimumTransferAmountAmendment minimumTransferAmountAmendment, CounterpartyRoleEnum party) {
		return minimumTransferAmountAmendment.getPartyElections().stream()
				.filter(e -> party == e.getParty())
				.findFirst()
				.orElseThrow(() -> new RuntimeException("No partyElection found for " + party));
	}

	private Mapping getEmptyMapping(Path xmlPath, String xmlValue) {
		return new Mapping(xmlPath, xmlValue, null, null, "no destination", false, false, false);
	}
}