package org.finos.cdm.ingest.diagnostics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.transform.TransformType;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.RegPaths.directoryNameOfDataset;
import static com.regnosys.testing.TestingExpectationUtil.TEST_WRITE_BASE_PATH;
import static org.finos.cdm.testpack.CdmTestPackCreator.EVENT_TEST_PACKS;

public class IngestUtils {

    static final Boolean WRITE_EXPECTATIONS = Optional.ofNullable(System.getenv("WRITE_EXPECTATIONS"))
            .map(Boolean::parseBoolean)
            .orElse(false);
    static final ObjectMapper OBJECT_MAPPER = RosettaObjectMapper.getNewMinimalRosettaObjectMapper();

    static final Path PROJECT_ROOT = Path.of("").toAbsolutePath().getParent();
    static final Path MAIN_RESOURCES_PATH = TEST_WRITE_BASE_PATH.orElseThrow();
    static final Path INGEST_OUTPUT_PATH = MAIN_RESOURCES_PATH.resolve(TransformType.TRANSLATE.getResourcePath()).resolve("output");
    static final Path SYNONYM_INGEST_OUTPUT_BASE_PATH = MAIN_RESOURCES_PATH.resolve("result-json-files");
    // synonym ingest folder structure - top level folders
    static final List<String> FPML_GROUPS = Arrays.asList("fpml-5-10", "fpml-5-12", "fpml-5-13"); //"native-cdm-events"

    static boolean isFpmlTestPack(Path synonymIngestOutputPath) {
        return FPML_GROUPS.stream().anyMatch(x -> String.format("/%s/", synonymIngestOutputPath.toString()).contains(x));
    }

    static boolean isJsonExt(Path synonymIngestOutputPath) {
        return synonymIngestOutputPath.getFileName().toString().endsWith(".json");
    }

    static String toTestPackName(String synonymIngestOutputPath) {
        String testPackName = synonymIngestOutputPath.replaceAll("[-._/]+", " ");
        return directoryNameOfDataset(testPackName);
    }

    static Path getIngestOutputPath(String testPackName, Path fileName) {
        String functionFolder = EVENT_TEST_PACKS.contains(testPackName) ?
                "fpml-confirmation-to-workflow-step" : "fpml-confirmation-to-trade-state";
        return INGEST_OUTPUT_PATH.resolve(functionFolder).resolve(testPackName).resolve(fileName);
    }

    static String getTestName(String testPackName, Path fileName) {
        return String.format("%s | %s", testPackName, fileName.toString().replace(".json", ""));
    }
}
