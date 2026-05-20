package cdm.migration.fpml.rewrite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdm.migration.fpml.Confidence;
import cdm.migration.fpml.analysis.IngestAnalysisResult;
import cdm.migration.fpml.analysis.RosettaFunctionInfo;
import cdm.migration.fpml.analysis.RosettaPathExpression;
import cdm.migration.fpml.analysis.TypeResolver;
import cdm.migration.fpml.mapping.PathMapping;
import cdm.migration.fpml.model.RosettaAttributeInfo;
import cdm.migration.fpml.model.RosettaModelInventory;

public class RewritePlanner {
    private static final String CONTEXT_FIELD_ONLY = "__CONTEXT_FIELD_ONLY__";
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("([A-Za-z_\\^][A-Za-z0-9_\\.\\^]*)");
    private static final Pattern EXTRACT_SOURCE_PATTERN = Pattern.compile("([A-Za-z_\\^][A-Za-z0-9_\\.\\^]*(?:\\s*->\\s*[A-Za-z_\\^][A-Za-z0-9_\\.\\^]*)*)\\s*$");
    public final List<String> unresolved = new ArrayList<String>();
    public final List<String> ambiguous = new ArrayList<String>();
    public final List<String> cardinalityWarnings = new ArrayList<String>();
    public final List<String> missingTypes = new ArrayList<String>();

    public List<RewriteCandidate> plan(
            IngestAnalysisResult ingestAnalysis,
            Set<String> reachableFunctions,
            Map<String, Map<String, PathMapping>> pathMapIndex,
            Confidence threshold,
            RosettaModelInventory oldModel,
            RosettaModelInventory newModel,
            TypeResolver typeResolver) {
        List<RewriteCandidate> out = new ArrayList<RewriteCandidate>();
        for (RosettaFunctionInfo function : ingestAnalysis.functions) {
            if (!reachableFunctions.contains(function.name)) {
                continue;
            }
            List<RosettaPathExpression> orderedPaths = new ArrayList<RosettaPathExpression>(function.pathExpressions);
            Collections.sort(orderedPaths, (a, b) -> Integer.compare(a.startOffset, b.startOffset));
            Map<String, String> aliases = ingestAnalysis.importAliasesByFile.get(function.file);
            for (RosettaPathExpression path : orderedPaths) {
                if (isMetadataKeyProjection(function, path)) {
                    continue;
                }
                if (isNonFpmlOutputAssignment(function, path)) {
                    continue;
                }
                String rootType = typeResolver.resolveRootType(function, path, oldModel, aliases);
                String effectiveRootVariable = path.rootVariable;
                List<String> effectiveOldPath = path.segments;
                if (function.aliasExpressions.containsKey(path.rootVariable)) {
                    TypeResolver.ExpandedAliasPath expanded = typeResolver.expandAliasPath(
                            function,
                            path.rootVariable,
                            path.segments,
                            oldModel,
                            aliases);
                    if (expanded != null) {
                        rootType = expanded.rootType;
                        effectiveRootVariable = expanded.rootVariable;
                        effectiveOldPath = expanded.segments;
                    }
                }
                if (rootType == null) {
                    rootType = inferContextualRootType(
                            function,
                            path,
                            orderedPaths,
                            oldModel,
                            typeResolver,
                            aliases);
                }
                if (CONTEXT_FIELD_ONLY.equals(rootType)) {
                    continue;
                }
                if (rootType == null) {
                    if (looksLikeEnumRoot(path.rootVariable)) {
                        continue;
                    }
                    if (isReferenceHrefProjection(path)) {
                        continue;
                    }
                    if (isLocalTemporalFieldProjection(function, path)) {
                        continue;
                    }
                    if (isNonFpmlRoot(function, path.rootVariable)) {
                        continue;
                    }
                    unresolved.add(function.file + ":" + path.line + " [" + function.name + "] unresolved root type for " + path.expression);
                    continue;
                }
                if (!isFpmlType(rootType)) {
                    continue;
                }
                if (!newModel.typeByQualifiedName.containsKey(rootType)) {
                    missingTypes.add(function.file + ":" + path.line + " [" + function.name + "] missing type in new model: " + rootType);
                }
                Map<String, PathMapping> perRoot = pathMapIndex.get(rootType);
                if (perRoot == null) {
                    unresolved.add(function.file + ":" + path.line + " [" + function.name + "] no mappings for root " + rootType);
                    continue;
                }
                String oldPathKey = String.join("->", effectiveOldPath);
                PathMapping mapping = perRoot.get(oldPathKey);
                if (mapping == null) {
                    if (isScalarProjectionPath(rootType, effectiveOldPath, oldModel, typeResolver)) {
                        continue;
                    }
                    unresolved.add(function.file + ":" + path.line + " [" + function.name + "] no mapping for path " + oldPathKey + " under " + rootType);
                    continue;
                }
                if (mapping.confidence == Confidence.LOW) {
                    ambiguous.add(function.file + ":" + path.line + " [" + function.name + "] low-confidence mapping " + oldPathKey + " -> "
                            + String.join("->", mapping.newPath));
                }
                if (!threshold.allows(mapping.confidence)) {
                    continue;
                }
                if (mapping.confidence == Confidence.UNRESOLVED || mapping.newPath.isEmpty()) {
                    continue;
                }
                String rewritten = effectiveRootVariable + " -> " + String.join(" -> ", mapping.newPath);
                if (rewritten.equals(path.expression)) {
                    continue;
                }
                RosettaAttributeInfo oldTerminal = typeResolver.resolveTerminalAttribute(oldModel, rootType, mapping.oldPath);
                RosettaAttributeInfo newTerminal = typeResolver.resolveTerminalAttribute(newModel, rootType, mapping.newPath);
                if (oldTerminal != null && newTerminal != null && !oldTerminal.cardinality.isCompatible(newTerminal.cardinality)) {
                    cardinalityWarnings.add(function.file + ":" + path.line + " [" + function.name + "] "
                            + path.expression + " => " + rewritten
                            + " changed cardinality " + oldTerminal.cardinality + " -> " + newTerminal.cardinality);
                }

                out.add(new RewriteCandidate(
                        function.file,
                        function.name,
                        path.line,
                        path.startOffset,
                        path.endOffset,
                        path.expression,
                        rewritten,
                        mapping.confidence,
                        mapping.reason,
                        rootType,
                        effectiveOldPath,
                        mapping.newPath));
            }
        }
        Collections.sort(out, (a, b) -> {
            int cmp = a.file.compareTo(b.file);
            if (cmp != 0) {
                return cmp;
            }
            return Integer.compare(a.startOffset, b.startOffset);
        });
        return out;
    }

