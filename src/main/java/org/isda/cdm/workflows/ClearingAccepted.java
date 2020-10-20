package org.isda.cdm.workflows;

import java.util.Optional;
import java.util.function.Function;

import cdm.base.staticdata.identifier.Identifier;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.*;
import org.isda.cdm.functions.Create_ClearedTrade;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;

import cdm.base.staticdata.party.Party;

public class ClearingAccepted implements Function<TradeState, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private Create_ClearedTrade clear;
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(TradeState tradeState) {
		TradeState.TradeStateBuilder contractBuilder = tradeState.toBuilder();
		runner.postProcess(Contract.class, contractBuilder);  // TODO: is this needed here?

		TradeState alphaContract = contractBuilder.build();

		Party party1 = tradeState.getTrade().getParty().stream()
				.filter(party -> "party1".equals(party.getMeta().getExternalKey()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party1 on alpha"));

		Party party2 = tradeState.getTrade().getParty().stream()
				.filter(party -> "party2".equals(party.getMeta().getExternalKey()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party2 on alpha"));

		String externalReference = alphaContract.getMeta().getGlobalKey();

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, alphaContract, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, alphaContract, party1, party2, externalReference, identifierService);

		Identifier identifier = Optional.ofNullable(alphaContract.getTrade().getIdentifier())
				.flatMap(ids -> ids.stream().findFirst())
				.orElse(null);
		Date tradeDate = Optional.ofNullable(alphaContract.getTrade().getTradeDate())
				.map(TradeDate::getDate)
				.orElse(null);

		WorkflowStep clearStep = ClearingUtils.buildClear(runner, externalReference, proposeStep, proposeStep.getProposedInstruction().getClearing(),
				clear, identifierService, tradeDate, identifier);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, clearStep)).build();
	}

}
