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
            Map<String, String> aliases = ingestAnalysis.importAliasesByFile.get(function.file);
            for (RosettaPathExpression path : function.pathExpressions) {
                String rootType = typeResolver.resolveRootType(function, path, oldModel, aliases);
                if (rootType == null) {
                    if (looksLikeEnumRoot(path.rootVariable)) {
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
}
