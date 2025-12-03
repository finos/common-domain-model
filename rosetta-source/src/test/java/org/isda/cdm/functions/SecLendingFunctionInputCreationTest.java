package org.isda.cdm.functions;

import cdm.base.math.*;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.common.functions.Create_BusinessEvent;
import cdm.event.common.functions.Create_Execution;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.event.Observation;
import cdm.observable.event.ObservationIdentifier;
import cdm.product.template.TradeLot;
import cdm.security.lending.functions.RunNewSettlementWorkflow;
import cdm.security.lending.functions.RunReturnSettlementWorkflow;
import cdm.security.lending.functions.RunReturnSettlementWorkflowInput;
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
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
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
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.ResourcesUtils.getJson;
import static util.ResourcesUtils.reKey;

class SecLendingFunctionInputCreationTest {

    private static final boolean WRITE_EXPECTATIONS =
            Optional.ofNullable(System.getenv("WRITE_EXPECTATIONS"))
                    .map(Boolean::parseBoolean).orElse(false);
    private static final Optional<Path> TEST_WRITE_BASE_PATH =
            Optional.ofNullable(System.getenv("TEST_WRITE_BASE_PATH")).map(Paths::get);
    private static final Logger LOGGER = LoggerFactory.getLogger(SecLendingFunctionInputCreationTest.class);


    private static final ObjectMapper STRICT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

    public static final ObjectMapper MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper();

    // AVOID ADDING MANUALLY CRAFTED JSON

    // ALLOCATION AND REALLOCATION EXAMPLES ARE BASED ON THIS EXECUTION INSTRUCTION.
    // This is the execution instruction between an agent lender and a borrower
    public static final String EXECUTION_INSTRUCTION_JSON = "/functions/sec-lending/block-execution-instruction.json";
    public static final String BLOCK_EXECUTION_TRADE_STATE_JSON = "functions/sec-lending/block-execution-trade-state.json";

    // SETTLEMENT AND RETURN WORKFLOWS ARE BASED OF THIS..
    public static final String SETTLEMENT_WORKFLOW_FUNC_INPUT_JSON = "/functions/sec-lending/new-settlement-workflow-func-input.json";

    public static final String EXECUTION_CASH_FUNC_INPUT_JSON = "/functions/sec-lending/execution/execution-cash-input.json";
    public static final String EXECUTION_CASH_BENCHMARK_FUNC_INPUT_JSON = "/functions/sec-lending/execution/execution-cash-benchmark-input.json";
    public static final String EXECUTION_NONCASH_PORTFOLIO_FUNC_INPUT_JSON = "/functions/sec-lending/execution/execution-noncash-portfolio-input.json";

    

    private static Injector injector;

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
    void executionCash() throws IOException {
        assertJsonConformsToRosettaType(EXECUTION_CASH_FUNC_INPUT_JSON, CreateBusinessEventInput.class);
    }

    @Test
    void executionCashBenchmark() throws IOException {
        assertJsonConformsToRosettaType(EXECUTION_CASH_BENCHMARK_FUNC_INPUT_JSON, CreateBusinessEventInput.class);
    }

    @Test
    void executionNoncashPortfolio() throws IOException {
        assertJsonConformsToRosettaType(EXECUTION_NONCASH_PORTFOLIO_FUNC_INPUT_JSON, CreateBusinessEventInput.class);
    }
    
    @Test
    void validateNewSettlementWorkflowFuncInputJson() throws IOException {
        assertJsonConformsToRosettaType(SETTLEMENT_WORKFLOW_FUNC_INPUT_JSON, ExecutionInstruction.class);
    }

    @Test
    void validateExecutionInstructionWorkflowFuncInputJson() throws IOException {
        assertJsonConformsToRosettaType(EXECUTION_INSTRUCTION_JSON, ExecutionInstruction.class);
    }

    @Test
    void validateExecutionInstructionWorkflowFuncOutputJson() throws IOException {
        URL resource = SecLendingFunctionInputCreationTest.class.getResource(EXECUTION_INSTRUCTION_JSON);
        ExecutionInstruction executionInstruction = STRICT_MAPPER.readValue(resource, ExecutionInstruction.class);
        Create_Execution createExecution = injector.getInstance(Create_Execution.class);

        TradeState.TradeStateBuilder tradeStateBuilder = createExecution.evaluate(executionInstruction).toBuilder();

        PostProcessor postProcessor = injector.getInstance(PostProcessor.class);
        postProcessor.postProcess(TradeState.class, tradeStateBuilder);
        
        assertJsonEquals(BLOCK_EXECUTION_TRADE_STATE_JSON, tradeStateBuilder.build());
    }

