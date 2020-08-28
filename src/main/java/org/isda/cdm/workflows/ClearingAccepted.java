package org.isda.cdm.workflows;

import java.util.Optional;
import java.util.function.Function;

import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.Contract;
import org.isda.cdm.TradeDate;
import org.isda.cdm.Workflow;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.functions.Create_ClearedTrade;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;

import cdm.base.staticdata.party.Party;

public class ClearingAccepted implements Function<Contract, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private Create_ClearedTrade clear;
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(Contract contract) {
		Contract.ContractBuilder contractBuilder = contract.toBuilder();
		runner.postProcess(Contract.class, contractBuilder);  // TODO: is this needed here?

		Contract alphaContract = contractBuilder.build();

		Party party1 = getParty(contract, CounterpartyEnum.PARTY_1);
		Party party2 = getParty(contract, CounterpartyEnum.PARTY_2);

		String externalReference = alphaContract.getMeta().getGlobalKey();

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, alphaContract, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, alphaContract, party1, party2, externalReference, identifierService);

		Identifier identifier = Optional.ofNullable(alphaContract.getContractIdentifier())
				.flatMap(ids -> ids.stream().findFirst())
				.orElse(null);
		Date tradeDate = Optional.ofNullable(alphaContract.getTradeDate())
				.map(TradeDate::getDate)
				.orElse(null);

		WorkflowStep clearStep = ClearingUtils.buildClear(runner, externalReference, proposeStep, proposeStep.getProposedInstruction().getClearing(),
				clear, identifierService, tradeDate, identifier);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, clearStep)).build();
	}

	/**
	 * Extract the party related to the given counterparty enum.
	 */
	private Party getParty(Contract contract, CounterpartyEnum counterparty) {
		return contract.getTradableProduct().getCounterparties().stream()
				.filter(c -> c.getCounterparty() == counterparty)
				.map(Counterparty::getParty)
				.map(ReferenceWithMetaParty::getGlobalReference)
				.flatMap(partyReference -> contract.getParty().stream().filter(p -> partyReference.equals(p.getMeta().getGlobalKey())))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Party not found for counterparty " + counterparty));
	}
}
