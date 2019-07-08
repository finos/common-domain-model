package org.isda.cdm.functions.example;

import com.google.common.collect.ClassToInstanceMap;
import com.rosetta.model.lib.functions.RosettaFunction;
import org.isda.cdm.functions.GetBusinessDateSpec;

import java.time.LocalDate;

public class GetBusinessDateSpecExmaple extends GetBusinessDateSpec {

    private final LocalDate date;

    protected GetBusinessDateSpecExmaple(ClassToInstanceMap<RosettaFunction> classRegistry, LocalDate date) {
        super(classRegistry);
        this.date = date;
    }

    @Override
    protected LocalDate doEvaluate() {
        return date;
    }
}
