package cdm.product.asset.calculation.functions;

import cdm.base.math.NonNegativeStep;
import cdm.base.math.NonNegativeStepSchedule;

import java.util.List;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class SelectNonNegativeScheduleStepImpl extends SelectNonNegativeScheduleStep {
	@Override
	protected NonNegativeStep.NonNegativeStepBuilder doEvaluate(NonNegativeStepSchedule schedule, Integer stepNo) {
		List<? extends NonNegativeStep> steps = emptyIfNull(schedule.getStep());
		NonNegativeStep step = stepNo < 0 || stepNo >= steps.size() ? null : steps.get(stepNo);

		NonNegativeStep.NonNegativeStepBuilder builder = NonNegativeStep.builder();
		if (step != null) {
			builder.setStepDate(step.getStepDate());
			builder.setStepValue(step.getStepValue());
		}
		return builder;
	}
}
