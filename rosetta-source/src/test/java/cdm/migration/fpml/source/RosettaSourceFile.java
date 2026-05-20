package cdm.migration.fpml.source;

public class RosettaSourceFile {
    private final String logicalPath;
    private final String content;

    public RosettaSourceFile(String logicalPath, String content) {
        this.logicalPath = logicalPath;
        this.content = content;
    }

    public String getLogicalPath() {
        return logicalPath;
    }

    public String getContent() {
        return content;
    }
}
