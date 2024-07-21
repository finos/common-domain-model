package org.isda.cdm.functions;

import com.rosetta.model.lib.expression.ComparisonResult;
import com.rosetta.model.lib.functions.ConditionValidator;

import java.util.function.Supplier;

public class NoOpConditionValidator implements ConditionValidator {
    @Override
    public void validate(Supplier<ComparisonResult> supplier, String s) {
        // do nothing
    }
}
