package cdm.migration.fpml.rewrite;

import java.util.ArrayList;
import java.util.List;

import cdm.migration.fpml.Confidence;

public class RewriteCandidate {
    public final String file;
    public final String function;
    public final int line;
    public final int startOffset;
    public final int endOffset;
    public final String oldExpression;
    public final String newExpression;
    public final Confidence confidence;
    public final String reason;
    public final String rootType;
    public final List<String> oldPath;
    public final List<String> newPath;

    public RewriteCandidate(
            String file,
            String function,
            int line,
            int startOffset,
            int endOffset,
            String oldExpression,
            String newExpression,
            Confidence confidence,
            String reason,
            String rootType,
            List<String> oldPath,
            List<String> newPath) {
        this.file = file;
        this.function = function;
        this.line = line;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.oldExpression = oldExpression;
        this.newExpression = newExpression;
        this.confidence = confidence;
        this.reason = reason;
        this.rootType = rootType;
        this.oldPath = new ArrayList<String>(oldPath);
        this.newPath = new ArrayList<String>(newPath);
    }
}