    private String inferContextualRootType(
            RosettaFunctionInfo function,
            RosettaPathExpression targetPath,
            List<RosettaPathExpression> orderedPaths,
            RosettaModelInventory oldModel,
            TypeResolver typeResolver,
            Map<String, String> aliases) {
        if (targetPath.rootVariable == null || targetPath.rootVariable.trim().isEmpty()) {
            return null;
        }
        String rootToken = targetPath.rootVariable.trim();
        String extractElementType = resolveExtractElementTypeFromText(function, targetPath, oldModel, typeResolver, aliases);
        if (extractElementType != null) {
            String inferredFromExtract = inferRootTypeFromElement(rootToken, extractElementType, oldModel, typeResolver, aliases);
            if (inferredFromExtract != null) {
                return inferredFromExtract;
            }
        }
        for (RosettaPathExpression prior : orderedPaths) {
            if (prior.startOffset >= targetPath.startOffset) {
                break;
            }
            int distance = targetPath.startOffset - prior.endOffset;
            if (distance < 0 || distance > 1200) {
                continue;
            }
            if (prior.rootVariable == null) {
                continue;
            }
            String priorRoot = typeResolver.resolveRootType(function, prior, oldModel, aliases);
            if (priorRoot == null) {
                continue;
            }
            String extractedElementType = typeResolver.resolveTerminalType(oldModel, priorRoot, prior.segments);
            if (extractedElementType == null) {
                continue;
            }
            if (!isExtractContext(function.body, function.startOffset, prior, targetPath)) {
                continue;
            }
            String elementType = normalizeType(extractedElementType);
            String inferred = inferRootTypeFromElement(rootToken, elementType, oldModel, typeResolver, aliases);
            if (inferred != null) {
                return inferred;
            }
        }
        return null;
    }

    private String inferRootTypeFromElement(
            String rootToken,
            String extractedElementType,
            RosettaModelInventory oldModel,
            TypeResolver typeResolver,
            Map<String, String> aliases) {
        String elementType = normalizeType(extractedElementType);
        if ("item".equals(rootToken)) {
            return elementType;
        }
        cdm.migration.fpml.model.RosettaTypeInfo element = oldModel.typeByQualifiedName.get(elementType);
        if (element == null) {
            return null;
        }
        cdm.migration.fpml.model.RosettaAttributeInfo attr = oldModel.findAttributeIncludingInherited(element, rootToken);
        if (attr == null) {
            return null;
        }
        if (attr.typeQualifiedName != null) {
            return attr.typeQualifiedName;
        }
        String resolvedAttrType = typeResolver.resolveTypeRef(attr.typeName, oldModel, aliases);
        if (resolvedAttrType != null) {
            return resolvedAttrType;
        }
        return CONTEXT_FIELD_ONLY;
    }

