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

    public List<WorkflowStep> withLineage(WorkflowStep... steps) {
        List<WorkflowStep> lineageSteps = new ArrayList<>(steps.length);
        for (int i = 0; i < steps.length; i++) {

            System.out.println("Current iteration: " + i);


            Optional<WorkflowStep> previous = i == 0 ? Optional.empty() : Optional.of(lineageSteps.get(i - 1));
            WorkflowStep.WorkflowStepBuilder current = steps[i].toBuilder();

            runner.postProcess(WorkflowStep.class, current);

            System.out.println("Current Key : " + current.getMeta().getGlobalKey());

            if (previous.isPresent()) {

                String prevKey = previous.get().getMeta().getGlobalKey();
                System.out.println("Previous Key: " + prevKey);

                ReferenceWithMetaWorkflowStep previousStepReference = ReferenceWithMetaWorkflowStep.builder()
                        .setGlobalReference(previous.get().getMeta().getGlobalKey()).build();

                current.setLineage(Lineage.builder().addEventReference(previousStepReference).build())
                        .setPreviousWorkflowStep(previousStepReference);
            }
            lineageSteps.add(current.build());
        }
        return lineageSteps;
    }
}
