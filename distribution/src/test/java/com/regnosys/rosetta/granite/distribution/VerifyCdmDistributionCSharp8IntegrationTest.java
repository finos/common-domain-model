package com.regnosys.rosetta.granite.distribution;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.granite.distribution.VerifyFileUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerifyCdmDistributionCSharp8IntegrationTest {

    @Test
    void shouldVerifyCdmDistributionZipContainsExpectedFolders() throws IOException {
        Optional<Path> distZipFile = getDistZipFile("target/distribution-csharp8-*.zip");
        assertTrue(distZipFile.isPresent(), "CDM distribution zip not found");

        Map<Path, Long> distZipContents = getFolderFileCount(distZipFile.get());

        long csharp8SrcFileCount = getFileCount(distZipContents, "/*/csharp8/**");
        assertTrue(csharp8SrcFileCount > 0, "csharp8 src is not found or empty");

        long csharp8LibFileCount = getFileCount(distZipContents, "/*/lib/app");
        assertTrue(csharp8LibFileCount > 0, "csharp8 artifacts (.dll) not found");

        long rosettaSrcFileCount = getFileCount(distZipContents, "/*/common-domain-model");
        assertTrue(rosettaSrcFileCount > 0, "rosetta src is not found or empty");
    }
}