    private String resolveExtractElementTypeFromText(
            RosettaFunctionInfo function,
            RosettaPathExpression targetPath,
            RosettaModelInventory oldModel,
            TypeResolver typeResolver,
            Map<String, String> aliases) {
        if (function == null || function.body == null || targetPath == null) {
            return null;
        }
        int targetStartLocal = Math.max(0, targetPath.startOffset - function.startOffset);
        if (targetStartLocal <= 0 || targetStartLocal > function.body.length()) {
            return null;
        }
        String before = function.body.substring(0, targetStartLocal);
        String beforeLower = before.toLowerCase();
        int extractIdx = beforeLower.lastIndexOf("extract");
        if (extractIdx < 0) {
            return null;
        }
        int windowStart = Math.max(0, extractIdx - 1200);
        String sourceWindow = before.substring(windowStart, extractIdx);
        Matcher sourceMatcher = EXTRACT_SOURCE_PATTERN.matcher(sourceWindow);
        if (!sourceMatcher.find()) {
            return null;
        }
        ParsedPath sourcePath = parsePath(sourceMatcher.group(1));
        if (sourcePath == null || sourcePath.root == null) {
            return null;
        }
        RosettaPathExpression pseudoPath = new RosettaPathExpression(
                function.file,
                function.name,
                targetPath.startOffset,
                targetPath.startOffset,
                targetPath.line,
                sourcePath.root,
                sourcePath.root,
                Collections.<String>emptyList());
        String sourceRootType = typeResolver.resolveRootType(function, pseudoPath, oldModel, aliases);
        if (sourceRootType == null) {
            return null;
        }
        if (sourcePath.segments.isEmpty()) {
            return normalizeType(sourceRootType);
        }
        String terminalType = typeResolver.resolveTerminalType(oldModel, sourceRootType, sourcePath.segments);
        return normalizeType(terminalType);
    }

    private ParsedPath parsePath(String expression) {
        if (expression == null) {
            return null;
        }
        String[] parts = expression.split("->");
        if (parts.length == 0) {
            return null;
        }
        String root = firstIdentifier(parts[0]);
        if (root == null) {
            return null;
        }
        List<String> segments = new ArrayList<String>();
        for (int i = 1; i < parts.length; i++) {
            String seg = firstIdentifier(parts[i]);
            if (seg != null) {
                segments.add(seg);
            }
        }
        return new ParsedPath(root, segments);
    }

