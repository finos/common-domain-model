package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.Contract;
import org.isda.cdm.Event;
import org.isda.cdm.Identifier;
import org.isda.cdm.functions.EquityResetEvent;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

public class EquityResetEventExample extends EquityResetEvent {

    private final IdentifierService identifierService;

    EquityResetEventExample(ClassToInstanceMap<RosettaFunction> classRegistry, IdentifierService identifierService) {
        super(classRegistry);
        this.identifierService = identifierService;
    }

    @Override
    protected Event doEvaluate(Contract contract, Event observation) {
        Identifier id = identifierService.nextVersion(contract.getContractIdentifier().get(0).getIssuer().getValue(), Event.class.getSimpleName());

        return Event.builder()
                .addEventIdentifier(id)
                .build();
    }
}
