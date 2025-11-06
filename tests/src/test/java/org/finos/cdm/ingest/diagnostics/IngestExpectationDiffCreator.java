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

public class IngestExpectationDiffCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestExpectationDiffCreator.class);

    private static final Path FUNCTION_INGEST_DIFF_PATH = PROJECT_ROOT.resolve(Path.of("tests/src/test/resources")).resolve("ingest").resolve("expected-output");

    public void generateIngestExpectationDiffs() {
        inputs().forEach(input ->
                generateIngestExpectationDiffFile(input.clazz, input.synonymIngestOutputPath, input.ingestOutputPath, input.diffPath));
    }

    private List<Input> inputs() {
        try (Stream<Path> synonymIngestOutputFileStream = Files.walk(SYNONYM_INGEST_OUTPUT_BASE_PATH)) {
            return synonymIngestOutputFileStream
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
                        return new Input(clazz,
                                synonymIngestOutputPath,
                                ingestOutputPath,
                                getDiffPath(ingestOutputPath));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Failed to walk files %s", SYNONYM_INGEST_OUTPUT_BASE_PATH), e);
        }
    }

    void generateIngestExpectationDiffFile(Class<? extends RosettaModelObject> clazz, 
                                           Path synonymIngestOutputPath, 
                                           Path ingestOutputPath, 
                                           Path diffPath) {
        List<String> actualOutputFileContent = readFile(clazz, synonymIngestOutputPath);
        if (!Files.exists(ingestOutputPath)) {
            LOGGER.warn("No output file found for {}", synonymIngestOutputPath);
            return;
        }
        List<String> targetOutputFileContent = readFile(clazz, ingestOutputPath);
        try {
            String contents = createPatchFile(actualOutputFileContent, targetOutputFileContent, getRelativePath(synonymIngestOutputPath), getRelativePath(ingestOutputPath));
            writeFile(diffPath, contents);
        } catch (IOException e) {
            LOGGER.error("Error while creating diff file {}", diffPath, e);
        }
    }

    private Path getDiffPath(Path ingestOutputPath) {
        return Path.of(ingestOutputPath.toString()
                .replace("/tests/", "/rosetta-source/")
                .replace(INGEST_OUTPUT_PATH.toString(), FUNCTION_INGEST_DIFF_PATH.toString())
                .replace(".json", ".diff"));
    }

    private List<String> readFile(Class<? extends RosettaModelObject> clazz, Path synonymIngestOutputPath) {
        try {
            String json = Files.readString(synonymIngestOutputPath);
            RosettaModelObject modelObject = deserialise(clazz, json);
            RosettaModelObject processedModelObject = removeGlobalKeys(modelObject);
            String processedJson = serialise(processedModelObject);
            return Arrays.asList(processedJson.split("\n"));
        } catch (Exception e) {
            LOGGER.error("Failed to read file {}", synonymIngestOutputPath, e);
            return Collections.emptyList();
        }
    }

    private <T extends RosettaModelObject> T deserialise(Class<T> clazz, String json) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    private <T extends RosettaModelObject> RosettaModelObject removeGlobalKeys(RosettaModelObject o) {
        RosettaModelObjectBuilder builder = o.toBuilder();
        builder.process(RosettaPath.valueOf("Root"), new GlobalKeyRemover());
        GlobalKey.GlobalKeyBuilder globalKeyBuilder = (GlobalKey.GlobalKeyBuilder) builder;
        globalKeyBuilder.getOrCreateMeta().setGlobalKey(null);
        return builder.prune().build();
    }

    private String serialise(RosettaModelObject o) throws JsonProcessingException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }

    private String getRelativePath(Path actualOutputPath) {
        return PROJECT_ROOT.relativize(actualOutputPath).toString();
    }

    private String createPatchFile(List<String> targetOutputContent, List<String> actualOutputContent, String targetOutputFileName, String actualOutputFileName) throws IOException {
        Patch<String> patch = DiffUtils.diff(targetOutputContent, actualOutputContent);
        UnifiedDiffFile revised = UnifiedDiffFile.from(targetOutputFileName, actualOutputFileName, patch);
        UnifiedDiff unifiedDiff = UnifiedDiff.from(null, null, revised);
        StringBuilder writer = new StringBuilder();
        UnifiedDiffWriter.write(unifiedDiff, f -> targetOutputContent, s -> writer.append(s.trim()).append("\n"), 0);
        return writer.toString();
    }

    private void writeFile(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, content.getBytes());
        LOGGER.info("Created diff file {}", path);
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

    private static class Input {
        private final Class<? extends RosettaModelObject> clazz;
        private final Path synonymIngestOutputPath;
        private final Path ingestOutputPath;
        private final Path diffPath;

        private Input(Class<? extends RosettaModelObject> clazz, Path synonymIngestOutputPath, Path ingestOutputPath, Path diffPath) {
            this.clazz = clazz;
            this.synonymIngestOutputPath = synonymIngestOutputPath;
            this.ingestOutputPath = ingestOutputPath;
            this.diffPath = diffPath;
        }
    }
}
