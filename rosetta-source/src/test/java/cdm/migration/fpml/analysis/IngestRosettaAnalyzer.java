package cdm.migration.fpml.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.regnosys.rosetta.rosetta.RosettaModel;

import cdm.migration.fpml.source.RosettaSourceFile;
import cdm.migration.fpml.xtext.RosettaAstExtractor;
import cdm.migration.fpml.xtext.RosettaXtextLoader;

public class IngestRosettaAnalyzer {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^\\s*namespace\\s+([A-Za-z0-9_.]+)");
    private static final Pattern IMPORT_PATTERN = Pattern.compile("^\\s*import\\s+([A-Za-z0-9_.*]+)(?:\\s+as\\s+([A-Za-z0-9_]+))?");
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("(?m)^\\s*func\\s+([A-Za-z0-9_]+)\\s*:");
    private static final Pattern INPUT_LINE_PATTERN = Pattern.compile("^\\s*([A-Za-z_][A-Za-z0-9_]*)\\s+([A-Za-z_][A-Za-z0-9_.]*)\\s*\\([^)]*\\).*");
    private static final Pattern CALL_PATTERN = Pattern.compile("\\b([A-Z][A-Za-z0-9_]*)\\s*\\(");
    private static final Pattern SWITCH_CALL_PATTERN = Pattern.compile("\\bthen\\s+([A-Z][A-Za-z0-9_]*)\\b");
    private final RosettaXtextLoader xtextLoader;
    private final RosettaAstExtractor astExtractor;

    public IngestRosettaAnalyzer() {
        RosettaXtextLoader loader = null;
        RosettaAstExtractor extractor = null;
        try {
            loader = new RosettaXtextLoader();
            extractor = new RosettaAstExtractor();
        } catch (Throwable t) {
            loader = null;
            extractor = null;
        }
        this.xtextLoader = loader;
        this.astExtractor = extractor;
    }

    public IngestAnalysisResult analyze(List<RosettaSourceFile> files) {
        IngestAnalysisResult result = new IngestAnalysisResult();
        for (RosettaSourceFile file : files) {
            parseFile(file, result);
        }
        return result;
    }

    private void parseFile(RosettaSourceFile file, IngestAnalysisResult result) {
        String text = file.getContent();
        String namespace = parseNamespace(text);
        Map<String, String> aliases = parseImports(text);
        result.importAliasesByFile.put(file.getLogicalPath(), aliases);

        Matcher fnMatcher = FUNCTION_PATTERN.matcher(text);
        List<FunctionSlice> slices = new ArrayList<FunctionSlice>();
        while (fnMatcher.find()) {
            slices.add(new FunctionSlice(fnMatcher.group(1), fnMatcher.start()));
        }
        for (int i = 0; i < slices.size(); i++) {
            FunctionSlice slice = slices.get(i);
            int end = i + 1 < slices.size() ? slices.get(i + 1).start : text.length();
            String body = text.substring(slice.start, end);
            int line = lineOfOffset(text, slice.start);
            RosettaFunctionInfo functionInfo = new RosettaFunctionInfo(file.getLogicalPath(), namespace, slice.name, slice.start, end, line, body);
            parseInputs(functionInfo, text);
            parseCalls(functionInfo);
            parseCallSites(functionInfo, text);
            parsePathExpressions(functionInfo, text);
            result.functions.add(functionInfo);
            result.functionByName.put(functionInfo.name, functionInfo);
        }
        enrichFromAstIfAvailable(file, result);
    }

    private void enrichFromAstIfAvailable(RosettaSourceFile file, IngestAnalysisResult result) {
        if (xtextLoader == null || astExtractor == null) {
            return;
        }
        try {
            RosettaModel model = xtextLoader.loadModel(java.nio.file.Paths.get(file.getLogicalPath()));
            astExtractor.enrichIngestResult(model, file, result);
        } catch (Throwable ignored) {
            // Text fallback remains active.
        }
    }

