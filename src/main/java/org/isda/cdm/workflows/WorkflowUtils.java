package org.isda.cdm.workflows;

import com.google.common.collect.Lists;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.BusinessEvent;
import org.isda.cdm.Lineage;
import org.isda.cdm.Workflow;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.WorkflowStep.WorkflowStepBuilder;
import org.isda.cdm.metafields.ReferenceWithMetaWorkflowStep;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WorkflowUtils {

	static <T> List<T> guard(List<T> list) {
		return Optional.ofNullable(list).orElse(Lists.newArrayList());		
	}

	static Workflow buildWorkflow(PostProcessor runner, BusinessEvent... events) {
		Workflow.WorkflowBuilder builder = Workflow.builder();
		Arrays.stream(events).forEach(b -> builder.addSteps(WorkflowStep.builder().setBusinessEvent(b).build()));
		runner.postProcess(Workflow.class, builder);
		buildLineage(builder);
		return builder.build();
	}

	private static void buildLineage(Workflow.WorkflowBuilder workflowBuilder) {
		List<WorkflowStepBuilder> steps = workflowBuilder.getSteps();
		WorkflowStepBuilder s = null;
		for (WorkflowStepBuilder workflowStepBuilder : steps) {
			if (s != null) {
				ReferenceWithMetaWorkflowStep ref = ReferenceWithMetaWorkflowStep.builder()
						.setGlobalReference(s.getMeta().getGlobalKey())
						.build();
				workflowStepBuilder
						.setPreviousWorkflowStep(ref)
						.setLineage(Lineage.builder()
								.addEventReference(ref)
								.build());
			}
			s = workflowStepBuilder;
		}
	}

}
