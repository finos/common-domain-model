package cdm.migration.fpml.model;

public class RosettaAttributeInfo {
    public final String name;
    public final String typeName;
    public final String typeNamespace;
    public final String typeQualifiedName;
    public final Cardinality cardinality;
    public final String sourceFile;
    public final int sourceLine;

    public RosettaAttributeInfo(
            String name,
            String typeName,
            String typeNamespace,
            String typeQualifiedName,
            Cardinality cardinality,
            String sourceFile,
            int sourceLine) {
        this.name = name;
        this.typeName = typeName;
        this.typeNamespace = typeNamespace;
        this.typeQualifiedName = typeQualifiedName;
        this.cardinality = cardinality;
        this.sourceFile = sourceFile;
        this.sourceLine = sourceLine;
    }
}
