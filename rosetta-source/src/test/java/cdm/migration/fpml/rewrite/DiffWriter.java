package cdm.migration.fpml.rewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiffWriter {
    public String buildDiff(Map<String, String> originalByFile, Map<String, String> rewrittenByFile) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : originalByFile.entrySet()) {
            String file = entry.getKey();
            String oldText = entry.getValue();
            String newText = rewrittenByFile.get(file);
            if (newText == null || oldText.equals(newText)) {
                continue;
            }
            sb.append("--- ").append(file).append('\n');
            sb.append("+++ ").append(file).append('\n');
            List<String> oldLines = splitLines(oldText);
            List<String> newLines = splitLines(newText);
            int max = Math.max(oldLines.size(), newLines.size());
            for (int i = 0; i < max; i++) {
                String oldLine = i < oldLines.size() ? oldLines.get(i) : null;
                String newLine = i < newLines.size() ? newLines.get(i) : null;
                if (oldLine == null) {
                    sb.append("+").append(newLine).append('\n');
                } else if (newLine == null) {
                    sb.append("-").append(oldLine).append('\n');
                } else if (!oldLine.equals(newLine)) {
                    sb.append("-").append(oldLine).append('\n');
                    sb.append("+").append(newLine).append('\n');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private List<String> splitLines(String text) {
        String[] arr = text.split("\\R", -1);
        List<String> out = new ArrayList<String>();
        for (String s : arr) {
            out.add(s);
        }
        return out;
    }
}
