package cdm.migration.fpml.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cdm.migration.fpml.model.RosettaAttributeInfo;
import cdm.migration.fpml.model.RosettaModelInventory;
import cdm.migration.fpml.model.RosettaTypeInfo;

public class TypeResolver {
    public String resolveRootType(
            RosettaFunctionInfo function,
            RosettaPathExpression pathExpression,
            RosettaModelInventory oldModel,
            Map<String, String> aliasImports) {
        String typeRef = function.inputTypes.get(pathExpression.rootVariable);
        if (typeRef == null) {
            return null;
        }
        return resolveTypeRef(typeRef, oldModel, aliasImports);
    }

    public String resolveTypeRef(String typeRef, RosettaModelInventory model, Map<String, String> aliasImports) {
        if (typeRef == null) {
            return null;
        }
        if (typeRef.contains(".")) {
            String[] parts = typeRef.split("\\.");
            if (parts.length == 2 && aliasImports != null) {
                String alias = parts[0];
                String simple = parts[1];
                List<String> namespaces = namespacesForAlias(aliasImports, alias);
                String resolved = null;
                for (String ns : namespaces) {
                    String wildcardNs = ns.endsWith(".*") ? ns.substring(0, ns.length() - 2) : ns;
                    RosettaTypeInfo t = model.typeByQualifiedName.get(wildcardNs + "." + simple);
                    if (t != null) {
                        if (resolved != null && !resolved.equals(t.qualifiedName)) {
                            return null;
                        }
                        resolved = t.qualifiedName;
                    }
                }
                if (resolved != null) {
                    return resolved;
                }
            }
            RosettaTypeInfo direct = model.typeByQualifiedName.get(typeRef);
            return direct == null ? null : direct.qualifiedName;
        }
        RosettaTypeInfo bySimple = model.findType(null, typeRef);
        return bySimple == null ? null : bySimple.qualifiedName;
    }

    private List<String> namespacesForAlias(Map<String, String> aliasImports, String alias) {
        List<String> list = new ArrayList<String>();
        for (Map.Entry<String, String> e : aliasImports.entrySet()) {
            String key = e.getKey();
            int idx = key.indexOf(':');
            if (idx > 0 && alias.equals(key.substring(0, idx))) {
                list.add(e.getValue());
            }
        }
        return list;
    }

    public String resolveTerminalType(RosettaModelInventory model, String rootTypeQname, List<String> pathSegments) {
        RosettaTypeInfo current = model.typeByQualifiedName.get(rootTypeQname);
        if (current == null) {
            return null;
        }
        String terminal = rootTypeQname;
        for (String seg : pathSegments) {
            RosettaAttributeInfo attr = findAttribute(model, current, seg);
            if (attr == null) {
                return null;
            }
            terminal = attr.typeQualifiedName == null ? attr.typeName : attr.typeQualifiedName;
            current = attr.typeQualifiedName == null ? null : model.typeByQualifiedName.get(attr.typeQualifiedName);
        }
        return terminal;
    }

    public RosettaAttributeInfo resolveTerminalAttribute(RosettaModelInventory model, String rootTypeQname, List<String> pathSegments) {
        RosettaTypeInfo current = model.typeByQualifiedName.get(rootTypeQname);
        if (current == null) {
            return null;
        }
        RosettaAttributeInfo terminal = null;
        for (String seg : pathSegments) {
            terminal = findAttribute(model, current, seg);
            if (terminal == null) {
                return null;
            }
            current = terminal.typeQualifiedName == null ? null : model.typeByQualifiedName.get(terminal.typeQualifiedName);
        }
        return terminal;
    }

    private RosettaAttributeInfo findAttribute(RosettaModelInventory model, RosettaTypeInfo typeInfo, String name) {
        if (typeInfo == null) {
            return null;
        }
        RosettaAttributeInfo attr = model.findAttributeIncludingInherited(typeInfo, name);
        if (attr != null) {
            return attr;
        }
        for (RosettaAttributeInfo direct : typeInfo.attributes) {
            if (direct.name.equals(name)) {
                return direct;
            }
        }
        return null;
    }
}
