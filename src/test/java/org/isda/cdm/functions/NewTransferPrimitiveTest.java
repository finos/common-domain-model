package org.isda.cdm.functions;

import static org.isda.cdm.util.TestObjectsFactory.CLIENT_A_ACC_1_ID;
import static org.isda.cdm.util.TestObjectsFactory.CLIENT_A_ACC_1_NAME;
import static org.isda.cdm.util.TestObjectsFactory.COUNTERPARTY_BROKER_A_ID;
import static org.isda.cdm.util.TestObjectsFactory.COUNTERPARTY_BROKER_A_NAME;
import static org.isda.cdm.util.TestObjectsFactory.CURRENCY_USD;
import static org.isda.cdm.util.TestObjectsFactory.CUSIP_US1234567891;
import static org.isda.cdm.util.TestObjectsFactory.EXECUTING_BROKER_ID;
import static org.isda.cdm.util.TestObjectsFactory.EXECUTING_BROKER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.isda.cdm.CashTransferComponent;
import org.isda.cdm.Execution;
import org.isda.cdm.Money;
import org.isda.cdm.Party;
import org.isda.cdm.PayerReceiver;
import org.isda.cdm.SecurityTransferComponent;
import org.isda.cdm.TransferPrimitive;
import org.isda.cdm.TransferSettlementEnum;
import org.isda.cdm.TransferStatusEnum;
import org.isda.cdm.TransferorTransferee;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.util.TestObjectsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

class NewTransferPrimitiveTest  extends AbstractFunctionTest {

	@Inject private NewTransferPrimitive func;

	private static final int QUANTITY = 1500000;
	private static final double DIRTY_PRICE = 95.0975;

	private Execution execution;
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
	}

	@Test
	void shouldBuildNewTransferPrimitive() {
		TransferPrimitive transferPrimitive = func.evaluate(execution);

		assertNotNull(transferPrimitive);

		assertEquals(TransferStatusEnum.INSTRUCTED, transferPrimitive.getStatus());
		assertEquals(settlementDate, transferPrimitive.getSettlementDate().getAdjustedDate().getValue().toLocalDate());
		assertEquals(TransferSettlementEnum.DELIVERY_VERSUS_PAYMENT, transferPrimitive.getSettlementType());

		// cash transfer

		List<CashTransferComponent> cashTransfer = transferPrimitive.getCashTransfer();
		assertTrue(cashTransfer != null && cashTransfer.size() == 1);
		CashTransferComponent cashTransferComponent = cashTransfer.get(0);
		assertNotNull(cashTransferComponent);

		// cash
		Money cashTransferAmount = cashTransferComponent.getAmount();
		assertEquals(BigDecimal.valueOf(QUANTITY).multiply(BigDecimal.valueOf(DIRTY_PRICE)).setScale(2), cashTransferAmount.getAmount().setScale(2));
		assertEquals(CURRENCY_USD, cashTransferAmount.getCurrency().getValue());

		// parties
		PayerReceiver payerReceiver = cashTransferComponent.getPayerReceiver();

		Party payerParty = payerReceiver.getPayerPartyReference().getValue();
		assertEquals(CLIENT_A_ACC_1_ID, payerParty.getMeta().getExternalKey());
		assertEquals(CLIENT_A_ACC_1_ID + "AccountName", payerParty.getAccount().getAccountName().getValue());

		Party receiverParty = payerReceiver.getReceiverPartyReference().getValue();
		assertEquals(COUNTERPARTY_BROKER_A_ID, receiverParty.getMeta().getExternalKey());
		assertEquals(COUNTERPARTY_BROKER_A_NAME+ "AccountName", receiverParty.getAccount().getAccountName().getValue());

		// security transfer

		List<SecurityTransferComponent> securityTransfer = transferPrimitive.getSecurityTransfer();
		assertTrue(securityTransfer != null && securityTransfer.size() == 1);
		SecurityTransferComponent securityTransferComponent = securityTransfer.get(0);
		assertNotNull(securityTransferComponent);

		// security
		assertEquals(BigDecimal.valueOf(QUANTITY), securityTransferComponent.getQuantity());
		List<FieldWithMetaString> productIds = securityTransferComponent.getSecurity().getBond().getProductIdentifier().getIdentifier();
		assertTrue(productIds != null && productIds.size() == 1);
		assertEquals(CUSIP_US1234567891, productIds.get(0).getValue());

		// parties
		TransferorTransferee transferorTransferee = securityTransferComponent.getTransferorTransferee();

		Party transfereeParty = transferorTransferee.getTransfereePartyReference().getValue();
		assertEquals(CLIENT_A_ACC_1_ID, transfereeParty.getMeta().getExternalKey());
		assertEquals(CLIENT_A_ACC_1_ID + "AccountName", transfereeParty.getAccount().getAccountName().getValue());

		Party transferor = transferorTransferee.getTransferorPartyReference().getValue();
		assertEquals(COUNTERPARTY_BROKER_A_ID, transferor.getMeta().getExternalKey());
		assertEquals(COUNTERPARTY_BROKER_A_NAME+ "AccountName", transferor.getAccount().getAccountName().getValue());
	}
}
