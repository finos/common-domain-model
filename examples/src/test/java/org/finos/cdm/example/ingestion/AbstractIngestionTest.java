package org.finos.cdm.example.ingestion;

import cdm.ingest.fpml.confirmation.message.functions.Ingest_FpmlConfirmationToTradeState;
import cdm.ingest.fpml.confirmation.message.functions.Ingest_FpmlConfirmationToWorkflowStep;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapperCreator;
import com.regnosys.runefpml.RuneFpmlModelConfig;
import jakarta.inject.Inject;
import org.finos.cdm.example.AbstractExampleTest;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

public abstract class AbstractIngestionTest extends AbstractExampleTest {

    private static final String XSD_SCHEMA_NAME = "schemas/fpml-5-13/confirmation/fpml-main-5-13.xsd";
    private static final String EXPECTED_SCHEMA_LOCATION = "http://www.fpml.org/FpML-5/confirmation " + XSD_SCHEMA_NAME;

    // Ingestion function from Fpml to TradeState
    @Inject
    Ingest_FpmlConfirmationToTradeState fpmlConfirmationToTradeStateFunc;

    // Ingestion function from Fpml to WorkflowStep
    @Inject
    Ingest_FpmlConfirmationToWorkflowStep fpmlConfirmationToWorkflowStepfunc;

    public static ObjectMapper defaultXmlMapper;
    public static ObjectWriter defaultXmlWriter;

    @BeforeAll
    static void setupOnce() {
        defaultXmlMapper = getXmlMapper(RuneFpmlModelConfig.FPML_CONFIRMATION_XML_CONFIG_PATH);
        defaultXmlWriter = defaultXmlMapper
                .writerWithDefaultPrettyPrinter()
                .withAttribute("schemaLocation", EXPECTED_SCHEMA_LOCATION);
    }

    // Load the XML configuration file and create the Rosetta XML ObjectMapper
    // This configuration file defines how the Rosetta XML ObjectMapper should interpret,
    // validate, and map the XML structures used for FpML confirmations.
    protected static ObjectMapper getXmlMapper(String xmlConfigPath) {
        try {
            URL url = Objects.requireNonNull(Resources.getResource(xmlConfigPath));
            ObjectMapper mapper = RosettaObjectMapperCreator.forXML(url.openStream()).create();
            ((XmlMapper) mapper).configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            return mapper;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
