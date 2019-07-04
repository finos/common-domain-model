package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.NewContractEvent;
import org.isda.cdm.functions.NewContractFormationFromExecution;
import org.isda.cdm.functions.NewExecutionFromProduct;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

public class NewContractEventExample extends NewContractEvent {

    private final IdentifierService identifierService;

    NewContractEventExample(ClassToInstanceMap<RosettaFunction> classRegistry, IdentifierService identifierService) {
        super(classRegistry);
        this.identifierService = identifierService;
    }

    @Override
    protected Event doEvaluate(Product product, Party partyA, Party partyB, LegalAgreement legalAgreement) {
        Identifier id = identifierService.nextType(partyA.getMeta().getExternalKey(), Event.class.getSimpleName());
        identifierService.put(id);

        NewExecutionFromProduct newExecutionFromProduct = classRegistry.getInstance(NewExecutionFromProduct.class);
        ExecutionPrimitive executionPrimitive = newExecutionFromProduct.evaluate(product, partyA, partyB);

        NewContractFormationFromExecution newContractFormationFromExecution = classRegistry.getInstance(NewContractFormationFromExecution.class);
        ContractFormation contractFormation = newContractFormationFromExecution.evaluate(executionPrimitive.getAfter(), partyA, partyB, legalAgreement);

        return Event.builder()
                .addEventIdentifier(id)
                .addParty(partyA)
                .addParty(partyB)
                .setPrimitiveBuilder(PrimitiveEvent.builder()
                        .addExecution(executionPrimitive)
                        .addContractFormation(contractFormation))
                .build();
    }

}
