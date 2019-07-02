package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.NewExecutionFromProduct;

public class NewExecutionFromProductExample extends NewExecutionFromProduct {

    protected NewExecutionFromProductExample(ClassToInstanceMap<RosettaFunction> classRegistry) {
        super(classRegistry);
    }

    @Override
    protected ExecutionPrimitive doEvaluate(Product product, Party partyA, Party partyB) {
        return ExecutionPrimitive.builder()
                .setAfterBuilder(ExecutionState.builder()
                    .setExecutionBuilder(Execution.builder()
                        .setProduct(product)
                        .addParty(partyA)
                        .addParty(partyB)))
                .build();
    }
}
