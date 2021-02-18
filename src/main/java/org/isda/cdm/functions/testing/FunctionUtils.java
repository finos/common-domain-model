package org.isda.cdm.functions.testing;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

public class FunctionUtils {

    public static <T> List<T> guard(List<T> list) {
        return Optional.ofNullable(list).orElse(Lists.newArrayList());
    }

}
