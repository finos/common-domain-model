package cdm.event.workflow.processor;

import cdm.base.datetime.*;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import cdm.base.staticdata.asset.common.ProductIdTypeEnum;
import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.asset.common.SecurityTypeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.event.common.ExecutionTypeEnum;
import cdm.event.common.TradeState;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.WorkflowStep;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.settlement.DeliveryMethodEnum;
import cdm.product.template.CollateralTypeEnum;
import cdm.product.template.DurationTypeEnum;
import cdm.product.template.EconomicTerms;
import cdm.product.template.TradableProduct;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Streams;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.flat.Capture;
import com.regnosys.rosetta.common.translation.flat.FlatFileMappingProcessor;
import com.regnosys.rosetta.common.translation.flat.IndexCapturePath;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static cdm.product.template.SecurityFinancePayout.SecurityFinancePayoutBuilder;

@SuppressWarnings("unused")
public class FISMapperMappingProcessor extends FlatFileMappingProcessor<WorkflowStep.WorkflowStepBuilder> {

	private static final String BORROWER = "Borrower";
	private static final String AGENT_LENDER = "AGENT_LENDER";

	protected void doHardCodings(WorkflowStep.WorkflowStepBuilder workflow) {

	}

	private void staticMappings(PathValue<TradeState.TradeStateBuilder> tradeState) {

		tradeState.getValue().getOrCreateTrade().getOrCreateExecutionDetails().setExecutionType(ExecutionTypeEnum.OFF_FACILITY);

		//TradProd
		getTradableProduct(tradeState).getValue().getOrCreateCounterparty(0).setRole(CounterpartyRoleEnum.PARTY_1);

		getTradableProduct(tradeState).getValue().getOrCreateCounterparty(1).setRole(CounterpartyRoleEnum.PARTY_2);
		getTradableProduct(tradeState).getValue().getOrCreateCounterparty(1).getOrCreatePartyReference().setExternalReference(BORROWER);

		//IRP
		getIRP(tradeState).getValue().getOrCreateCalculationPeriodDates().getOrCreateCalculationPeriodFrequency().setPeriod(PeriodExtendedEnum.T);
		getIRP(tradeState).getValue().getOrCreateCalculationPeriodDates().getOrCreateCalculationPeriodFrequency().setPeriodMultiplier(1);
		getIRP(tradeState).getValue().getOrCreateCalculationPeriodDates().getOrCreateCalculationPeriodFrequency().setRollConvention(RollConventionEnum.NONE);

		getIRP(tradeState).getValue()
				.getOrCreatePaymentDates()
				.getOrCreatePaymentDatesAdjustments()
				.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING);
		getIRP(tradeState).getValue()
				.getOrCreatePaymentDates()
				.getOrCreatePaymentDatesAdjustments()
				.getOrCreateBusinessCenters()
				.getOrCreateBusinessCenter(0)
				.setValue(BusinessCenterEnum.EUTA);
		getIRP(tradeState).getValue().getOrCreatePaymentDates().getOrCreatePaymentFrequency().setPeriod(PeriodExtendedEnum.T);
		getIRP(tradeState).getValue().getOrCreatePaymentDates().getOrCreatePaymentFrequency().setPeriodMultiplier(1);

		//sec lending payout
		getSecPO(tradeState).getValue().getOrCreateDurationType().setDurationType(DurationTypeEnum.OPEN);
		getSecPO(tradeState).getValue().getOrCreateSecurityInformation().getOrCreateSecurity().setSecurityType(SecurityTypeEnum.EQUITY);

