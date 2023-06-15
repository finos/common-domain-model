/*******************************************************************************
 * Copyright (c) icmagroup.org  All rights reserved.
 *
 * This file is part of the International Capital Market Association (ICMA)
 * CDM for Repo and Bonds Demo
 *
 * This file is intended for demo purposes only and may not be distributed
 * or used in any commercial capacity other than its intended purpose.
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
import cdm.base.math.Quantity;
import cdm.base.math.QuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.metafields.ReferenceWithMetaProductIdentifier;
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
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import cdm.product.common.settlement.*;
import cdm.product.common.settlement.validation.datarule.PrincipalPaymentPrincipalAmount;
import cdm.product.template.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.opengamma.strata.basics.StandardId;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.GlobalKeyFields;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.BasicReferenceWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;


import java.io.IOException;
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

public class RepoExecutionCreation{
	
	private final PostProcessStep keyProcessor;

    public RepoExecutionCreation() {
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
				String buyerNameStr,
				String sellerLEIStr,
				String sellerNameStr,
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
				String agreementUrl,
				String businessCenter,
				String execVenueCode,
				String execVenueScheme,
				String settlementAgentLEIStr,
				String settlementAgentNameStr,
				String ccpLeiStr,
				String ccpNameStr,
				String  csdParticipantLeiStr,
				String  csdParticipantNameStr,
				String  brokerLeiStr,
				String  brokerNameStr,
				String  tripartyLeiStr,
				String  tripartyNameStr,
				String  clearingMemberLeiStr,
				String  clearingMemberNameStr
	) throws JsonProcessingException {
		
		RepoExecutionCreation rc = new RepoExecutionCreation();

		DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
		ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(tradeDateStr, formatter);
		ZonedDateTime zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		//System.out.println("Trade Date:" + zdtWithZoneOffset);
		//System.out.println(zdtInLocalTimeline);

		IcmaRepoUtil ru = new IcmaRepoUtil();

		FieldWithMetaDate tradeDate = addGlobalKey(FieldWithMetaDate.class,
						ru.createTradeDate(zdtWithZoneOffset.getYear(), zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth()));
		
		TradeIdentifier tradeIdentifier = ru.createRepoTradeIdentifier(tradeUTIStr, "UnqTradIdr", "5493000SCC07UI6DB380");

		//Party 1 is defined in the interest rate payout model as the payer and thus represents the repo seller also referred to as the collateral giver.
        Party party1 = ru.createRepoParty(sellerLEIStr,"LEI",sellerNameStr);
				
		//Party 2 is defined in the interest rate payout model as the receiver and thus represents the repo buyer also referred to as the collateral taker.
        Party party2 = ru.createRepoParty(sellerLEIStr,"LEI",buyerNameStr);

		Party settlementAgent = ru.createRepoParty(settlementAgentLEIStr,"LEI",settlementAgentNameStr);
		Party ccp = ru.createRepoParty(ccpLeiStr,"LEI",ccpNameStr);
		Party csdparticipant = ru.createRepoParty(csdParticipantLeiStr,"LEI",csdParticipantNameStr);
		Party broker = ru.createRepoParty(brokerLeiStr,"LEI",brokerNameStr);
		Party triparty = ru.createRepoParty(tripartyLeiStr,"LEI",tripartyNameStr);
		Party clearingMember = ru.createRepoParty(clearingMemberLeiStr,"LEI",clearingMemberNameStr);

		List<Party> parties = List.of(party1, party2, settlementAgent,ccp, csdparticipant,broker,triparty,clearingMember);

		PartyRole partyRole1 = ru.createRepoPartyRole(party1, buyerLEIStr, "SELLER");
		PartyRole partyRole2 = ru.createRepoPartyRole(party2, sellerLEIStr,"BUYER");
		PartyRole settlementAgentRole = ru.createRepoPartyRole(settlementAgent, settlementAgentLEIStr, "SETTLEMENT_AGENT");
		PartyRole ccpRole = ru.createRepoPartyRole(ccp, ccpLeiStr, "CLEARING_ORGANIZATION");
		PartyRole  tripartyRole = ru.createRepoPartyRole(triparty, tripartyLeiStr, "CLEARING_ORGANIZATION");
		PartyRole  brokerRole = ru.createRepoPartyRole(broker, brokerLeiStr, "EXECUTING_BROKER");
		PartyRole  clearingMemberRole = ru.createRepoPartyRole(clearingMember, clearingMemberLeiStr, "CLEARING_CLIENT");
		PartyRole  csdParticipantRole = ru.createRepoPartyRole(csdparticipant, csdParticipantLeiStr, "CLEARING_CLIENT");

		List<PartyRole> partyRoles = List.of(partyRole1, partyRole2, settlementAgentRole,ccpRole,csdParticipantRole,brokerRole,tripartyRole,clearingMemberRole);
		
		Counterparty counterparty1 = ru.createRepoCounterparty(party1, "PARTY_1");
		Counterparty counterparty2 = ru.createRepoCounterparty(party2, "PARTY_2");
		List<Counterparty> counterparties = List.of(counterparty1, counterparty2);
		
		double cashQuantity = Double.parseDouble(purchasePriceStr);
		double repurchaseQuantity = Double.parseDouble(repurchasePriceStr);
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

		//System.out.println("Purchase Date:" + zdtWithZoneOffset);

		zdtWithZoneOffset = ZonedDateTime.parse(repurchaseDateStr, formatter);
		zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		Date terminationDate = of(zdtWithZoneOffset.getYear(),zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());
		//System.out.println("Repurchase Date:" + zdtWithZoneOffset);

		Product repoProduct = createRepoProduct(effectiveDate, terminationDate, repoRate, haircut , cashCurrencyStr,
				cashQuantity, repurchaseQuantity, collateralCurrencyStr, collateralQuantity, collateralCleanPrice, collateralDirtyPrice,
				repoRate, collateralISINStr, "", businessCenter, deliveryMethodStr, purchaseDateStr, repurchaseDateStr,settlementAgentNameStr);

		String execType;

		if(execVenueCode.equals("OTC"))
			execType = "OFF_FACILITY";
		else
			execType = "ON_VENUE";

		String execVenueName = ""; //Possibility to add lookup based on code;
		ExecutionDetails executionDetails = createRepoExecutionDetails(execType,execVenueCode, execVenueScheme,execVenueName);

		return ExecutionInstruction.builder()
			.setProduct(repoProduct)
			.addPriceQuantity(repoPriceQuantity)
			.addCounterparty(counterparties)
			.addParties(parties)
			.addPartyRoles(partyRoles)
			.setExecutionDetails(executionDetails)
			.setTradeDate(tradeDate)
			.addTradeIdentifier(tradeIdentifier)
			.build();
	}
	
	private Product createRepoProduct(
			Date effectiveDate,
			Date terminationDate,
			double repoRate,
			double haircut,
			String cashCurrency,
			double cashQuantity,
			double repurchaseQuantity,
			String collateralCurrency,
			double collateralQuantity,
			double collteralCleanPrice,
			double collateralDirtyPrice,
			double rate,
			String collateralISINStr,
			String scheme,
			String businessCenter,
			String deliveryMethodStr,
			String purchaseDateStr,
			String repurchaseDateStr,
			String settlementAgentNameStr){
		
		BigDecimal repoRateBD = new BigDecimal(repoRate);
		BigDecimal haircutBD = new BigDecimal(haircut);
		
		ContractualProduct contractualRepoProduct = createContractualRepoProduct(
							effectiveDate, terminationDate, repoRateBD, haircutBD, cashCurrency,
							cashQuantity, repurchaseQuantity,collateralCurrency, collateralQuantity, collteralCleanPrice, collateralDirtyPrice,
							rate, collateralISINStr, scheme,businessCenter,deliveryMethodStr,purchaseDateStr,repurchaseDateStr,settlementAgentNameStr
				);
		
		return Product.builder()
					.setContractualProduct(contractualRepoProduct)
					.build();
		
	}
	
	
	private ContractualProduct createContractualRepoProduct(
			Date effectiveDate,
			Date terminationDate,
			BigDecimal repoRateBD,
			BigDecimal haircutBD,
			String cashCurrency,
			double cashQuantity,
			double repurchaseQuantity,
			String collateralCurrency,
			double collateralQuantity,
			double collteralCleanPrice,
			double collateralDirtyPrice,
			double rate,
			String collateralISINStr,
			String scheme,
			String businessCenter,
			String deliveryMethodStr,
			String purchaseDateStr,
			String repurchaseDateStr,
			String settlementAgentNameStr
	) {
        return ContractualProduct.builder()
				.addProductTaxonomy(ProductTaxonomy.builder()
					.setSource(TaxonomySourceEnum.valueOf("CFI"))
					.setValue(TaxonomyValue.builder()
							.setNameValue("LRSTXD")))
                .setEconomicTerms(createEconomicTerms(effectiveDate, terminationDate, repoRateBD, haircutBD,
						cashCurrency, cashQuantity, repurchaseQuantity, collateralCurrency, collateralQuantity,
						collteralCleanPrice, collateralDirtyPrice, rate, collateralISINStr, scheme,businessCenter,deliveryMethodStr,
				purchaseDateStr, repurchaseDateStr,settlementAgentNameStr))
				.build();

    }

	private EconomicTerms createEconomicTerms(
			Date effectiveDate,
			Date terminationDate,
			BigDecimal repoRateBD,
			BigDecimal haircutBD,
			String cashCurrency,
			double cashQuantity,
			double repurchaseQuantity,
			String collateralCurrency,
			double collateralQuantity,
			double collteralCleanPrice,
			double collateralDirtyPrice,
			double rate,
			String collateralISINStr,
			String scheme,
			String businessCenter,
			String deliveryMethodStr,
			String purchaseDateStr,
			String repurchaseDateStr,
			String settlementAgentNameStr
			){

		BusinessCenterEnum businessCenterEnum = BusinessCenterEnum.valueOf(businessCenter);

		List<? extends FieldWithMetaBusinessCenterEnum> businessCenterEnumList =
				List.of(FieldWithMetaBusinessCenterEnum.builder()
								.setValue(businessCenterEnum)
						.setMeta(MetaFields.builder()
								.build())
						.build());

		AdjustableOrRelativeDate effectiveAdjustableDate = AdjustableOrRelativeDate.builder()
				.setAdjustableDate(AdjustableDate.builder()
						.setUnadjustedDate(effectiveDate)
						.setDateAdjustments(BusinessDayAdjustments.builder()
								//Repo purchase dates are not subject to adjustment unless agree to by counterparties
								.setBusinessDayConvention(BusinessDayConventionEnum.NONE)
								.setBusinessCenters(BusinessCenters.builder()
										.setBusinessCenter(businessCenterEnumList))))
				.setMeta(MetaFields.builder()
						.setScheme(scheme)
						.setExternalKey("PurchaseDate")
						.setGlobalKey("PurchaseDate"))
				.build();

		return EconomicTerms.builder()
				.setEffectiveDate(effectiveAdjustableDate)
				.setPayout(Payout.builder()
						.addInterestRatePayout(createFixedRateRepoPayout(effectiveDate, terminationDate,repoRateBD, cashCurrency,
								cashQuantity, repurchaseQuantity, collateralCurrency, collateralQuantity, collteralCleanPrice, collateralDirtyPrice,
								rate, collateralISINStr, scheme, deliveryMethodStr, purchaseDateStr, repurchaseDateStr, settlementAgentNameStr))
						.addAssetPayout(createRepoCollateralPayout(deliveryMethodStr, effectiveAdjustableDate, collateralISINStr)))
				.setTerminationDate(AdjustableOrRelativeDate.builder()
						.setAdjustableDate(AdjustableDate.builder()
								.setUnadjustedDate(terminationDate)
								.setDateAdjustments(BusinessDayAdjustments.builder()
										.setBusinessDayConvention(BusinessDayConventionEnum.NONE)
								.setMeta(MetaFields.builder()
									.setScheme(scheme)
									.setExternalKey("RepurchaseDate")
									.setGlobalKey("RepurchaseDate")))))
				.setCollateral(Collateral.builder()
						.setCollateralProvisions(CollateralProvisions.builder()
								.setEligibleCollateral(createRepoHairCut(haircutBD))))
				.build();

	}
	private List<EligibleCollateralSpecification> createRepoHairCut(BigDecimal haircut) {

		return List.of(
				EligibleCollateralSpecification.builder()
						.addCriteria(EligibleCollateralCriteria.builder()
							.setTreatment(CollateralTreatment.builder()
								.setValuationTreatment(CollateralValuationTreatment.builder()
									.setHaircutPercentage(haircut)))).build());

	}
	
	private AssetPayout createRepoCollateralPayout(
			String deliveryMethod,
			AdjustableOrRelativeDate effectiveDate,
			String collateralISINStr
	){

		ProductIdentifier collateralId = ProductIdentifier.builder()
										.setIdentifier(FieldWithMetaString.builder()
										.setValue(collateralISINStr)
										.build());

		GlobalKey collateralIdKey = addGlobalKey(ProductIdentifier.class, collateralId);

		return AssetPayout.builder()
                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_1)
                        .setReceiver(CounterpartyRoleEnum.PARTY_2))
				.setAssetLeg(createAssetPayoutLeg(deliveryMethod, effectiveDate))
				.setSecurityInformation(Product.builder()
						.setSecurity(Security.builder()
								.setProductIdentifier(List.of(ReferenceWithMetaProductIdentifier.builder()
										.setValue(ProductIdentifier.builder()
												.setSource(ProductIdTypeEnum.ISIN)
												.setIdentifier(FieldWithMetaString.builder()
														.setValue(collateralISINStr)
														.setMeta(MetaFields.builder()
																.setGlobalKey(getGlobalReference(collateralIdKey)))
																.build()))
								)
						))

				.build());
		
	}

	private List<AssetLeg> createAssetPayoutLeg (
			String deliveryMethod,
			AdjustableOrRelativeDate effectiveDate){

		return List.of(AssetLeg.builder()
				.setDeliveryMethod(DeliveryMethodEnum.valueOf(deliveryMethod))
				.setSettlementDate(AdjustableOrRelativeDate.builder()
						.setRelativeDate(AdjustedRelativeDateOffset.builder()
								.setPeriod(PeriodEnum.D)
								.setPeriodMultiplier(0)
								.setBusinessDayConvention(BusinessDayConventionEnum.NONE)
								.setDateRelativeTo(BasicReferenceWithMetaDate.builder()
										.setExternalReference("PurchaseDate")
										.setGlobalReference(getGlobalReference(effectiveDate)))))
				.build());


	}
	
	private InterestRatePayout createFixedRateRepoPayout(
		Date effectiveDate,
		Date terminationDate,
		BigDecimal fixedRate,
		String cashCurrency,
		double cashQuantity,
		double repurchaseQuantity,
		String collateralCurrency,
		double collateralQuantity,
		double collteralCleanPrice,
		double collateralDirtyPrice,
		double rate,
		String collateralISINStr,
		String scheme,
		String deliveryMethodStr,
		String purchaseDateStr,
		String repurchaseDateStr,
		String settlementAgent
		)
		{

			IcmaRepoUtil ru = new IcmaRepoUtil();
			if (deliveryMethodStr.equals("DVP"))
				deliveryMethodStr = "DELIVERY_VERSUS_PAYMENT";
			String settlementAgentType;



        	return InterestRatePayout.builder()
                .setPriceQuantity(createResolveableLoanPriceQuantity(
						cashCurrency,
						cashQuantity,
						scheme))
				.setPrincipalPayment(PrincipalPayments.builder()
						.setInitialPayment(Boolean.TRUE)
						.setFinalPayment(Boolean.TRUE)
						.setIntermediatePayment(Boolean.FALSE)
						.setPrincipalPaymentSchedule(PrincipalPaymentSchedule.builder()
								.setInitialPrincipalPayment(PrincipalPayment.builder()
										.setPrincipalAmount(Money.builder()
												.setValue(BigDecimal.valueOf(cashQuantity))
												.setUnit(UnitType.builder()
													.setCurrencyValue(cashCurrency))))
								.setFinalPrincipalPayment(PrincipalPayment.builder()
								.setPrincipalAmount(Money.builder()
										.setValue(BigDecimal.valueOf(repurchaseQuantity))
										.setUnit(UnitType.builder()
												.setCurrencyValue(cashCurrency))))
								.build())
						.build())
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
                                                        .setPriceExpression(PriceExpression.builder()
                                                                .setPriceType(PriceTypeEnum.INTEREST_RATE)))))))
                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_1)
                        .setReceiver(CounterpartyRoleEnum.PARTY_2))
				.setSettlementTerms(SettlementTerms.builder()
						.addCashSettlementTerms(CashSettlementTerms.builder()
								.setCashSettlementMethod(CashSettlementMethodEnum.CASH_PRICE_METHOD))
						.setSettlementType(SettlementTypeEnum.CASH)
						.setTransferSettlementType(TransferSettlementEnum.valueOf(deliveryMethodStr))
						.setSettlementCurrency(FieldWithMetaString.builder()
								.setValue(cashCurrency)
								.setMeta(MetaFields.builder()
										.setScheme(scheme)).build())
						.setSettlementDate(SettlementDate.builder()
								.setAdjustableOrRelativeDate(ru.createAdjustableOrAdjustedOrRelativeDate(purchaseDateStr)))
                .build());
    }


	private ResolvablePriceQuantity createResolveableLoanPriceQuantity(
			String cashCurrencyStr,
			double cashQuantity,
			String scheme)
	{

		return ResolvablePriceQuantity.builder()
				.setMeta(MetaFields.builder()
						.setScheme(scheme)
						.setExternalKey("PurchasePrice"))
						.setResolvedQuantity(Quantity.builder()
								.setValue(BigDecimal.valueOf(cashQuantity))
								.setUnit(UnitType.builder()
										.setCurrencyValue(cashCurrencyStr)))
						.setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
								.setReference(Reference.builder()
										.setScope("DOCUMENT")
										.setReference("quantity-1"))
								.setValue(NonNegativeQuantitySchedule.builder()
									.setValue(BigDecimal.valueOf(cashQuantity))
										.setUnit(UnitType.builder()
												.setCurrencyValue(cashCurrencyStr))))
						.build();

	}

	private ResolvablePriceQuantity createResolveableLoanPriceQuantityX(
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

		return ResolvablePriceQuantity.builder()
				.setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
						.setReference(Reference.builder()
								.setScope("DOCUMENT")
								.setReference("quantity-2"))
						.setValue(NonNegativeQuantitySchedule.builder()
								.setValue(BigDecimal.valueOf(cashQuantity))
								.setUnit(UnitType.builder()
										.setCurrencyValue(cashCurrencyStr))
								.build()));

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
                                .setPriceExpression(PriceExpression.builder().setPriceType(PriceTypeEnum.INTEREST_RATE)))
								.build())
								
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
                                .setPriceExpression(PriceExpression.builder()
									.setPriceType(PriceTypeEnum.ASSET_PRICE)
									.setPriceExpression(PriceExpressionEnum.PERCENTAGE_OF_NOTIONAL)
									.setCleanOrDirty(CleanOrDirtyPriceEnum.DIRTY)))
						.build())
									
                .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                        .setValue(NonNegativeQuantitySchedule.builder()
                                .setValue(BigDecimal.valueOf(collateralQuantity))
                                .setUnit(UnitType.builder()
                                        .setCurrencyValue(collateralCurrencyStr)))
						.build())

				.build();
                     
    }
	

	

	
	private  ExecutionDetails createRepoExecutionDetails(String etype, String venueId, String scheme, String venueName){


		return ExecutionDetails.builder()
				.setExecutionType(ExecutionTypeEnum.valueOf(etype))
				.setExecutionVenue(LegalEntity.builder()
						.setEntityId(List.of(FieldWithMetaString.builder()
								.setValue(venueId)
								.setMeta(MetaFields.builder()
										.setScheme(scheme)).build()))
						.setName(FieldWithMetaString.builder()
								.setValue(venueName)
								.setMeta(MetaFields.builder()
										.setScheme(scheme)).build()))
				.build();
	}
	


}