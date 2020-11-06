package org.isda.cdm.workflows;

import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.Party;
import cdm.event.common.TradeState;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import java.util.function.Function;

import static org.isda.cdm.workflows.ClearingUtils.getParty;

public class ClearingRejected implements Function<TradeState, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(TradeState tradeState) {

		String externalReference = tradeState.getMeta().getGlobalKey();
		Party party1 = getParty(tradeState, CounterpartyEnum.PARTY_1);
		Party party2 = getParty(tradeState, CounterpartyEnum.PARTY_2);

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, tradeState, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, tradeState, party1, party2, externalReference, identifierService);

		WorkflowStep rejectStep = ClearingUtils.buildRejectStep(runner, proposeStep, externalReference, identifierService);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, rejectStep)).build();
	}

}
