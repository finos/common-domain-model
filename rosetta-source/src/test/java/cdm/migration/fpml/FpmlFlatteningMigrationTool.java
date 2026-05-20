package cdm.migration.fpml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdm.migration.fpml.analysis.CallGraphBuilder;
import cdm.migration.fpml.analysis.IngestAnalysisResult;
import cdm.migration.fpml.analysis.IngestRosettaAnalyzer;
import cdm.migration.fpml.analysis.ReachabilityAnalyzer;
import cdm.migration.fpml.analysis.RosettaFunctionInfo;
import cdm.migration.fpml.analysis.RosettaPathExpression;
import cdm.migration.fpml.analysis.TypeResolver;
import cdm.migration.fpml.mapping.PathMapDeriver;
import cdm.migration.fpml.mapping.PathMapping;
import cdm.migration.fpml.mapping.RemovedTypeClassification;
import cdm.migration.fpml.mapping.RemovedTypeClassifier;
import cdm.migration.fpml.model.ModelInventoryExtractor;
import cdm.migration.fpml.model.RosettaAttributeInfo;
import cdm.migration.fpml.model.RosettaModelInventory;
import cdm.migration.fpml.model.RosettaTypeInfo;
import cdm.migration.fpml.report.JsonWriter;
import cdm.migration.fpml.report.MarkdownReportWriter;
import cdm.migration.fpml.report.MigrationReportWriter;
import cdm.migration.fpml.rewrite.DiffWriter;
import cdm.migration.fpml.rewrite.MissingTypeInputExpander;
import cdm.migration.fpml.rewrite.RewriteCandidate;
import cdm.migration.fpml.rewrite.RewritePlanner;
import cdm.migration.fpml.rewrite.SourceRewriter;
import cdm.migration.fpml.rewrite.TextEdit;
import cdm.migration.fpml.source.JarRosettaSourceLoader;
import cdm.migration.fpml.source.LocalRosettaSourceLoader;
import cdm.migration.fpml.source.RosettaSourceFile;

public class FpmlFlatteningMigrationTool {
    public static void main(String[] args) throws Exception {
        MigrationConfig config = args != null && args.length > 0
                ? MigrationConfig.parse(args)
                : defaultIntellijConfig();
        new FpmlFlatteningMigrationTool().run(config);
    }

    private static MigrationConfig defaultIntellijConfig() {
        Path projectRoot = Paths.get(".").toAbsolutePath().normalize();
        String home = System.getProperty("user.home");
        Path oldFpmlJar = Paths.get(home, ".m2", "repository", "com", "regnosys", "rune-fpml", "rosetta-source", "1.5.3",
                "rosetta-source-1.5.3.jar");
        Path newFpmlJar = Paths.get(home, ".m2", "repository", "com", "regnosys", "rune-fpml", "rosetta-source",
                "0.0.0.master-SNAPSHOT", "rosetta-source-0.0.0.master-SNAPSHOT.jar");
        List<String> entryFunctions = new ArrayList<String>();
        entryFunctions.add("Ingest_FpmlConfirmationToTradeState");
        entryFunctions.add("Ingest_FpmlConfirmationToWorkflowStep");
        return new MigrationConfig(
                projectRoot,
                oldFpmlJar,
                newFpmlJar,
                "fpml/rosetta",
                "fpml/rosetta",
                projectRoot.resolve("rosetta-source/src/main/rosetta"),
                "ingest-*.rosetta",
                entryFunctions,
                Confidence.MEDIUM,
                FidelityMode.MAXIMUM,
                true,
                false,
                projectRoot.resolve("target/fpml-migration"));
    }

