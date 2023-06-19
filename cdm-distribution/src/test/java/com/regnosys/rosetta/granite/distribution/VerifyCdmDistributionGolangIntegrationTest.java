package com.regnosys.rosetta.granite.distribution;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.granite.distribution.VerifyFileUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerifyCdmDistributionGolangIntegrationTest {

    @Test
    void shouldVerifyCdmDistributionZipContainsExpectedFolders() throws IOException {
        Optional<Path> distZipFile = getDistZipFile("target/cdm-distribution-golang-*.zip");
        assertTrue(distZipFile.isPresent(), "CDM distribution zip not found");

        Map<Path, Long> distZipContents = getFolderFileCount(distZipFile.get());

        long golangSrcFileCount = getFileCount(distZipContents, "/*/golang/**");
        assertTrue(golangSrcFileCount > 0, "golang src is not found or empty");

        long rosettaSrcFileCount = getFileCount(distZipContents, "/*/common-domain-model");
        assertTrue(rosettaSrcFileCount > 0, "rosetta src is not found or empty");
    }
}
