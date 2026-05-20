package cdm.migration.fpml.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cdm.migration.fpml.Confidence;
import cdm.migration.fpml.model.RosettaAttributeInfo;
import cdm.migration.fpml.model.RosettaModelInventory;
import cdm.migration.fpml.model.RosettaTypeInfo;

public class PathMapDeriver {
    private static final int MAX_PATH_DEPTH = 8;

    public List<PathMapping> derive(
            RosettaModelInventory oldModel,
            RosettaModelInventory newModel,
            Map<String, RemovedTypeClassification> removedTypeClassifications) {
        List<PathMapping> mappings = new ArrayList<PathMapping>();
        Set<String> mappingKeys = new LinkedHashSet<String>();
        for (RosettaTypeInfo oldRoot : oldModel.types) {
            if (!"data".equals(oldRoot.kind)) {
                continue;
            }
            RosettaTypeInfo newRoot = resolveNewRoot(oldRoot, newModel);
            if (newRoot == null) {
                continue;
            }
            List<PathNode> oldPaths = collectPaths(oldModel, oldRoot, MAX_PATH_DEPTH);
            List<PathNode> newPaths = collectPaths(newModel, newRoot, MAX_PATH_DEPTH);

            Map<String, List<PathNode>> newByTerminal = new HashMap<String, List<PathNode>>();
            Set<String> newPathKeys = new LinkedHashSet<String>();
            for (PathNode newPath : newPaths) {
                newPathKeys.add(pathKey(newPath.segments));
                String terminal = newPath.segments.isEmpty() ? "" : newPath.segments.get(newPath.segments.size() - 1);
                List<PathNode> bucket = newByTerminal.get(terminal);
                if (bucket == null) {
                    bucket = new ArrayList<PathNode>();
                    newByTerminal.put(terminal, bucket);
                }
                bucket.add(newPath);
            }

            for (PathNode oldPath : oldPaths) {
                if (oldPath.segments.isEmpty()) {
                    continue;
                }
                List<String> oldSegments = oldPath.segments;
                String oldPathKey = pathKey(oldSegments);

                List<String> normalized = removeWrapperSegments(oldSegments, oldPath.intermediateTypeQualifiedNames, removedTypeClassifications);
                String normalizedKey = pathKey(normalized);

                Confidence confidence = Confidence.UNRESOLVED;
                List<String> chosenNewPath = Collections.emptyList();
                String reason;

                if (newPathKeys.contains(oldPathKey)) {
                    chosenNewPath = oldSegments;
                    confidence = Confidence.HIGH;
                    reason = "Same path exists in new model.";
                } else if (!normalized.equals(oldSegments) && newPathKeys.contains(normalizedKey)) {
                    chosenNewPath = normalized;
                    confidence = confidenceFromWrapperTypes(oldPath.intermediateTypeQualifiedNames, removedTypeClassifications);
                    reason = "Removed wrapper-like intermediate segments.";
                } else {
                    String terminal = oldSegments.get(oldSegments.size() - 1);
                    List<PathNode> terminalMatches = newByTerminal.get(terminal);
                    if (terminalMatches != null && terminalMatches.size() == 1) {
                        chosenNewPath = terminalMatches.get(0).segments;
                        confidence = Confidence.MEDIUM;
                        reason = "Single terminal-name match in target root type.";
                    } else if (terminalMatches != null && terminalMatches.size() > 1) {
                        chosenNewPath = terminalMatches.get(0).segments;
                        confidence = Confidence.LOW;
                        reason = "Multiple terminal-name matches.";
                    } else {
                        reason = "No candidate path in new model.";
                    }
                }

                String key = oldRoot.qualifiedName + "::" + oldPathKey;
                if (mappingKeys.add(key)) {
                    mappings.add(new PathMapping(oldRoot.qualifiedName, oldSegments, chosenNewPath, confidence, reason));
                }
            }
        }
        return mappings;
    }

    private RosettaTypeInfo resolveNewRoot(RosettaTypeInfo oldRoot, RosettaModelInventory newModel) {
        RosettaTypeInfo direct = newModel.typeByQualifiedName.get(oldRoot.qualifiedName);
        if (direct != null) {
            return direct;
        }
        List<RosettaTypeInfo> simple = newModel.typeBySimpleName.get(oldRoot.simpleName);
        if (simple != null && simple.size() == 1) {
            return simple.get(0);
        }
        return null;
    }

