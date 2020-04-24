package org.isda.cdm.functions.testing;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.functions.Create_Execute;
import org.isda.cdm.functions.Create_FormContract;

import javax.inject.Inject;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunFormContractWithLegalAgreement implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Create_Execute execute;

    @Inject
    Create_FormContract formContract;


    @Override
    public BusinessEvent execute(Contract contract) {
        BusinessEvent executeBusinessEvent = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()));

        LegalAgreement legalAgreement = LegalAgreement.builder()
                .addContractualPartyRef(guard(contract.getParty()))
                .setAgreementDate(DateImpl.of(1994, 12, 01))
                .setAgreementType(LegalAgreementType.builder()
                        .setName(LegalAgreementNameEnum.MASTER_AGREEMENT)
                        .setPublisher(LegalAgreementPublisherEnum.ISDA)
                        .setGoverningLaw(GoverningLawEnum.AS_SPECIFIED_IN_MASTER_AGREEMENT)
                        .build())
                .build();

        return formContract.evaluate(executeBusinessEvent, legalAgreement);
    }


    @Override
    public Class<Contract> getInputType() {
        return Contract.class;
    }

    @Override
    public Class<BusinessEvent> getOutputType() {
        return BusinessEvent.class;
    }
}
