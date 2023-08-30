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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import cdm.base.datetime.*;
import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableDates;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.base.staticdata.asset.common.metafields.FieldWithMetaProductIdentifier;
import cdm.base.staticdata.asset.common.*;
import cdm.event.common.ExecutionInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeIdentifier;
import cdm.event.common.ExecutionDetails;
import cdm.event.common.*;
import cdm.product.asset.*;
import cdm.product.collateral.*;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PayRelativeToEnum;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
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
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.rosetta.model.lib.records.Date.of;

//This class is used to generate a repo execution using an execution instruction and business event.
/*func Create_ExecutionInstruction:
    inputs:
        product Product (1..1) <"Defines the financial product to be executed and contract formed.">
        priceQuantity PriceQuantity (1..*) <"Specifies the price, quantity, and optionally the observable and settlement terms for use in a trade or other purposes.">
        counterparty Counterparty (2..2) <"Maps two defined parties to counterparty enums for the transacted product.">
        ancillaryParty AncillaryParty (0..*) <"Maps any ancillary parties, e.g. parties involved in the transaction that are not one of the two principal parties.">
        parties Party (2..*) <"Defines all parties to that execution, including agents and brokers.">
        partyRoles PartyRole (0..*) <"Defines the role(s) that party(ies) may have in relation to the execution.">
        executionDetails ExecutionDetails (0..1) <"Specifies the type of execution, e.g. via voice or electronically.">
        tradeDate date (1..1) <"Denotes the trade/execution date.">
        tradeIdentifier TradeIdentifier (1..*) <"Denotes one or more identifiers associated with the transaction.">
    output:
        instruction ExecutionInstruction (1..1)
*/

public class RepoTradeCreation{
	
	private final PostProcessStep keyProcessor;

