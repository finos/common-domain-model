package com.regnosys.granite.ingestor.workflows;

import cdm.event.common.TradeState;
import cdm.event.workflow.Workflow;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.granite.ingestor.IngestionReport;
import com.regnosys.granite.ingestor.postprocess.WorkflowPostProcessRunner;
import com.regnosys.granite.ingestor.postprocess.pathduplicates.PathCollector;
import com.regnosys.granite.ingestor.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.granite.ingestor.service.IngestionFactory;
import com.regnosys.granite.ingestor.service.IngestionService;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.regnosys.rosetta.common.hashing.ReferenceConfig;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.validation.ModelObjectValidator;
import org.isda.cdm.CdmRuntimeModule;
import org.isda.cdm.processor.EventEffectProcessStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.regnosys.rosetta.common.util.ClassPathUtils.findPathsFromClassPath;

public class CdmWorkflowTest {
	private static final String SAMPLE_FILES_DIR = "cdm-sample-files/workflows/";
	private static IngestionService ingestionService;
	private static Injector injector;
	private static List<String> WORKFLOWS_TO_TEST = List.of("clearing-accepted", "clearing-rejected");
	private static String WORKFLOW_NS = "org.isda.cdm.workflows.";
	private static ObjectMapper defaultRosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();

	@BeforeAll
	static void setup() {
		Module module = Modules.override(new CdmRuntimeModule())
			.with(new AbstractModule() {
				@Override
				protected void configure() {
					bind(PostProcessor.class).to(WorkflowPostProcessRunner.class);
					bind(ModelObjectValidator.class).to(NoOpValidator.class);

				}
			});
		injector = Guice.createInjector(module);

		initialiseIngestionFactory();
		ingestionService = IngestionFactory.getInstance().getFpml510EventsAndBundles();

	}

	@ParameterizedTest(name = "Workflow test {2}")
	@MethodSource("ingestedSource")
	public void runWorkflow(TradeState ingested, Optional<Workflow> expected, String name) throws ClassNotFoundException, JsonProcessingException {
		Class<Function<TradeState, Workflow>> workflowFunctionClass = loadWorkflowFunctionClass(name);
		Function<TradeState, Workflow> workflowFunction = injector.getInstance(workflowFunctionClass);
		Workflow workflow = workflowFunction.apply(ingested);
		String jsonActual = defaultRosettaObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(workflow);
		Assertions.assertEquals(expected(expected), jsonActual);
	}

	private static String expected(Optional<Workflow> expected) throws JsonProcessingException {
		if (expected.isPresent()) {
			return defaultRosettaObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected.get());
		} else {
			return "No input json found";
		}
	}

	private static Stream<Arguments> ingestedSource() {
		return WORKFLOWS_TO_TEST.stream().flatMap(CdmWorkflowTest::ingestArguments);
	}

	private static Stream<Arguments> ingestArguments(String workflowToTest) {
		return findPathsFromClassPath(List.of(SAMPLE_FILES_DIR + workflowToTest), ".*\\.xml", Optional.empty(), CdmWorkflowTest.class.getClassLoader())
			.stream()
			.map(contractPath -> {
				IngestionReport<TradeState> contractIngestionReport = ingestionService.ingestValidateAndPostProcess(TradeState.class, reader(contractPath));
				TradeState tradeState = contractIngestionReport.getRosettaModelInstance();
				List<Path> expectedPaths = findPathsFromClassPath(List.of(SAMPLE_FILES_DIR + workflowToTest),
					".*" + contractPath.getFileName().toString().replace(".xml", ".json"),
					Optional.empty(),
					CdmWorkflowTest.class.getClassLoader());
				Optional<Workflow> workflow = readWorkflow(expectedPaths);
				return Arguments.of(tradeState, workflow, workflowToTest);
			});
	}

	private static Optional<Workflow> readWorkflow(List<Path> expectedPaths) {
		if (expectedPaths.size() > 0) {
			try {
				URL expectedUrl = getOnlyElement(expectedPaths).toUri().toURL();
				return Optional.of(defaultRosettaObjectMapper.readValue(expectedUrl, Workflow.class));
			} catch (IOException e) {
				throw new UncheckedIOException("Could not read workflow from "+expectedPaths.get(0), e);
			}
		}
		return Optional.empty();
	}

	private static Reader reader(Path path) {
		try {
			return new InputStreamReader(ClassPathUtils.toUrl(path).openStream());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<Function<TradeState, Workflow>> loadWorkflowFunctionClass(String name) throws ClassNotFoundException {
		return (Class<Function<TradeState, Workflow>>)
			CdmWorkflowTest.class.getClassLoader().loadClass(workflowNameToClassName(name));
	}

	private String workflowNameToClassName(String name) {
		return WORKFLOW_NS + CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, name);
	}

	private static void initialiseIngestionFactory() {
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		IngestionFactory.init(
			injector.getInstance(ReferenceConfig.class),
			globalKeyProcessStep,
			new ReKeyProcessStep(globalKeyProcessStep),
			new EventEffectProcessStep(globalKeyProcessStep),
			injector.getInstance(QualifyProcessorStep.class),
			new PathCollector<>(),
			injector.getInstance(RosettaTypeValidator.class));
	}


	private static class NoOpValidator implements ModelObjectValidator {

		@Override
		public <T extends RosettaModelObject> void validateAndFailOnErorr(Class<T> topClass, T modelObject) {
		}

		@Override
		public <T extends RosettaModelObject> void validateAndFailOnErorr(Class<T> topClass, List<? extends T> modelObjects) {
		}
	}

}
