package org.isda.cdm.util;

import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.AllocationInstructions.AllocationInstructionsBuilder;
import org.isda.cdm.metafields.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class to generate sample data for unit tests
 */
public class TestObjectsFactory {

	public static final String CUSIP_US1234567891 = "US1234567891";
	public static final String CUSIP_DH9105730505 = "DH9105730505";
	public static final String CURRENCY_USD = "USD";
	public static final String CLIENT_A_ID = "c1";
	public static final String CLIENT_A_NAME = "clientA";
	public static final String CLIENT_A_ACC_1_ID = "c1a1";
	public static final String CLIENT_A_ACC_1_NAME = "clientA_account1";
	public static final String CLIENT_A_ACC_2_ID = "c1a2";
	public static final String CLIENT_A_ACC_2_NAME = "clientA_account2";
	public static final String CLIENT_A_ACC_3_ID = "c1a3";
	public static final String CLIENT_A_ACC_3_NAME = "clientA_account3";
	public static final String EXECUTING_BROKER_ID = "b1";
	public static final String EXECUTING_BROKER_NAME = "executingBroker";
	public static final String COUNTERPARTY_BROKER_A_ID = "b2";
	public static final String COUNTERPARTY_BROKER_A_NAME = "counterpartyBrokerA";
	public static final String COUNTERPARTY_BROKER_B_ID = "b3";
	public static final String COUNTERPARTY_BROKER_B_NAME = "counterpartyBrokerB";

	private final List<PostProcessStep> postProcessors;

