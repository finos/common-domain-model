package cdm.migration.fpml.model;

import java.util.ArrayList;
import java.util.List;

public class RosettaTypeInfo {
    public final String namespace;
    public final String simpleName;
    public final String qualifiedName;
    public final String kind;
    public final String extendsTypeName;
    public String extendsTypeQualifiedName;
    public final String sourceFile;
    public final int sourceLine;
    public final List<RosettaAttributeInfo> attributes = new ArrayList<RosettaAttributeInfo>();

    public RosettaTypeInfo(String namespace, String simpleName, String kind, String extendsTypeName, String sourceFile, int sourceLine) {
        this.namespace = namespace;
        this.simpleName = simpleName;
        this.qualifiedName = namespace + "." + simpleName;
        this.kind = kind;
        this.extendsTypeName = extendsTypeName;
        this.sourceFile = sourceFile;
        this.sourceLine = sourceLine;
    }
}
