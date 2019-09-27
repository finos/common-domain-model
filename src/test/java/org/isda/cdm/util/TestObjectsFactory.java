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

	public List<Execution> getExecutions() {
		return Arrays.asList(
				getExecution(1, LocalDate.of(2019, 8, 26), CUSIP_US1234567891,
						150000000, 95.0975, 94.785, CURRENCY_USD, COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,
						LocalDate.of(2019, 8, 28), true),
				getExecution(2, LocalDate.of(2019, 8, 26), CUSIP_DH9105730505,
						250000000, 95.095, 94.78, CURRENCY_USD, COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,
						LocalDate.of(2019, 8, 28), true),
				getExecution(3, LocalDate.of(2019, 8, 27), CUSIP_US1234567891,
						10000000, 95.0575, 94.77, CURRENCY_USD, COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,
						LocalDate.of(2019, 8, 29), false),
				getExecution(4, LocalDate.of(2019, 8, 27), CUSIP_DH9105730505,
						125000000, 95.065, 94.73, CURRENCY_USD, COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,
						LocalDate.of(2019, 8, 29), true),
				getExecution(5, LocalDate.of(2019, 8, 28), CUSIP_US1234567891,
						2000000, 95.05, 94.79, CURRENCY_USD, COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,
						LocalDate.of(2019, 8, 30), false),
				getExecution(6, LocalDate.of(2019, 8, 28), CUSIP_DH9105730505,
						35000000, 95.025, 94.65, CURRENCY_USD, COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,
						LocalDate.of(2019, 8, 30), true),
				getExecution(7, LocalDate.of(2019, 8, 29), CUSIP_US1234567891,
						11000000, 95.0575, 94.63, CURRENCY_USD, COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,
						LocalDate.of(2019, 9, 02), true),
				getExecution(8, LocalDate.of(2019, 8, 29), CUSIP_DH9105730505,
						13500000, 95.0955, 94.685, CURRENCY_USD, COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,
						LocalDate.of(2019, 9, 02), false),
				getExecution(9, LocalDate.of(2019, 8, 30), CUSIP_US1234567891,
						80000000, 95.03, 94.355, CURRENCY_USD, COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME,
						LocalDate.of(2019, 9, 03), true),
				getExecution(10, LocalDate.of(2019, 8, 30), CUSIP_DH9105730505,
						7500000, 95.095, 94.555, CURRENCY_USD, COUNTERPARTY_BROKER_B_ID, COUNTERPARTY_BROKER_B_NAME,
						LocalDate.of(2019, 9, 03), false));
	}

	public Execution getExecution(int tradeId, LocalDate tradeDate, String cusip, long quantity, double dirtyPrice, double cleanPrice, String currency,
			String counterpartyBrokerId, String counterpartyName, LocalDate settlementDate, boolean isExecutingEntityBuy) {

		Execution.ExecutionBuilder builder = Execution.builder()
						.setExecutionType(ExecutionTypeEnum.ELECTRONIC)
						.setExecutionVenue(LegalEntity.builder().setName(FieldWithMetaString.builder().setValue("Tradeweb").build()).build())
						.addIdentifier(getIdentifier("tradeId" + tradeId, EXECUTING_BROKER_ID))
						.setTradeDate(FieldWithMetaDate.builder().setValue(DateImpl.of(tradeDate)).build())
						.setProduct(getProduct(cusip, ProductIdSourceEnum.CUSIP))
						.setQuantity(getQuantity(quantity))
						.setPrice(getPrice(dirtyPrice, cleanPrice, currency))
						.addParty(getReferenceWithMetaParty(CLIENT_A_ID, CLIENT_A_NAME, Optional.empty()))
						.addParty(getReferenceWithMetaParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME, Optional.empty()))
						.addParty(getReferenceWithMetaParty(counterpartyBrokerId, counterpartyName, Optional.empty()))
						.addPartyRole(getPartyRole(EXECUTING_BROKER_ID, isExecutingEntityBuy ? PartyRoleEnum.BUYER : PartyRoleEnum.SELLER))
						.addPartyRole(getPartyRole(counterpartyBrokerId, isExecutingEntityBuy ? PartyRoleEnum.SELLER : PartyRoleEnum.BUYER))
						.addPartyRole(getPartyRole(CLIENT_A_ID, PartyRoleEnum.CLIENT))
						.addPartyRole(getPartyRole(EXECUTING_BROKER_ID, PartyRoleEnum.EXECUTING_ENTITY))
						.addPartyRole(getPartyRole(counterpartyBrokerId, PartyRoleEnum.COUNTERPARTY))
						.setClosedState(getClosedState())
						.setSettlementTerms(getSettlementTerms(settlementDate, dirtyPrice * quantity, currency));

		// Generate global key/references etc
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Execution.class, builder));

		return builder.build();
	}

	private PartyRole getPartyRole(String partyReference, PartyRoleEnum partyRole) {
		return PartyRole.builder()
						.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyReference).build())
						.setRole(partyRole)
						.build();
	}

	private ReferenceWithMetaParty getReferenceWithMetaParty(String id, String partyId, Optional<Account> account) {
		return ReferenceWithMetaParty.builder()
				.setValue(getParty(id, partyId, account))
				.build();
	}

	private Party getParty(String id, String partyId, Optional<Account> account) {
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

		account.ifPresent(partyBuilder::setAccount);

		return partyBuilder.build();
	}

	private Account getAccount(String accountName, String accountNumber) {
		return Account.builder()
					  .setAccountName(FieldWithMetaString.builder().setValue(accountName).build())
					  .setAccountType(FieldWithMetaAccountTypeEnum.builder().setValue(AccountTypeEnum.CLIENT).build())
					  .setAccountNumber(FieldWithMetaString.builder().setValue(accountNumber).build())
					  .build();
	}

	private Identifier getIdentifier(String identifier, String issuer) {
		return Identifier.builder()
						 .addAssignedIdentifierBuilder(AssignedIdentifier.builder()
							 .setIdentifier(FieldWithMetaString.builder().setValue(identifier).build()))
						 .setIssuerReference(ReferenceWithMetaParty.builder().setExternalReference(issuer).build())
						 .build();
	}

	private Quantity getQuantity(long quantity) {
		return Quantity.builder()
					   .setAmount(BigDecimal.valueOf(quantity))
					   .build();
	}

	private Product getProduct(String productId, ProductIdSourceEnum productSource) {
		return Product.builder()
					  .setSecurityBuilder(Security.builder()
						  .setBondBuilder(Bond.builder()
						  .setProductIdentifierBuilder(ProductIdentifier.builder()
								.addIdentifier(FieldWithMetaString.builder().setValue(productId).build())
								.setSource(productSource))))
					  .build();
	}

	private Price getPrice(double dirtyPrice, double cleanPrice, String currency) {
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

	private SettlementTerms getSettlementTerms(LocalDate settlementDate, double settlementAmount, String settlementCurrency) {
		return SettlementTerms.builder()
				.setSettlementDateBuilder(AdjustableOrRelativeDate.builder()
					.setAdjustableDateBuilder(AdjustableDate.builder()
						.setAdjustedDate(FieldWithMetaDate.builder().setValue(DateImpl.of(settlementDate)).build())))
				.setSettlementAmountBuilder(Money.builder()
					.setAmount(BigDecimal.valueOf(settlementAmount))
					.setCurrency(FieldWithMetaString.builder().setValue(settlementCurrency).build()))
				.setSettlementCurrency(FieldWithMetaString.builder().setValue(settlementCurrency).build())
				.build();
	}

	private ClosedState getClosedState() {
		return ClosedState.builder().build();
	}

	public AllocationInstructions getAllocationInstructions(int quantity1, int quantity2, int quantity3) {
		AllocationInstructionsBuilder builder = AllocationInstructions.builder()
				.addBreakdowns(AllocationBreakdown.builder()
						.setPartyReference(ReferenceWithMetaParty.builder()
								.setValue(getParty(CLIENT_A_ACC_1_ID,
										CLIENT_A_ACC_1_NAME,
										Optional.of(getAccount("account1", "accountNumber1"))))
								.build())
						.setQuantity(Quantity.builder().setAmount(BigDecimal.valueOf(quantity1)).build())
						.build())
				.addBreakdowns(AllocationBreakdown.builder()
						.setPartyReference(ReferenceWithMetaParty.builder()
								.setValue(getParty(CLIENT_A_ACC_2_ID,
										CLIENT_A_ACC_2_NAME,
										Optional.of(getAccount("account2", "accountNumber2"))))
								.build())
						.setQuantity(Quantity.builder().setAmount(BigDecimal.valueOf(quantity2)).build())
						.build())
				.addBreakdowns(AllocationBreakdown.builder()
						.setPartyReference(ReferenceWithMetaParty.builder()
								.setValue(getParty(CLIENT_A_ACC_3_ID,
										CLIENT_A_ACC_3_NAME,
										Optional.of(getAccount("account3", "accountNumber3"))))
								.build())
						.setQuantity(Quantity.builder().setAmount(BigDecimal.valueOf(quantity3)).build())
						.build());

		// Generate global key/references etc
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Execution.class, builder));

		return builder.build();
	}
}
