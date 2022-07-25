package org.isda.cdm.function;

import com.rosetta.model.lib.expression.ComparisonResult;
import com.rosetta.model.lib.functions.ConditionValidator;

import java.util.function.Supplier;

public class DefaultConditionValidator implements ConditionValidator {
    @Override
    public void validate(Supplier<ComparisonResult> condition, String description) {
        if (!condition.get().get()) {
            throw new ConditionException(description);
        }
    }
}