    private List<PathNode> collectPaths(RosettaModelInventory model, RosettaTypeInfo rootType, int maxDepth) {
        List<PathNode> result = new ArrayList<PathNode>();
        collectPathsRec(model, rootType, new ArrayList<String>(), new ArrayList<String>(), new LinkedHashSet<String>(), maxDepth, result);
        return result;
    }

    private void collectPathsRec(
            RosettaModelInventory model,
            RosettaTypeInfo currentType,
            List<String> path,
            List<String> intermediateTypes,
            Set<String> visitedTypePath,
            int remainingDepth,
            List<PathNode> out) {
        if (currentType == null || remainingDepth <= 0) {
            return;
        }
        if (!visitedTypePath.add(currentType.qualifiedName + "/" + pathKey(path))) {
            return;
        }
        for (RosettaAttributeInfo attr : model.attributesIncludingInherited(currentType)) {
            List<String> childPath = new ArrayList<String>(path);
            childPath.add(attr.name);
            List<String> childIntermediates = new ArrayList<String>(intermediateTypes);
            if (attr.typeQualifiedName != null) {
                childIntermediates.add(attr.typeQualifiedName);
            }
            out.add(new PathNode(childPath, childIntermediates));
            RosettaTypeInfo nextType = attr.typeQualifiedName == null ? null : model.typeByQualifiedName.get(attr.typeQualifiedName);
            collectPathsRec(model, nextType, childPath, childIntermediates, visitedTypePath, remainingDepth - 1, out);
        }
    }

    private Confidence confidenceFromWrapperTypes(List<String> typePath, Map<String, RemovedTypeClassification> removedTypeClassifications) {
        boolean hasPossibleWrapper = false;
        for (String typeQName : typePath) {
            RemovedTypeClassification c = removedTypeClassifications.get(typeQName);
            if (c == RemovedTypeClassification.POSSIBLE_WRAPPER) {
                hasPossibleWrapper = true;
            }
            if (c == RemovedTypeClassification.POSSIBLE_DOMAIN_TYPE || c == RemovedTypeClassification.UNKNOWN) {
                return Confidence.MEDIUM;
            }
        }
        if (hasPossibleWrapper) {
            return Confidence.MEDIUM;
        }
        return Confidence.HIGH;
    }

    private List<String> removeWrapperSegments(
            List<String> segments,
            List<String> intermediateTypes,
            Map<String, RemovedTypeClassification> removedTypeClassifications) {
        List<String> out = new ArrayList<String>();
        for (int i = 0; i < segments.size(); i++) {
            String typeName = i < intermediateTypes.size() ? intermediateTypes.get(i) : null;
            RemovedTypeClassification classification = typeName == null ? null : removedTypeClassifications.get(typeName);
            boolean drop = classification == RemovedTypeClassification.LIKELY_WRAPPER
                    || classification == RemovedTypeClassification.POSSIBLE_WRAPPER;
            if (!drop || i == segments.size() - 1) {
                out.add(segments.get(i));
            }
        }
        return out;
    }

    private String pathKey(List<String> path) {
        return String.join("->", path);
    }

    public Map<String, Map<String, PathMapping>> indexByRootAndOldPath(List<PathMapping> mappings) {
        Map<String, Map<String, PathMapping>> index = new LinkedHashMap<String, Map<String, PathMapping>>();
        for (PathMapping mapping : mappings) {
            Map<String, PathMapping> perRoot = index.get(mapping.rootType);
            if (perRoot == null) {
                perRoot = new LinkedHashMap<String, PathMapping>();
                index.put(mapping.rootType, perRoot);
            }
            perRoot.put(mapping.oldPathKey(), mapping);
        }
        return index;
    }

    private static class PathNode {
        private final List<String> segments;
        private final List<String> intermediateTypeQualifiedNames;

        private PathNode(List<String> segments, List<String> intermediateTypeQualifiedNames) {
            this.segments = segments;
            this.intermediateTypeQualifiedNames = intermediateTypeQualifiedNames;
        }
    }
}