    private static TradeState getBlockExecutionTradeStateJson() throws IOException {
        return ResourcesUtils.getObjectAndResolveReferences(TradeState.class, BLOCK_EXECUTION_TRADE_STATE_JSON);
    }

    @Test
    void validatePartReturnSettlementWorkflowFuncInputJson() throws IOException {
        RunReturnSettlementWorkflowInput actual = new RunReturnSettlementWorkflowInput(getTransferTradeState(),
                ReturnInstruction.builder()
                        .addQuantity(Quantity.builder()
                                .setValue(BigDecimal.valueOf(50000))
                                .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE).build())
                                .build())
                        .addQuantity(Quantity.builder()
                                .setValue(BigDecimal.valueOf(1250000))
                                .setUnit(UnitType.builder()
                                        .setCurrency(FieldWithMetaString.builder().setValue("USD").build()).build())
                                .build())
                        .build(),
                Date.of(2020, 10, 8)
        );

        assertJsonEquals("functions/sec-lending/part-return-settlement-workflow-func-input.json", actual);
        assertJsonConformsToRosettaType("/functions/sec-lending/part-return-settlement-workflow-func-input.json", RunReturnSettlementWorkflowInput.class);
    }

    @Test
    void validateFullReturnSettlementWorkflowFuncInputJson() throws IOException {
        ReturnInstruction returnInstruction = ReturnInstruction.builder()
                .addQuantity(Quantity.builder()
                        .setValue(BigDecimal.valueOf(200000))
                        .setUnit(UnitType.builder().setFinancialUnit(FinancialUnitEnum.SHARE)))
                .addQuantity(Quantity.builder()
                        .setValue(BigDecimal.valueOf(5000000))
                        .setUnit(UnitType.builder().setCurrencyValue("USD")))
                .build();

        RunReturnSettlementWorkflowInput actual = new RunReturnSettlementWorkflowInput(getTransferTradeState(),
                returnInstruction,
                Date.of(2020, 10, 21));

        assertJsonEquals("functions/sec-lending/full-return-settlement-workflow-func-input.json", actual);
        assertJsonConformsToRosettaType("/functions/sec-lending/full-return-settlement-workflow-func-input.json", RunReturnSettlementWorkflowInput.class);
    }

    @Test
    void validateCreateAllocationFuncInputJson() throws IOException {
        CreateBusinessEventInput actual = getAllocationInput();

        assertJsonEquals("functions/sec-lending/allocation/allocation-sec-lending-func-input.json", actual);
    }


    private CreateBusinessEventInput getAllocationInput() throws IOException {
        // Agent Lender lends 200k SDOL to Borrower CP001
        TradeState blockExecutionTradeState = getBlockExecutionTradeStateJson();

        SplitInstruction splitInstruction = SplitInstruction.builder()
                // Fund 1 lends 120k SDOL to Borrower CP001
                .addBreakdown(createAllocationInstruction(blockExecutionTradeState,
                        "lender-1",
                        "Fund 1",
                        CounterpartyRoleEnum.PARTY_1,
                        0.60))
                // Fund 2 lends 80k SDOL to Borrower CP001
                .addBreakdown(createAllocationInstruction( blockExecutionTradeState,
                        "lender-2",
                        "Fund 2",
                        CounterpartyRoleEnum.PARTY_1,
                        0.40))
                // Close original trade
                .addBreakdown(PrimitiveInstruction.builder()
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .addQuantityValue(NonNegativeQuantitySchedule.builder()
                                                .setValue(BigDecimal.valueOf(0.0))
                                                .setUnit(UnitType.builder()
                                                        .setCurrencyValue("USD")))
                                        .addQuantityValue(NonNegativeQuantitySchedule.builder()
                                                .setValue(BigDecimal.valueOf(0.0))
                                                .setUnit(UnitType.builder()
                                                        .setFinancialUnit(FinancialUnitEnum.SHARE))))));

        Instruction.InstructionBuilder instruction = Instruction.builder()
                .setBeforeValue(blockExecutionTradeState)
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setSplit(splitInstruction));

        reKey(instruction);

        return new CreateBusinessEventInput(
                Lists.newArrayList(instruction.build()),
                EventIntentEnum.ALLOCATION,
                Date.of(2020, 9, 21),
                null);
    }

    @Test
    void validateCreateReallocationFuncInputJson() throws IOException {
        // We want to get the contract formation for the 40% allocated trade, and back it out by 25% causing a
        // decrease quantity change (so notional will be 10% of the original block).
        // We then want to do a new allocation of 10% of the original block.
        BusinessEvent originalAllocationBusinessEvent = runCreateBusinessEventFunc(getAllocationInput());

        // Trade state where the quantity is 40%
        TradeState tradeStateToBeReallocated = originalAllocationBusinessEvent.getAfter().get(1);

        SplitInstruction splitInstruction = SplitInstruction.builder()
                // Fund 3 lends 20k SDOL to Borrower CP001
                .addBreakdown(createAllocationInstruction(tradeStateToBeReallocated,
                        "lender-3",
                        "Fund 3",
                        CounterpartyRoleEnum.PARTY_1,
                        0.25))
                // Fund 2 lends 60k SDOL to Borrower CP001
                .addBreakdown(PrimitiveInstruction.builder()
                        .setQuantityChange(QuantityChangeInstruction.builder()
                                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                                .addChange(PriceQuantity.builder()
                                        .setQuantityValue(scaleQuantities(tradeStateToBeReallocated, 0.75)))));

        Instruction.InstructionBuilder instruction = Instruction.builder()
                .setBeforeValue(tradeStateToBeReallocated)
                .setPrimitiveInstruction(PrimitiveInstruction.builder().setSplit(splitInstruction));

        reKey(instruction);

        CreateBusinessEventInput actual = new CreateBusinessEventInput(
                Lists.newArrayList(instruction.build()),
                EventIntentEnum.REALLOCATION,
                Date.of(2020, 9, 21),
                null);

        assertJsonEquals("functions/sec-lending/reallocation/reallocation-pre-settled-func-input.json", actual);
    }

    @Test
    void validateCreateSecurityLendingInvoiceFuncInputJson() throws IOException {
        RunReturnSettlementWorkflowInput input = assertJsonConformsToRosettaType("/functions/sec-lending/part-return-settlement-workflow-func-input.json", RunReturnSettlementWorkflowInput.class);
        Workflow part = injector.getInstance(RunReturnSettlementWorkflow.class).execute(input);

        TradeState fullReturnAfterTradeState = getTransferTradeState();

        WorkflowStep partReturnWorkflowStep = part.getSteps().get(2);

        TradeState partReturnBeforeTradeState = partReturnWorkflowStep.getBusinessEvent()
                .getInstruction().get(0)
                .getBefore().getValue();

        TradeState partReturnAfterTradeState = partReturnWorkflowStep.getBusinessEvent()
                .getAfter().get(0);

        BillingInstruction actualBillingInstruction = BillingInstruction.builder()
                .setSendingParty(getParty(partReturnAfterTradeState, CounterpartyRoleEnum.PARTY_1))
                .setReceivingParty(getParty(partReturnAfterTradeState, CounterpartyRoleEnum.PARTY_2))
                .setBillingStartDate(Date.of(2020, 10, 1))
                .setBillingEndDate(Date.of(2020, 10, 31))

                .addBillingRecordInstruction(createBillingRecordInstruction(fullReturnAfterTradeState,
                        Date.of(2020, 10, 1),
                        Date.of(2020, 10, 22),
                        Date.of(2020, 11, 10), Arrays.asList(
                                obs("2020-10-22", 28.18, "USD"),
                                obs("2020-10-21", 28.34, "USD"),
                                obs("2020-10-20", 30.72, "USD"),
                                obs("2020-10-19", 32.01, "USD"),
                                obs("2020-10-18", 32.12, "USD"),
                                obs("2020-10-17", 32.12, "USD"),
                                obs("2020-10-16", 32.12, "USD"),
                                obs("2020-10-15", 31.46, "USD"),
                                obs("2020-10-14", 31.93, "USD"),
                                obs("2020-10-13", 31.87, "USD"),
                                obs("2020-10-12", 31.13, "USD"),
                                obs("2020-10-11", 30.03, "USD"),
                                obs("2020-10-10", 30.03, "USD"),
                                obs("2020-10-09", 30.03, "USD"),
                                obs("2020-10-08", 29.53, "USD"),
                                obs("2020-10-07", 28.72, "USD"),
                                obs("2020-10-06", 28.04, "USD"),
                                obs("2020-10-05", 27.67, "USD"),
                                obs("2020-10-04", 27.23, "USD"),
                                obs("2020-10-03", 27.23, "USD"),
                                obs("2020-10-02", 27.23, "USD"),
                                obs("2020-10-01", 26.87, "USD")
                        )))
                .addBillingRecordInstruction(createBillingRecordInstruction(partReturnBeforeTradeState,
                        Date.of(2020, 10, 1),
                        Date.of(2020, 10, 9),
                        Date.of(2020, 11, 10), Arrays.asList(
                                obs("2020-10-09", 30.03, "USD"),
                                obs("2020-10-08", 29.53, "USD"),
                                obs("2020-10-07", 28.72, "USD"),
                                obs("2020-10-06", 28.04, "USD"),
                                obs("2020-10-05", 27.67, "USD"),
                                obs("2020-10-04", 27.23, "USD"),
                                obs("2020-10-03", 27.23, "USD"),
                                obs("2020-10-02", 27.23, "USD"),
                                obs("2020-10-01", 26.87, "USD")
                        )))
                .addBillingRecordInstruction(createBillingRecordInstruction(partReturnAfterTradeState,
                        Date.of(2020, 10, 10),
                        Date.of(2020, 10, 22),
                        Date.of(2020, 11, 10), Arrays.asList(
                                obs("2020-10-22", 28.18, "USD"),
                                obs("2020-10-21", 28.34, "USD"),
                                obs("2020-10-20", 30.72, "USD"),
                                obs("2020-10-19", 32.01, "USD"),
                                obs("2020-10-18", 32.12, "USD"),
                                obs("2020-10-17", 32.12, "USD"),
                                obs("2020-10-16", 32.12, "USD"),
                                obs("2020-10-15", 31.46, "USD"),
                                obs("2020-10-14", 31.93, "USD"),
                                obs("2020-10-13", 31.87, "USD"),
                                obs("2020-10-12", 31.13, "USD"),
                                obs("2020-10-11", 30.03, "USD"),
                                obs("2020-10-10", 30.03, "USD")
                        )))
                .build();

        assertJsonEquals("functions/sec-lending/create-security-lending-invoice-func-input.json", actualBillingInstruction);
        assertJsonConformsToRosettaType("/functions/sec-lending/create-security-lending-invoice-func-input.json", BillingInstruction.class);
    }

    private BillingRecordInstruction createBillingRecordInstruction(TradeState transferTradeState, Date billingStartDate, Date billingEndDate, Date settlementDate, List<Observation> observations) {
        return BillingRecordInstruction.builder()
                .setTradeStateValue(transferTradeState)
                .setRecordStartDate(billingStartDate)
                .setRecordEndDate(billingEndDate)
                .setSettlementDate(settlementDate)
                .setObservation(observations)
                .build();
    }

    private Observation obs(String date, double price, @SuppressWarnings("SameParameterValue") String currency) {
        return Observation.builder()
                .setObservationIdentifier(ObservationIdentifier.builder()
                        .setObservationDate(Date.of(LocalDate.parse(date)))
                        .build())
                .setObservedValue(Price.builder()
                        .setValue(BigDecimal.valueOf(price))
                        .setUnit(UnitType.builder().setCurrencyValue(currency).build())
                        .build())
                .build();
    }

    private static TradeState getTransferTradeState() throws IOException {
        URL resource = SecLendingFunctionInputCreationTest.class.getResource(SETTLEMENT_WORKFLOW_FUNC_INPUT_JSON);
        ExecutionInstruction executionInstruction = STRICT_MAPPER.readValue(resource, ExecutionInstruction.class);
        RunNewSettlementWorkflow runNewSettlementWorkflow = injector.getInstance(RunNewSettlementWorkflow.class);
        Workflow.WorkflowBuilder workflowBuilder = runNewSettlementWorkflow.execute(executionInstruction).toBuilder();
        reKey(workflowBuilder);
        Workflow workflow = workflowBuilder.build();

        assertNotNull(workflow, "Expected a workflow");
        List<? extends WorkflowStep> workflowSteps = workflow.getSteps();
        assertNotNull(workflowSteps, "Expected workflow " + workflow + " to have some steps");
        assertEquals(3, workflowSteps.size(), "Expected 3 workflow steps but was " + workflowSteps.size());
        WorkflowStep workflowStep = workflowSteps.get(2);
        BusinessEvent businessEvent = workflowStep.getBusinessEvent();
        assertNotNull(businessEvent, "Expected a business event");
        TradeState afterTradeState = businessEvent.getAfter().get(0);
        assertNotNull(afterTradeState, "Expected an after trade state");
        return afterTradeState;
    }

    private PrimitiveInstruction createAllocationInstruction(TradeState tradeState, String externalKey, String partyId, CounterpartyRoleEnum role, double percent) {
        Party agentLenderParty = getParty(tradeState, role);
        TradeIdentifier allocationIdentifier = createAllocationIdentifier(tradeState.build().toBuilder(), "allocation-" + externalKey);
        List<NonNegativeQuantitySchedule> allocatedQuantities = scaleQuantities(tradeState, percent);

        PartyChangeInstruction.PartyChangeInstructionBuilder partyChangeInstruction = PartyChangeInstruction.builder()
                .setCounterparty(Counterparty.builder()
                        .setPartyReferenceValue(Party.builder()
                                .setMeta(MetaFields.builder().setExternalKey(externalKey))
                                .setNameValue(partyId)
                                .addPartyId(PartyIdentifier.builder()
                                        .setIdentifierValue(partyId)
                                        .build()))
                        .setRole(role))
                .setPartyRole(PartyRole.builder()
                        .setPartyReferenceValue(agentLenderParty)
                        .setRole(PartyRoleEnum.AGENT_LENDER))
                .setTradeId(Lists.newArrayList(allocationIdentifier));

        QuantityChangeInstruction.QuantityChangeInstructionBuilder quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                .addChange(PriceQuantity.builder()
                        .setQuantityValue(allocatedQuantities));

        return PrimitiveInstruction.builder()
                .setPartyChange(partyChangeInstruction)
                .setQuantityChange(quantityChangeInstruction);
    }

    private static Party getParty(TradeState tradeState, CounterpartyRoleEnum counterpartyRoleEnum) {
        return tradeState.build().toBuilder().getTrade()
                .getCounterparty().stream()
                .filter(c -> c.getRole() == counterpartyRoleEnum)
                .map(Counterparty::getPartyReference)
                .map(ReferenceWithMetaParty::getValue)
                .findFirst().orElseThrow(RuntimeException::new);
    }


    private static List<NonNegativeQuantitySchedule> scaleQuantities(TradeState tradeState, double percent) {
        return tradeState.build().toBuilder().getTrade()
                .getTradeLot().stream()
                .map(TradeLot.TradeLotBuilder::getPriceQuantity)
                .flatMap(Collection::stream)
                .map(PriceQuantity::getQuantity)
                .flatMap(Collection::stream)
                .map(FieldWithMetaNonNegativeQuantitySchedule::getValue)
                .map(NonNegativeQuantitySchedule::toBuilder)
                .map(q -> q.setValue(q.getValue().multiply(BigDecimal.valueOf(percent))))
                .map(NonNegativeQuantitySchedule::build)
                .collect(Collectors.toList());
    }

    private static TradeIdentifier createAllocationIdentifier(TradeState tradeState, String allocationName) {
        TradeIdentifier.TradeIdentifierBuilder allocationIdentifierBuilder = tradeState.getTrade().getTradeIdentifier().get(0)
                .build().toBuilder();
        allocationIdentifierBuilder.getAssignedIdentifier()
                .forEach(c -> c.setIdentifierValue(c.getIdentifier().getValue() + "-" + allocationName));
        return allocationIdentifierBuilder.build();
    }

    private static String readResource(String inputJson) throws IOException {
        URL resource = SecLendingFunctionInputCreationTest.class.getResource(inputJson);
        //noinspection UnstableApiUsage
        return Resources.toString(Objects.requireNonNull(resource), StandardCharsets.UTF_8);
    }

    private <T> T assertJsonConformsToRosettaType(String inputJson, Class<T> rosettaType) throws IOException {
        URL expectedURL = SecLendingFunctionInputCreationTest.class.getResource(inputJson);
        // dont use the strict one here as we want to see the diff to help us fix
        T actual = MAPPER.readValue(expectedURL, rosettaType);

        assertEquals(readResource(inputJson),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for " + inputJson + " has been updated (probably due to a model change). Update the input file");
        return actual;
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

    private void assertJsonEquals(String expectedJsonPath, Object actual) throws IOException {
        String actualJson = STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual);
        String expectedJson = getJson(expectedJsonPath);
        if (!expectedJson.equals(actualJson)) {
            if (WRITE_EXPECTATIONS) {
                writeExpectation(expectedJsonPath, actualJson);
            }
        }
        assertEquals(expectedJson, actualJson,
                "The input JSON for " + Paths.get(expectedJsonPath).getFileName() + " has been updated (probably due to a model change). Update the input file");
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