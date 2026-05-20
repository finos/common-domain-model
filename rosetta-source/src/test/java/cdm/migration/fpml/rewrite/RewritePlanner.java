package cdm.migration.fpml.rewrite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                String rootType = typeResolver.resolveRootType(function, path, oldModel, aliases);
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
                    if (isNonFpmlRoot(function, path.rootVariable)) {
                        continue;
                    }
                    unresolved.add(function.file + ":" + path.line + " [" + function.name + "] unresolved root type for " + path.expression);
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
                String oldPathKey = String.join("->", path.segments);
                PathMapping mapping = perRoot.get(oldPathKey);
                if (mapping == null) {
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
                String rewritten = path.rootVariable + " -> " + String.join(" -> ", mapping.newPath);
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
                        path.segments,
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
            if ("item".equals(rootToken)) {
                return elementType;
            }
            cdm.migration.fpml.model.RosettaTypeInfo element = oldModel.typeByQualifiedName.get(elementType);
            if (element == null) {
                continue;
            }
            cdm.migration.fpml.model.RosettaAttributeInfo attr = oldModel.findAttributeIncludingInherited(element, rootToken);
            if (attr == null) {
                continue;
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
}
