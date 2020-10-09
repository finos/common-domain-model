package org.isda.cdm.workflows;

import static org.isda.cdm.workflows.ClearingUtils.getParty;

import java.util.function.Function;

import org.isda.cdm.functions.example.services.identification.IdentifierService;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;

import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.Party;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import cdm.legalagreement.contract.Contract;

public class ClearingRejected implements Function<Contract, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(Contract contract) {

		String externalReference = contract.getMeta().getGlobalKey();
		Party party1 = getParty(contract, CounterpartyEnum.PARTY_1);
		Party party2 = getParty(contract, CounterpartyEnum.PARTY_2);

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, contract, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, contract, party1, party2, externalReference, identifierService);

		WorkflowStep rejectStep = ClearingUtils.buildRejectStep(runner, proposeStep, externalReference, identifierService);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, rejectStep)).build();
	}

}
