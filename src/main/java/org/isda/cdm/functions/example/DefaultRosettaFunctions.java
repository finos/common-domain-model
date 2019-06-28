package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.functions.DefaultNewContractEvent;

public class DefaultRosettaFunctions {

    private static final ClassToInstanceMap<RosettaFunction> map = MutableClassToInstanceMap.create();

    public static ClassToInstanceMap<RosettaFunction> get() {
        new DefaultNewContractEvent(map);
        return map;
    }
}
