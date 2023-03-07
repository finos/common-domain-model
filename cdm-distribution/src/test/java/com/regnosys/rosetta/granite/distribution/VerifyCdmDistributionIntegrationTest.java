package com.regnosys.rosetta.granite.distribution;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.granite.distribution.VerifyFileUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerifyCdmDistributionIntegrationTest {

    @Test
    void shouldVerifyCdmDistributionZipContainsExpectedFolders() throws IOException {
        Optional<Path> distZipFile = getDistZipFile("target/cdm-distribution-*.zip");
        assertTrue(distZipFile.isPresent(), "CDM distribution zip not found");

        Map<Path, Long> distZipContents = getFolderFileCount(distZipFile.get());

        long xmlFileCount = getFileCount(distZipContents, "**/json-xml-document/xml**");
        assertTrue(xmlFileCount > 0, "json-xml-document/xml is not found or empty");

        long jsonFileCount = getFileCount(distZipContents, "**/json-xml-document/json**");
        assertTrue(jsonFileCount > 0, "json-xml-document/json is not found or empty");

        long rosettaSrcFileCount = getFileCount(distZipContents, "/*/common-domain-model");
        assertTrue(rosettaSrcFileCount > 0, "rosetta src is not found or empty");
    }
}
