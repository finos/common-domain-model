package com.regnosys.granite.ingestor.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.granite.ingestor.postprocess.WorkflowPostProcessRunner;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.testing.ExecutionDescriptor;
import com.regnosys.rosetta.common.testing.FunctionRunner;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.validation.ModelObjectValidator;
import org.isda.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static com.regnosys.rosetta.common.util.ClassPathUtils.loadFromClasspath;
import static com.regnosys.rosetta.common.util.ClassPathUtils.toUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SecLendingFunctionTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecLendingFunctionTest.class);

    private static final ObjectMapper ROSETTA_OBJECT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper();
    private static final String EXECUTION_DESCRIPTOR_PATH = "cdm-sample-files/functions/sec-lending-execution-descriptor.json";
    private static Injector injector;

    @BeforeAll
    static void setup() {
        Module module = Modules.override(new CdmRuntimeModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(PostProcessor.class).to(WorkflowPostProcessRunner.class);
                        bind(ModelObjectValidator.class).toInstance(Mockito.mock(ModelObjectValidator.class));
                    }
                });
        injector = Guice.createInjector(module);
    }

    private static Stream<Arguments> loadExecutionDescriptors() {
        return loadFromClasspath(EXECUTION_DESCRIPTOR_PATH, SecLendingFunctionTest.class.getClassLoader())
                .map(path -> ExecutionDescriptor.loadExecutionDescriptor(ROSETTA_OBJECT_MAPPER, toUrl(path)))
                .flatMap(Collection::stream)
                .map(executionDescriptor -> Arguments
                        .of(executionDescriptor.getGroup(), executionDescriptor.getName(), executionDescriptor));
    }

    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("loadExecutionDescriptors")
    void runFunction(@VisibleForTesting String groupName, @VisibleForTesting String testName, ExecutionDescriptor executionDescriptor) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException {
        LOGGER.info("Running Test: " + groupName + ":" + testName);
        FunctionRunner functionRunner = new FunctionRunner(executionDescriptor,
                injector::getInstance,
                this.getClass().getClassLoader(),
                ROSETTA_OBJECT_MAPPER);
        FunctionRunner.FunctionRunnerResult<Object, Object> run = functionRunner.run();
        if (!run.isSuccess()) {
            assertEquals(Optional.ofNullable(run.getJsonExpected()).map(s -> s.replace("\r", "")).orElse(null), run
                    .getJsonActual().replace("\r", ""));
        }
    }

}
