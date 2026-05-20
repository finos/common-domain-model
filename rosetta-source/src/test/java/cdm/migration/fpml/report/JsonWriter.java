package cdm.migration.fpml.report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonWriter {
    public void writeJson(Path file, Object value) throws IOException {
        Files.createDirectories(file.getParent());
        Files.write(file, toJson(value, 0).getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("unchecked")
    private String toJson(Object value, int depth) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + escape((String) value) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof List) {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            List<Object> list = (List<Object>) value;
            for (int i = 0; i < list.size(); i++) {
                sb.append(indent(depth + 1)).append(toJson(list.get(i), depth + 1));
                if (i + 1 < list.size()) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append(indent(depth)).append("]");
            return sb.toString();
        }
        if (value instanceof Map) {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            Iterator<Map.Entry<Object, Object>> it = ((Map<Object, Object>) value).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Object, Object> e = it.next();
                sb.append(indent(depth + 1))
                        .append("\"")
                        .append(escape(String.valueOf(e.getKey())))
                        .append("\": ")
                        .append(toJson(e.getValue(), depth + 1));
                if (it.hasNext()) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append(indent(depth)).append("}");
            return sb.toString();
        }
        return "\"" + escape(String.valueOf(value)) + "\"";
    }

    private String indent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
