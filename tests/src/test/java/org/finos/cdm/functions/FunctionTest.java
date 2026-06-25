package org.finos.cdm.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.rosetta.common.postprocess.WorkflowPostProcessor;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.testing.ExecutionDescriptor;
import com.regnosys.rosetta.common.testing.FunctionRunner;
import com.rosetta.model.lib.process.PostProcessor;
import org.finos.cdm.CdmRuntimeModule;
import org.finos.rune.mapper.RuneJsonObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.stream.Stream;

import static com.regnosys.rosetta.common.util.ClassPathUtils.loadFromClasspath;
import static com.regnosys.rosetta.common.util.UrlUtils.toUrl;
import static org.finos.cdm.functions.FunctionCreator.EXECUTION_DESCRIPTOR_PATHS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionTest.class);

    private static final ObjectMapper ROSETTA_OBJECT_MAPPER = RosettaObjectMapper.getNewRosettaObjectMapper();

    private static Injector injector;

    @BeforeAll
    static void setup() {
        Module module = Modules.override(new CdmRuntimeModule())
                .with(new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(PostProcessor.class).to(WorkflowPostProcessor.class);
                    }
                });
        injector = Guice.createInjector(module);
    }

    private static Stream<Arguments> loadExecutionDescriptors() {
        return EXECUTION_DESCRIPTOR_PATHS.stream().flatMap(x -> loadFromClasspath(x, FunctionTest.class.getClassLoader()))
                .map(path -> ExecutionDescriptor.loadExecutionDescriptor(ROSETTA_OBJECT_MAPPER, toUrl(path)))
                .flatMap(Collection::stream)
                .map(executionDescriptor -> Arguments.of(executionDescriptor.getGroup(), executionDescriptor.getName(), executionDescriptor));
    }

    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("loadExecutionDescriptors")
    void runFunction(String groupName, String testName, ExecutionDescriptor executionDescriptor) throws ClassNotFoundException, IOException, InvocationTargetException, IllegalAccessException {
        LOGGER.info("Running Test: " + groupName + ":" + testName);
        FunctionRunner functionRunner = new FunctionRunner(executionDescriptor,
                injector::getInstance,
                this.getClass().getClassLoader(),
                new RuneJsonObjectMapper());
        FunctionRunner.FunctionRunnerResult<Object, Object> run = functionRunner.run();
        assertEquals(run.getJsonExpected().replace("\r", ""), run.getJsonActual().replace("\r", ""));
    }
}
