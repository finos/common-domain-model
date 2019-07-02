package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.NewContractEvent;
import org.isda.cdm.functions.NewContractFormationFromExecution;
import org.isda.cdm.functions.NewExecutionFromProduct;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

public class NewContractEventExample extends NewContractEvent {

    NewContractEventExample(ClassToInstanceMap<RosettaFunction> classRegistry) {
        super(classRegistry);
    }

    @Override
    protected Event doEvaluate(Product product, Party partyA, Party partyB, LegalAgreement legalAgreement) {

        NewExecutionFromProduct newExecutionFromProduct = classRegistry.getInstance(NewExecutionFromProduct.class);
        ExecutionPrimitive executionPrimitive = newExecutionFromProduct.evaluate(product, partyA, partyB);

        NewContractFormationFromExecution newContractFormationFromExecution = classRegistry.getInstance(NewContractFormationFromExecution.class);
        ContractFormation contractFormation = newContractFormationFromExecution.evaluate(executionPrimitive.getAfter(), partyA, partyB, legalAgreement);


        return Event.builder()
                .addEventIdentifierBuilder(Identifier.builder()
                    .addAssignedIdentifierBuilder(AssignedIdentifier.builder()
                        .setIdentifier(FieldWithMetaString.builder()  // Implementors will likely use an Identifier Service to produce this value
                                .setValue(partyA.getMeta().getGlobalKey())
                                .build())
                        .setVersion(1))
                    .setIssuer(FieldWithMetaString.builder()
                            .setValue(partyA.getMeta().getGlobalKey())
                            .build())
                    .setIssuerReferenceBuilder(ReferenceWithMetaParty.builder()
                        .setExternalReference(partyA.getMeta().getExternalKey())))
                .addParty(partyA)
                .addParty(partyB)
                .setPrimitiveBuilder(PrimitiveEvent.builder()
                        .addExecution(executionPrimitive)
                    .addContractFormation(contractFormation))
                .build();
    }

}
