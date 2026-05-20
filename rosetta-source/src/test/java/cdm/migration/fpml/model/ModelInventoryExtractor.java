package cdm.migration.fpml.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdm.migration.fpml.source.RosettaSourceFile;

public class ModelInventoryExtractor {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^\\s*namespace\\s+([A-Za-z0-9_.]+)");
    private static final Pattern TYPE_PATTERN = Pattern.compile("^\\s*type\\s+([A-Za-z0-9_^]+)\\b");
    private static final Pattern ENUM_PATTERN = Pattern.compile("^\\s*enum\\s+([A-Za-z0-9_^]+)\\b");
    private static final Pattern TYPE_ALIAS_PATTERN = Pattern.compile("^\\s*typeAlias\\s+([A-Za-z0-9_^]+)\\s*:");
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(
            "^\\s*([\\^A-Za-z_][A-Za-z0-9_]*)\\s+([A-Za-z_][A-Za-z0-9_.^]*)\\s*(\\([^)]*\\))?.*");

    public RosettaModelInventory extract(List<RosettaSourceFile> files) {
        RosettaModelInventory inventory = new RosettaModelInventory();
        for (RosettaSourceFile file : files) {
            parseFile(file, inventory);
        }
        resolveAttributeQualifiedType(inventory);
        return inventory;
    }

    private void parseFile(RosettaSourceFile file, RosettaModelInventory inventory) {
        String namespace = "";
        RosettaTypeInfo currentType = null;
        String[] lines = file.getContent().split("\\R", -1);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Matcher namespaceMatcher = NAMESPACE_PATTERN.matcher(line);
            if (namespaceMatcher.find()) {
                namespace = namespaceMatcher.group(1);
                continue;
            }

            Matcher typeAliasMatcher = TYPE_ALIAS_PATTERN.matcher(line);
            if (typeAliasMatcher.find()) {
                currentType = new RosettaTypeInfo(namespace, typeAliasMatcher.group(1), "typeAlias", file.getLogicalPath(), i + 1);
                inventory.addType(currentType);
                continue;
            }

            Matcher typeMatcher = TYPE_PATTERN.matcher(line);
            if (typeMatcher.find()) {
                currentType = new RosettaTypeInfo(namespace, typeMatcher.group(1), "data", file.getLogicalPath(), i + 1);
                inventory.addType(currentType);
                continue;
            }

            Matcher enumMatcher = ENUM_PATTERN.matcher(line);
            if (enumMatcher.find()) {
                currentType = new RosettaTypeInfo(namespace, enumMatcher.group(1), "enum", file.getLogicalPath(), i + 1);
                inventory.addType(currentType);
                continue;
            }

            if (currentType != null && "data".equals(currentType.kind)) {
                Matcher attrMatcher = ATTRIBUTE_PATTERN.matcher(line);
                if (attrMatcher.find()) {
                    String attrName = attrMatcher.group(1);
                    if ("condition".equals(attrName) || "version".equals(attrName) || "namespace".equals(attrName)
                            || "annotation".equals(attrName) || "synonym".equals(attrName)) {
                        continue;
                    }
                    String typeName = attrMatcher.group(2);
                    String cardinalityRaw = attrMatcher.group(3);
                    if (line.trim().startsWith("[") || line.trim().startsWith("condition ") || line.trim().startsWith("one-of")
                            || line.trim().startsWith("optional ")) {
                        continue;
                    }
                    currentType.attributes.add(new RosettaAttributeInfo(
                            attrName,
                            typeName,
                            null,
                            null,
                            Cardinality.parse(cardinalityRaw),
                            file.getLogicalPath(),
                            i + 1));
                }
            }
        }
    }

    private void resolveAttributeQualifiedType(RosettaModelInventory inventory) {
        for (RosettaTypeInfo typeInfo : inventory.types) {
            for (int i = 0; i < typeInfo.attributes.size(); i++) {
                RosettaAttributeInfo attr = typeInfo.attributes.get(i);
                RosettaTypeInfo resolved = inventory.findType(typeInfo.namespace, attr.typeName);
                String typeNamespace = resolved == null ? null : resolved.namespace;
                String typeQualifiedName = resolved == null ? null : resolved.qualifiedName;
                typeInfo.attributes.set(i, new RosettaAttributeInfo(
                        attr.name,
                        attr.typeName,
                        typeNamespace,
                        typeQualifiedName,
                        attr.cardinality,
                        attr.sourceFile,
                        attr.sourceLine));
            }
        }
    }
}
