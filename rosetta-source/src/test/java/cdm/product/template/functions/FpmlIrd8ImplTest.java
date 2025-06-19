package cdm.product.template.functions;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import javax.inject.Inject;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FpmlIrd8ImplTest extends AbstractFunctionTest {

	@Inject
	private FpmlIrd8 func;

	@Test
	void differentPartiesShouldEvaluateToValid() {
		Party party1 = getParty("Party A");
		Party party2 = getParty("Party B");
		Trade trade = getTradableProduct(party1, party2);

		assertTrue(func.evaluate(trade, Collections.emptyList()));
	}

	@Test
	void samePartiesAndDifferentAccountsShouldEvaluateToValid() {
		Party party = getParty("Party A");
		Trade trade = getTradableProduct(party, party);
		List<Account> accounts = getAccounts(party, "Account 1", party, "Account 2");

		assertTrue(func.evaluate(trade, accounts));
	}

	@Test
	void samePartiesAndNoAccountsShouldEvaluateToInvalid() {
		Party party = getParty("Party A");
		Trade trade = getTradableProduct(party, party);

		assertFalse(func.evaluate(trade, Collections.emptyList()));
	}

	@Test
	void samePartiesAndSameAccountsShouldEvaluateToInvalid() {
		Party party = getParty("Party A");
		Trade trade = getTradableProduct(party, party);
		List<Account> accounts = getAccounts(party, "Account 1", party, "Account 1");

		assertFalse(func.evaluate(trade, accounts));
	}

	private Party getParty(String name) {
		return generateGlobalKeys(Party.class, Party.builder().setName(FieldWithMetaString.builder().setValue(name).build()));
	}

	private Trade getTradableProduct(Party party1, Party party2) {
		return Trade.builder()
				.addCounterparty(getCounterparty(party1, CounterpartyRoleEnum.PARTY_1))
				.addCounterparty(getCounterparty(party2, CounterpartyRoleEnum.PARTY_2))
				.build();
	}

	private Counterparty getCounterparty(Party party, CounterpartyRoleEnum counterparty) {
		return Counterparty.builder()
				.setRole(counterparty)
				.setPartyReference(getPartyReference(party))
				.build();
	}

	private ReferenceWithMetaParty getPartyReference(Party party) {
		return ReferenceWithMetaParty.builder()
				.setGlobalReference(party.getMeta().getGlobalKey())
				.build();
	}

	private List<Account> getAccounts(Party party1, String account1, Party party2, String account2) {
		return Arrays.asList(getAccount(party1, account1), getAccount(party2, account2));
	}

	private Account getAccount(Party party, String name) {
		return Account.builder()
				.setAccountName(FieldWithMetaString.builder().setValue(name).build())
				.setPartyReference(getPartyReference(party))
				.build();
	}

	private <T extends RosettaModelObject> T generateGlobalKeys(Class<T> clazz, RosettaModelObjectBuilder builder) {
		new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(clazz, builder);
		return (T) builder.build();
	}
}