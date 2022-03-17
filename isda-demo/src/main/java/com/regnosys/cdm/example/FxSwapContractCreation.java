package com.regnosys.cdm.example;

import cdm.base.datetime.AdjustableDates;
import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.base.math.metafields.ReferenceWithMetaQuantity;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.asset.ForeignExchange;
import cdm.product.common.settlement.*;
import cdm.product.template.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;
import java.util.List;

import static com.rosetta.model.lib.records.Date.of;

public class FxSwapContractCreation {

    private final PostProcessStep keyProcessor;

    public FxSwapContractCreation() {
        keyProcessor = new GlobalKeyProcessStep(NonNullHashCollector::new);
    }

    public static void main(String[] args) throws JsonProcessingException {
        FxSwapContractCreation fxSwapContractCreation = new FxSwapContractCreation();
        Trade fxSwapContract = fxSwapContractCreation.createFxSwapContractExample();

        String json = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(fxSwapContract);

        System.out.println(json);
    }

    /**
     * @return FX Swap Trade
     */
    private Trade createFxSwapContractExample() {
        String currency1Str = "GBP";
        long quantity1 = 10000000;

        String currency2Str = "USD";
        long quantity2 = 14800000;

        double rate = 1.48;

        Party party1 = addGlobalKey(Party.class,
                createParty("5493000SCC07UI6DB380", "http://www.fpml.org/coding-scheme/external/iso17442"));

        Party party2 = addGlobalKey(Party.class,
                createParty("529900DTJ5A7S5UCBB52", "http://www.fpml.org/coding-scheme/external/iso17442"));

        PriceQuantity priceQuantity = createPriceQuantity(currency1Str, quantity1, currency2Str, quantity2, rate);

        Product underlier = createForeignExchangeUnderlier(
                createExchangeCurrency(party1, party2),
                createExchangeCurrency(party2, party1));

        Date settlementDate = of(2001, 10, 25);

        Date tradeDate = of(2001, 10, 23);

        ContractualProduct contractualProduct = createContractualProduct(underlier, settlementDate);

        Identifier citi123 = createIdentifier("CITI123", "http://www.citi.com/fx/trade-id", party1);
        Identifier barc987 = createIdentifier("BARC987", "http://www.barclays.com/fx/trade-id", party2);

//        QuantityNotation quantityNotation1 = createQuantityNotation(currency1, quantity1);
//        QuantityNotation quantityNotation2 = createQuantityNotation(currency2, quantity2);

        List<Identifier> identifiers = List.of(citi123, barc987);
        List<Party> parties = List.of(party1, party2);
//        List<QuantityNotation> quantityNotations = List.of(quantityNotation1, quantityNotation2);

        return createFxSwapContract(identifiers, parties, priceQuantity, contractualProduct, tradeDate);
    }

