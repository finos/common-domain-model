package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.common.util.UrlUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FpMLSchemeHelper {

    /**
     *
     * @return this is the url of the set-of-schemes-n-n.xml published by FpML
     */
    public URL getLatestSetOfSchemeUrl()  {
        String schemaPath;
        try {
            schemaPath = getLatestSetOfSchemeFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ClassPathUtils.loadFromClasspath(schemaPath, getClass().getClassLoader())
                .map(UrlUtils::toUrl)
                .findFirst().orElseThrow();
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
