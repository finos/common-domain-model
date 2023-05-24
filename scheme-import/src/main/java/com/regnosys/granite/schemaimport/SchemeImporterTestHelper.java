package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.transgest.ModelLoader;
import com.regnosys.testing.WhitespaceAgnosticAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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

public class SchemeImporterTestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemeImporterTestHelper.class);

    @Inject
    private SchemeImporter schemeImporter;
    @Inject
    private ModelLoader modelLoader;


    public void checkEnumsAreValid(String rosettaPathRoot, String body, String codingScheme, boolean writeTestOutput) throws IOException {
        URL[] rosettaPaths = getRosettaPaths(rosettaPathRoot);
        List<RosettaModel> models = modelLoader.loadRosettaModels(rosettaPaths);
        Map<String, String> generatedFromScheme =
                schemeImporter.generateRosettaEnums(
                        models,
                        body,
                        codingScheme);

        if (writeTestOutput) {
            writeTestOutput(generatedFromScheme);
        }

        for (String fileName : generatedFromScheme.keySet()) {
            String contents = getContents(rosettaPaths, fileName);
            WhitespaceAgnosticAssert.assertEquals(contents, generatedFromScheme.get(fileName));
        }
    }

    private static URL[] getRosettaPaths(String rosettaPathRoot) {
        return ClassPathUtils.findPathsFromClassPath(
                        List.of(rosettaPathRoot),
                        ".*\\.rosetta",
                        Optional.empty(),
                        SchemeImporter.class.getClassLoader()
                ).stream()
                .map(UrlUtils::toUrl)
                .toArray(URL[]::new);
    }

    private String getContents(URL[] rosettaPaths, String fileName) throws IOException {
        URL rosettaPath = Arrays.stream(rosettaPaths)
                .filter(x -> getFileName(x.getFile()).equals(fileName))
                .findFirst().orElseThrow();
        String contents = new String(rosettaPath.openStream().readAllBytes(), StandardCharsets.UTF_8);
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
            Files.writeString(outputPath, rosettaExpected.get(fileName), StandardCharsets.UTF_8);
            LOGGER.info("Wrote test output to {}", outputPath.toAbsolutePath());
        }
    }


}
