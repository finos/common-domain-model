package org.finos.cdm.example.ingestion;

import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import cdm.ingest.cme.clearport.tradecapture.CmeClearPortFixmlDocument;
import cdm.ingest.cme.clearport.tradecapture.functions.Ingest_CmeClearPortFixmlToTradeState;
import cdm.ingest.cme.clearport.tradecapture.functions.Ingest_CmeClearPortFixmlToWorkflowStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.rosetta.model.lib.records.Date;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * End-to-end JUnit 5 verification of the CME ClearPort / CME IRS Submission FIXML 5.0 SP2
 * ingestion pipeline into FINOS CDM {@link TradeState} and {@link WorkflowStep}.
 *
 * <p>Validates both:
 * <ul>
 *   <li>Post-clearing CME ClearPort {@code TradeCaptureReport} ({@code TrdCaptRpt}) confirmation fixtures</li>
 *   <li>Pre-clearing CME IRS Submission ({@code cme-submission-irs-1-0}) fixtures from the CDM benchmark suite</li>
 * </ul>
 * using the declarative {@code RosettaXMLConfiguration} ({@code xml-config/cme-clearport-fixml-xml-config.json})
 * and the {@code [ingest XML]} Rune DSL functions.
 */
public class IngestCmeClearPortFixmlTest extends AbstractIngestionTest {

    private static final String CME_CLEARPORT_XML_CONFIG = "xml-config/cme-clearport-fixml-xml-config.json";

    private static ObjectMapper cmeXmlMapper;

    @Inject
    private Ingest_CmeClearPortFixmlToTradeState ingestToTradeState;

    @Inject
    private Ingest_CmeClearPortFixmlToWorkflowStep ingestToWorkflowStep;

    @BeforeAll
    static void initCmeXmlMapper() {
        cmeXmlMapper = getXmlMapper(CME_CLEARPORT_XML_CONFIG);
    }

    @Test
    void shouldIngestPostClearingTradeCaptureSample() throws IOException {
        CmeClearPortFixmlDocument doc = readFixml("ingest/input/cme-clearport/cme-clearport-tradecapture-sample.xml");
        assertNotNull(doc);
        assertNotNull(doc.getTradeCaptureReport());
        assertEquals("RPT-CME-20260926-001", doc.getTradeCaptureReport().getRptId());
        assertEquals("TRD-CME-883921", doc.getTradeCaptureReport().getTrdId());
        assertEquals("ES_FUT", doc.getTradeCaptureReport().getInstrument().getSymbol());

        TradeState tradeState = ingestToTradeState.evaluate(doc);
        assertNotNull(tradeState);
        assertNotNull(tradeState.getTrade());
        assertEquals(Date.of(2026, 9, 26), tradeState.getTrade().getTradeDate().getValue());
        assertEquals(4, tradeState.getTrade().getParty().size());
        assertEquals(4, tradeState.getTrade().getPartyRole().size());

        WorkflowStep workflowStep = ingestToWorkflowStep.evaluate(doc);
        assertNotNull(workflowStep);
        assertNotNull(workflowStep.getBusinessEvent());
        assertEquals(1, workflowStep.getBusinessEvent().getAfter().size());
        assertEquals(1, workflowStep.getEventIdentifier().size());
        assertEquals("RPT-CME-20260926-001",
                workflowStep.getEventIdentifier().get(0).getAssignedIdentifier().get(0).getIdentifier().getValue());
    }

    @Test
    void shouldIngestPostClearingSwapSettlementSample() throws IOException {
        CmeClearPortFixmlDocument doc = readFixml("ingest/input/cme-clearport/cme-clearport-swap-settlement-sample.xml");
        assertNotNull(doc);
        assertEquals("RPT-CME-20260926-002", doc.getTradeCaptureReport().getRptId());
        assertEquals("TRD-CME-994102", doc.getTradeCaptureReport().getTrdId());
        assertEquals("USD_SOFR_IRS", doc.getTradeCaptureReport().getInstrument().getSymbol());

        TradeState tradeState = ingestToTradeState.evaluate(doc);
        assertNotNull(tradeState);
        assertEquals(Date.of(2026, 9, 26), tradeState.getTrade().getTradeDate().getValue());
        assertEquals(4, tradeState.getTrade().getParty().size());

        WorkflowStep workflowStep = ingestToWorkflowStep.evaluate(doc);
        assertNotNull(workflowStep);
        assertEquals("RPT-CME-20260926-002",
                workflowStep.getEventIdentifier().get(0).getAssignedIdentifier().get(0).getIdentifier().getValue());
    }

    @Test
    void shouldIngestPreClearingCmeSubmissionUsdIrsSample() throws IOException {
        CmeClearPortFixmlDocument doc = readFixml("ingest/input/cme-clearport/cme-submission-irs-1-0-usd-irs-sample.xml");
        assertNotNull(doc);
        assertEquals("43401154", doc.getTradeCaptureReport().getExecId());
        assertEquals("IRS", doc.getTradeCaptureReport().getInstrument().getSecurityType());

        TradeState tradeState = ingestToTradeState.evaluate(doc);
        assertNotNull(tradeState);
        assertEquals(Date.of(2018, 4, 6), tradeState.getTrade().getTradeDate().getValue());
        assertEquals(10, tradeState.getTrade().getParty().size());

        WorkflowStep workflowStep = ingestToWorkflowStep.evaluate(doc);
        assertNotNull(workflowStep);
        assertEquals("43401154",
                workflowStep.getEventIdentifier().get(0).getAssignedIdentifier().get(0).getIdentifier().getValue());
    }

    @Test
    void shouldIngestPreClearingCmeSubmissionUsdOisSofrSample() throws IOException {
        CmeClearPortFixmlDocument doc = readFixml("ingest/input/cme-clearport/cme-submission-irs-1-0-usd-ois-sofr-sample.xml");
        assertNotNull(doc);
        assertEquals("43401034", doc.getTradeCaptureReport().getExecId());
        assertEquals("USD_OIS_SOFR_COMPOUND", doc.getTradeCaptureReport().getInstrument().getSymbol());

        TradeState tradeState = ingestToTradeState.evaluate(doc);
        assertNotNull(tradeState);
        assertEquals(Date.of(2018, 4, 6), tradeState.getTrade().getTradeDate().getValue());
        assertEquals(10, tradeState.getTrade().getParty().size());

        WorkflowStep workflowStep = ingestToWorkflowStep.evaluate(doc);
        assertNotNull(workflowStep);
        assertEquals("43401034",
                workflowStep.getEventIdentifier().get(0).getAssignedIdentifier().get(0).getIdentifier().getValue());
    }

    private static CmeClearPortFixmlDocument readFixml(String resourcePath) throws IOException {
        URL url = Resources.getResource(resourcePath);
        return cmeXmlMapper.readValue(url, CmeClearPortFixmlDocument.class);
    }
}
