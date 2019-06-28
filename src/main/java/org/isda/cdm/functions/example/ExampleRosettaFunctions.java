package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;

public class ExampleRosettaFunctions {

    private static final ClassToInstanceMap<RosettaFunction> map = MutableClassToInstanceMap.create();

    public static ClassToInstanceMap<RosettaFunction> get() {
        new ExampleNewContractEvent(map);
        new ExampleEmptyLegalAgreement(map);
        return map;
    }
}
