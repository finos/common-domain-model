package org.isda.cdm.functions;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;

public class DefaultNewContractEvent extends NewContractEvent {

    public DefaultNewContractEvent(ClassToInstanceMap<RosettaFunction> classRegistry) {
        super(classRegistry);
    }

    @Override
    Event doEvaluate(Product product, Party partyA, Party partyB, LegalAgreement legalAgreement) {
        return Event.builder()
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
