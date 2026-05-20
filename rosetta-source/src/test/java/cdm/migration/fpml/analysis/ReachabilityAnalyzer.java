package cdm.migration.fpml.analysis;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ReachabilityAnalyzer {
    public Set<String> reachableFunctions(Map<String, Set<String>> callGraph, List<String> entries) {
        Set<String> reachable = new LinkedHashSet<String>();
        Queue<String> queue = new ArrayDeque<String>();
        for (String entry : entries) {
            if (callGraph.containsKey(entry)) {
                queue.add(entry);
            }
        }
        while (!queue.isEmpty()) {
            String current = queue.remove();
            if (!reachable.add(current)) {
                continue;
            }
            Set<String> edges = callGraph.get(current);
            if (edges != null) {
                for (String next : edges) {
                    if (!reachable.contains(next)) {
                        queue.add(next);
                    }
                }
            }
        }
        return reachable;
    }
}
