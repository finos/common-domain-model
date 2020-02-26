package org.isda.cdm.workflows;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.*;
import org.isda.cdm.Workflow.WorkflowBuilder;
import org.isda.cdm.functions.InceptionBusinessEvent;

import java.util.Optional;
import java.util.function.Function;

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
				Optional.ofNullable(contract.getPartyRole()).orElse(Lists.newArrayList()), 
				LegalAgreement.builder().build());
		
		WorkflowBuilder workflowBuilder = Workflow.builder().addSteps(WorkflowStep.builder().setBusinessEvent(businessEvent).build());
		runner.postProcess(Workflow.class, workflowBuilder);
		return workflowBuilder.build();
	}
}
