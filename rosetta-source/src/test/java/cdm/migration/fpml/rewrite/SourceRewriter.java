package cdm.migration.fpml.rewrite;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SourceRewriter {
    public RewriteResult rewrite(List<RewriteCandidate> candidates, boolean apply) throws IOException {
        return rewriteWithAdditionalEdits(candidates, Collections.<TextEdit>emptyList(), apply);
    }

    public RewriteResult rewriteWithAdditionalEdits(List<RewriteCandidate> candidates, List<TextEdit> additionalEdits, boolean apply)
            throws IOException {
        Map<String, List<RewriteCandidate>> byFile = new LinkedHashMap<String, List<RewriteCandidate>>();
        for (RewriteCandidate candidate : candidates) {
            List<RewriteCandidate> list = byFile.get(candidate.file);
            if (list == null) {
                list = new ArrayList<RewriteCandidate>();
                byFile.put(candidate.file, list);
            }
            list.add(candidate);
        }
        Map<String, List<TextEdit>> extraByFile = new LinkedHashMap<String, List<TextEdit>>();
        for (TextEdit edit : additionalEdits) {
            List<TextEdit> list = extraByFile.get(edit.file);
            if (list == null) {
                list = new ArrayList<TextEdit>();
                extraByFile.put(edit.file, list);
            }
            list.add(edit);
        }
        for (Map.Entry<String, List<TextEdit>> entry : extraByFile.entrySet()) {
            if (!byFile.containsKey(entry.getKey())) {
                byFile.put(entry.getKey(), new ArrayList<RewriteCandidate>());
            }
        }

        Map<String, String> originalText = new LinkedHashMap<String, String>();
        Map<String, String> rewrittenText = new LinkedHashMap<String, String>();
        List<String> changedFiles = new ArrayList<String>();

        for (Map.Entry<String, List<RewriteCandidate>> entry : byFile.entrySet()) {
            String file = entry.getKey();
            Path filePath = Paths.get(file);
            String text = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            originalText.put(file, text);
            List<RewriteCandidate> list = entry.getValue();
            List<TextEdit> edits = new ArrayList<TextEdit>();
            for (RewriteCandidate candidate : list) {
                edits.add(new TextEdit(candidate.file, candidate.startOffset, candidate.endOffset, candidate.newExpression, "path rewrite"));
            }
            List<TextEdit> extras = extraByFile.get(file);
            if (extras != null) {
                edits.addAll(extras);
            }
            Collections.sort(edits, (a, b) -> Integer.compare(b.startOffset, a.startOffset));

            String current = text;
            int previousStart = Integer.MAX_VALUE;
            for (TextEdit edit : edits) {
                if (edit.endOffset > previousStart) {
                    continue;
                }
                current = current.substring(0, edit.startOffset) + edit.replacement + current.substring(edit.endOffset);
                previousStart = edit.startOffset;
            }
            rewrittenText.put(file, current);
            if (!text.equals(current)) {
                changedFiles.add(file);
                if (apply) {
                    Files.write(filePath, current.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        return new RewriteResult(originalText, rewrittenText, changedFiles);
    }

    public static class RewriteResult {
        public final Map<String, String> originalByFile;
        public final Map<String, String> rewrittenByFile;
        public final List<String> changedFiles;

        public RewriteResult(Map<String, String> originalByFile, Map<String, String> rewrittenByFile, List<String> changedFiles) {
            this.originalByFile = originalByFile;
            this.rewrittenByFile = rewrittenByFile;
            this.changedFiles = changedFiles;
        }
    }
}
