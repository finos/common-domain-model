package org.finos.cdm.functions;

import cdm.event.workflow.WorkflowStep;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateWorkflowInput {

    @JsonProperty
    private List<WorkflowStep> steps;

    public CreateWorkflowInput() {
    }

    public CreateWorkflowInput(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    public List<WorkflowStep> getSteps() {
        return steps;
    }
}
