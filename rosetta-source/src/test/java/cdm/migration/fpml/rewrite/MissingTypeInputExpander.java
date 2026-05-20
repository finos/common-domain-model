package cdm.migration.fpml.rewrite;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cdm.migration.fpml.analysis.FunctionCallSite;
import cdm.migration.fpml.analysis.IngestAnalysisResult;
import cdm.migration.fpml.analysis.RosettaFunctionInfo;
import cdm.migration.fpml.analysis.RosettaPathExpression;
import cdm.migration.fpml.analysis.TypeResolver;
import cdm.migration.fpml.mapping.RemovedTypeClassification;
import cdm.migration.fpml.model.RosettaAttributeInfo;
import cdm.migration.fpml.model.RosettaModelInventory;
import cdm.migration.fpml.model.RosettaTypeInfo;

public class MissingTypeInputExpander {
    public final List<String> expansionReport = new ArrayList<String>();

    public List<TextEdit> plan(
            IngestAnalysisResult analysis,
            Set<String> reachableFunctions,
            RosettaModelInventory oldModel,
            RosettaModelInventory newModel,
            Map<String, RemovedTypeClassification> removedTypes,
            TypeResolver typeResolver) {
        List<TextEdit> edits = new ArrayList<TextEdit>();
        Map<String, ExpansionPlan> plans = new LinkedHashMap<String, ExpansionPlan>();

        for (RosettaFunctionInfo function : analysis.functions) {
            if (!reachableFunctions.contains(function.name)) {
                continue;
            }
            Map<String, String> aliases = analysis.importAliasesByFile.get(function.file);
            for (String inputName : function.inputOrder) {
                String inputTypeRef = function.inputTypes.get(inputName);
                String inputTypeQName = typeResolver.resolveTypeRef(inputTypeRef, oldModel, aliases);
                if (inputTypeQName == null) {
                    continue;
                }
                if (newModel.typeByQualifiedName.containsKey(inputTypeQName)) {
                    continue;
                }
                RemovedTypeClassification classification = removedTypes.get(inputTypeQName);
                if (classification != RemovedTypeClassification.LIKELY_WRAPPER && classification != RemovedTypeClassification.POSSIBLE_WRAPPER) {
                    continue;
                }
                RosettaTypeInfo oldType = oldModel.typeByQualifiedName.get(inputTypeQName);
                if (oldType == null) {
                    continue;
                }
                LinkedHashSet<String> usedSegments = collectFirstSegments(function, inputName);
                if (usedSegments.isEmpty()) {
                    continue;
                }

                List<ExpandedInput> expandedInputs = new ArrayList<ExpandedInput>();
                for (String segment : usedSegments) {
                    RosettaAttributeInfo attr = findAttribute(oldType, segment);
                    if (attr != null) {
                        expandedInputs.add(new ExpandedInput(segment, toInputType(inputTypeRef, attr.typeName), attr.cardinality.toString()));
                    }
                }
                if (expandedInputs.isEmpty()) {
                    continue;
                }
                ExpansionPlan plan = new ExpansionPlan(function, inputName, inputTypeRef, expandedInputs);
                plans.put(function.name + "::" + inputName, plan);
                edits.addAll(planFunctionEdits(function, plan));
                expansionReport.add(function.file + ":" + function.startLine + " [" + function.name + "] "
                        + inputName + " " + inputTypeRef + " expanded to " + expandedInputNames(expandedInputs));
            }
        }

        for (RosettaFunctionInfo function : analysis.functions) {
            if (!reachableFunctions.contains(function.name)) {
                continue;
            }
            for (FunctionCallSite callSite : function.callSites) {
                for (ExpansionPlan plan : plans.values()) {
                    if (!plan.functionName().equals(callSite.calledFunction)) {
                        continue;
                    }
                    int argIndex = plan.argumentIndex;
                    if (argIndex < 0 || argIndex >= callSite.arguments.size()) {
                        continue;
                    }
                    FunctionCallSite.CallArgument arg = callSite.arguments.get(argIndex);
                    String replacement = expandedArgs(arg.text, plan.expandedInputs);
                    edits.add(new TextEdit(
                            function.file,
                            arg.startOffset,
                            arg.endOffset,
                            replacement,
                            "expand missing wrapper-type call argument"));
                }
            }
        }

        return edits;
    }

