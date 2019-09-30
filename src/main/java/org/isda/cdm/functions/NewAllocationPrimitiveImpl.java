package org.isda.cdm.functions;

import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.hashing.*;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.AllocationPrimitive.AllocationPrimitiveBuilder;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.isda.cdm.AllocationPrimitive.builder;
import static org.isda.cdm.PartyRoleEnum.CLIENT;

/**
 * Sample NewTransferPrimitive implementation, should be used as a simple example only.
 *
 * TODO move to CDM exmaples repo
 */
public class NewAllocationPrimitiveImpl extends NewAllocationPrimitive {

	private final List<PostProcessStep> postProcessors;

	public NewAllocationPrimitiveImpl() {
		RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
		this.postProcessors = Arrays.asList(rosettaKeyProcessStep,
				new RosettaKeyValueProcessStep(RosettaKeyValueHashFunction::new),
				new ReKeyProcessStep(rosettaKeyProcessStep));
	}

	@Override
	protected AllocationPrimitiveBuilder doEvaluate(Execution execution, AllocationInstructions allocationInstructions) {
		// Get client reference
		String clientReference = execution.getPartyRole()
				.stream()
				.filter(r -> r.getRole() == CLIENT)
				.collect(MoreCollectors.onlyElement())
				.getPartyReference()
				.getGlobalReference();

		// Get broker partyRoles (e.g. excluding client)
		List<PartyRole> brokerPartyRoles = execution.getPartyRole().stream()
				.filter(r -> !r.getPartyReference().getGlobalReference().equals(clientReference))
				.collect(Collectors.toList());

		// Get client partyRoles which will be replaced by allocating client party.
		List<PartyRole> clientPartyRoles = execution.getPartyRole().stream()
				.filter(r -> r.getPartyReference().getGlobalReference().equals(clientReference))
				.collect(Collectors.toList());

		// Get broker parties
		List<ReferenceWithMetaParty> brokerParties = execution.getParty().stream()
				.filter(p -> !p.getValue().getMeta().getGlobalKey().equals(clientReference))
				.collect(Collectors.toList());

		// Create AllocationPrimitive and set the before trade.
		AllocationPrimitiveBuilder allocationPrimitiveBuilder = builder()
				.setBeforeBuilder(Trade.builder().setExecution(execution));

		// Set the after originalTrade (and update the closedState to allocated)
		AllocationOutcome.AllocationOutcomeBuilder allocationOutcomeBuilder = AllocationOutcome.builder()
				.setOriginalTradeBuilder(Trade.builder()
						.setExecutionBuilder(execution.toBuilder()
								.setClosedStateBuilder(ClosedState.builder()
										.setState(ClosedStateEnum.ALLOCATED)
										.setActivityDate(DateImpl.of(LocalDate.now())))));

		// For each allocation instruction, create a new execution with the allocated party and amount
		allocationInstructions.getBreakdowns().forEach(breakdown -> {
			Quantity allocatedQuantity = breakdown.getQuantity();
			Party allocatingParty = breakdown.getPartyReference().getValue();

			// Update the partyReference on all the client partyRoles to the allocating client party reference
			List<PartyRole> allocatingClientPartyRoles = clientPartyRoles.stream()
					.map(PartyRole::toBuilder)
					.map(b -> b.setPartyReference(ReferenceWithMetaParty.builder()
							.setExternalReference(allocatingParty.getMeta().getExternalKey())
							.build()))
					.map(PartyRole.PartyRoleBuilder::build)
					.collect(Collectors.toList());

			// Build the allocated executions
			allocationOutcomeBuilder.addAllocatedTradeBuilder(Trade.builder()
					.setExecutionBuilder(
							execution.toBuilder() // Start with the input execution and the required fields
									// identifier
									.clearIdentifier()
									.addIdentifier(getIdentifier("tradeId_" + allocatingParty.getMeta().getExternalKey()))
									// party and partyRoles
									.clearParty()
									.clearPartyRole()
									.addParty(brokerParties)
									.addPartyRole(brokerPartyRoles)
									.addParty(ReferenceWithMetaParty.builder().setValue(allocatingParty).build())
									.addPartyRole(allocatingClientPartyRoles)
									// quantity
									.setQuantity(allocatedQuantity)
									// settlement terms
									.setSettlementTerms(getSettlementTerms(execution, allocatedQuantity))));
		});

		allocationPrimitiveBuilder.setAfterBuilder(allocationOutcomeBuilder);

		// Update keys / references
		postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(AllocationPrimitive.class, allocationPrimitiveBuilder));

		return allocationPrimitiveBuilder;
	}

	private SettlementTerms getSettlementTerms(Execution execution, Quantity allocatedQuantity) {
		ActualPrice dirtyPrice = execution.getPrice().getNetPrice();
		SettlementTerms settlementTerms = execution.getSettlementTerms();

		String tradeCurrency = dirtyPrice.getCurrency().getValue();
		String settlementCurrency = settlementTerms.getSettlementCurrency().getValue();
		if (!tradeCurrency.equals(settlementCurrency)) {
			throw new IllegalArgumentException(String.format("Different trade and settlement currencies not supported", tradeCurrency, settlementCurrency));
		}

		BigDecimal settlementAmount = dirtyPrice.getAmount().multiply(allocatedQuantity.getAmount());
		return settlementTerms.toBuilder()
				.setSettlementAmount(Money.builder()
						.setAmount(settlementAmount)
						.build())
				.build();
	}

	private Identifier getIdentifier(String id) {
		return Identifier.builder()
				.addAssignedIdentifierBuilder(AssignedIdentifier.builder()
						.setIdentifier(FieldWithMetaString.builder()
								.setValue(id)
								.build())
						.setVersion(1))
				.build();
	}
}
