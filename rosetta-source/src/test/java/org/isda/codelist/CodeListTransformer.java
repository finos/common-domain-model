package org.isda.codelist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.file.*;

/**
 * This class transforms `https://docs.oasis-open.org/codelist/ns/genericode/1.0/` codelist XML files to JSON-serialized CDM format using an XSLT script.
 * The transformation applies to all XML files in a specified directory.
 * Results are stored in the specified output directory.
 */
public class CodeListTransformer {

    private static final Logger logger = LoggerFactory.getLogger(CodeListTransformer.class);

    /** Path to the XSLT transformation script located in resources. */
    private static final String XSLT_RESOURCE = "/org/isda/codelist/codelist2cdmjson.xsl";

    /**
     * Transforms a single XML file using the provided Transformer and writes the output to the specified OutputStream.
     *
     * @param xmlFile      XML file to transform.
     * @param transformer  Pre-configured Transformer instance.
     * @param outputStream Output stream where the transformed JSON is written.
     * @throws TransformerException If an error occurs during transformation.
     */
    public static void transformFile(File xmlFile, Transformer transformer, OutputStream outputStream) throws TransformerException {
        StreamSource xmlSource = new StreamSource(xmlFile); // Load XML file as a source
        Result outputTarget = new StreamResult(outputStream);
        transformer.transform(xmlSource, outputTarget);
    }

    /**
     * Transforms all XML files in the given input directory and saves them as JSON in the output directory.
     * This method initializes a single Transformer instance to process multiple files.
     *
     * @param inputDirectory  Directory containing XML files (resource path).
     * @param outputDirectory Directory where JSON files will be saved.
     * @throws Exception If an error occurs during transformation.
     */
    public static void transformDirectory(String inputDirectory, String outputDirectory) throws Exception {

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
        ClassLoader classLoader = CodeListTransformer.class.getClassLoader();
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

        URL xmlDirURL = classLoader.getResource(inputDirectory);
        if (xmlDirURL == null) {
            throw new FileNotFoundException("XML resource directory not found: " + inputDirectory);
        }

        File xmlDir = new File(xmlDirURL.toURI());
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
            try (OutputStream outputStream = Files.newOutputStream(outputPath)) {
                transformFile(xmlFile, transformer, outputStream);
                logger.info("Successfully transformed: {} -> {}", xmlFile.getName(), outputPath);
            } catch (Exception e) {
                logger.error("Failed to transform {}: {}", xmlFile.getName(), e.getMessage(), e);
            }
        }

        // Close XSLT stream to avoid resource leaks
        xsltStream.close();
    }

    /**
     * Entry point for manually running the transformation.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            transformDirectory("org/isda/codelist/xml", "rosetta-source/src/main/resources/org/isda/codelist/json");
        } catch (Exception e) {
            logger.error("Transformation process failed", e);
        }
    }
}
