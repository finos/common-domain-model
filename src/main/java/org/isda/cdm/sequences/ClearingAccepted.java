package org.isda.cdm.sequences;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessorRunnerI;
import com.rosetta.model.lib.sequence.Sequence;
import org.isda.cdm.Contract;
import org.isda.cdm.Workflow;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.functions.Clear;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

public class ClearingAccepted implements Sequence<Contract, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private Clear clear;
	@Inject
	private PostProcessorRunnerI runner;

	@Override
	public Workflow enrich(Contract contract) {
		Contract.ContractBuilder contractBuilder = contract.toBuilder();
		runner.postProcess(Contract.class, contractBuilder);  // TODO: is this needed here?
		Contract contractWithParties = ClearingUtils.addPartyRoles(contractBuilder.build());

		String externalReference = contractWithParties.getMeta().getGlobalKey();

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, contractWithParties, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, contractWithParties, externalReference, identifierService);

		WorkflowStep clearStep = ClearingUtils.buildClear(runner, contractWithParties, externalReference, proposeStep, proposeStep.getProposedInstruction().getClearing(), clear, identifierService);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, clearStep)).build();
	}

}
