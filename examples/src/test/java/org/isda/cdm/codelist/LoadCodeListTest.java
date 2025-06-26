package org.isda.cdm.codelist;

import cdm.base.staticdata.codelist.CodeList;
import cdm.base.staticdata.codelist.functions.LoadCodeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for the {@link LoadCodeList} function which loads CDM CodeList JSON resources.
 *
 * <p>Test ensures that known domains like "business-center"
 * are properly resolved into valid {@link CodeList} objects and conform to schema validation.
 */
class LoadCodeListTest {
    private static final Logger logger = LoggerFactory.getLogger(LoadCodeListTest.class);

    // Dependency injection
    private static Injector injector;

    // Serialization
    private static final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    // Test Function
    @Inject
    LoadCodeList loadCodeListFunc;

    // CDM object Validator
    @Inject
    RosettaTypeValidator validator;

    /**
     * Initializes the Guice injector once for the entire test suite.
     */
    @BeforeAll
    static void setUpOnce() {
        injector = Guice.createInjector(new CdmRuntimeModule());
    }

    /**
     * Injects test dependencies before each test case.
     */
    @BeforeEach
    public void setUp() {
        injector.injectMembers(this);
    }

    /**
     * Test case to verify resolution and validation of the "business-center" code list domain.
     */
    @Test
    void mustResolveBusinessCenterCodeList() throws JsonProcessingException {
        shoudlResolveCodeListByDomainAsExpected("business-center");
    }

    /**
     * Test case to verify resolution and validation of the "floating-rate-index" code list domain.
     */
    @Test
    void mustResolveFloatingRateIndexCodeList() throws JsonProcessingException {
        shoudlResolveCodeListByDomainAsExpected("floating-rate-index");
    }

    /**
     * Helper method that evaluates the given code list domain, logs the result,
     * validates it, and asserts CDM conformance.
     *
     * @param domain the code list domain to evaluate (e.g., "business-center")
     */
    private void shoudlResolveCodeListByDomainAsExpected(String domain) throws JsonProcessingException {
        // Resolve code list by domain
        CodeList supportedCodes = loadCodeListFunc.evaluate(domain);
        logger.info("CDM CodeList for '{}' domain", domain);
        logger.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(supportedCodes));

        // Run validation
        ValidationReport report = validator.runProcessStep(CodeList.class, supportedCodes);
        report.logReport();

        // Assert schema validation success
        assertTrue(report.success(), "Must resolve a valid CodeList");
    }
}

