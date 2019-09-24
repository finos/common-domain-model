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
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.isda.cdm.util.TestObjectsFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class NewAllocationPrimitiveTest extends AbstractFunctionTest {

	@Inject private NewAllocationPrimitive func;

	private static final int QUANTITY_1 = 750000;
	private static final int QUANTITY_2 = 250000;
	private static final int QUANTITY_3 = 50000;

	private Execution execution;
	private AllocationInstructions allocationInstructions;

	@BeforeEach
	public void setUpTests() {
		TestObjectsFactory factory = new TestObjectsFactory();
		LocalDate tradeDate = LocalDate.of(2019, 8, 26);
		LocalDate settlementDate = LocalDate.of(2019, 8, 28);
		execution = factory.getExecution(1, tradeDate, CUSIP_US1234567891, 1500000,
				95.0975, 94.785, CURRENCY_USD, COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME, settlementDate, true);
		allocationInstructions = factory.getAllocationInstructions(QUANTITY_1, QUANTITY_2, QUANTITY_3);
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
		Optional<Execution> allocatedExecution1 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a1");
		assertTrue(allocatedExecution1.isPresent());
		assertEquals(3, allocatedExecution1.get().getParty().size());
		assertThat(getPartyReferences(allocatedExecution1.get()), hasItems(CLIENT_A_ACC_1_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));
		assertThat(getPartyRoleEnums(allocatedExecution1.get(), CLIENT_A_ACC_1_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution1.get(), EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution1.get(), COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));
		assertEquals(BigDecimal.valueOf(QUANTITY_1), allocatedExecution1.get().getQuantity().getAmount());

		// allocated trade 2
		Optional<Execution> allocatedExecution2 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a2");
		assertTrue(allocatedExecution2.isPresent());
		assertEquals(3, allocatedExecution2.get().getParty().size());
		assertThat(getPartyReferences(allocatedExecution2.get()), hasItems(CLIENT_A_ACC_2_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));
		assertThat(getPartyRoleEnums(allocatedExecution2.get(), CLIENT_A_ACC_2_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution2.get(), EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution2.get(), COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));
		assertEquals(BigDecimal.valueOf(QUANTITY_2), allocatedExecution2.get().getQuantity().getAmount());

		// allocated trade 3
		Optional<Execution> allocatedExecution3 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a3");
		assertTrue(allocatedExecution3.isPresent());
		assertEquals(3, allocatedExecution3.get().getParty().size());
		assertThat(getPartyReferences(allocatedExecution3.get()), hasItems(CLIENT_A_ACC_3_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));
		assertThat(getPartyRoleEnums(allocatedExecution3.get(), CLIENT_A_ACC_3_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution3.get(), EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution3.get(), COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));
		assertEquals(BigDecimal.valueOf(QUANTITY_3), allocatedExecution3.get().getQuantity().getAmount());
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

	private List<String> getPartyReferences(Execution e) {
		return e.getParty()
				.stream()
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
