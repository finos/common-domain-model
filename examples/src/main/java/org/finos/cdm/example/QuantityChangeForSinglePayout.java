package org.finos.cdm.example;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.Asset;
import cdm.base.staticdata.asset.common.AssetIdTypeEnum;
import cdm.base.staticdata.asset.common.AssetIdentifier;
import cdm.base.staticdata.asset.common.AssetTypeEnum;
import cdm.base.staticdata.asset.common.Instrument;
import cdm.base.staticdata.asset.common.Security;
import cdm.base.staticdata.asset.common.SecurityTypeEnum;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.party.AssetAncillaryPartyRoleEnum;
import cdm.base.staticdata.party.BuyerSeller;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.EventIntentEnum;
import cdm.event.common.Instruction;
import cdm.event.common.PrimitiveInstruction;
import cdm.event.common.QuantityChangeInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeIdentifier;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_BusinessEvent;
import cdm.observable.asset.Observable;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaObservable;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.observable.asset.metafields.ReferenceWithMetaObservable;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import cdm.product.common.settlement.SettlementTerms;
import cdm.product.common.settlement.SettlementTypeEnum;
import cdm.product.template.EconomicTerms;
import cdm.product.template.ExerciseTerms;
import cdm.product.template.ExpirationTimeTypeEnum;
import cdm.product.template.NonTransferableProduct;
import cdm.product.template.OptionExerciseStyleEnum;
import cdm.product.template.OptionPayout;
import cdm.product.template.OptionTypeEnum;
import cdm.product.template.Payout;
import cdm.product.template.TradeLot;
import cdm.product.template.Underlier;
import jakarta.inject.Inject;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.finos.cdm.example.util.AbstractExample;

import java.math.BigDecimal;
import java.util.List;

import static org.finos.cdm.util.ResourcesUtils.resolveReferences;

/**
 * Example to illustrate how to apply a quantity change instruction to one or more specific objects in TradeLot.
 * <p>
 * Note that the example trade used is not completely specified and only those attributes are populated that
 * make it pass Rosetta validation.
 */
public class QuantityChangeForSinglePayout extends AbstractExample {
    @Inject
    Create_BusinessEvent createBusinessEvent;

    @Override
    public void example() throws RuntimeException {
        // Create a trade with three identical option payouts (qty = 10,000 & price = 15.67)
        TradeState ts = createDummyTrade();

        Date chgDate = Date.of(2026, 10, 20);

        // In this example, we only change quantities but the price can also be changed in the same way.
        // Change quantity on legs 1 & 3 to 5,000 and leave quantity-2 untouched.
        var instr = Instruction.builder()
                .setBeforeValue(ts)
                .setPrimitiveInstruction(buildQtyChange(List.of("quantity-1", "quantity-3"), BigDecimal.valueOf(5000)));
        var busEvt = createBusinessEvent.evaluate(List.of(instr), EventIntentEnum.CONTRACT_TERMS_AMENDMENT, chgDate, chgDate);
        ts = resolveReferences(busEvt.getAfter().get(0));
        checkQuantities(ts, BigDecimal.valueOf(5000), BigDecimal.valueOf(10000), BigDecimal.valueOf(5000));

        // Change quantity to 6,000 on leg 2 only
        instr = Instruction.builder()
                .setBeforeValue(ts)
                .setPrimitiveInstruction(buildQtyChange(List.of("quantity-2"), BigDecimal.valueOf(6000)));
        busEvt = createBusinessEvent.evaluate(List.of(instr), EventIntentEnum.CONTRACT_TERMS_AMENDMENT, chgDate, chgDate);
        ts = resolveReferences(busEvt.getAfter().get(0));
        checkQuantities(ts, BigDecimal.valueOf(5000), BigDecimal.valueOf(6000), BigDecimal.valueOf(5000));
    }

    private void checkQuantities(TradeState ts, BigDecimal q1, BigDecimal q2, BigDecimal q3) {
        BigDecimal qty1 = ts.getTrade().getProduct().getEconomicTerms().getPayout().get(0).getOptionPayout()
                .getPriceQuantity().getQuantitySchedule().getValue().getValue();
        BigDecimal qty2 = ts.getTrade().getProduct().getEconomicTerms().getPayout().get(1).getOptionPayout()
                .getPriceQuantity().getQuantitySchedule().getValue().getValue();
        BigDecimal qty3 = ts.getTrade().getProduct().getEconomicTerms().getPayout().get(2).getOptionPayout()
                .getPriceQuantity().getQuantitySchedule().getValue().getValue();

        if (qty1.compareTo(q1) != 0 || qty2.compareTo(q2) != 0 || qty3.compareTo(q3) != 0)
            throw new RuntimeException("Quantities don't match - " + q1 + ", " + q2 + ", " + q3 + " vs. "
                    + qty1 + ", " + qty2 + ", " + qty3);
    }

