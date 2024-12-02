package org.finos.cdm.example.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.regnosys.rosetta.common.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import org.finos.cdm.example.AbstractExampleTest;

/**
 * Abstract base class for tests involving validation and qualification of Rosetta model objects.
 * This class provides utility methods for running validation and qualification steps
 * on input objects and leverages the CDM's framework for processing Rosetta model objects.
 */
public abstract class AbstractProcessorTest extends AbstractExampleTest {

    // Validator for running validation processes on Rosetta model objects.
    @Inject
    RosettaTypeValidator validator;

    // Processor for running qualification steps on Rosetta model objects.
    @Inject
    QualifyProcessorStep qualifyProcessor;

    // JSON mapper for serializing and deserializing Rosetta model objects.
    protected final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    /**
     * Validates a Rosetta model object by running the validation process step.
     *
     * @param input The Rosetta model object to be validated.
     * @param <T>   The type of the Rosetta model object.
     * @return A ValidationReport containing the results of the validation process.
     */
    protected <T extends RosettaModelObject> ValidationReport validate(T input) {
        // Run validation on the input object using its class type.
        return validator.runProcessStep(input.getClass(), input);
    }

    /**
     * Qualifies a Rosetta model object by running the qualification process step.
     *
     * @param input The Rosetta model object builder to be qualified.
     * @param <T>   The type of the Rosetta model object builder.
     * @return A QualificationReport containing the results of the qualification process.
     */
    protected <T extends RosettaModelObjectBuilder> QualificationReport qualify(T input) {
        // Run qualification on the input builder using its class type.
        return qualifyProcessor.runProcessStep(input.getClass(), input);
    }
}

