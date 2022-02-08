package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.ContractFormationInstruction;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_ContractFormation;
import cdm.event.position.PositionStatusEnum;
import cdm.legalagreement.common.*;
import cdm.legalagreement.common.LegalAgreement.LegalAgreementBuilder;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.Date;

import javax.inject.Inject;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunFormContractWithLegalAgreement implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(TradeState tradeState) {
        LegalAgreementBuilder legalAgreement = LegalAgreement.builder()
                .addContractualPartyValue(guard(tradeState.getTrade().getParty()))
                .setAgreementDate(Date.of(1994, 12, 01))
                .setAgreementType(LegalAgreementType.builder()
                        .setName(LegalAgreementNameEnum.MASTER_AGREEMENT)
                        .setPublisher(LegalAgreementPublisherEnum.ISDA)
                        .setGoverningLaw(GoverningLawEnum.AS_SPECIFIED_IN_MASTER_AGREEMENT)
                        .build());

        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
        tradeStateBuilder.getOrCreateState().setPositionState(PositionStatusEnum.EXECUTED);

        ContractFormationInstruction contractFormationInstruction = ContractFormationInstruction.builder()
                .addLegalAgreement(legalAgreement)
                .build();

        return formContract.evaluate(contractFormationInstruction, tradeStateBuilder, Date.of(1994, 12, 1));
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
