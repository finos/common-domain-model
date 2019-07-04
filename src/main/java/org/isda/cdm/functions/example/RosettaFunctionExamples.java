package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

public class RosettaFunctionExamples {

    private static final RosettaFunctionExamples instance = new RosettaFunctionExamples();

    private final ClassToInstanceMap<RosettaFunction> map;
    private final IdentifierService identifierService;

    private RosettaFunctionExamples() {
        map = MutableClassToInstanceMap.create();
        identifierService = new IdentifierService();

        new NewContractEventExample(map, identifierService);
        new EmptyLegalAgreementExample(map);
        new NewExecutionFromProductExample(map, identifierService);
        new NewContractFormationFromExecutionExample(map, identifierService);
        new EquityResetEventExample(map, identifierService);
    }

    public <T extends RosettaFunction> T get(Class<T> clazz) {
        return map.getInstance(clazz);
    }

    public IdentifierService getIdentifierService() {
        return identifierService;
    }

    public static RosettaFunctionExamples getInstance() {
        return instance;
    }
}
