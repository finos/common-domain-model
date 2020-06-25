package org.isda.cdm.workflows;

import cdm.base.staticdata.party.Party;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.Contract;
import org.isda.cdm.Workflow;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import java.util.function.Function;

public class ClearingRejected implements Function<Contract, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(Contract contract) {

		String externalReference = contract.getMeta().getGlobalKey();
		Party party1 = contract.getParty().stream()
				.filter(party -> "party1".equals(party.getMeta().getExternalKey()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party1 on alpha"));

		Party party2 = contract.getParty().stream()
				.filter(party -> "party2".equals(party.getMeta().getExternalKey()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party2 on alpha"));

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, contract, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, party1, party2, externalReference, identifierService);

		WorkflowStep rejectStep = ClearingUtils.buildRejectStep(runner, proposeStep, externalReference, identifierService);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, rejectStep)).build();
	}

}
