package org.isda.cdm.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaDate;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExecutionFactory {

	@Test
	void generateExecutions() throws JsonProcessingException {
		Execution execution = getExecution();
		String json = RosettaObjectMapper.getDefaultRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(execution);
		System.out.println(json);
	}

	public Execution getExecution() {
		return Execution.builder()
						.setExecutionType(ExecutionTypeEnum.ELECTRONIC)
						.addIdentifier(getIdentifier("O2NVGQ0UBNSRA"))
						.setProduct(getProduct("US1234567891", ProductIdSourceEnum.CUSIP))
						.setQuantity(getQuantity(50000000))
						.setPrice(getPrice(95.0975, 94.785, 0.3125, "USD"))
						.addParty(getParty("p1", "BUYSideLEI", "CL1_174935213"))
						.addParty(getParty("p2", "ClientBrokerLEI", "CL1_174935213"))
						.addPartyRole(getPartyRole("p1", PartyRoleEnum.BUYER))
						.addPartyRole(getPartyRole("p2", PartyRoleEnum.SELLER))
						.addPartyRole(getPartyRole("p1", PartyRoleEnum.EXECUTING_ENTITY))
						.addPartyRole(getPartyRole("p2", PartyRoleEnum.COUNTERPARTY))
						.setClosedState(getClosedState())
						.setSettlementTerms(getSettlementTerms(LocalDate.of(2019, 8, 01), 47548750.00, "USD"))
						.build();
	}

	private PartyRole getPartyRole(String partyReference, PartyRoleEnum partyRole) {
		return PartyRole.builder()
						.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyReference).build())
						.setRole(partyRole)
						.build();
	}

	private Party getParty(String id, String partyId, String account) {
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
					.setAccount(Account.builder().setAccountNumber(FieldWithMetaString.builder().setValue(account).build()).build())
					.build();
	}

	private Identifier getIdentifier(String identifier) {
		return Identifier.builder()
				.addAssignedIdentifierBuilder(AssignedIdentifier.builder()
					.setIdentifier(FieldWithMetaString.builder().setValue(identifier).build()))
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
							.addIdentifier(FieldWithMetaString.builder().setValue(productId).build())
							.setSource(productSource))))
				.build();
	}

	private Price getPrice(double dirtyPrice, double cleanPrice, double accruedInterest, String currency) {
		return Price.builder()
				.setNetPrice(getActualPrice(dirtyPrice, currency))
				.setCleanNetPrice(getActualPrice(cleanPrice, "USD"))
				.setAccruedInterest(BigDecimal.valueOf(accruedInterest))
				.build();
	}

	private ActualPrice getActualPrice(double amount, String currency) {
		return ActualPrice.builder()
				.setAmount(BigDecimal.valueOf(amount))
				.setCurrency(FieldWithMetaString.builder().setValue(currency).build())
				.build();
	}

	private SettlementTerms getSettlementTerms(LocalDate settlementDate, double settlementAmount, String settlementCurrency) {
		return SettlementTerms.builder()
				.setSettlementDate(AdjustableOrRelativeDate.builder()
					.setAdjustableDate(AdjustableDate.builder()
						.setAdjustedDate(
							FieldWithMetaDate.builder().setValue(DateImpl.of(settlementDate)).build()).build())
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
