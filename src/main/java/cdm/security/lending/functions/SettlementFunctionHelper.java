package cdm.security.lending.functions;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.MeasureBase;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.event.common.*;
import cdm.event.common.functions.Create_Execution;
import cdm.event.common.functions.Create_Return;
import cdm.event.common.functions.Create_Transfer;
import cdm.event.workflow.WorkflowStep;
import cdm.product.template.*;
import com.google.common.collect.Iterables;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaDate;
import org.isda.cdm.functions.testing.LineageUtils;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SettlementFunctionHelper {

    @Inject
    Create_Execution create_execution;

    @Inject
    Create_Return create_Return;

    @Inject
    Create_Transfer create_transfer;

    @Inject
    LineageUtils lineageUtils;

    public BusinessEvent createExecution(ExecutionInstruction executionInstruction) {
        ExecutionInstruction executionInstructionWithRefs = lineageUtils
                .withGlobalReference(ExecutionInstruction.class, executionInstruction);

        BusinessEvent businessEvent = create_execution.evaluate(executionInstructionWithRefs);
        return lineageUtils.withGlobalReference(BusinessEvent.class, businessEvent);
    }

    public BusinessEvent createReturn(TradeState tradeState, ReturnInstruction returnInstruction, Date returnDate) {
        BusinessEvent returnBusinessEvent = create_Return.evaluate(tradeState, returnInstruction, returnDate);
        return lineageUtils.withGlobalReference(BusinessEvent.class, returnBusinessEvent);
    }

    public BusinessEvent createTransferBusinessEvent(WorkflowStep executionWorkflowStep, WorkflowStep proposedTransferWorkflowStep, LocalDate settlementDate) {
        BusinessEvent transferBusinessEvent = create_transfer.evaluate(
                getAfterState(executionWorkflowStep.getBusinessEvent()).orElse(null),
                proposedTransferWorkflowStep.getProposedInstruction().getTransfer(),
                DateImpl.of(settlementDate));
        return lineageUtils.withGlobalReference(BusinessEvent.class, transferBusinessEvent);
    }


    public LocalDate nearSettlementDate(BusinessEvent businessEvent) {
        return settlementDate(businessEvent, x -> x.get(0));
    }

    public LocalDate farSettlementDate(BusinessEvent businessEvent) {
        return settlementDate(businessEvent, Iterables::getLast);
    }

    private LocalDate settlementDate(BusinessEvent businessEvent, Function<List<? extends SecurityFinanceLeg>, SecurityFinanceLeg> financeLegSelector) {
        return getSecurityPayout(businessEvent)
                .map(Payout::getSecurityFinancePayout)
                .filter(x -> !x.isEmpty()).map(Iterables::getLast)
                .map(SecurityFinancePayout::getSecurityFinanceLeg)
                .filter(x -> !x.isEmpty()).map(financeLegSelector::apply)
                .map(SecurityFinanceLeg::getSettlementDate)
                .map(AdjustableOrRelativeDate::getAdjustableDate)
                .map(AdjustableDate::getAdjustedDate)
                .map(FieldWithMetaDate::getValue)
                .map(Date::toLocalDate)
                .orElse(LocalDate.now());
    }

    public ZonedDateTime dateTime(LocalDate tradeDate, int hour, int minute) {
        return ZonedDateTime.of(tradeDate, LocalTime.of(hour, minute), ZoneOffset.UTC.normalized());
    }

    public Instruction createTransferInstruction(BusinessEvent executionBusinessEvent) {
        Payout payout = getSecurityPayout(executionBusinessEvent).orElse(null);
        return Instruction.builder()
                .setInstructionFunction(Create_Transfer.class.getSimpleName())
                .setTransfer(TransferInstruction.builder()
                        .setPayoutValue(payout)
                        .setPayerReceiver(getPayerReceiver(payout).orElse(null))
                        .build()).build();
    }

    public Instruction createReturnTransferInstruction(BusinessEvent executionBusinessEvent, List<? extends Quantity> quantities) {
        Payout payout = getSecurityPayout(executionBusinessEvent).orElse(null);
        Quantity shareQuantity = getShareQuantity(quantities);
        return Instruction.builder()
                .setInstructionFunction(Create_Transfer.class.getSimpleName())
                .setTransfer(TransferInstruction.builder()
                        .setPayoutValue(payout)
                        .setPayerReceiver(getReturnPayerReceiver(payout).orElse(null))
                        .setQuantity(shareQuantity)
                        .build()).build();
    }

    private Quantity getShareQuantity(List<? extends Quantity> quantities) {
        List<Quantity> quantitiesO = quantities.stream()
                .filter(q -> Optional.ofNullable(q)
                        .map(MeasureBase::getUnitOfAmount)
                        .map(UnitType::getFinancialUnit)
                        .filter(u -> u == FinancialUnitEnum.SHARE).isPresent())
                .collect(Collectors.toList());
        return Iterables.getLast(quantitiesO);
    }

    private Optional<PayerReceiver> getPayerReceiver(Payout payout) {
        return Optional.ofNullable(payout)
                .map(Payout::getSecurityFinancePayout)
                .filter(x -> !x.isEmpty()).map(Iterables::getLast)
                .map(SecurityFinancePayout::getPayerReceiver);
    }

    private Optional<PayerReceiver> getReturnPayerReceiver(Payout payout) {
        return Optional.ofNullable(payout)
                .map(Payout::getSecurityFinancePayout)
                .filter(x -> !x.isEmpty()).map(Iterables::getLast)
                .map(x -> invert(x.getPayerReceiver()));
    }

    private PayerReceiver invert(PayerReceiver payerReceiver) {
        return payerReceiver.toBuilder()
                .setPayer(payerReceiver.getReceiver())
                .setReceiver(payerReceiver.getPayer())
                .build();
    }

    private Optional<Payout> getPayout(BusinessEvent executionBusinessEvent) {
        return getAfterState(executionBusinessEvent)
                .map(TradeState::getTrade)
                .map(Trade::getTradableProduct)
                .map(TradableProduct::getProduct)
                .map(Product::getContractualProduct)
                .map(ContractualProduct::getEconomicTerms)
                .map(EconomicTerms::getPayout);
    }

    private Optional<Payout> getSecurityPayout(BusinessEvent executionBusinessEvent) {
        return getPayout(executionBusinessEvent)
                .map(Payout::getSecurityFinancePayout)
                .map(securityFinancePayouts -> Payout.builder().setSecurityFinancePayout(securityFinancePayouts)
                        .build());
    }

    private Optional<TradeState> getAfterState(BusinessEvent executionBusinessEvent) {
        return Optional.of(executionBusinessEvent)
                .map(BusinessEvent::getPrimitives)
                .filter(x -> !x.isEmpty()).map(Iterables::getLast)
                .flatMap(this::getTradeState);
    }

    private Optional<TradeState> getTradeState(PrimitiveEvent p) {
        if (p.getExecution() != null)
            return Optional.of(p.getExecution()).map(ExecutionPrimitive::getAfter);
        else if (p.getTransfer() != null)
            return Optional.of(p.getTransfer()).map(TransferPrimitive::getAfter);
        else if (p.getQuantityChange() != null)
            return Optional.of(p.getQuantityChange()).map(QuantityChangePrimitive::getAfter);

        return Optional.empty();
    }

}
