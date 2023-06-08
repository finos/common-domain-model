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
import cdm.base.staticdata.asset.common.*;
import cdm.event.common.ExecutionInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeIdentifier;
import cdm.event.common.ExecutionDetails;
import cdm.event.common.*;
import cdm.event.common.functions.*;
import cdm.event.common.metafields.ReferenceWithMetaTradeState;
import cdm.event.position.PositionStatusEnum;
import cdm.event.workflow.*;
import cdm.event.workflow.functions.Create_WorkflowStep;
import cdm.legaldocumentation.common.LegalAgreement;
import cdm.legaldocumentation.common.functions.Create_LegalAgreementWithPartyReference;
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
import cdm.product.template.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.functions.*;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.validation.ValidatorFactory;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.event.common.PrimitiveInstruction;
import cdm.event.common.PrimitiveInstruction.PrimitiveInstructionBuilder;
import cdm.event.common.QuantityChangeInstruction;
import cdm.event.common.TermsChangeInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.ContractualProduct;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.rosetta.model.lib.mapper.MapperC;
import com.rosetta.model.lib.mapper.MapperS;
import cdm.event.workflow.functions.Create_AcceptedWorkflowStepFromInstruction;
import cdm.event.common.BusinessEvent;
import cdm.event.common.BusinessEvent.BusinessEventBuilder;
import cdm.event.common.EventIntentEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyIdentifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.metafields.MetaFields;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.hashing.ReferenceResolverProcessStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import org.isda.cdm.processor.CdmReferenceConfig;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import java.math.BigDecimal;
import java.util.List;
import cdm.event.common.ExecutionInstruction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import cdm.event.common.TradeState;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationResult;
import com.regnosys.rosetta.common.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.CdmRuntimeModule;
import cdm.event.common.BusinessEvent;
import cdm.event.common.BusinessEvent.BusinessEventBuilder;
import cdm.event.common.EventIntentEnum;
import cdm.event.common.ExerciseInstruction;
import cdm.event.common.Instruction;
import cdm.event.common.PrimitiveInstruction;
import cdm.event.common.SplitInstruction;
import cdm.event.common.TradeState;
import cdm.event.common.metafields.ReferenceWithMetaTradeState;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.rosetta.model.lib.functions.ModelObjectValidator;
import com.rosetta.model.lib.functions.RosettaFunction;
import com.rosetta.model.lib.mapper.MapperC;
import com.rosetta.model.lib.mapper.MapperS;
import com.rosetta.model.lib.records.Date;
import java.util.List;
import java.util.Optional;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.event.common.PrimitiveInstruction;
import cdm.event.common.PrimitiveInstruction.PrimitiveInstructionBuilder;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.ContractualProduct;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.rosetta.model.lib.functions.ConditionValidator;
import com.rosetta.model.lib.functions.ModelObjectValidator;
import com.rosetta.model.lib.functions.RosettaFunction;
import com.rosetta.model.lib.mapper.MapperC;
import com.rosetta.model.lib.mapper.MapperS;
import com.rosetta.model.lib.functions.*;
import java.util.List;
import java.util.Optional;
import cdm.event.common.*;
import com.rosetta.model.lib.expression.*;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.meta.InterestRatePayoutMeta;
import cdm.product.template.Payout;
import cdm.product.template.validation.datarule.PayoutDayCountFraction;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.lib.validation.ValidatorFactory;


import java.math.BigDecimal;
import java.util.Comparator;

import static com.rosetta.model.lib.expression.ExpressionOperators.exists;
import static com.rosetta.model.lib.records.Date.of;

public class RepoLifeCycle {
	
