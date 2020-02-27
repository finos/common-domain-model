package org.isda.cdm.workflows;

import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.*;
import org.isda.cdm.Workflow.WorkflowBuilder;

import static org.isda.cdm.workflows.WorkflowUtils.guard;

import java.util.function.Function;

public class Inception implements Function<Contract, Workflow> {
	@Inject
	private org.isda.cdm.functions.Inception inception;
	
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(Contract contract) {
		BusinessEvent businessEvent = inception.evaluate(
				contract.getTradableProduct().getProduct(), 
				guard(contract.getTradableProduct().getQuantityNotation()), 
				guard(contract.getTradableProduct().getPriceNotation()), 
				guard(contract.getParty()), 
				guard(contract.getPartyRole()), 
				null);
		
		WorkflowBuilder workflowBuilder = Workflow.builder().addSteps(WorkflowStep.builder().setBusinessEvent(businessEvent).build());
		runner.postProcess(Workflow.class, workflowBuilder);
		return workflowBuilder.build();
	}
}
