package org.isda.cdm.functions;

import cdm.base.datetime.AdjustableOrAdjustedOrRelativeDate;
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
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.common.functions.Create_BusinessEvent;
import cdm.event.workflow.WorkflowStep;
import cdm.legalagreement.common.*;
import cdm.observable.asset.Observable;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaFloatingRateOption;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.common.settlement.SettlementTerms;
import cdm.product.template.TradableProduct;
import cdm.product.template.TradeLot;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Lists;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResourcesUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.isda.cdm.functions.testing.FunctionUtils.guard;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInputCreationTest {

    private static boolean WRITE_EXPECTATIONS = Optional.ofNullable(System.getenv("WRITE_EXPECTATIONS"))
            .map(Boolean::parseBoolean).orElse(false);
    private static Optional<Path> TEST_WRITE_BASE_PATH = Optional.ofNullable(System.getenv("TEST_WRITE_BASE_PATH"))
            .map(Paths::get);
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionInputCreationTest.class);

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
    void validateExecutionIrSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex01-vanilla-swap-versioned.json",
                Date.parse("1994-12-12"),
                "cdm-sample-files/functions/execution-business-event/execution-ir-swap-func-input.json");
    }

    @Test
    void validateExecutionIrSwapWithInitialFeeFuncInputJson() throws IOException {
        String tradeStatePath = "result-json-files/fpml-5-10/products/rates/ird-initial-fee.json";
        TradeState.TradeStateBuilder tradeStateBuilder = ResourcesUtils.getObject(TradeState.class, tradeStatePath).toBuilder();

        // Remove the fee PQ
        TradeLot.TradeLotBuilder tradeLotBuilder = tradeStateBuilder
                .getTrade()
                .getTradableProduct()
                .getTradeLot()
                .get(0);
        List<? extends PriceQuantity.PriceQuantityBuilder> priceQuantityBuilders = tradeLotBuilder.getPriceQuantity();
        PriceQuantity.PriceQuantityBuilder fixedLegPQ = priceQuantityBuilders.get(0);
        PriceQuantity.PriceQuantityBuilder floatingLegPQ = priceQuantityBuilders.get(1);
        tradeLotBuilder.setPriceQuantity(Lists.newArrayList(fixedLegPQ, floatingLegPQ));

        // Copy the fee PQ into a transferState
        PriceQuantity cashPricePQ = priceQuantityBuilders.get(2);
        Price cashPrice = cashPricePQ.getPrice().get(0).getValue();
        BuyerSeller buyerSeller = cashPricePQ.getBuyerSeller();
        SettlementTerms settlementTerms = cashPricePQ.getSettlementTerms();
        TransferState transferState = TransferState.builder()
                .setTransfer(Transfer.builder()
                        .setTransferExpression(TransferExpression.builder().setPriceTransfer(FeeTypeEnum.UPFRONT))
                        .setPayerReceiver(PartyReferencePayerReceiver.builder()
                                .setPayerPartyReference(getPartyReference(tradeStateBuilder, buyerSeller.getBuyer()))
                                .setReceiverPartyReference(getPartyReference(tradeStateBuilder, buyerSeller.getSeller())))
                        .setQuantity(Quantity.builder()
                                .setAmount(cashPrice.getAmount())
                                .setUnitOfAmount(cashPrice.getUnitOfAmount()))
                        .setSettlementDate(settlementTerms.getSettlementDate().getAdjustedDate()));

        validateExecutionFuncInputJson(
                tradeStateBuilder.build(),
                transferState,
                Date.parse("2018-02-20"),
                "cdm-sample-files/functions/execution-business-event/execution-ir-swap-with-fee-func-input.json");
    }

    private ReferenceWithMetaParty getPartyReference(TradeState tradeState, CounterpartyRoleEnum role) {
        return tradeState.getTrade().getTradableProduct().getCounterparty().stream()
                .filter(c -> c.getRole() == role)
                        .map(c -> c.getPartyReference())
                        .findFirst().get();
    }

    @Test
    void validateExecutionIrSwapWithOtherPartyPaymentFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/swap-with-other-party-payment.json",
                Date.parse("1994-12-12"),
                "cdm-sample-files/functions/execution-business-event/execution-ir-swap-with-other-party-payment-func-input.json");
    }

    @Test
    void validateExecutionFraFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex08-fra.json",
                Date.parse("1991-05-14"),
                "cdm-sample-files/functions/execution-business-event/execution-fra-func-input.json");
    }

    @Test
    void validateExecutionBasisSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/CAD-Long-Initial-Stub-versioned.json",
                Date.parse("2017-12-18"),
                "cdm-sample-files/functions/execution-business-event/execution-basis-swap-func-input.json");
    }

    @Test
    void validateExecutionOisSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex07-ois-swap-uti.json",
                Date.parse("2001-01-25"),
                "cdm-sample-files/functions/execution-business-event/execution-ois-swap-func-input.json");
    }

    @Test
    void validateExecutionCreditDefaultSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/credit/cd-ex01-long-asia-corp-fixreg-versioned.json",
                Date.parse("2002-12-04"),
                "cdm-sample-files/functions/execution-business-event/execution-credit-default-swap-func-input.json");
    }

    @Test
    void validateExecutionFxForwardFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/fx/fx-ex03-fx-fwd.json",
                Date.parse("2001-11-19"),
                "cdm-sample-files/functions/execution-business-event/execution-fx-forward-func-input.json");
    }

    @Test
    void validateExecutionRepoFixedRateFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/repo/repo-ex01-repo-fixed-rate.json",
                Date.parse("2013-10-29"),
                "cdm-sample-files/functions/execution-business-event/execution-repo-fixed-rate-func-input.json");
    }

    @Test
    void validateExecutionSwaptionFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex09-euro-swaption-explicit-versioned.json",
                Date.parse("2000-08-30"),
                "cdm-sample-files/functions/execution-business-event/execution-swaption-func-input.json");
    }

    private void validateExecutionFuncInputJson(String tradeStatePath, Date eventDate, String expectedJsonPath) throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        validateExecutionFuncInputJson(tradeState, null, eventDate, expectedJsonPath);
    }

    private void validateExecutionFuncInputJson(TradeState tradeState, TransferState transferState, Date eventDate, String expectedJsonPath) throws IOException {
        Instruction instructionBuilder = Instruction.builder()
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setExecution(FunctionUtils.createExecutionInstructionFromTradeState(tradeState)))
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setTransfer(TransferInstruction.builder()
                                .addTransferState(transferState)))
                .prune();

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                null,
                eventDate);

        assertJsonEquals(expectedJsonPath, actual);
    }

    @Test
    void validateContractFormationIrSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex01-vanilla-swap-versioned.json",
                Date.parse("1994-12-12"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-ir-swap-func-input.json",
                null);
    }

    @Test
    void validateContractFormationIrSwapWithLegalAgreementFuncInputJson() throws IOException {
        String tradeStatePath = "result-json-files/fpml-5-10/products/rates/ird-ex01-vanilla-swap-versioned.json";
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);
        Date date = Date.parse("1994-12-12");

        LegalAgreement.LegalAgreementBuilder legalAgreement = LegalAgreement.builder()
                .addContractualPartyValue(guard(tradeState.getTrade().getParty()))
                .setAgreementDate(date)
                .setAgreementType(LegalAgreementType.builder()
                        .setName(LegalAgreementNameEnum.MASTER_AGREEMENT)
                        .setPublisher(LegalAgreementPublisherEnum.ISDA)
                        .setGoverningLaw(GoverningLawEnum.AS_SPECIFIED_IN_MASTER_AGREEMENT)
                        .build());

        validateContractFormationFuncInputJson(
                tradeStatePath,
                date,
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-ir-swap-with-legal-agreement-func-input.json",
                legalAgreement);
    }

    @Test
    void validateContractFormationFraFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex08-fra.json",
                Date.parse("1991-05-14"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-fra-func-input.json",
                null);
    }

    @Test
    void validateContractFormationBasisSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/CAD-Long-Initial-Stub-versioned.json",
                Date.parse("2017-12-18"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-basis-swap-func-input.json",
                null);
    }

    @Test
    void validateContractFormationOisSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex07-ois-swap-uti.json",
                Date.parse("2001-01-25"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-ois-swap-func-input.json",
                null);
    }

    @Test
    void validateContractFormationSwaptionFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex09-euro-swaption-explicit-versioned.json",
                Date.parse("2000-08-30"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-swaption-func-input.json",
                null);
    }

    @Test
    void validateContractFormationCreditDefaultSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/credit/cd-ex01-long-asia-corp-fixreg-versioned.json",
                Date.parse("2002-12-04"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-credit-default-swap-func-input.json",
                null);
    }

    @Test
    void validateContractFormationFxForwardFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/fx/fx-ex03-fx-fwd.json",
                Date.parse("2001-11-19"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-fx-forward-func-input.json",
                null);
    }

    @Test
    void validateContractFormationRepoFixedRateFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/repo/repo-ex01-repo-fixed-rate.json",
                Date.parse("2013-10-29"),
                "cdm-sample-files/functions/contract-formation-business-event/contract-formation-repo-fixed-rate-func-input.json",
                null);
    }

    private void validateContractFormationFuncInputJson(String tradeStatePath, Date eventDate, String expectedJsonPath, LegalAgreement legalAgreement) throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        Instruction instructionBuilder = Instruction.builder()
                .setBefore(tradeState)
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setContractFormation(ContractFormationInstruction.builder().addLegalAgreement(legalAgreement)));

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                EventIntentEnum.CONTRACT_FORMATION,
                eventDate);

        assertJsonEquals(expectedJsonPath, actual);
    }

    @Test
    void validateFullTerminationVanillaSwapFuncInputJson() throws IOException {
        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(10000))
                                        .setUnitOfAmount(UnitType.builder()
                                                .setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))));

        validateQuantityChangeFuncInputJson(
                getTerminationVanillaSwapTradeState(),
                Date.of(2019, 12, 12),
                "cdm-sample-files/functions/quantity-change-business-event/full-termination-vanilla-swap-func-input.json",
                quantityChangeInstruction);
    }

    @Test
    void validateFullTerminationEquitySwapFuncInputJson() throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json");

        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(760400))
                                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))))
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(28469376))
                                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD")))));

        validateQuantityChangeFuncInputJson(
                tradeState,
                Date.of(2021, 11, 11),
                "cdm-sample-files/functions/quantity-change-business-event/full-termination-equity-swap-func-input.json",
                quantityChangeInstruction);
    }

    @Test
    void validatePartialTerminationVanillaSwapFuncInputJson() throws IOException {
        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(3000))
                                        .setUnitOfAmount(UnitType.builder()
                                                .setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))));

        validateQuantityChangeFuncInputJson(
                getTerminationVanillaSwapTradeState(),
                Date.of(2019, 12, 12),
                "cdm-sample-files/functions/quantity-change-business-event/partial-termination-vanilla-swap-func-input.json",
                quantityChangeInstruction);
    }

    @Test
    void validatePartialTerminationEquitySwapFuncInputJson() throws IOException {
        // The tradeState input to partial termination is output from increase event
        CreateBusinessEventWorkflowInput increaseEquitySwapInput = getIncreaseEquitySwapFuncInputJson();
        Create_BusinessEvent createBusinessEvent = injector.getInstance(Create_BusinessEvent.class);
        BusinessEvent increaseOutput = createBusinessEvent.evaluate(increaseEquitySwapInput.getInstruction(),
                increaseEquitySwapInput.getIntent(),
                increaseEquitySwapInput.getEventDate());
        TradeState increaseTradeState = increaseOutput.getAfter().get(0);

        // Quantity change to terminate tradeLot LOT-1.  Quantity in tradeLot LOT-2 remains unchanged.
        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addLotIdentifier(Identifier.builder()
                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                .setIdentifierValue("LOT-1")))
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(760400))
                                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))))
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(28469376))
                                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD")))));

        validateQuantityChangeFuncInputJson(
                increaseTradeState,
                Date.of(2021, 11, 11),
                "cdm-sample-files/functions/quantity-change-business-event/partial-termination-equity-swap-func-input.json",
                quantityChangeInstruction);
    }

    @Test
    void validateIncreaseEquitySwapFuncInputJson() throws IOException {
        CreateBusinessEventWorkflowInput actual = getIncreaseEquitySwapFuncInputJson();

        assertJsonEquals("cdm-sample-files/functions/quantity-change-business-event/increase-equity-swap-func-input.json", actual);
    }

    /**
     * This is in a separate method because it is used by validateIncreaseEquitySwapFuncInputJson (to validate input),
     * and validatePartialTerminationEquitySwapFuncInputJson (as the input uses the output of the increase func).
     */
    @NotNull
    private CreateBusinessEventWorkflowInput getIncreaseEquitySwapFuncInputJson() throws IOException {
        QuantityChangeInstruction quantityChangeInstructions = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.INCREASE)
                .addLotIdentifier(Identifier.builder()
                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                .setIdentifierValue("LOT-2")))
                // equity payout PQ
                .addChange(PriceQuantity.builder()
                        .setObservable(Observable.builder()
                                .addProductIdentifier(FieldWithMetaProductIdentifier.builder()
                                        .setMeta(createKey("productIdentifier-1"))
                                        .setValue(ProductIdentifier.builder()
                                                .setSource(ProductIdTypeEnum.OTHER)
                                                .setIdentifier(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.abc.com/instrumentId"))
                                                        .setValue("SHPGY.O")))))
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setMeta(createKey("quantity-2"))
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(250000))
                                        .setUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))))
                        .addPrice(FieldWithMetaPrice.builder()
                                .setMeta(createKey("price-2"))
                                .setValue(Price.builder()
                                        .setAmount(BigDecimal.valueOf(30))
                                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
                                        .setPerUnitOfAmount(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))
                                        .setPriceExpression(PriceExpression.builder()
                                                .setPriceType(PriceTypeEnum.ASSET_PRICE)
                                                .setGrossOrNet(GrossOrNetEnum.NET)))))
                // interest rate payout PQ
                .addChange(PriceQuantity.builder()
                        .setObservable(Observable.builder()
                                .setRateOption(FieldWithMetaFloatingRateOption.builder()
                                        .setMeta(createKey("rateOption-1"))
                                        .setValue(FloatingRateOption.builder()
                                                .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
                                                .setIndexTenor(Period.builder()
                                                        .setPeriod(PeriodEnum.M)
                                                        .setPeriodMultiplier(1)))))
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setMeta(createKey("quantity-1"))
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(7500000))
                                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))))
                        .addPrice(FieldWithMetaPrice.builder()
                                .setMeta(createKey("price-1"))
                                .setValue(Price.builder()
                                        .setAmount(BigDecimal.valueOf(0.0020))
                                        .setUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
                                        .setPerUnitOfAmount(UnitType.builder().setCurrencyValue("USD"))
                                        .setPriceExpression(PriceExpression.builder()
                                                .setPriceType(PriceTypeEnum.INTEREST_RATE)
                                                .setSpreadType(SpreadTypeEnum.SPREAD)))));

        TradeState tradeState = getQuantityChangeEquitySwapTradeState();

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBefore(tradeState)
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstructions))
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setTransfer(getTransferInstruction(tradeState)));

        return new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                null,
                Date.of(2021, 11, 11)
        );
    }

    private void validateQuantityChangeFuncInputJson(TradeState tradeState, Date eventDate, String expectedJsonPath, QuantityChangeInstruction quantityChangeInstruction) throws IOException {
        Instruction instructionBuilder = Instruction.builder()
                .setBefore(tradeState)
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstruction))
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setTransfer(getTransferInstruction(tradeState)));

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructionBuilder.build()),
                null,
                eventDate);

        assertJsonEquals(expectedJsonPath, actual);
    }

    @NotNull
    private TransferInstruction.TransferInstructionBuilder getTransferInstruction(TradeState tradeState) {
        Trade trade = tradeState.getTrade();
        List<? extends Counterparty> counterparties = trade.getTradableProduct().getCounterparty();
        UnitType currencyUnitType = trade.getTradableProduct().getTradeLot().stream()
                .map(TradeLot::getPriceQuantity)
                .flatMap(Collection::stream)
                .map(PriceQuantity::getQuantity)
                .flatMap(Collection::stream)
                .map(FieldWithMetaQuantity::getValue)
                .map(Quantity::getUnitOfAmount)
                .filter(unit -> unit.getCurrency() != null)
                .findFirst()
                .orElse(null);
        return TransferInstruction.builder()
                .addTransferState(TransferState.builder()
                        .setTransfer(Transfer.builder()
                                .setTransferExpression(TransferExpression.builder().setPriceTransfer(FeeTypeEnum.UPFRONT))
                                .setPayerReceiver(PartyReferencePayerReceiver.builder()
                                        .setPayerPartyReference(counterparties.get(0).getPartyReference())
                                        .setReceiverPartyReference(counterparties.get(1).getPartyReference()))
                                .setQuantity(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(2000.00))
                                        .setUnitOfAmount(currencyUnitType))
                                .setSettlementDate(AdjustableOrAdjustedOrRelativeDate.builder()
                                        .setAdjustedDateValue(trade.getTradeDate().getValue()))));
    }

    @Test
    void validateCompressionFuncInputJson() throws IOException {
        List<Instruction> instructions = new ArrayList<>();
        QuantityChangeInstruction terminateInstructions = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaQuantity.builder()
                                .setValue(Quantity.builder()
                                        .setAmount(BigDecimal.valueOf(0.0))
                                        .setUnitOfAmount(UnitType.builder()
                                                .setCurrency(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                                                        .setValue("USD"))))));


        instructions.add(Instruction.builder()
                .setBefore(getWorkflowStepAfter("result-json-files/native-cdm-events/Example-07-Submission-1.json"))
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(terminateInstructions))
                .build());

        instructions.add(Instruction.builder()
                .setBefore(getWorkflowStepAfter("result-json-files/native-cdm-events/Example-07-Submission-2.json"))
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(terminateInstructions))
                .build());

        instructions.add(Instruction.builder()
                .setBefore(getWorkflowStepAfter("result-json-files/native-cdm-events/Example-07-Submission-3.json"))
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(terminateInstructions))
                .build());

        instructions.add(Instruction.builder()
                .addPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setExecution(getCompressionExecutionInstructionInputJson()))
                .build());

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                instructions,
                EventIntentEnum.COMPRESSION,
                Date.of(2018, 4, 3));

        assertJsonEquals("cdm-sample-files/functions/compression-func-input.json", actual);
    }

    private ExecutionInstruction getCompressionExecutionInstructionInputJson() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder =
                ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/rates/USD-Vanilla-swap.json").toBuilder();

        Trade.TradeBuilder tradeBuilder = tradeStateBuilder.getTrade();
        TradableProduct.TradableProductBuilder tradableProductBuilder = tradeBuilder.getTradableProduct();

        TradeLot.TradeLotBuilder tradeLotBuilder = tradableProductBuilder.getTradeLot().get(0);
        tradeLotBuilder
                .getPriceQuantity().get(0)
                .getQuantity().get(0)
                .getValue().setAmount(BigDecimal.valueOf(16000.00));
        tradeLotBuilder
                .getPriceQuantity().get(1)
                .getQuantity().get(0)
                .getValue().setAmount(BigDecimal.valueOf(16000.00));

        List<? extends InterestRatePayout.InterestRatePayoutBuilder> interestRatePayoutBuilders = tradableProductBuilder
                .getProduct()
                .getContractualProduct()
                .getEconomicTerms()
                .getPayout()
                .getInterestRatePayout();

        Date effectiveDate = Date.of(2018, 4, 3);
        Date terminationDate = Date.of(2026, 2, 8);

        setDates(interestRatePayoutBuilders.get(0), effectiveDate, terminationDate);
        setDates(interestRatePayoutBuilders.get(1), effectiveDate, terminationDate);

        tradeBuilder
                .getParty().get(0)
                .getPartyId().get(0)
                .setValue("LEI1RPT0001");

        tradeBuilder
                .getParty().get(1)
                .getPartyId().get(0)
                .setValue("LEI2CP0002");

        Identifier tradeIdentifier = Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifier(FieldWithMetaString.builder()
                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/uti"))
                                .setValue("LEI1RPT0003EFG"))
                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER))
                        .setIssuer(FieldWithMetaString.builder()
                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso17442"))
                                .setValue("LEI1RPT0001"));
        tradeBuilder
                .setTradeIdentifier(Lists.newArrayList(tradeIdentifier.build()))
                .setTradeDateValue(effectiveDate);

        ResourcesUtils.reKey(tradeStateBuilder);

        return FunctionUtils.createExecutionInstructionFromTradeState(tradeStateBuilder.build());
    }

    private void setDates(InterestRatePayout.InterestRatePayoutBuilder payout, Date effectiveDate, Date terminationDate) {
        CalculationPeriodDates.CalculationPeriodDatesBuilder floatingLegCalcDates = payout
                .getCalculationPeriodDates();
        floatingLegCalcDates
                .getEffectiveDate()
                .getAdjustableDate()
                .setUnadjustedDate(effectiveDate);
        floatingLegCalcDates
                .getTerminationDate()
                .getAdjustableDate()
                .setUnadjustedDate(terminationDate);
    }

    private TradeState getWorkflowStepAfter(String resourceName) throws IOException {
        WorkflowStep workflowStep = ResourcesUtils.getObject(WorkflowStep.class, resourceName);
        // Get after TradeState
        TradeState.TradeStateBuilder tradeStateBuilder = workflowStep.getBusinessEvent().getPrimitives().stream()
                .map(PrimitiveEvent::getContractFormation)
                .map(ContractFormationPrimitive::getAfter)
                .findFirst().orElse(null).toBuilder();
        // Set Trade.party
        tradeStateBuilder.getTrade().setParty(workflowStep.getParty());
        return tradeStateBuilder.build();
    }

    @Test
    void validateNovationFuncInputJson() throws IOException {
        SplitInstruction splitInstruction = SplitInstruction.builder()
                .addBreakdown(PrimitiveInstructionList.builder()
                        .addPrimitiveInstruction(PrimitiveInstruction.builder()
                                .setPartyChange(PartyChangeInstruction.builder()
                                        .setCounterparty(Counterparty.builder()
                                                .setPartyReferenceValue(Party.builder()
                                                                .setMeta(MetaFields.builder().setExternalKey("party3"))
                                                                .setNameValue("Bank Z")
                                                                .addPartyId(FieldWithMetaString.builder()
                                                                        .setMeta(MetaFields.builder()
                                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso17442"))
                                                                        .setValue("LEI3RPT0003")))
                                                .setRole(CounterpartyRoleEnum.PARTY_1))
                                        .setTradeId(Lists.newArrayList(Identifier.builder()
                                                .addAssignedIdentifier(AssignedIdentifier.builder()
                                                        .setIdentifier(FieldWithMetaString.builder()
                                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                                .setValue("LEI3RPT0003CCC"))
                                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER))
                                                .setIssuer(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                                        .setValue("LEI3RPT0003")))))))
                .addBreakdown(PrimitiveInstructionList.builder()
                        .addPrimitiveInstruction(PrimitiveInstruction.builder()
                                .setQuantityChange(QuantityChangeInstruction.builder()
                                        .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                        .addChange(PriceQuantity.builder()
                                                .addQuantity(FieldWithMetaQuantity.builder()
                                                        .setValue(Quantity.builder()
                                                                .setAmount(BigDecimal.valueOf(0.0))
                                                                .setUnitOfAmount(UnitType.builder()
                                                                        .setCurrency(FieldWithMetaString.builder()
                                                                                .setMeta(MetaFields.builder()
                                                                                        .setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                                                                                .setValue("USD")))))))));

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBefore(getWorkflowStepAfter("result-json-files/native-cdm-events/Example-04-Submission-1.json"))
                .addPrimitiveInstruction(PrimitiveInstruction.builder().setSplit(splitInstruction));

        ResourcesUtils.reKey(instructions);

        CreateBusinessEventWorkflowInput actual = new CreateBusinessEventWorkflowInput(
                Lists.newArrayList(instructions.build()),
                EventIntentEnum.NOVATION,
                Date.of(2018, 4, 3));

        assertJsonEquals("cdm-sample-files/functions/novation-func-input.json", actual);
    }

    @Test
    void validateCreateAllocationWorkflowInputJson() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = getTerminationVanillaSwapTradeState();

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

        assertJsonEquals("cdm-sample-files/functions/allocation-workflow-func-input.json", executionInstruction);
    }

    /**
     * Use record-ex01-vanilla-swap.json sample and modify it to look exactly like CFTC example 3 (used in regs termination example)
     */
    @NotNull
    private TradeState.TradeStateBuilder getTerminationVanillaSwapTradeState() throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/rates/USD-Vanilla-swap.json");
        // parties
        List<Party> parties = tradeState.getTrade().getParty().stream()
                .filter(p -> p.getName().getValue().equals("Bank X") || p.getName().getValue().equals("Bank Y"))
                .collect(Collectors.toList());
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
        tradeStateBuilder.getTrade().getTradableProduct().getTradeLot().stream()
                .map(TradeLot.TradeLotBuilder::getPriceQuantity)
                .flatMap(Collection::stream)
                .map(PriceQuantity.PriceQuantityBuilder::getQuantity)
                .flatMap(Collection::stream)
                .map(FieldWithMetaQuantity.FieldWithMetaQuantityBuilder::getValue)
                .forEach(quantity -> quantity.setAmount(new BigDecimal(10000)));
        // trade id
        tradeStateBuilder.getTrade().getTradeIdentifier().get(0).getAssignedIdentifier().get(0).setIdentifierValue("LEI1RPT0001KKKK");
        // trade date
        tradeStateBuilder.getTrade().setTradeDateValue(Date.of(2018, 4, 1));
        return tradeStateBuilder;
    }

    /**
     * eqs-ex01-single-underlyer-execution-long-form.json with LOT-1 trade lot identifier
     */
    @NotNull
    private TradeState getQuantityChangeEquitySwapTradeState() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json").toBuilder();
        TradeLot.TradeLotBuilder tradeLotBuilder = tradeStateBuilder.getTrade().getTradableProduct().getTradeLot().get(0);
        tradeLotBuilder.addLotIdentifier(Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifierValue("LOT-1")));
        return tradeStateBuilder.build();
    }

    private MetaFields.MetaFieldsBuilder createKey(String s) {
        return MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue(s));
    }

    private void updatePartyId(TradeState.TradeStateBuilder tradeStateBuilder, String partyName, String partyId) {
        tradeStateBuilder.getTrade().getParty().stream()
                .filter(p -> p.getName().getValue().equals(partyName))
                .findFirst().ifPresent(bankXParty -> bankXParty.setPartyId(Arrays.asList(FieldWithMetaString.builder()
                        .setValue(partyId)
                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso17442")))));
    }

    private void assertJsonEquals(String expectedJsonPath, Object actual) throws IOException {
        String actualJson = STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual);
        String expectedJson = ResourcesUtils.getJson(expectedJsonPath);
        if (!expectedJson.equals(actualJson)) {
            if (WRITE_EXPECTATIONS) {
                writeExpectation(expectedJsonPath, actualJson);
            }
        }
        assertEquals(expectedJson, actualJson,
                "The input JSON for "+ Paths.get(expectedJsonPath).getFileName() +" has been updated (probably due to a model change). Update the input file");
    }

    private void writeExpectation(String writePath, String json) {
        // Add environment variable TEST_WRITE_BASE_PATH to override the base write path, e.g.
        // TEST_WRITE_BASE_PATH=/Users/hugohills/dev/github/REGnosys/rosetta-cdm/rosetta-source/src/main/resources/
        TEST_WRITE_BASE_PATH.filter(Files::exists).ifPresent(basePath -> {
            Path expectationFilePath = basePath.resolve(writePath);
            try {
                Files.createDirectories(expectationFilePath.getParent());
                Files.write(expectationFilePath, json.getBytes());
                LOGGER.warn("Updated expectation file {}", expectationFilePath.toAbsolutePath());
            } catch (IOException e) {
                LOGGER.error("Failed to write expectation file {}", expectationFilePath.toAbsolutePath(), e);
            }
        });
    }
}
