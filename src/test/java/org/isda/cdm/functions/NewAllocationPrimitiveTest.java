package org.isda.cdm.functions;

import com.google.common.collect.MoreCollectors;
import com.google.inject.Inject;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.isda.cdm.util.TestObjectsFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.isda.cdm.util.TestObjectsFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class NewAllocationPrimitiveTest extends AbstractFunctionTest {

	@Inject private NewAllocationPrimitive func;

	private static final double DIRTY_PRICE = 95.0975;
	private static final int QUANTITY_1 = 750000;
	private static final int QUANTITY_2 = 250000;
	private static final int QUANTITY_3 = 50000;

	private Execution execution;
	private AllocationInstructions allocationInstructions;

	@BeforeEach
	void setUpTests() {
		TestObjectsFactory factory = new TestObjectsFactory();
		LocalDate tradeDate = LocalDate.of(2019, 8, 26);
		LocalDate settlementDate = LocalDate.of(2019, 8, 28);
		execution = factory.getExecution(1, tradeDate, CUSIP_US1234567891,
				factory.getQuantity(1500000),
				factory.getPrice(DIRTY_PRICE, 94.785, CURRENCY_USD),
				settlementDate, true,
				factory.getParty(CLIENT_A_ID, CLIENT_A_NAME, null),
				factory.getParty(EXECUTING_BROKER_ID, EXECUTING_BROKER_NAME, factory.getAccount(EXECUTING_BROKER_NAME)),
				factory.getParty(COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME, factory.getAccount(COUNTERPARTY_BROKER_A_NAME)));
		allocationInstructions = factory.getAllocationInstructions(
				QUANTITY_1, factory.getParty(CLIENT_A_ACC_1_ID, CLIENT_A_ACC_1_NAME, factory.getAccount(CLIENT_A_ACC_1_NAME)),
				QUANTITY_2, factory.getParty(CLIENT_A_ACC_2_ID, CLIENT_A_ACC_2_NAME, factory.getAccount(CLIENT_A_ACC_2_NAME)),
				QUANTITY_3, factory.getParty(CLIENT_A_ACC_3_ID, CLIENT_A_ACC_3_NAME, factory.getAccount(CLIENT_A_ACC_3_NAME)));
	}

	@Test
	void shouldBuildNewAllocationPrimitive() {
		AllocationPrimitive allocationPrimitive = func.evaluate(execution, allocationInstructions);

		assertNotNull(allocationPrimitive);

		// assert before execution
		Optional<Execution> beforeExecution = Optional.ofNullable(allocationPrimitive.getBefore()).map(Trade::getExecution);
		assertTrue(beforeExecution.isPresent());
		assertEquals(this.execution, beforeExecution.get());

		AllocationOutcome after = allocationPrimitive.getAfter();
		assertNotNull(after);

		// assert after original trade
		Optional<Execution> afterOriginalTrade = Optional.ofNullable(after.getOriginalTrade()).map(Trade::getExecution);
		assertTrue(afterOriginalTrade.isPresent());
		Optional<ClosedState> closedState = afterOriginalTrade.map(Execution::getClosedState);
		assertTrue(closedState.isPresent());
		assertEquals(ClosedStateEnum.ALLOCATED, closedState.get().getState());
		assertEquals(LocalDate.now(), closedState.get().getActivityDate().toLocalDate());

		// assert allocated trades
		assertNotNull(after.getAllocatedTrade());
		List<Execution> allocatedExecutions = after.getAllocatedTrade().stream().map(Trade::getExecution).collect(Collectors.toList());
		assertEquals(3, allocatedExecutions.size());

		// allocated trade 1
		Optional<Execution> allocatedExecutionOptional1 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a1");
		assertTrue(allocatedExecutionOptional1.isPresent());
		Execution allocatedExecution1 = allocatedExecutionOptional1.get();

		assertEquals(3, allocatedExecution1.getParty().size());
		assertThat(getPartyExternalKeys(allocatedExecution1), hasItems(CLIENT_A_ACC_1_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));
		assertThat(getPartyRoleEnums(allocatedExecution1, CLIENT_A_ACC_1_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution1, EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution1, COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));
		assertEquals(BigDecimal.valueOf(QUANTITY_1), allocatedExecution1.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(QUANTITY_1).multiply(BigDecimal.valueOf(DIRTY_PRICE)),
				allocatedExecution1.getSettlementTerms().getSettlementAmount().getAmount());

		// allocated trade 2
		Optional<Execution> allocatedExecutionOptional2 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a2");
		assertTrue(allocatedExecutionOptional2.isPresent());
		Execution allocatedExecution2 = allocatedExecutionOptional2.get();

		assertEquals(3, allocatedExecution2.getParty().size());
		assertThat(getPartyExternalKeys(allocatedExecution2), hasItems(CLIENT_A_ACC_2_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));
		assertThat(getPartyRoleEnums(allocatedExecution2, CLIENT_A_ACC_2_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution2, EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution2, COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));
		assertEquals(BigDecimal.valueOf(QUANTITY_2), allocatedExecution2.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(QUANTITY_2).multiply(BigDecimal.valueOf(DIRTY_PRICE)),
				allocatedExecution2.getSettlementTerms().getSettlementAmount().getAmount());

		// allocated trade 3
		Optional<Execution> allocatedExecutionOptional3 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a3");
		assertTrue(allocatedExecutionOptional3.isPresent());
		Execution allocatedExecution3 = allocatedExecutionOptional3.get();

		assertEquals(3, allocatedExecution3.getParty().size());
		assertThat(getPartyExternalKeys(allocatedExecution3), hasItems(CLIENT_A_ACC_3_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));
		assertThat(getPartyRoleEnums(allocatedExecution3, CLIENT_A_ACC_3_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution3, EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution3, COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));
		assertEquals(BigDecimal.valueOf(QUANTITY_3), allocatedExecution3.getQuantity().getAmount());
		assertEquals(BigDecimal.valueOf(QUANTITY_3).multiply(BigDecimal.valueOf(DIRTY_PRICE)),
				allocatedExecution3.getSettlementTerms().getSettlementAmount().getAmount());
	}

	private Optional<Execution> getAllocatedExecution(List<Execution> allocatedExecutions, String tradeId) {
		return allocatedExecutions.stream()
				.filter(e -> e.getIdentifier().stream()
						.map(Identifier::getAssignedIdentifier)
						.flatMap(Collection::stream)
						.map(AssignedIdentifier::getIdentifier)
						.map(FieldWithMetaString::getValue)
						.anyMatch(id -> id.equals(tradeId)))
				.collect(MoreCollectors.toOptional());
	}

	private List<String> getPartyExternalKeys(Execution e) {
		return e.getParty()
				.stream()
				.map(ReferenceWithMetaParty::getValue)
				.map(Party::getMeta)
				.map(MetaFields::getExternalKey)
				.collect(Collectors.toList());
	}

	private List<PartyRoleEnum> getPartyRoleEnums(Execution allocatedExecution1, String partyReference) {
		return allocatedExecution1.getPartyRole()
				.stream()
				.filter(r -> r.getPartyReference().getExternalReference().equals(partyReference))
				.map(PartyRole::getRole)
				.collect(Collectors.toList());
	}
}
