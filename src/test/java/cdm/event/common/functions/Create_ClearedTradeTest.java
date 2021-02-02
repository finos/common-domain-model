package cdm.event.common.functions;

import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.event.common.BusinessEvent;
import cdm.event.common.ClearingInstruction;
import cdm.event.common.TradeState;
import com.google.common.io.Resources;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.workflows.ClearingUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;

class Create_ClearedTradeTest extends AbstractFunctionTest {

	@Inject
	private Create_ClearedTrade func;

	@Test
	void shouldCreateClearedTrade() throws IOException {
		TradeState alphaContract = getRosettaModelObject(TradeState.class,"result-json-files/products/rates/EUR-Vanilla-account.json");

		Party counterparty1 = ClearingUtils.getParty(alphaContract, CounterpartyRoleEnum.PARTY_1);
		Party counterparty2 = ClearingUtils.getParty(alphaContract, CounterpartyRoleEnum.PARTY_2);
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
		assertThat(getJson("expected-cleared-trade-business-event.json"), new IsEqualIgnoringWhiteSpace(businessEventJson));
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
