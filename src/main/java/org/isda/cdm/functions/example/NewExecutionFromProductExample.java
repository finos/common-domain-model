package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.NewExecutionFromProduct;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

public class NewExecutionFromProductExample extends NewExecutionFromProduct {

    private final IdentifierService identifierService;

    NewExecutionFromProductExample(ClassToInstanceMap<RosettaFunction> classRegistry, IdentifierService identifierService) {
        super(classRegistry);
        this.identifierService = identifierService;
    }

    @Override
    protected ExecutionPrimitive doEvaluate(Product product, Party partyA, Party partyB) {
        Identifier id = identifierService.nextType(partyA.getMeta().getExternalKey(), Execution.class.getSimpleName());

        return ExecutionPrimitive.builder()
                .setAfterBuilder(ExecutionState.builder()
                        .setExecutionBuilder(Execution.builder()
                                .addIdentifier(id)
                                .setProduct(product)
                                .addParty(partyA)
                                .addParty(partyB)))
                .build();
    }
}
