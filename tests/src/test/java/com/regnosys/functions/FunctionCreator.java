package com.regnosys.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.ingest.test.framework.ingestor.ExpectationUtil;
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.testing.ExecutionDescriptor;
import com.regnosys.rosetta.common.testing.FunctionRunner;
import com.rosetta.model.lib.process.PostProcessor;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.params.provider.Arguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.regnosys.rosetta.common.util.ClassPathUtils.loadFromClasspath;
import static com.regnosys.rosetta.common.util.UrlUtils.toUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionCreator.class);

    private static final ObjectMapper ROSETTA_OBJECT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper();

    private static final List<String> EXECUTION_DESCRIPTOR_PATHS = List.of(
            "functions/execution-descriptor.json",
            "functions/sec-lending-execution-descriptor.json",
            "functions/fpml-processes-execution-descriptor.json",
            "functions/cme-cleared-confirm-1-17-execution-descriptor.json",
            "functions/dtcc-11-0-execution-descriptor.json",
            "functions/repo-and-bond-execution-descriptor.json");

    private static Injector injector;

    public static void main(String[] args) {
        try {
            FunctionCreator secLendingFunctionInputCreator = new FunctionCreator();
            secLendingFunctionInputCreator.run();

            System.exit(0);
        } catch (Exception e) {
            LOGGER.error("Error executing {}.main()", FunctionCreator.class.getName(), e);
            System.exit(1);
        }
    }

    public void run() throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        Module module = Modules.override(new CdmRuntimeModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(PostProcessor.class).to(WorkflowPostProcessor.class);
                    }
                });
        injector = Guice.createInjector(module);
        List<ExecutionDescriptor> descriptors = loadExecutionDescriptors()
                .map(arg -> (ExecutionDescriptor) arg.get()[2])
                .collect(Collectors.toList());

        for (ExecutionDescriptor descriptor : descriptors) {
            runFunction(descriptor.getGroup(), descriptor.getName(), descriptor);
        }
    }

    private static Stream<Arguments> loadExecutionDescriptors() {
        return EXECUTION_DESCRIPTOR_PATHS.stream().flatMap(x -> loadFromClasspath(x, FunctionCreator.class.getClassLoader()))
                .map(path -> ExecutionDescriptor.loadExecutionDescriptor(ROSETTA_OBJECT_MAPPER, toUrl(path)))
                .flatMap(Collection::stream)
                .map(executionDescriptor -> Arguments.of(executionDescriptor.getGroup(), executionDescriptor.getName(), executionDescriptor));
    }

    public void runFunction(String groupName, String testName, ExecutionDescriptor executionDescriptor) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException {
        LOGGER.info("Running Test: " + groupName + ":" + testName);
        FunctionRunner functionRunner = new FunctionRunner(executionDescriptor,
                injector::getInstance,
                this.getClass().getClassLoader(),
                ROSETTA_OBJECT_MAPPER);
        FunctionRunner.FunctionRunnerResult<Object, Object> run = functionRunner.run();
        if (!run.isSuccess()) {
            writeTestOutput(executionDescriptor, run);
        }
    }

    private void writeTestOutput(ExecutionDescriptor executionDescriptor, FunctionRunner.FunctionRunnerResult<Object, Object> run) throws IOException {
        // Add environment variable TEST_WRITE_BASE_PATH to override the base write path, e.g.
        // TEST_WRITE_BASE_PATH=/Users/hugohills/code/src/github.com/REGnosys/rosetta-cdm/src/main/resources/
        Path path = ExpectationUtil.TEST_WRITE_BASE_PATH
                .filter(Files::exists)
                .orElse(Path.of("func-output"));
        Path actualOutputFile = path.resolve(executionDescriptor.getExpectedOutputFile());
        Files.createDirectories(actualOutputFile.getParent());
        Files.write(actualOutputFile, run.getJsonActual().getBytes());
        LOGGER.info("Wrote test output to {}", actualOutputFile.toAbsolutePath());
    }
}
