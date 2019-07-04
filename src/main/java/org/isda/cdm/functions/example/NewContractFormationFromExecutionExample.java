package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.NewContractFormationFromExecution;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.metafields.ReferenceWithMetaLegalAgreement;

public class NewContractFormationFromExecutionExample extends NewContractFormationFromExecution {

    private final IdentifierService identifierService;

    NewContractFormationFromExecutionExample(ClassToInstanceMap<RosettaFunction> classRegistry, IdentifierService identifierService) {
        super(classRegistry);
        this.identifierService = identifierService;
    }

    @Override
    protected ContractFormation doEvaluate(ExecutionState executionState, Party partyA, Party partyB, LegalAgreement legalAgreement) {
        Identifier id = identifierService.next(partyA.getMeta().getExternalKey(), Contract.class);
        ContractualProduct contractualProduct = executionState.getExecution().getProduct().getContractualProduct();

        return ContractFormation.builder()
                .setBefore(executionState)
                .setAfterBuilder(PostInceptionState.builder()
                        .setContractBuilder(Contract.builder()
                                .addContractIdentifier(id)
                                .setContractualProduct(contractualProduct)
                                .setDocumentationBuilder(Documentation.builder()
                                        .addLegalAgreementBuilder(ReferenceWithMetaLegalAgreement.builder()
                                                .setValue(legalAgreement)
                                                .setGlobalReference(legalAgreement.getMeta().getGlobalKey())))))
                .build();
    }
}
