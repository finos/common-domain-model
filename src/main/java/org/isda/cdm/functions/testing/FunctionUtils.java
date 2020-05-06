package org.isda.cdm.functions.testing;

import com.google.common.collect.Lists;
import org.isda.cdm.Lineage;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.metafields.ReferenceWithMetaWorkflowStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionUtils {

    public static <T> List<T> guard(List<T> list) {
        return Optional.ofNullable(list).orElse(Lists.newArrayList());
    }

}
