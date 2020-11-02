package org.isda.cdm.functions.testing;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaDate;

import cdm.event.common.BusinessEvent;
import cdm.event.common.functions.Create_ContractFormation;
import cdm.event.common.functions.Create_Execution;
import cdm.legalagreement.common.GoverningLawEnum;
import cdm.legalagreement.common.LegalAgreement;
import cdm.legalagreement.common.LegalAgreementNameEnum;
import cdm.legalagreement.common.LegalAgreementPublisherEnum;
import cdm.legalagreement.common.LegalAgreementType;
import cdm.legalagreement.contract.Contract;

public class RunFormContractWithLegalAgreement implements ExecutableFunction<Contract, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(Contract contract) {
        BusinessEvent executeBusinessEvent = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getTradableProduct().getCounterparty()),
                guard(contract.getTradableProduct().getAncillaryRole()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList(), 
                null, 
                Optional.ofNullable(contract.getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null), 
                guard(contract.getContractIdentifier()));

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
