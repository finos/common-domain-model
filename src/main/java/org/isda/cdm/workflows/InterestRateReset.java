package org.isda.cdm.workflows;

import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.ResetPrimitive.ResetPrimitiveBuilder;
import org.isda.cdm.functions.NewResetPrimitive;
import org.isda.cdm.metafields.FieldWithMetaInformationProviderEnum;
import org.isda.cdm.metafields.FieldWithMetaString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Function;

public class InterestRateReset implements Function<Contract, Workflow> {

    @Inject
    private PostProcessor runner;

    @Inject
    private NewResetPrimitive newResetPrimitive;

    @Override
    public Workflow apply(Contract contract) {
        Date date = DateImpl.of(LocalDate.now());
        ContractState contractState = ContractState.builder().setContract(contract).build();

        ObservationPrimitive observationPrimitive = createObservation(date, 666);

        BusinessEvent observationEvent = BusinessEvent.builder()
                .addPrimitives(PrimitiveEvent.builder().setObservation(observationPrimitive).build()).build();

        ResetPrimitiveBuilder resetPrimitiveBuilder = newResetPrimitive.evaluate(contractState, observationPrimitive, date).toBuilder();

        ResetPrimitive resetPrimitive = resetPrimitiveBuilder.build();

        BusinessEvent resetEvent = BusinessEvent.builder()
                .addPrimitives(PrimitiveEvent.builder().setReset(resetPrimitive).build()).build();

        TransferPrimitive transferCashPrimitive = TransferPrimitive.builder().setStatus(TransferStatusEnum.INSTRUCTED)
                .setSettlementDate(AdjustableOrAdjustedOrRelativeDate.builder().setUnadjustedDate(date).build())
                .addCashTransfer(CashTransferComponent.builder().setAmount(Money.builder()
                        .setAmount(BigDecimal.valueOf(100000))
                        .setCurrency(FieldWithMetaString.builder().setValue("USD").build())
                        .build())
                        .setPayerReceiver(null).build())
                .build();

        BusinessEvent transferCashEvent = BusinessEvent.builder()
                .addPrimitives(PrimitiveEvent.builder().setTransfer(transferCashPrimitive).build()).build();

        return WorkflowUtils.buildWorkflow(runner, observationEvent, resetEvent, transferCashEvent);
    }

    private ObservationPrimitive createObservation(Date date, int observation) {
        return ObservationPrimitive.builder()
                .setObservation(BigDecimal.valueOf(observation))
                .setDate(date)
                .setSource(ObservationSource.builder()
                        .setInformationSource(InformationSource.builder()
                                .setSourceProvider(FieldWithMetaInformationProviderEnum.builder()
                                        .setValue(InformationProviderEnum.BLOOMBERG)
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
