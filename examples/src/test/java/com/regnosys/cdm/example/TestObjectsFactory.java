package com.regnosys.cdm.example;

import cdm.base.datetime.AdjustableDates;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.*;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.legaldocumentation.common.ClosedState;
import cdm.observable.asset.Money;
import cdm.observable.asset.Observable;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.product.common.settlement.*;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;
import cdm.product.template.TradeLot;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class to generate sample data for unit tests
 */
public class TestObjectsFactory {

    public static final String CUSIP_US1234567891 = "US1234567891";
    public static final String CUSIP_DH9105730505 = "DH9105730505";
    public static final String CURRENCY_USD = "USD";
    public static final String CLIENT_A_ID = "c1";
    public static final String CLIENT_A_NAME = "clientA";
    public static final String EXECUTING_BROKER_ID = "b1";
    public static final String EXECUTING_BROKER_NAME = "executingBroker";
    public static final String COUNTERPARTY_BROKER_A_ID = "b2";
    public static final String COUNTERPARTY_BROKER_A_NAME = "counterpartyBrokerA";
    public static final String COUNTERPARTY_BROKER_B_ID = "b3";
    public static final String COUNTERPARTY_BROKER_B_NAME = "counterpartyBrokerB";

    private final List<PostProcessStep> postProcessors;

    public TestObjectsFactory() {
        GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
        this.postProcessors = Arrays.asList(globalKeyProcessStep,
                new ReKeyProcessStep(globalKeyProcessStep));
    }

    public TradeState getTradeState(int tradeId, LocalDate tradeDate, String cusip, long quantity, double dirtyPrice, double cleanPrice, String tradedCurrency, LocalDate settlementDate,
                                    boolean isExecutingEntityBuy, Party clientParty, Party executingBrokerParty, Party counterpartyBrokerParty) {

        TradeState.TradeStateBuilder tradeStateBuilder = TradeState.builder()
                .setTrade(Trade.builder()
                        .setExecutionDetails(ExecutionDetails.builder()
                                .setExecutionType(ExecutionTypeEnum.ELECTRONIC)
                                .setExecutionVenue(LegalEntity.builder().setName(FieldWithMetaString.builder().setValue("Tradeweb").build()).build()))
                        .addTradeIdentifier(getIdentifier("tradeId" + tradeId, executingBrokerParty.getMeta().getExternalKey()))
                        .setTradeDate(FieldWithMetaDate.builder().setValue(Date.of(tradeDate)).build())
                        .setTradableProduct(TradableProduct.builder()
                                .setProduct(getProduct(cusip))
                                .addTradeLot(TradeLot.builder()
                                        .addPriceQuantity(getPriceQuantity(quantity, cusip, cleanPrice, tradedCurrency,
                                                getSettlementTerms(settlementDate, dirtyPrice, quantity, tradedCurrency)))))
                        .addParty(clientParty)
                        .addParty(executingBrokerParty)
                        .addParty(counterpartyBrokerParty)
                        .addPartyRole(getPartyRole(executingBrokerParty, isExecutingEntityBuy ? PartyRoleEnum.BUYER : PartyRoleEnum.SELLER))
                        .addPartyRole(getPartyRole(counterpartyBrokerParty, isExecutingEntityBuy ? PartyRoleEnum.SELLER : PartyRoleEnum.BUYER))
                        .addPartyRole(getPartyRole(clientParty, PartyRoleEnum.CLIENT))
                        .addPartyRole(getPartyRole(executingBrokerParty, PartyRoleEnum.EXECUTING_ENTITY))
                        .addPartyRole(getPartyRole(counterpartyBrokerParty, PartyRoleEnum.COUNTERPARTY)))
                .setState(State.builder()
                        .setClosedState(ClosedState.builder()));

        // Generate global key/references etc
        postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(TradeState.class, tradeStateBuilder));

        return tradeStateBuilder.build();
    }

    private PartyRole getPartyRole(Party party, PartyRoleEnum partyRole) {
        return PartyRole.builder()
                .setPartyReference(ReferenceWithMetaParty.builder()
                        .setExternalReference(party.getMeta().getExternalKey())
                        .setValue(party))
                .setRole(partyRole)
                .build();
    }

    public Party getParty(String id, String partyId, Account account) {
        Party.PartyBuilder partyBuilder = Party.builder()
                .setMeta(MetaFields.builder()
                        .setExternalKey(id)
                        .build())
                .addPartyId(PartyIdentifier.builder()
                        .setIdentifierValue(partyId)
                        .setMeta(MetaFields.builder()
                                .setScheme("http://www.fpml.org/coding-scheme/external")
                                .build())
                        .build());

        Optional.ofNullable(account).ifPresent(partyBuilder::setAccount);

        return partyBuilder.build();
    }

    private TradeIdentifier getIdentifier(String identifier, String issuer) {
        return TradeIdentifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifier(FieldWithMetaString.builder().setValue(identifier).build()))
                .setIssuerReference(ReferenceWithMetaParty.builder().setExternalReference(issuer).build())
                .build();
    }

    private PriceQuantity getPriceQuantity(double notional, String productIdentifier, double cleanPrice, String tradedCurrency, SettlementTerms settlementTerms) {
        return PriceQuantity.builder()
                .addPrice(FieldWithMetaPriceSchedule.builder()
                        .setMeta(MetaFields.builder()
                                .addKey(Key.builder()
                                        .setScope("DOCUMENT")
                                        .setKeyValue("cleanPrice-1")))
                        .setValue(Price.builder()
                                .setValue(BigDecimal.valueOf(cleanPrice))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(tradedCurrency))
                                .setPriceType(PriceTypeEnum.ASSET_PRICE)))
                .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                        .setMeta(MetaFields.builder().addKey(Key.builder()
                                .setScope("DOCUMENT")
                                .setKeyValue("notional-1")))
                        .setValue(NonNegativeQuantitySchedule.builder()
                                .setValue(BigDecimal.valueOf(notional))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(tradedCurrency))))
                .setObservable(Observable.builder()
                        .addProductIdentifierValue(ProductIdentifier.builder()
                                .setIdentifierValue(productIdentifier)
                                .setSource(ProductIdTypeEnum.CUSIP)))
                .setSettlementTerms(settlementTerms)
                .build();
    }

    private Product getProduct(String productId) {
        return Product.builder()
                .setSecurity(Security.builder()
                        .addIdentifier(AssetIdentifier.builder()
                                .setIdentifierValue(productId)
                                .setIdentifierType(AssetIdTypeEnum.CUSIP))
                        .setSecurityType(SecurityTypeEnum.DEBT))
                .build();
    }

    private SettlementTerms getSettlementTerms(LocalDate settlementDate, double dirtyPrice, long quantity, String settlementCurrency) {
        return SettlementTerms.builder()
                .setSettlementDate(SettlementDate.builder()
                        .setAdjustableDates(AdjustableDates.builder()
                                .addAdjustedDateValue(Date.of(settlementDate))))
                .addCashSettlementTerms(CashSettlementTerms.builder()
                        .setCashSettlementAmount(Money.builder()
                                .setValue(BigDecimal.valueOf(dirtyPrice * quantity))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(settlementCurrency))))
                .setSettlementCurrencyValue(settlementCurrency)
                .setTransferSettlementType(TransferSettlementEnum.DELIVERY_VERSUS_PAYMENT)
                .build();
    }
}
