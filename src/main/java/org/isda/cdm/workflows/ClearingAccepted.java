package org.isda.cdm.workflows;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.Contract;
import org.isda.cdm.Workflow;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.functions.ClearBusinessEvent;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import java.util.function.Function;

public class ClearingAccepted implements Function<Contract, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private ClearBusinessEvent clear;
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(Contract contract) {
		Contract.ContractBuilder contractBuilder = contract.toBuilder();
		runner.postProcess(Contract.class, contractBuilder);  // TODO: is this needed here?
		Contract contractWithParties = ClearingUtils.addPartyRoles(contractBuilder.build());

		String externalReference = contractWithParties.getMeta().getGlobalKey();

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, contractWithParties, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, contractWithParties, externalReference, identifierService);

		WorkflowStep clearStep = ClearingUtils.buildClear(runner, externalReference, proposeStep, proposeStep.getProposedInstruction().getClearing(), clear, identifierService);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, clearStep)).build();
	}

}