    public void run(MigrationConfig config) throws Exception {
        Files.createDirectories(config.outputDir);

        JarRosettaSourceLoader jarLoader = new JarRosettaSourceLoader();
        LocalRosettaSourceLoader localLoader = new LocalRosettaSourceLoader();
        ModelInventoryExtractor modelExtractor = new ModelInventoryExtractor();
        RemovedTypeClassifier removedTypeClassifier = new RemovedTypeClassifier();
        PathMapDeriver pathMapDeriver = new PathMapDeriver();
        IngestRosettaAnalyzer ingestAnalyzer = new IngestRosettaAnalyzer();
        CallGraphBuilder callGraphBuilder = new CallGraphBuilder();
        ReachabilityAnalyzer reachabilityAnalyzer = new ReachabilityAnalyzer();
        TypeResolver typeResolver = new TypeResolver();
        RewritePlanner rewritePlanner = new RewritePlanner();
        MissingTypeInputExpander missingTypeInputExpander = new MissingTypeInputExpander();
        SourceRewriter sourceRewriter = new SourceRewriter();
        DiffWriter diffWriter = new DiffWriter();
        JsonWriter jsonWriter = new JsonWriter();
        MarkdownReportWriter markdownWriter = new MarkdownReportWriter();
        MigrationReportWriter migrationReportWriter = new MigrationReportWriter();

        List<RosettaSourceFile> oldFpmlFiles = jarLoader.load(config.oldFpmlJar, config.oldFpmlRootInJar);
        List<RosettaSourceFile> newFpmlFiles = jarLoader.load(config.newFpmlJar, config.newFpmlRootInJar);
        List<RosettaSourceFile> ingestFiles = localLoader.load(config.cdmRosettaDir, config.fileMask);

        RosettaModelInventory oldModel = modelExtractor.extract(oldFpmlFiles);
        RosettaModelInventory newModel = modelExtractor.extract(newFpmlFiles);
        Map<String, RemovedTypeClassification> removedTypes = removedTypeClassifier.classifyRemovedTypes(oldModel, newModel);
        List<PathMapping> pathMappings = pathMapDeriver.derive(oldModel, newModel, removedTypes);
        Map<String, Map<String, PathMapping>> pathMapIndex = pathMapDeriver.indexByRootAndOldPath(pathMappings);
        IngestAnalysisResult ingestAnalysis = ingestAnalyzer.analyze(ingestFiles);
        Map<String, Set<String>> callGraph = callGraphBuilder.build(ingestAnalysis);
        Set<String> reachableFunctions = reachabilityAnalyzer.reachableFunctions(callGraph, config.entryFunctions);
        List<RewriteCandidate> candidates = rewritePlanner.plan(
                ingestAnalysis,
                reachableFunctions,
                pathMapIndex,
                config.confidence,
                oldModel,
                newModel,
                typeResolver);
        List<TextEdit> inputExpansionEdits = missingTypeInputExpander.plan(
                ingestAnalysis,
                reachableFunctions,
                oldModel,
                newModel,
                removedTypes,
                typeResolver);
        List<String> filteredMissingTypes = filterHandledMissingTypes(rewritePlanner.missingTypes,
                missingTypeInputExpander.handledMissingTypeKeys);
        rewritePlanner.missingTypes.clear();
        rewritePlanner.missingTypes.addAll(filteredMissingTypes);
        SourceRewriter.RewriteResult rewriteResult = sourceRewriter.rewriteWithAdditionalEdits(candidates, inputExpansionEdits, config.apply);
        String diffText = diffWriter.buildDiff(rewriteResult.originalByFile, rewriteResult.rewrittenByFile);

        writeOutputs(
                config,
                oldModel,
                newModel,
                removedTypes,
                pathMappings,
                ingestAnalysis,
                callGraph,
                reachableFunctions,
                candidates,
                inputExpansionEdits,
                rewritePlanner,
                missingTypeInputExpander,
                rewriteResult,
                diffText,
                jsonWriter,
                markdownWriter,
                migrationReportWriter);
    }

