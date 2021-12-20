package org.isda.cdm.functions;

import cdm.base.datetime.Period;
import cdm.base.datetime.PeriodEnum;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.base.staticdata.asset.common.ProductIdTypeEnum;
import cdm.base.staticdata.asset.common.ProductIdentifier;
import cdm.base.staticdata.asset.common.metafields.FieldWithMetaProductIdentifier;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.workflow.WorkflowStep;
import cdm.observable.asset.*;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradableProduct;
import cdm.product.template.TradeLot;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.functions.testing.FunctionUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import util.ResourcesUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInputCreationTest {

    private static final ObjectMapper STRICT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

    @Test
    void validateCreateTerminationWorkflowFuncInputJson() throws IOException {
        CreateTerminationWorkflowInput actual = new CreateTerminationWorkflowInput(
                getTerminationTradeState(),
                TerminationInstruction.builder()
                        .addTerminatedPriceQuantity(PriceQuantity.builder()
                                .addQuantity(FieldWithMetaQuantity.builder()
                                        .setValue(Quantity.builder()
                                                .setAmount(BigDecimal.valueOf(10000))
                                                .setUnitOfAmount(UnitType.builder().setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))))
                        .setTerminationDate(DateImpl.of(2019, 12, 12))
                        .build());

        assertEquals(readResource("/cdm-sample-files/functions/termination-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for termination-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreatePartialTerminationWorkflowFuncInputJson() throws IOException {
        CreateTerminationWorkflowInput actual = new CreateTerminationWorkflowInput(
                getTerminationTradeState(),
                TerminationInstruction.builder()
                        .addTerminatedPriceQuantity(PriceQuantity.builder()
                                .addQuantity(FieldWithMetaQuantity.builder()
                                        .setValue(Quantity.builder()
                                                .setAmount(BigDecimal.valueOf(7000))
                                                .setUnitOfAmount(UnitType.builder().setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))))
                        .setTerminationDate(DateImpl.of(2019, 12, 12))
                        .build());

        assertEquals(readResource("/cdm-sample-files/functions/partial-termination-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for partial-termination-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreateFullTerminationEquitySwapFuncInputJson() throws IOException {
        Instruction.InstructionBuilder instructionBuilder = Instruction.builder();

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeBuilder =
                instructionBuilder.getOrCreatePrimitiveInstruction(0)
                        .getOrCreateQuantityChange();

        quantityChangeBuilder.setDirection(QuantityChangeDirectionEnum.DECREASE);

        PriceQuantity.PriceQuantityBuilder changeBuilder = quantityChangeBuilder
                .getOrCreateChange(0);

        changeBuilder.getOrCreateQuantity(0)
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(760400))
                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE).build())
                        .build());

        changeBuilder.getOrCreateQuantity(1)
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(28469376))
                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD").build())
                        .build());

        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json");
        instructionBuilder
                .setBefore(tradeState);

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                InstructionFunctionEnum.QUANTITY_CHANGE,
                new DateImpl(11, 11, 2021)
        );

        assertEquals(readResource("/cdm-sample-files/functions/full-termination-equity-swap-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for full-termination-equity-swap-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreatePartialTerminationEquitySwapFuncInputJson() throws IOException {
        Instruction.InstructionBuilder instructionBuilder = Instruction.builder();

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeBuilder =
                instructionBuilder.getOrCreatePrimitiveInstruction(0)
                        .getOrCreateQuantityChange();

        quantityChangeBuilder.setDirection(QuantityChangeDirectionEnum.DECREASE);

        PriceQuantity.PriceQuantityBuilder changeBuilder = quantityChangeBuilder
                .getOrCreateChange(0);

        changeBuilder.getOrCreateObservable()
                .getOrCreateProductIdentifier(0)
                .getOrCreateValue()
                .setSource(ProductIdTypeEnum.OTHER)
                .setIdentifier(FieldWithMetaString.builder()
                        .setMeta(MetaFields.builder().setScheme("http://www.abc.com/instrumentId"))
                        .setValue("SHPGY.O")
                );

        changeBuilder.getOrCreateQuantity(0)
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(300000))
                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE).build())
                        .build());

        changeBuilder.getOrCreateQuantity(1)
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(9600000))
                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD").build())
                        .build());

        FieldWithMetaProductIdentifier.FieldWithMetaProductIdentifierBuilder productIdentifier = changeBuilder.getOrCreateObservable()
                .getOrCreateProductIdentifier(0);

        productIdentifier
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("productIdentifier-1")));

        productIdentifier
                .getOrCreateValue()
                .setSource(ProductIdTypeEnum.OTHER);

        productIdentifier
                .getValue()
                .getOrCreateIdentifier()
                .setMeta(MetaFields.builder().setScheme("http://www.abc.com/instrumentId"))
                .setValue("SHPGY.O");

        quantityChangeBuilder.getOrCreateLotIdentifier(0)
                .getOrCreateAssignedIdentifier(0)
                .setIdentifierValue("LOT-1");

        // Build up TradeState with additional TradeLot
        //TODO: call create_BusinessEvent function using the "increase-equity-swap-func" as input, take the after from that and use as before here
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json");
        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
        TradableProduct.TradableProductBuilder tradableProduct = tradeStateBuilder
                .getTrade()
                .getTradableProduct();

        tradableProduct
                .getOrCreateTradeLot(0)
                .getOrCreateLotIdentifier(0)
                .getOrCreateAssignedIdentifier(0)
                .setIdentifierValue("LOT-1");

        TradeLot.TradeLotBuilder tradeLot2 = tradableProduct
                .getOrCreateTradeLot(1);

        tradeLot2
                .getOrCreateLotIdentifier(0)
                .getOrCreateAssignedIdentifier(0)
                .setIdentifierValue("LOT-2");

        PriceQuantity.PriceQuantityBuilder tradeLot2Pq1 = tradeLot2
                .getOrCreatePriceQuantity(0);

        tradeLot2Pq1
                .getOrCreateObservable()
                .getOrCreateProductIdentifier(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("productIdentifier-1")))
                .setValue(ProductIdentifier.builder()
                        .setIdentifier(FieldWithMetaString.builder().setValue("SHPGY.O").setMeta(MetaFields.builder().setScheme("http://www.abc.com/instrumentId")))
                        .setSource(ProductIdTypeEnum.OTHER)
                );

        tradeLot2Pq1
                .getOrCreatePrice(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("price-2")))
                .setValue(Price.builder()
                        .setAmount(BigDecimal.valueOf(30))
                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
                        .setPerUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))
                        .setPriceExpression(PriceExpression.builder().setGrossOrNet(GrossOrNetEnum.NET).setPriceType(PriceTypeEnum.ASSET_PRICE))
                );

        tradeLot2Pq1
                .getOrCreateQuantity(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("quantity-2")))
                .setValue(Quantity.builder().setAmount(BigDecimal.valueOf(250000)).setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE)));

        tradeLot2Pq1
                .getOrCreateQuantity(1)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("quantity-1")))
                .setValue(Quantity.builder().setAmount(BigDecimal.valueOf(7500000)).setUnitOfAmount(UnitType.builder().setCurrencyValue("USD")));

        PriceQuantity.PriceQuantityBuilder tradeLot2Pq2 = tradeLot2
                .getOrCreatePriceQuantity(1);

        tradeLot2Pq2
                .getOrCreateObservable()
                .getOrCreateRateOption()
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("rateOption-1")))
                .setValue(FloatingRateOption.builder()
                        .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
                        .setIndexTenor(Period.builder().setPeriod(PeriodEnum.M).setPeriodMultiplier(1))
                );

        tradeLot2Pq2
                .getOrCreatePrice(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("price-1")))
                .setValue(Price.builder()
                        .setAmount(BigDecimal.valueOf(0.002))
                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
                        .setPerUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
                        .setPriceExpression(PriceExpression.builder().setPriceType(PriceTypeEnum.INTEREST_RATE).setSpreadType(SpreadTypeEnum.SPREAD))
                );

        instructionBuilder
                .setBefore(tradeStateBuilder.build());

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                InstructionFunctionEnum.QUANTITY_CHANGE,
                new DateImpl(11, 11, 2021)
        );

        assertEquals(readResource("/cdm-sample-files/functions/partial-termination-equity-swap-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for partial-termination-equity-swap-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreateIncreaseEquitySwapFuncInputJson() throws IOException {
        Instruction.InstructionBuilder instructionBuilder = Instruction.builder();

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeBuilder =
                instructionBuilder.getOrCreatePrimitiveInstruction(0)
                        .getOrCreateQuantityChange();

        quantityChangeBuilder.setDirection(QuantityChangeDirectionEnum.INCREASE);

        PriceQuantity.PriceQuantityBuilder changeBuilder = quantityChangeBuilder
                .getOrCreateChange(0);

        changeBuilder.getOrCreateQuantity(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("quantity-2")))
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(250000))
                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE).build())
                        .build());

        changeBuilder.getOrCreateQuantity(1)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("quantity-1")))
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(7500000))
                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD").build())
                        .build());

        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json");
        instructionBuilder
                .setBefore(tradeState);

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                InstructionFunctionEnum.QUANTITY_CHANGE,
                new DateImpl(11, 11, 2021)
        );

        assertEquals(readResource("/cdm-sample-files/functions/increase-equity-swap-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for partial-termination-increase-equity-swap-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreateAllocationWorkflowInputJson() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = getTerminationTradeState();

        List<? extends InterestRatePayout.InterestRatePayoutBuilder> interestRatePayoutBuilders = tradeStateBuilder
                .getTrade()
                .getTradableProduct()
                .getProduct()
                .getContractualProduct()
                .getEconomicTerms()
                .getPayout()
                .getInterestRatePayout();

        interestRatePayoutBuilders.get(0)
                .getCalculationPeriodDates()
                .getTerminationDate()
                .getAdjustableDate()
                .setUnadjustedDate(DateImpl.of(2028, 4, 1));

        interestRatePayoutBuilders.get(1)
                .getCalculationPeriodDates()
                .getTerminationDate()
                .getAdjustableDate()
                .setUnadjustedDate(DateImpl.of(2028, 4, 1));

        tradeStateBuilder
                .getTrade()
                .getParty().get(0)
                .getPartyId().get(0)
                .setValue("LEI1RPT001");

        tradeStateBuilder
                .getTrade()
                .getParty().get(1)
                .getPartyId().get(0)
                .setValue("LEIFUNDMGR");

        tradeStateBuilder
                .getTrade()
                .getTradeIdentifier().get(0)
                .getAssignedIdentifier().get(0)
                .getIdentifier()
                .setValue("LEI1RPT001PREAA");

        ExecutionInstruction executionInstruction = FunctionUtils.createExecutionInstructionFromTradeState(tradeStateBuilder.build());

        assertEquals(readResource("/cdm-sample-files/functions/allocation-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(executionInstruction),
                "The input JSON for allocation-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateQuantityChangeIncreaseWorkflowFuncInputJson() throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json");

        List<? extends PriceQuantity> priceQuantities =
                tradeState.getTrade().getTradableProduct().getTradeLot().get(0).getPriceQuantity();
        // equity payout price quantity
        PriceQuantity.PriceQuantityBuilder equityPriceQuantity = priceQuantities.get(0).toBuilder();
        // Asset Price
        equityPriceQuantity.getPrice().get(0).getValue().setAmount(BigDecimal.valueOf(30));
        // Shares
        equityPriceQuantity.getQuantity().get(0).getValue().setAmount(BigDecimal.valueOf(250000));
        // Notional
        equityPriceQuantity.getQuantity().get(1).getValue().setAmount(BigDecimal.valueOf(7500000));
        // interest rate payout price quantity
        PriceQuantity.PriceQuantityBuilder interestRatePriceQuantity = priceQuantities.get(1).toBuilder();

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Arrays.asList(
                        Instruction.builder()
                                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                                        .setQuantityChange(QuantityChangeInstruction.builder()
                                                .addChange(equityPriceQuantity)
                                                .addChange(interestRatePriceQuantity)
                                                .setDirection(QuantityChangeDirectionEnum.INCREASE)))
                                .setBefore(tradeState)),
                InstructionFunctionEnum.QUANTITY_CHANGE,
                DateImpl.of(2021, 11, 11));

        assertEquals(readResource("/cdm-sample-files/functions/quantity-change-increase-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for quantity-change-increase-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }


    /**
     * Use record-ex01-vanilla-swap.json sample and modify it to look exactly like CFTC example 3 (used in regs termination example)
     */
    @NotNull
    private TradeState.TradeStateBuilder getTerminationTradeState() throws IOException {
        WorkflowStep workflowStep = ResourcesUtils.getObject(WorkflowStep.class, "result-json-files/fpml-5-10/record-keeping/record-ex01-vanilla-swap.json");
        // parties
        List<Party> parties = workflowStep.getParty().stream()
                .filter(p -> p.getName().getValue().equals("Bank X") || p.getName().getValue().equals("Bank Y"))
                .collect(Collectors.toList());
        TradeState tradeState = workflowStep.getBusinessEvent().getPrimitives().get(0).getContractFormation().getAfter();
        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
        tradeStateBuilder.getTrade().setParty(parties);
        // partyId
        updatePartyId(tradeStateBuilder, "Bank X", "LEI1RPT0001");
        updatePartyId(tradeStateBuilder, "Bank Y", "LEI2CP0002");
        // party role
        List<? extends PartyRole> partyRoles = tradeState.getTrade().getPartyRole();
        PartyRole partyRole = partyRoles.get(0);
        PartyRole.PartyRoleBuilder reportingPartyRole = partyRole.toBuilder()
                .setPartyReference(ReferenceWithMetaParty.builder()
                        .setGlobalReference(partyRole.getOwnershipPartyReference().getGlobalReference())
                        .setExternalReference(partyRole.getOwnershipPartyReference().getExternalReference()))
                .setRole(PartyRoleEnum.REPORTING_PARTY);
        tradeStateBuilder.getTrade().addPartyRole(reportingPartyRole);
        // effective and termination date
        List<? extends InterestRatePayout.InterestRatePayoutBuilder> interestRatePayouts = tradeStateBuilder.getTrade()
                .getTradableProduct()
                .getProduct()
                .getContractualProduct()
                .getEconomicTerms()
                .getPayout()
                .getInterestRatePayout();
        interestRatePayouts.stream()
                .filter(payout -> payout.getRateSpecification().getFloatingRate() != null)
                .findFirst()
                .ifPresent(floatingLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = floatingLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2014, 4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2025, 4, 1));
                });
        interestRatePayouts.stream()
                .filter(payout -> payout.getRateSpecification().getFixedRate() != null)
                .findFirst()
                .ifPresent(fixedLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = fixedLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2018, 4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(DateImpl.of(2025, 4, 1));
                });
        // quantity
        tradeStateBuilder.getTrade().getTradableProduct().getTradeLot().stream().map(TradeLot.TradeLotBuilder::getPriceQuantity).flatMap(Collection::stream).map(
                PriceQuantity.PriceQuantityBuilder::getQuantity).flatMap(Collection::stream).map(FieldWithMetaQuantity.FieldWithMetaQuantityBuilder::getValue).forEach(quantity -> {
            quantity.setAmount(new BigDecimal(10000));
        });
        // trade id
        tradeStateBuilder.getTrade().getTradeIdentifier().get(0).getAssignedIdentifier().get(0).setIdentifierValue("LEI1RPT0001KKKK");
        // trade date
        tradeStateBuilder.getTrade().setTradeDateValue(DateImpl.of(2018, 4, 1));
        return tradeStateBuilder;
    }

    private void updatePartyId(TradeState.TradeStateBuilder tradeStateBuilder, String partyName, String partyId) {
        tradeStateBuilder.getTrade().getParty().stream()
                .filter(p -> p.getName().getValue().equals(partyName))
                .findFirst().ifPresent(bankXParty -> bankXParty.setPartyId(Arrays.asList(FieldWithMetaString.builder()
                        .setValue(partyId)
                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso17442")))));
    }

    private static String readResource(String inputJson) throws IOException {
        //noinspection UnstableApiUsage
        return Resources
                .toString(Objects
                        .requireNonNull(FunctionInputCreationTest.class.getResource(inputJson)), Charset
                        .defaultCharset());
    }
}
