package cdm.event.common.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.staticdata.asset.common.ProductIdTypeEnum;
import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.party.PartyReferencePayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.observable.asset.PriceQuantity;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static util.ResourcesUtils.getInputObject;

public class Create_TransferTest extends AbstractFunctionTest {

	private static final String FUNC_INPUT_JSON = "cdm-sample-files/functions/security-lending-transfer-input.json";

	@Inject
	@SuppressWarnings("unused")
	private Create_Transfer createTransferFunc;

	@Test
	void shouldInvokeFunc() throws IOException {
		TradeState inputTradeState = getInputObject(FUNC_INPUT_JSON, "tradeState", TradeState.class);
		TransferInstruction instruction = getInputObject(FUNC_INPUT_JSON, "instruction", TransferInstruction.class);
		Date date = getInputObject(FUNC_INPUT_JSON, "date", Date.class);

		BusinessEvent transferBusinessEvent = createTransferFunc.evaluate(inputTradeState, instruction, date);
		assertNotNull(transferBusinessEvent);
		assertThat(transferBusinessEvent.getPrimitives(), hasSize(1));
		TransferPrimitive transferPrimitive = transferBusinessEvent.getPrimitives().get(0).getTransfer();
		assertNotNull(transferPrimitive);

		// The before and after trades remain unchanged
		TradeState beforeTradeState = transferPrimitive.getBefore().getValue().build();
		assertEquals(inputTradeState.getTrade(), beforeTradeState.getTrade());
		assertNull(beforeTradeState.getTransferHistory());

		TradeState afterTradeState = transferPrimitive.getAfter().build();
		assertEquals(inputTradeState.getTrade(), afterTradeState.getTrade());
		assertThat(afterTradeState.getTransferHistory(), hasSize(2));

		// Cash Transfer
		Transfer cashTransfer = afterTradeState.getTransferHistory().get(0);

		assertThat(cashTransfer.getPriceQuantity().getQuantity(), hasSize(1));
		Quantity cashQuantity = cashTransfer.getPriceQuantity().getQuantity().get(0).getValue();
		assertEquals(BigDecimal.valueOf(3350000.0), cashQuantity.getAmount());
		assertEquals("EUR", cashQuantity.getUnitOfAmount().getCurrency().getValue());

		assertEquals(LocalDate.of(2018, 10, 9), cashTransfer.getSettlementDate().getAdjustedDate().getValue().toLocalDate());

		PartyReferencePayerReceiver cashPayerReceiver = cashTransfer.getPayerReceiver();
		ReferenceWithMetaParty cashPayer = cashPayerReceiver.getPayerPartyReference();
		assertNotNull(cashPayer);
		assertEquals("Other Counterparty (Borrower)", cashPayer.getValue().getName().getValue());
		ReferenceWithMetaParty cashReceiver = cashPayerReceiver.getReceiverPartyReference();
		assertNotNull(cashReceiver);
		assertEquals("Lender (BO)", cashReceiver.getValue().getName().getValue());

		// Security Transfer
		Transfer securityTransfer = afterTradeState.getTransferHistory().get(1);

		PriceQuantity securityPriceQuantity = securityTransfer.getPriceQuantity();
		assertNotNull(securityPriceQuantity);

		assertThat(securityTransfer.getPriceQuantity().getQuantity(), hasSize(1));
		Quantity securityQuantity = securityPriceQuantity.getQuantity().get(0).getValue();
		assertEquals(BigDecimal.valueOf(50000), securityQuantity.getAmount());
		assertEquals(FinancialUnitEnum.SHARE, securityQuantity.getUnitOfAmount().getFinancialUnit());

		List<? extends ProductIdentifier> productIdentifiers = securityPriceQuantity.getObservable().getProductIdentifier();
		assertThat(productIdentifiers, hasSize(1));
		ProductIdentifier productIdentifier = productIdentifiers.get(0);
		assertEquals("DE0005557508", productIdentifier.getIdentifier().getValue());
		assertEquals(ProductIdTypeEnum.ISIN, productIdentifier.getSource());

		assertEquals(LocalDate.of(2018, 10, 9), securityTransfer.getSettlementDate().getAdjustedDate().getValue().toLocalDate());

		PartyReferencePayerReceiver securityPayerReceiver = securityTransfer.getPayerReceiver();
		ReferenceWithMetaParty securityPayer = securityPayerReceiver.getPayerPartyReference();
		assertNotNull(securityPayer);
		assertEquals("Lender (BO)", securityPayer.getValue().getName().getValue());
		ReferenceWithMetaParty securityReceiver = securityPayerReceiver.getReceiverPartyReference();
		assertNotNull(securityReceiver);
		assertEquals("Other Counterparty (Borrower)", securityReceiver.getValue().getName().getValue());
	}
}
