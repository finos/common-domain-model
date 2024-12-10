package org.finos.cdm.example.processors;

import cdm.event.workflow.functions.Create_AcceptedWorkflowStepFromInstruction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.regnosys.rosetta.common.hashing.ReferenceResolverProcessStep;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.regnosys.rosetta.common.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import org.finos.cdm.example.AbstractExampleTest;
import org.isda.cdm.processor.CdmReferenceConfig;

/**
 * Abstract base class for tests that involve validation, qualification, and processing
 * of Rosetta model objects in the Common Domain Model (CDM) framework.
 *
 * This class provides reusable utility methods to:
 * - Validate Rosetta model objects against defined CDM schemas and validation rules.
 * - Qualify Rosetta model object builders to assign them semantic categories.
 * - Resolve references within Rosetta model objects to ensure internal consistency.
 *
 * The utilities encapsulate core CDM processes, enabling subclasses to focus on
 * test logic while leveraging the framework's validation and qualification features.
 */
public abstract class AbstractProcessorTest extends AbstractExampleTest {

    // Dependency for validating Rosetta model objects against defined rules.
    @Inject
    RosettaTypeValidator validator;

    // Dependency for qualifying Rosetta model object builders.
    @Inject
    QualifyProcessorStep qualifyProcessor;

    // Dependency for creating workflow steps based on accepted instructions.
    @Inject
    protected Create_AcceptedWorkflowStepFromInstruction createAcceptedWorkflowStepFromInstructionFunc;

    // JSON ObjectMapper configured for Rosetta model serialization and deserialization.
    protected final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    /**
     * Validates a Rosetta model object by applying CDM validation rules.
     *
     * This method runs validation processes defined in the CDM framework to check
     * the correctness and consistency of the given object against its schema.
     *
     * @param input The Rosetta model object to be validated.
     * @param <T>   The type of the Rosetta model object.
     * @return A ValidationReport containing validation results, including errors
     *         and warnings, if any.
     */
    protected <T extends RosettaModelObject> ValidationReport validate(T input) {
        // Run validation on the input object using its type.
        return validator.runProcessStep(input.getClass(), input);
    }

    /**
     * Qualifies a Rosetta model object builder to determine its semantic role in the model.
     *
     * The qualification process assigns a semantic category to the object based on
     * predefined criteria, aiding in understanding the model's structure and intent.
     *
     * @param input The Rosetta model object builder to be qualified.
     * @param <T>   The type of the Rosetta model object builder.
     * @return A QualificationReport containing qualification results, including
     *         matched categories or errors.
     */
    protected <T extends RosettaModelObjectBuilder> QualificationReport qualify(T input) {
        // Run qualification on the input builder using its type.
        return qualifyProcessor.runProcessStep(input.getClass(), input);
    }

    /**
     * Resolves references within a Rosetta model object to ensure consistency and completeness.
     *
     * The resolution process ensures that all references within the object are valid and
     * points to the correct entities or objects in the model. This step is necessary
     * before validation or qualification in cases where objects have unresolved references.
     *
     * @param object The Rosetta model object with references to be resolved.
     * @param <T>    The type of the Rosetta model object.
     * @return A new instance of the Rosetta model object with resolved references.
     */
    protected static <T extends RosettaModelObject> T resolveReferences(T object) {
        // Convert the object to its builder representation for modification.
        RosettaModelObject builder = object.toBuilder();
        // Resolve references using the CDM reference resolution configuration.
        new ReferenceResolverProcessStep(CdmReferenceConfig.get()).runProcessStep(builder.getType(), builder);
        // Build and return the updated object with resolved references.
        return (T) builder.build();
    }
}


