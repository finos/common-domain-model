package cdm.migration.fpml.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdm.migration.fpml.model.RosettaAttributeInfo;
import cdm.migration.fpml.model.RosettaModelInventory;
import cdm.migration.fpml.model.RosettaTypeInfo;

public class TypeResolver {
    private static final List<String> OPERATOR_TOKENS = Arrays.asList(
            "first", "last", "extract", "filter", "then", "switch", "with-meta", "to-enum", "to-string", "distinct", "only-element");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("([A-Za-z_\\^][A-Za-z0-9_\\.\\^]*)");

    public String resolveRootType(
            RosettaFunctionInfo function,
            RosettaPathExpression pathExpression,
            RosettaModelInventory oldModel,
            Map<String, String> aliasImports) {
        String typeRef = function.inputTypes.get(pathExpression.rootVariable);
        if (typeRef == null) {
            typeRef = function.aliasTypes.get(pathExpression.rootVariable);
        }
        if (typeRef == null) {
            return resolveAliasTypeByExpression(function, pathExpression.rootVariable, oldModel, aliasImports, new HashSet<String>());
        }
        return resolveTypeRef(typeRef, oldModel, aliasImports);
    }

    public ExpandedAliasPath expandAliasPath(
            RosettaFunctionInfo function,
            String aliasName,
            List<String> suffixSegments,
            RosettaModelInventory oldModel,
            Map<String, String> aliasImports) {
        return expandAliasPathRecursive(
                function,
                aliasName,
                suffixSegments,
                oldModel,
                aliasImports,
                new HashSet<String>());
    }

    private ExpandedAliasPath expandAliasPathRecursive(
            RosettaFunctionInfo function,
            String aliasName,
            List<String> suffixSegments,
            RosettaModelInventory oldModel,
            Map<String, String> aliasImports,
            Set<String> visitedAliases) {
        if (aliasName == null || !visitedAliases.add(aliasName)) {
            return null;
        }
        String expr = function.aliasExpressions.get(aliasName);
        if (expr == null) {
            return null;
        }
        ParsedPath aliasPath = parsePath(expr);
        if (aliasPath == null) {
            return null;
        }
        List<String> combined = new ArrayList<String>();
        combined.addAll(aliasPath.segments);
        if (suffixSegments != null) {
            combined.addAll(suffixSegments);
        }
        if (function.aliasExpressions.containsKey(aliasPath.rootToken)) {
            ExpandedAliasPath expandedRoot = expandAliasPathRecursive(
                    function,
                    aliasPath.rootToken,
                    combined,
                    oldModel,
                    aliasImports,
                    visitedAliases);
            if (expandedRoot != null) {
                return expandedRoot;
            }
        }
        String baseRootType = resolveSymbolType(
                function,
                aliasPath.rootToken,
                oldModel,
                aliasImports,
                new HashSet<String>(visitedAliases));
        if (baseRootType == null) {
            return null;
        }
        return new ExpandedAliasPath(aliasPath.rootToken, baseRootType, combined);
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

    private String resolveAliasTypeByExpression(
            RosettaFunctionInfo function,
            String aliasName,
            RosettaModelInventory model,
            Map<String, String> aliasImports,
            Set<String> visitedAliases) {
        if (aliasName == null || !visitedAliases.add(aliasName)) {
            return null;
        }
        String expr = function.aliasExpressions.get(aliasName);
        if (expr == null) {
            return null;
        }
        ParsedPath p = parsePath(expr);
        if (p == null || p.segments.isEmpty()) {
            return null;
        }
        String rootType = resolveSymbolType(function, p.rootToken, model, aliasImports, visitedAliases);
        if (rootType == null) {
            return null;
        }
        String terminal = resolveTerminalType(model, rootType, p.segments);
        if (terminal != null) {
            function.aliasTypes.put(aliasName, terminal);
        }
        return terminal;
    }

    private String resolveSymbolType(
            RosettaFunctionInfo function,
            String symbol,
            RosettaModelInventory model,
            Map<String, String> aliasImports,
            Set<String> visitedAliases) {
        String typeRef = function.inputTypes.get(symbol);
        if (typeRef != null) {
            return resolveTypeRef(typeRef, model, aliasImports);
        }
        String aliasTypeRef = function.aliasTypes.get(symbol);
        if (aliasTypeRef != null) {
            String resolved = resolveTypeRef(aliasTypeRef, model, aliasImports);
            return resolved == null ? aliasTypeRef : resolved;
        }
        if (function.aliasExpressions.containsKey(symbol)) {
            return resolveAliasTypeByExpression(function, symbol, model, aliasImports, visitedAliases);
        }
        return null;
    }

    private ParsedPath parsePath(String expression) {
        if (expression == null || !expression.contains("->")) {
            return null;
        }
        String[] parts = expression.split("->");
        if (parts.length < 2) {
            return null;
        }
        String root = firstIdentifier(parts[0]);
        if (root == null) {
            return null;
        }
        List<String> segments = new ArrayList<String>();
        for (int i = 1; i < parts.length; i++) {
            String token = firstIdentifier(parts[i]);
            if (token == null) {
                continue;
            }
            if (isOperatorOnly(parts[i], token)) {
                continue;
            }
            segments.add(token);
        }
        if (segments.isEmpty()) {
            return null;
        }
        return new ParsedPath(root, segments);
    }

    private String firstIdentifier(String text) {
        Matcher m = IDENTIFIER_PATTERN.matcher(text == null ? "" : text);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private boolean isOperatorOnly(String part, String token) {
        String normalized = part == null ? "" : part.trim().toLowerCase();
        return OPERATOR_TOKENS.contains(token.toLowerCase()) && normalized.equals(token.toLowerCase());
    }

    private static class ParsedPath {
        private final String rootToken;
        private final List<String> segments;

        private ParsedPath(String rootToken, List<String> segments) {
            this.rootToken = rootToken;
            this.segments = segments;
        }
    }

    public static class ExpandedAliasPath {
        public final String rootVariable;
        public final String rootType;
        public final List<String> segments;

        public ExpandedAliasPath(String rootVariable, String rootType, List<String> segments) {
            this.rootVariable = rootVariable;
            this.rootType = rootType;
            this.segments = segments;
        }
    }
}
