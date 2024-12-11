package org.isda.cdm.functions;

import cdm.base.datetime.*;
import cdm.base.math.*;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.*;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.common.functions.*;
import cdm.event.common.metafields.ReferenceWithMetaTradeState;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.MessageInformation;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.functions.Create_WorkflowStep;
import cdm.legaldocumentation.common.*;
import cdm.legaldocumentation.master.MasterAgreementTypeEnum;
import cdm.observable.asset.Observable;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaInterestRateIndex;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.ReferenceInformation;
import cdm.product.collateral.*;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.settlement.ScheduledTransferEnum;
import cdm.product.common.settlement.SettlementDate;
import cdm.product.template.NonTransferableProduct;
import cdm.product.template.Payout;
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
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResourcesUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.isda.cdm.functions.FunctionUtils.guard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.*;

class FunctionInputCreationTest {

    private static final boolean WRITE_EXPECTATIONS =
            Optional.ofNullable(System.getenv("WRITE_EXPECTATIONS"))
                    .map(Boolean::parseBoolean).orElse(false);
    private static final Optional<Path> TEST_WRITE_BASE_PATH =
            Optional.ofNullable(System.getenv("TEST_WRITE_BASE_PATH")).map(Paths::get);
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
                        bind(PostProcessor.class).to(WorkflowPostProcessor.class);
                    }
                });
        injector = Guice.createInjector(module);
    }

    @Test
    void validateExecutionIrSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex01-vanilla-swap-versioned.json",
                Date.parse("1994-12-12"),
                "cdm-sample-files/functions/business-event/execution/execution-ir-swap-func-input.json");
    }

    @Test
    void validateExecutionIrSwapWithInitialFeeFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-initial-fee.json",
                Date.parse("2018-02-20"),
                "cdm-sample-files/functions/business-event/execution/execution-ir-swap-with-fee-func-input.json");
    }

    @Test
    void validateExecutionIrSwapWithOtherPartyPaymentFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/swap-with-other-party-payment.json",
                Date.parse("1994-12-12"),
                "cdm-sample-files/functions/business-event/execution/execution-ir-swap-with-other-party-payment-func-input.json");
    }

    @Test
    void validateExecutionFraFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex08-fra.json",
                Date.parse("1991-05-14"),
                "cdm-sample-files/functions/business-event/execution/execution-fra-func-input.json");
    }

    @Test
    void validateExecutionBasisSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/CAD-Long-Initial-Stub-versioned.json",
                Date.parse("2017-12-18"),
                "cdm-sample-files/functions/business-event/execution/execution-basis-swap-func-input.json");
    }

    @Test
    void validateExecutionOisSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex07-ois-swap-uti.json",
                Date.parse("2001-01-25"),
                "cdm-sample-files/functions/business-event/execution/execution-ois-swap-func-input.json");
    }

    @Test
    void validateExecutionCreditDefaultSwapFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/credit/cd-ex01-long-asia-corp-fixreg-versioned.json",
                Date.parse("2002-12-04"),
                "cdm-sample-files/functions/business-event/execution/execution-credit-default-swap-func-input.json");
    }

    @Test
    void validateExecutionFxForwardFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/fx/fx-ex03-fx-fwd.json",
                Date.parse("2001-11-19"),
                "cdm-sample-files/functions/business-event/execution/execution-fx-forward-func-input.json");
    }

    @Test
    void validateExecutionSwaptionFuncInputJson() throws IOException {
        validateExecutionFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex09-euro-swaption-explicit-versioned.json",
                Date.parse("2000-08-30"),
                "cdm-sample-files/functions/business-event/execution/execution-swaption-func-input.json");
    }

    private void validateExecutionFuncInputJson(String tradeStatePath, Date eventDate, String expectedJsonPath) throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        CreateBusinessEventInput actual = getExecutionFuncInputJson(tradeState, eventDate);

        assertJsonEquals(expectedJsonPath, actual);
    }


    private CreateBusinessEventInput getExecutionFuncInputJson(TradeState tradeState, Date eventDate) {
        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();

        // Get the TransferStates from the transferHistory
        List<? extends TransferState.TransferStateBuilder> transferState =
                new ArrayList<>(tradeStateBuilder.getTransferHistory());
        // Clear the transferHistory, so the adding of Transfer in the Instructions can be demonstrated
        tradeStateBuilder.getTransferHistory().clear();

        PrimitiveInstruction.PrimitiveInstructionBuilder primitiveInstructionBuilder =
                PrimitiveInstruction.builder()
                        .setExecution(FunctionUtils.createExecutionInstructionFromTradeState(tradeStateBuilder));

        if (!transferState.isEmpty()) {
            primitiveInstructionBuilder
                    .setTransfer(TransferInstruction.builder().setTransferState(transferState));
        }

        return new CreateBusinessEventInput(
                Lists.newArrayList(Instruction.builder().setPrimitiveInstruction(primitiveInstructionBuilder.build())),
                null,
                eventDate,
                null);
    }

    @Test
    void validateContractFormationIrSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex01-vanilla-swap-versioned.json",
                Date.parse("1994-12-12"),
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-ir-swap-func-input.json",
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
                .setLegalAgreementIdentification(LegalAgreementIdentification.builder()
                        .setGoverningLaw(GoverningLawEnum.AS_SPECIFIED_IN_MASTER_AGREEMENT)
                        .setAgreementName(AgreementName.builder()
                                .setAgreementType(LegalAgreementTypeEnum.MASTER_AGREEMENT)
                                .setMasterAgreementTypeValue(MasterAgreementTypeEnum.ISDA_MASTER))
                        .setPublisher(LegalAgreementPublisherEnum.ISDA)
                        .build());

        validateContractFormationFuncInputJson(
                tradeStatePath,
                date,
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-ir-swap-with-legal-agreement-func-input.json",
                legalAgreement);
    }

    @Test
    void validateContractFormationFraFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex08-fra.json",
                Date.parse("1991-05-14"),
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-fra-func-input.json",
                null);
    }

    @Test
    void validateContractFormationBasisSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/CAD-Long-Initial-Stub-versioned.json",
                Date.parse("2017-12-18"),
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-basis-swap-func-input.json",
                null);
    }

    @Test
    void validateContractFormationOisSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex07-ois-swap-uti.json",
                Date.parse("2001-01-25"),
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-ois-swap-func-input.json",
                null);
    }

    @Test
    void validateContractFormationSwaptionFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/rates/ird-ex09-euro-swaption-explicit-versioned.json",
                Date.parse("2000-08-30"),
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-swaption-func-input.json",
                null);
    }

    @Test
    void validateContractFormationCreditDefaultSwapFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/credit/cd-ex01-long-asia-corp-fixreg-versioned.json",
                Date.parse("2002-12-04"),
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-credit-default-swap-func-input.json",
                null);
    }

    @Test
    void validateContractFormationFxForwardFuncInputJson() throws IOException {
        validateContractFormationFuncInputJson(
                "result-json-files/fpml-5-10/products/fx/fx-ex03-fx-fwd.json",
                Date.parse("2001-11-19"),
                "cdm-sample-files/functions/business-event/contract-formation/contract-formation-fx-forward-func-input.json",
                null);
    }

    private void validateContractFormationFuncInputJson(String tradeStatePath, Date eventDate, String expectedJsonPath, LegalAgreement legalAgreement) throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        Instruction instructionBuilder = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setContractFormation(ContractFormationInstruction.builder()
                                .addLegalAgreement(legalAgreement)))
                .prune();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructionBuilder.build()),
                EventIntentEnum.CONTRACT_FORMATION,
                eventDate,
                null);

        assertJsonEquals(expectedJsonPath, actual);
    }

    @Test
    void validateFullTerminationVanillaSwapFuncInputJson() throws IOException {
        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(10000))
                                        .setUnit(UnitType.builder()
                                                .setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))));

        validateQuantityChangeFuncInputJson(
                getTerminationVanillaSwapTradeState(),
                Date.of(2019, 12, 12),
                "cdm-sample-files/functions/business-event/quantity-change/full-termination-vanilla-swap-func-input.json",
                quantityChangeInstruction, FeeTypeEnum.TERMINATION);
    }

    @Test
    void validateFullTerminationEquitySwapFuncInputJson() throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json");

        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(760400))
                                        .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))))
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(28469376))
                                        .setUnit(UnitType.builder().setCurrencyValue("USD")))));

        validateQuantityChangeFuncInputJson(
                tradeState,
                Date.of(2021, 11, 11),
                "cdm-sample-files/functions/business-event/quantity-change/full-termination-equity-swap-func-input.json",
                quantityChangeInstruction, FeeTypeEnum.TERMINATION);
    }

    @Test
    void validatePartialTerminationVanillaSwapFuncInputJson() throws IOException {
        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(3000))
                                        .setUnit(UnitType.builder()
                                                .setCurrency(FieldWithMetaString.builder()
                                                        .setValue("USD")
                                                        .setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217")))))));

        validateQuantityChangeFuncInputJson(
                getTerminationVanillaSwapTradeState(),
                Date.of(2019, 12, 12),
                "cdm-sample-files/functions/business-event/quantity-change/partial-termination-vanilla-swap-func-input.json",
                quantityChangeInstruction, FeeTypeEnum.PARTIAL_TERMINATION);
    }

    @Test
    void validatePartialTerminationEquitySwapFuncInputJson() throws IOException {
        // Quantity change to terminate tradeLot LOT-2.  Quantity in tradeLot LOT-1 remains unchanged.
        // 20 percentage decrease. Output quantity should be 152,080 shares and 5,693,875 USD
        final QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeInstructionBuilder = QuantityChangeInstruction.builder();
        QuantityChangeInstruction quantityChangeInstruction = quantityChangeInstructionBuilder
                .setDirection(QuantityChangeDirectionEnum.DECREASE)
                .addLotIdentifier(Identifier.builder()
                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                .setIdentifierValue("LOT-2")))
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(152080))
                                        .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))))
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(5693875))
                                        .setUnit(UnitType.builder().setCurrencyValue("USD")))));
        reKey(quantityChangeInstructionBuilder);

        validateQuantityChangeFuncInputJson(
                getQuantityChangeEquitySwapTradeStateWithMultipleTradeLots(),
                Date.of(2021, 11, 11),
                "cdm-sample-files/functions/business-event/quantity-change/partial-termination-equity-swap-func-input.json",
                quantityChangeInstruction, FeeTypeEnum.PARTIAL_TERMINATION);
    }

    @Test
    void validateIncreaseEquitySwapFuncInputJson() throws IOException {
        CreateBusinessEventInput actual = getIncreaseEquitySwapFuncInputJson();
        assertJsonEquals("cdm-sample-files/functions/business-event/quantity-change/increase-equity-swap-func-input.json", actual);
    }

    @Test
    void validateIncreaseEquitySwapExistingTradeLotFuncInputJson() throws IOException {
        CreateBusinessEventInput actual = getIncreaseEquitySwapExistingTradeLotFuncInputJson();
        assertJsonEquals("cdm-sample-files/functions/business-event/quantity-change/increase-equity-swap-existing-trade-lot-func-input.json", actual);
    }

    private CreateBusinessEventInput getIncreaseEquitySwapExistingTradeLotFuncInputJson() throws IOException {
        final Identifier.IdentifierBuilder identifierBuilder = Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifierValue("LOT-2"));
        QuantityChangeInstruction quantityChangeInstructions = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.INCREASE)
                .addLotIdentifier(identifierBuilder).addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setMeta(createKey("quantity-2"))
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(250000))
                                        .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE)))))
                // interest rate payout PQ
                .addChange(PriceQuantity.builder()
                        .setObservableValue(Observable.builder()
                                .setIndex(Index.builder()
                                        .setInterestRateIndex(FieldWithMetaInterestRateIndex.builder()
                                                .setMeta(createKey("rateOption-1"))
                                                .setValue(InterestRateIndex.builder()
                                                        .setFloatingRateIndex(FloatingRateIndex.builder()
                                                                .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
                                                                .setIndexTenor(Period.builder()
                                                                        .setPeriod(PeriodEnum.M)
                                                                        .setPeriodMultiplier(1)))))))
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setMeta(createKey("quantity-1"))
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(7500000))
                                        .setUnit(UnitType.builder().setCurrencyValue("USD"))))
                        .addPrice(FieldWithMetaPriceSchedule.builder()
                                .setMeta(createKey("price-1"))
                                .setValue(PriceSchedule.builder()
                                        .setValue(BigDecimal.valueOf(0.0020))
                                        .setUnit(UnitType.builder().setCurrencyValue("USD"))
                                        .setPerUnitOf(UnitType.builder().setCurrencyValue("USD"))
                                        .setArithmeticOperator(ArithmeticOperationEnum.ADD)
                                        .setPriceType(PriceTypeEnum.INTEREST_RATE))));

        TradeState tradeState = getQuantityChangeEquitySwapTradeStateWithMultipleTradeLots();

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstructions));

        reKey(instructionBuilder);

        return new CreateBusinessEventInput(
                Lists.newArrayList(instructionBuilder.build()),
                null,
                Date.of(2021, 11, 11),
                null);
    }

    private TradeState getQuantityChangeEquitySwapTradeStateWithMultipleTradeLots() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json").toBuilder();
        TradableProduct.TradableProductBuilder tradableProductBuilder = tradeStateBuilder.getTrade();
        TradeLot.TradeLotBuilder tradeLot1Builder = tradableProductBuilder.getTradeLot().get(0);

        //Take a copy of the trade lot
        TradeLot.TradeLotBuilder tradeLot2Builder = tradeLot1Builder.build().toBuilder();

        tradeLot1Builder.addLotIdentifier(Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifierValue("LOT-1")));
        tradeLot2Builder.addLotIdentifier(Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifierValue("LOT-2")));
        tradableProductBuilder.addTradeLot(tradeLot2Builder);
        reKey(tradableProductBuilder);
        return tradeStateBuilder.build();
    }

    /**
     * This is in a separate method because it is used by validateIncreaseEquitySwapFuncInputJson (to validate input),
     * and validatePartialTerminationEquitySwapFuncInputJson (as the input uses the output of the increase func).
     */

    private CreateBusinessEventInput getIncreaseEquitySwapFuncInputJson() throws IOException {
        QuantityChangeInstruction quantityChangeInstructions = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.INCREASE)
                .addLotIdentifier(Identifier.builder()
                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                .setIdentifierValue("LOT-2")))
                // equity payout PQ
                .addChange(PriceQuantity.builder()
                        .setObservableValue(Observable.builder()
                                .setAsset(Asset.builder()
                                        .setInstrument(Instrument.builder()
                                                .setSecurity(Security.builder()
                                                        .setInstrumentType(InstrumentTypeEnum.EQUITY)
                                                        .addIdentifier(AssetIdentifier.builder()
                                                                .setIdentifierType(AssetIdTypeEnum.OTHER)
                                                                .setIdentifier(FieldWithMetaString.builder()
                                                                        .setMeta(MetaFields.builder().setScheme("http://www.abc.com/instrumentId"))
                                                                        .setValue("SHPGY.O")))))))
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setMeta(createKey("quantity-2"))
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(250000))
                                        .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))))
                        .addPrice(FieldWithMetaPriceSchedule.builder()
                                .setMeta(createKey("price-2"))
                                .setValue(PriceSchedule.builder()
                                        .setValue(BigDecimal.valueOf(30))
                                        .setUnit(UnitType.builder().setCurrencyValue("USD"))
                                        .setPerUnitOf(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE))
                                        .setPriceType(PriceTypeEnum.ASSET_PRICE))))
                // interest rate payout PQ
                .addChange(PriceQuantity.builder()
                        .setObservableValue(Observable.builder()
                                .setIndex(Index.builder()
                                        .setInterestRateIndex(
                                                FieldWithMetaInterestRateIndex.builder()
                                                        .setMeta(createKey("rateOption-1"))
                                                        .setValue(InterestRateIndex.builder()
                                                                .setFloatingRateIndex(FloatingRateIndex.builder()
                                                                        .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_LIBOR_BBA)
                                                                        .setIndexTenor(Period.builder()
                                                                                .setPeriod(PeriodEnum.M)
                                                                                .setPeriodMultiplier(1))
                                                                )

                                                        )
                                        )
                                )
                        )
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setMeta(createKey("quantity-1"))
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(7500000))
                                        .setUnit(UnitType.builder().setCurrencyValue("USD"))))
                        .addPrice(FieldWithMetaPriceSchedule.builder()
                                .setMeta(createKey("price-1"))
                                .setValue(PriceSchedule.builder()
                                        .setValue(BigDecimal.valueOf(0.0020))
                                        .setUnit(UnitType.builder().setCurrencyValue("USD"))
                                        .setPerUnitOf(UnitType.builder().setCurrencyValue("USD"))
                                        .setArithmeticOperator(ArithmeticOperationEnum.ADD)
                                        .setPriceType(PriceTypeEnum.INTEREST_RATE))));

        TradeState tradeState = getQuantityChangeEquitySwapTradeState();

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstructions)
                        .setTransfer(getTransferInstruction(tradeState, FeeTypeEnum.INCREASE)));
        reKey(instructionBuilder);
        return new CreateBusinessEventInput(
                Lists.newArrayList(instructionBuilder.build()),
                null,
                Date.of(2021, 11, 11),
                null);
    }

    private void validateQuantityChangeFuncInputJson(TradeState tradeState, Date eventDate, String expectedJsonPath, QuantityChangeInstruction quantityChangeInstruction, FeeTypeEnum feeType) throws IOException {
        Instruction instructionBuilder = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstruction)
                        .setTransfer(getTransferInstruction(tradeState, feeType)));

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructionBuilder.build()),
                null,
                eventDate,
                null);

        assertJsonEquals(expectedJsonPath, actual);
    }


    private TransferInstruction.TransferInstructionBuilder getTransferInstruction(TradeState tradeState, FeeTypeEnum feeType) {
        Trade trade = tradeState.getTrade();
        List<? extends Counterparty> counterparties = trade.getCounterparty();
        UnitType currencyUnitType = trade.getTradeLot().stream()
                .map(TradeLot::getPriceQuantity)
                .flatMap(Collection::stream)
                .map(PriceQuantity::getQuantity)
                .flatMap(Collection::stream)
                .map(FieldWithMetaNonNegativeQuantitySchedule::getValue)
                .map(NonNegativeQuantitySchedule::getUnit)
                .filter(unit -> unit.getCurrency() != null)
                .findFirst()
                .orElse(null);
        return TransferInstruction.builder()
                .addTransferState(TransferState.builder()
                        .setTransfer(Transfer.builder()
                                .setTransferExpression(TransferExpression.builder().setPriceTransfer(feeType))
                                .setPayerReceiver(PartyReferencePayerReceiver.builder()
                                        .setPayerPartyReference(counterparties.get(0).getPartyReference())
                                        .setReceiverPartyReference(counterparties.get(1).getPartyReference()))
                                .setQuantity(NonNegativeQuantity.builder()
                                        .setValue(BigDecimal.valueOf(2000.00))
                                        .setUnit(currencyUnitType))
                                .setSettlementDate(AdjustableOrAdjustedOrRelativeDate.builder()
                                        .setAdjustedDateValue(trade.getTradeDate().getValue()))));
    }

    @Test
    void validateCompressionFuncInputJson() throws IOException {
        List<Instruction> instructions = new ArrayList<>();
        QuantityChangeInstruction terminateInstructions = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                .addChange(PriceQuantity.builder()
                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                .setValue(NonNegativeQuantitySchedule.builder()
                                        .setValue(BigDecimal.valueOf(0.0))
                                        .setUnit(UnitType.builder()
                                                .setCurrency(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                                                        .setValue("USD"))))));


        instructions.add(Instruction.builder()
                .setBeforeValue(getProposedEventInstructionBefore("result-json-files/native-cdm-events/Example-07-Submission-1.json"))
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(terminateInstructions))
                .build());

        instructions.add(Instruction.builder()
                .setBeforeValue(getProposedEventInstructionBefore("result-json-files/native-cdm-events/Example-07-Submission-2.json"))
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(terminateInstructions))
                .build());

        instructions.add(Instruction.builder()
                .setBeforeValue(getProposedEventInstructionBefore("result-json-files/native-cdm-events/Example-07-Submission-3.json"))
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(terminateInstructions))
                .build());

        instructions.add(Instruction.builder()
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setExecution(getCompressionExecutionInstructionInputJson()))
                .build());

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                instructions,
                EventIntentEnum.COMPRESSION,
                Date.of(2018, 4, 3),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/compression/compression-func-input.json", actual);
    }

    private ExecutionInstruction getCompressionExecutionInstructionInputJson() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder =
                ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/rates/USD-Vanilla-swap.json").toBuilder();

        Trade.TradeBuilder tradeBuilder = tradeStateBuilder.getTrade();

        TradeLot.TradeLotBuilder tradeLotBuilder = tradeBuilder.getTradeLot().get(0);
        tradeLotBuilder
                .getPriceQuantity().get(0)
                .getQuantity().get(0)
                .getValue().setValue(BigDecimal.valueOf(16000.00));
        tradeLotBuilder
                .getPriceQuantity().get(1)
                .getQuantity().get(0)
                .getValue().setValue(BigDecimal.valueOf(16000.00));

        List<? extends InterestRatePayout.InterestRatePayoutBuilder> interestRatePayoutBuilders =
                tradeBuilder
                        .getProduct()
                        .getEconomicTerms()
                        .getPayout()
                        .stream()
                        .map(Payout.PayoutBuilder::getInterestRatePayout)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        Date effectiveDate = Date.of(2018, 4, 3);
        Date terminationDate = Date.of(2026, 2, 8);

        setDates(interestRatePayoutBuilders.get(0), effectiveDate, terminationDate);
        setDates(interestRatePayoutBuilders.get(1), effectiveDate, terminationDate);

        tradeBuilder
                .getParty().get(0)
                .getPartyId().get(0)
                .setIdentifierValue("LEI1RPT0001");

        tradeBuilder
                .getParty().get(1)
                .getPartyId().get(0)
                .setIdentifierValue("LEI2CP0002");

        TradeIdentifier tradeIdentifier = TradeIdentifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifier(FieldWithMetaString.builder()
                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/uti"))
                                .setValue("LEI1RPT0003EFG")))
                .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                .setIssuer(FieldWithMetaString.builder()
                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso17442"))
                        .setValue("LEI1RPT0001"));

        tradeBuilder
                .setTradeIdentifier(Lists.newArrayList(tradeIdentifier.build()))
                .setTradeDateValue(effectiveDate);

        reKey(tradeStateBuilder);

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

    private TradeState getProposedEventInstructionBefore(String resourceName) throws IOException {
        return ResourcesUtils.getObject(WorkflowStep.class, resourceName).getProposedEvent()
                .getInstruction()
                .get(0)
                .getBefore()
                .getValue();
    }

    @Test
    void validateFullNovationFuncInputJson() throws IOException {
        SplitInstruction splitInstruction = SplitInstruction.builder()
                .addBreakdown(PrimitiveInstruction.builder()
                        .setPartyChange(PartyChangeInstruction.builder()
                                .setCounterparty(Counterparty.builder()
                                        .setPartyReferenceValue(Party.builder()
                                                .setMeta(MetaFields.builder().setExternalKey("party3"))
                                                .setNameValue("Bank Z")
                                                .addPartyId(PartyIdentifier.builder()
                                                        .setIdentifierType(PartyIdentifierTypeEnum.LEI)
                                                        .setIdentifier(FieldWithMetaString.builder().setValue("LEI3RPT0003").setMeta(MetaFields.builder()
                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso17442")))))
                                        .setRole(CounterpartyRoleEnum.PARTY_1))
                                .setTradeId(Lists.newArrayList(TradeIdentifier.builder()
                                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                                .setIdentifier(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                        .setValue("LEI3RPT0003CCC")))
                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                                        .setIssuer(FieldWithMetaString.builder()
                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                                .setValue("LEI3RPT0003"))))))
                .addBreakdown(PrimitiveInstruction.builder()
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                                .setValue(NonNegativeQuantitySchedule.builder()
                                                        .setValue(BigDecimal.valueOf(0.0))
                                                        .setUnit(UnitType.builder()
                                                                .setCurrency(FieldWithMetaString.builder()
                                                                        .setMeta(MetaFields.builder()
                                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                                                                        .setValue("USD"))))))));

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(getProposedEventInstructionBefore("result-json-files/native-cdm-events/Example-04-Submission-1.json"))
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setSplit(splitInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                EventIntentEnum.NOVATION,
                Date.of(2018, 4, 3),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/novation/full-novation-func-input.json", actual);
    }

    @Test
    void validatePartialNovationFuncInputJson() throws IOException {
        SplitInstruction splitInstruction = SplitInstruction.builder()
                .addBreakdown(PrimitiveInstruction.builder()
                        .setPartyChange(PartyChangeInstruction.builder()
                                .setCounterparty(Counterparty.builder()
                                        .setPartyReferenceValue(Party.builder()
                                                .setMeta(MetaFields.builder().setExternalKey("party3"))
                                                .setNameValue("Bank Z")
                                                .addPartyId(PartyIdentifier.builder()
                                                        .setIdentifierType(PartyIdentifierTypeEnum.LEI)
                                                        .setIdentifier(FieldWithMetaString.builder().setValue("LEI3RPT0003")
                                                                .setMeta(MetaFields.builder()
                                                                        .setScheme("http://www.fpml.org/coding-scheme/external/iso17442")))))
                                        .setRole(CounterpartyRoleEnum.PARTY_1))
                                .setTradeId(Lists.newArrayList(TradeIdentifier.builder()
                                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                                .setIdentifier(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                        .setValue("LEI3RPT0003DDDD")))
                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                                        .setIssuer(FieldWithMetaString.builder()
                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                                .setValue("LEI3RPT0003")))))
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                                .setValue(NonNegativeQuantitySchedule.builder()
                                                        .setValue(BigDecimal.valueOf(5000.0))
                                                        .setUnit(UnitType.builder()
                                                                .setCurrency(FieldWithMetaString.builder()
                                                                        .setMeta(MetaFields.builder()
                                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                                                                        .setValue("USD"))))))))
                .addBreakdown(PrimitiveInstruction.builder()
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                                .setValue(NonNegativeQuantitySchedule.builder()
                                                        .setValue(BigDecimal.valueOf(8000.0))
                                                        .setUnit(UnitType.builder()
                                                                .setCurrency(FieldWithMetaString.builder()
                                                                        .setMeta(MetaFields.builder()
                                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                                                                        .setValue("USD"))))))));

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(getProposedEventInstructionBefore("result-json-files/native-cdm-events/Example-05-Submission-1.json"))
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setSplit(splitInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                EventIntentEnum.NOVATION,
                Date.of(2018, 4, 4),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/novation/partial-novation-func-input.json", actual);
    }

    @Test
    void validateClearingFuncInputJson() throws IOException {
        SplitInstruction splitInstruction = SplitInstruction.builder()
                .addBreakdown(PrimitiveInstruction.builder()
                        .setPartyChange(PartyChangeInstruction.builder()
                                .setCounterparty(Counterparty.builder()
                                        .setPartyReferenceValue(Party.builder()
                                                .setMeta(MetaFields.builder().setExternalKey("clearing-svc"))
                                                .setNameValue("ClearItAll")
                                                .addPartyId(PartyIdentifier.builder()
                                                        .setIdentifier(FieldWithMetaString.builder()
                                                                .setMeta(MetaFields.builder()
                                                                        .setScheme("http://www.fpml.org/coding-scheme/external/iso17442"))
                                                                .setValue("LEI1DCO"))
                                                        .setIdentifierType(PartyIdentifierTypeEnum.LEI))
                                                .build())
                                        .setRole(CounterpartyRoleEnum.PARTY_2))
                                .setTradeId(Lists.newArrayList(TradeIdentifier.builder()
                                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                                .setIdentifier(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                        .setValue("LEI1DCO01BETA")))
                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                                        .setIssuer(FieldWithMetaString.builder()
                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                                .setValue("LEI1DCO"))))))
                .addBreakdown(PrimitiveInstruction.builder()
                        .setPartyChange(PartyChangeInstruction.builder()
                                .setCounterparty(Counterparty.builder()
                                        .setPartyReferenceValue(Party.builder()
                                                .setMeta(MetaFields.builder().setExternalKey("clearing-svc"))
                                                .setNameValue("ClearItAll")
                                                .addPartyId(PartyIdentifier.builder()
                                                        .setIdentifier(FieldWithMetaString.builder()
                                                                .setMeta(MetaFields.builder()
                                                                        .setScheme("http://www.fpml.org/coding-scheme/external/iso17442"))
                                                                .setValue("LEI1DCO"))
                                                        .setIdentifierType(PartyIdentifierTypeEnum.LEI))
                                                .build())
                                        .setRole(CounterpartyRoleEnum.PARTY_1))
                                .setTradeId(Lists.newArrayList(TradeIdentifier.builder()
                                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                                .setIdentifier(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                        .setValue("LEI1DCO01GAMMA")))
                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                                        .setIssuer(FieldWithMetaString.builder()
                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                                .setValue("LEI1DCO"))))))
                .addBreakdown(PrimitiveInstruction.builder()
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                                .setValue(NonNegativeQuantitySchedule.builder()
                                                        .setValue(BigDecimal.valueOf(0.0))
                                                        .setUnit(UnitType.builder()
                                                                .setCurrency(FieldWithMetaString.builder()
                                                                        .setMeta(MetaFields.builder()
                                                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                                                                        .setValue("USD"))))))));

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(getProposedEventInstructionBefore("result-json-files/native-cdm-events/Example-06-Submission-1.json"))
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setSplit(splitInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                EventIntentEnum.CLEARING,
                Date.of(2018, 4, 1),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/clearing/clearing-func-input.json", actual);
    }

    @Test
    void validateAllocationFuncInputJson() throws IOException {
        SplitInstruction splitInstruction = SplitInstruction.builder()
                // Allocated to Fund 2
                .addBreakdown(PrimitiveInstruction.builder()
                        .setPartyChange(PartyChangeInstruction.builder()
                                .setCounterparty(Counterparty.builder()
                                        .setPartyReferenceValue(Party.builder()
                                                .setMeta(MetaFields.builder().setExternalKey("party3"))
                                                .setNameValue("Fund 2")
                                                .addPartyId(PartyIdentifier.builder()
                                                        .setIdentifierType(PartyIdentifierTypeEnum.LEI)
                                                        .setIdentifier(FieldWithMetaString.builder().setValue("LEI2CP00A1")
                                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso17442")))))
                                        .setRole(CounterpartyRoleEnum.PARTY_2))
                                .setTradeId(Lists.newArrayList(TradeIdentifier.builder()
                                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                                .setIdentifier(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                        .setValue("LEI1RPT001POST1")))
                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                                        .setIssuer(FieldWithMetaString.builder()
                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                                .setValue("LEI1RPT001")))))
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                                .setValue(NonNegativeQuantitySchedule.builder()
                                                        .setValue(BigDecimal.valueOf(7000.0))
                                                        .setUnit(UnitType.builder()
                                                                .setCurrencyValue("EUR")))))))
                // Allocated to Fund 3
                .addBreakdown(PrimitiveInstruction.builder()
                        .setPartyChange(PartyChangeInstruction.builder()
                                .setCounterparty(Counterparty.builder()
                                        .setPartyReferenceValue(Party.builder()
                                                .setMeta(MetaFields.builder().setExternalKey("party4"))
                                                .setNameValue("Fund 3")
                                                .addPartyId(PartyIdentifier.builder()
                                                        .setIdentifierType(PartyIdentifierTypeEnum.LEI)
                                                        .setIdentifier(FieldWithMetaString.builder().setValue("LEI3CP00A2")
                                                                .setMeta(MetaFields.builder()
                                                                        .setScheme("http://www.fpml.org/coding-scheme/external/iso17442")))))
                                        .setRole(CounterpartyRoleEnum.PARTY_2))
                                .setTradeId(Lists.newArrayList(TradeIdentifier.builder()
                                        .addAssignedIdentifier(AssignedIdentifier.builder()
                                                .setIdentifier(FieldWithMetaString.builder()
                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                        .setValue("LEI1RPT001POST2")))
                                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                                        .setIssuer(FieldWithMetaString.builder()
                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                                .setValue("LEI1RPT001")))))
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                                .setValue(NonNegativeQuantitySchedule.builder()
                                                        .setValue(BigDecimal.valueOf(3000.0))
                                                        .setUnit(UnitType.builder()
                                                                .setCurrencyValue("EUR")))))))
                // Close original trade
                .addBreakdown(PrimitiveInstruction.builder()
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                                .setValue(NonNegativeQuantitySchedule.builder()
                                                        .setValue(BigDecimal.valueOf(0.0))
                                                        .setUnit(UnitType.builder()
                                                                .setCurrencyValue("EUR")))))));

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(getProposedEventInstructionBefore("result-json-files/native-cdm-events/Example-11-Submission-1.json"))
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setSplit(splitInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                EventIntentEnum.ALLOCATION,
                Date.of(2018, 4, 1),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/allocation/allocation-func-input.json", actual);
    }

    ObservationEvent getCreditEventObservationEvent() {
        ObservationEvent observationEvent = ObservationEvent.builder()
                .setCreditEvent(CreditEvent.builder()
                        .setCreditEventType(CreditEventTypeEnum.BANKRUPTCY)
                        .setEventDeterminationDate(Date.of(2022, 2, 4))
                        .setAuctionDate(Date.of(2022, 3, 3))
                        .setReferenceInformation(ReferenceInformation.builder()
                                .setReferenceEntity(
                                        LegalEntity.builder()
                                                .setEntityId(Collections.singletonList(FieldWithMetaString.builder()
                                                        .setValue("UE2136O97NLB5BYP9H04")))
                                                .setName(FieldWithMetaString.builder()
                                                        .setValue("McDonald's Corporation")))
                                .setNoReferenceObligation(true))

                );

        return observationEvent;
    }

    @Test
    void validateCreditEventFuncInputJson() throws IOException {

        ObservationInstruction observationInstruction = ObservationInstruction.builder()
                .setObservationEvent(getCreditEventObservationEvent());

        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/credit/cdindex-ex01-cdx-uti.json");

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setObservation(observationInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                null,
                Date.of(2022, 2, 4),
                Date.of(2022, 2, 4));

        assertJsonEquals("cdm-sample-files/functions/business-event/credit-event/credit-event-func-input.json", actual);
    }

    @Test
    void validateCreditEventWithObservationFuncInputJson() throws IOException {

        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/credit/cdindex-ex01-cdx-uti.json");
        TradeState tradeStateWithObs = tradeState.toBuilder().setObservationHistory(Collections.singletonList(getCreditEventObservationEvent())).build();

        ObservationEvent observationEvent = ObservationEvent.builder()
                .setCreditEvent(CreditEvent.builder()
                        .setCreditEventType(CreditEventTypeEnum.FAILURE_TO_PAY)
                        .setEventDeterminationDate(Date.of(2023, 2, 4))
                        .setAuctionDate(Date.of(2023, 3, 3))
                        .setReferenceInformation(ReferenceInformation.builder()
                                .setReferenceEntity(LegalEntity.builder()
                                        .setEntityId(Collections.singletonList(FieldWithMetaString.builder()
                                                .setValue("UE2136O97NLB5BYP9H04")))
                                        .setName(FieldWithMetaString.builder()
                                                .setValue("McDonald's Corporation")))
                                .setNoReferenceObligation(true))

                );

        ObservationInstruction observationInstruction = ObservationInstruction.builder()
                .setObservationEvent(observationEvent);
        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(tradeStateWithObs)
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setObservation(observationInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                null,
                Date.of(2023, 2, 2),
                Date.of(2023, 2, 2));

        assertJsonEquals("cdm-sample-files/functions/business-event/credit-event/credit-event-obs-func-input.json", actual);
    }

    private ObservationEvent getCorporateActionObservationEvent() {
        ObservationEvent observationEvent = ObservationEvent.builder()
                .setCorporateAction(CorporateAction.builder()
                                .setCorporateActionType(CorporateActionTypeEnum.STOCK_SPLIT)
                                .setExDate(Date.of(2009, 2, 1))
                                .setPayDate(Date.of(2009, 2, 1))
//                        .setUnderlier(Underlier.builder()
//                                .setObservableValue(Observable.builder()
//                                        .setIndex(Index.builder()
//                                                .setEquityIndex(EquityIndex.builder()
//                                                        .setAssetClass(AssetClassEnum.EQUITY)
//                                                        .setNameValue("VOLKSWAGEN AG VZO O.N.")))))
                );
        return observationEvent;
    }

    @Test
    void validateCorporateActionFuncInputJson() throws IOException {

        ObservationInstruction observationInstruction = ObservationInstruction.builder()
                .setObservationEvent(getCorporateActionObservationEvent());

        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex12-on-european-index-underlyer-short-form.json");

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setObservation(observationInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                null,
                Date.of(2009, 2, 1),
                Date.of(2009, 2, 1));

        assertJsonEquals("cdm-sample-files/functions/business-event/corporate-actions/corporate-actions-func-input.json", actual);
    }

    @Test
    void validateCorporateActionWithObservationFuncInputJson() throws IOException {

        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex12-on-european-index-underlyer-short-form.json");
        TradeState tradeStateWithObs = tradeState.toBuilder().setObservationHistory(Collections.singletonList(getCorporateActionObservationEvent())).build();

        ObservationEvent observationEvent = ObservationEvent.builder()
                .setCorporateAction(CorporateAction.builder()
                                .setCorporateActionType(CorporateActionTypeEnum.CASH_DIVIDEND)
                                .setExDate(Date.of(2009, 2, 13))
                                .setPayDate(Date.of(2009, 2, 13))
//                        .setUnderlier(Underlier.builder()
//                                .setObservableValue(Observable.builder()
//                                        .setIndex(Index.builder()
//                                                .setEquityIndex(EquityIndex.builder()
//                                                        .setAssetClass(AssetClassEnum.EQUITY)
//                                                        .setNameValue("VOLKSWAGEN AG VZO O.N.")))))
                );

        ObservationInstruction observationInstruction = ObservationInstruction.builder()
                .setObservationEvent(observationEvent);
        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(tradeStateWithObs)
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setObservation(observationInstruction));

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                null,
                Date.of(2009, 2, 13),
                Date.of(2009, 2, 13));


        assertJsonEquals("cdm-sample-files/functions/business-event/corporate-actions/corporate-actions-obs-func-input.json", actual);
    }


    @Test
    void validateExerciseSwaptionFullPhysicalInputJson() throws IOException {
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/rates/ird-ex09-euro-swaption-explicit-physical-exercise.json");

        ExerciseInstruction.ExerciseInstructionBuilder exerciseInstructionBuilder = ExerciseInstruction.builder();
        exerciseInstructionBuilder.getOrCreateExerciseQuantity()
                .setQuantityChange(createQuantityChangeInstruction(
                        UnitType.builder().setCurrencyValue("EUR"),
                        BigDecimal.ZERO
                ));


        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setExercise(exerciseInstructionBuilder)
                );

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                EventIntentEnum.OPTION_EXERCISE,
                Date.of(2001, 8, 28),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/exercise/exercise-swaption-full-physical-func-input.json", actual);
    }

    @Test
    void validateExerciseCashSettledInputJson() throws IOException {
        String example8Submission1 = "result-json-files/native-cdm-events/Example-08-Submission-1.json";
        TradeState afterTradeState = getProposedEventInstructionBefore(example8Submission1);

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeInstructionBuilder = createQuantityChangeInstruction(UnitType.builder().setCurrencyValue("EUR").build(), BigDecimal.ZERO);

        TransferInstruction.TransferInstructionBuilder transferInstructionBuilder = TransferInstruction.builder();

        Transfer.TransferBuilder transferBuilder = transferInstructionBuilder
                .getOrCreateTransferState(0)
                .getOrCreateTransfer();

        transferBuilder.getOrCreatePayerReceiver()
                .setPayerPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party1").build())
                .setReceiverPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party2").build());

        transferBuilder.getOrCreateQuantity()
                .setValue(BigDecimal.valueOf(2000))
                .setUnit(UnitType.builder()
                        .setCurrency(FieldWithMetaString.builder()
                                .setValue("EUR")
                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso4217").build())
                                .build())
                        .build());

        transferBuilder.getOrCreateSettlementDate()
                .setAdjustedDateValue(Date.of(2019, 4, 3));

        transferBuilder.getOrCreateTransferExpression()
                .getOrCreateScheduledTransfer()
                .setTransferType(ScheduledTransferEnum.EXERCISE);

        Instruction.InstructionBuilder instructions = Instruction.builder()
                .setBeforeValue(afterTradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstructionBuilder)
                        .setTransfer(transferInstructionBuilder)
                );

        reKey(instructions);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructions.build()),
                EventIntentEnum.OPTION_EXERCISE,
                Date.of(2019, 4, 1),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/exercise/exercise-cash-settled-func-input.json", actual);
    }

    @Test
    void validateExercisePartialExerciseInputJson() throws IOException {
        String example9Submission1 = "result-json-files/native-cdm-events/Example-09-Submission-1.json";
        TradeState afterTradeState = getProposedEventInstructionBefore(example9Submission1);

        Date tradeDate = Date.of(2019, 4, 1);

        ExerciseInstruction.ExerciseInstructionBuilder exerciseInstructionBuilder = ExerciseInstruction.builder();

        exerciseInstructionBuilder.getOrCreateExerciseQuantity()
                .setQuantityChange(
                        createQuantityChangeInstruction(
                                UnitType.builder().setCurrencyValue("EUR"),
                                BigDecimal.valueOf(11000))
                );

        exerciseInstructionBuilder.addReplacementTradeIdentifier(
                TradeIdentifier.builder()
                        .addAssignedIdentifier(
                                AssignedIdentifier.builder()
                                        .setIdentifier(FieldWithMetaString.builder()
                                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/unique-transaction-identifier"))
                                                .setValue("LEI1RPT0001IIIIEx")
                                        )
                        )
                        .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                        .setIssuer(FieldWithMetaString.builder()
                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/cftc/issuer-identifier"))
                                .setValue("LEI1RPT0001")
                        )
        );

        Instruction.InstructionBuilder instruction = Instruction.builder()
                .setBeforeValue(afterTradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setExercise(exerciseInstructionBuilder)
                );

        reKey(instruction);


        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instruction.build()),
                EventIntentEnum.OPTION_EXERCISE,
                tradeDate,
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/exercise/exercise-partial-exercise-func-input.json", actual);
    }

    @Test
    void validateExerciseCancellableOptionInputJson() throws IOException {
        String example10Submission1 = "result-json-files/native-cdm-events/Example-10-Submission-1.json";
        TradeState afterTradeState = getProposedEventInstructionBefore(example10Submission1);

        Date tradeDate = Date.of(2019, 4, 1);

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeInstructionBuilder = createQuantityChangeInstruction(UnitType.builder().setCurrencyValue("EUR"), BigDecimal.valueOf(12000));

        TransferInstruction.TransferInstructionBuilder transferInstructionBuilder = TransferInstruction.builder();

        Transfer.TransferBuilder transferBuilder = transferInstructionBuilder
                .getOrCreateTransferState(0)
                .getOrCreateTransfer();

        transferBuilder.getOrCreatePayerReceiver()
                .setPayerPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party1"))
                .setReceiverPartyReference(ReferenceWithMetaParty.builder().setExternalReference("party2"));

        transferBuilder.getOrCreateQuantity()
                .setValue(BigDecimal.valueOf(2000))
                .setUnit(UnitType.builder()
                        .setCurrency(FieldWithMetaString.builder()
                                .setValue("EUR")
                                .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))
                        )
                );

        transferBuilder.setSettlementDate(AdjustableOrAdjustedOrRelativeDate.builder()
                .setAdjustedDateValue(Date.of(2019, 4, 3))
        );

        transferBuilder.getOrCreateTransferExpression()
                .getOrCreateScheduledTransfer()
                .setTransferType(ScheduledTransferEnum.EXERCISE);

        Instruction.InstructionBuilder instruction = Instruction.builder()
                .setBeforeValue(afterTradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setQuantityChange(quantityChangeInstructionBuilder)
                        .setTransfer(transferInstructionBuilder)
                );

        reKey(instruction);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instruction.build()),
                EventIntentEnum.OPTION_EXERCISE,
                tradeDate,
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/exercise/exercise-cancellable-option-func-input.json", actual);
    }


    private QuantityChangeInstruction.QuantityChangeInstructionBuilder createQuantityChangeInstruction(UnitType unitOfAmount, BigDecimal amount) {
        return QuantityChangeInstruction.builder()
                .addChange(PriceQuantity.builder()
                        .addQuantityValue(NonNegativeQuantitySchedule.builder()
                                .setValue(amount)
                                .setUnit(unitOfAmount)))
                .setDirection(QuantityChangeDirectionEnum.REPLACE);
    }

    /**
     * Use record-ex01-vanilla-swap.json sample and modify it to look exactly like CFTC example 3 (used in regs termination example)
     */

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
                .getProduct()
                .getEconomicTerms()
                .getPayout()
                .stream()
                .map(Payout.PayoutBuilder::getInterestRatePayout)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        interestRatePayouts.stream()
                .filter(payout -> payout.getRateSpecification().getFloatingRateSpecification() != null)
                .findFirst()
                .ifPresent(floatingLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = floatingLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(Date.of(2014, 4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(Date.of(2025, 4, 1));
                });
        interestRatePayouts.stream()
                .filter(payout -> payout.getRateSpecification().getFixedRateSpecification() != null)
                .findFirst()
                .ifPresent(fixedLeg -> {
                    CalculationPeriodDates.CalculationPeriodDatesBuilder calculationPeriodDates = fixedLeg.getCalculationPeriodDates();
                    calculationPeriodDates.getEffectiveDate().getAdjustableDate().setUnadjustedDate(Date.of(2018, 4, 3));
                    calculationPeriodDates.getTerminationDate().getAdjustableDate().setUnadjustedDate(Date.of(2025, 4, 1));
                });
        // quantity
        tradeStateBuilder.getTrade().getTradeLot().stream()
                .map(TradeLot.TradeLotBuilder::getPriceQuantity)
                .flatMap(Collection::stream)
                .map(PriceQuantity.PriceQuantityBuilder::getQuantity)
                .flatMap(Collection::stream)
                .map(FieldWithMetaNonNegativeQuantitySchedule.FieldWithMetaNonNegativeQuantityScheduleBuilder::getValue)
                .forEach(quantity -> quantity.setValue(new BigDecimal(10000)));
        // trade id
        tradeStateBuilder.getTrade().getTradeIdentifier().get(0).getAssignedIdentifier().get(0).setIdentifierValue("LEI1RPT0001KKKK");
        // trade date
        tradeStateBuilder.getTrade().setTradeDateValue(Date.of(2018, 4, 1));
        return tradeStateBuilder;
    }

    /**
     * eqs-ex01-single-underlyer-execution-long-form.json with LOT-1 trade lot identifier
     */

    private TradeState getQuantityChangeEquitySwapTradeState() throws IOException {
        TradeState.TradeStateBuilder tradeStateBuilder = ResourcesUtils.getObject(TradeState.class, "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json").toBuilder();
        TradeLot.TradeLotBuilder tradeLotBuilder = tradeStateBuilder.getTrade().getTradeLot().get(0);
        tradeLotBuilder.addLotIdentifier(Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifierValue("LOT-1")));
        return tradeStateBuilder.build();
    }

    @Test
    void validateIndexTransitionVanillaSwapFuncInputJson() throws IOException {
        String tradeStatePath = "result-json-files/fpml-5-10/products/rates/ird-ex05-long-stub-swap-uti.json";
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        Instruction instructionBuilder = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setIndexTransition(IndexTransitionInstruction.builder()
                                .addPriceQuantity(PriceQuantity.builder()
                                        .setObservableValue(Observable.builder()
                                                .setIndex(Index.builder()
                                                        .setInterestRateIndex(
                                                                FieldWithMetaInterestRateIndex.builder()
                                                                        .setValue(InterestRateIndex.builder()
                                                                                .setFloatingRateIndex(FloatingRateIndex.builder()
                                                                                        .setFloatingRateIndexValue(FloatingRateIndexEnum.EUR_EURIBOR_REUTERS)
                                                                                        .setIndexTenor(Period.builder()
                                                                                                .setPeriod(PeriodEnum.M)
                                                                                                .setPeriodMultiplier(6)))))))
                                        .addPriceValue(Price.builder()
                                                .setValue(BigDecimal.valueOf(0.003))
                                                .setUnit(UnitType.builder().setCurrencyValue("EUR"))
                                                .setPerUnitOf(UnitType.builder().setCurrencyValue("EUR"))
                                                .setPriceType(PriceTypeEnum.INTEREST_RATE)
                                                .setArithmeticOperator(ArithmeticOperationEnum.ADD)))
                                .setEffectiveDate(Date.of(2000, 10, 3))))
                .prune();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructionBuilder.build()),
                EventIntentEnum.INDEX_TRANSITION,
                Date.of(2000, 10, 1),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/index-transition/index-transition-vanilla-swap-func-input.json", actual);
    }

    @Test
    void validateIndexTransitionXccySwapFuncInputJson() throws IOException {
        String tradeStatePath = "result-json-files/fpml-5-10/products/rates/cdm-xccy-swap-after-usi-uti.json";
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        Instruction instructionBuilder = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setIndexTransition(IndexTransitionInstruction.builder()
                                .addPriceQuantity(PriceQuantity.builder()
                                        .setObservableValue(Observable.builder()
                                                .setIndex(Index.builder()
                                                        .setInterestRateIndex(
                                                                FieldWithMetaInterestRateIndex.builder()
                                                                        .setValue(InterestRateIndex.builder()
                                                                                .setFloatingRateIndex(FloatingRateIndex.builder()
                                                                                        .setFloatingRateIndex(
                                                                                                FieldWithMetaFloatingRateIndexEnum.builder()
                                                                                                        .setValue(FloatingRateIndexEnum.USD_LIBOR_ISDA)
                                                                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/floating-rate-index")))
                                                                                        .setIndexTenor(Period.builder()
                                                                                                .setPeriod(PeriodEnum.M)
                                                                                                .setPeriodMultiplier(6)))))))

                                        .addPriceValue(Price.builder()
                                                .setValue(BigDecimal.valueOf(0.002))
                                                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                                                .setPerUnitOf(UnitType.builder().setCurrencyValue("USD"))
                                                .setPriceType(PriceTypeEnum.INTEREST_RATE)
                                                .setArithmeticOperator(ArithmeticOperationEnum.ADD)))
                                .addPriceQuantity(PriceQuantity.builder()
                                        .setObservableValue(Observable.builder()
                                                .setIndex(Index.builder()
                                                        .setInterestRateIndex(
                                                                FieldWithMetaInterestRateIndex.builder()
                                                                        .setValue(InterestRateIndex.builder()
                                                                                .setFloatingRateIndex(FloatingRateIndex.builder()
                                                                                        .setFloatingRateIndex(
                                                                                                FieldWithMetaFloatingRateIndexEnum.builder()
                                                                                                        .setValue(FloatingRateIndexEnum.EUR_EURIBOR_REUTERS)
                                                                                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/floating-rate-index")))
                                                                                        .setIndexTenor(Period.builder()
                                                                                                .setPeriod(PeriodEnum.M)
                                                                                                .setPeriodMultiplier(3)))))))
                                        .addPriceValue(Price.builder()
                                                .setValue(BigDecimal.valueOf(0.001))
                                                .setUnit(UnitType.builder().setCurrencyValue("EUR"))
                                                .setPerUnitOf(UnitType.builder().setCurrencyValue("EUR"))
                                                .setPriceType(PriceTypeEnum.INTEREST_RATE)
                                                .setArithmeticOperator(ArithmeticOperationEnum.ADD)))
                                .setEffectiveDate(Date.of(2018, 6, 19))))
                .prune();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructionBuilder.build()),
                EventIntentEnum.INDEX_TRANSITION,
                Date.of(2018, 6, 17),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/index-transition/index-transition-xccy-swap-func-input.json", actual);
    }

    @Test
    void validateStockSplitFuncInputJson() throws IOException {
        String tradeStatePath = "result-json-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.json";
        TradeState tradeState = ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        Instruction instructionBuilder = Instruction.builder()
                .setBeforeValue(tradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder()
                        .setStockSplit(StockSplitInstruction.builder()
                                .setAdjustmentRatio(BigDecimal.valueOf(2.0))
                                .setEffectiveDate(Date.of(2001, 11, 3))))
                .prune();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instructionBuilder.build()),
                EventIntentEnum.CORPORATE_ACTION_ADJUSTMENT,
                Date.of(2001, 11, 1),
                null);

        assertJsonEquals("cdm-sample-files/functions/business-event/stock-split/stock-split-equity-swap-func-input.json", actual);
    }

    @Test
    void validateCorrectionWorkflowFuncInputJson() throws IOException {
        String tradeStatePath = "result-json-files/fpml-5-10/products/rates/ird-ex01-vanilla-swap-versioned.json";
        Date eventDate = Date.of(1994, 12, 12);

        // New WorkflowStep containing Execution (with incorrect quantity)

        TradeState.TradeStateBuilder tradeStateWithIncorrectQuantity =
                ResourcesUtils.getObject(TradeState.class, tradeStatePath).toBuilder();
        // Update quantity to an incorrect value (which is corrected later)
        tradeStateWithIncorrectQuantity.getTrade()
                .getTradeLot()
                .stream()
                .map(TradeLot.TradeLotBuilder::getPriceQuantity)
                .flatMap(Collection::stream)
                .map(PriceQuantity.PriceQuantityBuilder::getQuantity)
                .flatMap(Collection::stream)
                .forEach(q -> q.getOrCreateValue().setValue(BigDecimal.valueOf(99_999)));

        WorkflowStep newExecutionWorkflowStep =
                getExecutionWorkflowStep(tradeStateWithIncorrectQuantity,
                        eventDate,
                        LocalTime.of(18, 12),
                        "msg-1",
                        "id-1",
                        null,
                        ActionEnum.NEW);

        // Correct WorkflowStep containing Execution (with corrected quantity)

        TradeState tradeStateWithCorrectedQuantity =
                ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        WorkflowStep correctExecutionWorkflowStep =
                getExecutionWorkflowStep(tradeStateWithCorrectedQuantity,
                        eventDate,
                        LocalTime.of(19, 13),
                        "msg-2",
                        "id-2",
                        newExecutionWorkflowStep,
                        ActionEnum.CORRECT);

        List<WorkflowStep> steps = new ArrayList<>();
        steps.add(newExecutionWorkflowStep);
        steps.add(correctExecutionWorkflowStep);

        CreateWorkflowInput actual = new CreateWorkflowInput(steps);

        assertJsonEquals("cdm-sample-files/functions/workflow-step/correction/correction-func-input.json", actual);
    }

    @Test
    void validateCancellationWorkflowFuncInputJson() throws IOException {
        String tradeStatePath = "result-json-files/fpml-5-10/products/rates/ird-ex01-vanilla-swap-versioned.json";
        Date eventDate = Date.of(1994, 12, 12);

        // New WorkflowStep containing Execution (with incorrect quantity)

        TradeState.TradeStateBuilder tradeStateWithIncorrectQuantity =
                ResourcesUtils.getObject(TradeState.class, tradeStatePath).toBuilder();
        // Update quantity to an incorrect value (which is corrected later)
        tradeStateWithIncorrectQuantity.getTrade()
                .getTradeLot()
                .stream()
                .map(TradeLot.TradeLotBuilder::getPriceQuantity)
                .flatMap(Collection::stream)
                .map(PriceQuantity.PriceQuantityBuilder::getQuantity)
                .flatMap(Collection::stream)
                .forEach(q -> q.getOrCreateValue().setValue(BigDecimal.valueOf(99_999)));

        WorkflowStep initialExecutionWorkflowStep =
                getExecutionWorkflowStep(tradeStateWithIncorrectQuantity,
                        eventDate,
                        LocalTime.of(18, 12),
                        "msg-1",
                        "id-1",
                        null,
                        ActionEnum.NEW);

        // Cancel WorkflowStep

        CreateWorkflowStepInput cancelWorkflowStepInput =
                new CreateWorkflowStepInput(MessageInformation.builder().setMessageIdValue("msg-2"),
                        getEventTimestamp(eventDate, LocalTime.of(18, 55)),
                        getIdentifier("id-2"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        initialExecutionWorkflowStep,
                        ActionEnum.CANCEL,
                        null);
        WorkflowStep cancelWorkflowStep = runCreateWorkflowStepFunc(cancelWorkflowStepInput);

        // New WorkflowStep containing Execution (with corrected quantity)

        TradeState tradeStateWithCorrectedQuantity =
                ResourcesUtils.getObject(TradeState.class, tradeStatePath);

        WorkflowStep newExecutionWorkflowStep =
                getExecutionWorkflowStep(tradeStateWithCorrectedQuantity,
                        eventDate,
                        LocalTime.of(19, 13),
                        "msg-3",
                        "id-3",
                        null,
                        ActionEnum.NEW);

        List<WorkflowStep> steps = new ArrayList<>();
        steps.add(initialExecutionWorkflowStep);
        steps.add(cancelWorkflowStep);
        steps.add(newExecutionWorkflowStep);

        CreateWorkflowInput actual = new CreateWorkflowInput(steps);

        assertJsonEquals("cdm-sample-files/functions/workflow-step/cancellation/cancellation-func-input.json", actual);
    }

    private WorkflowStep getExecutionWorkflowStep(TradeState tradeState,
                                                  Date eventDate,
                                                  LocalTime eventTime,
                                                  String messageId,
                                                  String eventId,
                                                  WorkflowStep previousWorkflowStep,
                                                  ActionEnum action) {
        // Create Execution BusinessEvent
        CreateBusinessEventInput executionBusinessEventInput = getExecutionFuncInputJson(tradeState.build(), eventDate);
        BusinessEvent businessEvent = runCreateBusinessEventFunc(executionBusinessEventInput);

        // Create WorkflowStep
        CreateWorkflowStepInput workflowStepInput =
                new CreateWorkflowStepInput(MessageInformation.builder().setMessageIdValue(messageId),
                        getEventTimestamp(eventDate, eventTime),
                        getIdentifier(eventId),
                        getParties(businessEvent),
                        Collections.emptyList(),
                        previousWorkflowStep,
                        action,
                        businessEvent);
        return runCreateWorkflowStepFunc(workflowStepInput);
    }

    private List<EventTimestamp> getEventTimestamp(Date date, LocalTime time) {
        return Collections.singletonList(EventTimestamp.builder()
                .setDateTime(ZonedDateTime.of(date.toLocalDate(), time, ZoneId.of("UTC")))
                .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME));
    }

    private List<Identifier> getIdentifier(String id) {
        return Collections.singletonList(Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifierValue(id)));
    }

    private List<Party> getParties(BusinessEvent businessEvent) {
        if (businessEvent == null) {
            return Collections.emptyList();
        }

        Set<? extends Party> afterTradesParties = businessEvent.getAfter().stream()
                .map(TradeState::getTrade)
                .map(Trade::getParty)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Set<? extends Party> beforeTradesParties = businessEvent.getInstruction().stream()
                .map(Instruction::getBefore)
                .filter(Objects::nonNull)
                .map(ReferenceWithMetaTradeState::getValue)
                .map(TradeState::getTrade)
                .map(Trade::getParty)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        Set<Party> distinctParties = new HashSet<>();
        distinctParties.addAll(afterTradesParties);
        distinctParties.addAll(beforeTradesParties);

        return distinctParties.stream()
                .sorted(Comparator.comparing(p ->
                        Optional.ofNullable(p.getName())
                                .map(FieldWithMetaString::getValue)
                                .orElse("")))
                .collect(Collectors.toList());
    }

    private MetaFields.MetaFieldsBuilder createKey(String s) {
        return MetaFields.builder().addKey(Key.builder().setScope("DOCUMENT").setKeyValue(s));
    }

    private void updatePartyId(TradeState.TradeStateBuilder tradeStateBuilder, String partyName, String partyId) {
        tradeStateBuilder.getTrade().getParty().stream()
                .filter(p -> p.getName().getValue().equals(partyName))
                .findFirst().ifPresent(party ->
                        party.addPartyId(PartyIdentifier.builder()
                                .setIdentifierType(PartyIdentifierTypeEnum.LEI)
                                .setIdentifier(FieldWithMetaString.builder()
                                        .setValue(partyId)
                                        .setMeta(MetaFields.builder().setScheme("http://www.fpml.org/coding-scheme/external/iso17442")
                                        ))));
    }


    private BusinessEvent runCreateBusinessEventFunc(CreateBusinessEventInput input) {
        Create_BusinessEvent func = injector.getInstance(Create_BusinessEvent.class);
        BusinessEvent.BusinessEventBuilder businessEvent =
                func.evaluate(input.getInstruction(),
                                input.getIntent(),
                                input.getEventDate(),
                                null)
                        .toBuilder();
        PostProcessor postProcessor = injector.getInstance(PostProcessor.class);
        postProcessor.postProcess(BusinessEvent.class, businessEvent);
        return businessEvent.build();
    }

    private WorkflowStep runCreateWorkflowStepFunc(CreateWorkflowStepInput input) {
        Create_WorkflowStep func = injector.getInstance(Create_WorkflowStep.class);
        WorkflowStep.WorkflowStepBuilder workflowStep =
                func.evaluate(input.getMessageInformation(),
                                input.getTimestamp(),
                                input.getEventIdentifier(),
                                input.getParty(),
                                input.getAccount(),
                                input.getPreviousWorkflowStep(),
                                input.getAction(),
                                input.getBusinessEvent())
                        .toBuilder();
        PostProcessor postProcessor = injector.getInstance(PostProcessor.class);
        postProcessor.postProcess(WorkflowStep.class, workflowStep);
        return workflowStep.build();
    }


    @Test
    void validateBondExecutionInput() throws IOException {
        BusinessEvent.BusinessEventBuilder businessEventBuilder = ResourcesUtils.getObject(BusinessEvent.class, "cdm-sample-files/functions/repo-and-bond/bond-execution-func-input.json").toBuilder();
        BusinessEvent businessEvent = reKey(businessEventBuilder).build();
        List<Instruction> instruction = (List<Instruction>) businessEvent.getInstruction();
        CreateBusinessEventInput actual = new CreateBusinessEventInput(instruction, businessEvent.getIntent(), businessEvent.getEventDate(), businessEvent.getEffectiveDate());
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/bond-execution-func-input.json", actual);
    }

    @Test
    void validateRepoExecutionInput() throws IOException {
        BusinessEvent.BusinessEventBuilder businessEventBuilder = ResourcesUtils.getObject(BusinessEvent.class, "cdm-sample-files/functions/repo-and-bond/repo-execution-func-input.json").toBuilder();
        BusinessEvent businessEvent = reKey(businessEventBuilder).build();
        List<Instruction> instruction = (List<Instruction>) businessEvent.getInstruction();
        CreateBusinessEventInput actual = new CreateBusinessEventInput(instruction, businessEvent.getIntent(), businessEvent.getEventDate(), businessEvent.getEffectiveDate());
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/repo-execution-func-input.json", actual);
    }

    @Test
    void validateRollInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();
        AdjustableOrRelativeDate effectiveRollDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/roll-primitive-instruction-effective-roll-date.json");
        AdjustableOrRelativeDate terminationDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/roll-primitive-instruction-termination-date.json");
        List<? extends PriceQuantity> priceQuantity = executionTradeState.getTrade().getTradeLot().get(0).getPriceQuantity();

        Create_RollPrimitiveInstruction create_rollPrimitiveInstruction = injector.getInstance(Create_RollPrimitiveInstruction.class);
        PrimitiveInstruction rollPrimitiveInstruction = create_rollPrimitiveInstruction.evaluate(executionTradeState,
                effectiveRollDate,
                terminationDate,
                priceQuantity);

        Date unadjustedRollDate = effectiveRollDate.getAdjustableDate().getUnadjustedDate();
        Instruction.InstructionBuilder rollInstructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(rollPrimitiveInstruction);

        reKey(rollInstructionBuilder);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(rollInstructionBuilder.build()), null, unadjustedRollDate, unadjustedRollDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/roll-input.json", actual);
    }

    @Test
    void validateOnDemandRateChangeInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();
        AdjustableOrRelativeDate effectiveDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/on-demand-rate-change-primitive-instruction-effective-date.json");
        BigDecimal agreedRate = new BigDecimal("0.005");

        Create_OnDemandRateChangePrimitiveInstruction create_onDemandRateChangePrimitiveInstruction = injector.getInstance(Create_OnDemandRateChangePrimitiveInstruction.class);
        PrimitiveInstruction onDemandRateChangePrimitiveInstruction = create_onDemandRateChangePrimitiveInstruction.evaluate(executionTradeState, effectiveDate, agreedRate);

        Date unadjustedEffectiveDate = effectiveDate.getAdjustableDate().getUnadjustedDate();
        Instruction.InstructionBuilder onDemandRateChangeInstructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(onDemandRateChangePrimitiveInstruction);

        reKey(onDemandRateChangeInstructionBuilder);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(onDemandRateChangeInstructionBuilder.build()), null, unadjustedEffectiveDate, unadjustedEffectiveDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/on-demand-rate-change-input.json", actual);
    }

    @Test
    void validatePairOffInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();

        Identifier.IdentifierBuilder pairReferenceIdentifierBuilder = Identifier.builder();
        pairReferenceIdentifierBuilder.getOrCreateAssignedIdentifier(0)
                .setIdentifierValue("Package");

        Create_PairOffInstruction create_pairOffInstruction = injector.getInstance(Create_PairOffInstruction.class);

        List<? extends Instruction> pairOffInstruction = create_pairOffInstruction.evaluate(Lists.newArrayList(executionTradeState, executionTradeState), pairReferenceIdentifierBuilder.build());
        List<Instruction> rekeyedPairOffInstructions = pairOffInstruction.stream()
                .map(Instruction::toBuilder)
                .map(b -> reKey(b))
                .map(Instruction::build)
                .collect(Collectors.toList());

        Date tradeDate = executionTradeState.getTrade().getTradeDate().getValue();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(rekeyedPairOffInstructions, null, tradeDate, tradeDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/pair-off-input.json", actual);
    }

    @Test
    void validateCancellationInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();
        AdjustableOrRelativeDate cancellationDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/cancellation-primitive-instruction-cancellation-date.json");

        Create_CancellationPrimitiveInstruction create_cancellationPrimitiveInstruction = injector.getInstance(Create_CancellationPrimitiveInstruction.class);
        PrimitiveInstruction cancellationPrimitiveInstruction = create_cancellationPrimitiveInstruction.evaluate(executionTradeState, null, cancellationDate);

        Instruction.InstructionBuilder cancellationInstructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(cancellationPrimitiveInstruction);

        reKey(cancellationInstructionBuilder);

        Date unadjustedCancellationDate = cancellationDate.getAdjustableDate().getUnadjustedDate();
        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(cancellationInstructionBuilder.build()), null, unadjustedCancellationDate, unadjustedCancellationDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/cancellation-input.json", actual);
    }

    @Test
    void validateOnDemandInterestPaymentEventInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();

        Money interestAmount = ResourcesUtils.getObject(Money.class, "cdm-sample-files/functions/repo-and-bond/on-demand-interest-payment-primitive-instruction-interest-amount.json");
        SettlementDate settlementDate = ResourcesUtils.getObject(SettlementDate.class, "cdm-sample-files/functions/repo-and-bond/on-demand-interest-payment-primitive-instruction-settlement-date.json");

        Create_OnDemandInterestPaymentPrimitiveInstruction create_onDemandInterestPaymentPrimitiveInstruction =
                injector.getInstance(Create_OnDemandInterestPaymentPrimitiveInstruction.class);

        PrimitiveInstruction.PrimitiveInstructionBuilder primitiveInstructionBuilder = create_onDemandInterestPaymentPrimitiveInstruction
                .evaluate(executionTradeState, interestAmount, settlementDate)
                .toBuilder();

        reKey(primitiveInstructionBuilder);

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(primitiveInstructionBuilder);

        Date tradeDate = executionTradeState.getTrade().getTradeDate().getValue();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(instructionBuilder.build()), null, tradeDate, tradeDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/on-demand-interest-payment-input.json", actual);
    }

    @Test
    void validateShapingPrimitiveInstructionTradeLots() throws IOException {
        String resourceName = "cdm-sample-files/functions/repo-and-bond/shaping-primitive-instruction-trade-lots.json";
        List<TradeLot.TradeLotBuilder> tradeLotBuilders = toBuilder(ResourcesUtils.getObjectList(TradeLot.class, resourceName));
        List<TradeLot> actual = build(reKey(tradeLotBuilders));
        assertJsonEquals(resourceName, actual);
    }

    @Test
    void validateShapingEventInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();

        List<TradeLot> tradeLots = ResourcesUtils.getObjectList(TradeLot.class, "cdm-sample-files/functions/repo-and-bond/shaping-primitive-instruction-trade-lots.json");
        Identifier shapeIdentifier = ResourcesUtils.getObject(Identifier.class, "cdm-sample-files/functions/repo-and-bond/shaping-primitive-instruction-shape-identifier.json");

        Create_ShapingInstruction create_shapingInstruction = injector.getInstance(Create_ShapingInstruction.class);
        PrimitiveInstruction.PrimitiveInstructionBuilder primitiveInstructionBuilder = create_shapingInstruction.evaluate(executionTradeState, tradeLots, shapeIdentifier).toBuilder();

        reKey(primitiveInstructionBuilder);

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(primitiveInstructionBuilder);

        Date tradeDate = executionTradeState.getTrade().getTradeDate().getValue();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(instructionBuilder.build()), null, tradeDate, tradeDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/shaping-input.json", actual);
    }

    @Test
    void validatePartialDeliveryDeliveredPriceQuantity() throws IOException {
        String resourceName = "cdm-sample-files/functions/repo-and-bond/partial-delivery-delivered-price-quantity.json";
        List<PriceQuantity.PriceQuantityBuilder> priceQuantityBuilder = toBuilder(ResourcesUtils.getObjectList(PriceQuantity.class, resourceName));
        List<PriceQuantity> actual = build(reKey(priceQuantityBuilder));
        assertJsonEquals(resourceName, actual);
    }

    @Test
    void validatePartialDeliveryEventInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();
        AdjustableOrRelativeDate effectiveDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/partial-delivery-effective-date.json");
        List<? extends PriceQuantity> deliveredPriceQuantity = ResourcesUtils.getObjectList(PriceQuantity.class, "cdm-sample-files/functions/repo-and-bond/partial-delivery-delivered-price-quantity.json");

        Create_PartialDeliveryPrimitiveInstruction create_partialDeliveryPrimitiveInstruction = injector.getInstance(Create_PartialDeliveryPrimitiveInstruction.class);
        PrimitiveInstruction partialDeliveryPrimitiveInstruction = create_partialDeliveryPrimitiveInstruction.evaluate(executionTradeState, deliveredPriceQuantity).toBuilder();

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(partialDeliveryPrimitiveInstruction);

        reKey(instructionBuilder);

        Date eventDate = effectiveDate.getAdjustableDate().getUnadjustedDate();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(instructionBuilder.build()), null, eventDate, eventDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/partial-delivery-input.json", actual);
    }

    @Test
    void validateRepriceEventInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();
        BigDecimal newAllinPrice = new BigDecimal("101.25");
        BigDecimal newCashValue = new BigDecimal("9922500.00");
        AdjustableOrRelativeDate effectiveDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/repo-reprice-effective-date.json");

        Create_RepricePrimitiveInstruction create_repriceInstruction = injector.getInstance(Create_RepricePrimitiveInstruction.class);
        PrimitiveInstruction.PrimitiveInstructionBuilder primitiveInstructionBuilder = create_repriceInstruction.evaluate(executionTradeState, newAllinPrice, newCashValue, effectiveDate).toBuilder();

        reKey(primitiveInstructionBuilder);

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(primitiveInstructionBuilder);

        Date eventDate = effectiveDate.getAdjustableDate().getUnadjustedDate();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(instructionBuilder.build()), null, eventDate, eventDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/repo-reprice-input.json", actual);
    }

    @Test
    void validateAdjustmentEventInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();
        BigDecimal newAllinPrice = new BigDecimal("99.25");
        BigDecimal newAssetQuantity = new BigDecimal("10151134");
        AdjustableOrRelativeDate effectiveDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/repo-adjustment-effective-date.json");

        Create_AdjustmentPrimitiveInstruction create_adjustmentInstruction = injector.getInstance(Create_AdjustmentPrimitiveInstruction.class);
        PrimitiveInstruction.PrimitiveInstructionBuilder primitiveInstructionBuilder = create_adjustmentInstruction.evaluate(executionTradeState, newAllinPrice, newAssetQuantity, effectiveDate).toBuilder();

        reKey(primitiveInstructionBuilder);

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(primitiveInstructionBuilder);

        Date eventDate = effectiveDate.getAdjustableDate().getUnadjustedDate();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(instructionBuilder.build()), null, eventDate, eventDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/repo-adjustment-input.json", actual);
    }

    @Test
    void validateRepoSubstitutionCollateral() throws IOException {
        String resourceName = "cdm-sample-files/functions/repo-and-bond/repo-substitution-collateral.json";
        Collateral.CollateralBuilder collateralBuilder = ResourcesUtils.getObject(Collateral.class, resourceName).toBuilder();
        Collateral actual = reKey(collateralBuilder).build();
        assertJsonEquals(resourceName, actual);
    }

    @Test
    void validateRepoSubstitutionPriceQuantity() throws IOException {
        String resourceName = "cdm-sample-files/functions/repo-and-bond/repo-substitution-price-quantity.json";
        List<TradeLot.TradeLotBuilder> tradeLotBuilders = toBuilder(ResourcesUtils.getObjectList(TradeLot.class, resourceName));
        List<TradeLot> actual = build(reKey(tradeLotBuilders));
        assertJsonEquals(resourceName, actual);
    }

    @Test
    void validateSubstitutionEventInput() throws IOException {
        TradeState executionTradeState = getRepoExecutionAfterTradeState();
        AdjustableOrRelativeDate effectiveDate = ResourcesUtils.getObject(AdjustableOrRelativeDate.class, "cdm-sample-files/functions/repo-and-bond/repo-substitution-effective-date.json");
        CollateralPortfolio newCollateralPortfolio = ResourcesUtils.getObject(CollateralPortfolio.class, "cdm-sample-files/functions/repo-and-bond/repo-substitution-collateral.json");
        List<? extends PriceQuantity> priceQuantity = ResourcesUtils.getObjectList(PriceQuantity.class, "cdm-sample-files/functions/repo-and-bond/repo-substitution-price-quantity.json");

        Create_SubstitutionPrimitiveInstruction create_substitutionInstruction = injector.getInstance(Create_SubstitutionPrimitiveInstruction.class);
        PrimitiveInstruction.PrimitiveInstructionBuilder primitiveInstructionBuilder = create_substitutionInstruction.evaluate(executionTradeState, effectiveDate, newCollateralPortfolio, priceQuantity).toBuilder();

        reKey(primitiveInstructionBuilder);

        Instruction.InstructionBuilder instructionBuilder = Instruction.builder()
                .setBeforeValue(executionTradeState)
                .setPrimitiveInstruction(primitiveInstructionBuilder);

        Date eventDate = effectiveDate.getAdjustableDate().getUnadjustedDate();

        CreateBusinessEventInput actual = new CreateBusinessEventInput(Lists.newArrayList(instructionBuilder.build()), null, eventDate, eventDate);
        assertJsonEquals("cdm-sample-files/functions/repo-and-bond/repo-substitution-input.json", actual);
    }

    private TradeState removeIsdaProductTaxonomy(TradeState tradeState) {
        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
        NonTransferableProduct.NonTransferableProductBuilder nonTransferableProductBuilder =
                tradeStateBuilder.getTrade().getProduct();
        List<? extends ProductTaxonomy.ProductTaxonomyBuilder> newProductTaxonomies = nonTransferableProductBuilder.getTaxonomy().stream()
                .filter(taxonomy -> taxonomy.getSource() == null || !taxonomy.getSource().equals(TaxonomySourceEnum.ISDA))
                .collect(Collectors.toList());
        nonTransferableProductBuilder.setTaxonomy(newProductTaxonomies);
        return tradeStateBuilder.build();
    }

    private TradeState getRepoExecutionAfterTradeState() throws IOException {
        BusinessEvent executionBusinessEvent = ResourcesUtils.getObject(BusinessEvent.class, "cdm-sample-files/functions/repo-and-bond/repo-execution-func-output.json");
        return ResourcesUtils.resolveReferences(removeIsdaProductTaxonomy(executionBusinessEvent.getAfter().get(0)));
    }

    @Test
    void validateEligibleCollateralScheduleHelper() {
        // Common criteria - GILTS
        EligibleCollateralCriteria common = EligibleCollateralCriteria.builder()
                .setCollateralCriteria(CollateralCriteria.builder()
                        .setAssetType(AssetType.builder()
                                .setAssetType(AssetTypeEnum.SECURITY)
                                .setSecurityType(InstrumentTypeEnum.DEBT))
                        .setCollateralIssuerType(CollateralIssuerType.builder()
                                .setIssuerType(
                                IssuerTypeEnum.SOVEREIGN_CENTRAL_BANK))
                        .setIssuerCountryOfOrigin(IssuerCountryOfOrigin.builder()
                                .setIssuerCountryOfOrigin(ISOCountryCodeEnum.GB)))
                .build();
        ;

        // Variable criteria - Valuation percentages for each maturity range
        List<EligibleCollateralCriteria> variable = Arrays.asList(
                getVariableCriteria(0.97, getMaturityRange(0, 1)),
                getVariableCriteria(0.96, getMaturityRange(1, 5)),
                getVariableCriteria(0.95, getMaturityRange(5, 10)),
                getVariableCriteria(0.93, getMaturityRange(10, 30)),
                getVariableCriteria(0.9, getMaturityRange(30)));

        // Create instruction
        EligibleCollateralSpecificationInstruction instruction = EligibleCollateralSpecificationInstruction.builder()
                .setCommon(common)
                .setVariable(variable)
                .build();

        assertJsonEquals("cdm-sample-files/functions/eligible-collateral/merge-criteria-func-input.json", instruction);
    }

    private static PeriodRange getMaturityRange(int lowerBound, int upperBound) {
        return PeriodRange.builder()
                .setLowerBound(getMaturityBound(lowerBound, true))
                .setUpperBound(getMaturityBound(upperBound, false))
                .build();
    }

    private static PeriodRange getMaturityRange(int lowerBound) {
        return PeriodRange.builder()
                .setLowerBound(getMaturityBound(lowerBound, true))
                .build();
    }

    private static PeriodBound.PeriodBoundBuilder getMaturityBound(int years, boolean inclusive) {
        return PeriodBound.builder()
                .setInclusive(inclusive)
                .setPeriod(Period.builder()
                        .setPeriodMultiplier(years)
                        .setPeriod(PeriodEnum.Y));
    }

    private static EligibleCollateralCriteria getVariableCriteria(double haircutPercentage, PeriodRange maturityRange) {
        return EligibleCollateralCriteria.builder()
                .setTreatment(CollateralTreatment.builder()
                        .setIsIncluded(true)
                        .setValuationTreatment(CollateralValuationTreatment.builder()
                                .setHaircutPercentage(BigDecimal.valueOf(haircutPercentage))))
                .setCollateralCriteria(CollateralCriteria.builder()
                        .setAssetMaturity(AssetMaturity.builder()
                                .setMaturityType(MaturityTypeEnum.REMAINING_MATURITY)
                                .setMaturityRange(maturityRange))
                )
                .build();
    }

    private void assertJsonEquals(String expectedJsonPath, Object actual) {
        try {
            String actualJson = STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual);
            String expectedJson = ResourcesUtils.getJson(expectedJsonPath);
            if (!expectedJson.equals(actualJson)) {
                if (WRITE_EXPECTATIONS) {
                    writeExpectation(expectedJsonPath, actualJson);
                }
            }
            assertEquals(expectedJson, actualJson,
                    "The input JSON for " + Paths.get(expectedJsonPath).getFileName() + " has been updated (probably due to a model change). Update the input file");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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