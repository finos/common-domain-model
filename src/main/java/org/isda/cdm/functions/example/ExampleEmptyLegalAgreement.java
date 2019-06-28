package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.LegalAgreement;
import org.isda.cdm.functions.EmptyLegalAgreement;

public class ExampleEmptyLegalAgreement extends EmptyLegalAgreement {

    protected ExampleEmptyLegalAgreement(ClassToInstanceMap<RosettaFunction> classRegistry) {
        super(classRegistry);
    }

    @Override
    protected LegalAgreement doEvaluate() {
        return null;
    }
}
