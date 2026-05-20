package cdm.migration.fpml.analysis;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallSite {
    public final String file;
    public final String enclosingFunction;
    public final String calledFunction;
    public final int callStartOffset;
    public final int callEndOffset;
    public final List<CallArgument> arguments = new ArrayList<CallArgument>();

    public FunctionCallSite(String file, String enclosingFunction, String calledFunction, int callStartOffset, int callEndOffset) {
        this.file = file;
        this.enclosingFunction = enclosingFunction;
        this.calledFunction = calledFunction;
        this.callStartOffset = callStartOffset;
        this.callEndOffset = callEndOffset;
    }

    public static class CallArgument {
        public final String text;
        public final int startOffset;
        public final int endOffset;

        public CallArgument(String text, int startOffset, int endOffset) {
            this.text = text;
            this.startOffset = startOffset;
            this.endOffset = endOffset;
        }
    }
}
