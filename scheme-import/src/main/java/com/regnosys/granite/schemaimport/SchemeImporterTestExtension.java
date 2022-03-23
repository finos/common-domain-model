package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.transgest.ModelLoaderImpl;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchemeImporterTestExtension implements BeforeEachCallback {
    private URL[] rosettaPaths;
    private SchemeImporter schemeImporter;
    private final String schemaPath;
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemeImporterTestExtension.class);
    private final String rosettaPathRoot;

    public SchemeImporterTestExtension(String schemaPath, String rosettaPathRoot) {
        this.schemaPath = schemaPath;
        this.rosettaPathRoot = rosettaPathRoot;
    }


    @Override
    public void beforeEach(ExtensionContext context) {
        rosettaPaths = getRosettaPaths(rosettaPathRoot);
        ModelLoaderImpl modelLoader = new ModelLoaderImpl(rosettaPaths);
        schemeImporter = new SchemeImporter(
                new FpMLSchemeEnumReader(
                        schemeUrl(),
                        "coding-schemes/fpml/"
                ),
                new AnnotatedRosettaEnumReader(modelLoader),
                new RosettaResourceWriter()
        );
    }

    public void checkEnumsAreValid(String body, String codingScheme, boolean writeTestOutput) throws IOException {
        Map<String, String> generatedFromScheme = schemeImporter.generateRosettaEnums(body, codingScheme);

        if (writeTestOutput) {
            writeTestOutput(generatedFromScheme);
        }

        for (String fileName : generatedFromScheme.keySet()) {
            String contents = getContents(fileName);
            assertEquals(contents, generatedFromScheme.get(fileName));
        }
    }

    private static URL[] getRosettaPaths(String rosettaPathRoot) {
        return ClassPathUtils.findPathsFromClassPath(
                        List.of(rosettaPathRoot),
                        ".*\\.rosetta",
                        Optional.empty(),
                        SchemeImporter.class.getClassLoader()
                ).stream()
                .map(ClassPathUtils::toUrl)
                .toArray(URL[]::new);
    }

    private URL schemeUrl() {
        return ClassPathUtils.loadFromClasspath(schemaPath, getClass().getClassLoader())
                .map(ClassPathUtils::toUrl)
                .findFirst().orElseThrow();
    }

    private String getContents(String fileName) throws IOException {
        URL rosettaPath = Arrays.stream(rosettaPaths)
                .filter(x -> getFileName(x.getFile()).equals(fileName))
                .findFirst().orElseThrow();
        String contents = new String(rosettaPath.openStream().readAllBytes());
        return RosettaResourceWriter.rewriteProjectVersion(contents);
    }

    private String getFileName(String path) {
        return path.substring(path.lastIndexOf('/')+1);
    }

    private void writeTestOutput(Map<String, String> rosettaExpected) throws IOException {
        // Add environment variable TEST_WRITE_BASE_PATH to override the base write path, e.g.
        // TEST_WRITE_BASE_PATH=/Users/hugohills/code/src/github.com/REGnosys/rosetta-cdm/src/main/rosetta/
        Path basePath = Optional.ofNullable(System.getenv("TEST_WRITE_BASE_PATH"))
                .map(Paths::get)
                .filter(Files::exists)
                .orElseThrow();

        for (String fileName : rosettaExpected.keySet()) {
            Path outputPath = basePath.resolve(fileName);
            Files.write(outputPath, rosettaExpected.get(fileName).getBytes(StandardCharsets.UTF_8));
            LOGGER.info("Wrote test output to {}", outputPath.toAbsolutePath());
        }
    }
}
