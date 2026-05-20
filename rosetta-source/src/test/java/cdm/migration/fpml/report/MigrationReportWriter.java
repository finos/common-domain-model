package cdm.migration.fpml.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class MigrationReportWriter {
    public void writeMigrationReport(Path file, Map<String, Object> metrics) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# FpML Flattening Migration Report\n\n");
        appendMetric(sb, "Old FpML type count", metrics.get("oldTypeCount"));
        appendMetric(sb, "New FpML type count", metrics.get("newTypeCount"));
        appendMetric(sb, "Removed type count", metrics.get("removedTypeCount"));
        appendMetric(sb, "Likely wrapper type count", metrics.get("likelyWrapperCount"));
        appendMetric(sb, "Ingest files scanned", metrics.get("ingestFileCount"));
        appendMetric(sb, "Functions found", metrics.get("functionCount"));
        appendMetric(sb, "Reachable functions", metrics.get("reachableFunctionCount"));
        appendMetric(sb, "FpML paths found", metrics.get("pathExpressionCount"));
        appendMetric(sb, "Resolved paths", metrics.get("resolvedPathCount"));
        appendMetric(sb, "Unresolved paths", metrics.get("unresolvedPathCount"));
        appendMetric(sb, "High-confidence rewrites", metrics.get("highRewriteCount"));
        appendMetric(sb, "Medium-confidence rewrites", metrics.get("mediumRewriteCount"));
        appendMetric(sb, "Missing-type input expansions", metrics.get("inputExpansionEdits"));
        appendMetric(sb, "Low-confidence skipped", metrics.get("lowSkippedCount"));
        appendMetric(sb, "Missing types", metrics.get("missingTypeCount"));
        appendMetric(sb, "Cardinality warnings", metrics.get("cardinalityWarningCount"));
        appendMetric(sb, "Files changed", metrics.get("changedFileCount"));
        sb.append("\n## Next Steps\n\n");
        sb.append("1. Review `applied-rewrites.diff`.\n");
        sb.append("2. Review `missing-types.md` and `unresolved-paths.md`.\n");
        sb.append("3. Run Maven/Rosetta generation and compile.\n");
        sb.append("4. Feed remaining compiler errors into follow-up targeted fixes.\n");
        Files.createDirectories(file.getParent());
        Files.write(file, sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void appendMetric(StringBuilder sb, String label, Object value) {
        sb.append("- ").append(label).append(": ").append(value == null ? 0 : value).append("\n");
    }
}
