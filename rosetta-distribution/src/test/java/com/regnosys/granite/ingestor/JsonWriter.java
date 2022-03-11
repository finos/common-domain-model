package com.regnosys.granite.ingestor;

import cdm.event.common.TradeState;
import cdm.legalagreement.common.LegalAgreement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.regnosys.granite.ingestor.postprocess.pathduplicates.PathCollector;
import com.regnosys.granite.ingestor.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.granite.ingestor.service.IngestionFactory;
import com.regnosys.granite.ingestor.service.IngestionService;
import com.regnosys.granite.ingestor.testing.Expectation;
import com.regnosys.rosetta.common.hashing.*;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.RosettaModelObject;
import org.isda.cdm.CdmRuntimeModule;
import org.isda.cdm.processor.EventEffectProcessStep;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Replicates the {@link ObjectMapper} used in the rosetta-compass-service for writing {@link RosettaModelObject}s
 */
public class JsonWriter {

	@Inject RosettaTypeValidator validator;
	@Inject QualifyProcessorStep qualifyProcessorStep;

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriter.class);
	private static final String SAMPLES_DIR = "cdm-sample-files/";

	public static void main(String[] args) throws URISyntaxException, IOException {
		new JsonWriter().init(args);
	}

	public void init(String[] args) throws IOException, URISyntaxException {
		String basePath = Arrays.stream(args).findFirst().orElse("");
		LOGGER.info("Base path {}", basePath);
		// Guice Injection
		Injector injector = Guice.createInjector(new CdmRuntimeModule());
		injector.injectMembers(this);

		String outputPath = Arrays.stream(args).findFirst().orElse("");
		LOGGER.info("Base path {}", outputPath);

		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);

		IngestionFactory.init(
			injector.getInstance(ReferenceConfig.class),
			globalKeyProcessStep,
				new ReKeyProcessStep(globalKeyProcessStep),
				new ReferenceResolverProcessStep(injector.getInstance(ReferenceConfig.class)),
				new EventEffectProcessStep(globalKeyProcessStep),
				qualifyProcessorStep,
				new PathCollector<>(),
				validator);

		IngestionFactory factory = IngestionFactory.getInstance();
		IngestionService service = factory.getFpml510EventsAndBundles();
//		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "events"), Paths.get(outputPath + "events"), WorkflowStep.class);
//		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "invalid-events"), Paths.get(outputPath + "invalid-events"), WorkflowStep.class);
		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/products/commodity"), Paths.get(outputPath + "fpml-5-10/products/commodity"), TradeState.class);
		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/products/credit"), Paths.get(outputPath + "fpml-5-10/products/credit"), TradeState.class);
		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/products/equity"), Paths.get(outputPath + "fpml-5-10/products/equity"), TradeState.class);
		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/products/fx"), Paths.get(outputPath + "fpml-5-10/products/fx"), TradeState.class);
		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/products/rates"), Paths.get(outputPath + "fpml-5-10/products/rates"), TradeState.class);
		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/products/repo"), Paths.get(outputPath + "fpml-5-10/products/repo"), TradeState.class);
//		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/invalid-products"), Paths.get(outputPath + "fpml-5-10/invalid-products"), TradeState.class);
//		xmlPathToJson(service, loadFromClasspath(SAMPLES_DIR + "bundles"), Paths.get(outputPath + "bundles"), EventTestBundle.class);

//		IngestionService fpml512Service = factory.getFpml512();
//		xmlPathToJson(fpml512Service, loadFromClasspath(SAMPLES_DIR + "fpml-5-12/products/commodity"), Paths.get(outputPath + "fpml-5-12/products/commodity"), TradeState.class);
//		xmlPathToJson(fpml512Service, loadFromClasspath(SAMPLES_DIR + "fpml-5-12/products/credit"), Paths.get(outputPath + "fpml-5-12/products/credit"), TradeState.class);
//		xmlPathToJson(fpml512Service, loadFromClasspath(SAMPLES_DIR + "fpml-5-12/products/equity"), Paths.get(outputPath + "fpml-5-12/products/equity"), TradeState.class);
//		xmlPathToJson(fpml512Service, loadFromClasspath(SAMPLES_DIR + "fpml-5-12/products/fx"), Paths.get(outputPath + "fpml-5-12/products/fx"), TradeState.class);
//		xmlPathToJson(fpml512Service, loadFromClasspath(SAMPLES_DIR + "fpml-5-12/products/rates"), Paths.get(outputPath + "fpml-5-12/products/rates"), TradeState.class);
//		xmlPathToJson(fpml512Service, loadFromClasspath(SAMPLES_DIR + "fpml-5-12/products/repo"), Paths.get(outputPath + "fpml-5-12/products/repo"), TradeState.class);

		isdaCreatePathToJson(outputPath, factory.getIsdaCreateAll(), XMLSchema.ISDA_CREATE_ALL);