    private String firstIdentifier(String text) {
        Matcher matcher = IDENTIFIER_PATTERN.matcher(text == null ? "" : text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean isExtractContext(String functionBody, int functionStartOffset, RosettaPathExpression prior, RosettaPathExpression target) {
        int priorEndLocal = Math.max(0, prior.endOffset - functionStartOffset);
        int targetStartLocal = Math.max(0, target.startOffset - functionStartOffset);
        if (targetStartLocal <= priorEndLocal || priorEndLocal >= functionBody.length()) {
            return false;
        }
        int end = Math.min(targetStartLocal, functionBody.length());
        String between = functionBody.substring(priorEndLocal, end).toLowerCase();
        return between.contains("extract");
    }

    private String normalizeType(String t) {
        if (t == null) {
            return null;
        }
        if (t.endsWith("[]")) {
            return t.substring(0, t.length() - 2);
        }
        return t;
    }

    private boolean looksLikeEnumRoot(String rootVariable) {
        if (rootVariable == null) {
            return false;
        }
        String v = rootVariable.trim();
        if (v.endsWith("Enum")) {
            return true;
        }
        return v.matches("[A-Z][A-Za-z0-9_]*");
    }

    private boolean isNonFpmlRoot(RosettaFunctionInfo function, String rootVariable) {
        if (rootVariable == null) {
            return false;
        }
        String typeRef = function.inputTypes.get(rootVariable);
        if (typeRef == null) {
            typeRef = function.aliasTypes.get(rootVariable);
        }
        if (typeRef == null) {
            return false;
        }
        String normalized = typeRef.trim().toLowerCase();
        return !normalized.startsWith("fpml.") && !normalized.contains(".fpml.");
    }

    private boolean isFpmlType(String typeQName) {
        if (typeQName == null) {
            return false;
        }
        String t = typeQName.toLowerCase();
        return t.startsWith("fpml.");
    }

    private boolean isNonFpmlOutputAssignment(RosettaFunctionInfo function, RosettaPathExpression path) {
        if (function == null || path == null || path.rootVariable == null) {
            return false;
        }
        if (function.outputName == null || function.outputType == null) {
            return false;
        }
        if (!path.rootVariable.equals(function.outputName)) {
            return false;
        }
        String outType = function.outputType.toLowerCase();
        if (outType.startsWith("fpml.") || outType.contains(".fpml.")) {
            return false;
        }
        int localStart = path.startOffset - function.startOffset;
        if (localStart < 0 || localStart > function.body.length()) {
            return false;
        }
        int lineStart = function.body.lastIndexOf('\n', Math.max(0, localStart - 1));
        lineStart = lineStart < 0 ? 0 : lineStart + 1;
        int lineEnd = function.body.indexOf('\n', localStart);
        if (lineEnd < 0) {
            lineEnd = function.body.length();
        }
        String line = function.body.substring(lineStart, lineEnd).trim();
        return line.startsWith("set " + path.rootVariable + " ->");
    }

    private boolean isScalarProjectionPath(
            String rootType,
            List<String> oldPath,
            RosettaModelInventory oldModel,
            TypeResolver typeResolver) {
        if (oldPath == null || oldPath.size() < 2) {
            return false;
        }
        List<String> prefix = oldPath.subList(0, oldPath.size() - 1);
        String tail = oldPath.get(oldPath.size() - 1);
        String prefixTerminal = typeResolver.resolveTerminalType(oldModel, rootType, prefix);
        if (prefixTerminal == null) {
            return false;
        }
        String t = prefixTerminal.toLowerCase();
        boolean scalarSource =
                "zoneddatetime".equals(t)
                        || "datetime".equals(t)
                        || "date".equals(t)
                        || "time".equals(t);
        if (!scalarSource) {
            return false;
        }
        String tailLower = tail.toLowerCase();
        return "date".equals(tailLower)
                || "time".equals(tailLower)
                || "hour".equals(tailLower)
                || "minute".equals(tailLower)
                || "second".equals(tailLower)
                || "timezone".equals(tailLower);
    }

    private boolean isReferenceHrefProjection(RosettaPathExpression path) {
        if (path == null || path.rootVariable == null || path.segments == null) {
            return false;
        }
        if (path.segments.size() != 1) {
            return false;
        }
        String seg = path.segments.get(0);
        if (!"href".equalsIgnoreCase(seg) && !"reference".equalsIgnoreCase(seg)) {
            return false;
        }
        String root = path.rootVariable;
        return root.endsWith("Reference")
                || root.endsWith("accountReference")
                || root.endsWith("partyReference");
    }

    private boolean isMetadataKeyProjection(RosettaFunctionInfo function, RosettaPathExpression path) {
        if (function == null || function.body == null || path == null) {
            return false;
        }
        int localStart = path.startOffset - function.startOffset;
        if (localStart < 0 || localStart > function.body.length()) {
            return false;
        }
        int lineStart = function.body.lastIndexOf('\n', Math.max(0, localStart - 1));
        lineStart = lineStart < 0 ? 0 : lineStart + 1;
        int lineEnd = function.body.indexOf('\n', localStart);
        if (lineEnd < 0) {
            lineEnd = function.body.length();
        }
        String line = function.body.substring(lineStart, lineEnd).toLowerCase();
        if (!line.contains("key:")) {
            return false;
        }
        int contextStart = Math.max(0, lineStart - 200);
        String prefix = function.body.substring(contextStart, lineStart).toLowerCase();
        return prefix.contains("with-meta");
    }

    private boolean isLocalTemporalFieldProjection(RosettaFunctionInfo function, RosettaPathExpression path) {
        if (function == null || function.body == null || path == null || path.rootVariable == null || path.segments == null) {
            return false;
        }
        if (path.segments.size() != 1) {
            return false;
        }
        String tail = path.segments.get(0);
        if (tail == null) {
            return false;
        }
        String tailLower = tail.toLowerCase();
        boolean temporalProjection = "date".equals(tailLower)
                || "time".equals(tailLower)
                || "hour".equals(tailLower)
                || "minute".equals(tailLower)
                || "second".equals(tailLower)
                || "timezone".equals(tailLower);
        if (!temporalProjection) {
            return false;
        }
        int localStart = path.startOffset - function.startOffset;
        if (localStart < 0 || localStart > function.body.length()) {
            return false;
        }
        int lineStart = function.body.lastIndexOf('\n', Math.max(0, localStart - 1));
        lineStart = lineStart < 0 ? 0 : lineStart + 1;
        int lineEnd = function.body.indexOf('\n', localStart);
        if (lineEnd < 0) {
            lineEnd = function.body.length();
        }
        String line = function.body.substring(lineStart, lineEnd);
        String root = path.rootVariable;
        Pattern assignmentPattern = Pattern.compile(
                "\\b" + Pattern.quote(root) + "\\b\\s*:\\s*\\b" + Pattern.quote(root) + "\\b\\s*->\\s*\\b" + Pattern.quote(tail) + "\\b",
                Pattern.CASE_INSENSITIVE);
        return assignmentPattern.matcher(line).find();
    }

    private static class ParsedPath {
        private final String root;
        private final List<String> segments;

        private ParsedPath(String root, List<String> segments) {
            this.root = root;
            this.segments = segments;
        }
    }
}
