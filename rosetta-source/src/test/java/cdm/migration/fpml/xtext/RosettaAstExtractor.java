package cdm.migration.fpml.xtext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import com.regnosys.rosetta.rosetta.Import;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.rosetta.RosettaRootElement;
import com.regnosys.rosetta.rosetta.RosettaSymbol;
import com.regnosys.rosetta.rosetta.expression.RosettaDeepFeatureCall;
import com.regnosys.rosetta.rosetta.expression.RosettaExpression;
import com.regnosys.rosetta.rosetta.expression.RosettaFeatureCall;
import com.regnosys.rosetta.rosetta.expression.RosettaSymbolReference;
import com.regnosys.rosetta.rosetta.simple.Function;

import cdm.migration.fpml.analysis.IngestAnalysisResult;
import cdm.migration.fpml.analysis.RosettaFunctionInfo;
import cdm.migration.fpml.analysis.RosettaPathExpression;
import cdm.migration.fpml.source.RosettaSourceFile;

public class RosettaAstExtractor {
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("([A-Za-z_\\^][A-Za-z0-9_\\.\\^]*)");
    private static final List<String> OPERATORS = Arrays.asList(
            "first", "last", "only-element", "extract", "filter", "then", "switch", "with-meta", "to-enum", "to-string", "distinct");

    public void enrichIngestResult(RosettaModel model, RosettaSourceFile file, IngestAnalysisResult result) {
        if (model == null) {
            return;
        }
        Map<String, RosettaFunctionInfo> fileFunctionsByName = new HashMap<String, RosettaFunctionInfo>();
        for (RosettaFunctionInfo fn : result.functions) {
            if (file.getLogicalPath().equals(fn.file)) {
                fileFunctionsByName.put(fn.name, fn);
            }
        }
        Map<String, String> aliases = new LinkedHashMap<String, String>();
        for (Import imp : model.getImports()) {
            if (imp.getNamespaceAlias() != null && imp.getImportedNamespace() != null) {
                aliases.put(imp.getNamespaceAlias() + ":" + imp.getImportedNamespace(), imp.getImportedNamespace());
            }
        }
        if (!aliases.isEmpty()) {
            result.importAliasesByFile.put(file.getLogicalPath(), aliases);
        }

        for (RosettaRootElement element : model.getElements()) {
            if (!(element instanceof Function)) {
                continue;
            }
            Function function = (Function) element;
            RosettaFunctionInfo target = fileFunctionsByName.get(function.getName());
            if (target == null) {
                continue;
            }
            List<RosettaPathExpression> astPaths = extractPaths(function, target, file.getContent());
            if (!astPaths.isEmpty()) {
                target.pathExpressions.clear();
                target.pathExpressions.addAll(astPaths);
            }
        }
    }

    private List<RosettaPathExpression> extractPaths(Function function, RosettaFunctionInfo target, String fileContent) {
        Map<Integer, RosettaPathExpression> bestByOffset = new LinkedHashMap<Integer, RosettaPathExpression>();
        TreeIterator<EObject> it = function.eAllContents();
        while (it.hasNext()) {
            EObject obj = it.next();
            if (!(obj instanceof RosettaExpression)) {
                continue;
            }
            if (!(obj instanceof RosettaFeatureCall) && !(obj instanceof RosettaDeepFeatureCall)) {
                continue;
            }
            RosettaExpression expr = (RosettaExpression) obj;
            ICompositeNode node = NodeModelUtils.getNode(expr);
            if (node == null) {
                continue;
            }
            String expressionText = node.getText().replaceAll("\\s+", " ").trim();
            ParsedPath parsedPath = parsePath(expressionText);
            if (parsedPath == null || parsedPath.segments.isEmpty()) {
                continue;
            }
            if (isEnumRoot(expr)) {
                continue;
            }
            int start = node.getOffset();
            int end = node.getOffset() + node.getLength();
            RosettaPathExpression path = new RosettaPathExpression(
                    target.file,
                    target.name,
                    start,
                    end,
                    lineOfOffset(fileContent, start),
                    expressionText,
                    parsedPath.rootVariable,
                    parsedPath.segments);
            RosettaPathExpression existing = bestByOffset.get(start);
            if (existing == null || (existing.endOffset - existing.startOffset) < (path.endOffset - path.startOffset)) {
                bestByOffset.put(start, path);
            }
        }
        return new ArrayList<RosettaPathExpression>(bestByOffset.values());
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
        if (root == null || root.endsWith("Enum")) {
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

    private boolean isOperatorOnly(String part, String token) {
        String normalized = part.trim().toLowerCase();
        return OPERATORS.contains(token.toLowerCase()) && normalized.equals(token.toLowerCase());
    }

    private String firstIdentifier(String text) {
        if (text == null) {
            return null;
        }
        Matcher matcher = IDENTIFIER_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private int lineOfOffset(String text, int offset) {
        int line = 1;
        int max = Math.min(offset, text.length());
        for (int i = 0; i < max; i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    private boolean isEnumRoot(RosettaExpression expr) {
        RosettaExpression root = findRootExpression(expr);
        if (root instanceof RosettaSymbolReference) {
            RosettaSymbol symbol = ((RosettaSymbolReference) root).getSymbol();
            if (symbol == null) {
                return false;
            }
            String className = symbol.getClass().getName();
            return className.contains("RosettaEnumeration") || className.contains("RosettaEnumValue");
        }
        return false;
    }

    private RosettaExpression findRootExpression(RosettaExpression expr) {
        RosettaExpression current = expr;
        while (true) {
            if (current instanceof RosettaFeatureCall) {
                RosettaExpression receiver = ((RosettaFeatureCall) current).getReceiver();
                if (receiver == null) {
                    return current;
                }
                current = receiver;
                continue;
            }
            if (current instanceof RosettaDeepFeatureCall) {
                RosettaExpression receiver = ((RosettaDeepFeatureCall) current).getReceiver();
                if (receiver == null) {
                    return current;
                }
                current = receiver;
                continue;
            }
            return current;
        }
    }

    private static class ParsedPath {
        private final String rootVariable;
        private final List<String> segments;

        private ParsedPath(String rootVariable, List<String> segments) {
            this.rootVariable = rootVariable;
            this.segments = segments;
        }
    }
}