    private PriceQuantity createPriceQuantity(String currency1Str, long quantity1, String currency2Str, long quantity2, double rate) {
        return PriceQuantity.builder()
                .addPrice(FieldWithMetaPrice.builder()
                        .setMeta(MetaFields.builder()
                                .addKey(Key.builder()
                                        .setScope("DOCUMENT")
                                        .setKeyValue("price-1")))
                        .setValue(Price.builder()
                                .setAmount(BigDecimal.valueOf(rate))
                                .setUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency1Str))
                                .setPerUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency2Str))
                                .setPriceExpression(PriceExpression.builder().setPriceType(PriceTypeEnum.EXCHANGE_RATE)))
                )
                .addQuantity(FieldWithMetaQuantity.builder()
                        .setMeta(MetaFields.builder()
                                .addKey(Key.builder()
                                        .setScope("DOCUMENT")
                                        .setKeyValue("quantity-1")))
                        .setValue(Quantity.builder()
                                .setAmount(BigDecimal.valueOf(quantity1))
                                .setUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency1Str))))
                .addQuantity(FieldWithMetaQuantity.builder()
                        .setMeta(MetaFields.builder()
                                .addKey(Key.builder()
                                        .setScope("DOCUMENT")
                                        .setKeyValue("quantity-2")))
                        .setValue(Quantity.builder()
                                .setAmount(BigDecimal.valueOf(quantity2))
                                .setUnitOfAmount(UnitType.builder()
                                        .setCurrencyValue(currency2Str))))
                .setObservable(Observable.builder()
                        .setCurrencyPairValue(QuotedCurrencyPair.builder()
                                .setCurrency1Value(currency1Str)
                                .setCurrency2Value(currency2Str)
                                .setQuoteBasis(QuoteBasisEnum.CURRENCY_2_PER_CURRENCY_1)))
                .build();
    }

    private Trade createFxSwapContract(List<Identifier> identifiers, List<Party> parties, PriceQuantity priceQuantity, ContractualProduct contractualProduct, Date tradeDate) {
        Trade trade = Trade.builder()
                .addTradeIdentifier(identifiers)
                .setTradableProduct(TradableProduct.builder()
                        .addTradeLot(TradeLot.builder()
                                .addPriceQuantity(priceQuantity))
                        .setProduct(Product.builder().setContractualProduct(contractualProduct)))
                .addParty(parties)
                .setTradeDate(FieldWithMetaDate.builder().setValue(tradeDate).build())
                .build();

        return addGlobalKey(Trade.class, trade);
    }

    private Product createForeignExchangeUnderlier(Cashflow exchangedCurrency1, Cashflow exchangedCurrency2) {
        return Product.builder()
                        .setForeignExchange(ForeignExchange.builder()
                                .setExchangedCurrency1(exchangedCurrency1)
                                .setExchangedCurrency2(exchangedCurrency2)
                                .build())
                        .build();
    }

    private Cashflow createExchangeCurrency(Party payer, Party receiver) {
        return Cashflow.builder()
                .setPayoutQuantity(ResolvablePayoutQuantity.builder()
                        .setResolvedQuantity(ReferenceWithMetaQuantity.builder()
                            .setReference(Reference.builder()
                                .setScope("DOCUMENT")
                                .setReference("quantity-2"))))
                .setPayerReceiver(PayerReceiver.builder()
                        .setReceiverPartyReference(ReferenceWithMetaParty.builder()
                                .setGlobalReference(getGlobalReference(receiver)))
                        .setPayerPartyReference(ReferenceWithMetaParty.builder()
                                .setGlobalReference(getGlobalReference(payer))))
                .build();
    }


    private ContractualProduct createContractualProduct(Product underlier, Date settlementDate) {
        return ContractualProduct.builder()
                .setEconomicTerms(EconomicTerms.builder()
                        .setPayout(Payout.builder()
                                .addForwardPayout(ForwardPayout.builder()
                                        .setSettlementTerms(SettlementTerms.builder()
                                                .setSettlementDate(SettlementDate.builder()
                                                        .setAdjustableDates(AdjustableDates.builder()
                                                        .addAdjustedDateValue(settlementDate))))
                                        .setUnderlier(underlier)))
                .build());
    }

    private Party createParty(String partyId, String scheme) {
        return Party.builder().addPartyId(FieldWithMetaString.builder()
                .setValue(partyId)
                .setMeta(MetaFields.builder().setScheme(scheme).build())
                .build())
                .build();
    }

    private Identifier createIdentifier(String identifier, String scheme, Party issuer) {
        return Identifier.builder().addAssignedIdentifier(
                AssignedIdentifier.builder().setIdentifier(
                        FieldWithMetaString.builder().setValue(identifier)
                                .setMeta(MetaFields.builder()
                                        .setScheme(scheme)
                                        .build())
                                .build())
                        .build())
                .setIssuerReference(ReferenceWithMetaParty.builder()
                        .setGlobalReference(getGlobalReference(issuer))
                        .build())
                .build();
    }

    /**
     * Utility to post process a {@link RosettaModelObject} to add ll gloval keys.
     */
    private <T extends RosettaModelObject> T addGlobalKey(Class<T> type, T modelObject) {
        RosettaModelObjectBuilder builder = modelObject.toBuilder();
        keyProcessor.runProcessStep(type, builder);
        return type.cast(builder.build());
    }

    /**
     * Utility to get the global reference string from a {@link GlobalKey} instance.
     */
    private String getGlobalReference(GlobalKey globalKey) {
        return globalKey.getMeta().getGlobalKey();
    }
}
