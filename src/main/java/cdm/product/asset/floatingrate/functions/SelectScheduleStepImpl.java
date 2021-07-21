package cdm.product.asset.floatingrate.functions;

import cdm.base.math.RateSchedule;
import cdm.base.math.Step;

import java.util.List;

public class SelectScheduleStepImpl  extends SelectScheduleStep {

    @Override
    protected Step.StepBuilder doEvaluate(List<? extends Step> steps, Integer stepNo) {

    int n = stepNo;
    Step step = n < 0 || n >= steps.size() ? null : steps.get(n);


    Step.StepBuilder builder = Step.builder();
    if (step != null) {
        builder.setStepDate(step.getStepDate());
        builder.setStepValue(step.getStepValue());
    }
    return builder;
}


}
