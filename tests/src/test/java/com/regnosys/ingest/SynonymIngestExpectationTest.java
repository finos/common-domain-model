package com.regnosys.ingest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import com.regnosys.rosetta.common.util.UrlUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;

public class SynonymIngestExpectationTest {

    private static final String INPUT_FILES_DIR = "cdm-sample-files";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<Expectation>> TYPE_REFERENCE = new TypeReference<>() {
    };
    private static final Path RESOURCES_FOLDER = Path.of("../rosetta-source/src/main/resources");
    private static final Set<String> EXCLUDED_TEST_PACKS = Set.of("ore-1-0-39");
    private static final Set<String> EXCLUDED_FILENAMES = Set.of("expectations.json", "coverage.json");

    @ParameterizedTest(name = "{0}")
    @MethodSource("namedArguments")
    void checkExpectationsIsComplete(String name, Path expectationsPath) throws IOException {
        Set<String> filesListedInExpectationsFile = MAPPER.readValue(UrlUtils.toUrl(expectationsPath), TYPE_REFERENCE)
                .stream()
                .map(Expectation::getFileName)
                .collect(Collectors.toSet());

        Set<String> filesInDir = Files.walk(expectationsPath.getParent())
                .filter(Files::isRegularFile)
                .filter(path -> EXCLUDED_FILENAMES.stream().noneMatch(f -> path.toString().endsWith(f)))
                .map(RESOURCES_FOLDER::relativize)
                .map(UrlUtils::toPortableString)
                .collect(Collectors.toSet());
        
        assertThat(filesInDir, Matchers.containsInAnyOrder(filesListedInExpectationsFile.toArray()));
    }

    private static List<Arguments> namedArguments() throws IOException {
        Path sampleFilesDir = RESOURCES_FOLDER.resolve(INPUT_FILES_DIR);
        return Files.walk(sampleFilesDir)
                .filter(x -> x.getFileName().toString().equals("expectations.json"))
                .filter(x -> EXCLUDED_TEST_PACKS.stream().noneMatch(testPack -> x.toString().contains(testPack)))
                .map(path -> Arguments.of(sampleFilesDir.relativize(path.getParent()).toString()
                        .replace(File.pathSeparatorChar, '-'), path))
                .collect(Collectors.toList());
    }
}