    private PrimitiveInstruction buildQtyChange(List<String> qtyRef, BigDecimal newQty) {
        return PrimitiveInstruction.builder()
                .setQuantityChange(QuantityChangeInstruction.builder()
                        .addChange(PriceQuantity.builder()
                                .setQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                        .setValue(NonNegativeQuantitySchedule.builder()
                                                .setValue(newQty)
                                                .setUnit(UnitType.builder()
                                                        .setFinancialUnit(FinancialUnitEnum.SHARE)))))
                        .setDirection(QuantityChangeDirectionEnum.REPLACE)
                        .setQuantityLocations(qtyRef));     // Use setPriceLocations to modify prices
    }

    private TradeState createDummyTrade() {
        TradeState ts = TradeState.builder()
                .setTrade(Trade.builder()
                        .setCounterparty(List.of(Counterparty.builder()
                                        .setPartyReference(ReferenceWithMetaParty.builder()
                                                .setExternalReference("Cp-1"))
                                        .setRole(CounterpartyRoleEnum.PARTY_1),
                                Counterparty.builder()
                                        .setPartyReference(ReferenceWithMetaParty.builder()
                                                .setExternalReference("Cp-2"))
                                        .setRole(CounterpartyRoleEnum.PARTY_2)))
                        .addTradeIdentifier(TradeIdentifier.builder()
                                .addAssignedIdentifier(AssignedIdentifier.builder()
                                        .setIdentifierValue("Junk"))
                                .setIssuer(FieldWithMetaString.builder()
                                        .setValue("Internal")))
                        .setTradeDate(FieldWithMetaDate.builder()
                                .setValue(Date.of(2026, 10, 15)))
                        .setProduct(NonTransferableProduct.builder()
                                .setEconomicTerms(EconomicTerms.builder()
                                        .setPayout(List.of(
                                                buildPayout("quantity-1", "price-1", "observable-1"),
                                                buildPayout("quantity-2", "price-3", "observable-2"),
                                                buildPayout("quantity-3", "price-3", "observable-3")))))
                        .setTradeLot(List.of(TradeLot.builder()
                                .addPriceQuantity(buildPriceQuantity("quantity-1", "price-1", "observable-1"))
                                .addPriceQuantity(buildPriceQuantity("quantity-2", "price-2", "observable-2"))
                                .addPriceQuantity(buildPriceQuantity("quantity-3", "price-3", "observable-3")))));
        return resolveReferences(ts);
    }

    private Payout buildPayout(String quantityRef, String priceRef, String obsRef) {
        return Payout.builder()
                .setOptionPayout(OptionPayout.builder()
                        .setOptionType(OptionTypeEnum.CALL)
                        .setExerciseTerms(ExerciseTerms.builder()
                                .setStyle(OptionExerciseStyleEnum.EUROPEAN)
                                .setExpirationTimeType(ExpirationTimeTypeEnum.CLOSE)
                                .setExpirationDate(List.of(AdjustableOrRelativeDate.builder()
                                                .setAdjustableDate(AdjustableDate.builder()
                                                        .setAdjustedDateValue(Date.of(2026,12,1))))))
                        .setSettlementTerms(SettlementTerms.builder()
                                .setSettlementType(SettlementTypeEnum.CASH)
                                .setSecuritySettlementCentre(AssetAncillaryPartyRoleEnum.SECURITY_SETTLEMENT_CENTRE)
                                .setCashSettlementCentre(AssetAncillaryPartyRoleEnum.CASH_SETTLEMENT_CENTRE))
                        .setBuyerSeller(BuyerSeller.builder()
                                .setBuyer(CounterpartyRoleEnum.PARTY_1)
                                .setSeller(CounterpartyRoleEnum.PARTY_2))
                        .setPayerReceiver(PayerReceiver.builder()
                                .setPayer(CounterpartyRoleEnum.PARTY_1)
                                .setReceiver(CounterpartyRoleEnum.PARTY_2))
                        .setUnderlier(Underlier.builder()
                                .setObservable(ReferenceWithMetaObservable.builder()
                                        .setReference(Reference.builder()
                                                .setReference(obsRef)
                                                .setScope("DOCUMENT"))))
                        .setPriceQuantity(ResolvablePriceQuantity.builder()
                                .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                        .setReference(Reference.builder()
                                                .setReference(quantityRef)
                                                .setScope("DOCUMENT")))
                                .setPriceSchedule(List.of(ReferenceWithMetaPriceSchedule.builder()
                                        .setReference(Reference.builder()
                                                .setReference(priceRef)
                                                .setScope("DOCUMENT"))))));
    }

    private PriceQuantity buildPriceQuantity(String quantityRef, String priceRef, String obsRef) {
        return PriceQuantity.builder()
                .setQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                        .setValue(NonNegativeQuantitySchedule.builder()
                                .setValue(BigDecimal.valueOf(10000))
                                .setUnit(UnitType.builder()
                                        .setFinancialUnit(FinancialUnitEnum.SHARE)))
                        .setMeta(MetaFields.builder()
                                .setScopedKey(quantityRef)))
                .setPrice(List.of(FieldWithMetaPriceSchedule.builder()
                        .setValue(PriceSchedule.builder()
                                .setValue(BigDecimal.valueOf(15.67))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue("USD"))
                                .setPriceType(PriceTypeEnum.ASSET_PRICE)
                                .setPerUnitOf(UnitType.builder()
                                        .setFinancialUnit(FinancialUnitEnum.SHARE)))
                        .setMeta(MetaFields.builder()
                                .setScopedKey(priceRef))))
                .setObservable(FieldWithMetaObservable.builder()
                        .setValue(Observable.builder()
                                .setAsset(Asset.builder()
                                        .setInstrument(Instrument.builder()
                                                .setSecurity(Security.builder()
                                                        .addIdentifier(AssetIdentifier.builder()
                                                                .setIdentifierValue("SomeIdentifier")
                                                                .setIdentifierType(AssetIdTypeEnum.NAME))
                                                        .setAssetType(AssetTypeEnum.SECURITY)
                                                        .setSecurityType(SecurityTypeEnum.EQUITY)))))
                        .setMeta(MetaFields.builder()
                                .setScopedKey(obsRef)));
    }

    public static void main(String[] args) {
        new QuantityChangeForSinglePayout().run();
    }
}
