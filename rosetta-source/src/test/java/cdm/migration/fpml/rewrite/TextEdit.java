package cdm.migration.fpml.rewrite;

public class TextEdit {
    public final String file;
    public final int startOffset;
    public final int endOffset;
    public final String replacement;
    public final String reason;

    public TextEdit(String file, int startOffset, int endOffset, String replacement, String reason) {
        this.file = file;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.replacement = replacement;
        this.reason = reason;
    }
}