    private String parseNamespace(String text) {
        String[] lines = text.split("\\R");
        for (String line : lines) {
            Matcher matcher = NAMESPACE_PATTERN.matcher(line);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return "";
    }

    private Map<String, String> parseImports(String text) {
        Map<String, String> aliases = new LinkedHashMap<String, String>();
        String[] lines = text.split("\\R");
        for (String line : lines) {
            Matcher matcher = IMPORT_PATTERN.matcher(line);
            if (matcher.find()) {
                String ns = matcher.group(1);
                String alias = matcher.group(2);
                if (alias != null) {
                    aliases.put(alias + ":" + ns, ns);
                }
            }
        }
        return aliases;
    }

    private void parseInputs(RosettaFunctionInfo functionInfo, String fullText) {
        String[] lines = functionInfo.body.split("\\R", -1);
        boolean inInputs = false;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmed = line.trim();
            if (trimmed.startsWith("inputs:")) {
                inInputs = true;
                continue;
            }
            if (inInputs && (trimmed.startsWith("output:") || trimmed.startsWith("set ") || trimmed.startsWith("alias ")
                    || trimmed.startsWith("condition ") || trimmed.startsWith("annotation ") || trimmed.startsWith("func "))) {
                inInputs = false;
            }
            if (inInputs) {
                Matcher matcher = INPUT_LINE_PATTERN.matcher(line);
                if (matcher.find()) {
                    String var = matcher.group(1);
                    String type = matcher.group(2);
                    functionInfo.inputTypes.put(var, type);
                    functionInfo.inputOrder.add(var);
                    int absoluteOffset = functionInfo.startOffset + offsetInString(functionInfo.body, line, i);
                    functionInfo.inputLines.put(var, lineOfOffset(fullText, absoluteOffset));
                }
            }
        }
    }

    private int offsetInString(String text, String targetLine, int lineNumber) {
        int offset = 0;
        int currentLine = 0;
        while (currentLine < lineNumber && offset < text.length()) {
            char c = text.charAt(offset++);
            if (c == '\n') {
                currentLine++;
            }
        }
        return offset;
    }

    private void parseCalls(RosettaFunctionInfo functionInfo) {
        Matcher matcher = CALL_PATTERN.matcher(functionInfo.body);
        Set<String> dedup = new LinkedHashSet<String>();
        while (matcher.find()) {
            String call = matcher.group(1);
            if (!call.equals(functionInfo.name) && !"if".equals(call) && !"then".equals(call) && !"switch".equals(call)) {
                dedup.add(call);
            }
        }
        Matcher switchMatcher = SWITCH_CALL_PATTERN.matcher(functionInfo.body);
        while (switchMatcher.find()) {
            String call = switchMatcher.group(1);
            if (!call.equals(functionInfo.name) && !"empty".equals(call)) {
                dedup.add(call);
            }
        }
        functionInfo.calledFunctions.addAll(dedup);
    }

    private void parseCallSites(RosettaFunctionInfo functionInfo, String fullText) {
        String body = functionInfo.body;
        int i = 0;
        while (i < body.length()) {
            char c = body.charAt(i);
            if (!isIdentifierStart(c) || !Character.isUpperCase(c)) {
                i++;
                continue;
            }
            String token = readToken(body, i);
            int afterToken = i + token.length();
            int cursor = skipWs(body, afterToken);
            if (cursor >= body.length() || body.charAt(cursor) != '(') {
                i = afterToken;
                continue;
            }
            int close = findMatchingParen(body, cursor);
            if (close < 0) {
                i = afterToken;
                continue;
            }
            int absStart = functionInfo.startOffset + i;
            int absEnd = functionInfo.startOffset + close + 1;
            FunctionCallSite callSite = new FunctionCallSite(functionInfo.file, functionInfo.name, token, absStart, absEnd);
            parseArguments(body, functionInfo.startOffset, cursor + 1, close, callSite);
            functionInfo.callSites.add(callSite);
            i = close + 1;
        }
    }

