package cdm.migration.fpml.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngestAnalysisResult {
    public final List<RosettaFunctionInfo> functions = new ArrayList<RosettaFunctionInfo>();
    public final Map<String, RosettaFunctionInfo> functionByName = new HashMap<String, RosettaFunctionInfo>();
    public final Map<String, Map<String, String>> importAliasesByFile = new HashMap<String, Map<String, String>>();
}
