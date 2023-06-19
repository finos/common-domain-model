package com.regnosys.rosetta.granite.distribution;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static com.regnosys.rosetta.granite.distribution.VerifyFileUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VerifyCdmDistributionKotlinIntegrationTest {

    @Test
    void shouldVerifyCdmDistributionZipContainsExpectedFolders() throws IOException {
        Optional<Path> distZipFile = getDistZipFile("target/cdm-distribution-kotlin-*.zip");
        assertTrue(distZipFile.isPresent(), "CDM distribution zip not found");

        Map<Path, Long> distZipContents = getFolderFileCount(distZipFile.get());

        long kotlinJarFileCount = getFileCount(distZipContents, "/*/lib");
        assertThat("kotlin artifact (.jar) is not found", kotlinJarFileCount, is(2L));

        long rosettaSrcFileCount = getFileCount(distZipContents, "/*/common-domain-model");
        assertTrue(rosettaSrcFileCount > 0, "rosetta src is not found or empty");
    }
}
