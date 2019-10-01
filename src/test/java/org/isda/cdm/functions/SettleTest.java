package org.isda.cdm.functions;

import com.google.common.collect.MoreCollectors;
import com.google.inject.Inject;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.util.TestObjectsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.isda.cdm.util.TestObjectsFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class SettleTest extends AbstractFunctionTest {

	@Inject private Settle func;

	private static final int QUANTITY = 1500000;
	private static final double DIRTY_PRICE = 95.0975;

	private Execution execution;
	private Event previousEvent;

	private LocalDate settlementDate = LocalDate.of(2019, 8, 28);

	@BeforeEach
	void setUpTests() {
		TestObjectsFactory factory = new TestObjectsFactory();
		LocalDate tradeDate = LocalDate.of(2019, 8, 26);
		execution = factory.getExecution(1, tradeDate, CUSIP_US1234567891,
				factory.getQuantity(QUANTITY),
				factory.getPrice(DIRTY_PRICE, 94.785, CURRENCY_USD),
				settlementDate, true,
				factory.getParty(CLIENT_A_ACC_1_ID, CLIENT_A_ACC_1_NAME, factory.getAccount(CLIENT_A_ACC_1_ID)),
				factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME, null),
				factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME, factory.getAccount(COUNTERPARTY_BROKER_A_NAME)));
		previousEvent = Event.builder().build();
	}

	@Test
	void shouldBuildNewSettleEvent() {
		Event settleEvent = func.evaluate(execution, previousEvent);

		assertNotNull(settleEvent);

		// event date
		assertEquals(LocalDate.now(), settleEvent.getEventDate().toLocalDate());

		// event creation dateTime
		List<EventTimestamp> eventTimestamps = settleEvent.getTimestamp();
		assertNotNull(eventTimestamps);
		assertEquals(1, eventTimestamps.size());
		EventTimestamp eventCreationTimestamp = eventTimestamps.get(0);
		assertEquals(LocalDate.now(), eventCreationTimestamp.getDateTime().toLocalDate());
		assertEquals(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME, eventCreationTimestamp.getQualification());

		// event identifier
		List<Identifier> eventIdentifiers = settleEvent.getEventIdentifier();
		assertNotNull(eventIdentifiers);
		assertEquals(1, eventIdentifiers.size());
		List<String> eventIdentifier = eventIdentifiers.get(0)
				.getAssignedIdentifier()
				.stream()
				.map(AssignedIdentifier::getIdentifier)
				.map(FieldWithMetaString::getValue)
				.collect(Collectors.toList());
		assertThat(eventIdentifier, hasItems("settleEvent1"));

		// event effects
		EventEffect eventEffect = settleEvent.getEventEffect();
		assertNotNull(eventEffect);
		assertEquals(1, new HashSet<>(eventEffect.getProductIdentifier()).size());
		assertEquals(1, new HashSet<>(eventEffect.getTransfer()).size());

		// event parties
		List<Party> parties = settleEvent.getParty();

		List<String> partyGlobalKeys = getPartyGlobalKeys(parties);
		assertEquals(2, partyGlobalKeys.size());

		List<String> partyExternalKeys = getPartyExternalKeys(parties);
		assertThat(partyExternalKeys, hasItems(COUNTERPARTY_BROKER_A_ID, CLIENT_A_ACC_1_ID));

		List<TransferPrimitive> transferPrimitives = Optional.ofNullable(settleEvent.getPrimitive())
				.map(PrimitiveEvent::getTransfer)
				.orElse(Collections.emptyList());
		assertEquals(1, transferPrimitives.size());
		TransferPrimitive transferPrimitive = transferPrimitives.get(0);

		// cash transfer

		assertTrue(transferPrimitive.getCashTransfer() != null && transferPrimitive.getCashTransfer().size() == 1);
		CashTransferComponent cashTransferComponent = transferPrimitive.getCashTransfer().get(0);

		Money cashTransferMoney = cashTransferComponent.getAmount();
		assertEquals(BigDecimal.valueOf(DIRTY_PRICE).multiply(BigDecimal.valueOf(QUANTITY)).setScale(2), cashTransferMoney.getAmount().setScale(2));
		assertEquals(CURRENCY_USD, cashTransferMoney.getCurrency().getValue());

		PayerReceiver payerReceiver = cashTransferComponent.getPayerReceiver();
		assertEquals(getPartyGlobalKey(parties, CLIENT_A_ACC_1_ID), payerReceiver.getPayerPartyReference().getGlobalReference());
		assertEquals(getPartyGlobalKey(parties, CLIENT_A_ACC_1_ID), payerReceiver.getPayerAccountReference().getGlobalReference());
		assertEquals(getPartyGlobalKey(parties, COUNTERPARTY_BROKER_A_ID), payerReceiver.getReceiverPartyReference().getGlobalReference());
		assertEquals(getPartyGlobalKey(parties, COUNTERPARTY_BROKER_A_ID), payerReceiver.getReceiverAccountReference().getGlobalReference());

		// security transfer

		assertTrue(transferPrimitive.getSecurityTransfer() != null && transferPrimitive.getSecurityTransfer().size() == 1);
		SecurityTransferComponent securityTransferComponent = transferPrimitive.getSecurityTransfer().get(0);

		assertEquals(BigDecimal.valueOf(QUANTITY), securityTransferComponent.getQuantity());
		List<FieldWithMetaString> productIds = securityTransferComponent.getSecurity().getBond().getProductIdentifier().getIdentifier();
		assertTrue(productIds != null && productIds.size() == 1);
		assertEquals(CUSIP_US1234567891, productIds.get(0).getValue());

		TransferorTransferee transferorTransferee = securityTransferComponent.getTransferorTransferee();
		assertEquals(getPartyGlobalKey(parties, CLIENT_A_ACC_1_ID), transferorTransferee.getTransfereePartyReference().getGlobalReference());
		assertEquals(getPartyGlobalKey(parties, CLIENT_A_ACC_1_ID), transferorTransferee.getTransfereeAccountReference().getGlobalReference());
		assertEquals(getPartyGlobalKey(parties, COUNTERPARTY_BROKER_A_ID), transferorTransferee.getTransferorPartyReference().getGlobalReference());
		assertEquals(getPartyGlobalKey(parties, COUNTERPARTY_BROKER_A_ID), transferorTransferee.getTransferorAccountReference().getGlobalReference());
	}

	private String getPartyGlobalKey(List<Party> party, String partyExternalKey) {
		return party
				.stream()
				.map(Party::getMeta)
				.filter(m -> m.getExternalKey().equals(partyExternalKey))
				.map(MetaFields::getGlobalKey)
				.collect(MoreCollectors.onlyElement());
	}

	private List<String> getPartyGlobalKeys(List<Party> party) {
		return party
				.stream()
				.map(Party::getMeta)
				.map(MetaFields::getGlobalKey)
				.collect(Collectors.toList());
	}

	private List<String> getPartyExternalKeys(List<Party> party) {
		return party
				.stream()
				.map(Party::getMeta)
				.map(MetaFields::getExternalKey)
				.collect(Collectors.toList());
	}
}
