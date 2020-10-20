package org.isda.cdm.functions.testing;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import org.isda.cdm.*;
import org.isda.cdm.functions.Create_ContractFormation;
import org.isda.cdm.functions.Create_Execution;

import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;

public class RunFormContractWithLegalAgreement implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(TradeState tradeState) {
        BusinessEvent executeBusinessEvent = execute.evaluate(tradeState.getTrade().getTradableProduct().getProduct(),
                guard(tradeState.getTrade().getTradableProduct().getQuantityNotation()),
                guard(tradeState.getTrade().getTradableProduct().getPriceNotation()),
                guard(tradeState.getTrade().getTradableProduct().getCounterparties()),
                guard(tradeState.getTrade().getParty()),
                guard(tradeState.getTrade().getPartyRole()),
                Collections.emptyList(), 
                null, 
                Optional.ofNullable(tradeState.getTrade().getTradeDate()).map(TradeDate::getDate).orElse(null),
                guard(tradeState.getTrade().getIdentifier()));

        LegalAgreement legalAgreement = LegalAgreement.builder()
                .addContractualPartyRef(guard(tradeState.getTrade().getParty()))
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
    public Class<TradeState> getInputType() {
        return TradeState.class;
    }

    @Override
    public Class<BusinessEvent> getOutputType() {
        return BusinessEvent.class;
    }
}
