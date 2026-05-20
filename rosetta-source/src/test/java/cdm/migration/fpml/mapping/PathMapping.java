package cdm.migration.fpml.mapping;

import java.util.ArrayList;
import java.util.List;

import cdm.migration.fpml.Confidence;

public class PathMapping {
    public final String rootType;
    public final List<String> oldPath;
    public final List<String> newPath;
    public final Confidence confidence;
    public final String reason;

    public PathMapping(String rootType, List<String> oldPath, List<String> newPath, Confidence confidence, String reason) {
        this.rootType = rootType;
        this.oldPath = new ArrayList<String>(oldPath);
        this.newPath = new ArrayList<String>(newPath);
        this.confidence = confidence;
        this.reason = reason;
    }

    public String oldPathKey() {
        return String.join("->", oldPath);
    }
}
