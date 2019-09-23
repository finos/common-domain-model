package org.isda.cdm.functions;

import org.isda.cdm.ContractFormation;
import org.isda.cdm.Event;
import org.isda.cdm.Event.EventBuilder;
import org.isda.cdm.EventEffect;
import org.isda.cdm.ExecutionPrimitive;
import org.isda.cdm.Identifier;
import org.isda.cdm.LegalAgreement;
import org.isda.cdm.Party;
import org.isda.cdm.PrimitiveEvent;
import org.isda.cdm.Product;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.metafields.ReferenceWithMetaContract;
import org.isda.cdm.metafields.ReferenceWithMetaExecution;

import com.google.inject.Inject;

public class NewContractEventImpl extends NewContractEvent {
    @Inject
    private IdentifierService identifierService;
    

    @Override
    protected EventBuilder doEvaluate(Product product, Party partyA, Party partyB, LegalAgreement legalAgreement) {
        Identifier id = identifierService.nextType(partyA.getMeta().getExternalKey(), Event.class.getSimpleName());
        identifierService.put(id);

        ExecutionPrimitive executionPrimitive = newExecutionFromProduct.evaluate(product, partyA, partyB);

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

        return eventBuilder;
    }

}
