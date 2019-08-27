package org.isda.cdm.functions;

import static com.google.common.collect.Iterables.getOnlyElement;

import java.util.List;

import org.isda.cdm.ExecutionState;
import org.isda.cdm.functions.ExtractSingleExecutionState;

public class ExtractSingleExecutionStateImpl extends ExtractSingleExecutionState {

	@Override
	protected ExecutionState doEvaluate(List<ExecutionState> executionStates) {
		return getOnlyElement(executionStates);
	}
}
