package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_ContractFormation;
import cdm.event.common.functions.Create_Execution;
import cdm.legalagreement.common.*;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaDate;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunFormContractWithLegalAgreement implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_Execution execute;

    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(TradeState tradeState) {
        BusinessEvent executeBusinessEvent = execute.evaluate(tradeState.getTrade().getTradableProduct().getProduct(),
                guard(tradeState.getTrade().getTradableProduct().getPriceQuantity()),
                guard(tradeState.getTrade().getTradableProduct().getCounterparty()),
                guard(tradeState.getTrade().getTradableProduct().getAncillaryParty()),
                guard(tradeState.getTrade().getParty()),
                guard(tradeState.getTrade().getPartyRole()),
                Collections.emptyList(),
                null,
                Optional.ofNullable(tradeState.getTrade().getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
                guard(tradeState.getTrade().getTradeIdentifier()));

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
