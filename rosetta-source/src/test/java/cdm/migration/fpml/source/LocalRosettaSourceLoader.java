package cdm.migration.fpml.source;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalRosettaSourceLoader {
    public List<RosettaSourceFile> load(Path directory, String fileMask) throws IOException {
        List<RosettaSourceFile> files = new ArrayList<RosettaSourceFile>();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + fileMask);
        Files.list(directory).forEach(path -> {
            if (Files.isRegularFile(path) && matcher.matches(path.getFileName())) {
                try {
                    String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                    files.add(new RosettaSourceFile(path.toString(), content));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Collections.sort(files, (a, b) -> a.getLogicalPath().compareTo(b.getLogicalPath()));
        return files;
    }
}
