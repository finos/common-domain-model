package cdm.migration.fpml.analysis;

import java.util.ArrayList;
import java.util.List;

public class RosettaPathExpression {
    public final String file;
    public final String function;
    public final int startOffset;
    public final int endOffset;
    public final int line;
    public final String expression;
    public final String rootVariable;
    public final List<String> segments;

    public RosettaPathExpression(
            String file,
            String function,
            int startOffset,
            int endOffset,
            int line,
            String expression,
            String rootVariable,
            List<String> segments) {
        this.file = file;
        this.function = function;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.line = line;
        this.expression = expression;
        this.rootVariable = rootVariable;
        this.segments = new ArrayList<String>(segments);
    }
}
