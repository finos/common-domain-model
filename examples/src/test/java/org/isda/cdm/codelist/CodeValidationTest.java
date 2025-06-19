package org.isda.cdm.codelist;

import cdm.base.datetime.BusinessCenterTime;
import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.finos.cdm.CdmRuntimeModule;
import org.isda.cdm.codelist.impl.CustomCodelistRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validation tests for FpML coding scheme using {@link ValidateFpMLCodingSchemeDomain}.
 *
 * <p>This test class verifies how code list validation behaves under default and custom Guice module bindings.
 * It specifically targets the validation of a {@link BusinessCenterTime} instance using a test code ("TEST").
 *
 * <ul>
 *     <li>With default bindings, the code "TEST" is not part of the standard CDM code list, so validation must fail.</li>
 *     <li>With custom bindings (overridden via {@link CustomCodelistRuntimeModule}), the code is assumed valid and must pass validation.</li>
 * </ul>
 */
class CodeValidationTest {

    //Test Function
    @Inject
    ValidateFpMLCodingSchemeDomain function;

    // CDM Object Validator
    @Inject
    RosettaTypeValidator validator;

    //Test Object
    private static BusinessCenterTime testObject;
    // Dependency injection
    private static Injector defaultInjector;
    private static Injector customInjector;

    /**
     * Initializes the test object and two Guice injectors:
     * - The default injector uses {@link CdmRuntimeModule}, which binds the standard CDM code list.
     * - The custom injector overrides the default with {@link CustomCodelistRuntimeModule}, enabling test-specific code list behavior.
     */
    @BeforeAll
    static void setUpOnce() {
        defaultInjector = Guice.createInjector(new CdmRuntimeModule());
        customInjector = Guice.createInjector(Modules.override(new CdmRuntimeModule()).with(new CustomCodelistRuntimeModule()));

        testObject = BusinessCenterTime.builder()
                .setBusinessCenter(FieldWithMetaString.builder()
                        .setValue("TEST")
                        .build())
                .build();
    }

    /**
     * Asserts that validation fails under default module bindings.
     * <p>Since "TEST" is not a valid business center code in the standard CDM code list,
     * the validation report must contain a data rule failure.</p>
     */
    @Test
    void mustErrorCodeListValidationWithDefaultBindings() {
        defaultInjector.injectMembers(this);
        ValidationReport report = validator.runProcessStep(testObject.getClass(), testObject);
        report.logReport();

        assertTrue(report.getValidationResults().stream()
                .filter(it -> it.getValidationType() == ValidationResult.ValidationType.DATA_RULE)
                .map(ValidationResult::getFailureReason)
                .map(Optional::get)
                .anyMatch(Predicate.isEqual("Condition has failed."))
        );
    }

    /**
     * Asserts that validation passes under custom module bindings.
     * <p>Using {@link CustomCodelistRuntimeModule}, the code "TEST" is injected as a valid business center code,
     * and no validation failure should occur.</p>
     */
    @Test
    void mustPassCodeListValidationWithCustomBindings() {
        customInjector.injectMembers(this);
        ValidationReport report = validator.runProcessStep(testObject.getClass(), testObject);
        report.logReport();

        assertTrue(report.getValidationResults().stream()
                .filter(it -> it.getValidationType() == ValidationResult.ValidationType.DATA_RULE)
                .map(ValidationResult::getFailureReason)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .noneMatch(Predicate.isEqual("Conditions has failed."))
        );
    }
}

