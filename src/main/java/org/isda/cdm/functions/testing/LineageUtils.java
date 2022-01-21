package org.isda.cdm.functions.testing;

import cdm.event.common.Lineage;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.metafields.ReferenceWithMetaWorkflowStep;
import com.google.inject.Inject;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.process.PostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LineageUtils {

    @Inject
    private PostProcessor runner;

    public List<WorkflowStep> withLineage(WorkflowStep... workflowSteps) {
        List<WorkflowStep> lineageSteps = new ArrayList<>(workflowSteps.length);

        for (int i = 0; i < workflowSteps.length; i++) {
            Optional<WorkflowStep> previous = i == 0 ? Optional.empty() : Optional.of(lineageSteps.get(i - 1));
            WorkflowStep currentStep = workflowSteps[i];
            WorkflowStep.WorkflowStepBuilder current = currentStep.toBuilder();

            if (previous.isPresent()) {
                String prevKey = previous.get().getMeta().getGlobalKey();
                ReferenceWithMetaWorkflowStep previousStepReference = ReferenceWithMetaWorkflowStep.builder()
                        .setGlobalReference(prevKey).build();

                current.setLineage(Lineage.builder().addEventReference(previousStepReference).build())
                        .setPreviousWorkflowStep(previousStepReference);
            }
            runner.postProcess(WorkflowStep.class, current);
            lineageSteps.add(current.build());
        }
        return lineageSteps;
    }
    
    public <T extends RosettaModelObject> T withGlobalReference(Class<T> modelType, T modelObject) {
        return modelType.cast(runner.postProcess(modelType, modelObject.toBuilder().prune()).build());
    }

}
