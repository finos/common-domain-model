package cdm.migration.fpml.analysis;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CallGraphBuilder {
    public Map<String, Set<String>> build(IngestAnalysisResult analysis) {
        Map<String, Set<String>> graph = new LinkedHashMap<String, Set<String>>();
        for (RosettaFunctionInfo function : analysis.functions) {
            Set<String> edges = new LinkedHashSet<String>();
            for (String call : function.calledFunctions) {
                if (analysis.functionByName.containsKey(call)) {
                    edges.add(call);
                }
            }
            graph.put(function.name, edges);
        }
        return graph;
    }
}
