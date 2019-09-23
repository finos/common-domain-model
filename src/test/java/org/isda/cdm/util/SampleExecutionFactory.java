package org.isda.cdm.util;

import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaDate;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Class to generate sample data for unit tests
 */
public class SampleExecutionFactory {

	public static final String CUSIP_US1234567891 = "US1234567891";
	public static final String CUSIP_DH9105730505 = "DH9105730505";
	public static final String CURRENCY_USD = "USD";
	public static final String EXECUTING_ENTITY_1 = "executingEntity1";
	public static final String CLIENT_A_ID = "p2";
	public static final String CLIENT_A_NAME = "counterpartyA";
	public static final String CLIENT_B_ID = "p3";
	public static final String CLIENT_B_NAME = "counterpartyB";

	private final List<PostProcessStep> postProcessors;

	public SampleExecutionFactory() {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(() -> new NonNullHashCollector());
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(() -> new RosettaKeyValueHashFunction()),
				new ReKeyProcessStep(rosettaKeyProcessStep));
	}

	public List<Execution> getExecutions() {
		return Arrays.asList(
				getExecution(1, LocalDate.of(2019, 8, 26), CUSIP_US1234567891,
						150000000, 95.0975, 94.785, CURRENCY_USD, CLIENT_A_ID, CLIENT_A_NAME,
						LocalDate.of(2019, 8, 28), true),
				getExecution(2, LocalDate.of(2019, 8, 26), CUSIP_DH9105730505,
						250000000, 95.095, 94.78, CURRENCY_USD, CLIENT_B_ID, CLIENT_B_NAME,
						LocalDate.of(2019, 8, 28), true),
				getExecution(3, LocalDate.of(2019, 8, 27), CUSIP_US1234567891,
						10000000, 95.0575, 94.77, CURRENCY_USD, CLIENT_A_ID, CLIENT_A_NAME,
						LocalDate.of(2019, 8, 29), false),
				getExecution(4, LocalDate.of(2019, 8, 27), CUSIP_DH9105730505,
						125000000, 95.065, 94.73, CURRENCY_USD, CLIENT_A_ID, CLIENT_A_NAME,
						LocalDate.of(2019, 8, 29), true),
				getExecution(5, LocalDate.of(2019, 8, 28), CUSIP_US1234567891,
						2000000, 95.05, 94.79, CURRENCY_USD, CLIENT_B_ID, CLIENT_B_NAME,
						LocalDate.of(2019, 8, 30), false),
				getExecution(6, LocalDate.of(2019, 8, 28), CUSIP_DH9105730505,
						35000000, 95.025, 94.65, CURRENCY_USD, CLIENT_B_ID, CLIENT_B_NAME,
						LocalDate.of(2019, 8, 30), true),
				getExecution(7, LocalDate.of(2019, 8, 29), CUSIP_US1234567891,
						11000000, 95.0575, 94.63, CURRENCY_USD, CLIENT_B_ID, CLIENT_B_NAME,
						LocalDate.of(2019, 9, 02), true),
				getExecution(8, LocalDate.of(2019, 8, 29), CUSIP_DH9105730505,
						13500000, 95.0955, 94.685, CURRENCY_USD, CLIENT_A_ID, CLIENT_A_NAME,
						LocalDate.of(2019, 9, 02), false),
				getExecution(9, LocalDate.of(2019, 8, 30), CUSIP_US1234567891,
						80000000, 95.03, 94.355, CURRENCY_USD, CLIENT_A_ID, CLIENT_A_NAME,
						LocalDate.of(2019, 9, 03), true),
				getExecution(10, LocalDate.of(2019, 8, 30), CUSIP_DH9105730505,
						7500000, 95.095, 94.555, CURRENCY_USD, CLIENT_B_ID, CLIENT_B_NAME,
						LocalDate.of(2019, 9, 03), false));
	}

	private Execution getExecution(int tradeId, LocalDate tradeDate,
			String cusip, long quantity, double dirtyPrice, double cleanPrice, String currency,
			String counterpartyId, String counterpartyPartyId, LocalDate settlementDate, boolean isExecutingEntityBuy) {

		Execution.ExecutionBuilder builder = Execution.builder()
						.setExecutionType(ExecutionTypeEnum.ELECTRONIC)
						.setExecutionVenue(LegalEntity.builder().setName(FieldWithMetaString.builder().setValue("Tradeweb").build()).build())
						.addIdentifier(getIdentifier("tradeId" + tradeId, "p1"))
						.setTradeDate(FieldWithMetaDate.builder().setValue(DateImpl.of(tradeDate)).build())
						.setProduct(getProduct(cusip, ProductIdSourceEnum.CUSIP))
						.setQuantity(getQuantity(quantity))
						.setPrice(getPrice(dirtyPrice, cleanPrice, currency))
						.addParty(getParty("p1", EXECUTING_ENTITY_1))
						.addParty(getParty(counterpartyId, counterpartyPartyId))
						.addPartyRole(getPartyRole("p1", isExecutingEntityBuy ? PartyRoleEnum.BUYER : PartyRoleEnum.SELLER))
						.addPartyRole(getPartyRole(counterpartyId, isExecutingEntityBuy ? PartyRoleEnum.SELLER : PartyRoleEnum.BUYER))
						.addPartyRole(getPartyRole("p1", PartyRoleEnum.EXECUTING_ENTITY))
						.addPartyRole(getPartyRole(counterpartyId, PartyRoleEnum.COUNTERPARTY))
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

	private Party getParty(String id, String partyId) {
		return Party.builder()
					.setMeta(MetaFields.builder()
									   .setExternalKey(id)
									   .build())
					.addPartyId(FieldWithMetaString.builder()
												   .setValue(partyId)
												   .setMeta(MetaFields.builder()
																	  .setScheme("http://www.fpml.org/coding-scheme/external")
																	  .build())
												   .build())
					//.setAccount(Account.builder().setAccountNumber(FieldWithMetaString.builder().setValue(account).build()).build())
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
				.build();
	}

	private ClosedState getClosedState() {
		return ClosedState.builder().build();
	}

}
