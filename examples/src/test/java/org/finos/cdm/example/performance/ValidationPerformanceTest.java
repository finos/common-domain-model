package org.finos.cdm.example.performance;

import cdm.event.common.TradeState;
import com.regnosys.rosetta.common.validation.ValidationReport;
import org.finos.cdm.example.processors.AbstractProcessorTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Performance test to measure the efficiency of serialization and validation
 * of CDM (Common Domain Model) samples.
 * It tracks execution time, CPU time, and memory allocation.
 */
public class ValidationPerformanceTest extends AbstractProcessorTest {

    // Logger instance for debugging and reporting metrics
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationPerformanceTest.class);

    // Directory containing test sample JSON files in the jar resource
    private final String cdmTestPackSamples = "result-json-files/fpml-5-13/products";

    /**
     * Test method to validate the performance of serialization and validation of CDM samples.
     * This evaluates execution time, CPU time, and memory usage for each operation.
     *
     * @throws IOException When input resources cannot be resolved
     */
    @Test
    public void validateCdmSamplesPerformance() throws IOException {
        // Lists to hold performance metrics for serialization and validation
        final List<PerformanceMetric> serializationMetrics = new ArrayList<>();
        final List<PerformanceMetric> validationMetrics = new ArrayList<>();

        // Locate the resources (sample JSON files) within the project dependencies
        URL url = this.getClass().getClassLoader().getResource(cdmTestPackSamples);
        assertNotNull(url, cdmTestPackSamples + " should be resolvable through project dependencies");

        // Extract the jar file path from the resource URL
        String[] parts = url.getPath().split("!");
        String jarPath = parts[0].substring(parts[0].indexOf("file:") + 5);
        JarFile jarFile = new JarFile(jarPath);

        // Iterate through each file entry in the specified directory within the jar file
        jarFile.stream()
                .filter(entry -> entry.getName().startsWith(cdmTestPackSamples) && entry.getName().endsWith(".json"))
                .forEach(entry -> {
                    // Create builders to track serialization and validation metrics
                    PerformanceMetric.PerformanceMetricBuilder serializationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();
                    PerformanceMetric.PerformanceMetricBuilder validationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();

                    try {
                        // Deserialize the JSON file into a TradeState object
                        InputStream ins = jarFile.getInputStream(entry);
                        serializationMetricBuilder.start();
                        TradeState deserialized = mapper.readValue(ins, TradeState.class);
                        serializationMetricBuilder.end();
                        assertNotNull(deserialized); // Ensure deserialization was successful

                        // Validate the deserialized TradeState object
                        validationMetricBuilder.start();
                        ValidationReport result = validate(deserialized); // Perform validation
                        validationMetricBuilder.end();
                        assertNotNull(result); // Ensure validation report is not null

                        // Capture and store performance metrics for serialization and validation
                        PerformanceMetric serializationMetric = serializationMetricBuilder.build();
                        PerformanceMetric validationMetric = validationMetricBuilder.build();
                        serializationMetrics.add(serializationMetric);
                        validationMetrics.add(validationMetric);

                    } catch (Exception e) {
                        // Convert checked exceptions to RuntimeException for simplicity
                        throw new RuntimeException(e);
                    }
                });

        // Log serialization performance metrics
        String header = String.format("\n| %-25s | %-25s | %-25s\n", "Execution time (ms)", "CPU time (ms)", "Memory (MB)");
        LOGGER.debug(":: Serialization Metrics ::");
        LOGGER.debug(header.concat(serializationMetrics.stream()
                .map(it -> String.format("| %-25s | %-25s | %-25s",
                        it.getExecutionTime() / 1_000_000.0,
                        it.getCpuTime() / 1_000_000.0,
                        it.getMemoryAllocation() / 1_000_000.0))
                .collect(Collectors.joining("\n"))));

        // Log validation performance metrics
        LOGGER.debug(":: Validation Metrics ::");
        LOGGER.debug(header.concat(validationMetrics.stream()
                .map(it -> String.format("| %-25s | %-25s | %-25s",
                        it.getExecutionTime() / 1_000_000.0,
                        it.getCpuTime() / 1_000_000.0,
                        it.getMemoryAllocation() / 1_000_000.0))
                .collect(Collectors.joining("\n"))));

        // Calculate averages for serialization and validation metrics
        double avgSerializationExecutionTime = serializationMetrics.stream().mapToLong(PerformanceMetric::getExecutionTime).average().orElse(0);
        double avgSerializationCpuTime = serializationMetrics.stream().mapToLong(PerformanceMetric::getCpuTime).average().orElse(0);
        double avgSerializationMemory = serializationMetrics.stream().mapToLong(PerformanceMetric::getMemoryAllocation).average().orElse(0);

        double avgValidationExecutionTime = validationMetrics.stream().mapToLong(PerformanceMetric::getExecutionTime).average().orElse(0);
        double avgValidationCpuTime = validationMetrics.stream().mapToLong(PerformanceMetric::getCpuTime).average().orElse(0);
        double avgValidationMemory = validationMetrics.stream().mapToLong(PerformanceMetric::getMemoryAllocation).average().orElse(0);

        // Log average metrics
        LOGGER.info(":: Metrics Average ::");
        LOGGER.info(String.format("| %-25s | %-25s | %-25s | %-25s", "Process", "Execution time (ms)", "CPU time (ms)", "Memory (MB)"));
        LOGGER.info(String.format("| %-25s | %-25s | %-25s | %-25s",
                "Validation",
                avgValidationExecutionTime / 1_000_000.0,
                avgValidationCpuTime / 1_000_000.0,
                avgValidationMemory / 1_000_000.0));
        LOGGER.info(String.format("| %-25s | %-25s | %-25s | %-25s",
                "Deserialization",
                avgSerializationExecutionTime / 1_000_000.0,
                avgSerializationCpuTime / 1_000_000.0,
                avgSerializationMemory / 1_000_000.0));
    }
}