	private final PostProcessStep keyProcessor;
    public RepoLifeCycle() {keyProcessor = new GlobalKeyProcessStep(NonNullHashCollector::new);
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
	
	private Workflow repoWorkflow;



    public String RepoExecution (
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

        //Create_ExecutionInstruction instruction = new Create_ExecutionInstruction.Create_ExecutionInstructionDefault();
		RepoExecutionCreation repoExecutionInstructionCreation = new RepoExecutionCreation();
		
		ExecutionInstruction repoExecutionInstruction = repoExecutionInstructionCreation.createRepoExecutionInstruction(
		tradeDateStr,					// tradeDateStr = .getText();
		purchaseDateStr,				// purchaseDateStr
		repurchaseDateStr,				// repurchaseDateStr
		tradeUTIStr,  					// tradeUTIStr
		buyerLEIStr, 					// buyerLEIStr
		sellerLEIStr, 					// sellerLEIStr
		collateralDescriptionStr,		// collateralDescriptionStr,
		collateralISINStr,				// collateralISINStr
		collateralQuantityStr,			// collateralQuantityStr
		collateralCleanPriceStr,			// collateralQuantityStr,
		collateralDirtyPriceStr,		// collateralDirtyPriceStr,
		collateralAdjustedValueStr,		// collateralAdjustedValueStr,
		collateralCurrencyStr,			// collateralCurrencyStr
		repoRateStr,					// repoRateStr
		cashCurrencyStr,				// cashCurrencyStr
		cashQuantityStr,				// cashQuantityStr
		haircutStr,						// haircutStr,
		termTypeStr,					// termTypeStr,
		terminationOptionStr,			// terminationOptionStr,
		noticePeriodStr,				// noticePeriodStr,
		deliveryMethodStr,				// deliveryMethodStr,
		substitutionAllowedStr,			// substitutionAllowedStr,
		rateTypeStr,					// rateTypeStr,
		dayCountFractionStr,			// dayCountFractionStr,
		purchasePriceStr, 				// purchasePriceStr,
		repurchasePriceStr,				// repurchasePriceStr
		agreementNameStr,
		agreementGoverningLawStr,
		agreementVintageStr,
		agreementPublisherStr,
		agreementDateStr,
		agreementIdentifierStr,
		agreementEffectiveDate,
		agreementUrl
		);


		Injector injector = Guice.createInjector(new CdmRuntimeModule());

		//Create a primitive execution instruction
		PrimitiveInstruction repoPrimitiveInstruction = PrimitiveInstruction.builder()
				.setExecution(repoExecutionInstruction);

		//Create an instruction from primitive. Before state is null
		Instruction instruction = Instruction.builder()
				.setPrimitiveInstruction(repoPrimitiveInstruction)
				.setBefore(null)
				.build();

		Date effectiveDate = repoExecutionInstruction.getProduct().getContractualProduct().getEconomicTerms().getEffectiveDate().getAdjustableDate().getUnadjustedDate();
		Date eventDate = repoExecutionInstruction.getProduct().getContractualProduct().getEconomicTerms().getEffectiveDate().getAdjustableDate().getUnadjustedDate();

		Create_Execution.Create_ExecutionDefault repoExecution = new Create_Execution.Create_ExecutionDefault();
		injector.injectMembers(repoExecution);
		TradeState tradeState = repoExecution.evaluate(repoExecutionInstruction);

		RepoLegalAgreementCreation rlg = new RepoLegalAgreementCreation();
		LegalAgreement repoLegalAgreement = rlg.createLegalAgreementObject(
				agreementNameStr,
				agreementGoverningLawStr,
				agreementVintageStr,
				agreementPublisherStr,
				agreementDateStr,
				agreementIdentifierStr,
				agreementEffectiveDate,
				agreementUrl
		);

		ContractFormationInstruction contractFormationInstruction = ContractFormationInstruction.builder()
				.addLegalAgreement(repoLegalAgreement)
				.build();

		repoPrimitiveInstruction = PrimitiveInstruction.builder().setContractFormation(contractFormationInstruction);
		instruction = Instruction.builder()
				.setPrimitiveInstruction(repoPrimitiveInstruction)
				.setBeforeValue(tradeState)
				.build();

		//String repoExecutionInstructionJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(instruction);
		//System.out.println(repoExecutionInstructionJson);

		List<Instruction> instructionList = List.of(instruction);

		Create_ContractFormation.Create_ContractFormationDefault cf = new Create_ContractFormation.Create_ContractFormationDefault();
		injector.injectMembers(cf);
		TradeState afterTradeState = cf.evaluate(contractFormationInstruction, tradeState);

		PrimitiveInstruction.PrimitiveInstructionBuilder primitiveInstruction = PrimitiveInstruction.builder();

		Create_Instruction ci = new Create_Instruction.Create_InstructionDefault();
		injector.injectMembers(ci);
		instructionList = List.of(ci.evaluate(tradeState, primitiveInstruction));

		Create_BusinessEvent be= new Create_BusinessEvent.Create_BusinessEventDefault();
		injector.injectMembers(be);
		BusinessEvent businessEvent = be.evaluate(instructionList, null, eventDate, effectiveDate);


		String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);

		return repoBusinesseventJson;
		

		
    }


