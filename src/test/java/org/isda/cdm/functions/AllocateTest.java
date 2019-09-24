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
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.isda.cdm.util.TestObjectsFactory.*;
import static org.junit.jupiter.api.Assertions.*;

public class AllocateTest extends AbstractFunctionTest {

	@Inject private Allocate func;

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
		execution = factory.getExecution(1, tradeDate, CUSIP_US1234567891, 1500000, 95.0975, 94.785, CURRENCY_USD,
				COUNTERPARTY_BROKER_A_ID, COUNTERPARTY_BROKER_A_NAME, settlementDate, true);
		allocationInstructions = factory.getAllocationInstructions(QUANTITY_1, QUANTITY_2, QUANTITY_3);
	}

	@Test
	void shouldBuildNewAllocateEvent() {
		Event allocateEvent = func.evaluate(execution, allocationInstructions);

		assertNotNull(allocateEvent);

		// event date
		assertEquals(LocalDate.now(), allocateEvent.getEventDate().toLocalDate());

		// event creation dateTime
		List<EventTimestamp> eventTimestamps = allocateEvent.getTimestamp();
		assertNotNull(eventTimestamps);
		assertEquals(1, eventTimestamps.size());
		EventTimestamp eventCreationTimestamp = eventTimestamps.get(0);
		assertEquals(LocalDate.now(), eventCreationTimestamp.getDateTime().toLocalDate());
		assertEquals(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME, eventCreationTimestamp.getQualification());

		// event identifier
		List<Identifier> eventIdentifiers = allocateEvent.getEventIdentifier();
		assertNotNull(eventIdentifiers);
		assertEquals(1, eventIdentifiers.size());
		List<String> eventIdentifier = eventIdentifiers.get(0)
				.getAssignedIdentifier()
				.stream()
				.map(AssignedIdentifier::getIdentifier)
				.map(FieldWithMetaString::getValue)
				.collect(Collectors.toList());
		assertThat(eventIdentifier, hasItems("allocationEvent1"));

		// event effects
		EventEffect eventEffect = allocateEvent.getEventEffect();
		assertNotNull(eventEffect);
		assertEquals(1, new HashSet<>(eventEffect.getEffectedExecution()).size());
		assertEquals(4, new HashSet<>(eventEffect.getExecution()).size());
		assertEquals(1, new HashSet<>(eventEffect.getProductIdentifier()).size());

		List<String> partyGlobalReferences = getPartyGlobalReferences(allocateEvent.getParty());
		assertEquals(6, partyGlobalReferences.size());

		List<String> partyExternalReferences = getPartyExternalReferences(allocateEvent.getParty());
		assertThat(partyExternalReferences,
				hasItems(EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID, CLIENT_A_ID, CLIENT_A_ACC_1_ID, CLIENT_A_ACC_2_ID, CLIENT_A_ACC_3_ID));

		List<AllocationPrimitive> allocationPrimitives = Optional.ofNullable(allocateEvent.getPrimitive())
				.map(PrimitiveEvent::getAllocation)
				.orElse(Collections.emptyList());
		assertEquals(1, allocationPrimitives.size());
		AllocationPrimitive allocationPrimitive = allocationPrimitives.get(0);

		// before -> execution
		Optional<Execution> beforeExecution = Optional.ofNullable(allocationPrimitive.getBefore()).map(Trade::getExecution);
		assertTrue(beforeExecution.isPresent());
		assertEquals(execution, beforeExecution.get());

		AllocationOutcome after = allocationPrimitive.getAfter();
		assertNotNull(after);

		// after -> original trade
		Optional<Execution> afterOriginalTrade = Optional.ofNullable(after.getOriginalTrade()).map(Trade::getExecution);
		assertTrue(afterOriginalTrade.isPresent());
		Optional<ClosedState> closedState = afterOriginalTrade.map(Execution::getClosedState);
		assertTrue(closedState.isPresent());
		assertEquals(ClosedStateEnum.ALLOCATED, closedState.get().getState());
		assertEquals(LocalDate.now(), closedState.get().getActivityDate().toLocalDate());

		// after -> allocated trades
		assertNotNull(after.getAllocatedTrade());
		List<Execution> allocatedExecutions = after.getAllocatedTrade().stream()
				.map(Trade::getExecution)
				.collect(Collectors.toList());
		assertEquals(3, allocatedExecutions.size());

		// allocated trade 1
		Optional<Execution> allocatedExecutionOptional1 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a1");
		assertTrue(allocatedExecutionOptional1.isPresent());
		Execution allocatedExecution = allocatedExecutionOptional1.get();

		List<Party> allocatedExecutionParties1 = allocatedExecution.getParty();
		assertEquals(3, allocatedExecutionParties1.size());
		assertThat(getPartyGlobalReferences(allocatedExecutionParties1),
				hasItems(getPartyGlobalReferences(allocatedExecutionParties1, CLIENT_A_ACC_1_ID),
						getPartyGlobalReferences(allocatedExecutionParties1, EXECUTING_BROKER_ID),
						getPartyGlobalReferences(allocatedExecutionParties1, COUNTERPARTY_BROKER_A_ID)));
		assertThat(getPartyExternalReferences(allocatedExecutionParties1), hasItems(CLIENT_A_ACC_1_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));

		assertThat(getPartyRoleEnums(allocatedExecution, CLIENT_A_ACC_1_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution, EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution, COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));

		assertEquals(BigDecimal.valueOf(QUANTITY_1), allocatedExecution.getQuantity().getAmount());

		// allocated trade 2
		Optional<Execution> allocatedExecutionOptional2 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a2");
		assertTrue(allocatedExecutionOptional2.isPresent());
		Execution allocatedExecution2 = allocatedExecutionOptional2.get();

		List<Party> allocatedExecutionParties2 = allocatedExecution2.getParty();
		assertEquals(3, allocatedExecutionParties2.size());
		assertThat(getPartyGlobalReferences(allocatedExecutionParties2),
				hasItems(getPartyGlobalReferences(allocatedExecutionParties2, CLIENT_A_ACC_2_ID),
						getPartyGlobalReferences(allocatedExecutionParties2, EXECUTING_BROKER_ID),
						getPartyGlobalReferences(allocatedExecutionParties2, COUNTERPARTY_BROKER_A_ID)));
		assertThat(getPartyExternalReferences(allocatedExecutionParties2), hasItems(CLIENT_A_ACC_2_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));

		assertThat(getPartyRoleEnums(allocatedExecution2, CLIENT_A_ACC_2_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution2, EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution2, COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));

		assertEquals(BigDecimal.valueOf(QUANTITY_2), allocatedExecution2.getQuantity().getAmount());

		// allocated trade 3
		Optional<Execution> allocatedExecutionOptional3 = getAllocatedExecution(allocatedExecutions, "tradeId_c1a3");
		assertTrue(allocatedExecutionOptional3.isPresent());
		Execution allocatedExecution3 = allocatedExecutionOptional3.get();

		List<Party> allocatedExecutionParties3 = allocatedExecution3.getParty();
		assertEquals(3, allocatedExecutionParties3.size());
		assertThat(getPartyGlobalReferences(allocatedExecutionParties3),
				hasItems(getPartyGlobalReferences(allocatedExecutionParties3, CLIENT_A_ACC_3_ID),
						getPartyGlobalReferences(allocatedExecutionParties3, EXECUTING_BROKER_ID),
						getPartyGlobalReferences(allocatedExecutionParties3, COUNTERPARTY_BROKER_A_ID)));
		assertThat(getPartyExternalReferences(allocatedExecutionParties3), hasItems(CLIENT_A_ACC_3_ID, EXECUTING_BROKER_ID, COUNTERPARTY_BROKER_A_ID));

		assertThat(getPartyRoleEnums(allocatedExecution3, CLIENT_A_ACC_3_ID), hasItems(PartyRoleEnum.CLIENT));
		assertThat(getPartyRoleEnums(allocatedExecution3, EXECUTING_BROKER_ID), hasItems(PartyRoleEnum.EXECUTING_ENTITY, PartyRoleEnum.BUYER));
		assertThat(getPartyRoleEnums(allocatedExecution3, COUNTERPARTY_BROKER_A_ID), hasItems(PartyRoleEnum.COUNTERPARTY, PartyRoleEnum.SELLER));

		assertEquals(BigDecimal.valueOf(QUANTITY_3), allocatedExecution3.getQuantity().getAmount());
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

	private String getPartyGlobalReferences(List<Party> party, String partyExternalReference) {
		return party
				.stream()
				.map(Party::getMeta)
				.filter(m -> m.getExternalKey().equals(partyExternalReference))
				.map(MetaFields::getGlobalKey)
				.collect(MoreCollectors.onlyElement());
	}

	private List<String> getPartyGlobalReferences(List<Party> party) {
		return party
				.stream()
				.map(Party::getMeta)
				.map(MetaFields::getGlobalKey)
				.collect(Collectors.toList());
	}

	private List<String> getPartyExternalReferences(List<Party> party) {
		return party
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
