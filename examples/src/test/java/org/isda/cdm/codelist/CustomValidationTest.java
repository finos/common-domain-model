package org.isda.cdm.codelist;

import cdm.base.datetime.BusinessCenterTime;
import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomValidationTest {

    @Inject
    ValidateFpMLCodingSchemeDomain function;
    @Inject
    RosettaTypeValidator validator;

    private static BusinessCenterTime testObject;

    private static Injector defaultInjector;
    private static Injector customInjector;
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

    @Test
    void mustErrorCodeListValidationWithDefaultBindings() {
        defaultInjector.injectMembers(this);
        ValidationReport report = validator.runProcessStep(testObject.getClass(), testObject);
        report.logReport();

        assertTrue(report.getValidationResults().stream()
                .filter(it -> it.getValidationType() == ValidationResult.ValidationType.TYPE_FORMAT)
                .map(ValidationResult::getFailureReason)
                .map(Optional::get)
                .anyMatch(Predicate.isEqual("TEST code not found in domain 'business-center'"))
        );
    }

    //Works with update/validatorinject branch development rune-dsl version
    //@Test
    void mustPassCodeListValidationWithCustomBindings() {
        customInjector.injectMembers(this);
        ValidationReport report = validator.runProcessStep(testObject.getClass(), testObject);
        report.logReport();

        assertTrue(report.getValidationResults().stream()
                .filter(it -> it.getValidationType() == ValidationResult.ValidationType.TYPE_FORMAT)
                .map(ValidationResult::getFailureReason)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .noneMatch(Predicate.isEqual("TEST code not found in domain 'business-center'"))
        );
    }
}
