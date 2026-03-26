package org.finos.cdm.example.performance;

import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.regnosys.rosetta.common.validation.ValidationReport;
import org.finos.cdm.example.processors.AbstractProcessorTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class contains performance tests for validating and serializing Common Domain Model (CDM) data.
 * It measures performance metrics such as execution time, CPU usage, and memory allocation during
 * serialization, validation, qualification, and workflow processing operations. The results can
 * be used to monitor and optimize performance for CDM-based processes.
 *
 * The class focuses on two primary CDM constructs:
 * 1. TradeState: Represents a product-based view of financial trades.
 * 2. WorkflowStep: Represents event-based workflows, such as trade execution advice.
 *
 * Performance metrics are logged and averaged to help understand bottlenecks and resource usage,
 * providing insights into the computational cost of processing CDM data in various scenarios.
 */
final class ProcessorPerformanceTests extends AbstractProcessorTest {

    // Logger instance for detailed debug information and performance reports
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorPerformanceTests.class);

    // Path to the test sample JSON files for TradeState objects within the JAR resources
    private final String tradeStateTestPackSamples = "ingest/output/fpml-confirmation-to-trade-state";

    // Path to the test sample JSON files for WorkflowStep objects within the JAR resources
    private final String workflowStepTestPackSamples = "ingest/output/fpml-confirmation-to-workflow-step";

    /**
     * Evaluates the performance of serialization, validation, and qualification of TradeState objects.
     *
     * Steps:
     * - Deserializes TradeState objects from JSON files.
     * - Validates the objects against CDM schemas and rules.
     * - Qualifies the objects to determine their semantic category in the model.
     *
     * Performance metrics are collected for each step and aggregated for analysis.
     *
     * @throws IOException If the input resources cannot be located or read.
     */
    @Test
    void tradeStateProcessesPerformanceMetrics() throws IOException, URISyntaxException {

        LOGGER.info("Collecting product-based (TradeState) performance metrics...");
        // Lists to hold performance metrics
        final List<PerformanceMetric> serializationMetrics = new ArrayList<>();
        final List<PerformanceMetric> validationMetrics = new ArrayList<>();
        final List<PerformanceMetric> qualificationMetrics = new ArrayList<>();

        // Locate the resources (sample JSON files) within the project dependencies
        URL url = this.getClass().getClassLoader().getResource(tradeStateTestPackSamples);
        assertNotNull(url, tradeStateTestPackSamples + " should be resolvable through project dependencies");

        // Iterate through each file entry in the specified directory within the jar file
        Files.walk(Path.of(url.toURI()))
                .filter(entry -> entry.toString().endsWith(".json"))
                .forEach(entry -> {
                    // Create builders to track serialization and validation metrics
                    PerformanceMetric.PerformanceMetricBuilder serializationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();
                    PerformanceMetric.PerformanceMetricBuilder validationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();
                    PerformanceMetric.PerformanceMetricBuilder qualificationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();

                    try {

                        // Deserialize the JSON file into a TradeState object
                        File ins = entry.toFile();
                        serializationMetricBuilder.start();
                        TradeState deserialized = resolveReferences(mapper.readValue(ins, TradeState.class));
                        serializationMetricBuilder.end();
                        assertNotNull(deserialized); // Ensure deserialization was successful

                        // Validate the deserialized TradeState object
                        validationMetricBuilder.start();
                        ValidationReport validationReport = validate(deserialized); // Perform validation
                        validationMetricBuilder.end();
                        assertNotNull(validationReport); // Ensure validation report is not null

                        // Qualify the deserialized TradeState object
                        qualificationMetricBuilder.start();
                        QualificationReport qualificationReport = qualify(deserialized.toBuilder());
                        qualificationMetricBuilder.end();
                        assertNotNull(qualificationReport);

                        // Capture and store performance metrics for serialization, validation, and qualification
                        serializationMetrics.add(serializationMetricBuilder.build());
                        validationMetrics.add(validationMetricBuilder.build());
                        qualificationMetrics.add(qualificationMetricBuilder.build());

                    } catch (Exception e) {
                        // Convert checked exceptions to RuntimeException for simplicity
                        LOGGER.error("Could not process sample '{}'", entry, e);
                        //throw new RuntimeException(e);
                    }
                });

        String header = String.format("%n| %-25s | %-25s%n", "Execution time (ms)", "Memory (MB)");
        // Log serialization performance metrics
        debugMetricCollection(":: Serialization Metrics ::", header, serializationMetrics);

        // Log validation performance metrics
        debugMetricCollection(":: Validation Metrics ::", header, validationMetrics);

        // Log qualification performance metrics
        debugMetricCollection(":: Qualification Metrics ::", header, qualificationMetrics);

        // Calculate averages for serialization, validation and qualification metrics
        // Log average metrics
        LOGGER.info(":: Metrics Average from {} TradeState samples in test pack {} ::", serializationMetrics.size(), tradeStateTestPackSamples);
        LOGGER.info(String.format("| %-25s | %-25s | %-25s", "Process", "Execution time (ms)", "Memory (MB)"));
        infoMetricAverage("Deserialization", serializationMetrics);
        infoMetricAverage("Validation", validationMetrics);
        infoMetricAverage("Qualification", qualificationMetrics);
    }

    /**
     * Measures performance metrics for processing WorkflowStep objects, which represent
     * event-based workflows (e.g., execution advice).
     *
     * Steps:
     * - Deserializes WorkflowStep objects from JSON files.
     * - Validates the objects against CDM schemas and rules.
     * - Performs state transitions (e.g., generating follow-up events).
     * - Qualifies the objects to determine their semantic category in the model.
     *
     * Performance metrics for each operation are collected and logged to analyze resource usage
     * and identify optimization opportunities.
     *
     * @throws IOException If the input resources cannot be located or read.
     */
    @Test
    void workflowStepProcessesPerformanceMetrics() throws IOException, URISyntaxException {

        LOGGER.info("Collecting event-based (WofklowStep) performance metrics...");

        // Lists to hold performance metrics
        final List<PerformanceMetric> serializationMetrics = new ArrayList<>();
        final List<PerformanceMetric> validationMetrics = new ArrayList<>();
        final List<PerformanceMetric> qualificationMetrics = new ArrayList<>();
        final List<PerformanceMetric> stateTransitionMetrics = new ArrayList<>();

        // Locate the resources (sample JSON files) within the project dependencies
        URL url = this.getClass().getClassLoader().getResource(workflowStepTestPackSamples);
        assertNotNull(url, workflowStepTestPackSamples + " should be resolvable through project dependencies");


        // Iterate through each file entry in the specified directory within the jar file
        Files.walk(Path.of(url.toURI()))
                .filter(entry -> entry.toString().endsWith(".json"))
                .forEach(entry -> {
                    // Create builders to track performance metrics
                    PerformanceMetric.PerformanceMetricBuilder serializationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();
                    PerformanceMetric.PerformanceMetricBuilder validationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();
                    PerformanceMetric.PerformanceMetricBuilder qualificationMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();
                    PerformanceMetric.PerformanceMetricBuilder stateTransitionMetricBuilder = PerformanceMetric.PerformanceMetricBuilder.newInstance();

                    try {
                        // Deserialize the JSON file into a WorkflowStep object
                        File ins = entry.toFile();
                        serializationMetricBuilder.start();
                        WorkflowStep deserialized = resolveReferences(mapper.readValue(ins, WorkflowStep.class));
                        serializationMetricBuilder.end();
                        assertNotNull(deserialized); // Ensure deserialization was successful

                        // Validate the deserialized WorkflowStep object
                        validationMetricBuilder.start();
                        ValidationReport validationReport = validate(deserialized); // Perform validation
                        validationMetricBuilder.end();
                        assertNotNull(validationReport); // Ensure validation report is not null

                        //BusinessEvent execution
                        stateTransitionMetricBuilder.start();
                        WorkflowStep resultingWorkflowStep = createAcceptedWorkflowStepFromInstructionFunc.evaluate(deserialized);
                        stateTransitionMetricBuilder.end();
                        assertNotNull(resultingWorkflowStep);

                        // Qualify the deserialized WorkflowStep object
                        qualificationMetricBuilder.start();
                        QualificationReport qualificationReport = qualify(resultingWorkflowStep.toBuilder());
                        qualificationMetricBuilder.end();
                        assertNotNull(qualificationReport);

                        // Capture and store performance metrics
                        serializationMetrics.add(serializationMetricBuilder.build());
                        validationMetrics.add(validationMetricBuilder.build());
                        qualificationMetrics.add(qualificationMetricBuilder.build());
                        stateTransitionMetrics.add(stateTransitionMetricBuilder.build());

                    } catch (Exception e) {
                        // Convert checked exceptions to RuntimeException for simplicity
                        throw new RuntimeException(e);
                    }
                });

        String header = String.format("%n| %-25s | %-25s%n", "Execution time (ms)", "Memory (MB)");
        // Log serialization performance metrics
        debugMetricCollection(":: Serialization Metrics ::", header, serializationMetrics);

        // Log validation performance metrics
        debugMetricCollection(":: Validation Metrics ::", header, validationMetrics);

        // Log state transition performance metrics
        debugMetricCollection(":: BusinessEvent execution Metrics ::", header, stateTransitionMetrics);

        // Log qualification performance metrics
        debugMetricCollection(":: BusinessEvent qualification Metrics ::", header, qualificationMetrics);

        // Calculate averages for performance metrics
        // Log average metrics
        LOGGER.info(":: Metrics Average from {} WorkflowStep samples in test pack {} ::", serializationMetrics.size(), workflowStepTestPackSamples);
        LOGGER.info(String.format("| %-25s | %-25s | %-25s", "Process", "Execution time (ms)", "Memory (MB)"));
        infoMetricAverage("Deserialization", serializationMetrics);
        infoMetricAverage("Validation", validationMetrics);
        infoMetricAverage("Event execution", stateTransitionMetrics);
        infoMetricAverage("Qualification", qualificationMetrics);
    }

    private static void debugMetricCollection (String title, String header, List<PerformanceMetric> metrics) {
        LOGGER.debug(title);
        LOGGER.debug(header.concat(metrics.stream()
                .map(PerformanceMetric::toString)
                .collect(Collectors.joining("\n"))));
    }

    private static void infoMetricAverage (String processLabel, List<PerformanceMetric> metrics) {
        double avgExecutionTime = metrics.stream().mapToLong(PerformanceMetric::getExecutionTime).average().orElse(0);
        double avgMemory = metrics.stream().mapToLong(PerformanceMetric::getMemoryAllocation).average().orElse(0);
        LOGGER.info(String.format("| %-25s %s",
                processLabel,
                PerformanceMetric.toString(avgExecutionTime, avgMemory)
                )
        );
    }


}
