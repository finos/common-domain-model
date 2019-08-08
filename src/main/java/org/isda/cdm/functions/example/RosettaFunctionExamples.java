package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.RosettaKeyProcessStep;
import com.rosetta.model.lib.functions.RosettaFunction;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.processor.EventEffectProcessStep;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class RosettaFunctionExamples {

    private static final RosettaFunctionExamples instance = new RosettaFunctionExamples();

    private final ClassToInstanceMap<RosettaFunction> map;
    private final IdentifierService identifierService;
    private final LocalDate businessDate;

    private RosettaFunctionExamples() {
        map = MutableClassToInstanceMap.create();
        identifierService = new IdentifierService();
        businessDate = LocalDate.of(2001, 10, 10);

        new NewContractEventExample(map, identifierService);
        new EmptyLegalAgreementExample(map);
        new NewExecutionFromProductExample(map, identifierService);
        new NewContractFormationFromExecutionExample(map, identifierService);
        new GetBusinessDateSpecExmaple(map, businessDate);
        new ExtractSingleExecutionStateExample(map);
    }

    public <T extends RosettaFunction> T get(Class<T> clazz) {
        return map.getInstance(clazz);
    }

    public IdentifierService getIdentifierService() {
        return identifierService;
    }

    public List<PostProcessStep> getPostProcessor() {
        RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
        return Arrays.asList(rosettaKeyProcessStep, new EventEffectProcessStep(rosettaKeyProcessStep));
    }

    public static RosettaFunctionExamples getInstance() {
        return instance;
    }
}
