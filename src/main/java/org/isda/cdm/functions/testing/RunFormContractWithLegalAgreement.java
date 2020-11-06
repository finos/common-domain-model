package org.isda.cdm.functions.testing;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
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
                guard(contract.getTradableProduct().getCounterparties()),
                guard(contract.getTradableProduct().getRelatedParties()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList(), 
                null, 
                Optional.ofNullable(contract.getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null), 
                guard(contract.getContractIdentifier()));

        List<Counterparty.CounterpartyBuilder> counterparties = guard(contract.getTradableProduct().getCounterparties())
                .stream()
                .map(Counterparty::toBuilder)
                .collect(Collectors.toList());
        counterparties.forEach(c ->
                c.setPartyReference(ReferenceWithMetaParty.builder()
                        .setValueBuilder(extractParty(guard(contract.getParty()), c.getPartyReference().getGlobalReference()))
                        .build()));

        LegalAgreement legalAgreement = LegalAgreement.builder()
                .addContractualParty(counterparties.stream().map(Counterparty.CounterpartyBuilder::build).collect(Collectors.toList()))
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

    private Party.PartyBuilder extractParty(List<Party> parties, String globalKey) {
        return parties.stream()
                .map(Party::toBuilder)
                .filter(p -> p.getOrCreateMeta().getGlobalKey().equals(globalKey))
                .findFirst().orElse(null);
    }
}