//		IngestionService cmeClearedService = factory.getCmeCleared117();
//		xmlPathToJson(cmeClearedService, loadFromClasspath(SAMPLES_DIR + "cme-cleared-confirm-1-17"), Paths.get(outputPath + "cme-cleared-confirm-1-17"), WorkflowStep.class);
//
//		IngestionService cmeSubmissionService = factory.getCmeSubmissionIrs1();
//		xmlPathToJson(cmeSubmissionService, loadFromClasspath(SAMPLES_DIR + "cme-submission-irs-1-0"), Paths.get(outputPath + "cme-submission-irs-1-0"), WorkflowStep.class);
//
//		IngestionService dtcc11Service = factory.getDtcc11();
//		xmlPathToJson(dtcc11Service, loadFromClasspath(SAMPLES_DIR + "dtcc-11-0"), Paths.get(outputPath + "dtcc-11-0"), WorkflowStep.class);
//
//		IngestionService dtcc9Service = factory.getDtcc9();
//		xmlPathToJson(dtcc9Service, loadFromClasspath(SAMPLES_DIR + "dtcc-9-0"), Paths.get(outputPath + "dtcc-9-0"), WorkflowStep.class);

//		IngestionService fpml510RecordKeepingService = factory.getFpml510RecordKeeping();
//		xmlPathToJson(fpml510RecordKeepingService, loadFromClasspath(SAMPLES_DIR + "fpml-5-10/record-keeping"), Paths.get(outputPath + "fpml-5-10/record-keeping"), WorkflowStep.class);
//
//		IngestionService oreService = factory.getOre1039();
//		xmlPathToJson(oreService, loadFromClasspath(SAMPLES_DIR + "ore-1-0-39"), Paths.get(outputPath + "ore-1-0-39"), TradeState.class);
	}

	private static Path loadFromClasspath(String path) throws URISyntaxException, IOException {
		URL resource = TradeState.class.getClassLoader().getResource(path);
		if (resource.toURI().getScheme().equals("jar")) {
			try {
				FileSystems.getFileSystem(resource.toURI());
			} catch (FileSystemNotFoundException e) {
				FileSystems.newFileSystem(resource.toURI(), Collections.emptyMap());
			}
		}
		return Paths.get(resource.toURI());
	}

	private static <T extends RosettaModelObject> void xmlPathToJson(IngestionService service, Path inFolder, Path outFolder, Class<T> ingestType) throws IOException {
		List<Path> xmlPaths = Files.list(inFolder)
				.filter(f -> toFileName(f).endsWith("xml"))
				.collect(Collectors.toList());
		for (Path xmlPath : xmlPaths) {
			LOGGER.debug("xmlPathToJson: {}", toFileName(xmlPath));
			IngestionReport<T> ingest = service.ingestValidateAndPostProcess(ingestType, new InputStreamReader(xmlPath.toUri().toURL().openStream()));
			writeFileToDisk(outFolder, xmlPath.getFileName().toString(), ingest.getRosettaModelInstance(), f -> f.replace(".xml", ".json"));
		}
	}

	private void isdaCreatePathToJson(String outputPath, IngestionService ingestionService, XMLSchema xmlSchema) throws IOException, URISyntaxException {
		for(String location : xmlSchema.getSampleFilesLocation()) {
			isdaCreatePathToJson(ingestionService,
				loadFromClasspath(SAMPLES_DIR + location),
				outputPath,
				LegalAgreement.class);
		}

	}

	private static <T extends RosettaModelObject> void isdaCreatePathToJson(IngestionService service, Path inFolder, String outputPath, Class<T> ingestType) throws IOException {
		List<String> isdaCreateInputJsonPaths = getInputPathsFromExpectationsFile(inFolder);
		for (String isdaCreateInputJsonPath : isdaCreateInputJsonPaths) {
			URL inFile = Resources.getResource(isdaCreateInputJsonPath);
			LOGGER.debug("isdaCreateInputJsonPath: {}", inFile.getPath());
			IngestionReport<T> ingest = service.ingestAndPostProcessJson(ingestType, new InputStreamReader(inFile.openStream()));
			// Convert input path to output path by removing the cdm-sample-files folder and appending it on to the output path (e.g. target/result-json-files)
			Path outFile = Paths.get(outputPath).resolve(isdaCreateInputJsonPath.substring(SAMPLES_DIR.length()));
			writeFileToDisk(outFile.getParent(), toFileName(outFile), ingest.getRosettaModelInstance(), f -> f.replace(".json", "_cdm.json"));
		}
	}

	@NotNull
	private static List<String> getInputPathsFromExpectationsFile(Path inFolder) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Expectation>> type = new TypeReference<List<Expectation>>() {};
		List<Expectation> expectations = mapper.readValue(Files.readAllBytes(inFolder.resolve("expectations.json")), type);
		return expectations.stream().map(Expectation::getFileName).collect(Collectors.toList());
	}

	private static String toFileName(Path f) {
		return f.getFileName().toString().toLowerCase();
	}

	/**
	 * @param folder creates the folder if it doesn't already exist
	 */
	private static void writeFileToDisk(Path folder, String filename, RosettaModelObject object, Function<String, String> renameFunc) throws IOException {
		Path path = folder.resolve(renameFunc.apply(filename));
		Files.createDirectories(path.getParent());
		LOGGER.debug("Writing path {}", path);

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(toJson(object));
		}
	}

	static String toJson(Object object) throws JsonProcessingException {
		return RosettaObjectMapper.getDefaultRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}
}
