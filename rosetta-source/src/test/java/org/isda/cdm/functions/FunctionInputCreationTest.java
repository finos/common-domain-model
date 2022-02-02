package org.isda.cdm.functions;

import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.Quantity;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.base.staticdata.asset.common.ProductIdTypeEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.common.functions.Create_BusinessEvent;
import cdm.event.workflow.WorkflowStep;
import cdm.observable.asset.GrossOrNetEnum;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceTypeEnum;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradeLot;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.validation.ModelObjectValidator;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.isda.cdm.CdmRuntimeModule;
import org.isda.cdm.functions.testing.FunctionUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    private static Injector injector;


    private static final ObjectMapper STRICT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

    @BeforeAll
    static void setup() {
        Module module = Modules.override(new CdmRuntimeModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(PostProcessor.class).to(GlobalKeyProcessRunner.class);
                        bind(ModelObjectValidator.class).toInstance(Mockito.mock(ModelObjectValidator.class));
                    }
                });
        injector = Guice.createInjector(module);
    }

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
                        .setTerminationDate(Date.of(2019, 12, 12))
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
                        .setTerminationDate(Date.of(2019, 12, 12))
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
                Date.of(2021, 11, 11)
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

        quantityChangeBuilder.getOrCreateLotIdentifier(0)
                .getOrCreateAssignedIdentifier(0)
                .setIdentifierValue("LOT-1");

        PriceQuantity.PriceQuantityBuilder changeBuilder = quantityChangeBuilder
                .getOrCreateChange(0);

        changeBuilder.getOrCreateQuantity(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("quantity-2")))
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(760400))
                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE).build())
                        .build());

        changeBuilder.getOrCreateQuantity(1)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("quantity-1")))
                .setValue(Quantity.builder()
                        .setAmount(BigDecimal.valueOf(28469376))
                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD").build())
                        .build());

        CreateBusinessEventWorkflowInput increaseEquitySwapInput = generateIncreaseEquitySwapInput();
        Create_BusinessEvent createBusinessEvent = injector.getInstance(Create_BusinessEvent.class);
        BusinessEvent increaseOutput = createBusinessEvent.evaluate(increaseEquitySwapInput.getInstruction(),
                increaseEquitySwapInput.getInstructionFunction(),
                increaseEquitySwapInput.getEventDate());

        TradeState increaseAfterState = increaseOutput.getAfter().get(0);

        instructionBuilder
                .setBefore(increaseAfterState);

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                InstructionFunctionEnum.QUANTITY_CHANGE,
                Date.of(2021, 11, 11)
        );

        assertEquals(readResource("/cdm-sample-files/functions/partial-termination-equity-swap-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for partial-termination-equity-swap-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreateIncreaseEquitySwapFuncInputJson() throws IOException {
        CreateBusinessEventWorkflowInput actual = generateIncreaseEquitySwapInput();

        assertEquals(readResource("/cdm-sample-files/functions/increase-equity-swap-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for increase-equity-swap-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @NotNull
    private CreateBusinessEventWorkflowInput generateIncreaseEquitySwapInput() throws IOException {
        Instruction.InstructionBuilder instructionBuilder = Instruction.builder();

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeBuilder =
                instructionBuilder.getOrCreatePrimitiveInstruction(0)
                        .getOrCreateQuantityChange();

        quantityChangeBuilder.setDirection(QuantityChangeDirectionEnum.INCREASE);

        quantityChangeBuilder.getOrCreateLotIdentifier(0)
                .getOrCreateAssignedIdentifier(0)
                .setIdentifierValue("LOT-2");

        PriceQuantity.PriceQuantityBuilder changeBuilder = quantityChangeBuilder
                .getOrCreateChange(0);

        changeBuilder.getOrCreateObservable()
                .getOrCreateProductIdentifier(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("productIdentifier-1")))
                .getOrCreateValue()
                .setSource(ProductIdTypeEnum.OTHER)
                .setIdentifier(FieldWithMetaString.builder()
                        .setMeta(MetaFields.builder().setScheme("http://www.abc.com/instrumentId"))
                        .setValue("SHPGY.O")
                );

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

        changeBuilder.getOrCreatePrice(0)
                .setMeta(MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue("price-2")))
                .setValue(
                        Price.builder()
                                .setAmount(BigDecimal.valueOf(30))
                                .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
                                .setPerUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))
                                .setPriceExpression(PriceExpression.builder().setGrossOrNet(GrossOrNetEnum.NET).setPriceType(PriceTypeEnum.ASSET_PRICE))
                );

        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json");
        instructionBuilder
                .setBefore(tradeState);

        return new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                InstructionFunctionEnum.QUANTITY_CHANGE,
                Date.of(2021, 11, 11)
        );
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
                .setUnadjustedDate(Date.of(2028, 4, 1));

        interestRatePayoutBuilders.get(1)
                .getCalculationPeriodDates()
                .getTerminationDate()
                .getAdjustableDate()
                .setUnadjustedDate(Date.of(2028, 4, 1));

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
                Date.of(2021, 11, 11));

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
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(Date.of(2014, 4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(Date.of(2025, 4, 1));
                });
        interestRatePayouts.stream()
                .filter(payout -> payout.getRateSpecification().getFixedRate() != null)
                .findFirst()
                .ifPresent(fixedLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = fixedLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(Date.of(2018, 4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(Date.of(2025, 4, 1));
                });
        // quantity
        tradeStateBuilder.getTrade().getTradableProduct().getTradeLot().stream().map(TradeLot.TradeLotBuilder::getPriceQuantity).flatMap(Collection::stream).map(
                PriceQuantity.PriceQuantityBuilder::getQuantity).flatMap(Collection::stream).map(FieldWithMetaQuantity.FieldWithMetaQuantityBuilder::getValue).forEach(quantity -> {
            quantity.setAmount(new BigDecimal(10000));
        });
        // trade id
        tradeStateBuilder.getTrade().getTradeIdentifier().get(0).getAssignedIdentifier().get(0).setIdentifierValue("LEI1RPT0001KKKK");
        // trade date
        tradeStateBuilder.getTrade().setTradeDateValue(Date.of(2018, 4, 1));
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
