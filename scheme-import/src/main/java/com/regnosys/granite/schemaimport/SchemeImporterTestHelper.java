package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.transgest.ModelLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SchemeImporterTestHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchemeImporterTestHelper.class);

    @Inject
    private SchemeImporter schemeImporter;
    @Inject
    private ModelLoader modelLoader;


    public void checkEnumsAreValid(String rosettaPathRoot, String body, String codingScheme, boolean writeTestOutput) throws IOException {
        URL[] rosettaPaths = getRosettaPaths(rosettaPathRoot);
        List<RosettaModel> models = modelLoader.loadRosettaModels(rosettaPaths);
        String schemaPath = getLatestSetOfSchemeFile();
        Map<String, String> generatedFromScheme =
                schemeImporter.generateRosettaEnums(
                        schemeUrl(schemaPath),
                        "coding-schemes/fpml/",
                        models,
                        body,
                        codingScheme);

        if (writeTestOutput) {
            writeTestOutput(generatedFromScheme);
        }

        for (String fileName : generatedFromScheme.keySet()) {
            String contents = getContents(rosettaPaths, fileName);
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
                .map(UrlUtils::toUrl)
                .toArray(URL[]::new);
    }

    private URL schemeUrl(String schemaPath) {
        return ClassPathUtils.loadFromClasspath(schemaPath, getClass().getClassLoader())
                .map(UrlUtils::toUrl)
                .findFirst().orElseThrow();
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

    public String getLatestSetOfSchemeFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path baseFolder = ClassPathUtils
                .loadFromClasspath("coding-schemes/fpml", classLoader)
                .findFirst()
                .orElseThrow();
        assertNotNull(baseFolder);

        HashMap<String, BigDecimal> versionNumberFileName = new HashMap<>();
        try (Stream<Path> walk = Files.walk(baseFolder)) {
            walk.filter(this::isSetOfSchemeXmlFile)
                    .forEach(inFile -> {
                        String fileName = inFile.getFileName().toString();
                        String versionNumber = fileName.substring(15, fileName.indexOf(".")).replace("-", ".");

                        versionNumberFileName.put(fileName,new BigDecimal(versionNumber));
                    });
        }

        BigDecimal highestVersion = new BigDecimal(0);
        String latestSetOfSchemesFile = "coding-schemes/fpml/set-of-schemes-2-2.xml";
        for (Map.Entry<String, BigDecimal> entry : versionNumberFileName.entrySet()) {
            BigDecimal value = entry.getValue();
            if (value.unscaledValue().compareTo(highestVersion.unscaledValue())>0){
                highestVersion = value;
                latestSetOfSchemesFile = "coding-schemes/fpml/" + entry.getKey();
            }

        }

        return latestSetOfSchemesFile;
    }


    private boolean isSetOfSchemeXmlFile(Path inFile) {
        return inFile.getFileName().toString().endsWith(".xml") && inFile.getFileName().toString().contains("set-of-schemes-");
    }
}
