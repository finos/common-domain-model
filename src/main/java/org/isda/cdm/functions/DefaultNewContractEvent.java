package org.isda.cdm.functions;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaParty;

public class DefaultNewContractEvent extends NewContractEvent {

    public DefaultNewContractEvent(ClassToInstanceMap<RosettaFunction> classRegistry) {
        super(classRegistry);
    }

    @Override
    Event doEvaluate(Product product, Party partyA, Party partyB, LegalAgreement legalAgreement) {
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
                        .addExecutionBuilder(ExecutionPrimitive.builder()
                                .setAfterBuilder(ExecutionState.builder()
                                        .setExecutionBuilder(Execution.builder()
                                                .setProduct(product)))))
                .build();
    }

}