		getSecPO(tradeState).getValue().getOrCreateSecurityFinanceLeg(0).getOrCreatePayerReceiver().setPayer(CounterpartyRoleEnum.PARTY_1);
		getSecPO(tradeState).getValue().getOrCreateSecurityFinanceLeg(0).getOrCreatePayerReceiver().setReceiver(CounterpartyRoleEnum.PARTY_2);
	}

	public FISMapperMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);

		Multimap<String, MappingConsumer<TradeState.TradeStateBuilder>> commonMappings = buildCommonMappings();
		buildTradeStateMappings(commonMappings);
		buildAllocationMappings(commonMappings);

		//need to combine trade date and time so capture them both
		buildTradeStateMapping("Activity_Input_Date", capture("ActivityDate"));
		buildTradeStateMapping("Activity_Time_In_Milliseconds", capture("ActivityTime"));
		addPostCaptureProcessors((captures, workflow) -> {
			LocalTime time = LocalTime.parse(any(captures.get("ActivityTime")).get().getValue(), localTimeParser);
			LocalDate data = LocalDate.parse(any(captures.get("ActivityDate")).get().getValue(), dateParser);
			ZonedDateTime ts = ZonedDateTime.of(data, time, ZoneId.of("Europe/London"));
			workflow.getOrCreateTimestamp(0).setDateTime(ts);
			workflow.getOrCreateTimestamp(0).setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME);
		});

		buildTradeStateMapping("Collateral_Type_IND", capture("CollateralType"));
		buildTradeStateMapping("Activity_Rate", capture("ActivityRate"));
		addPostCaptureProcessors((captures, workflow) -> {
			Optional<Capture> colatCap = any(captures.get("CollateralType"));
			Optional<Capture> rateCap = any(captures.get("ActivityRate"));
			if (colatCap.isPresent() && rateCap.isPresent()) {
				String colat = colatCap.get().getValue();
				BigDecimal rate = parseDecimal(rateCap.get().getValue());
				Stream<InterestRatePayout.InterestRatePayoutBuilder> allIRPs =
						Streams.concat(Stream.of(getTradeState(new PathValue<>(BASE_PATH, workflow)).getValue()),
								Streams.stream(Iterables.skip(workflow.getOrCreateBusinessEvent().getOrCreatePrimitives(0).getOrCreateSplit().getAfter(), 1)))
								.map(s -> new PathValue<>(BASE_PATH, s))
								.map(this::getIRP)
								.map(PathValue::getValue);
				if ("C".equals(colat) && rate.compareTo(BigDecimal.ZERO) >= 0) {
					allIRPs.forEach(irp -> {
						irp.getOrCreatePayerReceiver().setPayer(CounterpartyRoleEnum.PARTY_1);
						irp.getOrCreatePayerReceiver().setReceiver(CounterpartyRoleEnum.PARTY_2);
					});
				} else {
					allIRPs.forEach(irp -> {
						irp.getOrCreatePayerReceiver().setPayer(CounterpartyRoleEnum.PARTY_2);
						irp.getOrCreatePayerReceiver().setReceiver(CounterpartyRoleEnum.PARTY_1);
					});
				}
			}
		});
	}

	private Multimap<String, MappingConsumer<TradeState.TradeStateBuilder>> buildCommonMappings() {
		Multimap<String, MappingConsumer<TradeState.TradeStateBuilder>> commonMappings = HashMultimap.create();

		commonMappings.put("Own_Cpty_LEI", (indexes, value, tradeState) -> {
			Party.PartyBuilder party = tradeState.getValue().getOrCreateTrade().getOrCreateParty(1);
			party.getOrCreatePartyId(0).setValue(value);
			party.getOrCreateMeta().setExternalKey(AGENT_LENDER);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Own_Client_Identifier", (indexes, value, tradeState) -> {
			tradeState.getValue()
					.getOrCreateTrade()
					.getOrCreateParty(1)
					.getOrCreateName()
					.setValue(value);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Client_Identifier", (indexes, value, tradeState) -> {
			Party.PartyBuilder party = tradeState.getValue().getOrCreateTrade().getOrCreateParty(2);
			party.getOrCreateName().setValue(value);
			party.getOrCreateMeta().setExternalKey(BORROWER);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Cpty_LEI", (indexes, value, tradeState) -> {
			FieldWithMetaString.FieldWithMetaStringBuilder partyId = tradeState.getValue().getOrCreateTrade().getOrCreateParty(2).getOrCreatePartyId(0);
			partyId.setValue(value);
			//TODO should hard code the scheme
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Effective_Date", (indexes, value, tradeState) -> {
			AdjustableDate.AdjustableDateBuilder orCreateAdjustableDate = getEcTerms(tradeState).getValue()
					.getOrCreateEffectiveDate()
					.getOrCreateAdjustableDate();
			orCreateAdjustableDate.setUnadjustedDate(parseISODate(value));
			orCreateAdjustableDate.getOrCreateDateAdjustments().setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Activity_Rate", (indexes, value, tradeState) -> {
			// key
			PathValue<PriceQuantity.PriceQuantityBuilder> pq = getPriceQuantityForInterestRatePayout(tradeState);
			pq.getValue()
					.getOrCreatePrice(0)
					.getOrCreateValue()
					.setAmount(parseDecimal(value))
					.setPriceType(PriceTypeEnum.INTEREST_RATE);
			// reference
			Reference.ReferenceBuilder reference = Reference.builder();
			PathValue<InterestRatePayout.InterestRatePayoutBuilder> irp = getIRP(tradeState);
			irp.getValue()
					.getOrCreateRateSpecification()
					.getOrCreateFixedRate()
					.getOrCreateRateSchedule()
					.getOrCreateInitialValue()
					.setReference(reference);
			return List.of(
					new PathValue<>(pq.getModelPath().append(Path.parse("price[0].value.amount")), value),
					new PathValue<>(irp.getModelPath().append(Path.parse("rateSpecification.fixedRate.rateSchedule.initialValue")), reference));
		});

		commonMappings.put("Loan_Value", (indexes, value, tradeState) -> {
			// key
			PathValue<PriceQuantity.PriceQuantityBuilder> pq = getPriceQuantityForInterestRatePayout(tradeState);
			pq.getValue()
					.getOrCreateQuantity(0)
					.getOrCreateValue()
					.setAmount(parseDecimal(value));
			// reference
			Reference.ReferenceBuilder reference = Reference.builder();
			PathValue<InterestRatePayout.InterestRatePayoutBuilder> irp = getIRP(tradeState);
			irp.getValue()
					.getOrCreatePayoutQuantity()
					.getOrCreateQuantitySchedule()
					.getOrCreateInitialQuantity()
					.setReference(reference);
			return List.of(
					new PathValue<>(pq.getModelPath().append(Path.parse("quantity[0].value.amount")), value),
					new PathValue<>(irp.getModelPath().append(Path.parse("payoutQuantity.quantitySchedule.initialQuantity")), reference));
		});

		commonMappings.put("Loan_Value_Currency", (indexes, value, tradeState) -> {
			UnitType.UnitTypeBuilder currencyUnit = UnitType.builder().setCurrency(FieldWithMetaString.builder().setValue(value));
			PathValue<PriceQuantity.PriceQuantityBuilder> pq = getPriceQuantityForInterestRatePayout(tradeState);
			pq.getValue()
					.getOrCreatePrice(0)
					.getOrCreateValue()
					.setUnitOfAmount(currencyUnit)
					.setPerUnitOfAmount(currencyUnit);
			pq.getValue()
					.getOrCreateQuantity(0)
					.getOrCreateValue()
					.setUnitOfAmount(currencyUnit);
			return List.of(
					new PathValue<>(pq.getModelPath().append(Path.parse("price[0].value.unitOfAmount.currency.value")), value),
					new PathValue<>(pq.getModelPath().append(Path.parse("price[0].value.perUnitOfAmount.currency.value")), value),
					new PathValue<>(pq.getModelPath().append(Path.parse("quantity[0].value.unitOfAmount.currency.value")), value));
		});

		commonMappings.put("Activity_Price", (indexes, value, tradeState) -> {
			getPriceQuantityForSecurityFinancePayout(tradeState)
					.getValue()
					.getOrCreatePrice(0)
					.getOrCreateValue()
					.setAmount(parseDecimal(value))
					.setPriceType(PriceTypeEnum.NET_PRICE)
					.setPerUnitOfAmount(UnitType.builder()
							.setFinancialUnit(FinancialUnitEnum.SHARE));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Security_Prcing_Curr", (indexes, value, tradeState) -> {
			getPriceQuantityForSecurityFinancePayout(tradeState)
					.getValue()
					.getOrCreatePrice(0)
					.getOrCreateValue()
					.getOrCreateUnitOfAmount()
					.getOrCreateCurrency()
					.setValue(value);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Quantity", (indexes, value, tradeState) -> {
			getPriceQuantityForSecurityFinancePayout(tradeState)
					.getValue()
					.getOrCreateQuantity(0)
					.getOrCreateValue()
					.setAmount(parseDecimal(value))
					.setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Effective_Date", (indexes, value, tradeState) -> {
			AdjustableDate.AdjustableDateBuilder adjDate = getIRP(tradeState)
					.getValue()
					.getOrCreateCalculationPeriodDates()
					.getOrCreateEffectiveDate().getOrCreateAdjustableDate();
			adjDate.setAdjustedDateValue(parseISODate(value));
			adjDate.getOrCreateMeta().setExternalKey("Effective_Date");
			adjDate.getOrCreateDateAdjustments().setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		// SecLendingPayout
		commonMappings.put("Net_Dividend_pct", (indexes, value, tradeState) -> {
			getSecPO(tradeState)
					.getValue()
					.getOrCreateDividendTerms()
					.getOrCreateManufacturedIncomeRequirement()
					.setDividendPayoutRatio(parseDecimal(value).divide(BigDecimal.valueOf(100)));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Collateral_Type_IND", (indexes, value, tradeState) -> {
			getSecPO(tradeState)
					.getValue()
					.getOrCreateCollateralProvisions()
					.setCollateralType(parseCollateralType(value));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Required_Trade_Mrgn", (indexes, value, tradeState) -> {
			getSecPO(tradeState)
					.getValue()
					.getOrCreateCollateralProvisions()
					.getOrCreateMarginPercentage()
					.setValuationPercentage(parseDecimal(value).divide(BigDecimal.valueOf(100)));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Minimum_Fee_Currency", (indexes, value, tradeState) -> {
			getSecPO(tradeState)
					.getValue()
					.getOrCreateMinimumFee()
					.getOrCreateUnitOfAmount()
					.getOrCreateCurrency()
					.setValue(value);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Minimum_Fee", (indexes, value, tradeState) -> {
			getSecPO(tradeState)
					.getValue()
					.getOrCreateMinimumFee()
					.setAmount(parseDecimal(value));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Security_Sedol_Code", (indexes, value, tradeState) -> {
			// key
			PathValue<PriceQuantity.PriceQuantityBuilder> pq = getPriceQuantityForSecurityFinancePayout(tradeState);
			ProductIdentifier productIdentifier = ProductIdentifier.builder()
					.setIdentifierValue(value)
					.setSource(ProductIdTypeEnum.SEDOL)
					.build();
			pq.getValue()
					.getOrCreateObservable()
					.addProductIdentifierValue(productIdentifier, 0);
			// reference
			Reference.ReferenceBuilder reference = Reference.builder();
			PathValue<SecurityFinancePayoutBuilder> secLendingPayout = getSecPO(tradeState);
			secLendingPayout
					.getValue()
					.getOrCreateSecurityInformation()
					.getOrCreateSecurity()
					.setSecurityType(SecurityTypeEnum.EQUITY)
					.getOrCreateProductIdentifier(0)
					.setReference(reference);
			return List.of(
					new PathValue<>(pq.getModelPath().append(Path.parse("observable.productIdentifier[0].value.identifier.value")), value),
					new PathValue<>(secLendingPayout.getModelPath().append(Path.parse("securityInformation.security.productIdentifier[0].value.identifier.value")), reference));
		});

		commonMappings.put("DVP_Indicator", (indexes, value, tradeState) -> {
			getSecPO(tradeState)
					.getValue()
					.getOrCreateSecurityFinanceLeg(0)
					.setDeliveryMethod(parseDeliveryMethod(value));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Security_SettDueDt", (indexes, value, tradeState) -> {
			getSecPO(tradeState)
					.getValue()
					.getOrCreateSecurityFinanceLeg(0)
					.getOrCreateSettlementDate()
					.getOrCreateAdjustableDate()
					.setAdjustedDateValue(parseISODate(value));
			getSecPO(tradeState)
					.getValue()
					.getOrCreateSecurityFinanceLeg(0)
					.getOrCreateSettlementDate()
					.getOrCreateAdjustableDate()
					.getOrCreateDateAdjustments()
					.setBusinessDayConvention(BusinessDayConventionEnum.NONE);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Trade_Date", (indexes, value, tradeState) -> {
			tradeState
					.getValue()
					.getOrCreateTrade()
					.getOrCreateTradeDate()
					.setValue(parseISODate(value));
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		commonMappings.put("Trade_Reference", (indexes, value, tradeState) -> {
			tradeState
					.getValue()
					.getOrCreateTrade()
					.getOrCreateTradeIdentifier(0)
					.getOrCreateAssignedIdentifier(0)
					.getOrCreateIdentifier()
					.setValue(value);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		return commonMappings;
	}

	private void buildTradeStateMappings(Multimap<String, MappingConsumer<TradeState.TradeStateBuilder>> commonMappings) {
		addMapping(IndexCapturePath.parse("FIS_TRADE.Activity[0]"), (indexes, value, workflow) -> {
			staticMappings(getTradeState(workflow));
			return List.of();
		});

		addMapping(IndexCapturePath.parse("FIS_TRADE.Activity[0]"), (indexes, value, workflow) -> {
			workflow.getValue()
					.getOrCreateBusinessEvent()
					.getOrCreatePrimitives(0)
					.getOrCreateSplit()
					.getOrCreateAfter(0)
					.getOrCreateMeta()
					.setExternalKey("TradeState");
			workflow.getValue()
					.getOrCreateBusinessEvent()
					.getOrCreatePrimitives(0)
					.getOrCreateSplit()
					.getOrCreateBefore()
					.setExternalReference("TradeState");
			getTradableProduct(getTradeState(workflow))
					.getValue()
					.getOrCreateCounterparty(0)
					.getOrCreatePartyReference()
					.setExternalReference(AGENT_LENDER);
			return List.of();
		});

		for (Map.Entry<String, MappingConsumer<TradeState.TradeStateBuilder>> mapping : commonMappings.entries()) {
			buildTradeStateMapping(mapping.getKey(), (i, v, w) -> mapping.getValue().accept(i, v, getTradeState(w)));
		}

		//event stuff
		buildTradeStateMapping("Trade_Date", (indexes, value, workflow) -> {
			workflow.getValue()
					.getBusinessEvent()
					.setEventDate(parseISODate(value));
			return Collections.singletonList(new PathValue<>(workflow.getModelPath(), value));
		});
	}

	private void buildAllocationMappings(Multimap<String, MappingConsumer<TradeState.TradeStateBuilder>> commonMappings) {
		addMapping(IndexCapturePath.parse("FIS_TRADE.Activity[allocationNumPlus1]"),
				allocaNumberMapConsumer((indexes, value, workflow) -> {
					staticMappings(getSplitTradeState(workflow, indexes));
					return List.of();
				}));

		for (Map.Entry<String, MappingConsumer<TradeState.TradeStateBuilder>> mapping : commonMappings.entries()) {
			buildAllocationMapping(mapping.getKey(), (i, v, w) -> mapping.getValue().accept(i, v, getSplitTradeState(w, i)));
		}

		commonMappings.put("Own_Client_Identifier", (indexes, value, tradeState) -> {
			tradeState
					.getValue()
					.getOrCreateTrade()
					.getOrCreateParty(1)
					.setNameValue(value);
			return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
		});

		buildAllocationMapping("Fund_LEI", (i, v, w) -> {
			PathValue<TradeState.TradeStateBuilder> splitTradeState = getSplitTradeState(w, i);
			Party.PartyBuilder party = splitTradeState
					.getValue()
					.getOrCreateTrade()
					.getOrCreateParty(0);
			party.getOrCreatePartyId(0).setValue(v);
			party.getOrCreateMeta().setExternalKey("Lender" + i.get("allocationNum"));
			getTradableProduct(splitTradeState)
					.getValue()
					.getOrCreateCounterparty(0)
					.getOrCreatePartyReference()
					.setExternalReference("Lender" + i.get("allocationNum"));
			return Collections.singletonList(new PathValue<>(splitTradeState.getModelPath(), v));
		});

		buildAllocationMapping("Fund_cost_Centre_Nme", (i, v, w) -> {
			getSplitTradeState(w, i)
					.getValue()
					.getOrCreateTrade()
					.getOrCreateParty(0)
					.getOrCreateName()
					.setValue(v);
			return Collections.singletonList(new PathValue<>(w.getModelPath(), v));
		});

		buildAllocationMapping("Activity_Input_Date", (i, v, w) -> List.of());
		buildAllocationMapping("Activity_Time_In_Milliseconds", (i, v, w) -> List.of());

	}

	private DeliveryMethodEnum parseDeliveryMethod(String value) {
		switch (value) {
		case "Y":
			return DeliveryMethodEnum.DELIVERY_VERSUS_PAYMENT;
		default:
			return DeliveryMethodEnum.FREE_OF_PAYMENT;
		}
	}

	private CollateralTypeEnum parseCollateralType(String value) {
		switch (value) {
		case "C":
			return CollateralTypeEnum.CASH;
		case "CP":
			return CollateralTypeEnum.CASH_POOL;
		case "NC":
			return CollateralTypeEnum.NON_CASH;
		default:
			throw new RuntimeException("Unknown Collateral type");
		}
	}

	private PathValue<TradeState.TradeStateBuilder> getTradeState(PathValue<WorkflowStep.WorkflowStepBuilder> w) {
		return new PathValue<>(
				w.getModelPath().append(Path.parse("businessEvent.primitives[0].split.after[0]")),
				w.getValue()
						.getOrCreateBusinessEvent()
						.getOrCreatePrimitives(0)
						.getOrCreateSplit()
						.getOrCreateAfter(0));
	}

	private PathValue<TradeState.TradeStateBuilder> getSplitTradeState(PathValue<WorkflowStep.WorkflowStepBuilder> w, Map<String, Integer> indexes) {
		int i = indexes.get("allocationNum") + 1; // +1 because the first is the trade being split (in closed state)
		return new PathValue<>(
				w.getModelPath().append(Path.parse("businessEvent.primitives[0].split.after[" + i + "]")),
				w.getValue()
						.getOrCreateBusinessEvent()
						.getOrCreatePrimitives(0)
						.getOrCreateSplit()
						.getOrCreateAfter(i));
	}

	private PathValue<TradableProduct.TradableProductBuilder> getTradableProduct(PathValue<TradeState.TradeStateBuilder> pv) {
		return new PathValue<>(pv.getModelPath().append(Path.parse("trade.tradableProduct")),
				pv.getValue().getOrCreateTrade().getOrCreateTradableProduct());
	}

	private PathValue<InterestRatePayout.InterestRatePayoutBuilder> getIRP(PathValue<TradeState.TradeStateBuilder> ts) {
		PathValue<EconomicTerms.EconomicTermsBuilder> et = getEcTerms(ts);
		return new PathValue<>(et.getModelPath().addElement("payout").addElement("interestRatePayout", 0),
				et.getValue().getOrCreatePayout().getOrCreateInterestRatePayout(0));
	}

	private PathValue<EconomicTerms.EconomicTermsBuilder> getEcTerms(PathValue<TradeState.TradeStateBuilder> ts) {
		PathValue<TradableProduct.TradableProductBuilder> tp = getTradableProduct(ts);
		return new PathValue<>(tp.getModelPath().append(Path.parse("product.contractualProduct.economicTerms")),
				tp.getValue().getOrCreateProduct().getOrCreateContractualProduct().getOrCreateEconomicTerms());
	}

	private PathValue<SecurityFinancePayoutBuilder> getSecPO(PathValue<TradeState.TradeStateBuilder> ts) {
		PathValue<EconomicTerms.EconomicTermsBuilder> et = getEcTerms(ts);
		return new PathValue<>(et.getModelPath().addElement("payout").addElement("securityFinancePayout", 0),
				et.getValue().getOrCreatePayout().getOrCreateSecurityFinancePayout(0));
	}

	private PathValue<PriceQuantity.PriceQuantityBuilder> getPriceQuantityForInterestRatePayout(PathValue<TradeState.TradeStateBuilder> ts) {
		PathValue<TradableProduct.TradableProductBuilder> tp = getTradableProduct(ts);
		return new PathValue<>(tp.getModelPath().addElement("priceQuantity", 0),
				tp.getValue().getOrCreatePriceQuantity(0));
	}

	private PathValue<PriceQuantity.PriceQuantityBuilder> getPriceQuantityForSecurityFinancePayout(PathValue<TradeState.TradeStateBuilder> ts) {
		PathValue<TradableProduct.TradableProductBuilder> tp = getTradableProduct(ts);
		return new PathValue<>(tp.getModelPath().addElement("priceQuantity", 1),
				tp.getValue().getOrCreatePriceQuantity(1));
	}

	private void buildTradeStateMapping(String xmlPath, MappingConsumer<WorkflowStep.WorkflowStepBuilder> consumer) {
		IndexCapturePath path = IndexCapturePath.parse("FIS_TRADE.Activity[0]." + xmlPath);
		addMapping(path, nonNullConsumer(consumer));
	}

	private void buildAllocationMapping(String xmlPath, MappingConsumer<WorkflowStep.WorkflowStepBuilder> consumer) {
		IndexCapturePath path = IndexCapturePath.parse("FIS_TRADE.Activity[allocationNumPlus1]." + xmlPath);
		addMapping(path, allocaNumberMapConsumer(nonNullConsumer(consumer)));
	}

	private MappingConsumer<WorkflowStep.WorkflowStepBuilder> allocaNumberMapConsumer(MappingConsumer<WorkflowStep.WorkflowStepBuilder> consumer) {
		return (i, v, w) -> {
			Integer allocationNumPlus1 = i.get("allocationNumPlus1");
			if (allocationNumPlus1 != null && allocationNumPlus1 > 0) {
				Integer allocationNum = allocationNumPlus1 - 1;
				i.put("allocationNum", allocationNum);
				return consumer.accept(i, v, w);
			}
			return List.of();
		};
	}
}
