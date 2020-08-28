package org.isda.cdm.functions;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.BusinessEvent;
import org.isda.cdm.ClearingInstruction;
import org.isda.cdm.Contract;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Create_ClearedTradeTest extends AbstractFunctionTest {

	@Inject
	private Create_ClearedTrade func;

	@Test
	void shouldCreateClearedTrade() throws IOException {
		Contract alphaContract = getRosettaModelObject(Contract.class,"result-json-files/products/rates/EUR-Vanilla-account.json");

		Party counterparty1 = getParty(alphaContract, CounterpartyEnum.PARTY_1);
		Party counterparty2 = getParty(alphaContract, CounterpartyEnum.PARTY_2);
		Party clearingParty = Party.builder()
				.setName(FieldWithMetaString.builder()
						.setValue("Clearing Party")
						.build())
				.addPartyId(FieldWithMetaString.builder()
						.setValue("CLEARING_PARTY_LEI")
						.setMeta(MetaFields.builder()
								.setScheme("http://www.fpml.org/coding-scheme/external/iso17442")
								.build())
						.build())
				.build();

		ClearingInstruction clearingInstruction = ClearingInstruction.builder()
				.setAlphaContract(alphaContract)
				.setParty1(counterparty1)
				.setParty2(counterparty2)
				.setClearingParty(clearingParty)
				.build();
		Date tradeDate = DateImpl.of(2020, 8, 28);

		BusinessEvent businessEvent = func.evaluate(clearingInstruction, tradeDate, null);

		String businessEventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);
		assertEquals(getJson("expected-cleared-trade-business-event.json"), businessEventJson);
	}

	private Party getParty(Contract alphaContract, CounterpartyEnum counterparty) {
		return alphaContract.getTradableProduct().getCounterparties().stream()
				.filter(c -> c.getCounterparty() == counterparty)
				.map(Counterparty::getParty)
				.map(ReferenceWithMetaParty::getGlobalReference)
				.flatMap(partyReference -> alphaContract.getParty().stream().filter(p -> partyReference.equals(p.getMeta().getGlobalKey())))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Party not found for counterparty " + counterparty));
	}

	private <T extends RosettaModelObject> T getRosettaModelObject(Class<T> clazz, String pathToJson) throws IOException {
		String json = getJson(pathToJson);
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
	}

	private String getJson(String pathToJson) throws IOException {
		URL url = Resources.getResource(pathToJson);
		return Resources.toString(url, Charset.defaultCharset());
	}
}
