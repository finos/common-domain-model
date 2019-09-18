package org.isda.cdm.functions;

import com.regnosys.rosetta.common.hashing.*;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaDate;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Temp class to generate Execution sample data - to be deleted
 */
public class ExecutionFactory {

	private static final String EXECUTING_ENTITY_1 = "executingEntity1";
	private static final String CLIENT_A_ID = "p2";
	private static final String CLIENT_A_NAME = "counterpartyA";
	private static final String CLIENT_B_ID = "p3";
	private static final String CLIENT_B_NAME = "counterpartyB";

	@Test
	@Disabled
	void generateExecutions() throws IOException {
		processAndSerialize(1, getExecution(1, LocalDate.of(2019, 8, 26), "US1234567891",
				50000000, 95.0975, 94.785, "USD", CLIENT_A_ID, CLIENT_A_NAME,
				LocalDate.of(2019, 8, 28), true));
		processAndSerialize(2, getExecution(1, LocalDate.of(2019, 8, 26), "DH9105730505",
				5000000, 95.095, 94.78, "USD", CLIENT_B_ID, CLIENT_B_NAME,
				LocalDate.of(2019, 8, 28), true));
		processAndSerialize(3, getExecution(1, LocalDate.of(2019, 8, 27), "US1234567891",
				100000000, 95.0575, 94.77, "USD", CLIENT_A_ID, CLIENT_A_NAME,
				LocalDate.of(2019, 8, 29), true));
		processAndSerialize(4, getExecution(1, LocalDate.of(2019, 8, 27), "DH9105730505",
				125000000, 95.065, 94.73, "USD", CLIENT_A_ID, CLIENT_A_NAME,
				LocalDate.of(2019, 8, 29), true));
		processAndSerialize(5, getExecution(1, LocalDate.of(2019, 8, 28), "US1234567891",
				2000000, 95.05, 94.79, "USD", CLIENT_B_ID, CLIENT_B_NAME,
				LocalDate.of(2019, 8, 30), true));
		processAndSerialize(6, getExecution(1, LocalDate.of(2019, 8, 28), "DH9105730505",
				35000000, 95.025, 94.65, "USD", CLIENT_B_ID, CLIENT_B_NAME,
				LocalDate.of(2019, 8, 30), true));
		processAndSerialize(7, getExecution(1, LocalDate.of(2019, 8, 29), "US1234567891",
				11000000, 95.0575, 94.63, "USD", CLIENT_B_ID, CLIENT_B_NAME,
				LocalDate.of(2019, 9, 02), true));
		processAndSerialize(8, getExecution(1, LocalDate.of(2019, 8, 29), "DH9105730505",
				135000000, 95.0955, 94.685, "USD", CLIENT_A_ID, CLIENT_A_NAME,
				LocalDate.of(2019, 9, 02), true));
		processAndSerialize(9, getExecution(1, LocalDate.of(2019, 8, 30), "US1234567891",
				80000000, 95.03, 94.355, "USD", CLIENT_A_ID, CLIENT_A_NAME,
				LocalDate.of(2019, 9, 03), true));
		processAndSerialize(10, getExecution(1, LocalDate.of(2019, 8, 30), "DH9105730505",
				75000000, 95.095, 94.555, "USD", CLIENT_B_ID, CLIENT_B_NAME,
				LocalDate.of(2019, 9, 03), true));
		return;
	}

	private void processAndSerialize(int id, Execution.ExecutionBuilder executionBuilder) throws IOException {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(() -> new NonNullHashCollector());
		List<PostProcessStep> postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(() -> new RosettaKeyValueHashFunction()),
				new ReKeyProcessStep(rosettaKeyProcessStep));

		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(Execution.class, executionBuilder));

		new RosettaTypeValidator().runProcessStep(Execution.class, executionBuilder).getValidationResults().stream()
								  .filter(r -> !r.isSuccess())
								  .forEach(System.out::println);

		Execution execution = executionBuilder.build();

		byte[] json = RosettaObjectMapper.getDefaultRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(execution);
		Files.write(Paths.get(String.format("src/main/resources/cdm-sample-files/functions/position/execution-%s.json", id)), json);
	}

	public Execution.ExecutionBuilder getExecution(int tradeId, LocalDate tradeDate,
			String cusip, int quantity, double dirtyPrice, double cleanPrice, String currency,
			String counterpartyId, String counterpartyPartyId, LocalDate settlementDate, boolean isExecutingEntityBuy) {
		return Execution.builder()
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

	private Quantity getQuantity(int quantity) {
		return Quantity.builder()
					   .setAmount(BigDecimal.valueOf(quantity))
					   .build();
	}

	private Product getProduct(String productId, ProductIdSourceEnum productSource) {
		return Product.builder()
					  .setSecurityBuilder(Security.builder()
												  .setBondBuilder(Bond.builder()
																	  .setProductIdentifierBuilder(ProductIdentifier.builder()
																													.addIdentifier(FieldWithMetaString.builder()
																																					  .setValue(
																																							  productId)
																																					  .build())
																													.setSource(productSource))))
					  .build();
	}

	private Price getPrice(double dirtyPrice, double cleanPrice, String currency) {
		return Price.builder()
					.setNetPrice(getActualPrice(dirtyPrice, currency))
					.setCleanNetPrice(getActualPrice(cleanPrice, "USD"))
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
							  .setSettlementDate(AdjustableOrRelativeDate.builder()
																		 .setAdjustableDate(AdjustableDate.builder()
																										  .setAdjustedDate(
																												  FieldWithMetaDate.builder()
																																   .setValue(DateImpl.of(
																																		   settlementDate))
																																   .build()).build())
																		 .build())
							  .setSettlementAmount(Money.builder()
														.setAmount(BigDecimal.valueOf(settlementAmount))
														.setCurrency(FieldWithMetaString.builder().setValue(settlementCurrency).build()).build())
							  .build();
	}

	private ClosedState getClosedState() {
		return ClosedState.builder().build();
	}

}