    private void parseArguments(String body, int baseOffset, int start, int end, FunctionCallSite callSite) {
        int depth = 0;
        boolean inString = false;
        int argStart = start;
        for (int i = start; i < end; i++) {
            char c = body.charAt(i);
            if (c == '"' && (i == 0 || body.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            if (inString) {
                continue;
            }
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            } else if (c == ',' && depth == 0) {
                addArgument(body, baseOffset, argStart, i, callSite);
                argStart = i + 1;
            }
        }
        if (argStart <= end) {
            addArgument(body, baseOffset, argStart, end, callSite);
        }
    }

    private void addArgument(String body, int baseOffset, int rawStart, int rawEnd, FunctionCallSite callSite) {
        int start = rawStart;
        int end = rawEnd;
        while (start < end && Character.isWhitespace(body.charAt(start))) {
            start++;
        }
        while (end > start && Character.isWhitespace(body.charAt(end - 1))) {
            end--;
        }
        String text = body.substring(start, end);
        callSite.arguments.add(new FunctionCallSite.CallArgument(text, baseOffset + start, baseOffset + end));
    }

    private int findMatchingParen(String s, int openPos) {
        int depth = 0;
        boolean inString = false;
        for (int i = openPos; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' && (i == 0 || s.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            if (inString) {
                continue;
            }
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void parsePathExpressions(RosettaFunctionInfo functionInfo, String fullText) {
        String body = functionInfo.body;
        int i = 0;
        boolean inString = false;
        boolean inLineComment = false;
        while (i < body.length()) {
            char c = body.charAt(i);
            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                }
                i++;
                continue;
            }
            if (c == '"' && (i == 0 || body.charAt(i - 1) != '\\')) {
                inString = !inString;
                i++;
                continue;
            }
            if (!inString && c == '/' && i + 1 < body.length() && body.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }
            if (!inString && isIdentifierStart(c)) {
                int tokenStart = i;
                String rootToken = readToken(body, i);
                int next = tokenStart + rootToken.length();
                int cursor = skipWs(body, next);
                if (cursor + 1 < body.length() && body.charAt(cursor) == '-' && body.charAt(cursor + 1) == '>') {
                    List<String> segments = new ArrayList<String>();
                    int end = cursor;
                    while (true) {
                        cursor = cursor + 2;
                        cursor = skipWs(body, cursor);
                        if (cursor >= body.length() || !isIdentifierStart(body.charAt(cursor))) {
                            break;
                        }
                        String seg = readToken(body, cursor);
                        segments.add(seg);
                        cursor += seg.length();
                        end = cursor;
                        int ws = skipWs(body, cursor);
                        if (ws + 1 >= body.length() || body.charAt(ws) != '-' || body.charAt(ws + 1) != '>') {
                            break;
                        }
                        end = ws + 2;
                        cursor = ws;
                    }
                    if (!segments.isEmpty()) {
                        int absStart = functionInfo.startOffset + tokenStart;
                        int absEnd = functionInfo.startOffset + end;
                        int line = lineOfOffset(fullText, absStart);
                        String expression = body.substring(tokenStart, end).trim();
                        functionInfo.pathExpressions.add(new RosettaPathExpression(
                                functionInfo.file,
                                functionInfo.name,
                                absStart,
                                absEnd,
                                line,
                                expression,
                                rootToken,
                                segments));
                        i = end;
                        continue;
                    }
                }
            }
            i++;
        }
    }

    private int skipWs(String s, int i) {
        int cursor = i;
        while (cursor < s.length() && Character.isWhitespace(s.charAt(cursor))) {
            cursor++;
        }
        return cursor;
    }

    private boolean isIdentifierStart(char c) {
        return Character.isLetter(c) || c == '_' || c == '^';
    }

    private String readToken(String s, int i) {
        int j = i;
        while (j < s.length()) {
            char c = s.charAt(j);
            if (Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '^') {
                j++;
            } else {
                break;
            }
        }
        return s.substring(i, j);
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

    private static class FunctionSlice {
        private final String name;
        private final int start;

        private FunctionSlice(String name, int start) {
            this.name = name;
            this.start = start;
        }
    }
}
