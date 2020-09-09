package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.metafields.FieldWithMetaString;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.product.template.TradableProduct;

class FpmlIrd8ImplTest extends AbstractFunctionTest {

	@Inject
	private FpmlIrd8 func;

	@Test
	void differentPartiesShouldEvaluateToValid() {
		Party party1 = getParty("Party A");
		Party party2 = getParty("Party B");
		TradableProduct tradableProduct = getTradableProduct(party1, party2);

		assertTrue(func.evaluate(tradableProduct, Collections.emptyList()));
	}

	@Test
	void samePartiesAndDifferentAccountsShouldEvaluateToValid() {
		Party party = getParty("Party A");
		TradableProduct tradableProduct = getTradableProduct(party, party);
		List<Account> accounts = getAccounts(party, "Account 1", party, "Account 2");

		assertTrue(func.evaluate(tradableProduct, accounts));
	}

	@Test
	void samePartiesAndNoAccountsShouldEvaluateToInvalid() {
		Party party = getParty("Party A");
		TradableProduct tradableProduct = getTradableProduct(party, party);

		assertFalse(func.evaluate(tradableProduct, Collections.emptyList()));
	}

	@Test
	void samePartiesAndSameAccountsShouldEvaluateToInvalid() {
		Party party = getParty("Party A");
		TradableProduct tradableProduct = getTradableProduct(party, party);
		List<Account> accounts = getAccounts(party, "Account 1", party, "Account 1");

		assertFalse(func.evaluate(tradableProduct, accounts));
	}

	private Party getParty(String name) {
		return generateGlobalKeys(Party.class, Party.builder().setName(FieldWithMetaString.builder().setValue(name).build()));
	}

	private TradableProduct getTradableProduct(Party party1, Party party2) {
		return TradableProduct.builder()
				.addCounterparties(getCounterparty(party1, CounterpartyEnum.PARTY_1))
				.addCounterparties(getCounterparty(party2, CounterpartyEnum.PARTY_2))
				.build();
	}

	private Counterparty getCounterparty(Party party, CounterpartyEnum counterparty) {
		return Counterparty.builder()
				.setCounterparty(counterparty)
				.setParty(getPartyReference(party))
				.build();
	}

	private ReferenceWithMetaParty getPartyReference(Party party) {
		return ReferenceWithMetaParty.builder()
				.setGlobalReference(party.getMeta().getGlobalKey())
				.build();
	}

	@NotNull
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