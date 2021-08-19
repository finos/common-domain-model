package cdm.product.asset.floatingrate.functions;

import cdm.base.math.Step;

import java.util.List;

public class SelectScheduleStepImpl extends SelectScheduleStep {

	@Override
	protected Step.StepBuilder doEvaluate(List<? extends Step> steps, Integer stepNo) {
		Step step = steps == null || stepNo < 0 || stepNo >= steps.size() ? null : steps.get(stepNo);

		Step.StepBuilder builder = Step.builder();
		if (step != null) {
			builder.setStepDate(step.getStepDate());
			builder.setStepValue(step.getStepValue());
		}
		return builder;
	}
}
