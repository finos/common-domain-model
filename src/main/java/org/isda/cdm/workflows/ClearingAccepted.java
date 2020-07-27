package org.isda.cdm.workflows;

import cdm.base.staticdata.party.Party;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.Contract;
import org.isda.cdm.Workflow;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.functions.Create_ClearedTrade;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import java.util.function.Function;

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

		Party party1 = contract.getParty().stream()
				.filter(party -> "party1".equals(party.getMeta().getExternalKey()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party1 on alpha"));

		Party party2 = contract.getParty().stream()
				.filter(party -> "party2".equals(party.getMeta().getExternalKey()))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party2 on alpha"));

		String externalReference = alphaContract.getMeta().getGlobalKey();

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, alphaContract, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, alphaContract, party1, party2, externalReference, identifierService);

		WorkflowStep clearStep = ClearingUtils.buildClear(runner, externalReference, proposeStep, proposeStep.getProposedInstruction().getClearing(), clear, identifierService);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, clearStep)).build();
	}

}