    public  String RepoRoll (
			String afterTradeStateStr,
			String purchasePriceStr,
			String cashQuantityStr,
			String repurchasePriceStr,
			String cashCurrencyStr,
			String collateralISINStr,
			String collateralQuantityStr,
			String collateralCleanPriceStr,
			String collateralDirtyPriceStr,
			String collateralAdjustedValueStr,
			String collateralCurrencyStr,
			String repoRateStr,
			String haircutStr,
			String purchaseDateStr,
			String repurchaseDateStr) throws JsonProcessingException {


			//List<? extends TradeState> tradeStateList,
			//AdjustableOrRelativeDate effectiveRollDate,
			//AdjustableOrRelativeDate terminationDate,
			//List<PriceQuantity> newPriceQuantity) throws JsonProcessingException {

		Injector injector = Guice.createInjector(new CdmRuntimeModule());

		IcmaRepoUtil ru = new IcmaRepoUtil();

		ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
		TradeState tradeState = new TradeState.TradeStateBuilderImpl();
		TradeState beforeTradeState = rosettaObjectMapper.readValue(afterTradeStateStr, tradeState.getClass());

		//set before trade state
		//TradeState beforeTradeState = this.tradeState;

		DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
		ZonedDateTime zdtWithZoneOffset = ZonedDateTime.parse(purchaseDateStr, formatter);
		ZonedDateTime zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());

		zdtWithZoneOffset = ZonedDateTime.parse(purchaseDateStr, formatter);
		zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		Date effectiveRollDate = of(zdtWithZoneOffset.getYear(),zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());

		AdjustableOrRelativeDate AdjustableOrRelativeDateEffectiveRollDate = AdjustableOrRelativeDate.builder()
				.setAdjustableDate(AdjustableDate.builder()
						.setUnadjustedDate(effectiveRollDate)
						.setDateAdjustments(BusinessDayAdjustments.builder()
								.setBusinessDayConvention(BusinessDayConventionEnum.NONE)));



		//Set the termination date
		zdtWithZoneOffset = ZonedDateTime.parse(repurchaseDateStr, formatter);
		zdtInLocalTimeline = zdtWithZoneOffset.withZoneSameInstant(ZoneId.systemDefault());
		Date terminationDate = of(zdtWithZoneOffset.getYear(),zdtWithZoneOffset.getMonthValue(), zdtWithZoneOffset.getDayOfMonth());

		AdjustableOrRelativeDate AdjustableOrRelativeDateTerminationDateDate = AdjustableOrRelativeDate.builder()
				.setAdjustableDate(AdjustableDate.builder()
						.setUnadjustedDate(terminationDate)
						.setDateAdjustments(BusinessDayAdjustments.builder()
								.setBusinessDayConvention(BusinessDayConventionEnum.NONE)));

		//List<PriceQuantity> priceQuantity =  ru.createRepoPriceQuantity();

		List<PriceQuantity> priceQuantityList = ru.createRepoPriceQuantity(
				cashQuantityStr,
				cashCurrencyStr,
				collateralISINStr,
				collateralQuantityStr,
				collateralCurrencyStr,
				repoRateStr,
				haircutStr,
				collateralCleanPriceStr,
				collateralDirtyPriceStr);


		Create_RollPrimitiveInstruction.Create_RollPrimitiveInstructionDefault cr = new Create_RollPrimitiveInstruction.Create_RollPrimitiveInstructionDefault();
		injector.injectMembers(cr);
		PrimitiveInstruction pi = cr.evaluate(beforeTradeState, AdjustableOrRelativeDateEffectiveRollDate, AdjustableOrRelativeDateTerminationDateDate, priceQuantityList);

		Create_Instruction ci = new Create_Instruction.Create_InstructionDefault();
		injector.injectMembers(ci);
		List<Instruction> instruction = List.of(ci.evaluate(beforeTradeState, pi));


		Date eventDate = AdjustableOrRelativeDateEffectiveRollDate.getAdjustableDate().getUnadjustedDate();
		Date effectiveDate = AdjustableOrRelativeDateEffectiveRollDate.getAdjustableDate().getUnadjustedDate();
		Create_BusinessEvent be= new Create_BusinessEvent.Create_BusinessEventDefault();
		injector.injectMembers(be);
		BusinessEvent businessEvent = be.evaluate(instruction, null, eventDate, effectiveDate);

