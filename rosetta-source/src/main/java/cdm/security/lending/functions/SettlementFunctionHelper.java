package cdm.security.lending.functions;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.MeasureBase;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.event.common.*;
import cdm.event.common.functions.CalculateTransfer;
import cdm.event.common.functions.Create_BusinessEvent;
import cdm.event.common.functions.Create_Return;
import cdm.event.workflow.EventInstruction;
import cdm.product.template.*;
import com.google.common.collect.Iterables;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;

import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SettlementFunctionHelper {

    @Inject
    Create_Return create_Return;

    @Inject
    CalculateTransfer calculateTransfer;

    @Inject
    PostProcessor postProcessor;

    @Inject
    Create_BusinessEvent create_businessEvent;

    public BusinessEvent createExecution(ExecutionInstruction executionInstruction, Date eventDate) {
        ExecutionInstruction executionInstructionWithRefs = postProcess(ExecutionInstruction.class, executionInstruction);

        List<Instruction> instructions = Arrays.asList(Instruction.builder()
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setExecution(executionInstructionWithRefs))
                .build());

        BusinessEvent businessEvent = create_businessEvent.evaluate(instructions, null, eventDate, null);
        return postProcess(BusinessEvent.class, businessEvent);
    }

    public BusinessEvent createReturn(TradeState tradeState, ReturnInstruction returnInstruction, Date returnDate) {
        return create_Return.evaluate(tradeState, returnInstruction, returnDate);
    }

    public BusinessEvent createTransferBusinessEvent(EventInstruction transferInstruction) {
        BusinessEvent transferBusinessEvent = create_businessEvent.evaluate(transferInstruction.getInstruction(),
                transferInstruction.getIntent(),
                transferInstruction.getEventDate(),
                transferInstruction.getEffectiveDate());
        return postProcess(BusinessEvent.class, transferBusinessEvent);
    }


    public LocalDate nearSettlementDate(BusinessEvent businessEvent) {
        return settlementDate(businessEvent, x -> x.get(0));
    }

    public LocalDate farSettlementDate(BusinessEvent businessEvent) {
        return settlementDate(businessEvent, Iterables::getLast);
    }

    private LocalDate settlementDate(BusinessEvent businessEvent, Function<List<? extends AssetLeg>, AssetLeg> assetLegSelector) {
        return Optional.ofNullable(Iterables.getLast(getAssetPayouts(businessEvent)))
                .map(Payout::getAssetPayout)
                .map(AssetPayout::getAssetLeg)
                .map(assetLegSelector)
                .map(AssetLeg::getSettlementDate)
                .map(AdjustableOrRelativeDate::getAdjustableDate)
                .map(AdjustableDate::getAdjustedDate)
                .map(FieldWithMetaDate::getValue)
                .map(Date::toLocalDate)
                .orElse(LocalDate.now());
    }

    public EventInstruction createTransferInstruction(BusinessEvent executionBusinessEvent, LocalDate transferDate) {
        Payout payout = Iterables.getLast(getAssetPayouts(executionBusinessEvent));
        TradeState before = getAfterState(executionBusinessEvent).orElse(null);
        CalculateTransferInstruction calculateTransferInstruction =
                CalculateTransferInstruction.builder()
                        .setTradeState(before)
                        .setPayoutValue(payout)
                        .setPayerReceiver(getPayerReceiver(payout).orElse(null))
                        .setDate(Date.of(transferDate))
                        .build();
        List<? extends Transfer> transfers = calculateTransfer.evaluate(calculateTransferInstruction);
        return createTransferInstruction(transfers, transferDate, before);
    }

    public EventInstruction createReturnTransferInstruction(BusinessEvent executionBusinessEvent,
                                                            List<? extends Quantity> quantities,
                                                            LocalDate transferDate) {
        Payout payout = Iterables.getLast(getAssetPayouts(executionBusinessEvent));
        Quantity shareQuantity = getShareQuantity(quantities);
        TradeState before = getAfterState(executionBusinessEvent).orElse(null);
        CalculateTransferInstruction calculateTransferInstruction =
                CalculateTransferInstruction.builder()
                        .setTradeState(before)
                        .setPayoutValue(payout)
                        .setPayerReceiver(getReturnPayerReceiver(payout).orElse(null))
                        .setQuantity(shareQuantity)
                        .setDate(Date.of(transferDate))
                        .build();
        List<? extends Transfer> transfers = calculateTransfer.evaluate(calculateTransferInstruction);
        return createTransferInstruction(transfers, transferDate, before);
    }

    private EventInstruction createTransferInstruction(List<? extends Transfer> transfers, LocalDate transferDate, TradeState before) {
        List<TransferState> transferStates = transfers.stream()
                .map(t -> TransferState.builder().setTransfer(t).build())
                .collect(Collectors.toList());
        return EventInstruction.builder()
                .addInstruction(Instruction.builder()
                        .setBeforeValue(before)
                        .setPrimitiveInstruction(PrimitiveInstruction.builder()
                                .setTransfer(TransferInstruction.builder()
                                        .setTransferState(transferStates))))
                .setEventDate(Date.of(transferDate));
    }

    private Quantity getShareQuantity(List<? extends Quantity> quantities) {
        List<Quantity> quantitiesO = quantities.stream()
                .filter(q -> Optional.ofNullable(q)
                        .map(MeasureBase::getUnit)
                        .map(UnitType::getFinancialUnit)
                        .filter(u -> u == FinancialUnitEnum.SHARE).isPresent())
                .collect(Collectors.toList());
        return Iterables.getLast(quantitiesO);
    }

    private Optional<PayerReceiver> getPayerReceiver(Payout payout) {
        return Optional.ofNullable(payout)
                .map(Payout::getAssetPayout)
                .map(AssetPayout::getPayerReceiver);
    }

    private Optional<PayerReceiver> getReturnPayerReceiver(Payout payout) {
        return getPayerReceiver(payout)
                .map(this::invert);
    }

    private PayerReceiver invert(PayerReceiver payerReceiver) {
        return payerReceiver.toBuilder()
                .setPayer(payerReceiver.getReceiver())
                .setReceiver(payerReceiver.getPayer())
                .build();
    }

    private List<? extends Payout> getPayouts(BusinessEvent executionBusinessEvent) {
        return getAfterState(executionBusinessEvent)
                .map(TradeState::getTrade)
                .map(TradableProduct::getProduct)
                .map(NonTransferableProduct::getEconomicTerms)
                .map(EconomicTerms::getPayout)
                .orElse(Collections.emptyList());
    }

    private List<? extends Payout> getAssetPayouts(BusinessEvent executionBusinessEvent) {
        return getPayouts(executionBusinessEvent).stream()
                .filter(p -> p.getAssetPayout() != null)
                .collect(Collectors.toList());
    }

    private Optional<TradeState> getAfterState(BusinessEvent executionBusinessEvent) {
        return Optional.of(executionBusinessEvent)
                .map(BusinessEvent::getAfter)
                .map(Iterables::getLast);
    }

    private <T extends RosettaModelObject> T postProcess(Class<T> modelType, T modelObject) {
        return modelType.cast(postProcessor.postProcess(modelType, modelObject.toBuilder().prune()).build());
    }
}
