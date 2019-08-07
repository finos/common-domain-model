package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.NewContractEvent;
import org.isda.cdm.functions.NewContractFormationFromExecution;
import org.isda.cdm.functions.NewExecutionFromProduct;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.metafields.ReferenceWithMetaContract;
import org.isda.cdm.metafields.ReferenceWithMetaExecution;

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

        Event.EventBuilder eventBuilder = Event.builder()
                .addEventIdentifier(id)
                .addParty(partyA)
                .addParty(partyB)
                .setPrimitiveBuilder(PrimitiveEvent.builder()
                        .addExecution(executionPrimitive)
                        .addContractFormation(contractFormation))
                // In our example, we manually resolve references
                .setEventEffectBuilder(EventEffect.builder()
                        .addContractBuilder(ReferenceWithMetaContract.builder().setValue(contractFormation.getAfter().getContract()))
                        .addExecutionBuilder(ReferenceWithMetaExecution.builder().setValue(executionPrimitive.getAfter().getExecution()))
                        .addEffectedExecutionBuilder(ReferenceWithMetaExecution.builder().setValue(contractFormation.getBefore().getExecution())));

        //RosettaFunctionExamples.getInstance().getPostProcessor().forEach(step -> step.runProcessStep(Event.class, eventBuilder));

        return eventBuilder.build();
    }

}