	public TestObjectsFactory() {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(() -> new NonNullHashCollector());
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(() -> new RosettaKeyValueHashFunction()),
				new ReKeyProcessStep(rosettaKeyProcessStep));
	}

	public Execution getExecution(int tradeId, LocalDate tradeDate, String cusip, Quantity quantity, Price price, LocalDate settlementDate,
			boolean isExecutingEntityBuy, Party clientParty, Party executingBrokerParty, Party counterpartyBrokerParty) {

		String clientExternalKey = clientParty.getMeta().getExternalKey();
		String executingBrokerExternalKey = executingBrokerParty.getMeta().getExternalKey();
		String counterpartyBrokerExternalKey = counterpartyBrokerParty.getMeta().getExternalKey();

		Execution.ExecutionBuilder builder = Execution.builder()
				.setExecutionType(ExecutionTypeEnum.ELECTRONIC)
				.setExecutionVenue(LegalEntity.builder().setName(FieldWithMetaString.builder().setValue("Tradeweb").build()).build())
				.addIdentifier(getIdentifier("tradeId" + tradeId, executingBrokerExternalKey))
				.setTradeDate(FieldWithMetaDate.builder().setValue(DateImpl.of(tradeDate)).build())
				.setProduct(getProduct(cusip))
				.setQuantity(quantity)
				.setPrice(price)
				.addParty(getReferenceWithMetaParty(clientParty))
				.addParty(getReferenceWithMetaParty(executingBrokerParty))
				.addParty(getReferenceWithMetaParty(counterpartyBrokerParty))
				.addPartyRole(getPartyRole(executingBrokerExternalKey, isExecutingEntityBuy ? PartyRoleEnum.BUYER : PartyRoleEnum.SELLER))
				.addPartyRole(getPartyRole(counterpartyBrokerExternalKey, isExecutingEntityBuy ? PartyRoleEnum.SELLER : PartyRoleEnum.BUYER))
				.addPartyRole(getPartyRole(clientExternalKey, PartyRoleEnum.CLIENT))
				.addPartyRole(getPartyRole(executingBrokerExternalKey, PartyRoleEnum.EXECUTING_ENTITY))
				.addPartyRole(getPartyRole(counterpartyBrokerExternalKey, PartyRoleEnum.COUNTERPARTY))
				.setClosedState(getClosedState())
				.setSettlementTerms(getSettlementTerms(settlementDate, price, quantity));

		// Generate global key/references etc
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Execution.class, builder));

		return builder.build();
	}

	private PartyRole getPartyRole(String partyExternalReference, PartyRoleEnum partyRole) {
		return PartyRole.builder()
				.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyExternalReference).build())
				.setRole(partyRole)
				.build();
	}

	private ReferenceWithMetaParty getReferenceWithMetaParty(Party party) {
		return ReferenceWithMetaParty.builder()
				.setValue(party)
				.build();
	}

	public Party getParty(String id, String partyId, Account account) {
		Party.PartyBuilder partyBuilder = Party.builder()
				.setMeta(MetaFields.builder()
						.setExternalKey(id)
						.build())
				.addPartyId(FieldWithMetaString.builder()
						.setValue(partyId)
						.setMeta(MetaFields.builder()
								.setScheme("http://www.fpml.org/coding-scheme/external")
								.build())
						.build());

		Optional.ofNullable(account).ifPresent(partyBuilder::setAccount);

		return partyBuilder.build();
	}

	public Account getAccount(String partyName) {
		return Account.builder()
				.setAccountName(FieldWithMetaString.builder().setValue(partyName + "AccountName").build())
				.setAccountType(FieldWithMetaAccountTypeEnum.builder().setValue(AccountTypeEnum.CLIENT).build())
				.setAccountNumber(FieldWithMetaString.builder().setValue(partyName + "AccountNumber").build())
				.build();
	}

	private Identifier getIdentifier(String identifier, String issuer) {
		return Identifier.builder()
				.addAssignedIdentifierBuilder(AssignedIdentifier.builder()
						.setIdentifier(FieldWithMetaString.builder().setValue(identifier).build()))
				.setIssuerReference(ReferenceWithMetaParty.builder().setExternalReference(issuer).build())
				.build();
	}

	public Quantity getQuantity(long quantity) {
		return Quantity.builder()
				.setAmount(BigDecimal.valueOf(quantity))
				.build();
	}

	private Product getProduct(String productId) {
		return Product.builder()
				.setSecurityBuilder(Security.builder()
						.setBondBuilder(Bond.builder()
								.setProductIdentifierBuilder(ProductIdentifier.builder()
										.addIdentifier(FieldWithMetaString.builder().setValue(productId).build())
										.setSource(ProductIdSourceEnum.CUSIP))))
				.build();
	}

	public Price getPrice(double dirtyPrice, double cleanPrice, String currency) {
		return Price.builder()
				.setNetPrice(getActualPrice(dirtyPrice, currency))
				.setCleanNetPrice(getActualPrice(cleanPrice, currency))
				.setAccruedInterest(BigDecimal.valueOf(dirtyPrice - cleanPrice))
				.build();
	}

	private ActualPrice getActualPrice(double amount, String currency) {
		return ActualPrice.builder()
				.setAmount(BigDecimal.valueOf(amount))
				.setCurrency(FieldWithMetaString.builder().setValue(currency).build())
				.setPriceExpression(PriceExpressionEnum.ABSOLUTE_TERMS)
				.build();
	}

	private SettlementTerms getSettlementTerms(LocalDate settlementDate, Price price, Quantity quantity) {
		ActualPrice dirtyPrice = price.getNetPrice();
		BigDecimal dirtyPriceAmount = dirtyPrice.getAmount();
		BigDecimal settlementAmount = dirtyPriceAmount.multiply(quantity.getAmount());
		String settlementCurrency = dirtyPrice.getCurrency().getValue();

		return SettlementTerms.builder()
				.setSettlementDateBuilder(AdjustableOrRelativeDate.builder()
						.setAdjustableDateBuilder(AdjustableDate.builder()
								.setAdjustedDate(FieldWithMetaDate.builder().setValue(DateImpl.of(settlementDate)).build())))
				.setSettlementAmountBuilder(Money.builder()
						.setAmount(settlementAmount)
						.setCurrency(FieldWithMetaString.builder().setValue(settlementCurrency).build()))
				.setSettlementCurrency(FieldWithMetaString.builder().setValue(settlementCurrency).build())
				.setTransferSettlementType(TransferSettlementEnum.DELIVERY_VERSUS_PAYMENT)
				.build();
	}

	private ClosedState getClosedState() {
		return ClosedState.builder().build();
	}

	public AllocationInstructions getAllocationInstructions(int quantity1, Party party1, int quantity2, Party party2, int quantity3, Party party3) {
		AllocationInstructionsBuilder builder = AllocationInstructions.builder()
				.addBreakdowns(AllocationBreakdown.builder()
						.setPartyReference(getReferenceWithMetaParty(party1))
						.setQuantity(Quantity.builder().setAmount(BigDecimal.valueOf(quantity1)).build())
						.build())
				.addBreakdowns(AllocationBreakdown.builder()
						.setPartyReference(getReferenceWithMetaParty(party2))
						.setQuantity(Quantity.builder().setAmount(BigDecimal.valueOf(quantity2)).build())
						.build())
				.addBreakdowns(AllocationBreakdown.builder()
						.setPartyReference(getReferenceWithMetaParty(party3))
						.setQuantity(Quantity.builder().setAmount(BigDecimal.valueOf(quantity3)).build())
						.build());

		// Generate global key/references etc
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Execution.class, builder));

		return builder.build();
	}
}