    public RepoTradeCreation() {
        keyProcessor = new GlobalKeyProcessStep(NonNullHashCollector::new);
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


	public ExecutionInstruction createRepoExecutionInstruction (
				String tradeDateStr,
				String purchaseDateStr,
				String repurchaseDateStr,
				String tradeUTIStr,
				String buyerLEIStr,
				String sellerLEIStr,
				String collateralDescriptionStr,
				String collateralISINStr,
				String collateralQuantityStr,
				String collateralCleanPriceStr,
				String collateralDirtyPriceStr,
				String collateralAdjustedValueStr,
				String collateralCurrencyStr,
				String repoRateStr,
				String cashCurrencyStr,
				String cashQuantityStr,
				String haircutStr,
				String termTypeStr,
				String terminationOptionStr,
				String noticePeriodStr,
				String deliveryMethodStr,
				String substitutionAllowedStr,
				String rateTypeStr,
				String dayCountFractionStr,
				String purchasePriceStr,
				String repurchasePriceStr,
				String agreementNameStr,
				String agreementGoverningLawStr,
				String agreementVintageStr,
				String agreementPublisherStr,
				String agreementDateStr,
				String agreementIdentifierStr,
				String agreementEffectiveDate,
				String agreementUrl
	) throws JsonProcessingException {
		
		RepoTradeCreation rc = new RepoTradeCreation();

		DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
		ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(tradeDateStr, formatter);
		ZonedDateTime zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		System.out.println("Trade Date:" + zdtWithZoneOffset);
		System.out.println(zdtInLocalTimeline);
		
		FieldWithMetaDate tradeDate = addGlobalKey(FieldWithMetaDate.class,
						rc.createTradeDate(zdtWithZoneOffset.getYear(), zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth()));
		
		TradeIdentifier tradeIdentifier = rc.createRepoTradeIdentifier(tradeUTIStr, "UnqTradIdr", "5493000SCC07UI6DB380");
		
		//Party 1 is defined in the interest rate payout model as the receiver and thus represents the repo seller also referred to as the collateral taker.
        Party party1 = addGlobalKey(Party.class,
                createRepoParty(buyerLEIStr,"","BUYER_BANK"));
				
		//Party 2 is defined in the interest rate payout model as the payer and thus represents the repo seller also referred to as the collateral giver.
        Party party2 = addGlobalKey(Party.class,
                createRepoParty(sellerLEIStr,"","SELLER_BANK"));	
		List<Party> parties = List.of(party1, party2);
		
		PartyRole partyRole1 = createRepoPartyRole(party1, "BUYER");		
		PartyRole partyRole2 = createRepoPartyRole(party2, "SELLER");
		List<PartyRole> partyRoles = List.of(partyRole1, partyRole2);
		
		Counterparty counterparty1 = createRepoCounterparty(party1, "PARTY_1");
		Counterparty counterparty2 = createRepoCounterparty(party2, "PARTY_2");
		List<Counterparty> counterparties = List.of(counterparty1, counterparty2);
		
		double cashQuantity = Double.parseDouble(cashQuantityStr);
		double collateralQuantity = Double.parseDouble(collateralQuantityStr);
		double repoRate = Double.parseDouble(repoRateStr);
		double haircut = Double.parseDouble(haircutStr);
		double collateralCleanPrice = Double.parseDouble(collateralCleanPriceStr);
		double collateralDirtyPrice = Double.parseDouble(collateralDirtyPriceStr);
			
		PriceQuantity loanPriceQuantity = addGlobalKey(PriceQuantity.class,
				createLoanPriceQuantity(cashCurrencyStr, cashQuantity, collateralCurrencyStr, collateralQuantity, collateralCleanPrice, collateralDirtyPrice, repoRate,collateralISINStr,""));
		
		PriceQuantity collateralPriceQuantity = addGlobalKey(PriceQuantity.class,
				createCollateralPriceQuantity(cashCurrencyStr, cashQuantity, collateralCurrencyStr, collateralQuantity, collateralCleanPrice, collateralDirtyPrice, repoRate,collateralISINStr,""));
		
		List<PriceQuantity> repoPriceQuantity = List.of(loanPriceQuantity, collateralPriceQuantity);
	
		zdtWithZoneOffset = ZonedDateTime.parse(purchaseDateStr, formatter);
		zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		Date effectiveDate = of(zdtWithZoneOffset.getYear(), zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());
		
		System.out.println("Purchase Date:" + zdtWithZoneOffset);

		zdtWithZoneOffset = ZonedDateTime.parse(repurchaseDateStr, formatter);
		zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		Date terminationDate = of(zdtWithZoneOffset.getYear(),zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());
		System.out.println("Repurchase Date:" + zdtWithZoneOffset);

		Product repoProduct = createRepoProduct(effectiveDate, terminationDate, repoRate, haircut , cashCurrencyStr);
		
		
		return ExecutionInstruction.builder()
			.setProduct(repoProduct)
			.addPriceQuantity(repoPriceQuantity)
			.addCounterparty(counterparties)
			.addParties(parties)
			.addPartyRoles(partyRoles)
			.setExecutionDetails(null)
			.setTradeDate(tradeDate)
			.addTradeIdentifier(tradeIdentifier)
			.build();
	}
	
	private Product createRepoProduct(Date effectiveDate, Date terminationDate, double repoRate, double haircut, String cashCurrency){
		
		BigDecimal repoRateBD = new BigDecimal(repoRate);
		BigDecimal haircutBD = new BigDecimal(haircut);
		
		ContractualProduct contractualRepoProduct = createContractualRepoProduct(effectiveDate, terminationDate, repoRateBD, haircutBD, cashCurrency);
		
		return Product.builder()
					.setContractualProduct(contractualRepoProduct)
					.build();
		
	}
	
	
	private ContractualProduct createContractualRepoProduct(
			Date effectiveDate,
			Date terminationDate,
			BigDecimal repoRateBD,
			BigDecimal haircutBD,
			String cashCurrency
	) {
        return ContractualProduct.builder()
				.addProductTaxonomy(ProductTaxonomy.builder()
					.setSource(TaxonomySourceEnum.valueOf("CFI"))
					.setValue(TaxonomyValue.builder()
							.setNameValue("LRSTXD")))
                .setEconomicTerms(EconomicTerms.builder()
						.setEffectiveDate(AdjustableOrRelativeDate.builder()
										.setAdjustableDate(AdjustableDate.builder()
												.setUnadjustedDate(effectiveDate)
												.setDateAdjustments(BusinessDayAdjustments.builder()
														.setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                        .setPayout(Payout.builder()
							.addInterestRatePayout(createFixedRateRepoPayout(effectiveDate, terminationDate,repoRateBD, cashCurrency))
							.addAssetPayout(createRepoCollateralPayout()))
						.setTerminationDate(AdjustableOrRelativeDate.builder()
										.setAdjustableDate(AdjustableDate.builder()
												.setUnadjustedDate(effectiveDate)
												.setDateAdjustments(BusinessDayAdjustments.builder()
														.setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
						.setCollateral(Collateral.builder()
							.setCollateralProvisions(CollateralProvisions.builder()
										.setEligibleCollateral(createRepoHairCut(haircutBD))))
						.build());

    }

	private List<EligibleCollateralSpecification> createRepoHairCut(BigDecimal haircut) {

		return List.of(
				EligibleCollateralSpecification.builder()
						.addCriteria(EligibleCollateralCriteria.builder()
							.setTreatment(CollateralTreatment.builder()
								.setValuationTreatment(CollateralValuationTreatment.builder()
									.setHaircutPercentage(haircut)))).build());

	}
	
	private AssetPayout createRepoCollateralPayout(){
		
		return AssetPayout.builder()
                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_2)
                        .setReceiver(CounterpartyRoleEnum.PARTY_1))
				.build();
		
	}
	
	private InterestRatePayout createFixedRateRepoPayout(
		Date effectiveDate,
		Date terminationDate,
		BigDecimal fixedRate,
		String cashCurrency
		
		)
		{
        return InterestRatePayout.builder()
                .setPriceQuantity(ResolvablePriceQuantity.builder()
                        .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                .setReference(Reference.builder()
                                        .setScope("DOCUMENT")
                                        .setReference("quantity-2"))))
                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build())
                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(effectiveDate)
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(terminationDate)
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                .setBusinessCenters(BusinessCenters.builder()
                                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                                .setExternalReference("primaryBusinessCenters"))
                                                        .addBusinessCenter(
                                                                FieldWithMetaBusinessCenterEnum.builder().setValue(BusinessCenterEnum.EUTA).build())))))
                        .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                                .setRollConvention(RollConventionEnum._3)
                                .setPeriodMultiplier(3)
                                .setPeriod(PeriodExtendedEnum.M))
                        .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                .setBusinessCenters(BusinessCenters.builder()
                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                .setExternalReference("primaryBusinessCenters")))))
                .setPaymentDates(PaymentDates.builder()
                        .setPayRelativeTo(PayRelativeToEnum.CALCULATION_PERIOD_END_DATE)
                        .setPaymentFrequency(Frequency.builder()
                                .setPeriodMultiplier(1)
                                .setPeriod(PeriodExtendedEnum.Y)
                                .build())
                        .build())
                .setRateSpecification(RateSpecification.builder()
                        .setFixedRate(FixedRateSpecification.builder()
                                .setRateSchedule(RateSchedule.builder()
                                        .setPrice(ReferenceWithMetaPriceSchedule.builder()
                                                .setReference(Reference.builder()
                                                        .setScope("DOCUMENT")
                                                        .setReference("price-1"))
                                                .setValue(Price.builder()
                                                        .setValue(fixedRate)
                                                        .setUnit(UnitType.builder().setCurrencyValue(cashCurrency))
                                                        .setPerUnitOf(UnitType.builder().setCurrencyValue(cashCurrency))
                                                                .setPriceType(PriceTypeEnum.INTEREST_RATE))))))
                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_2)
                        .setReceiver(CounterpartyRoleEnum.PARTY_1))
                .build();
    }
	
	
	
    private PriceQuantity createLoanPriceQuantity(
		String cashCurrencyStr, 
		double cashQuantity, 
		String collateralCurrencyStr,
		double collateralQuantity,
		double collteralCleanPrice,
		double collateralDirtyPrice,
		double rate,
		String collateralISINStr,
		String scheme)
		{
			
        return PriceQuantity.builder()
			// Set cash amount and rate
                .addPrice(FieldWithMetaPriceSchedule.builder()
					.setMeta(MetaFields.builder().setScheme(scheme).build())
					.setValue(Price.builder()
                        .setUnit(UnitType.builder()
                            .setCurrencyValue(cashCurrencyStr)))
                        .setValue(Price.builder()
                                .setValue(BigDecimal.valueOf(rate))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(cashCurrencyStr))
                                .setPerUnitOf(UnitType.builder()
                                        .setCurrencyValue(cashCurrencyStr))
										.setPriceType(PriceTypeEnum.INTEREST_RATE)))
                .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                        .setValue(NonNegativeQuantitySchedule.builder()
                                .setValue(BigDecimal.valueOf(cashQuantity))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(cashCurrencyStr)))
								.build())

				.build();
                     
    }
	
	private PriceQuantity createCollateralPriceQuantity(
		String cashCurrencyStr, 
		double cashQuantity, 
		String collateralCurrencyStr,
		double collateralQuantity,
		double collteralCleanPrice,
		double collateralDirtyPrice,
		double rate,
		String collateralISINStr,
		String scheme)
		{
			
        return PriceQuantity.builder()
			// Set cash amount and rate
				.setObservable(Observable.builder()
						.addProductIdentifierValue(ProductIdentifier.builder()
							.setIdentifierValue(collateralISINStr)
							.setSource(ProductIdTypeEnum.ISIN)
								.build()))				
				// Set collateral amount and price
                .addPrice(FieldWithMetaPriceSchedule.builder()
                        .setValue(Price.builder()
                                .setValue(BigDecimal.valueOf(collateralDirtyPrice))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(collateralCurrencyStr))
                                .setPerUnitOf(UnitType.builder()
                                        .setCurrencyValue(collateralCurrencyStr))
									.setPriceType(PriceTypeEnum.ASSET_PRICE)
									.setPriceExpression(PriceExpressionEnum.PERCENTAGE_OF_NOTIONAL)))
                .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                        .setValue(NonNegativeQuantitySchedule.builder()
                                .setValue(BigDecimal.valueOf(collateralQuantity))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(collateralCurrencyStr)))
						.build())

				.build();
                     
    }
	
	private Counterparty createRepoCounterparty(Party party, String role) {
		
		return Counterparty.builder()
			.setPartyReferenceValue(party)
			.setRole(CounterpartyRoleEnum.valueOf(role))
			.build();
		}
	
    private Party createRepoParty(String partyId, String scheme, String pName) {
        return Party.builder()
						.addPartyId(PartyIdentifier.builder()
                        .setIdentifierValue(partyId)
                        .setMeta(MetaFields.builder().setScheme(scheme).build())
                                .build())
						.setNameValue(pName)
                .build();
    }
	
	private PartyRole createRepoPartyRole(Party party, String role) {
        return PartyRole.builder()
				.setPartyReference(ReferenceWithMetaParty.builder()
                        .setGlobalReference(getGlobalReference(party))
						.setExternalReference("GlobalBank")
                        .build())
				.setRole(PartyRoleEnum.valueOf(role))
                .build();
    }
	
	private  ExecutionDetails createRepoExecutionDetails(String etype){
		
		return ExecutionDetails.builder()
				.setExecutionType(ExecutionTypeEnum.valueOf(etype))
				.build();
	}
	
	private FieldWithMetaDate createTradeDate(int y, int m, int d){

		Date dt;
		dt = of(y, m, d);

		return FieldWithMetaDate.builder().setValue(dt).build();
		

		
	}
	
	private TradeIdentifier createRepoTradeIdentifier(String identifier, String scheme, String issuer){
			
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

}