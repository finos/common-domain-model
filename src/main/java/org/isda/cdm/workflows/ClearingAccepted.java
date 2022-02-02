package org.isda.cdm.workflows;

import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_ClearedTrade;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import java.util.Optional;
import java.util.function.Function;

import static org.isda.cdm.workflows.ClearingUtils.getParty;

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
		runner.postProcess(TradeState.class, contractBuilder);  // TODO: is this needed here?

		TradeState alphaContract = contractBuilder.build();

		Party party1 = getParty(tradeState, CounterpartyRoleEnum.PARTY_1);
		Party party2 = getParty(tradeState, CounterpartyRoleEnum.PARTY_2);

		String externalReference = alphaContract.getMeta().getGlobalKey();

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, alphaContract, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, alphaContract, party1, party2, externalReference, identifierService);

		Identifier identifier = Optional.ofNullable(alphaContract.getTrade().getTradeIdentifier())
				.flatMap(ids -> ids.stream().findFirst())
				.orElse(null);
		Date tradeDate = Optional.ofNullable(alphaContract.getTrade().getTradeDate())
				.map(FieldWithMetaDate::getValue)
				.orElse(null);

		WorkflowStep clearStep = ClearingUtils.buildClear(runner, externalReference, proposeStep, proposeStep.getProposedInstruction().getClearing(),
				clear, identifierService, tradeDate, identifier);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, clearStep)).build();
	}
}
