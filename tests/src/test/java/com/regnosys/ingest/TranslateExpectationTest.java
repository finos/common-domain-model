package com.regnosys.ingest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import com.regnosys.rosetta.common.util.UrlUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
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

public class TranslateExpectationTest {

    private static final String INPUT_FILES_DIR = "cdm-sample-files";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<Expectation>> TYPE_REFERENCE = new TypeReference<>() {
    };
    private static final Path RESOURCES_FOLDER = Path.of("../rosetta-source/src/main/resources");

    @ParameterizedTest(name = "{0}")
    @MethodSource("namedArguments")
    void checkExpectationsIsComplete(String name, Path expectationsPath) throws IOException {
        Set<String> expectationsSamples = MAPPER.readValue(UrlUtils.toUrl(expectationsPath), TYPE_REFERENCE)
                .stream().map(Expectation::getFileName)
                .collect(Collectors.toSet());

        Set<String> xmlFilesInDir = Files.walk(expectationsPath.getParent())
                .filter(Files::isRegularFile)
                .filter(x -> !x.toString().endsWith("expectations.json") && !x.toString().endsWith("coverage.json"))
                .map(RESOURCES_FOLDER::relativize)
                .map(Path::toString)
                .collect(Collectors.toSet());
        
        assertThat(xmlFilesInDir, Matchers.containsInAnyOrder(expectationsSamples.toArray()));
    }

    private static List<Arguments> namedArguments() throws IOException {
        Path sampleFilesDir = RESOURCES_FOLDER.resolve(INPUT_FILES_DIR);
        return Files.walk(sampleFilesDir)
                .filter(x -> x.getFileName().toString().equals("expectations.json"))
                .filter(x -> !x.toString().contains("ore-1-0-39"))
                .map(path -> Arguments.of(sampleFilesDir.relativize(path.getParent()).toString()
                        .replace(File.pathSeparatorChar, '-'), path))
                .collect(Collectors.toList());
    }
}