    private void writeOutputs(
            MigrationConfig config,
            RosettaModelInventory oldModel,
            RosettaModelInventory newModel,
            Map<String, RemovedTypeClassification> removedTypes,
            List<PathMapping> pathMappings,
            IngestAnalysisResult ingestAnalysis,
            Map<String, Set<String>> callGraph,
            Set<String> reachableFunctions,
            List<RewriteCandidate> candidates,
            List<TextEdit> inputExpansionEdits,
            RewritePlanner rewritePlanner,
            MissingTypeInputExpander missingTypeInputExpander,
            SourceRewriter.RewriteResult rewriteResult,
            String diffText,
            JsonWriter jsonWriter,
            MarkdownReportWriter markdownWriter,
            MigrationReportWriter migrationReportWriter) throws IOException {
        Path out = config.outputDir;

        jsonWriter.writeJson(out.resolve("old-model.json"), inventoryToJson(oldModel));
        jsonWriter.writeJson(out.resolve("new-model.json"), inventoryToJson(newModel));
        jsonWriter.writeJson(out.resolve("path-map.json"), pathMapToJson(pathMappings));
        jsonWriter.writeJson(out.resolve("ingest-analysis.json"), ingestAnalysisToJson(ingestAnalysis));
        jsonWriter.writeJson(out.resolve("rewrite-candidates.json"), rewriteCandidatesToJson(candidates));
        jsonWriter.writeJson(out.resolve("input-expansion-edits.json"), inputExpansionEditsToJson(inputExpansionEdits));

        markdownWriter.writeLines(out.resolve("removed-types.md"), "Removed Types", removedTypesLines(removedTypes));
        markdownWriter.writeLines(out.resolve("path-map-report.md"), "Path Map Report", pathMapReportLines(pathMappings));
        markdownWriter.writeCallGraph(out.resolve("call-graph.md"), callGraph);
        markdownWriter.writeLines(out.resolve("reachable-functions.md"), "Reachable Functions", new ArrayList<String>(reachableFunctions));
        markdownWriter.writeLines(out.resolve("ambiguous-paths.md"), "Ambiguous Paths", rewritePlanner.ambiguous);
        markdownWriter.writeLines(out.resolve("unresolved-paths.md"), "Unresolved Paths", rewritePlanner.unresolved);
        markdownWriter.writeLines(out.resolve("missing-types.md"), "Missing Types", rewritePlanner.missingTypes);
        markdownWriter.writeLines(out.resolve("missing-type-input-expansions.md"), "Missing Type Input Expansions",
                missingTypeInputExpander.expansionReport);
        markdownWriter.writeLines(out.resolve("cardinality-warnings.md"), "Cardinality Warnings", rewritePlanner.cardinalityWarnings);
        markdownWriter.writeLines(out.resolve("medium-confidence-rewrites.md"), "Medium Confidence Rewrites", mediumCandidates(candidates));

        Files.write(out.resolve("applied-rewrites.diff"), diffText.getBytes(StandardCharsets.UTF_8));
        Files.write(out.resolve("changed-files.txt"), String.join("\n", rewriteResult.changedFiles).getBytes(StandardCharsets.UTF_8));

        Map<String, Object> metrics = buildMetrics(oldModel, newModel, removedTypes, ingestAnalysis, reachableFunctions, candidates, rewritePlanner,
                rewriteResult);
        metrics.put("inputExpansionEdits", inputExpansionEdits.size());
        migrationReportWriter.writeMigrationReport(out.resolve("migration-report.md"), metrics);
    }

    private List<String> mediumCandidates(List<RewriteCandidate> candidates) {
        List<String> lines = new ArrayList<String>();
        for (RewriteCandidate c : candidates) {
            if (c.confidence == Confidence.MEDIUM) {
                lines.add(c.file + ":" + c.line + " [" + c.function + "] " + c.oldExpression + " -> " + c.newExpression);
            }
        }
        return lines;
    }

