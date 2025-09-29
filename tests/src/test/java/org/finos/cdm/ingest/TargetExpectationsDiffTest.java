package org.finos.cdm.ingest;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;
import com.github.difflib.unifieddiff.UnifiedDiff;
import com.github.difflib.unifieddiff.UnifiedDiffFile;
import com.github.difflib.unifieddiff.UnifiedDiffWriter;
import com.regnosys.rosetta.common.transform.TransformType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.regnosys.rosetta.common.RegPaths.directoryNameOfDataset;
import static org.finos.cdm.testpack.CdmTestPackCreator.EVENT_TEST_PACKS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class TargetExpectationsDiffTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TargetExpectationsDiffTest.class);
    private static final Boolean WRITE_EXPECTATIONS = Optional.ofNullable(System.getenv("WRITE_EXPECTATIONS"))
            .map(Boolean::parseBoolean)
            .orElse(false);

    private static final Path PROJECT_ROOT = Path.of("").toAbsolutePath().getParent();
    private static final Path MAIN_RESOURCES_PATH = PROJECT_ROOT.resolve(Path.of("rosetta-source/src/main/resources"));
    private static final Path INGEST_OUTPUT_PATH = MAIN_RESOURCES_PATH.resolve(TransformType.TRANSLATE.getResourcePath()).resolve("output");
    private static final Path SYNONYM_INGEST_OUTPUT_BASE_PATH = MAIN_RESOURCES_PATH.resolve("result-json-files");
    private static final Path FUNCTION_INGEST_DIFF_PATH = PROJECT_ROOT.resolve(Path.of("tests/src/test/resources")).resolve("expected-output");
    public static final List<String> FPML_GROUPS = Arrays.asList("fpml-5-10", "fpml-5-12", "fpml-5-13");

    @MethodSource("inputs")
    @ParameterizedTest(name = "{0}")
    void checkTargetOutputDiff(String testName, Path synonymIngestOutputPath, Path ingestOutputPath, Path diffPath) throws IOException {
        List<String> actualOutputFileContent = Files.readAllLines(synonymIngestOutputPath);
        if (!Files.exists(ingestOutputPath)) {
            return;
        }
        List<String> targetOutputFileContent = Files.readAllLines(ingestOutputPath);

        String expectedDiff = readFile(diffPath);
        String actualDiff = createPatchFile(actualOutputFileContent, targetOutputFileContent, getRelativePath(synonymIngestOutputPath), getRelativePath(ingestOutputPath));
        assertDiffEquals(expectedDiff, actualDiff, diffPath);
    }

    private String readFile(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            LOGGER.error("Failed to read file {}", file.getFileName().toString(), e);
            return "";
        }
    }

    @NotNull
    private String getRelativePath(Path actualOutputPath) {
        return PROJECT_ROOT.relativize(actualOutputPath).toString();
    }

    @NotNull
    private String createPatchFile(List<String> targetOutputContent, List<String> actualOutputContent, String targetOutputFileName, String actualOutputFileName) throws IOException {
        Patch<String> patch = DiffUtils.diff(targetOutputContent, actualOutputContent);
        UnifiedDiffFile revised = UnifiedDiffFile.from(targetOutputFileName, actualOutputFileName, patch);
        UnifiedDiff unifiedDiff = UnifiedDiff.from(null, null, revised);
        StringBuilder writer = new StringBuilder();
        UnifiedDiffWriter.write(unifiedDiff, f -> targetOutputContent, s -> writer.append(s.trim()).append("\n"), 0);
        return writer.toString();
    }

    private void assertDiffEquals(String expectedDiff, String actualDiff, Path diffPath) {
        if (!expectedDiff.equals(actualDiff)) {
            if (WRITE_EXPECTATIONS) {
                try {
                    Files.createDirectories(diffPath.getParent());
                    Files.write(diffPath, actualDiff.getBytes());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
        assertEquals(expectedDiff, actualDiff,
                "The expected diff between actual and target output does not match for path " + diffPath);
    }

    private static Stream<Arguments> inputs() throws IOException {
        List<Arguments> arguments;
        try (Stream<Path> synonymIngestOutputFileStream = Files.walk(SYNONYM_INGEST_OUTPUT_BASE_PATH)) {
            arguments = synonymIngestOutputFileStream
                    .filter(TargetExpectationsDiffTest::isFpmlTestPack)
                    .filter(TargetExpectationsDiffTest::isJsonExt)
                    .map(synonymIngestOutputPath ->
                    {
                        Path ingestOutputPath = getIngestOutputPath(synonymIngestOutputPath);
                        return Arguments.of(getTestName(synonymIngestOutputPath),
                                synonymIngestOutputPath,
                                ingestOutputPath,
                                getDiffPath(ingestOutputPath));
                    })
                    .collect(Collectors.toList());
        }
        return arguments.stream();
    }

    private static boolean isFpmlTestPack(Path synonymIngestOutputPath) {
        return FPML_GROUPS.stream().anyMatch(x -> String.format("/%s/", synonymIngestOutputPath.toString()).contains(x));
    }

    private static boolean isJsonExt(Path synonymIngestOutputPath) {
        return synonymIngestOutputPath.getFileName().toString().endsWith(".json");
    }

    @NotNull
    private static String getTestName(Path synonymIngestOutputPath) {
        Path fileName = synonymIngestOutputPath.getFileName();
        Path relativePath = SYNONYM_INGEST_OUTPUT_BASE_PATH.relativize(synonymIngestOutputPath.getParent());
        String testPackName = toTestPackName(relativePath.toString());
        return String.format("%s | %s", testPackName, fileName.toString().replace(".json", ""));
    }

    @NotNull
    private static Path getIngestOutputPath(Path synonymIngestOutputPath) {
        Path fileName = synonymIngestOutputPath.getFileName();
        Path relativePath = SYNONYM_INGEST_OUTPUT_BASE_PATH.relativize(synonymIngestOutputPath.getParent());
        String testPackName = toTestPackName(relativePath.toString());
        String functionFolder = EVENT_TEST_PACKS.contains(testPackName) ? "fpml-confirmation-to-workflow-step" : "fpml-confirmation-to-trade-state";
        return INGEST_OUTPUT_PATH.resolve(functionFolder).resolve(testPackName).resolve(fileName);
    }

    @NotNull
    private static String toTestPackName(String cdmLegacyIngestTestPackPath) {
        String testPackName = cdmLegacyIngestTestPackPath.replaceAll("[-._/]+", " ");
        return directoryNameOfDataset(testPackName);
    }


    @NotNull
    private static Path getDiffPath(Path ingestOutputPath) {
        return Path.of(ingestOutputPath.toString()
                .replace("/tests/", "/rosetta-source/")
                .replace(INGEST_OUTPUT_PATH.toString(), FUNCTION_INGEST_DIFF_PATH.toString())
                .replace(".json", ".diff"));
    }
}
