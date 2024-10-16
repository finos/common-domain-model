package cdm.event.workflow.processor;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessDayConventionEnum;
import cdm.base.datetime.PeriodExtendedEnum;
import cdm.base.datetime.RollConventionEnum;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.UnitType;
import cdm.base.staticdata.asset.common.AssetIdTypeEnum;
import cdm.base.staticdata.asset.common.AssetIdentifier;
import cdm.base.staticdata.asset.common.Security;
import cdm.base.staticdata.asset.common.SecurityTypeEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PartyIdentifier;
import cdm.event.common.ExecutionTypeEnum;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.WorkflowStep.WorkflowStepBuilder;
import cdm.observable.asset.PriceTypeEnum;
import cdm.product.collateral.CollateralProvisions;
import cdm.product.collateral.CollateralTypeEnum;
import cdm.product.common.settlement.DeliveryMethodEnum;
import cdm.product.template.AssetPayout;
import cdm.product.template.EconomicTerms.EconomicTermsBuilder;
import cdm.product.template.TradableProduct;
import cdm.product.template.TradableProduct.TradableProductBuilder;
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
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static cdm.base.datetime.AdjustableDate.AdjustableDateBuilder;
import static cdm.base.math.UnitType.UnitTypeBuilder;
import static cdm.base.staticdata.party.Party.PartyBuilder;
import static cdm.event.common.TradeState.TradeStateBuilder;
import static cdm.observable.asset.PriceQuantity.PriceQuantityBuilder;
import static cdm.product.asset.InterestRatePayout.InterestRatePayoutBuilder;

/**
 * This instance override the version in CDM so it can be kept up to date with ISLA model changes.
 */
@SuppressWarnings("unused")
public class FISMapperMappingProcessor extends FlatFileMappingProcessor<WorkflowStepBuilder> {

    private static final String BORROWER = "Borrower";
    private static final String AGENT_LENDER = "AGENT_LENDER";

    protected void doHardCodings(WorkflowStepBuilder workflow) {

    }

    private void staticMappings(PathValue<TradeStateBuilder> tradeState) {

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
        getSecPO(tradeState).getValue().getOrCreateUnderlier().getOrCreateInstrument().getOrCreateSecurity().setSecurityType(SecurityTypeEnum.EQUITY);

        getSecPO(tradeState).getValue().getOrCreatePayerReceiver().setPayer(CounterpartyRoleEnum.PARTY_1);
        getSecPO(tradeState).getValue().getOrCreatePayerReceiver().setReceiver(CounterpartyRoleEnum.PARTY_2);
    }

