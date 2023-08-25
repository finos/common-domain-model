package com.regnosys.rosetta.granite.distribution;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.granite.distribution.VerifyFileUtils.getDistZipFile;
import static com.regnosys.rosetta.granite.distribution.VerifyFileUtils.getFileCount;
import static com.regnosys.rosetta.granite.distribution.VerifyFileUtils.getFolderFileCount;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerifyCdmDistributionIntegrationTest {

    @Test
    void shouldVerifyCdmDistributionZipContainsExpectedFolders() throws IOException {
        Optional<Path> distZipFile = getDistZipFile("target/distribution-*.zip");
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
