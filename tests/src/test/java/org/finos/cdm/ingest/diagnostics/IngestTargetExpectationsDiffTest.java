package org.finos.cdm.ingest.diagnostics;

import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;
import com.github.difflib.unifieddiff.UnifiedDiff;
import com.github.difflib.unifieddiff.UnifiedDiffFile;
import com.github.difflib.unifieddiff.UnifiedDiffWriter;
import com.regnosys.rosetta.common.util.SimpleBuilderProcessor;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.FieldWithMeta;
import com.rosetta.model.lib.meta.ReferenceWithMeta;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.AttributeMeta;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.finos.cdm.ingest.diagnostics.IngestUtils.*;
import static org.finos.cdm.testpack.CdmTestPackCreator.EVENT_TEST_PACKS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class IngestTargetExpectationsDiffTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestTargetExpectationsDiffTest.class);

    @MethodSource("inputs")
    @ParameterizedTest(name = "{0}")
    void checkTargetOutputDiff(String testName, Class<? extends RosettaModelObject> clazz, Path synonymIngestOutputPath, Path ingestOutputPath, Path diffPath) throws IOException {
        List<String> actualOutputFileContent = readFile(clazz, synonymIngestOutputPath);
        if (!Files.exists(ingestOutputPath)) {
            LOGGER.warn("No output file found for {}", synonymIngestOutputPath);
            return;
        }
        List<String> targetOutputFileContent = readFile(clazz, ingestOutputPath);

        String expectedDiff = readFile(diffPath);
        String actualDiff = createPatchFile(actualOutputFileContent, targetOutputFileContent, getRelativePath(synonymIngestOutputPath), getRelativePath(ingestOutputPath));
        assertDiffEquals(expectedDiff, actualDiff, diffPath);
    }

    private static Stream<Arguments> inputs() throws IOException {
        List<Arguments> arguments;
        try (Stream<Path> synonymIngestOutputFileStream = Files.walk(SYNONYM_INGEST_OUTPUT_BASE_PATH)) {
            arguments = synonymIngestOutputFileStream
                    .filter(IngestUtils::isFpmlTestPack)
                    .filter(IngestUtils::isJsonExt)
                    .map(synonymIngestOutputPath ->
                    {
                        
                        Path fileName = synonymIngestOutputPath.getFileName();
                        Path relativePath = SYNONYM_INGEST_OUTPUT_BASE_PATH.relativize(synonymIngestOutputPath.getParent());
                        String testPackName = toTestPackName(relativePath.toString());
                        Path ingestOutputPath = getIngestOutputPath(testPackName, fileName);
                        Class<? extends RosettaModelObject> clazz = EVENT_TEST_PACKS.contains(testPackName) ?
                                WorkflowStep.class : TradeState.class;
                        return Arguments.of(getTestName(testPackName, fileName),
                                clazz,
                                synonymIngestOutputPath,
                                ingestOutputPath,
                                getDiffPath(ingestOutputPath));
                    })
                    .collect(Collectors.toList());
        }
        return arguments.stream();
    }
    
    @NotNull
    private static Path getDiffPath(Path ingestOutputPath) {
        return Path.of(ingestOutputPath.toString()
                .replace("/tests/", "/rosetta-source/")
                .replace(INGEST_OUTPUT_PATH.toString(), FUNCTION_INGEST_DIFF_PATH.toString())
                .replace(".json", ".diff"));
    }

    @NotNull
    private List<String> readFile(Class<? extends RosettaModelObject> clazz, Path synonymIngestOutputPath) throws IOException {
        try {
            String json = Files.readString(synonymIngestOutputPath);
            RosettaModelObject modelObject = deserialise(clazz, json);
            RosettaModelObject processedModelObject = removeGlobalKeys(modelObject);
            String processedJson = serialise(processedModelObject);
            return Arrays.asList(processedJson.split("\n"));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to read file {}", synonymIngestOutputPath, e);
            return Collections.emptyList();
        }
    }
    
    private static <T extends RosettaModelObject> T deserialise(Class<T> clazz, String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    private <T extends RosettaModelObject> RosettaModelObject removeGlobalKeys(RosettaModelObject o) {
        RosettaModelObjectBuilder builder = o.toBuilder();
        builder.process(RosettaPath.valueOf("Root"), new GlobalKeyRemover());
        GlobalKey.GlobalKeyBuilder globalKeyBuilder = (GlobalKey.GlobalKeyBuilder) builder;
        globalKeyBuilder.getOrCreateMeta().setGlobalKey(null);
        return builder.prune().build();
    }

    private static String serialise(RosettaModelObject o) throws JsonProcessingException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
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

    private static class GlobalKeyRemover extends SimpleBuilderProcessor {
        @Override
        public <R extends RosettaModelObject> boolean processRosetta(RosettaPath path,
                                                                     Class<R> rosettaType,
                                                                     RosettaModelObjectBuilder builder,
                                                                     RosettaModelObjectBuilder parent,
                                                                     AttributeMeta... metas) {
            if (builder == null)
                return false;
            if (isGlobalReference(builder)) {
                ReferenceWithMeta.ReferenceWithMetaBuilder<?> refBuilder = (ReferenceWithMeta.ReferenceWithMetaBuilder<?>) builder;
                refBuilder.setGlobalReference(null);
            } else if (isGlobalKey(builder, metas)) {
                GlobalKey.GlobalKeyBuilder keyBuilder = (GlobalKey.GlobalKeyBuilder) builder;
                Optional.ofNullable(keyBuilder.getMeta())
                        .ifPresent(b -> b.setGlobalKey(null));
            }
            return true;
        }

        @Override
        public Report report() {
            return null;
        }

        private boolean isGlobalKey(RosettaModelObjectBuilder builder, AttributeMeta... metas) {
            return builder instanceof GlobalKey
                    // exclude FieldWithMetas unless they contain a IS_GLOBAL_KEY_FIELD meta
                    && !(builder instanceof FieldWithMeta && !Arrays.asList(metas).contains(AttributeMeta.GLOBAL_KEY_FIELD));
        }

        private boolean isGlobalReference(RosettaModelObjectBuilder builder) {
            return builder instanceof ReferenceWithMeta;
        }
    }
}
