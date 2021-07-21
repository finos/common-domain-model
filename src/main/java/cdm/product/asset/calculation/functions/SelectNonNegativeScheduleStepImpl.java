package cdm.product.asset.calculation.functions;

import cdm.base.math.NonNegativeStep;
import cdm.base.math.NonNegativeStepSchedule;

import java.util.List;

public class SelectNonNegativeScheduleStepImpl extends SelectNonNegativeScheduleStep {
    @Override
    protected NonNegativeStep.NonNegativeStepBuilder doEvaluate(NonNegativeStepSchedule schedule, Integer stepNo) {

        List<? extends NonNegativeStep> steps = schedule.getStep();
        int n = stepNo;
        NonNegativeStep step = n < 0 || n >= steps.size() ? null : steps.get(n);


        NonNegativeStep.NonNegativeStepBuilder builder = NonNegativeStep.builder();
        if (step != null) {
            builder.setStepDate(step.getStepDate());
            builder.setStepValue(step.getStepValue());
        }
        return builder;
    }
}
