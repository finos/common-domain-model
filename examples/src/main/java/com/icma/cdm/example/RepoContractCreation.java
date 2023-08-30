/*******************************************************************************
 * Copyright (c) icmagroup.org  All rights reserved.
 *
 * This file is part of the International Capital Market Association (ICMA)
 * CDM for Repo and Bonds Demo
 *
 * This file is intended for demo purposes only and may not be distributed
 * or used in any commercial capacity other then its intended purpose.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE.
 *
 *
 * Contact International Capital Market Association (ICMA),110 Cannon Street,
 * London EC4N 6EU, ph. +44 20 7213 0310, if you have any questions.
 *
 ******************************************************************************/
package com.icma.cdm.example;

import cdm.base.datetime.AdjustableDates;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.ExecutionInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeIdentifier;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
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


//function to create a repo contract 
public class RepoContractCreation {

    private final PostProcessStep keyProcessor;

    public RepoContractCreation() {
        keyProcessor = new GlobalKeyProcessStep(NonNullHashCollector::new);
    }

    /**
     * @return Repo Trade
     */
    private Trade createRepoContractExample() {
        String currency1Str = "GBP";
        long quantity1 = 10000000;

        String currency2Str = "USD";
        long quantity2 = 14800000;

        double rate = 1.48;

        Party party1 = addGlobalKey(Party.class,
                createParty("5493000SCC07UI6DB380", "http://www.fpml.org/coding-scheme/external/iso17442"));

        Party party2 = addGlobalKey(Party.class,
                createParty("529900DTJ5A7S5UCBB52", "http://www.fpml.org/coding-scheme/external/iso17442"));
		
		PartyRole partyRole1 = createPartyRole(party1, "SELLER");
				
		PartyRole partyRole2 = createPartyRole(party2, "BUYER");

        PriceQuantity priceQuantity = createPriceQuantity(currency1Str, quantity1, currency2Str, quantity2, rate);


        Date settlementDate = of(2022, 07, 13);

        Date tradeDate = of(2022, 07, 13);

        ContractualProduct contractualProduct = createContractualProduct();
		
        TradeIdentifier uti = createIdentifier("L302TW1XER000101", "UnqTradIdr", "5493000SCC07UI6DB380");

        TradeIdentifier identifier = uti;
        List<Party> parties = List.of(party1, party2);

        return createRepoContract(identifier, parties, priceQuantity, contractualProduct, tradeDate, party1, party2);
    }

    private PriceQuantity createPriceQuantity(String currency1Str, long quantity1, String currency2Str, long quantity2, double rate) {
        return PriceQuantity.builder()
                .addPrice(FieldWithMetaPriceSchedule.builder()
                        .setMeta(MetaFields.builder()
                                .addKey(Key.builder()
                                        .setScope("DOCUMENT")
                                        .setKeyValue("price-1")))
                        .setValue(Price.builder()
                                .setValue(BigDecimal.valueOf(rate))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(currency1Str))
                                .setPerUnitOf(UnitType.builder()
                                        .setCurrencyValue(currency2Str))
                                        .setPriceType(PriceTypeEnum.EXCHANGE_RATE)))
                .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                        .setMeta(MetaFields.builder()
                                .addKey(Key.builder()
                                        .setScope("DOCUMENT")
                                        .setKeyValue("quantity-1")))
                        .setValue(NonNegativeQuantitySchedule.builder()
                                .setValue(BigDecimal.valueOf(quantity1))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(currency1Str))))
                .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                        .setMeta(MetaFields.builder()
                                .addKey(Key.builder()
                                        .setScope("DOCUMENT")
                                        .setKeyValue("quantity-2")))
                        .setValue(NonNegativeQuantitySchedule.builder()
                                .setValue(BigDecimal.valueOf(quantity2))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(currency2Str))))
                .setObservable(Observable.builder()
                        .setCurrencyPairValue(QuotedCurrencyPair.builder()
                                .setCurrency1Value(currency1Str)
                                .setCurrency2Value(currency2Str)
                                .setQuoteBasis(QuoteBasisEnum.CURRENCY_2_PER_CURRENCY_1)))
                .build();
    }

    private Trade createRepoContract(TradeIdentifier identifier,
                                       List<Party> parties,
                                       PriceQuantity priceQuantity,
                                       ContractualProduct contractualProduct,
                                       Date tradeDate,
                                       Party party1,
                                       Party party2
									   ) {
        Trade trade = Trade.builder()
                .addTradeIdentifier(identifier)
                .setTradableProduct(TradableProduct.builder()
                        .addCounterparty(Counterparty.builder()
                                .setPartyReferenceValue(party1)
                                .setRole(CounterpartyRoleEnum.PARTY_1))
                        .addCounterparty(Counterparty.builder()
                                .setPartyReferenceValue(party2)
                                .setRole(CounterpartyRoleEnum.PARTY_2))
                        .addTradeLot(TradeLot.builder()
                                .addPriceQuantity(priceQuantity))
                        .setProduct(Product.builder().setContractualProduct(contractualProduct)))
                .addParty(parties)
                .setTradeDate(FieldWithMetaDate.builder().setValue(tradeDate).build())
                .build();

        return addGlobalKey(Trade.class, trade);
    }


    private ContractualProduct createContractualProduct() {
        return ContractualProduct.builder()
                .setEconomicTerms(EconomicTerms.builder()
                        .setPayout(Payout.builder()
                                        ))
                .build();
    }

    private Party createParty(String partyId, String scheme) {
        return Party.builder().addPartyId(PartyIdentifier.builder()
                        .setIdentifierValue(partyId)
                        .setMeta(MetaFields.builder().setScheme(scheme).build())
                                .build())
                .build();
    }
	
	private PartyRole createPartyRole(Party partyReference, String role) {
        return PartyRole.builder()
				.setRole(PartyRoleEnum.valueOf(role))
                .build();
    }

    private TradeIdentifier createIdentifier(String identifier, String scheme, String issuer) {
		
        return TradeIdentifier.builder().addAssignedIdentifier(
                AssignedIdentifier.builder().setIdentifier(
                        FieldWithMetaString.builder().setValue(identifier)
                                .setMeta(MetaFields.builder()
                                        .setScheme(scheme)
                                        .build())
                                .build())
                        .build())
                .setIssuer(FieldWithMetaString.builder()
						.setValue(issuer)
                        .build())
				.setIdentifierType(TradeIdentifierTypeEnum.valueOf("UNIQUE_TRANSACTION_IDENTIFIER"))
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
