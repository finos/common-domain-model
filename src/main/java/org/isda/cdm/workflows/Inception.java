package org.isda.cdm.workflows;

import java.util.function.Function;

import org.isda.cdm.BusinessEvent;
import org.isda.cdm.Contract;
import org.isda.cdm.LegalAgreement;
import org.isda.cdm.Workflow;
import org.isda.cdm.Workflow.WorkflowBuilder;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.functions.InceptionBusinessEvent;

import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;

public class Inception implements Function<Contract, Workflow> {
	@Inject
	private InceptionBusinessEvent inception;
	
	@Inject
	private PostProcessor runner;

	@Override
	public Workflow apply(Contract contract) {
		BusinessEvent businessEvent = inception.evaluate(
				contract.getTradableProduct(), 
				contract.getParty(), 
				contract.getPartyRole(), 
				LegalAgreement.builder().build());
		
		WorkflowBuilder workflowBuilder = Workflow.builder().addSteps(WorkflowStep.builder().setBusinessEvent(businessEvent).build());
		runner.postProcess(Workflow.class, workflowBuilder);
		return workflowBuilder.build();
	}
}
