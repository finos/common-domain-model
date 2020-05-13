package org.isda.cdm.workflows;

import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.BusinessEvent;
import org.isda.cdm.Contract;
import org.isda.cdm.Workflow;
import org.isda.cdm.Workflow.WorkflowBuilder;
import org.isda.cdm.WorkflowStep;

import java.util.function.Function;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class Inception implements Function<Contract, Workflow> {
	@Inject
	private org.isda.cdm.functions.Create_Inception inception;
	
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
