package com.regnosys.rosetta.granite.distribution;

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

public class VerifyFileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyFileUtils.class);

    static Optional<Path> getDistZipFile(String pattern) throws IOException {
        PathMatcher matcher =
                FileSystems.getDefault().getPathMatcher("glob:" + pattern);

        return Files.list(Paths.get("target"))
                .filter(matcher::matches)
                .findFirst();
    }

    static Map<Path, Long> getFolderFileCount(Path distZipFile) throws IOException {
        Map<Path, Long> folderFileCount = new HashMap<>();

        try (FileSystem zipFileSystem = createZipFileSystem(distZipFile)) {
            final Path root = zipFileSystem.getPath("/");

            // walk the zip file tree and collect the folder and file count
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path folder, BasicFileAttributes attrs) throws IOException {
                    long fileCount = Files.list(folder)
                            // exclude directories in the count
                            .filter(file -> Files.isRegularFile(file))
                            .count();
                    LOGGER.debug("Path {} has {} files", folder, fileCount);
                    folderFileCount.put(folder, fileCount);

                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return folderFileCount;
    }

    static long getFileCount(Map<Path, Long> folderFileCountMap, String pattern) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        return folderFileCountMap.entrySet().stream()
                .filter(entry -> matcher.matches(entry.getKey()))
                .mapToLong(Map.Entry::getValue)
                .sum();
    }

    static FileSystem createZipFileSystem(Path path) throws IOException {
        final URI uri = URI.create("jar:file:" + path.toUri().getPath());
        return FileSystems.newFileSystem(uri, Collections.emptyMap());
    }
}