		String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);

		return repoBusinesseventJson;

    }
	

	public  String RepoEarlyTermination (
			String tradeStateStr,
			String newRepurchasePriceStr,
			String cancellationDateStr,
			String effectiveDateStr,
			String eventDateStr
	) throws JsonProcessingException {

			IcmaRepoUtil ru = new IcmaRepoUtil();

			Create_CancellationPrimitiveInstruction cf =  new Create_CancellationPrimitiveInstruction.Create_CancellationPrimitiveInstructionDefault();
			Injector injector = Guice.createInjector(new CdmRuntimeModule());
			injector.injectMembers(cf);

			BigDecimal newRepurchasePrice = new BigDecimal(newRepurchasePriceStr);

			ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
			TradeState tradeStateObj = new TradeState.TradeStateBuilderImpl();
			TradeState tradeState  = rosettaObjectMapper.readValue(tradeStateStr, tradeStateObj.getClass());

			AdjustableOrRelativeDate cancellationDate = ru.createAdjustableOrRelativeDate(cancellationDateStr);

			PrimitiveInstruction pi = cf.evaluate(tradeState, newRepurchasePrice, cancellationDate);

			Create_Instruction ci = new Create_Instruction.Create_InstructionDefault();
			injector.injectMembers(ci);
			List<Instruction> instruction = List.of(ci.evaluate(tradeState, pi));

			Create_BusinessEvent be= new Create_BusinessEvent.Create_BusinessEventDefault();
			injector.injectMembers(be);

			Date effectiveDate = ru.createCDMDate(effectiveDateStr);
			Date eventDate = ru.createCDMDate(eventDateStr);
			BusinessEvent businessEvent = be.evaluate(instruction, null, eventDate, effectiveDate);

			String repoBusinesseventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);

			return repoBusinesseventJson;
    }
	
	public BusinessEvent OnDemandRateChange (
			String effectiveDateStr,
			String eventDateStr
	) throws JsonProcessingException {


		BusinessEvent businessEvent = new BusinessEvent.BusinessEventBuilderImpl();
		return businessEvent;

    }
	
	public BusinessEvent OnDemandInterestPayment (
			String effectiveDateStr,
			String eventDateStr
	) throws JsonProcessingException {

		BusinessEvent businessEvent = new BusinessEvent.BusinessEventBuilderImpl();
		return businessEvent;

    }
	
	public BusinessEvent ShapingInstruction (
			String effectiveDateStr,
			String eventDateStr
    ) throws JsonProcessingException {


		BusinessEvent businessEvent = new BusinessEvent.BusinessEventBuilderImpl();
		return businessEvent;

    }
	
	public BusinessEvent PairOffInstruction (
			String effectiveDateStr,
			String eventDateStr
	) throws JsonProcessingException {


		BusinessEvent businessEvent = new BusinessEvent.BusinessEventBuilderImpl();
		return businessEvent;

    }

	public BusinessEvent SftrReport (
			String effectiveDateStr,
			String eventDateStr
	) throws JsonProcessingException {


		BusinessEvent businessEvent = new BusinessEvent.BusinessEventBuilderImpl();
		return businessEvent;

	}

	public String InitRepoWorkflow (String party1, String party2, BusinessEvent businessEvent) throws JsonProcessingException {

		Workflow.WorkflowBuilderImpl repoWorkflow = new Workflow.WorkflowBuilderImpl();

		repoWorkflow.setSteps(
				this.buildRepoWorkflowList(party1, party2, businessEvent)
		);

		String repoWorkflowJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(repoWorkflow);

		return repoWorkflowJson;
	}


	public List<WorkflowStep> buildRepoWorkflowList(String party1LEIStr, String party2LEIStr, BusinessEvent businessEvent){

			//Repo Execution Step
			Create_WorkflowStep wfs = new Create_WorkflowStep.Create_WorkflowStepDefault();
			Injector injector = Guice.createInjector(new CdmRuntimeModule());
			injector.injectMembers(wfs);

			DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
			LocalDateTime localDateTime = LocalDateTime.now();
			String eventDateTime = localDateTime.format(eventDateFormat);
			EventTimestamp eventTimeStamp = new EventTimestamp.EventTimestampBuilderImpl();
			eventTimeStamp.getDateTime();
			List<EventTimestamp> eventTimestampList = List.of(eventTimeStamp);

			Identifier eventIdentifier = new Identifier.IdentifierBuilderImpl();
			eventIdentifier.hashCode();
			List<Identifier> eventIdList = List.of(eventIdentifier);

			IcmaRepoUtil ru = new IcmaRepoUtil();

			Party party1 = addGlobalKey(Party.class,
				ru.createRepoParty(party1LEIStr,"","BUYER_BANK"));

			//Party 2 is defined in the interest rate payout model as the payer and thus represents the repo seller also referred to as the collateral giver.
			Party party2 = addGlobalKey(Party.class,
				ru.createRepoParty(party2LEIStr,"","SELLER_BANK"));
			List<Party> parties = List.of(party1, party2);

			MessageInformation messageInformation = null;
			List<Account> accountList = null;
			WorkflowStep previousWorkflowStep = null;

			List<WorkflowStep> repoWorkflowList = List.of(
					wfs.evaluate(
							messageInformation,
							eventTimestampList,
							eventIdList,
							parties,
							accountList,
							previousWorkflowStep,
							ActionEnum.NEW,
							businessEvent
					)
			);

		return repoWorkflowList;

		}



}
