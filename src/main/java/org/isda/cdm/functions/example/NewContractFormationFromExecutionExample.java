package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.*;
import org.isda.cdm.functions.NewContractFormationFromExecution;
import org.isda.cdm.metafields.ReferenceWithMetaLegalAgreement;

public class NewContractFormationFromExecutionExample extends NewContractFormationFromExecution {


    protected NewContractFormationFromExecutionExample(ClassToInstanceMap<RosettaFunction> classRegistry) {
        super(classRegistry);
    }

    @Override
    protected ContractFormation doEvaluate(ExecutionState executionState, Party partyA, Party partyB, LegalAgreement legalAgreement) {
        ContractualProduct contractualProduct = executionState.getExecution().getProduct().getContractualProduct();

        return ContractFormation.builder()
                .setBefore(executionState)
                .setAfterBuilder(PostInceptionState.builder()
                    .setContractBuilder(Contract.builder()
                        .addParty(partyA)
                        .addParty(partyB)
                        .setContractualProduct(contractualProduct)
                        .setDocumentationBuilder(Documentation.builder()
                            .addLegalAgreementBuilder(ReferenceWithMetaLegalAgreement.builder()
                                .setValue(legalAgreement)
                                .setGlobalReference(legalAgreement.getMeta().getGlobalKey())))))
                .build();
    }
}
