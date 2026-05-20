package cdm.migration.fpml.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RosettaFunctionInfo {
    public final String file;
    public final String namespace;
    public final String name;
    public final int startOffset;
    public final int endOffset;
    public final int startLine;
    public final String body;
    public final Map<String, String> inputTypes = new HashMap<String, String>();
    public final Map<String, Integer> inputLines = new HashMap<String, Integer>();
    public final List<String> inputOrder = new ArrayList<String>();
    public String outputName;
    public String outputType;
    public final Map<String, String> aliasExpressions = new HashMap<String, String>();
    public final Map<String, String> aliasTypes = new HashMap<String, String>();
    public final List<String> calledFunctions = new ArrayList<String>();
    public final List<FunctionCallSite> callSites = new ArrayList<FunctionCallSite>();
    public final List<RosettaPathExpression> pathExpressions = new ArrayList<RosettaPathExpression>();

    public RosettaFunctionInfo(String file, String namespace, String name, int startOffset, int endOffset, int startLine, String body) {
        this.file = file;
        this.namespace = namespace;
        this.name = name;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.startLine = startLine;
        this.body = body;
    }
}