    public FISMapperMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);

        Multimap<String, MappingConsumer<TradeStateBuilder>> commonMappings = buildCommonMappings();
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
                Stream<InterestRatePayoutBuilder> allIRPs =
                        Streams.concat(Stream.of(getTradeState(new PathValue<>(BASE_PATH, workflow)).getValue()),
                                        Streams.stream(Iterables.skip(workflow.getOrCreateBusinessEvent().getAfter(), 1)))
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

    private Multimap<String, MappingConsumer<TradeStateBuilder>> buildCommonMappings() {
        Multimap<String, MappingConsumer<TradeStateBuilder>> commonMappings = HashMultimap.create();

        commonMappings.put("Own_Cpty_LEI", (indexes, value, tradeState) -> {
            PartyBuilder party = tradeState.getValue().getOrCreateTrade().getOrCreateParty(1);
            party.getOrCreatePartyId(0).setIdentifierValue(value);
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
            PartyBuilder party = tradeState.getValue().getOrCreateTrade().getOrCreateParty(2);
            party.getOrCreateName().setValue(value);
            party.getOrCreateMeta().setExternalKey(BORROWER);
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Cpty_LEI", (indexes, value, tradeState) -> {
            PartyIdentifier.PartyIdentifierBuilder partyId = tradeState.getValue().getOrCreateTrade().getOrCreateParty(2).getOrCreatePartyId(0);
            partyId.setIdentifierValue(value);
            //TODO should hard code the scheme
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Effective_Date", (indexes, value, tradeState) -> {
            AdjustableDateBuilder orCreateAdjustableDate = getEcTerms(tradeState).getValue().getOrCreateEffectiveDate().getOrCreateAdjustableDate();
            orCreateAdjustableDate.setUnadjustedDate(parseISODate(value));
            orCreateAdjustableDate.getOrCreateDateAdjustments().setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING);
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Activity_Rate", (indexes, value, tradeState) -> {
            // key
            PathValue<PriceQuantityBuilder> pq = getPriceQuantityForInterestRatePayout(tradeState);
            pq.getValue()
                    .getOrCreatePrice(0)
                    .getOrCreateValue()
                    .setValue(parseDecimal(value))
                    .setPriceType(PriceTypeEnum.INTEREST_RATE);
            // reference
            Reference.ReferenceBuilder reference = Reference.builder();
            PathValue<InterestRatePayoutBuilder> irp = getIRP(tradeState);
            irp.getValue()
                    .getOrCreateRateSpecification()
                    .getOrCreateFixedRateSpecification()
                    .getOrCreateRateSchedule()
                    .getOrCreatePrice()
                    .setReference(reference);
            return Arrays.asList(
                    new PathValue<>(pq.getModelPath().append(Path.parse("price[0].value.amount")), value),
                    new PathValue<>(irp.getModelPath().append(Path.parse("rateSpecification.FixedRateSpecification.rateSchedule.price.reference")), reference));
        });

        commonMappings.put("Loan_Value", (indexes, value, tradeState) -> {
            // key
            PathValue<PriceQuantityBuilder> pq = getPriceQuantityForInterestRatePayout(tradeState);
            pq.getValue()
                    .getOrCreateQuantity(0)
                    .getOrCreateValue()
                    .setValue(parseDecimal(value));
            // reference
            Reference.ReferenceBuilder reference = Reference.builder();
            PathValue<InterestRatePayoutBuilder> irp = getIRP(tradeState);
            irp.getValue()
                    .getOrCreatePriceQuantity()
                    .getOrCreateQuantitySchedule()
                    .setReference(reference);
            return Arrays.asList(
                    new PathValue<>(pq.getModelPath().append(Path.parse("quantity[0].value.amount")), value),
                    new PathValue<>(irp.getModelPath().append(Path.parse("priceQuantity.quantitySchedule.reference")), reference));
        });

        commonMappings.put("Loan_Value_Currency", (indexes, value, tradeState) -> {
            UnitTypeBuilder currencyUnit = UnitType.builder().setCurrency(FieldWithMetaString.builder().setValue(value));
            PathValue<PriceQuantityBuilder> pq = getPriceQuantityForInterestRatePayout(tradeState);
            pq.getValue()
                    .getOrCreatePrice(0)
                    .getOrCreateValue()
                    .setUnit(currencyUnit)
                    .setPerUnitOf(currencyUnit);
            pq.getValue()
                    .getOrCreateQuantity(0)
                    .getOrCreateValue()
                    .setUnit(currencyUnit);
            return Arrays.asList(
                    new PathValue<>(pq.getModelPath().append(Path.parse("price[0].value.unitOfAmount.currency.value")), value),
                    new PathValue<>(pq.getModelPath().append(Path.parse("price[0].value.perUnitOfAmount.currency.value")), value),
                    new PathValue<>(pq.getModelPath().append(Path.parse("quantity[0].value.unitOfAmount.currency.value")), value));
        });

        commonMappings.put("Activity_Price", (indexes, value, tradeState) -> {
            getPriceQuantityForSecurityFinancePayout(tradeState)
                    .getValue()
                    .getOrCreatePrice(0)
                    .getOrCreateValue()
                    .setValue(parseDecimal(value))
                    .setPriceType(PriceTypeEnum.ASSET_PRICE)
                    .setPerUnitOf(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE));
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Security_Prcing_Curr", (indexes, value, tradeState) -> {
            getPriceQuantityForSecurityFinancePayout(tradeState)
                    .getValue()
                    .getOrCreatePrice(0)
                    .getOrCreateValue()
                    .getOrCreateUnit()
                    .getOrCreateCurrency()
                    .setValue(value);
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Quantity", (indexes, value, tradeState) -> {
            getPriceQuantityForSecurityFinancePayout(tradeState)
                    .getValue()
                    .getOrCreateQuantity(0)
                    .getOrCreateValue()
                    .setValue(parseDecimal(value))
                    .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE));
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Effective_Date", (indexes, value, tradeState) -> {
            AdjustableDateBuilder adjDate = getIRP(tradeState)
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
                    .setTotalRatio(parseDecimal(value).divide(BigDecimal.valueOf(100)));
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Collateral_Type_IND", (indexes, value, tradeState) -> {
            getColPro(tradeState)
                    .getValue()
                    .setCollateralType(parseCollateralType(value));
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Required_Trade_Mrgn", (indexes, value, tradeState) -> {
            getColPro(tradeState)
                    .getValue()
                    .getOrCreateEligibleCollateral(0)
                    .getOrCreateTreatment()
                    .getOrCreateValuationTreatment()
                    .setMarginPercentage(parseDecimal(value).divide(BigDecimal.valueOf(100)));
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Minimum_Fee_Currency", (indexes, value, tradeState) -> {
            getSecPO(tradeState)
                    .getValue()
                    .getOrCreateMinimumFee()
                    .getOrCreateUnit()
                    .getOrCreateCurrency()
                    .setValue(value);
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Minimum_Fee", (indexes, value, tradeState) -> {
            getSecPO(tradeState)
                    .getValue()
                    .getOrCreateMinimumFee()
                    .setValue(parseDecimal(value));
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Security_Sedol_Code", (indexes, value, tradeState) -> {
            // key
            PathValue<PriceQuantityBuilder> pq = getPriceQuantityForSecurityFinancePayout(tradeState);
            Security security = Security.builder()
                    .setSecurityType(SecurityTypeEnum.EQUITY)
                    .addIdentifier(AssetIdentifier.builder()
                            .setIdentifierValue(value)
                            .setIdentifierType(AssetIdTypeEnum.SEDOL));
            pq.getValue()
                    .getOrCreateObservable()
                    .getOrCreateValue()
                    .getOrCreateAsset()
                    .getOrCreateInstrument()
                    .setSecurity(security);
            // reference
            Reference.ReferenceBuilder reference = Reference.builder();
            PathValue<AssetPayout.AssetPayoutBuilder> secLendingPayout = getSecPO(tradeState);
            secLendingPayout
                    .getValue()
                    .getOrCreateUnderlier()
                    .getOrCreateInstrument()
                    .setSecurity(security);
            return Arrays.asList(
                    new PathValue<>(pq.getModelPath().append(Path.parse("observable.value.Asset.value.Instrument.security")), value),
                    new PathValue<>(secLendingPayout.getModelPath().append(Path.parse("securityInformation.identifier.identifier.value")), reference));
        });

        commonMappings.put("DVP_Indicator", (indexes, value, tradeState) -> {
            getSecPO(tradeState)
                    .getValue()
                    .getOrCreateAssetLeg(0)
                    .setDeliveryMethod(parseDeliveryMethod(value));
            return Collections.singletonList(new PathValue<>(tradeState.getModelPath(), value));
        });

        commonMappings.put("Security_SettDueDt", (indexes, value, tradeState) -> {
            getSecPO(tradeState)
                    .getValue()
                    .getOrCreateAssetLeg(0)
                    .getOrCreateSettlementDate()
                    .getOrCreateAdjustableDate()
                    .setAdjustedDateValue(parseISODate(value));
            getSecPO(tradeState)
                    .getValue()
                    .getOrCreateAssetLeg(0)
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

    private void buildTradeStateMappings(Multimap<String, MappingConsumer<TradeStateBuilder>> commonMappings) {
        addMapping(IndexCapturePath.parse("FIS_TRADE.Activity[0]"), (indexes, value, workflow) -> {
            staticMappings(getTradeState(workflow));
            return Collections.emptyList();
        });

        addMapping(IndexCapturePath.parse("FIS_TRADE.Activity[0]"), (indexes, value, workflow) -> {
            workflow.getValue()
                    .getOrCreateBusinessEvent()
                    .getOrCreateAfter(0)
                    .getOrCreateMeta()
                    .setExternalKey("TradeState");
            workflow.getValue()
                    .getOrCreateBusinessEvent()
                    .getOrCreateInstruction(0)
                    .getOrCreateBefore()
                    .setExternalReference("TradeState");
            getTradableProduct(getTradeState(workflow))
                    .getValue()
                    .getOrCreateCounterparty(0)
                    .getOrCreatePartyReference()
                    .setExternalReference(AGENT_LENDER);
            return Collections.emptyList();
        });

        for (Entry<String, MappingConsumer<TradeStateBuilder>> mapping : commonMappings.entries()) {
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

    private void buildAllocationMappings(Multimap<String, MappingConsumer<TradeStateBuilder>> commonMappings) {
        addMapping(IndexCapturePath.parse("FIS_TRADE.Activity[allocationNumPlus1]"),
                allocaNumberMapConsumer((indexes, value, workflow) -> {
                    staticMappings(getSplitTradeState(workflow, indexes));
                    return Collections.emptyList();
                }));

        for (Entry<String, MappingConsumer<TradeStateBuilder>> mapping : commonMappings.entries()) {
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
            PathValue<TradeStateBuilder> splitTradeState = getSplitTradeState(w, i);
            PartyBuilder party = splitTradeState
                    .getValue()
                    .getOrCreateTrade()
                    .getOrCreateParty(0);
            party.getOrCreatePartyId(0).setIdentifierValue(v);
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

        buildAllocationMapping("Activity_Input_Date", (i, v, w) -> Collections.emptyList());
        buildAllocationMapping("Activity_Time_In_Milliseconds", (i, v, w) -> Collections.emptyList());

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

    private PathValue<TradeStateBuilder> getTradeState(PathValue<WorkflowStepBuilder> w) {
        return new PathValue<>(
                w.getModelPath().append(Path.parse("businessEvent.after[0]")),
                w.getValue()
                        .getOrCreateBusinessEvent()
                        .getOrCreateAfter(0));
    }

    private PathValue<TradeStateBuilder> getSplitTradeState(PathValue<WorkflowStepBuilder> w, Map<String, Integer> indexes) {
        int i = indexes.get("allocationNum") + 1; // +1 because the first is the trade being split (in closed state)
        return new PathValue<>(
                w.getModelPath().append(Path.parse("businessEvent.after[" + i + "]")),
                w.getValue()
                        .getOrCreateBusinessEvent()
                        .getOrCreateAfter(i));
    }

    private PathValue<TradableProductBuilder> getTradableProduct(PathValue<TradeStateBuilder> pv) {
        return new PathValue<>(pv.getModelPath().append(Path.parse("trade")),
                pv.getValue().getOrCreateTrade());
    }

    private PathValue<InterestRatePayoutBuilder> getIRP(PathValue<TradeStateBuilder> ts) {
        PathValue<EconomicTermsBuilder> et = getEcTerms(ts);
        return new PathValue<>(et.getModelPath().addElement("payout", 1).addElement("InterestRatePayout"),
                et.getValue().getOrCreatePayout(1).getOrCreateInterestRatePayout());
    }

    private PathValue<EconomicTermsBuilder> getEcTerms(PathValue<TradeStateBuilder> ts) {
        PathValue<TradableProductBuilder> tp = getTradableProduct(ts);
        return new PathValue<>(tp.getModelPath().append(Path.parse("product.economicTerms")),
                tp.getValue().getOrCreateProduct().getOrCreateEconomicTerms());
    }

    private PathValue<AssetPayout.AssetPayoutBuilder> getSecPO(PathValue<TradeStateBuilder> ts) {
        PathValue<EconomicTermsBuilder> et = getEcTerms(ts);
        return new PathValue<>(et.getModelPath().addElement("payout", 0).addElement("AssetPayout"),
                et.getValue().getOrCreatePayout(0).getOrCreateAssetPayout());
    }

    private PathValue<CollateralProvisions.CollateralProvisionsBuilder> getColPro(PathValue<TradeStateBuilder> ts) {
        PathValue<EconomicTermsBuilder> et = getEcTerms(ts);
        return new PathValue<>(et.getModelPath().addElement("collateral").addElement("collateralProvisions", 0),
                et.getValue().getOrCreateCollateral().getOrCreateCollateralProvisions());
    }

    private PathValue<PriceQuantityBuilder> getPriceQuantityForInterestRatePayout(PathValue<TradeStateBuilder> ts) {
        PathValue<TradableProduct.TradableProductBuilder> tp = getTradableProduct(ts);
        return new PathValue<>(tp.getModelPath().addElement("tradeLot", 0).addElement("priceQuantity", 0),
                tp.getValue().getOrCreateTradeLot(0).getOrCreatePriceQuantity(0));
    }

    private PathValue<PriceQuantityBuilder> getPriceQuantityForSecurityFinancePayout(PathValue<TradeStateBuilder> ts) {
        PathValue<TradableProduct.TradableProductBuilder> tp = getTradableProduct(ts);
        return new PathValue<>(tp.getModelPath().addElement("tradeLot", 0).addElement("priceQuantity", 1),
                tp.getValue().getOrCreateTradeLot(0).getOrCreatePriceQuantity(1));
    }

    private void buildTradeStateMapping(String xmlPath, MappingConsumer<WorkflowStepBuilder> consumer) {
        IndexCapturePath path = IndexCapturePath.parse("FIS_TRADE.Activity[0]." + xmlPath);
        addMapping(path, nonNullConsumer(consumer));
    }

    private void buildAllocationMapping(String xmlPath, MappingConsumer<WorkflowStepBuilder> consumer) {
        IndexCapturePath path = IndexCapturePath.parse("FIS_TRADE.Activity[allocationNumPlus1]." + xmlPath);
        addMapping(path, allocaNumberMapConsumer(nonNullConsumer(consumer)));
    }

    private MappingConsumer<WorkflowStepBuilder> allocaNumberMapConsumer(MappingConsumer<WorkflowStepBuilder> consumer) {
        return (i, v, w) -> {
            Integer allocationNumPlus1 = i.get("allocationNumPlus1");
            if (allocationNumPlus1 != null && allocationNumPlus1 > 0) {
                Integer allocationNum = allocationNumPlus1 - 1;
                i.put("allocationNum", allocationNum);
                return consumer.accept(i, v, w);
            }
            return Collections.emptyList();
        };
    }
}
