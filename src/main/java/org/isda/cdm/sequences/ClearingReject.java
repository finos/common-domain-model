package org.isda.cdm.sequences;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessorRunner;
import com.rosetta.model.lib.sequence.Sequence;
import org.isda.cdm.Contract;
import org.isda.cdm.Workflow;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

public class ClearingReject implements Sequence<Contract, Workflow> {
	@Inject
	private IdentifierService identifierService;
	@Inject
	private PostProcessorRunner runner;

	public Workflow enrich(Contract contract) {
		Contract contractWithRoles = ClearingUtils.addPartyRoles(contract);

		String externalReference = contractWithRoles.getMeta().getGlobalKey();

		// Contract Formation
		WorkflowStep contractFormationStep = ClearingUtils.buildContractFormationStep(runner, contractWithRoles, externalReference, identifierService);

		// propose clear step
		WorkflowStep proposeStep = ClearingUtils.buildProposeStep(runner, contractFormationStep, externalReference, identifierService);

		WorkflowStep rejectStep = ClearingUtils.buildRejectStep(runner, proposeStep, externalReference, identifierService);

		return Workflow.builder().addSteps(Lists.newArrayList(contractFormationStep, proposeStep, rejectStep)).build();
	}

}
