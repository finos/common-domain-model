package cdm.product.common.settlement.processor;

import cdm.base.datetime.AdjustableDate;
import cdm.base.math.UnitType;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.observable.asset.Money;
import cdm.product.common.settlement.PrincipalPayment;
import cdm.product.common.settlement.PrincipalPayments;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static cdm.product.common.settlement.PrincipalPayment.PrincipalPaymentBuilder;
import static cdm.product.common.settlement.PrincipalPaymentSchedule.PrincipalPaymentScheduleBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;

public class PrincipalPaymentScheduleMappingProcessor extends MappingProcessor {

    public PrincipalPaymentScheduleMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        PrincipalPaymentScheduleBuilder principalPaymentScheduleBuilder = (PrincipalPaymentScheduleBuilder) builder;
        PrincipalPayments principalPaymentParent = (PrincipalPayments) parent;

        boolean initialPayment = Optional.ofNullable(principalPaymentParent.getInitialPayment()).orElse(false);
        boolean finalPayment = Optional.ofNullable(principalPaymentParent.getFinalPayment()).orElse(false);
        
        LinkedList<PrincipalPayment> principalPayments = getPrincipalPayments(synonymPath);
        if (principalPayments.isEmpty()) {
            return;
        }
        if (initialPayment) {
            PrincipalPayment firstPrincipalPayment = principalPayments.removeFirst();
            principalPaymentScheduleBuilder.setInitialPrincipalPayment(firstPrincipalPayment);;
        }
        if (principalPayments.isEmpty()) {
            return;
        }
        if (finalPayment) {
            PrincipalPayment finalPrincipalPayment = principalPayments.getLast();
            principalPaymentScheduleBuilder.setFinalPrincipalPayment(finalPrincipalPayment);
        }
    }


    private LinkedList<PrincipalPayment> getPrincipalPayments(Path synonymPath) {
        LinkedList<PrincipalPayment> principalPayments = new LinkedList<>();
        for (int i = 0; i < 2; i++) {
            getPrincipalPayment(synonymPath.addElement("principalExchange", i), synonymPath.getParent())
                    .ifPresent(principalPayments::add);
        }
        principalPayments
                .sort(Comparator.comparing(this::getAdjustedOrUnadjustedDate));

        return principalPayments;
    }


    private LocalDate getAdjustedOrUnadjustedDate(PrincipalPayment o) {
        return Optional.ofNullable(o.getPrincipalPaymentDate())
                .map(AdjustableDate::getAdjustedDate)
                .map(FieldWithMetaDate::getValue)
                .map(Date::toLocalDate)
                .orElse(Optional.ofNullable(o.getPrincipalPaymentDate())
                        .map(AdjustableDate::getUnadjustedDate)
                        .map(Date::toLocalDate)
                        .orElse(LocalDate.MIN));
    }

    private Optional<PrincipalPayment> getPrincipalPayment(Path principalExchangePath, Path swapStreamPath) {
        PrincipalPaymentBuilder principalPaymentBuilder = PrincipalPayment.builder();

        setValueAndUpdateMappings(principalExchangePath.addElement("principalExchangeAmount"),
                xmlValue -> {
                    BigDecimal amount = new BigDecimal(xmlValue);

                    Money.MoneyBuilder moneyBuilder = principalPaymentBuilder.getOrCreatePrincipalAmount();
                    moneyBuilder.setValue(amount.abs());

                    setCurrency(swapStreamPath, moneyBuilder);

                    //principalPaymentBuilder.getOrCreatePrincipalAmount()
                    //getNonNullMappedValue()


                    // set Payer/Receiver based on whether the payment amount is +/-
                    setPayerReceiver(swapStreamPath, principalPaymentBuilder, amount);
                });

        setValueAndUpdateMappings(principalExchangePath.addElement("unadjustedPrincipalExchangeDate"),
                xmlValue ->
                        principalPaymentBuilder
                                .getOrCreatePrincipalPaymentDate()
                                .setUnadjustedDate(Date.parse(xmlValue)));

        setValueAndUpdateMappings(principalExchangePath.addElement("adjustedPrincipalExchangeDate"),
                xmlValue ->
                        principalPaymentBuilder
                                .getOrCreatePrincipalPaymentDate()
                                .getOrCreateAdjustedDate()
                                .setValue(Date.parse(xmlValue)));

        return principalPaymentBuilder.hasData() ? Optional.of(principalPaymentBuilder) : Optional.empty();
    }

    private void setCurrency(Path swapStreamPath, Money.MoneyBuilder moneyBuilder) {
        Optional<String> settlementCurrency =
                getNonNullMappedValue(swapStreamPath
                                .addElement("settlementProvision")
                                .addElement("settlementCurrency"),
                        getMappings());
        if (settlementCurrency.isPresent()) {
            moneyBuilder.setUnit(UnitType.builder().setCurrencyValue(settlementCurrency.get()));
            return;
        }
        Optional<String> notionalCurrency =
                getNonNullMappedValue(swapStreamPath
                                .addElement("calculationPeriodAmount")
                                .addElement("calculation")
                                .addElement("notionalSchedule")
                                .addElement("notionalStepSchedule")
                                .addElement("currency"),
                        getMappings());
        if (notionalCurrency.isPresent()) {
            moneyBuilder.setUnit(UnitType.builder().setCurrencyValue(notionalCurrency.get()));
            return;
        }
    }

    private void setPayerReceiver(Path swapStreamPath, PrincipalPaymentBuilder principalPaymentBuilder, BigDecimal paymentAmount) {
        Path payerPath = swapStreamPath.addElement("payerPartyReference");
        Path receiverPath = swapStreamPath.addElement("receiverPartyReference");

        PayerReceiver.PayerReceiverBuilder payerReceiver = principalPaymentBuilder.getOrCreatePayerReceiver();
        if (paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
            setCounterpartyRoleEnum(payerPath, payerReceiver::setPayer);
            setCounterpartyRoleEnum(receiverPath, payerReceiver::setReceiver);
        } else {
            setCounterpartyRoleEnum(payerPath, payerReceiver::setReceiver);
            setCounterpartyRoleEnum(receiverPath, payerReceiver::setPayer);
        }
    }

    private void setCounterpartyRoleEnum(Path synonymPath, Consumer<CounterpartyRoleEnum> setter) {
        PartyMappingHelper.getInstance(getContext())
                .ifPresent(helper ->
                        helper.setCounterpartyRoleEnum(getModelPath(), synonymPath, setter));
    }
}
