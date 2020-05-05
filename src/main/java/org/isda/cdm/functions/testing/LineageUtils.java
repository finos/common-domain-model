package org.isda.cdm.functions.testing;

import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.Lineage;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.metafields.ReferenceWithMetaWorkflowStep;

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
}
