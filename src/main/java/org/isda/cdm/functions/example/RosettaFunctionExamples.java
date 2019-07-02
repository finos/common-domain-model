package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;

public class RosettaFunctionExamples {

    private static final ClassToInstanceMap<RosettaFunction> map = MutableClassToInstanceMap.create();

    public static ClassToInstanceMap<RosettaFunction> get() {
        new NewContractEventExample(map);
        new EmptyLegalAgreementExample(map);
        new NewExecutionFromProductExample(map);
        new NewContractFormationFromExecutionExample(map);
        return map;
    }
}