    private Map<String, Object> buildMetrics(
            RosettaModelInventory oldModel,
            RosettaModelInventory newModel,
            Map<String, RemovedTypeClassification> removedTypes,
            IngestAnalysisResult ingestAnalysis,
            Set<String> reachableFunctions,
            List<RewriteCandidate> candidates,
            RewritePlanner rewritePlanner,
            SourceRewriter.RewriteResult rewriteResult) {
        Map<String, Object> metrics = new HashMap<String, Object>();
        int pathCount = 0;
        for (RosettaFunctionInfo fn : ingestAnalysis.functions) {
            pathCount += fn.pathExpressions.size();
        }
        int highCount = 0;
        int mediumCount = 0;
        for (RewriteCandidate candidate : candidates) {
            if (candidate.confidence == Confidence.HIGH) {
                highCount++;
            } else if (candidate.confidence == Confidence.MEDIUM) {
                mediumCount++;
            }
        }
        int likelyWrapperCount = 0;
        for (RemovedTypeClassification c : removedTypes.values()) {
            if (c == RemovedTypeClassification.LIKELY_WRAPPER) {
                likelyWrapperCount++;
            }
        }
        metrics.put("oldTypeCount", oldModel.types.size());
        metrics.put("newTypeCount", newModel.types.size());
        metrics.put("removedTypeCount", removedTypes.size());
        metrics.put("likelyWrapperCount", likelyWrapperCount);
        metrics.put("ingestFileCount", ingestAnalysis.importAliasesByFile.size());
        metrics.put("functionCount", ingestAnalysis.functions.size());
        metrics.put("reachableFunctionCount", reachableFunctions.size());
        metrics.put("pathExpressionCount", pathCount);
        metrics.put("resolvedPathCount", candidates.size());
        metrics.put("unresolvedPathCount", rewritePlanner.unresolved.size());
        metrics.put("highRewriteCount", highCount);
        metrics.put("mediumRewriteCount", mediumCount);
        metrics.put("lowSkippedCount", rewritePlanner.ambiguous.size());
        metrics.put("missingTypeCount", rewritePlanner.missingTypes.size());
        metrics.put("cardinalityWarningCount", rewritePlanner.cardinalityWarnings.size());
        metrics.put("changedFileCount", rewriteResult.changedFiles.size());
        return metrics;
    }

    private List<String> removedTypesLines(Map<String, RemovedTypeClassification> removedTypes) {
        List<String> lines = new ArrayList<String>();
        for (Map.Entry<String, RemovedTypeClassification> e : removedTypes.entrySet()) {
            lines.add(e.getKey() + " : " + e.getValue());
        }
        return lines;
    }

    private List<String> pathMapReportLines(List<PathMapping> mappings) {
        List<String> lines = new ArrayList<String>();
        for (PathMapping mapping : mappings) {
            lines.add(mapping.rootType + " :: " + String.join(" -> ", mapping.oldPath) + " => "
                    + String.join(" -> ", mapping.newPath) + " [" + mapping.confidence + "] " + mapping.reason);
        }
        return lines;
    }

    private Object inventoryToJson(RosettaModelInventory inventory) {
        List<Object> types = new ArrayList<Object>();
        for (RosettaTypeInfo type : inventory.types) {
            Map<String, Object> t = new LinkedHashMap<String, Object>();
            t.put("namespace", type.namespace);
            t.put("simpleName", type.simpleName);
            t.put("qualifiedName", type.qualifiedName);
            t.put("kind", type.kind);
            t.put("sourceFile", type.sourceFile);
            t.put("sourceLine", type.sourceLine);
            List<Object> attrs = new ArrayList<Object>();
            for (RosettaAttributeInfo attr : type.attributes) {
                Map<String, Object> a = new LinkedHashMap<String, Object>();
                a.put("name", attr.name);
                a.put("typeName", attr.typeName);
                a.put("typeNamespace", attr.typeNamespace);
                a.put("typeQualifiedName", attr.typeQualifiedName);
                Map<String, Object> c = new LinkedHashMap<String, Object>();
                c.put("min", attr.cardinality.min);
                c.put("max", attr.cardinality.max);
                a.put("cardinality", c);
                a.put("sourceFile", attr.sourceFile);
                a.put("sourceLine", attr.sourceLine);
                attrs.add(a);
            }
            t.put("attributes", attrs);
            types.add(t);
        }
        Map<String, Object> root = new LinkedHashMap<String, Object>();
        root.put("typeCount", inventory.types.size());
        root.put("types", types);
        return root;
    }

