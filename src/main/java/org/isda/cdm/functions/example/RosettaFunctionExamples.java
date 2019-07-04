package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

public class RosettaFunctionExamples {

    private static final ClassToInstanceMap<RosettaFunction> map = MutableClassToInstanceMap.create();

    public static ClassToInstanceMap<RosettaFunction> get() {
        IdentifierService identifierService = new IdentifierService();

        new NewContractEventExample(map, identifierService);
        new EmptyLegalAgreementExample(map);
        new NewExecutionFromProductExample(map, identifierService);
        new NewContractFormationFromExecutionExample(map, identifierService);

        return map;
    }
}
