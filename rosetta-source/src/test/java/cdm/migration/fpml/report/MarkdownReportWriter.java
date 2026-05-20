package cdm.migration.fpml.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MarkdownReportWriter {
    public void writeLines(Path file, String title, List<String> lines) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(title).append("\n\n");
        if (lines.isEmpty()) {
            sb.append("- None\n");
        } else {
            for (String line : lines) {
                sb.append("- ").append(line).append("\n");
            }
        }
        Files.createDirectories(file.getParent());
        Files.write(file, sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void writeCallGraph(Path file, Map<String, Set<String>> callGraph) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# Call Graph\n\n");
        for (Map.Entry<String, Set<String>> e : callGraph.entrySet()) {
            sb.append("- `").append(e.getKey()).append("`");
            if (e.getValue().isEmpty()) {
                sb.append(" -> (none)\n");
            } else {
                sb.append(" -> ").append(String.join(", ", e.getValue())).append("\n");
            }
        }
        Files.createDirectories(file.getParent());
        Files.write(file, sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