    private Object pathMapToJson(List<PathMapping> mappings) {
        List<Object> list = new ArrayList<Object>();
        for (PathMapping mapping : mappings) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("rootType", mapping.rootType);
            item.put("oldPath", new ArrayList<String>(mapping.oldPath));
            item.put("newPath", new ArrayList<String>(mapping.newPath));
            item.put("confidence", mapping.confidence.name());
            item.put("reason", mapping.reason);
            list.add(item);
        }
        return list;
    }

    private Object ingestAnalysisToJson(IngestAnalysisResult analysis) {
        List<Object> functions = new ArrayList<Object>();
        for (RosettaFunctionInfo fn : analysis.functions) {
            Map<String, Object> f = new LinkedHashMap<String, Object>();
            f.put("file", fn.file);
            f.put("namespace", fn.namespace);
            f.put("name", fn.name);
            f.put("startLine", fn.startLine);
            f.put("inputs", new LinkedHashMap<String, String>(fn.inputTypes));
            f.put("calls", new ArrayList<String>(fn.calledFunctions));
            List<Object> paths = new ArrayList<Object>();
            for (RosettaPathExpression p : fn.pathExpressions) {
                Map<String, Object> x = new LinkedHashMap<String, Object>();
                x.put("line", p.line);
                x.put("expression", p.expression);
                x.put("rootVariable", p.rootVariable);
                x.put("segments", new ArrayList<String>(p.segments));
                x.put("startOffset", p.startOffset);
                x.put("endOffset", p.endOffset);
                paths.add(x);
            }
            f.put("paths", paths);
            functions.add(f);
        }
        Map<String, Object> root = new LinkedHashMap<String, Object>();
        root.put("functionCount", analysis.functions.size());
        root.put("functions", functions);
        root.put("importAliasesByFile", analysis.importAliasesByFile);
        return root;
    }

    private Object rewriteCandidatesToJson(List<RewriteCandidate> candidates) {
        List<Object> list = new ArrayList<Object>();
        for (RewriteCandidate c : candidates) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("file", c.file);
            item.put("function", c.function);
            item.put("line", c.line);
            item.put("oldExpression", c.oldExpression);
            item.put("newExpression", c.newExpression);
            item.put("confidence", c.confidence.name());
            item.put("reason", c.reason);
            item.put("rootType", c.rootType);
            item.put("oldPath", new ArrayList<String>(c.oldPath));
            item.put("newPath", new ArrayList<String>(c.newPath));
            list.add(item);
        }
        return list;
    }

    private Object inputExpansionEditsToJson(List<TextEdit> edits) {
        List<Object> list = new ArrayList<Object>();
        for (TextEdit edit : edits) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("file", edit.file);
            item.put("startOffset", edit.startOffset);
            item.put("endOffset", edit.endOffset);
            item.put("replacement", edit.replacement);
            item.put("reason", edit.reason);
            list.add(item);
        }
        return list;
    }

    private List<String> filterHandledMissingTypes(List<String> missingTypes, Set<String> handledKeys) {
        if (missingTypes.isEmpty() || handledKeys.isEmpty()) {
            return missingTypes;
        }
        Pattern pattern = Pattern.compile(".*\\[(.+?)\\]\\s+missing type in new model:\\s+(.+)$");
        List<String> out = new ArrayList<String>();
        for (String row : missingTypes) {
            Matcher matcher = pattern.matcher(row);
            if (matcher.matches()) {
                String key = matcher.group(1) + "::" + matcher.group(2).trim();
                if (handledKeys.contains(key)) {
                    continue;
                }
            }
            out.add(row);
        }
        return out;
    }
}
