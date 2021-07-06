package org.isda.cdm.functions.testing;

import cdm.event.common.BusinessEvent;
import cdm.event.common.ContractFormationInstruction;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_ContractFormation;
import cdm.event.position.PositionStatusEnum;
import cdm.legalagreement.common.*;
import cdm.legalagreement.common.LegalAgreement.LegalAgreementBuilder;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.lib.records.DateImpl;

import javax.inject.Inject;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunFormContractWithLegalAgreement implements ExecutableFunction<TradeState, BusinessEvent> {

    @Inject
    Create_ContractFormation formContract;


    @Override
    public BusinessEvent execute(TradeState tradeState) {
        LegalAgreementBuilder legalAgreement = LegalAgreement.builder()
                .addContractualPartyValue(guard(tradeState.getTrade().getParty()))
                .setAgreementDate(DateImpl.of(1994, 12, 01))
                .setAgreementType(LegalAgreementType.builder()
                        .setName(LegalAgreementNameEnum.MASTER_AGREEMENT)
                        .setPublisher(LegalAgreementPublisherEnum.ISDA)
                        .setGoverningLaw(GoverningLawEnum.AS_SPECIFIED_IN_MASTER_AGREEMENT)
                        .build());

        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
        tradeStateBuilder.getOrCreateState().setPositionState(PositionStatusEnum.EXECUTED);

        ContractFormationInstruction contractFormationInstruction = ContractFormationInstruction.builder()
                .setExecution(tradeStateBuilder)
                .addLegalAgreement(legalAgreement)
                .build();

        return formContract.evaluate(contractFormationInstruction, new DateImpl(1, 12, 1994));
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
