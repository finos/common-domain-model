package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.ExecutionState;
import org.isda.cdm.functions.ExtractSingleExecutionState;

import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;

public class ExtractSingleExecutionStateExample extends ExtractSingleExecutionState {

	ExtractSingleExecutionStateExample(ClassToInstanceMap<RosettaFunction> classRegistry) {
		super(classRegistry);
	}

	@Override
	protected ExecutionState doEvaluate(List<ExecutionState> executionStates) {
		return getOnlyElement(executionStates);
	}
}
