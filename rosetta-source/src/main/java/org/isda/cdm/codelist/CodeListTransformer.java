package org.isda.cdm.codelist;

import cdm.base.staticdata.codelist.CodeList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class transforms `https://docs.oasis-open.org/codelist/ns/genericode/1.0/` codelist XML files to JSON-serialized CDM format using an XSLT script.
 * The transformation applies to all XML files in a specified directory.
 * Results are stored in the specified output directory.
 */
public class CodeListTransformer {

    private static final Logger logger = LoggerFactory.getLogger(CodeListTransformer.class);

    private final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    /**
     * Transforms a single XML file using the provided Transformer and writes the output to the specified OutputStream.
     *
     * @param xmlFile      XML file to transform.
     * @param transformer  Pre-configured Transformer instance.
     * @param outputStream Output stream where the transformed JSON is written.
     * @throws TransformerException If an error occurs during transformation.
     */
    private void transformFile(File xmlFile, Transformer transformer, OutputStream outputStream) throws TransformerException {
        StreamSource xmlSource = new StreamSource(xmlFile);
        Result outputTarget = new StreamResult(outputStream);
        transformer.transform(xmlSource, outputTarget);
    }

    /**
     * Transforms all XML files in the given input directory and saves them as JSON in the output directory.
     * This method initializes a single Transformer instance to process multiple files.
     *
     * @param inputDirectory  Directory containing XML files (resource path).
     * @param outputDirectory Directory where JSON files will be saved.
     * @throws TransformerConfigurationException If an error occurs when parsing the Source or it is not possible to create a Transformer instance
     * @throws IOException If an error occurs during transformation resources resolution
     * @throws URISyntaxException If the resource URL are not properly formatted
     */
    public void transformDirectory(String inputDirectory, String outputDirectory) throws TransformerConfigurationException, IOException, URISyntaxException {

        // Path to the XSLT transformation script located in resources.
        final String XSLT_RESOURCE = "/org/isda/codelist/codelist2cdmjson.xsl";

        // Load the XSLT transformation script as a resource
        InputStream xsltStream = CodeListTransformer.class.getResourceAsStream(XSLT_RESOURCE);
        if (xsltStream == null) {
            throw new FileNotFoundException("XSLT file not found in resources: " + XSLT_RESOURCE);
        }

        // Set up Transformer using the loaded XSLT
        StreamSource xsltSource = new StreamSource(xsltStream);
        TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
        Transformer transformer = factory.newTransformer(xsltSource);

        // Load XML directory from resources
        Path outputDirPath = Paths.get(outputDirectory);
        // Remove any existing files inside the output directory before recreating it
        if (Files.exists(outputDirPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(outputDirPath)) {
                for (Path file : stream) {
                    Files.delete(file);
                }
            }
        }
        Files.createDirectories(outputDirPath); // Ensure directory exists

        File xmlDir = new File(inputDirectory);
        if (!xmlDir.exists()) {
            throw new FileNotFoundException("XML resource directory not found: " + inputDirectory);
        }

        File[] xmlFiles = xmlDir.listFiles((dir, name) -> name.endsWith(".xml"));
        if (xmlFiles == null || xmlFiles.length == 0) {
            logger.warn("No XML files found in: {}", inputDirectory);
            return;
        }

        // Process each XML file
        for (File xmlFile : xmlFiles) {
            String outputFileName = xmlFile.getName().replace(".xml", ".json");
            Path outputPath = outputDirPath.resolve(outputFileName);

            // Use try-with-resources to ensure OutputStream is properly closed
            try (OutputStream outputStream = new ByteArrayOutputStream()) {
                transformFile(xmlFile, transformer, outputStream);
                // Convert OutputStream to String
                String jsonString = outputStream.toString();

                // Parse JSON string into CodeList object
                CodeList codeList = mapper.readValue(jsonString, CodeList.class);

                //Save JSON string to outputPath
                mapper.writeValue(outputPath.toFile(), codeList);

                logger.info("Successfully transformed: {} -> {}", xmlFile.getName(), outputPath);
            } catch (Exception e) {
                logger.error("Failed to transform {}: {}", xmlFile.getName(), e.getMessage(), e);
            }
        }

        // Close XSLT stream to avoid resource leaks
        xsltStream.close();
    }

    /**
     * Entry point for manually running the FpML Coding Schemes default transformation.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            new CodeListTransformer().transformDirectory("rosetta-source/src/main/resources/org/isda/codelist/xml", "rosetta-source/src/main/resources/org/isda/codelist/json");
        } catch (Exception e) {
            logger.error("Transformation process failed", e);
        }
    }
}