    private List<TextEdit> planFunctionEdits(RosettaFunctionInfo function, ExpansionPlan plan) {
        List<TextEdit> edits = new ArrayList<TextEdit>();
        int index = function.inputOrder.indexOf(plan.oldInputName);
        plan.argumentIndex = index;

        InputLineRange inputLineRange = locateInputLine(function, plan.oldInputName, plan.oldInputTypeRef);
        if (inputLineRange != null) {
            String replacement = inputLineRange.indent + plan.expandedInputsAsLines(inputLineRange.indent);
            edits.add(new TextEdit(function.file, inputLineRange.startOffset, inputLineRange.endOffset, replacement,
                    "expand missing wrapper-type input"));
        }

        Set<String> segmentNames = new LinkedHashSet<String>();
        for (ExpandedInput expanded : plan.expandedInputs) {
            segmentNames.add(expanded.name);
        }
        for (RosettaPathExpression path : function.pathExpressions) {
            if (!plan.oldInputName.equals(path.rootVariable) || path.segments.isEmpty()) {
                continue;
            }
            String first = path.segments.get(0);
            if (!segmentNames.contains(first)) {
                continue;
            }
            String replacement = first;
            if (path.segments.size() > 1) {
                replacement = replacement + " -> " + String.join(" -> ", path.segments.subList(1, path.segments.size()));
            }
            edits.add(new TextEdit(function.file, path.startOffset, path.endOffset, replacement, "replace expanded wrapper input access"));
        }
        return edits;
    }

    private String expandedArgs(String arg, List<ExpandedInput> expandedInputs) {
        List<String> out = new ArrayList<String>();
        String trimmed = arg.trim();
        for (ExpandedInput expanded : expandedInputs) {
            if ("empty".equals(trimmed)) {
                out.add("empty");
            } else {
                out.add(trimmed + " -> " + expanded.name);
            }
        }
        return String.join(", ", out);
    }

    private String toInputType(String oldInputTypeRef, String attrTypeName) {
        if (attrTypeName.contains(".")) {
            return attrTypeName;
        }
        int idx = oldInputTypeRef == null ? -1 : oldInputTypeRef.indexOf('.');
        if (idx > 0) {
            String alias = oldInputTypeRef.substring(0, idx);
            return alias + "." + attrTypeName;
        }
        return attrTypeName;
    }

    private LinkedHashSet<String> collectFirstSegments(RosettaFunctionInfo function, String inputName) {
        LinkedHashSet<String> segments = new LinkedHashSet<String>();
        for (RosettaPathExpression path : function.pathExpressions) {
            if (inputName.equals(path.rootVariable) && !path.segments.isEmpty()) {
                segments.add(path.segments.get(0));
            }
        }
        return segments;
    }

    private RosettaAttributeInfo findAttribute(RosettaTypeInfo type, String attrName) {
        for (RosettaAttributeInfo attr : type.attributes) {
            if (attr.name.equals(attrName)) {
                return attr;
            }
        }
        return null;
    }

    private InputLineRange locateInputLine(RosettaFunctionInfo function, String inputName, String inputType) {
        Pattern pattern = Pattern.compile("(?m)^(\\s*)" + Pattern.quote(inputName) + "\\s+" + Pattern.quote(inputType)
                + "\\s*\\([^\\n]*\\)\\s*\\R?");
        Matcher matcher = pattern.matcher(function.body);
        if (matcher.find()) {
            return new InputLineRange(
                    function.startOffset + matcher.start(),
                    function.startOffset + matcher.end(),
                    matcher.group(1) == null ? "        " : matcher.group(1));
        }
        return null;
    }

    private String expandedInputNames(List<ExpandedInput> expandedInputs) {
        List<String> names = new ArrayList<String>();
        for (ExpandedInput in : expandedInputs) {
            names.add(in.name + ":" + in.type + "(" + in.cardinality + ")");
        }
        return String.join(", ", names);
    }

    private static class ExpandedInput {
        private final String name;
        private final String type;
        private final String cardinality;

        private ExpandedInput(String name, String type, String cardinality) {
            this.name = name;
            this.type = type;
            this.cardinality = cardinality;
        }
    }

    private static class ExpansionPlan {
        private final RosettaFunctionInfo function;
        private final String oldInputName;
        private final String oldInputTypeRef;
        private final List<ExpandedInput> expandedInputs;
        private int argumentIndex = -1;

        private ExpansionPlan(RosettaFunctionInfo function, String oldInputName, String oldInputTypeRef, List<ExpandedInput> expandedInputs) {
            this.function = function;
            this.oldInputName = oldInputName;
            this.oldInputTypeRef = oldInputTypeRef;
            this.expandedInputs = expandedInputs;
        }

        private String functionName() {
            return function.name;
        }

        private String expandedInputsAsLines(String indent) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < expandedInputs.size(); i++) {
                ExpandedInput in = expandedInputs.get(i);
                sb.append(in.name).append(" ").append(in.type).append(" (").append(in.cardinality).append(")");
                if (i + 1 < expandedInputs.size()) {
                    sb.append(System.lineSeparator()).append(indent);
                }
            }
            return sb.toString();
        }
    }

    private static class InputLineRange {
        private final int startOffset;
        private final int endOffset;
        private final String indent;

        private InputLineRange(int startOffset, int endOffset, String indent) {
            this.startOffset = startOffset;
            this.endOffset = endOffset;
            this.indent = indent;
        }
    }
}
