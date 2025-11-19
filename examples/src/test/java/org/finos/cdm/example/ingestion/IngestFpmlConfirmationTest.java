package org.finos.cdm.example.ingestion;

import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import fpml.consolidated.doc.Document;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Integration-style test that manually runs the FpML -> TradeState/WorkflowStep ingestion
 * using Rosetta XML ObjectMapper (configured via xml-config)
 */
public class IngestFpmlConfirmationTest extends AbstractIngestionTest {

    private static final Logger logger = LoggerFactory.getLogger(IngestFpmlConfirmationTest.class);
    ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();
    ObjectMapper xmlMapper = getXmlMapper();

    /**
     * Tests the ingestion of an FpML confirmation XML file into a TradeState object.
     * This ensures that the XML configuration is correctly loaded, the FpML document is parsed,
     * and the ingestion function produces a valid TradeState instance.
     *
     * @throws IOException if the XML file cannot be read
     */
    @Test
    void ingestFpmlConfirmationToTradeState() throws IOException {

        // Load a specific FpML file
        URL url = Resources.getResource("ingest/input/fpml-5-13-products-interest-rate-derivatives/ird-ex01-vanilla-swap.xml");
        Document document = xmlMapper.readValue(url, Document.class);

        // Log the fpml document
        logger.debug(defaultXmlWriter.writeValueAsString(document));

        // Evaluate the function
        TradeState tradeState = fpmlConfirmationToTradeStateFunc.evaluate(document);
        assertNotNull(tradeState, "TradeState is null");

        // Log the resulting tradeState
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tradeState));
    }

    /**
     * Tests the ingestion of an FpML confirmation XML file into a WorkflowStep object.
     * This verifies that the XML configuration is correctly loaded, the document is parsed,
     * and the ingestion function produces a valid WorkflowStep.
     *
     * @throws IOException if the XML file cannot be read
     */
    @Test
    void ingestFpmlConfirmationToWorkflowStep() throws IOException {

        // Load a specific FpML file
        URL url = Resources.getResource("ingest/input/fpml-5-13-processes-execution-advice/msg-ex52-execution-advice-trade-partial-novation-C02-00.xml");
        Document document = xmlMapper.readValue(url, Document.class);

        // Log the fpml document
        logger.debug(defaultXmlWriter.writeValueAsString(document));

        // Evaluate the function
        WorkflowStep workflowStep = fpmlConfirmationToWorkflowStepfunc.evaluate(document);
        assertNotNull(workflowStep, "WorkflowStep is null");

        // Log the resulting workflowStep
        logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(workflowStep));
    }
}

