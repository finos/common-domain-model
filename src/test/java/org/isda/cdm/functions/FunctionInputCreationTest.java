package org.isda.cdm.functions;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.event.common.TerminationInstruction;
import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import util.ResourcesUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionInputCreationTest {

    private static final ObjectMapper STRICT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
            .setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));

    @Test
    void validateCreateTerminationWorkflowFuncInputJson() throws IOException {
        RunCreateTerminationWorkflowInput actual = new RunCreateTerminationWorkflowInput(
                getTradeState(),
                TerminationInstruction.builder()
                        .addTerminatedQuantity(Quantity.builder()
                                .setAmount(BigDecimal.valueOf(0))
                                .setUnitOfAmount(UnitType.builder().setCurrency(FieldWithMetaString.builder()
                                        .setValue("USD")
                                        .setMeta(MetaFields.builder()
                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))).build())
                                .build())
                        .setTerminationDate(DateImpl.of(2019, 12, 12)).build());

        assertEquals(readResource("/cdm-sample-files/functions/termination-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for termination-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreatePartialTerminationWorkflowFuncInputJson() throws IOException {
        RunCreateTerminationWorkflowInput actual = new RunCreateTerminationWorkflowInput(
                getTradeState(),
                TerminationInstruction.builder()
                        .addTerminatedQuantity(Quantity.builder()
                                .setAmount(BigDecimal.valueOf(3000))
                                .setUnitOfAmount(UnitType.builder().setCurrency(FieldWithMetaString.builder()
                                        .setValue("USD")
                                        .setMeta(MetaFields.builder()
                                                .setScheme("http://www.fpml.org/coding-scheme/external/iso4217"))).build())
                                .build())
                        .setTerminationDate(DateImpl.of(2019, 12, 12)).build());

        assertEquals(readResource("/cdm-sample-files/functions/partial-termination-workflow-func-input.json"),
                STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                "The input JSON for partial-termination-workflow-func-input.json has been updated (probably due to a model change). Update the input file");
    }

    @Test
    void validateCreateAllocationWorkflowInputJason() throws IOException {
        TradeState.TradeStateBuilder tradeState = getTradeState();


        String foo = STRICT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(tradeState);
        System.out.println(foo);
    }

    @NotNull
    private TradeState.TradeStateBuilder getTradeState() throws IOException {
        WorkflowStep workflowStep = ResourcesUtils.getObject(WorkflowStep.class, "result-json-files/fpml-5-10/record-keeping/record-ex01-vanilla-swap.json");
        TradeState.TradeStateBuilder tradeState = workflowStep.getBusinessEvent().getPrimitives().get(0).getContractFormation().getAfter().toBuilder();
        tradeState.getTrade().setParty(workflowStep.getParty());
        return tradeState;
    }

    private static String readResource(String inputJson) throws IOException {
        //noinspection UnstableApiUsage
        return Resources
                .toString(Objects
                        .requireNonNull(FunctionInputCreationTest.class.getResource(inputJson)), Charset
                        .defaultCharset());
    }
}
